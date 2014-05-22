/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.venefica.service;

import com.venefica.common.ZenclusiveImpl;
import com.venefica.dao.StaticListPromoCodeDao;
import com.venefica.model.Ad;
import com.venefica.model.NotificationType;
import com.venefica.model.PromoCodeProvider;
import com.venefica.model.Request;
import com.venefica.model.StaticListPromoCode;
import com.venefica.model.User;
import com.venefica.service.dto.PromoCodeProviderDto;
import com.venefica.service.fault.RequestNotFoundException;
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
@Service("promoCodeService")
@WebService(endpointInterface = "com.venefica.service.PromoCodeService")
public class PromoCodeServiceImpl extends AbstractService implements PromoCodeService {
    
    @Inject
    private ZenclusiveImpl zenclusive;
    @Inject
    private StaticListPromoCodeDao staticListPromoCodeDao;

    @Override
    @Transactional
    public List<PromoCodeProviderDto> getProviders() {
        List<PromoCodeProvider> providers = promoCodeProviderDao.getProviders();
        List<PromoCodeProviderDto> result = new LinkedList<PromoCodeProviderDto>();
        if ( providers != null && !providers.isEmpty() ) {
            for ( PromoCodeProvider provider : providers ) {
                result.add(new PromoCodeProviderDto(provider));
            }
        }
        return result;
    }
    
    @Override
    @Transactional
    public void updateZenclusivePromoCode(
            Long requestId,
            String promoCode,
            String expirationDate,
            String value,
            String supportPhone,
            String supportUrl,
            String incentiveName,
            String incentiveUrl
    ) throws RequestNotFoundException {
        logger.debug("Updating zenclusive promo code (requestId: " + requestId + ", promoCode: " + promoCode + ")");
        
        Request request = validateRequest(requestId);
        request.setPromoCode(promoCode);
        request.markAsRedeemed();
        requestDao.update(request);
        
        Ad ad = request.getAd();
        User requestor = request.getUser();
        
        Map<String, Object> vars = new HashMap<String, Object>(0);
        vars.put("ad", ad);
        vars.put("user", requestor);
        vars.put("promoCode", promoCode);
        vars.put("expirationDate", expirationDate);
        vars.put("value", value);
        vars.put("supportPhone", supportPhone);
        vars.put("supportUrl", supportUrl);
        vars.put("incentiveName", incentiveName);
        vars.put("incentiveUrl", incentiveUrl);
        
        emailSender.sendNotification(NotificationType.PROMOCODE_UPDATED, "zenclusive", requestor, vars);
    }
    
    @Override
    @Transactional
    public void processProvider(Long promoCodeProviderId, Long requestId) {
        Request request = requestDao.get(requestId);
        PromoCodeProvider promoCodeProvider = promoCodeProviderDao.get(promoCodeProviderId);
        
        if ( request == null || request.isDeleted() ) {
            logger.warn("The given request is null or is marked as deleted (requestId: " + requestId + ") - promocode processing is skipped");
            return;
        } else if ( promoCodeProvider == null || promoCodeProvider.isDeleted() ) {
            logger.warn("The given provider is null or is marked as deleted (providerId: " +promoCodeProviderId + ") - promocode processing is skipped");
            return;
        }
        
        Ad ad = request.getAd();
        switch ( promoCodeProvider.getProviderType() ) {
            case ZENCLUSIVE:
                String email = zenclusive.buildZenclusiveEmail(request.getId());
                ZenclusiveImpl.Incentive incentive = zenclusive.getIncentiveByParams(promoCodeProvider.getParam1(), promoCodeProvider.getParam2());
                boolean result = zenclusive.sendReward(email, incentive);
                logger.debug("Zenclusive reward (email: " + email + ", incentive: " + incentive + ") sending succeeded: " + result);
                break;
            case STATIC_LIST:
                String promoCode = "";
                StaticListPromoCode staticListPromoCode = staticListPromoCodeDao.getOneByAd(ad.getId());
                if ( staticListPromoCode == null ) {
                    logger.warn("There is no unused static promo code for ad (adId: " + ad.getId() + ")");
                } else {
                    promoCode = staticListPromoCode.getPromoCode();
                    staticListPromoCodeDao.update(staticListPromoCode);
                }
                
                request.setPromoCode(promoCode);
                requestDao.update(request);
                logger.debug("Using static promo code (" + promoCode + ") for ad (" + ad.getId() + ")");
                break;
            default:
                logger.warn("Unknown promo code provider (" + promoCodeProvider.getProviderType() + ") when requesting ad (adId: " + ad.getId() + ")");
        }
    }
}
