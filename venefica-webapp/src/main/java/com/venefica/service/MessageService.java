package com.venefica.service;

import java.util.List;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.ParameterStyle;

import com.venefica.service.dto.CommentDto;
import com.venefica.service.dto.MessageDto;
import com.venefica.service.fault.AdNotFoundException;
import com.venefica.service.fault.AuthorizationException;
import com.venefica.service.fault.CommentNotFoundException;
import com.venefica.service.fault.CommentValidationException;
import com.venefica.service.fault.MessageNotFoundException;
import com.venefica.service.fault.MessageValidationException;
import com.venefica.service.fault.UserNotFoundException;


/**
 * Allows to add comments to ads and sends messages directly to users.
 * 
 * @author Sviatoslav Grebenchukov
 */
@WebService(name = "MessageService", targetNamespace = Namespace.SERVICE)
@SOAPBinding(parameterStyle = ParameterStyle.WRAPPED)
public interface MessageService {

	/**
	 * Adds comment (question, answer, etc.) to the ad.
	 * 
	 * @param adId
	 *            the id of the ad
	 * @param messageDto
	 *            the comment to add
	 * @return id of the created message
	 * @throws CommentValidationException
	 *             if commentDto doesn't contain required fields
	 */
	@WebMethod(operationName = "AddCommentToAd")
	@WebResult(name = "commentId")
	Long addCommentToAd(@WebParam(name = "adId") Long adId,
			@WebParam(name = "comment") CommentDto commentDto) throws AdNotFoundException,
			CommentValidationException;

	/**
	 * Updates the comment in the database.
	 * 
	 * @param commentDto
	 *            updated comment
	 * @throws CommentValidationException
	 *             if commentDto doesn't contain required fields
	 */
	@WebMethod(operationName = "UpdateComment")
	void updateComment(@WebParam(name = "comment") CommentDto commentDto)
			throws CommentNotFoundException, CommentValidationException;

	/**
	 * Returns a list of comments by the ad.
	 * 
	 * @param id
	 *            if of the ad
	 * @param lastCommentId
	 *            the id of the last retrieved comment of -1 to return all comments
	 * @param numComments
	 *            the maximum number of comments to return
	 * @return list of ads
	 * @throws AdNotFoundException
	 *             if an ad with the specified id not found
	 */
	@WebMethod(operationName = "GetCommentsByAd")
	@WebResult(name = "comment")
	List<CommentDto> getCommentsByAd(@WebParam(name = "adId") Long adId,
			@WebParam(name = "lastCommentId") Long lastCommentId,
			@WebParam(name = "numComments") int numComments) throws AdNotFoundException;

	/**
	 * Places the message on the walls of the connected social networks.
	 * 
	 * @param message
	 *            text of the message
	 */
	@WebMethod(operationName = "ShareOnSocialNetworks")
	void shareOnSocialNetworks(@WebParam(name = "message") String message);

	/**
	 * Sends the message to the specified user.
	 * 
	 * @param message
	 *            message dto object
	 * @return id of the created message
	 * @throws UserNotFoundException
	 *             if a user with the specified name not found
	 * @throws MessageValidationException
	 *             if messageDto contains invalid fields.
	 */
	@WebMethod(operationName = "SendMessage")
	@WebResult(name = "messageId")
	Long sendMessage(@WebParam(name = "message") MessageDto messageDto)
			throws UserNotFoundException, MessageValidationException;

	/**
	 * Updates message sent by the current user.
	 * 
	 * @param messageDto
	 *            updated message
	 * @throws MessageNotFoundException
	 *             if a message with the specified id not found
	 * @throws AuthorizationException
	 *             if not owner of the message tries to update it
	 * @throws MessageValidationException
	 *             if updated message doesn't contain required fields
	 */
	@WebMethod(operationName = "UpdateMessage")
	void updateMessage(@WebParam(name = "message") MessageDto messageDto)
			throws MessageNotFoundException, AuthorizationException, MessageValidationException;

	/**
	 * Get all messages sent to the current user or received by him.
	 * 
	 * @return list of messages
	 */
	@WebMethod(operationName = "GetAllMessages")
	@WebResult(name = "message")
	List<MessageDto> getAllMessages();

	/**
	 * Marks the message as hidden one.
	 * 
	 * @param messageId
	 *            id of the message
	 * @throws MessageNotFoundException
	 *             if a message with the specified id not found
	 * @throws AuthorizationException
	 *             if we are neither recipient nor sender of the message
	 */
	@WebMethod(operationName = "HideMessage")
	void hideMessage(@WebParam(name = "messageId") Long messageId) throws MessageNotFoundException,
			AuthorizationException;

	/**
	 * Removes the message completely from the database.
	 * 
	 * @param messageId
	 *            id of the message
	 * @throws MessageNotFoundException
	 *             if a message with the specified id not found
	 * @throws AuthorizationException
	 *             if we are neither recipient nor sender of the message
	 */
	@WebMethod(operationName = "DeleteMessage")
	void deleteMessage(@WebParam(name = "messageId") Long messageId)
			throws MessageNotFoundException, AuthorizationException;
}
