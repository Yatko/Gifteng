/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.venefica.job;

import com.venefica.common.AmazonUpload;
import com.venefica.config.Constants;
import com.venefica.config.FileConfig;
import com.venefica.dao.AdDao;
import com.venefica.dao.AdDataDao;
import com.venefica.dao.ImageDao;
//import com.venefica.job.struct.AdExportStruct;
//import com.venefica.job.struct.AdExportStructList;
import com.venefica.model.Ad;
import com.venefica.model.AdData;
import com.venefica.service.dto.FilterDto;
import java.io.File;
import java.util.LinkedList;
import java.util.List;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.codehaus.jackson.map.AnnotationIntrospector;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.xc.JaxbAnnotationIntrospector;
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
    
    private final boolean enabled = true;
    private final boolean jsonEnabled = true;
    private final boolean xmlEnabled = true;
    private final boolean includeStaffPicks = true;
    private final int numberOfAds = 6;
    private final String folderName = "public"; //amazonaws folder
    private final String jsonFileName = "result.json";
    private final String xmlFileName = "result.xml";
    private final String jsonMimeType = "application/json";
    private final String xmlMimeType = "application/xml";
    
    @Override
    public void execute(JobExecutionContext ctx) throws JobExecutionException {
        if ( !enabled ) {
            return;
        }
        
        AdDao adDao = (AdDao) ctx.getJobDetail().getJobDataMap().get(Constants.AD_DAO);
        AdDataDao adDataDao = (AdDataDao) ctx.getJobDetail().getJobDataMap().get(Constants.AD_DATA_DAO);
        FileConfig fileConfig = (FileConfig) ctx.getJobDetail().getJobDataMap().get(Constants.FILE_CONFIG);
        AmazonUpload amazonUpload = (AmazonUpload) ctx.getJobDetail().getJobDataMap().get(Constants.AMAZON_UPLOAD);
        
        AdExportStructList adExportStructList = createStructList(adDao, adDataDao);
        
        if ( jsonEnabled ) {
            try {
                File jsonFile = serializeJSON(adExportStructList, fileConfig);
                amazonUpload.upload(jsonFile, folderName, jsonFileName, jsonMimeType);
            } catch ( Exception ex ) {
                log.error("Exception thrown when trying to export ads list as json result", ex);
            }
        }
        if ( xmlEnabled ) {
            try {
                File xmlFile = serializeXML(adExportStructList, fileConfig);
                amazonUpload.upload(xmlFile, folderName, xmlFileName, xmlMimeType);
            } catch ( Exception ex ) {
                log.error("Exception thrown when trying to export ads list as xml result", ex);
            }
        }
    }
    
    private AdExportStructList createStructList(AdDao adDao, AdDataDao adDataDao) {
        FilterDto filter = new FilterDto();
        filter.setIncludeHiddenForSearch(false);
        filter.setIncludeStaffPick(includeStaffPicks);
        
        AdExportStructList adExportStructs = new AdExportStructList();
        List<Ad> ads = adDao.get(0, numberOfAds, filter);
        for ( int i = 0; i < ads.size(); i++ ) {
            Ad ad = ads.get(i);
            AdData adData = adDataDao.getAdDataByAd(ad.getId());
            
            AdExportStruct struct = new AdExportStruct(
                    i + 1,
                    adData.getTitle(),
                    adData.getDescription(),
                    ad.getId(),
                    adData.getMainImage().getId()
            );
            adExportStructs.addStruct(struct);
        }
        return adExportStructs;
    }
    
    private File serializeJSON(AdExportStructList adExportStructList, FileConfig fileConfig) throws Exception {
        File file = createTempFile(jsonFileName, fileConfig);
        ObjectMapper mapper = new ObjectMapper();
        AnnotationIntrospector introspector = new JaxbAnnotationIntrospector();
        // make deserializer use JAXB annotations (only)
        mapper.getDeserializationConfig().setAnnotationIntrospector(introspector);
        // make serializer use JAXB annotations (only)
        mapper.getSerializationConfig().setAnnotationIntrospector(introspector);
        mapper.writeValue(file, adExportStructList);
        return file;
    }
    
    private File serializeXML(AdExportStructList adExportStructList, FileConfig fileConfig) throws Exception {
        File file = createTempFile(xmlFileName, fileConfig);
        JAXBContext context = JAXBContext.newInstance(AdExportStructList.class);
        Marshaller marshaller = context.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        marshaller.marshal(adExportStructList, file);
        return file;
    }
    
    private File createTempFile(String fileName, FileConfig fileConfig) {
        File file = new File(getUploadFolder(fileConfig) + fileName);
        file.deleteOnExit();
        return file;
    }
    
    private String getUploadFolder(FileConfig fileConfig) {
        String folder = fileConfig.getPath() + (fileConfig.getPath().endsWith("/") ? "" : "/");
        return folder;
    }
    
    //
    // internal classes
    //
    
    @XmlRootElement(name="gifts")
    @XmlAccessorType(XmlAccessType.FIELD)
    public static class AdExportStructList {

        @XmlElement(name = "gift")
        private List<AdExportStruct> structs;

        public AdExportStructList() {
        }

        public void addStruct(AdExportStruct struct) {
            if (structs == null) {
                structs = new LinkedList<AdExportStruct>();
            }
            structs.add(struct);
        }

        public List<AdExportStruct> getStructs() {
            return structs;
        }

        public void setStructs(List<AdExportStruct> structs) {
            this.structs = structs;
        }
    }
    
    @XmlAccessorType(XmlAccessType.FIELD)
    public static class AdExportStruct {

        @XmlAttribute
        private int index;
        @XmlElement
        private String title;
        @XmlElement
        private String description;
        @XmlAttribute
        private Long id;
        @XmlAttribute
        private Long imageId;

        protected AdExportStruct() {
        }

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

        public void setIndex(int index) {
            this.index = index;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public void setImageId(Long imageId) {
            this.imageId = imageId;
        }
    }
}
