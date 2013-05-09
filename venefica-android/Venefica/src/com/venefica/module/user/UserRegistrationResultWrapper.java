/**
 * 
 */
package com.venefica.module.user;

import java.util.List;

import com.venefica.services.ReviewDto;

/**
 * Wrapper class for user registration result data
 * 
 * @author avinash
 */
public class UserRegistrationResultWrapper {
	public int result = -1;
	public UserDto userDto = null;
	public String data = null;
	public List<UserDto> followings;
	public List<UserDto> followers;
	public List<ReviewDto> reviews;
}
