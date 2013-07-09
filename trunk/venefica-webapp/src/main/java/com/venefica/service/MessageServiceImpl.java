package com.venefica.service;

import com.venefica.dao.CommentDao;
import com.venefica.dao.MessageDao;
import com.venefica.model.Ad;
import com.venefica.model.Comment;
import com.venefica.model.Message;
import com.venefica.model.User;
import com.venefica.service.dto.CommentDto;
import com.venefica.service.dto.MessageDto;
import com.venefica.service.dto.builder.MessageDtoBuilder;
import com.venefica.service.fault.AdNotFoundException;
import com.venefica.service.fault.AuthorizationException;
import com.venefica.service.fault.CommentField;
import com.venefica.service.fault.CommentNotFoundException;
import com.venefica.service.fault.CommentValidationException;
import com.venefica.service.fault.MessageField;
import com.venefica.service.fault.MessageNotFoundException;
import com.venefica.service.fault.MessageValidationException;
import com.venefica.service.fault.UserNotFoundException;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import javax.inject.Inject;
import javax.jws.WebService;
import org.springframework.social.facebook.api.Facebook;
import org.springframework.social.twitter.api.Twitter;
import org.springframework.social.vkontakte.api.VKontakte;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementation of {@link MessageService} interface.
 *
 * @author Sviatoslav Grebenchukov
 */
@Service("messageService")
@WebService(endpointInterface = "com.venefica.service.MessageService")
public class MessageServiceImpl extends AbstractService implements MessageService {

    @Inject
    private CommentDao commentDao;
    @Inject
    private MessageDao messageDao;
    
    //**************
    //* commenting *
    //**************
    
    @Override
    @Transactional
    public Long addCommentToAd(Long adId, CommentDto commentDto) throws AdNotFoundException, CommentValidationException {
        validateCommentDto(commentDto);
        Ad ad = validateAd(adId);
        
        Comment comment = new Comment(ad, getCurrentUser(), commentDto.getText());
        commentDao.save(comment);

        // Attach the comment to the ad
        ad.getComments().add(comment);
        return comment.getId();
    }

    @Override
    @Transactional
    public void updateComment(CommentDto commentDto) throws CommentNotFoundException, CommentValidationException {
        validateCommentDto(commentDto);
        Comment comment = validateComment(commentDto.getId());

        // WARNING! This update must be performed within an active transaction!
        commentDto.update(comment);
        comment.setUpdatedAt(new Date());
    }

    @Override
    @Transactional
    public List<CommentDto> getCommentsByAd(Long adId, Long lastCommentId, int numComments) throws AdNotFoundException {
        Ad ad = validateAd(adId);
        List<CommentDto> result = new LinkedList<CommentDto>();

        User currentUser = getCurrentUser();
        List<Comment> comments = commentDao.getAdComments(adId, lastCommentId, numComments);

        for (Comment comment : comments) {
            CommentDto commentDto = new CommentDto(comment, currentUser);
            result.add(commentDto);
        }
        return result;
    }
    
    
    
    //*************
    //* messaging *
    //*************

    @Override
    public List<MessageDto> getMessagesByAd(Long adId) throws AdNotFoundException {
        Ad ad = validateAd(adId);
        List<Message> messages = messageDao.getByAd(adId);
        return buildMessages(messages);
    }
    
    @Override
    public List<MessageDto> getMessagesByUsers(Long user1Id, Long user2Id) throws UserNotFoundException {
        User user1 = validateUser(user1Id);
        User user2 = validateUser(user2Id);
        List<Message> messages = messageDao.getByUsers(user1Id, user2Id);
        return buildMessages(messages);
    }
    
    @Override
    public List<MessageDto> getMessagesByAdAndUsers(Long adId, Long user1Id, Long user2Id) throws AdNotFoundException, UserNotFoundException {
        Ad ad = validateAd(adId);
        User user1 = validateUser(user1Id);
        User user2 = validateUser(user2Id);
        List<Message> messages = messageDao.getByAdAndUsers(adId, user1Id, user2Id);
        return buildMessages(messages);
    }
    
    @Override
    @Transactional
    public Long sendMessage(MessageDto messageDto) throws UserNotFoundException, AdNotFoundException, MessageValidationException {
        validateMessageDto(messageDto);
        
        User currentUser = getCurrentUser();
        
        Ad ad = null;
        if ( messageDto.getAdId() != null ) {
            ad = validateAd(messageDto.getAdId());
        }
        
        User to = null;
        if ( messageDto.getToName() != null ) {
            to = validateUser(messageDto.getToName());
        } else if ( messageDto.getToId() != null ) {
            to = validateUser(messageDto.getToId());
        } else if ( ad != null ) {
            to = ad.getCreator();
        }

        if ( to == null ) {
            throw new UserNotFoundException("Could not detect the receipient (to) user.");
        }
        
        if (currentUser.getName().equals(to.getName())) {
            throw new MessageValidationException(MessageField.TO, "You can't send messages to yourself!");
        }

        Message message = new Message(messageDto.getText());
        message.setTo(to);
        message.setFrom(currentUser);
        message.setAd(ad);

        return messageDao.save(message);
    }

