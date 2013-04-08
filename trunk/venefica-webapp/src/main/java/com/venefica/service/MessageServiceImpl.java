package com.venefica.service;

import com.venefica.dao.AdDao;
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
    private AdDao adDao;
    
    @Inject
    private CommentDao commentDao;
    
    @Inject
    private MessageDao messageDao;
    
    @Override
    @Transactional
    public Long addCommentToAd(Long adId, CommentDto commentDto) throws AdNotFoundException,
            CommentValidationException {
        if (adId == null) {
            throw new NullPointerException("adId is null!");
        }
        if (commentDto == null) {
            throw new NullPointerException("commentDto is null!");
        }

        Ad ad = adDao.get(adId);

        if (ad == null) {
            throw new AdNotFoundException(adId);
        }

        // ++ TODO: create comment validator
        if (commentDto.getText() == null) {
            throw new CommentValidationException(CommentField.TEXT, "Text field not specified!");
        }
        // ++

        Comment comment = new Comment(ad, getCurrentUser(), commentDto.getText());
        commentDao.save(comment);

        // Attach the comment to the ad
        ad.getComments().add(comment);
        return comment.getId();
    }

    @Override
    @Transactional
    public void updateComment(CommentDto commentDto) throws CommentNotFoundException,
            CommentValidationException {
        if (commentDto == null) {
            throw new NullPointerException("commentDto is null!");
        }

        Long commentId = commentDto.getId();
        Comment comment = commentDao.get(commentId);

        if (comment == null) {
            throw new CommentNotFoundException(commentId);
        }

        // ++ TODO: create comment validator
        if (commentDto.getText() == null) {
            throw new CommentValidationException(CommentField.TEXT, "Text field not specified!");
        }
        // ++

        // WARNING! This update must be performed within an active transaction!
        commentDto.update(comment);
        comment.setUpdatedAt(new Date());
    }

    @Override
    @Transactional
    public List<CommentDto> getCommentsByAd(Long adId, Long lastCommentId, int numComments)
            throws AdNotFoundException {
        if (adId == null) {
            throw new NullPointerException("adId is null");
        }

        Ad ad = adDao.get(adId);

        if (ad == null) {
            throw new AdNotFoundException(adId);
        }

        LinkedList<CommentDto> result = new LinkedList<CommentDto>();

        User currentUser = getCurrentUser();
        List<Comment> comments = commentDao.getAdComments(adId, lastCommentId, numComments);

        for (Comment comment : comments) {
            CommentDto commentDto = new CommentDto(comment, currentUser);
            result.add(commentDto);
        }

        return result;
    }

    @Override
    @Transactional
    public Long sendMessage(MessageDto messageDto) throws UserNotFoundException,
            MessageValidationException {
        if (messageDto == null) {
            throw new NullPointerException("message is null!");
        }

        User to = userDao.findUserByName(messageDto.getToName());

        if (to == null) {
            throw new UserNotFoundException("User with name = '" + messageDto.getToName()
                    + "' not found!");
        }

        if (messageDto.getText() == null) {
            throw new MessageValidationException(MessageField.TEXT, "Text field not specified!");
        }

        User currentUser = getCurrentUser();

        if (currentUser.getName().equals(to.getName())) {
            throw new MessageValidationException(MessageField.TO,
                    "You can't send messages to yourself!");
        }

        Message message = new Message(messageDto.getText());
        message.setTo(to);
        message.setFrom(currentUser);

        return messageDao.save(message);
    }

    @Override
    @Transactional
    public void updateMessage(MessageDto messageDto) throws MessageNotFoundException,
            AuthorizationException, MessageValidationException {

        if (messageDto == null) {
            throw new NullPointerException("messageDto is null!");
        }

        Message message = messageDao.get(messageDto.getId());

        if (message == null) {
            throw new MessageNotFoundException(messageDto.getId());
        }

        User currentUser = getCurrentUser();

        if (!message.getFrom().equals(currentUser)) {
            throw new AuthorizationException("Only owner can update the message!");
        }

        if (messageDto.getText() == null) {
            throw new MessageValidationException(MessageField.TEXT, "Empty messages are not allowed!");
        }

        // Only text can be updated!
        message.setText(messageDto.getText());
    }

    @Override
    @Transactional
    public List<MessageDto> getAllMessages() {
        User currentUser = getCurrentUser();
        LinkedList<MessageDto> result = new LinkedList<MessageDto>();

        // incoming
        for (Message msg : currentUser.getReceivedMessages()) {
            if (msg.isHiddenByRecipient()) {
                continue;
            }

            MessageDto messageDto = new MessageDtoBuilder(msg).setCurrentUser(currentUser).build();
            msg.setRead(true); // mark as read
            result.add(messageDto);
        }

        // outgoing
        for (Message msg : currentUser.getSentMessages()) {
            if (msg.isHiddenBySender()) {
                continue;
            }

            MessageDto messageDto = new MessageDtoBuilder(msg).setCurrentUser(currentUser).build();
            result.add(messageDto);
        }

        return result;
    }

    @Override
    @Transactional
    public void hideMessage(Long messageId) throws MessageNotFoundException, AuthorizationException {
        Message message = messageDao.get(messageId);

        if (message == null) {
            throw new MessageNotFoundException(messageId);
        }

        User currentUser = getCurrentUser();

        if (message.getTo().equals(currentUser)) {
            message.setHiddenByRecipient(true);
        } else if (message.getFrom().equals(currentUser)) {
            message.setHiddenBySender(true);
        } else {
            throw new AuthorizationException(
                    "You are neither recipient nor sender to hide the message!");
        }
    }

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

    @Override
    @Transactional
    public void deleteMessage(Long messageId) throws MessageNotFoundException,
            AuthorizationException {
        Message message = messageDao.get(messageId);

        if (message == null) {
            throw new MessageNotFoundException(messageId);
        }

        User currentUser = getCurrentUser();

        if (!message.getTo().equals(currentUser) && !message.getFrom().equals(currentUser)) {
            throw new AuthorizationException("You are neither recipient nor sender of the message!");
        }

        messageDao.deleteMessage(message);
    }
}
