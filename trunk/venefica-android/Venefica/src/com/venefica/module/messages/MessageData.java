package com.venefica.module.messages;

/**
 * @author avinash
 * Class for message details
 */
public class MessageData {
	private long messageId;
	private long parentMessageId;
	private long addId;
	private long senderId;
	private String senderName = "";
	private String messageText = "";
	private String creationDate = "";
	private long likes;
	private long disLikes;
	private long totalVotes;
	private boolean isUnread = true;
	/**
	 * @return the messageId
	 */
	public long getMessageId() {
		return messageId;
	}
	/**
	 * @param messageId the messageId to set
	 */
	public void setMessageId(long messageId) {
		this.messageId = messageId;
	}
	/**
	 * @return the parentMessageId
	 */
	public long getParentMessageId() {
		return parentMessageId;
	}
	/**
	 * @param parentMessageId the parentMessageId to set
	 */
	public void setParentMessageId(long parentMessageId) {
		this.parentMessageId = parentMessageId;
	}
	/**
	 * @return the addId
	 */
	public long getAddId() {
		return addId;
	}
	/**
	 * @param addId the addId to set
	 */
	public void setAddId(long addId) {
		this.addId = addId;
	}
	/**
	 * @return the senderId
	 */
	public long getSenderId() {
		return senderId;
	}
	/**
	 * @param senderId the senderId to set
	 */
	public void setSenderId(long senderId) {
		this.senderId = senderId;
	}
	/**
	 * @return the senderName
	 */
	public String getSenderName() {
		return senderName;
	}
	/**
	 * @param senderName the senderName to set
	 */
	public void setSenderName(String senderName) {
		this.senderName = senderName;
	}
	/**
	 * @return the messageText
	 */
	public String getMessageText() {
		return messageText;
	}
	/**
	 * @param messageText the messageText to set
	 */
	public void setMessageText(String messageText) {
		this.messageText = messageText;
	}
	/**
	 * @return the creationDate
	 */
	public String getCreationDate() {
		return creationDate;
	}
	/**
	 * @param creationDate the creationDate to set
	 */
	public void setCreationDate(String creationDate) {
		this.creationDate = creationDate;
	}
	/**
	 * @return the likes
	 */
	public long getLikes() {
		return likes;
	}
	/**
	 * @param likes the likes to set
	 */
	public void setLikes(long likes) {
		this.likes = likes;
	}
	/**
	 * @return the disLikes
	 */
	public long getDisLikes() {
		return disLikes;
	}
	/**
	 * @param disLikes the disLikes to set
	 */
	public void setDisLikes(long disLikes) {
		this.disLikes = disLikes;
	}
	/**
	 * @return the isUnread
	 */
	public boolean isUnread() {
		return isUnread;
	}
	/**
	 * @param isUnread the isUnread to set
	 */
	public void setUnread(boolean isUnread) {
		this.isUnread = isUnread;
	}
	/**
	 * @return the totalVotes
	 */
	public long getTotalVotes() {
		return totalVotes;
	}
	/**
	 * @param totalVotes the totalVotes to set
	 */
	public void setTotalVotes(long totalVotes) {
		this.totalVotes = totalVotes;
	}
}
