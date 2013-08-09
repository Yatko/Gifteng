package com.venefica.service.dto.builder;

import com.venefica.config.Constants;
import com.venefica.model.Ad;
import com.venefica.model.AdStatus;
import com.venefica.model.Comment;
import com.venefica.model.Image;
import com.venefica.model.Rating;
import com.venefica.model.Request;
import com.venefica.model.SpamMark;
import com.venefica.model.User;
import com.venefica.service.dto.AdDto;
import com.venefica.service.dto.AdStatisticsDto;
import com.venefica.service.dto.AddressDto;
import com.venefica.service.dto.CommentDto;
import com.venefica.service.dto.ImageDto;
import com.venefica.service.dto.RequestDto;
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
    private boolean includeCanRequestFlag;
    private boolean includeRequestsFlag;
    private boolean includeStatisticsFlag = true;

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
    
    public AdDtoBuilder includeCanRequest() {
        includeCanRequestFlag = true;
        return this;
    }
    
    public AdDtoBuilder includeRequests() {
        return includeRequests(true);
    }
    
    public AdDtoBuilder includeRequests(boolean include) {
        includeRequestsFlag = include;
        return this;
    }
    
    public AdDtoBuilder includeStatistics(boolean include) {
        includeStatisticsFlag = include;
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
        adDto.setStatus(model.getStatus());
        adDto.setAddress(new AddressDto(model.getAdData().getAddress(), model.getAdData().getLocation()));

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
        
        if ( includeRequestsFlag ) {
            LinkedList<RequestDto> requests = new LinkedList<RequestDto>();
            
            for (Request request : model.getVisibleRequests()) {
                RequestDto requestDto = new RequestDto(request);
                requests.add(requestDto);
            }
            
            adDto.setRequests(requests);
        }
        
        adDto.setCreatedAt(model.getCreatedAt());
        adDto.setOnline(model.isOnline());
        adDto.setApproved(model.isApproved());
        adDto.setExpires(model.isExpires());
        adDto.setExpired(model.isExpired());
        adDto.setExpiresAt(model.getExpiresAt());
        adDto.setAvailableAt(model.getAvailableAt());
        adDto.setOwner(currentUser != null && model.getCreator().equals(currentUser));
        adDto.setSold(model.isSold());
        adDto.setSoldAt(model.getSoldAt());
        
        model.getAdData().updateAdDto(adDto);
        
        if (includeCreatorFlag) {
            UserDto creator = new UserDto(model.getCreator());
            creator.setInFollowers(currentUser.inFollowers(model.getCreator()));
            creator.setInFollowings(currentUser.inFollowings(model.getCreator()));
            
            adDto.setCreator(creator);
        }

        if (includeCanMarkAsSpamFlag) {
            boolean canMarkAsSpam = true;
            
            if ( canMarkAsSpam ) {
                //cannot mark own ad
                if ( model.getCreator().equals(currentUser) ) {
                    canMarkAsSpam = false;
                }
            }
            if ( canMarkAsSpam ) {
                //cannot mark twice the same ad
                for (SpamMark spamMark : model.getSpamMarks()) {
                    if (spamMark.getWitness().equals(currentUser)) {
                        canMarkAsSpam = false;
                        break;
                    }
                }
            }

            adDto.setCanMarkAsSpam(canMarkAsSpam);
        }

        if (includeCanRateFlag) {
            boolean canRate = true;

            if ( canRate ) {
                //cannot rate already rated ad (by the same user)
                for (Rating rating : model.getRatings()) {
                    if (rating.getFrom().equals(currentUser)) {
                        canRate = false;
                        break;
                    }
                }
            }
            if ( canRate ) {
                canRate = false;
                //cannot rate by not selected requestor
                for (Request request : model.getVisibleRequests()) {
                    if (
                        request.getUser().equals(currentUser) &&
                        request.isAccepted() &&
                        (request.isSent() || request.isReceived())
                    ) {
                        canRate = true;
                        break;
                    }
                }
            }

            adDto.setCanRate(canRate);
        }
        
        if ( includeCanRequestFlag ) {
            boolean canRequest = true;
            
            if ( canRequest ) {
                if ( model.getStatus() != AdStatus.ACTIVE && model.getStatus() != AdStatus.IN_PROGRESS ) {
                    canRequest = false;
                }
            }
            if ( canRequest ) {
                if ( model.getAdData().getQuantity() <= 0 ) {
                    //no more available
                    canRequest = false;
                }
            }
            if ( canRequest ) {
                if ( model.getActiveRequests().size() >= Constants.REQUEST_MAX_ALLOWED ) {
                    //active requests limit reched the allowed size
                    canRequest = false;
                }
            }
            if ( canRequest ) {
                if ( model.isExpired() ) {
                    canRequest = false;
                }
            }
            if ( canRequest ) {
                if ( model.getCreator().equals(currentUser) ) {
                    //owner cannot request its ad
                    canRequest = false;
                }
            }
            if ( canRequest ) {
                if ( currentUser.isBusinessAccount() ) {
                    //business accounts cannot request at all
                    canRequest = false;
                }
            }
            if ( canRequest ) {
                if ( model.isRequested(currentUser, true) ) {
                    //an active request exists for this ad
                    canRequest = false;
                }
            }
            
            adDto.setCanRequest(canRequest);
        }
        
        if (includeStatisticsFlag) {
            AdStatisticsDto statistics = new AdStatisticsDto();
            statistics.setNumAvailProlongations(model.getNumAvailProlongations());
            statistics.setNumViews(model.getNumViews());
            statistics.setRating(model.getRating());
            statistics.setNumBookmarks(model.getBookmarks() != null ? model.getBookmarks().size() : 0);
            statistics.setNumComments(model.getComments() != null ? model.getComments().size() : 0);
            statistics.setNumRequests(model.getRequests() != null ? model.getRequests().size() : 0);
            statistics.setNumShares(0); //TODO

            adDto.setStatistics(statistics);
        }

        return adDto;
    }
}
