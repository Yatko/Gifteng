package com.venefica.service;

import com.venefica.dao.AdDao;
import com.venefica.dao.CommentDao;
import com.venefica.dao.MessageDao;
import com.venefica.model.Ad;
import com.venefica.model.AdType;
import com.venefica.model.Comment;
import com.venefica.model.Message;
import com.venefica.service.dto.AdDto;
import com.venefica.service.dto.CommentDto;
import com.venefica.service.dto.MessageDto;
import com.venefica.service.fault.AdNotFoundException;
import com.venefica.service.fault.AdValidationException;
import com.venefica.service.fault.AlreadyRequestedException;
import com.venefica.service.fault.AuthorizationException;
import com.venefica.service.fault.CommentNotFoundException;
import com.venefica.service.fault.CommentValidationException;
import com.venefica.service.fault.GeneralException;
import com.venefica.service.fault.InvalidAdStateException;
import com.venefica.service.fault.InvalidRequestException;
import com.venefica.service.fault.MessageNotFoundException;
import com.venefica.service.fault.MessageValidationException;
import com.venefica.service.fault.PermissionDeniedException;
import com.venefica.service.fault.RequestNotFoundException;
import com.venefica.service.fault.UserNotFoundException;
import java.math.BigDecimal;
import java.util.List;
import javax.annotation.Resource;
import javax.inject.Inject;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;
import org.apache.cxf.endpoint.Client;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles("test")
@ContextConfiguration(locations = "/MessageServiceTest-context.xml")
public class MessageServiceTest extends ServiceTestBase<MessageService> {

    private static final Long TEST_AD_ID = 1L;
    private static final Long TEST_REQUEST_ID = 1L;
    //private static final Long TestMessageId = new Long(1);
    
    @Inject
    private AdDao adDao;
    @Inject
    private CommentDao commentDao;
    @Inject
    private MessageDao messageDao;
    
    @Resource(name = "adPublishedUrl")
    private String adEndpointAddress;
    @Resource(name = "adminPublishedUrl")
    private String adminEndpointAddress;
    
    private Ad ad;
    //private Message message;
    
    public MessageServiceTest() {
        super(MessageService.class);
    }

    @Before
    public void init() {
        ad = adDao.get(TEST_AD_ID);
        assertNotNull(ad);

        //message = messageDao.get(TestMessageId);
        //assertNotNull(message);
    }

    @Test
    public void addCommentToAdTest() throws AdNotFoundException, CommentValidationException {
        CommentDto comment = new CommentDto("This is a test comment");

        authenticateClientAsSecondUser();
        Long commentId = client.addCommentToAd(ad.getId(), comment);
        assertNotNull("Comment id must be returned!", commentId);
    }

    @Test(expected = AdNotFoundException.class)
    public void addCommentToUnexistingAdTest() throws AdNotFoundException,
            CommentValidationException {
        CommentDto comment = new CommentDto("Test comment");
        authenticateClientAsFirstUser();
        client.addCommentToAd(new Long(-1), comment);
    }

    @Test(expected = CommentValidationException.class)
    public void addInvalidCommentTest() throws AdNotFoundException, CommentValidationException {
        CommentDto comment = new CommentDto();
        comment.setText(null);

        authenticateClientAsFirstUser();
        client.addCommentToAd(ad.getId(), comment);
    }

    @Test
    public void updateCommentTest() throws AdNotFoundException, CommentValidationException,
            CommentNotFoundException {
        CommentDto comment = new CommentDto("New comment");

        authenticateClientAsFirstUser();
        Long commentId = client.addCommentToAd(ad.getId(), comment);

        comment.setId(commentId);
        comment.setText("Updated comment");
        client.updateComment(comment);

        Comment storedComment = commentDao.get(commentId);

        assertNotNull("Comment not found in the database!", storedComment);
        assertEquals("Comment not updated!", comment.getText(), storedComment.getText());
        assertNotNull("Comment updateAt field not set!", storedComment.getUpdatedAt());
    }

    @Test(expected = CommentNotFoundException.class)
    public void updateUnexistingCommentTest() throws CommentNotFoundException,
            CommentValidationException {
        CommentDto comment = new CommentDto("Some comment");
        comment.setId(new Long(-1));

        authenticateClientAsFirstUser();
        client.updateComment(comment);
    }

    @Test(expected = CommentValidationException.class)
    public void updateInvalidCommentTest() throws AdNotFoundException, CommentValidationException,
            CommentNotFoundException {
        CommentDto comment = new CommentDto("Some comment");
        authenticateClientAsFirstUser();
        Long messageId = client.addCommentToAd(ad.getId(), comment);

        // update with invalid comment
        comment.setId(messageId);
        comment.setText(null);
        client.updateComment(comment);
    }

