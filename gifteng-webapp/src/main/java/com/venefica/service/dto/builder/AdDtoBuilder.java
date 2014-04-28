package com.venefica.service.dto.builder;

import com.venefica.model.Ad;
import com.venefica.model.AdData;
import com.venefica.model.BusinessAdData;
import com.venefica.model.Comment;
import com.venefica.model.Image;
import com.venefica.model.PromoCodeProvider;
import com.venefica.model.Request;
import com.venefica.model.SpamMark;
import com.venefica.model.User;
import com.venefica.service.dto.AdDto;
import com.venefica.service.dto.AdStatisticsDto;
import com.venefica.service.dto.AddressDto;
import com.venefica.service.dto.CommentDto;
import com.venefica.service.dto.ImageDto;
import com.venefica.service.dto.PromoCodeProviderDto;
import com.venefica.service.dto.RequestDto;
import com.venefica.service.dto.ShippingBoxDto;
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
    private AdData adData;
    
    private boolean includeCreatorFlag;
    private boolean includeFollowerFlag = true;
    private boolean includeImagesFlag;
    private boolean includeCanRelistFlag = true;
    private boolean includeCanMarkAsSpamFlag;
    private boolean includeCanRateFlag;
    private boolean includeCanRequestFlag;
    private boolean includeRequestsFlag;
    private boolean includeAdStatisticsFlag = true;

    public AdDtoBuilder(Ad model) {
        super(model);
    }
    
    public AdDtoBuilder(Ad model, AdData adData) {
        this(model);
        this.adData = adData;
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
    
    public AdDtoBuilder includeFollower(boolean include) {
        includeFollowerFlag = include;
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
    
    public AdDtoBuilder includeAdStatistics(boolean include) {
        includeAdStatisticsFlag = include;
        return this;
    }
    
    public AdDtoBuilder includeRelist(boolean include) {
        includeCanRelistFlag = include;
        return this;
    }

    @Override
    public AdDto build() {
        AdDto adDto = new AdDto();
        adDto.setId(model.getId());
        adDto.setRevision(model.getRevision());
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

        if ( model.getAdData().getShippingBox() != null ) {
            adDto.setShippingBox(new ShippingBoxDto(model.getAdData().getShippingBox()));
        }
        
        if ( adData != null && model.isBusinessAd() ) {
            BusinessAdData businessAdData = (BusinessAdData) adData;
            PromoCodeProvider promoCodeProvider = businessAdData.getPromoCodeProvider();
            if ( promoCodeProvider != null ) {
                adDto.setPromoCodeProvider(new PromoCodeProviderDto(promoCodeProvider));
            }
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
        
        if ( includeRequestsFlag ) {
            //using all visible requests (not hidden and not deleted)
            LinkedList<RequestDto> requests = new LinkedList<RequestDto>();
            
            for (Request request : model.getVisibleRequests()) {
                RequestDto requestDto = new RequestDto(request, adData);
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
            if ( includeFollowerFlag ) {
                creator.setInFollowers(currentUser.inFollowers(model.getCreator()));
                creator.setInFollowings(currentUser.inFollowings(model.getCreator()));
            }
            
            adDto.setCreator(creator);
        }
        
        if ( includeCanRelistFlag ) {
            boolean canRelist = true;
            boolean canProlong = false;
            
            if ( canRelist ) {
                if ( !model.getCreator().equals(currentUser) ) {
                    canRelist = false;
                }
            }
            if ( canRelist ) {
                if ( model.isDeleted() ) {
                    canRelist = false;
                }
            }
            if ( canRelist ) {
                if ( !model.isExpired() ) {
                    canRelist = false;
                }
            }
            if ( canRelist ) {
                canProlong = model.canProlong();
            }
            
            adDto.setCanRelist(canRelist);
            adDto.setCanProlong(canProlong);
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
            boolean canRate = false;
            //cannot rate by not selected requestor neither already rated requests
            for (Request request : model.getVisibleRequests()) {
//                if ( !request.getUser().equals(currentUser) && !model.getCreator().equals(currentUser) ) {
//                    //the current user is not the owner and not the requestor
//                    continue;
//                } else if ( request.isAlreadyRated(currentUser) ) {
//                    continue;
//                } else if ( request.isAccepted() && (request.isSent() || request.isReceived()) ) {
//                    canRate = true;
//                    break;
//                }
                
                if (
                    request.isAccepted() && (request.isSent() || request.isReceived()) &&
                    currentUser.matches(request.getUser(), model.getCreator()) &&
                    !request.isAlreadyRated(currentUser)
                ) {
                    canRate = true;
                    break;
                }
            }

            adDto.setCanRate(canRate);
        }
        
        if ( includeCanRequestFlag ) {
            boolean canRequest = true;
            
            if ( canRequest ) {
                if ( model.getCreator().equals(currentUser) ) {
                    //owner cannot request owned ads
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
                if ( !model.canRequest() ) {
                    //ad cannot be requested by it's states
                    canRequest = false;
                }
            }
            if ( canRequest ) {
                if ( model.hasActiveRequest(currentUser) ) {
                    //an active request exists for this ad
                    canRequest = false;
                }
            }
            if ( canRequest ) {
                if ( !currentUser.getUserPoint().canRequest(model) ) {
                    //not enough user points
                    canRequest = false;
                }
            }
            
            adDto.setCanRequest(canRequest);
        }
        
        if (includeAdStatisticsFlag) {
            adDto.setStatistics(AdStatisticsDto.build(model));
        }

        return adDto;
    }
}
