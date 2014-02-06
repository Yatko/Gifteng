/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.venefica.service;

import com.venefica.common.MailException;
import com.venefica.config.AppConfig;
import com.venefica.dao.AdDataDao;
import com.venefica.dao.ApprovalDao;
import com.venefica.dao.ImageDao;
import com.venefica.dao.UserPointDao;
import com.venefica.model.Ad;
import com.venefica.model.AdStatus;
import com.venefica.model.Approval;
import com.venefica.model.Image;
import com.venefica.model.ImageModelType;
import com.venefica.model.MemberAdData;
import com.venefica.model.NotificationType;
import com.venefica.model.Request;
import com.venefica.model.Shipping;
import com.venefica.model.User;
import com.venefica.model.UserPoint;
import com.venefica.service.dto.AdDto;
import com.venefica.service.dto.ApprovalDto;
import com.venefica.service.dto.ImageDto;
import com.venefica.service.dto.ShippingDto;
import com.venefica.service.dto.UserDto;
import com.venefica.service.dto.builder.AdDtoBuilder;
import com.venefica.service.fault.AdNotFoundException;
import com.venefica.service.fault.ApprovalNotFoundException;
import com.venefica.service.fault.GeneralException;
import com.venefica.service.fault.PermissionDeniedException;
import com.venefica.service.fault.ShippingNotFoundException;
import com.venefica.service.fault.UserNotFoundException;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import javax.inject.Inject;
import javax.jws.WebService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author gyuszi
 */
@Service("adminService")
@WebService(endpointInterface = "com.venefica.service.AdminService")
public class AdminServiceImpl extends AbstractService implements AdminService {
    
    @Inject
    private AppConfig appConfig;
    @Inject
    private UserPointDao userPointDao;
    @Inject
    private ApprovalDao approvalDao;
    @Inject
    private AdDataDao adDataDao;
    @Inject
    private ImageDao imageDao;

    //***************
    //* approval    *
    //***************
    
    @Override
    @Transactional
    public List<AdDto> getUnapprovedAds() throws UserNotFoundException, PermissionDeniedException {
        User currentUser = getCurrentUser();
        validateAdminUser(currentUser);
        
        List<AdDto> result = new LinkedList<AdDto>();
        List<Ad> ads = adDao.getUnapprovedAds();
        
        for (Ad ad : ads) {
            Approval approval = approvalDao.getByAdAndRevision(ad.getId(), ad.getRevision());
            if ( approval != null ) {
                //an approval already exists for the actual ad revision
                continue;
            }
            
            AdDto adDto = new AdDtoBuilder(ad)
                    .setCurrentUser(currentUser)
                    .includeCreator()
                    .includeFollower(false)
                    .includeAdStatistics(false)
                    .includeRelist(false)
                    .build();
            
            result.add(adDto);
        }
        
        return result;
    }
    
    @Override
    @Transactional
    public List<AdDto> getOfflineAds() throws UserNotFoundException, PermissionDeniedException {
        User currentUser = getCurrentUser();
        validateAdminUser(currentUser);
        
        List<AdDto> result = new LinkedList<AdDto>();
        List<Ad> ads = adDao.getOfflineAds();
        
        for (Ad ad : ads) {
            AdDto adDto = new AdDtoBuilder(ad)
                    .setCurrentUser(currentUser)
                    .includeCreator()
                    .includeFollower(false)
                    .includeAdStatistics(false)
                    .includeRelist(false)
                    .build();
            
            result.add(adDto);
        }
        
        return result;
    }
    
    @Override
    public List<ApprovalDto> getApprovals(Long adId) throws UserNotFoundException, PermissionDeniedException, AdNotFoundException {
        User currentUser = getCurrentUser();
        validateAdminUser(currentUser);
        Ad ad = validateAd(adId);
        
        List<ApprovalDto> result = new LinkedList<ApprovalDto>();
        List<Approval> approvals = approvalDao.getByAd(adId);
        
        for (Approval approval : approvals) {
            ApprovalDto approvalDto = new ApprovalDto(approval);
            result.add(approvalDto);
        }
        
        return result;
    }
    
    @Override
    public ApprovalDto getApproval(Long adId, Integer revision) throws UserNotFoundException, PermissionDeniedException, AdNotFoundException, ApprovalNotFoundException {
        User currentUser = getCurrentUser();
        validateAdminUser(currentUser);
        Ad ad = validateAd(adId);
        
        Approval approval = approvalDao.getByAdAndRevision(adId, revision);
        if ( approval == null ) {
            throw new ApprovalNotFoundException(adId);
        }
        
        return new ApprovalDto(approval);
    }