    @Test
    public void getCommentsByAdTest() throws AdNotFoundException {
        authenticateClientAsFirstUser();
        List<CommentDto> comments = client.getCommentsByAd(ad.getId(), new Long(-1), 10);
        assertNotNull("List of comments must be returned!", comments);
        assertTrue("At least one comment must exist", !comments.isEmpty());
    }

    @Test(expected = UserNotFoundException.class)
    public void sendMessageToUnexistingUserTest() throws UserNotFoundException, RequestNotFoundException, MessageValidationException {
        authenticateClientAsFirstUser();
        MessageDto messageDto = new MessageDto("UnexisingUserName", "Test message: sendMessageToUnexistingUserTest");
        messageDto.setRequestId(TEST_REQUEST_ID);
        client.sendMessage(messageDto);
    }
    
    @Test(expected = MessageValidationException.class)
    public void sendMessageWithoutRequestTest() throws UserNotFoundException, RequestNotFoundException, MessageValidationException {
        authenticateClientAsFirstUser();
        MessageDto messageDto = new MessageDto(getSecondUser().getName(), "Test message: sendMessageWithoutRequestTest");
        client.sendMessage(messageDto);
    }

    @Test(expected = MessageValidationException.class)
    public void sendInvalidMessageTest() throws UserNotFoundException, RequestNotFoundException, MessageValidationException {
        authenticateClientAsFirstUser();
        MessageDto messageDto = new MessageDto(getSecondUser().getName(), null);
        messageDto.setRequestId(TEST_REQUEST_ID);
        client.sendMessage(messageDto);
    }

    @Test(expected = MessageValidationException.class)
    public void sendMessageToMyselfTest() throws UserNotFoundException, RequestNotFoundException, MessageValidationException {
        authenticateClientAsFirstUser();
        MessageDto messageDto = new MessageDto(getFirstUser().getName(), "Test message: sendMessageToMyselfTest");
        messageDto.setRequestId(TEST_REQUEST_ID);
        client.sendMessage(messageDto);
    }

    @Test
    public void sendMessageTest() throws UserNotFoundException, RequestNotFoundException, MessageValidationException {
        authenticateClientAsFirstUser();
        MessageDto messageDto = new MessageDto(getSecondUser().getName(), "Test message: sendMessageTest");
        messageDto.setRequestId(TEST_REQUEST_ID);
        Long messageId = client.sendMessage(messageDto);

        Message message = messageDao.get(messageId);
        assertNotNull("Message with id  = " + messageId + " not found!", message);
        assertNotNull("Id field not set!", message.getId());
        assertTrue("Message text not match", messageDto.getText().equals(message.getText()));
        assertNotNull("CreatedAt field is null!", message.getCreatedAt());
        assertTrue("Message must be marked as not read!", !message.isRead());
        messageDao.deleteMessage(message);
    }

    @Test(expected = MessageNotFoundException.class)
    public void updateUnexistingMessageTest() throws MessageNotFoundException, AuthorizationException, MessageValidationException {
        authenticateClientAsFirstUser();
        MessageDto messageDto = new MessageDto();
        messageDto.setId(new Long(-1));
        messageDto.setText("test");

        client.updateMessage(messageDto);
    }

    @Test(expected = AuthorizationException.class)
    public void updateMessageWithDifferentUserTest() throws MessageNotFoundException, AuthorizationException, MessageValidationException, UserNotFoundException, RequestNotFoundException {
        authenticateClientAsFirstUser();
        
        MessageDto messageDto = new MessageDto(getSecondUser().getName(), "Test message: updateMessageWithDifferentUserTest");
        messageDto.setRequestId(TEST_REQUEST_ID);
        Long messageId = client.sendMessage(messageDto);
        
        List<MessageDto> messages = client.getAllMessages();
        messageDto = messages.get(0);
        authenticateClientAsSecondUser();
        
        try {
            messageDto.setText("updated text");
            client.updateMessage(messageDto);
        } finally {
            Message message = messageDao.get(messageId);
            messageDao.deleteMessage(message);
        }
    }

    @Test(expected = MessageValidationException.class)
    public void updateWithInvalidMessageTest() throws MessageNotFoundException, AuthorizationException, MessageValidationException, UserNotFoundException, RequestNotFoundException {
        authenticateClientAsFirstUser();
        
        MessageDto messageDto = new MessageDto(getSecondUser().getName(), "Test message: updateWithInvalidMessageTest");
        messageDto.setRequestId(TEST_REQUEST_ID);
        Long messageId = client.sendMessage(messageDto);
        
        List<MessageDto> messages = client.getAllMessages();
        messageDto = messages.get(0);
        
        try {
            messageDto.setText(null);
            client.updateMessage(messageDto);
        } finally {
            Message message = messageDao.get(messageId);
            messageDao.deleteMessage(message);
        }
    }