    @Override
    @Transactional
    public void updateMessage(MessageDto messageDto) throws MessageNotFoundException, AuthorizationException, MessageValidationException {
        validateMessageDto(messageDto);
        Message message = validateMessage(messageDto.getId());
        User currentUser = getCurrentUser();

        if (!message.getFrom().equals(currentUser)) {
            throw new AuthorizationException("Only owner can update the message!");
        }

        // Only text can be updated!
        message.setText(messageDto.getText());
        message.setUpdatedAt(new Date());
    }

    @Override
    @Transactional
    public List<MessageDto> getAllMessages() {
        User currentUser = getCurrentUser();
        LinkedList<MessageDto> result = new LinkedList<MessageDto>();

        // incoming
        for (Message msg : currentUser.getReceivedMessages()) {
            if (msg.isDeleted() || msg.isHiddenByRecipient()) {
                continue;
            }

            MessageDto messageDto = new MessageDtoBuilder(msg)
                    .setCurrentUser(currentUser)
                    .build();
            msg.setRead(true); // mark as read
            
            result.add(messageDto);
        }

        // outgoing
        for (Message msg : currentUser.getSentMessages()) {
            if (msg.isDeleted() || msg.isHiddenBySender()) {
                continue;
            }

            MessageDto messageDto = new MessageDtoBuilder(msg)
                    .setCurrentUser(currentUser)
                    .build();
            result.add(messageDto);
        }

        return result;
    }

    @Override
    @Transactional
    public void hideMessage(Long messageId) throws MessageNotFoundException, AuthorizationException {
        Message message = validateMessage(messageId);
        User currentUser = getCurrentUser();

        if (message.getTo().equals(currentUser)) {
            message.setHiddenByRecipient(true);
        } else if (message.getFrom().equals(currentUser)) {
            message.setHiddenBySender(true);
        } else {
            throw new AuthorizationException("You are neither recipient nor sender to hide the message!");
        }
    }
    
    @Override
    @Transactional
    public void deleteMessage(Long messageId) throws MessageNotFoundException, AuthorizationException {
        Message message = validateMessage(messageId);
        User currentUser = getCurrentUser();

        if (!message.getTo().equals(currentUser) && !message.getFrom().equals(currentUser)) {
            throw new AuthorizationException("You are neither recipient nor sender of the message (messageId: " + messageId + ")!");
        }

        messageDao.deleteMessage(message);
    }
    
    
    
    //*********
    //* share *
    //*********

    @Override
    public void shareOnSocialNetworks(String message) {
        // Facebook
        Facebook facebook = getSocialNetworkApi(Facebook.class);
        if (facebook != null) {
            facebook.feedOperations().updateStatus(message);
        }

        // Twitter
        Twitter twitter = getSocialNetworkApi(Twitter.class);
        if (twitter != null) {
            twitter.timelineOperations().updateStatus(message);
        }

        // VKontakte
        VKontakte vkontakte = getSocialNetworkApi(VKontakte.class);
        if (vkontakte != null) {
            vkontakte.wallOperations().post(message);
        }
    }
    
    // internal helpers
    
    private Message validateMessage(Long messageId) throws MessageNotFoundException {
        Message message = messageDao.get(messageId);
        if (message == null) {
            throw new MessageNotFoundException(messageId);
        }
        return message;
    }
    
    private Comment validateComment(Long commentId) throws CommentNotFoundException {
        Comment comment = commentDao.get(commentId);
        if (comment == null) {
            throw new CommentNotFoundException(commentId);
        }
        return comment;
    }
    
    
    
    private void validateCommentDto(CommentDto commentDto) throws CommentValidationException {
        if (commentDto == null) {
            throw new NullPointerException("commentDto is null!");
        }
        
        // ++ TODO: create comment validator
        if (commentDto.getText() == null) {
            throw new CommentValidationException(CommentField.TEXT, "Text field not specified!");
        }
        // ++
    }
    
    private void validateMessageDto(MessageDto messageDto) throws MessageValidationException {
        if (messageDto == null) {
            throw new NullPointerException("message is null!");
        }

        if (messageDto.getText() == null) {
            throw new MessageValidationException(MessageField.TEXT, "Text field not specified!");
        }
    }
    
    
    
    private List<MessageDto> buildMessages(List<Message> messages) {
        List<MessageDto> result = new LinkedList<MessageDto>();
        User currentUser = getCurrentUser();
        
        for (Message message : messages) {
            if ( message.isHiddenByRecipient() || message.isHiddenBySender() ) {
                continue;
            }
            
            MessageDto messageDto = new MessageDtoBuilder(message)
                    .setCurrentUser(currentUser)
                    .build();
            message.setRead(true); // mark as read
            
            result.add(messageDto);
        }
        return result;
    }
}
