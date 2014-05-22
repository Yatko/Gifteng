/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.venefica.job;

import com.venefica.common.AmazonUpload;
import com.venefica.config.Constants;
import com.venefica.dao.AdDao;
import com.venefica.dao.AdDataDao;
import com.venefica.dao.ImageDao;
import com.venefica.model.Ad;
import com.venefica.model.AdData;
import java.util.LinkedList;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 *
 * @author gyuszi
 */
@DisallowConcurrentExecution
public class AdExportJob implements Job {
    
    private static final Log log = LogFactory.getLog(AdExportJob.class);
    
    private final boolean enabled = false;
    
    @Override
    public void execute(JobExecutionContext ctx) throws JobExecutionException {
        if ( !enabled ) {
            return;
        }
        
        AdDao adDao = (AdDao) ctx.getJobDetail().getJobDataMap().get(Constants.AD_DAO);
        AdDataDao adDataDao = (AdDataDao) ctx.getJobDetail().getJobDataMap().get(Constants.AD_DATA_DAO);
        ImageDao imageDao = (ImageDao) ctx.getJobDetail().getJobDataMap().get(Constants.IMAGE_DAO);
        AmazonUpload amazonUpload = (AmazonUpload) ctx.getJobDetail().getJobDataMap().get(Constants.AMAZON_UPLOAD);
        
        List<AdExportStruct> adExportStructs = new LinkedList<AdExportStruct>();
        List<Ad> ads = adDao.get(0, 6);
        for ( int i = 0; i < ads.size(); i++ ) {
            Ad ad = ads.get(i);
            AdData adData = adDataDao.getAdDataByAd(ad.getId());
            
            AdExportStruct struct = new AdExportStruct(i, adData.getTitle(), adData.getDescription(), ad.getId(), adData.getMainImage().getId());
            adExportStructs.add(struct);
        }
        
        //
        //... create XML and upload to amazon
        //
    }
    
    private class AdExportStruct {
        private final int index;
        private final String title;
        private final String description;
        private final Long id;
        private final Long imageId;

        public AdExportStruct(int index, String title, String description, Long id, Long imageId) {
            this.index = index;
            this.title = title;
            this.description = description;
            this.id = id;
            this.imageId = imageId;
        }

        public int getIndex() {
            return index;
        }

        public String getTitle() {
            return title;
        }

        public String getDescription() {
            return description;
        }

        public Long getId() {
            return id;
        }
        
        public Long getImageId() {
            return imageId;
        }
    }
}
