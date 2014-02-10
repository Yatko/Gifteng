package com.venefica.module.user;

import java.util.List;

/**
 * @author avinash
 * Group for followers and follwoings
 */
public class ProfileGroup {
	/**
	 * Group name
	 */
	private String groupName;
	/**
	 * followers and followings list
	 */
	private List<UserDto> users;
	/**
	 * @return the groupName
	 */
	public String getGroupName() {
		return groupName;
	}
	/**
	 * @param groupName the groupName to set
	 */
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
	/**
	 * @return the users
	 */
	public List<UserDto> getUsers() {
		return users;
	}
	/**
	 * @param users the users to set
	 */
	public void setUsers(List<UserDto> users) {
		this.users = users;
	}

}
