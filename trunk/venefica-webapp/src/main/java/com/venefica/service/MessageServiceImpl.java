package com.venefica.service;

import com.venefica.dao.CommentDao;
import com.venefica.dao.MessageDao;
import com.venefica.model.Ad;
import com.venefica.model.Comment;
import com.venefica.model.Message;
import com.venefica.model.NotificationType;
import com.venefica.model.Request;
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
import com.venefica.service.fault.RequestNotFoundException;
import com.venefica.service.fault.UserNotFoundException;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import javax.inject.Inject;
import javax.jws.WebService;
import org.springframework.social.facebook.api.Facebook;
import org.springframework.social.twitter.api.Twitter;
//import org.springframework.social.vkontakte.api.VKontakte;
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
        User creator = ad.getCreator();
        User currentUser = getCurrentUser();
        
        Comment comment = new Comment(ad, currentUser, commentDto.getText());
        Long commentId = commentDao.save(comment);

        // Attach the comment to the ad
        ad.getComments().add(comment);
        
        if ( !creator.equals(currentUser) ) {
            Map<String, Object> vars = new HashMap<String, Object>(0);
            vars.put("ad", ad);
            vars.put("creator", creator);
            vars.put("comment", comment);

            emailSender.sendNotification(NotificationType.AD_COMMENTED, creator, vars);
        }
        
        return commentId;
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
    @Transactional
    public List<MessageDto> getLastMessagePerRequest() {
        List<Message> messages = messageDao.getLastMessagePerRequestByUser(getCurrentUserId());
        return buildMessages(messages, getCurrentUser(), false);
    }
    
