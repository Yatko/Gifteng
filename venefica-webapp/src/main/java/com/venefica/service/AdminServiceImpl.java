/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.venefica.service;

import com.venefica.common.MailException;
import com.venefica.dao.ApprovalDao;
import com.venefica.model.Ad;
import com.venefica.model.AdStatus;
import com.venefica.model.Approval;
import com.venefica.model.User;
import com.venefica.service.dto.AdDto;
import com.venefica.service.dto.ApprovalDto;
import com.venefica.service.dto.UserDto;
import com.venefica.service.dto.builder.AdDtoBuilder;
import com.venefica.service.fault.AdNotFoundException;
import com.venefica.service.fault.ApprovalNotFoundException;
import com.venefica.service.fault.GeneralException;
import com.venefica.service.fault.PermissionDeniedException;
import java.util.ArrayList;
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
    
    private static final String AD_APPROVED_TEMPLATE = "ad-approval-accept/";
    private static final String AD_APPROVED_SUBJECT_TEMPLATE = AD_APPROVED_TEMPLATE + "subject.vm";
    private static final String AD_APPROVED_HTML_MESSAGE_TEMPLATE = AD_APPROVED_TEMPLATE + "message.html.vm";
    private static final String AD_APPROVED_PLAIN_MESSAGE_TEMPLATE = AD_APPROVED_TEMPLATE + "message.txt.vm";
    
    private static final String AD_UNAPPROVED_TEMPLATE = "ad-approval-reject/";
    private static final String AD_UNAPPROVED_SUBJECT_TEMPLATE = AD_UNAPPROVED_TEMPLATE + "subject.vm";
    private static final String AD_UNAPPROVED_HTML_MESSAGE_TEMPLATE = AD_UNAPPROVED_TEMPLATE + "message.html.vm";
    private static final String AD_UNAPPROVED_PLAIN_MESSAGE_TEMPLATE = AD_UNAPPROVED_TEMPLATE + "message.txt.vm";
    
    private static final String AD_ONLINE_TEMPLATE = "ad-online/";
    private static final String AD_ONLINE_SUBJECT_TEMPLATE = AD_ONLINE_TEMPLATE + "subject.vm";
    private static final String AD_ONLINE_HTML_MESSAGE_TEMPLATE = AD_ONLINE_TEMPLATE + "message.html.vm";
    private static final String AD_ONLINE_PLAIN_MESSAGE_TEMPLATE = AD_ONLINE_TEMPLATE + "message.txt.vm";
    
    @Inject
    private ApprovalDao approvalDao;

    //***************
    //* approval    *
    //***************
    
    @Override
    @Transactional
    public List<AdDto> getUnapprovedAds() throws PermissionDeniedException {
        User currentUser = getCurrentUser();
        validateAdminUser(currentUser);
        
        List<AdDto> result = new LinkedList<AdDto>();
        List<Ad> ads = adDao.getUnapprovedAds();
        
        for (Ad ad : ads) {
            AdDto adDto = new AdDtoBuilder(ad)
                    .setCurrentUser(currentUser)
                    .includeCreator()
                    .build();
            
            result.add(adDto);
        }
        
        return result;
    }
    
    @Override
    @Transactional
    public List<AdDto> getOfflineAds() throws PermissionDeniedException {
        User currentUser = getCurrentUser();
        validateAdminUser(currentUser);
        
        List<AdDto> result = new LinkedList<AdDto>();
        List<Ad> ads = adDao.getOfflineAds();
        
        for (Ad ad : ads) {
            AdDto adDto = new AdDtoBuilder(ad)
                    .setCurrentUser(currentUser)
                    .includeCreator()
                    .build();
            
            result.add(adDto);
        }
        
        return result;
    }
    
    @Override
    public List<ApprovalDto> getApprovals(Long adId) throws PermissionDeniedException, AdNotFoundException {
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
    public ApprovalDto getApproval(Long adId, Integer revision) throws PermissionDeniedException, AdNotFoundException, ApprovalNotFoundException {
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
    public void approveAd(Long adId) throws PermissionDeniedException, AdNotFoundException, GeneralException {
        User currentUser = getCurrentUser();
        validateAdminUser(currentUser);
        
        Ad ad = validateAd(adId);
        ad.markAsApproved();
        ad.setStatus(AdStatus.ACTIVE);
        
        Approval approval = new Approval(true);
        approval.setDecider(currentUser);
        approval.setAd(ad);
        approvalDao.save(approval);
        
        String email = ad.getCreator().getEmail();
        Map<String, Object> vars = new HashMap<String, Object>(0);
        vars.put("ad", ad);
        
        try {
            emailSender.sendHtmlEmailByTemplates(
                    AD_APPROVED_SUBJECT_TEMPLATE,
                    AD_APPROVED_HTML_MESSAGE_TEMPLATE,
                    AD_APPROVED_PLAIN_MESSAGE_TEMPLATE,
                    email,
                    vars
                    );
        } catch ( MailException ex ) {
            logger.error("Could not send approval notification email (email: " + email+ ")", ex);
            throw new GeneralException(ex.getErrorCode(), "Could not send approval notification mail!");
        }
    }

    @Override
    @Transactional
    public void unapproveAd(Long adId, String message) throws PermissionDeniedException, AdNotFoundException, GeneralException {
        User currentUser = getCurrentUser();
        validateAdminUser(currentUser);
        
        Ad ad = validateAd(adId);
        ad.unmarkAsApproved();
        ad.setStatus(AdStatus.OFFLINE);
        
        Approval approval = new Approval(false);
        approval.setDecider(currentUser);
        approval.setAd(ad);
        approval.setText(message);
        approvalDao.save(approval);
        
        String email = ad.getCreator().getEmail();
        Map<String, Object> vars = new HashMap<String, Object>(0);
        vars.put("ad", ad);
        vars.put("text", message);
        
        try {
            emailSender.sendHtmlEmailByTemplates(
                    AD_UNAPPROVED_SUBJECT_TEMPLATE,
                    AD_UNAPPROVED_HTML_MESSAGE_TEMPLATE,
                    AD_UNAPPROVED_PLAIN_MESSAGE_TEMPLATE,
                    email,
                    vars
                    );
        } catch ( MailException ex ) {
            logger.error("Could not send approval notification email (email: " + email+ ")", ex);
            throw new GeneralException(ex.getErrorCode(), "Could not send approval notification mail!");
        }
    }
    
    @Override
    @Transactional
    public void onlineAd(Long adId) throws PermissionDeniedException, AdNotFoundException {
        User currentUser = getCurrentUser();
        validateAdminUser(currentUser);
        
        Ad ad = validateAd(adId);
        ad.markAsOnline();
    }
    
    //***************
    //* user        *
    //***************

    @Override
    @Transactional
    public List<UserDto> getUsers() throws PermissionDeniedException {
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
    
    // internal helpers
    
    private void validateAdminUser(User user) throws PermissionDeniedException {
        if ( user == null ) {
            throw new PermissionDeniedException("Permission denied as user is null");
        }
        if ( !user.isAdmin() ) {
            throw new PermissionDeniedException("Permission denied as user (userId: " + user.getId() + ") is not admin");
        }
    }
}
