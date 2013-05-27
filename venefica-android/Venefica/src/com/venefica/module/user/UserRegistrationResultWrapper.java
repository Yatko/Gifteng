/**
 * 
 */
package com.venefica.module.user;

import java.util.List;

import com.venefica.services.RatingDto;

/**
 * Wrapper class for user registration result data
 * 
 * @author avinash
 */
public class UserRegistrationResultWrapper {
	public int result = -1;
	public UserDto userDto = null;
	public String data = null;
	public List<UserDto> followings  = null;
	public List<UserDto> followers = null;
	public List<RatingDto> reviews = null;
}
