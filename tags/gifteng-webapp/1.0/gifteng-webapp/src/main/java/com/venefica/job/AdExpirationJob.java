package com.venefica.job;

import com.venefica.config.Constants;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.venefica.dao.AdDao;
import com.venefica.dao.RequestDao;
import com.venefica.dao.UserTransactionDao;
import com.venefica.model.Ad;
import com.venefica.model.AdStatus;
import com.venefica.model.Request;
import com.venefica.model.TransactionStatus;
import com.venefica.model.UserTransaction;
import java.util.Date;
import java.util.List;
import org.quartz.ExecuteInJTATransaction;

@DisallowConcurrentExecution
//@ExecuteInJTATransaction
public class AdExpirationJob implements Job {

    private static final Log log = LogFactory.getLog(AdExpirationJob.class);
    
    @Override
    public void execute(JobExecutionContext ctx) throws JobExecutionException {
        AdDao adDao = (AdDao) ctx.getJobDetail().getJobDataMap().get(Constants.AD_DAO);
        RequestDao requestDao = (RequestDao) ctx.getJobDetail().getJobDataMap().get(Constants.REQUEST_DAO);
        UserTransactionDao userTransactionDao = (UserTransactionDao) ctx.getJobDetail().getJobDataMap().get(Constants.USER_TRANSACTION_DAO);

        try {
            List<Ad> ads = adDao.getExpiredAds();
            int numRows = 0;
            for ( Ad ad : ads ) {
                List<Request> requests = requestDao.getByAd(ad.getId());
                boolean expire = true;

                if ( requests != null && !requests.isEmpty() ) {
                    for ( Request request : requests ) {
                        if ( request.isAccepted() || request.isSent() || request.isReceived() ) {
                            expire = false;
                            break;
                        }
                    }
                }

                if ( expire ) {
                    ad.setExpired(true);
                    ad.setStatus(AdStatus.EXPIRED);
                    ad.setNumExpire(ad.getNumExpire() + 1);
                    adDao.update(ad);
                    
                    UserTransaction adTransaction = userTransactionDao.getByAd(ad.getId());
                    if ( adTransaction != null ) {
                        adTransaction.markAsFinalized(TransactionStatus.EXPIRED);
                        userTransactionDao.update(adTransaction);
                    } else {
                        log.error("There is no transaction associated with this ad (adId: " + ad.getId() + ")");
                    }
                    
                    for ( Request request : requests ) {
                        request.setDeleted(true);
                        request.setDeletedAt(new Date());
                        requestDao.update(request);
                        
                        UserTransaction requestTransaction = userTransactionDao.getByRequest(request.getId());
                        if ( requestTransaction == null ) {
                            log.error("There is no user transaction for the request (requestId: " + request.getId() + ")");
                            continue;
                        } else if ( requestTransaction.isFinalized() ) {
                            //transaction is already finalized
                            continue;
                        }
                        
                        requestTransaction.markAsFinalized(TransactionStatus.EXPIRED);
                        userTransactionDao.update(requestTransaction);
                    }
                    
                    numRows++;
                }
            }
            
            if (numRows > 0) {
                log.info(numRows + " ads marked as expired.");
            }
        } catch (Exception e) {
            log.error("Unable to process ads", e);
        }
    }
}