    @Test
    public void updateMessageTest() throws MessageNotFoundException, AuthorizationException, MessageValidationException, UserNotFoundException, RequestNotFoundException {
        authenticateClientAsFirstUser();
        
        MessageDto messageDto = new MessageDto(getSecondUser().getName(), "Test message: updateMessageTest");
        messageDto.setRequestId(TEST_REQUEST_ID);
        Long messageId = client.sendMessage(messageDto);
        
        List<MessageDto> messages = client.getAllMessages();
        messageDto = messages.get(0);
        
        try {
            messageDto.setText("New test message: updateMessageTest");
            client.updateMessage(messageDto);
        } finally {
            Message message = messageDao.get(messageId);
            messageDao.deleteMessage(message);
        }
    }

    @Test
    public void getAllMessagesCalledByThirdUserTest() {
        authenticateClientAsThirdUser();
        List<MessageDto> messages = client.getAllMessages();

        assertTrue("Third user can't see messages not addressed to him or not sent by him!",
                messages == null);
    }

    @Test
    public void getAllMessagesTest() throws UserNotFoundException, RequestNotFoundException, MessageValidationException {
        authenticateClientAsFirstUser();
        
        MessageDto messageDto = new MessageDto(getSecondUser().getName(), "Test message: getAllMessagesTest");
        messageDto.setRequestId(TEST_REQUEST_ID);
        client.sendMessage(messageDto);
        
        authenticateClientAsSecondUser();
        List<MessageDto> messages = client.getAllMessages();

        // only one incoming message should be in the collection
        assertTrue("Invalid number of messages in the collection!", messages.size() == 1);

        MessageDto message = messages.get(0);
        assertTrue("Message must be marked as not read!", !message.isRead());

        Message storedMessage = messageDao.get(message.getId());
        assertTrue("Message in the database must be marked as read!", storedMessage.isRead());
        
        messageDao.deleteMessage(storedMessage);
    }

    @Test(expected = MessageNotFoundException.class)
    public void hideUnexistingMessageTest() throws MessageNotFoundException, AuthorizationException {
        authenticateClientAsFirstUser();
        client.hideMessage(new Long(-1));
    }

    @Test(expected = AuthorizationException.class)
    public void hideMessageWithTirdUserTest() throws UserNotFoundException, RequestNotFoundException,
            MessageValidationException, MessageNotFoundException, AuthorizationException {
        authenticateClientAsFirstUser();
        MessageDto messageDto = new MessageDto(getSecondUser().getName(), "Second message: hideMessageWithTirdUserTest");
        messageDto.setRequestId(TEST_REQUEST_ID);
        Long messageId = client.sendMessage(messageDto);

        try {
            authenticateClientAsThirdUser();
            client.hideMessage(messageId);
        } finally {
            Message message = messageDao.get(messageId);
            messageDao.deleteMessage(message);
        }
    }

    @Test
    public void hideMessageTest() throws UserNotFoundException, RequestNotFoundException, MessageValidationException,
            MessageNotFoundException, AuthorizationException {
        authenticateClientAsFirstUser();
        MessageDto messageDto = new MessageDto(getSecondUser().getName(), "Third message: hideMessageTest");
        messageDto.setRequestId(TEST_REQUEST_ID);
        Long messageId = client.sendMessage(messageDto);
        client.hideMessage(messageId);

        Message updatedMessage = messageDao.get(messageId);
        assertTrue("HiddenBySender must be true!", updatedMessage.isHiddenBySender());
        assertTrue("HiddenByRecipient must be false!", !updatedMessage.isHiddenByRecipient());

        authenticateClientAsSecondUser();
        client.hideMessage(messageId);

        updatedMessage = messageDao.get(messageId);
        assertTrue("HiddenByRecipient must be true!", updatedMessage.isHiddenByRecipient());
        
        Message message = messageDao.get(messageId);
        messageDao.deleteMessage(message);
    }

    @Test(expected = MessageNotFoundException.class)
    public void deleteUnexistingMessageTest() throws MessageNotFoundException,
            AuthorizationException {
        authenticateClientAsFirstUser();
        client.deleteMessage(new Long(-1));
    }

    @Test(expected = AuthorizationException.class)
    public void deleteMessageByThirdUserTest() throws UserNotFoundException, RequestNotFoundException,
            MessageValidationException, MessageNotFoundException, AuthorizationException {
        authenticateClientAsFirstUser();
        MessageDto messageDto = new MessageDto(getSecondUser().getName(), "Four message: deleteMessageByThirdUserTest");
        messageDto.setRequestId(TEST_REQUEST_ID);
        Long messageId = client.sendMessage(messageDto);
        
        try {
            authenticateClientAsThirdUser();
            client.deleteMessage(messageId);
        } finally {
            Message message = messageDao.get(messageId);
            messageDao.deleteMessage(message);
        }
    }

