/**
 * 
 */
package com.venefica.module.messages;

import java.util.List;

import com.venefica.services.MessageDto;

/**
 * @author avinash
 *
 */
public class MessageResultWrapper {
	public int result = -1;
	public String data = null;
	public MessageDto message;
	public List<MessageDto> messages;
}
