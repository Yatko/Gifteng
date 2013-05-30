package com.venefica.service.dto.builder;

import com.venefica.model.Ad;
import com.venefica.model.Comment;
import com.venefica.model.Image;
import com.venefica.model.Rating;
import com.venefica.model.SpamMark;
import com.venefica.model.User;
import com.venefica.service.dto.AdDto;
import com.venefica.service.dto.AddressDto;
import com.venefica.service.dto.CommentDto;
import com.venefica.service.dto.ImageDto;
import com.venefica.service.dto.UserDto;
import java.util.LinkedList;
import java.util.List;

/**
 * Simplifies DTO object constructing.
 *
 * @author Sviatoslav Grebenchukov
 */
public class AdDtoBuilder extends DtoBuilderBase<Ad, AdDto> {

    private User currentUser;
    private List<Comment> filteredComments;
    private boolean includeCreatorFlag;
    private boolean includeImagesFlag;
    private boolean includeCanMarkAsSpamFlag;
    private boolean includeCanRateFlag;

    public AdDtoBuilder(Ad model) {
        super(model);
    }

    public AdDtoBuilder setCurrentUser(User user) {
        currentUser = user;
        return this;
    }
    
    public AdDtoBuilder setFilteredComments(List<Comment> comments) {
        filteredComments = comments;
        return this;
    }

    public AdDtoBuilder includeCreator() {
        return includeCreator(true);
    }
    
    public AdDtoBuilder includeCreator(boolean include) {
        includeCreatorFlag = include;
        return this;
    }

    public AdDtoBuilder includeImages() {
        return includeImages(true);
    }
    
    public AdDtoBuilder includeImages(boolean include) {
        includeImagesFlag = include;
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
        adDto.setCategoryId(model.getAdData().getCategory().getId());
        adDto.setCategory(model.getAdData().getCategory().getName());
        adDto.setTitle(model.getAdData().getTitle());
        adDto.setDescription(model.getAdData().getDescription());
        adDto.setPrice(model.getAdData().getPrice());
        adDto.setQuantity(model.getAdData().getQuantity());
        adDto.setFreeShipping(model.getAdData().getFreeShipping());
        adDto.setPickUp(model.getAdData().getPickUp());
        adDto.setPlace(model.getAdData().getPlace());
        adDto.setType(model.getAdData().getType());
        
        if (model.getAdData().getLocation() != null) {
            adDto.setAddress(new AddressDto(model.getAdData().getLocation()));
        }

        if (model.getAdData().getMainImage() != null) {
            adDto.setImage(new ImageDto(model.getAdData().getMainImage()));
        }

        if (model.getAdData().getThumbImage() != null) {
            adDto.setImageThumbnail(new ImageDto(model.getAdData().getThumbImage()));
        }
        
        if ( filteredComments != null ) {
            LinkedList<CommentDto> comments = new LinkedList<CommentDto>();
            
            for ( Comment comment : filteredComments ) {
                CommentDto commentDto = new CommentDto(comment, currentUser);
                comments.add(commentDto);
            }
            
            adDto.setComments(comments);
        }
        
        if (includeImagesFlag) {
            LinkedList<ImageDto> images = new LinkedList<ImageDto>();

            for (Image image : model.getAdData().getImages()) {
                ImageDto imageDto = new ImageDto(image);
                images.add(imageDto);
            }

            adDto.setImages(images);
        }

        adDto.setCreatedAt(model.getCreatedAt());
        adDto.setExpires(model.isExpires());
        adDto.setExpired(model.isExpired());
        adDto.setExpiresAt(model.getExpiresAt());
        adDto.setAvailableAt(model.getAvailableAt());
        adDto.setNumAvailProlongations(model.getNumAvailProlongations());
        adDto.setNumViews(model.getNumViews());
        adDto.setRating(model.getRating());
        adDto.setOwner(currentUser != null && model.getCreator().equals(currentUser));
        
        model.getAdData().updateAdDto(adDto);
        
        if (includeCreatorFlag) {
            UserDto creator = new UserDto(model.getCreator());
            creator.setInFollowers(currentUser.inFollowers(model.getCreator()));
            creator.setInFollowings(currentUser.inFollowings(model.getCreator()));
            
            adDto.setCreator(creator);
        }

        if (includeCanMarkAsSpamFlag) {
            boolean canMarkAsSpam = true;
            
            //cannot mark own ad
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

            //cannot rate already rated ads
            for (Rating rating : model.getRatings()) {
                if (rating.getFrom().equals(currentUser)) {
                    canRate = false;
                    break;
                }
            }

            adDto.setCanRate(canRate);
        }

        return adDto;
    }
}