    @Override
    @Transactional
    public void approveAd(Long adId) throws UserNotFoundException, PermissionDeniedException, AdNotFoundException, GeneralException {
        User currentUser = getCurrentUser();
        validateAdminUser(currentUser);
        
        Ad ad = validateAd(adId);
        ad.markAsApproved();
        ad.setStatus(AdStatus.ACTIVE);
        
        Approval approval = new Approval(true);
        approval.setDecider(currentUser);
        approval.setAd(ad);
        approval.setRevision(ad.getRevision());
        approvalDao.save(approval);
        
//        Map<String, Object> vars = new HashMap<String, Object>(0);
//        vars.put("ad", ad);
//        vars.put("creator", ad.getCreator());
//        
//        boolean success = emailSender.sendNotification(NotificationType.AD_APPROVED, ad.getCreator(), vars);
//        if ( !success ) {
//            logger.error("Could not send approval notification email (adId: " + adId + ")");
//            throw new GeneralException(MailException.EMAIL_SEND_ERROR, "Could not send approval notification mail!");
//        }
    }

    @Override
    @Transactional
    public void unapproveAd(Long adId, String message) throws UserNotFoundException, PermissionDeniedException, AdNotFoundException, GeneralException {
        User currentUser = getCurrentUser();
        validateAdminUser(currentUser);
        
        Ad ad = validateAd(adId);
        ad.unmarkAsApproved();
        ad.unmarkAsOnline();
        ad.setStatus(AdStatus.OFFLINE);
        
        Approval approval = new Approval(false);
        approval.setDecider(currentUser);
        approval.setAd(ad);
        approval.setRevision(ad.getRevision());
        approval.setText(message);
        approvalDao.save(approval);
        
        Map<String, Object> vars = new HashMap<String, Object>(0);
        vars.put("ad", ad);
        vars.put("creator", ad.getCreator());
        vars.put("text", message);
        
        boolean success = emailSender.sendNotification(NotificationType.AD_UNAPPROVED, ad.getCreator(), vars);
        if ( !success ) {
            logger.error("Could not send unapproval notification email (adId: " + adId + ")");
            throw new GeneralException(MailException.EMAIL_SEND_ERROR, "Could not send unapproval notification mail!");
        }
    }
    
    @Override
    @Transactional
    public void onlineAd(Long adId) throws UserNotFoundException, PermissionDeniedException, AdNotFoundException {
        User currentUser = getCurrentUser();
        validateAdminUser(currentUser);
        
        Ad ad = validateAd(adId);
        ad.markAsOnline();
        
        User creator = userDao.getEager(ad.getCreator().getId());
        if ( !creator.isBusinessAccount() ) {
            MemberAdData adData = adDataDao.getMemberAdDataByAd(ad.getId());
            if ( !adData.isRequestLimitIncreased() ) {
                adData.setRequestLimitIncreased(true);
                adDataDao.update(adData);
                
                //increasing request limit for members - just once
                UserPoint userPoint = creator.getUserPoint();
                userPoint.incrementRequestLimit(appConfig.getRequestLimitAdNew());
                userPointDao.update(userPoint);
            }
        }
    }
    
    //***************
    //* user        *
    //***************

    @Override
    @Transactional
    public List<UserDto> getUsers() throws UserNotFoundException, PermissionDeniedException {
        User currentUser = getCurrentUser();
        validateAdminUser(currentUser);
        
        List<UserDto> result = new ArrayList<UserDto>(0);
        List<User> users = userDao.getAll();
        for ( User user : users ) {
            UserDto userDto = new UserDto(user);
            result.add(userDto);
        }
        return result;
    }
    
    //***************
    //* shipping    *
    //***************
    
    @Override
    @Transactional
    public List<ShippingDto> getShippings() throws UserNotFoundException, PermissionDeniedException {
        User currentUser = getCurrentUser();
        validateAdminUser(currentUser);
        
        List<ShippingDto> result = new ArrayList<ShippingDto>(0);
        List<Shipping> shippings = shippingDao.getShippings();
        for ( Shipping shipping : shippings ) {
            ShippingDto shippingDto = new ShippingDto(shipping);
            result.add(shippingDto);
        }
        return result;
    }

    @Override
    @Transactional
    public void updateShipping(ShippingDto shippingDto) throws UserNotFoundException, PermissionDeniedException, ShippingNotFoundException {
        User currentUser = getCurrentUser();
        validateAdminUser(currentUser);
        
        try {
            Shipping shipping = validateShipping(shippingDto.getId());
            ImageDto imageDto = shippingDto.getBarcodeImage();
            Image image = shipping.getBarcodeImage();
            
            if ( imageDto != null && imageDto.isValid() ) {
                if ( image != null ) {
                    shipping.setBarcodeImage(null);
                    imageDao.delete(image, ImageModelType.SHIPPING);
                }
                
                image = imageDto.toImage();
                imageDao.save(image, ImageModelType.SHIPPING);
            }
            
            shipping.setReceivedAmount(shippingDto.getReceivedAmount());
            shipping.setTrackingNumber(shippingDto.getTrackingNumber());
            shipping.setBarcodeImage(image);
            shippingDao.update(shipping);
        } catch ( Exception ex ) {
            logger.error("Exception when saving image for shipping", ex);
        }
    }