    @Test
    public void deleteMessageTest() throws UserNotFoundException, RequestNotFoundException, MessageValidationException,
            MessageNotFoundException, AuthorizationException {
        authenticateClientAsFirstUser();
        MessageDto messageDto = new MessageDto(getSecondUser().getName(), "Five message: deleteMessageTest");
        messageDto.setRequestId(TEST_REQUEST_ID);
        Long messageId = client.sendMessage(messageDto);
        client.deleteMessage(messageId);

        Message deletedMessage = messageDao.get(messageId);
        assertTrue("Message has not been deleted!", deletedMessage.isDeleted());
    }
    
    @Test
    public void getLastMessagePerRequestTest() throws AdValidationException, AdNotFoundException, AlreadyRequestedException, InvalidRequestException, InvalidAdStateException, UserNotFoundException, RequestNotFoundException, MessageValidationException, PermissionDeniedException, GeneralException {
        AdService adService = buildAdService();
        AdminService adminService = buildAdminService();
        
        authenticateClientWithToken(adService, firstUserAuthToken);
        
        MessageDto messageDto;
        Long messageId;
        List<MessageDto> messages;
        
        AdDto adDto = new AdDto();
        adDto.setTitle("Test ad title");
        adDto.setDescription("Test ad description");
        adDto.setCategoryId(1L);
        adDto.setQuantity(1);
        adDto.setPrice(BigDecimal.TEN);
        adDto.setType(AdType.MEMBER);
        
        Long adId = adService.placeAd(adDto);
        assertNotNull("Ad has to be created", adId);
        
        authenticateClientWithToken(adminService, firstUserAuthToken);
        adminService.approveAd(adId);
        adminService.onlineAd(adId);
        
        authenticateClientWithToken(adService, secondUserAuthToken);
        
        Long requestId_1 = adService.requestAd(adId, "Please choose me (second user).");
        assertNotNull("The second user request should succeed", requestId_1);
        
        authenticateClientWithToken(adService, thirdUserAuthToken);
        
        Long requestId_2 = adService.requestAd(adId, "Please choose me (third user).");
        assertNotNull("The third user request should succeed", requestId_2);
        
        authenticateClientAsFirstUser();
        
        messageDto = new MessageDto();
        messageDto.setText("Give me a reason user 2.");
        messageDto.setRequestId(requestId_1);
        messageDto.setToName(SECOND_USER_NAME);
        
        messageId = client.sendMessage(messageDto);
        assertNotNull("The message to user 2 should succeed", messageId);
        
        messageDto = new MessageDto();
        messageDto.setText("Give me a reason user 3.");
        messageDto.setRequestId(requestId_2);
        messageDto.setToName(THIRD_USER_NAME);
        
        messageId = client.sendMessage(messageDto);
        assertNotNull("The message to user 3 should succeed", messageId);
        
        authenticateClientAsSecondUser();
        
        messageDto = new MessageDto();
        messageDto.setText("Because I deserve it - user 2.");
        messageDto.setRequestId(requestId_1);
        messageDto.setToName(FIRST_USER_NAME);
        
        messageId = client.sendMessage(messageDto);
        assertNotNull("The message from user 2 should succeed", messageId);
        
        authenticateClientAsThirdUser();
        
        messageDto = new MessageDto();
        messageDto.setText("Because I deserve it - user 3.");
        messageDto.setRequestId(requestId_2);
        messageDto.setToName(FIRST_USER_NAME);
        
        messageId = client.sendMessage(messageDto);
        assertNotNull("The message from user 3 should succeed", messageId);
        
        
        
        
        authenticateClientAsFirstUser();
        
        messages = client.getLastMessagePerRequest();
        System.out.println("messages: " + messages);
        assertEquals("The size should be 2", 2, messages.size());
        
        authenticateClientAsSecondUser();
        
        messages = client.getLastMessagePerRequest();
        System.out.println("messages: " + messages);
        assertEquals("The size should be 1", 1, messages.size());
        
        authenticateClientAsThirdUser();
        
        messages = client.getLastMessagePerRequest();
        System.out.println("messages: " + messages);
        assertEquals("The size should be 1", 1, messages.size());
    }
    
    private AdService buildAdService() {
        Class adServiceClass = AdService.class;
        AdService adService = (AdService) createClient(adEndpointAddress, adServiceClass);
        Client adServiceCxfClient = getCxfClient(adService);
        configureTimeouts(adServiceCxfClient);
        return adService;
    }
    
    private AdminService buildAdminService() {
        Class adminServiceClass = AdminService.class;
        AdminService adminService = (AdminService) createClient(adminEndpointAddress, adminServiceClass);
        Client adminServiceCxfClient = getCxfClient(adminService);
        configureTimeouts(adminServiceCxfClient);
        return adminService;
    }
}
