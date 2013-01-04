package com.venefica.module.listings;

import java.util.List;

import com.venefica.services.AdDto;
import com.venefica.services.CommentDto;

/**
 * @author avinash
 * Wrapper class for listing details 
 */
public class ListingDetailsResultWrapper {
	public int result = -1;
	public String data = null;
	public AdDto listing;
	public List<CommentDto> comments;
}