//    @Override
//    public List<MessageDto> getMessagesByAd(Long adId) throws AdNotFoundException {
//        Ad ad = validateAd(adId);
//        List<Message> messages = messageDao.getByAd(adId);
//        return buildMessages(messages);
//    }
    
    @Override
    @Transactional
    public List<MessageDto> getMessagesByRequest(Long requestId) throws RequestNotFoundException, AuthorizationException {
        Request request = validateRequest(requestId);
        User currentUser = getCurrentUser();
        
        if ( !request.getUser().equals(currentUser) && !request.getAd().getCreator().equals(currentUser) ) {
            throw new AuthorizationException("You can access only implied ads/requests");
        }
        
        if (request.getUser().equals(currentUser) && request.isMessagesHiddenByRequestor()) {
            return Collections.<MessageDto>emptyList();
        } else if (request.getAd().getCreator().equals(currentUser) && request.isMessagesHiddenByCreator()) {
            return Collections.<MessageDto>emptyList();
        }
        
        List<Message> messages = messageDao.getByRequest(requestId);
        return buildMessages(messages, currentUser, true);
    }
    
    @Override
    @Transactional
    public List<MessageDto> getMessagesByUsers(Long user1Id, Long user2Id) throws UserNotFoundException {
        User user1 = validateUser(user1Id);
        User user2 = validateUser(user2Id);
        List<Message> messages = messageDao.getByUsers(user1Id, user2Id);
        return buildMessages(messages, getCurrentUser(), true);
    }
    
    @Override
    @Transactional
    public List<MessageDto> getMessagesByAdAndUsers(Long adId, Long user1Id, Long user2Id) throws AdNotFoundException, UserNotFoundException {
        Ad ad = validateAd(adId);
        User user1 = validateUser(user1Id);
        User user2 = validateUser(user2Id);
        List<Message> messages = messageDao.getByAdAndUsers(adId, user1Id, user2Id);
        return buildMessages(messages, getCurrentUser(), true);
    }
    
    @Override
    @Transactional
    public Long sendMessage(MessageDto messageDto) throws UserNotFoundException, RequestNotFoundException, MessageValidationException {
        validateMessageDto(messageDto);
        
        User currentUser = getCurrentUser();
        
        Request request = null;
        if ( messageDto.getRequestId() != null ) {
            request = validateRequest(messageDto.getRequestId());
        }
        
        User to = null;
        if ( messageDto.getToName() != null ) {
            to = validateUser(messageDto.getToName());
        } else if ( messageDto.getToId() != null ) {
            to = validateUser(messageDto.getToId());
        } else if ( request != null ) {
            to = request.getAd().getCreator();
        }

        if ( request == null ) {
            throw new MessageValidationException("You can't send messages without request specified!");
        }
        if ( to == null ) {
            throw new UserNotFoundException("Could not detect the receipient (to) user.");
        }
        if (currentUser.equals(to)) {
            throw new MessageValidationException(MessageField.TO, "You can't send messages to yourself!");
        }

        Message message = new Message(messageDto.getText());
        message.setTo(to);
        message.setFrom(currentUser);
        message.setRequest(request);
        Long messageId = messageDao.save(message);
        
        request.setMessagesHiddenByRequestor(false);
        request.setMessagesHiddenByCreator(false);
        
        Map<String, Object> vars = new HashMap<String, Object>(0);
        vars.put("to", to);
        vars.put("from", currentUser);
        vars.put("request", request);

        emailSender.sendNotification(NotificationType.REQUEST_MESSAGED, to, vars);
        
        return messageId;
    }

    @Override
    @Transactional
    public void updateMessage(MessageDto messageDto) throws MessageNotFoundException, AuthorizationException, MessageValidationException {
        validateMessageDto(messageDto);
        Message message = validateMessage(messageDto.getId());
        User currentUser = getCurrentUser();
        Request request = message.getRequest();

        if (!message.getFrom().equals(currentUser)) {
            throw new AuthorizationException("Only owner can update the message!");
        }

        // Only text can be updated!
        message.setText(messageDto.getText());
        message.setUpdatedAt(new Date());
        
        request.setMessagesHiddenByRequestor(false);
        request.setMessagesHiddenByCreator(false);
    }

    @Override
    @Transactional
    public List<MessageDto> getAllMessages() {
        User currentUser = getCurrentUser();
        List<MessageDto> result = new LinkedList<MessageDto>();

        // incoming
        for (Message msg : currentUser.getReceivedMessages()) {
            if (msg.isDeleted() || msg.isHiddenByRecipient()) {
                continue;
            }

            MessageDto messageDto = new MessageDtoBuilder(msg)
                    .setCurrentUser(currentUser)
                    .build();
            result.add(messageDto);
            
            msg.setRead(true); // mark as read
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
    public int getUnreadMessagesSize(Long userId) throws UserNotFoundException {
        User user = validateUser(userId);
        List<Message> unreadMessages = messageDao.getUnreadMessages(userId);
        return unreadMessages != null ? unreadMessages.size() : 0;
    }
    
    @Override
    @Transactional
    public void hideRequestMessages(Long requestId) throws RequestNotFoundException, AuthorizationException {
        Request request = validateRequest(requestId);
        Ad ad = request.getAd();
        User currentUser = getCurrentUser();
        
        if (request.getUser().equals(currentUser)) {
            request.setMessagesHiddenByRequestor(true);
        } else if (ad.getCreator().equals(currentUser)) {
            request.setMessagesHiddenByCreator(true);
        } else {
            throw new AuthorizationException("You are neither requestor nor creator to hide the conversation messages!");
        }
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

//        // VKontakte
//        VKontakte vkontakte = getSocialNetworkApi(VKontakte.class);
//        if (vkontakte != null) {
//            vkontakte.wallOperations().post(message);
//        }
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
        if (commentDto.getText() == null || commentDto.getText().trim().isEmpty()) {
            throw new CommentValidationException(CommentField.TEXT, "Text field not specified!");
        }
        // ++
    }
    
    private void validateMessageDto(MessageDto messageDto) throws MessageValidationException {
        if (messageDto == null) {
            throw new NullPointerException("message is null!");
        }

        if (messageDto.getText() == null || messageDto.getText().trim().isEmpty()) {
            throw new MessageValidationException(MessageField.TEXT, "Text field not specified!");
        }
    }
    
    
    
    private List<MessageDto> buildMessages(List<Message> messages, User currentUser, boolean markAsRead) {
        List<MessageDto> result = new LinkedList<MessageDto>();
        for (Message message : messages) {
            if ( message.isDeleted() || message.isHiddenByRecipient() || message.isHiddenBySender() ) {
                continue;
            }
            
            MessageDto messageDto = new MessageDtoBuilder(message)
                    .setCurrentUser(currentUser)
                    .build();
            result.add(messageDto);
            
            if ( markAsRead && !messageDto.isOwner() ) {
                message.setRead(true); // mark as read
            }
        }
        return result;
    }
}
