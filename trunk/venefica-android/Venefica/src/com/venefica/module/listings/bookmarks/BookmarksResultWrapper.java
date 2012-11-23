package com.venefica.module.listings.bookmarks;

import java.util.List;

import com.venefica.services.AdDto;

/**
 * @author avinash
 * Wrapper class for bookmarks response
 */
public class BookmarksResultWrapper {
	public int result = -1;
	public String data = null;
	public List<AdDto> bookmarks;
}
