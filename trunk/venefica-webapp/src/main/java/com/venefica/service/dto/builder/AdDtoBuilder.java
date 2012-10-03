package com.venefica.service.dto.builder;

import java.util.LinkedList;

import com.venefica.model.Ad;
import com.venefica.model.Image;
import com.venefica.model.Rating;
import com.venefica.model.SpamMark;
import com.venefica.model.User;
import com.venefica.service.dto.AdDto;
import com.venefica.service.dto.ImageDto;
import com.venefica.service.dto.UserDto;


/**
 * Simplifies DTO object constructing.
 * 
 * @author Sviatoslav Grebenchukov
 */
public class AdDtoBuilder extends DtoBuilderBase<Ad, AdDto> {
	private User currentUser;
	private boolean includeCreatorFlag;
	private boolean includeImagesFlag;
	private boolean includeCanMarkAsSpamFlag;
	private boolean includeCanRateFlag;

	public AdDtoBuilder(Ad model) {
		super(model);
	}
	
	public AdDtoBuilder setCurrentUser(User user)  {
		currentUser = user;
		return this;
	}

	public AdDtoBuilder includeCreator() {
		includeCreatorFlag = true;
		return this;
	}

	public AdDtoBuilder includeImages() {
		includeImagesFlag = true;
		return this;
	}

	public AdDtoBuilder includeCanMarkAsSpam() {
		includeCanMarkAsSpamFlag = true;
		return this;
	}
	
	public AdDtoBuilder includeCanRate() {
		includeCanRateFlag = true;
		return this;
	}

	@Override
	public AdDto build() {
		AdDto adDto = new AdDto();
		adDto.setId(model.getId());
		adDto.setCategoryId(model.getCategory().getId());
		adDto.setCategory(model.getCategory().getName());
		adDto.setTitle(model.getTitle());
		adDto.setDescription(model.getDescription());
		adDto.setPrice(model.getPrice());

		if (model.getLocation() != null) {
			adDto.setLatitude(model.getLocation().getY());
			adDto.setLongitude(model.getLocation().getX());
		}

		if (model.getMainImage() != null)
			adDto.setImage(new ImageDto(model.getMainImage()));

		if (model.getThumbImage() != null)
			adDto.setImageThumbnail(new ImageDto(model.getThumbImage()));

		if (includeImagesFlag) {
			LinkedList<ImageDto> images = new LinkedList<ImageDto>();

			for (Image image : model.getImages()) {
				ImageDto imageDto = new ImageDto(image);
				images.add(imageDto);
			}

			adDto.setImages(images);
		}

		adDto.setCreatedAt(model.getCreatedAt());
		adDto.setWanted(model.isWanted());
		adDto.setExpired(model.isExpired());
		adDto.setExpiresAt(model.getExpiresAt());
		adDto.setNumAvailProlongations(model.getNumAvailProlongations());
		adDto.setNumViews(model.getNumViews());
		adDto.setRating(model.getRating());

		adDto.setOwner(currentUser != null && model.getCreator().equals(currentUser));

		if (includeCreatorFlag) {
			adDto.setCreator(new UserDto(model.getCreator()));
		}

		if (includeCanMarkAsSpamFlag) {
			boolean canMarkAsSpam = true;

			for (SpamMark spamMark : model.getSpamMarks()) {
				if (spamMark.getWitness().equals(currentUser)) {
					canMarkAsSpam = false;
					break;
				}
			}

			adDto.setCanMarkAsSpam(canMarkAsSpam);
		}
		
		if (includeCanRateFlag) {
			boolean canRate = true;
			
			for (Rating rating : model.getRatings()) {
				if (rating.getUser().equals(currentUser)) {
					canRate = false;
					break;
				}
			}
			
			adDto.setCanRate(canRate);
		}

		return adDto;
	}
}