    @Override
    @Transactional
    public boolean sendMailToCreator(Long shippingId) throws UserNotFoundException, PermissionDeniedException, ShippingNotFoundException {
        User currentUser = getCurrentUser();
        validateAdminUser(currentUser);
        
        Shipping shipping = validateShipping(shippingId);
        verifyShippingComplete(shipping);
        
        Request request = shipping.getRequest();
        Ad ad = request.getAd();
        User creator = ad.getCreator();
        User receiver = request.getUser();
        List<File> attachments = new ArrayList<File>(0);
        String subtype = NotificationType.SUBTYPE_SHIPPING_CREATOR;
        
        if ( !loadImageIntoAttachments(shipping, attachments) ) {
            return false;
        }
        
        Map<String, Object> vars = new HashMap<String, Object>(0);
        vars.put("ad", ad);
        vars.put("creator", creator);
        vars.put("receiver", receiver);
        
        boolean success = emailSender.sendNotification(NotificationType.SHIPPING, subtype, creator, vars, attachments);
        if ( success ) {
            shipping.setEmailCreatorSent(success);
            shipping.setEmailCreatorSentAt(new Date());
            shippingDao.update(shipping);
        }
        return success;
    }

    @Override
    @Transactional
    public boolean sendMailToReceiver(Long shippingId) throws UserNotFoundException, PermissionDeniedException, ShippingNotFoundException {
        User currentUser = getCurrentUser();
        validateAdminUser(currentUser);
        
        Shipping shipping = validateShipping(shippingId);
        verifyShippingComplete(shipping);
        
        Request request = shipping.getRequest();
        Ad ad = request.getAd();
        User creator = ad.getCreator();
        User receiver = request.getUser();
        String subtype = NotificationType.SUBTYPE_SHIPPING_RECEIVER;
        
        Map<String, Object> vars = new HashMap<String, Object>(0);
        vars.put("ad", ad);
        vars.put("creator", creator);
        vars.put("receiver", receiver);
        vars.put("shipping", shipping);
        
        boolean success = emailSender.sendNotification(NotificationType.SHIPPING, subtype, receiver, vars);
        if ( success ) {
            shipping.setEmailReceiverSent(success);
            shipping.setEmailReceiverSentAt(new Date());
            shippingDao.update(shipping);
        }
        return success;
    }

    @Override
    @Transactional
    public void deleteShipping(Long shippingId) throws UserNotFoundException, PermissionDeniedException, ShippingNotFoundException {
        User currentUser = getCurrentUser();
        validateAdminUser(currentUser);
        validateShipping(shippingId);
        
        shippingDao.delete(shippingId);
    }
    
    // internal helpers
    
    private void validateAdminUser(User user) throws PermissionDeniedException {
        if ( user == null ) {
            throw new PermissionDeniedException("Permission denied as user is null");
        }
        if ( !user.isAdmin() ) {
            throw new PermissionDeniedException("Permission denied as user (userId: " + user.getId() + ") is not admin");
        }
    }
    
    private Shipping validateShipping(Long shippingId) throws ShippingNotFoundException {
        if (shippingId == null) {
            throw new NullPointerException("shippingId is null!");
        }
        
        Shipping shipping = shippingDao.get(shippingId);
        if (shipping == null) {
            throw new ShippingNotFoundException(shippingId);
        }
        return shipping;
    }
    
    private boolean loadImageIntoAttachments(Shipping shipping, List<File> attachments) {
        try {
            Image image = shipping.getBarcodeImage() != null ? imageDao.get(shipping.getBarcodeImage().getId(), ImageModelType.SHIPPING) : null;
            if ( image != null && image.getFile() != null ) {
                attachments.add(image.getFile());
            }
            return true;
        } catch ( IOException ex ) {
            logger.error("Could not load barcode image for shipping (shippingId: " + shipping.getId() + ")", ex);
            return false;
        }
    }
    
    private void verifyShippingComplete(Shipping shipping) {
        if ( shipping == null ) {
            throw new NullPointerException("Shipping is null");
        } else if ( shipping.getTrackingNumber() == null || shipping.getTrackingNumber().trim().isEmpty() ) {
            throw new IllegalArgumentException("Shipping (shippingId: " + shipping.getId() + ") tracking number is empty/null.");
        } else if ( shipping.getReceivedAmount() == null || shipping.getReceivedAmount() == BigDecimal.ZERO ) {
            throw new IllegalArgumentException("Shipping (shippingId: " + shipping.getId() + ") amount is zero/null.");
        } else if ( shipping.getBarcodeImage() == null ) {
            throw new IllegalArgumentException("Shipping (shippingId: " + shipping.getId() + ") barcode image is null.");
        }
    }
}
