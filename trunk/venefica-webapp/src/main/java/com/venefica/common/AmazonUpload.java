/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.venefica.common;

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ListObjectsRequest;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.venefica.config.FileConfig;
import com.venefica.model.ImageModelType;
import com.venefica.model.ImageType;
import java.io.File;
import java.io.IOException;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author gyuszi
 */
@Named
public class AmazonUpload {
    
    private static final Log logger = LogFactory.getLog(EmailSender.class);
    
    @Inject
    private FileConfig fileConfig;
    
    private AmazonS3 client;
    
    @PostConstruct
    public void init() {
        if ( !fileConfig.isAmazonEnabled() ) {
            return;
        }
        
        client = new AmazonS3Client(new BasicAWSCredentials(fileConfig.getAmazonAccessKeyID(), fileConfig.getAmazonSecretAccessKey()));
    }
    
    public void upload(List<File> files, ImageModelType modelType, ImageType type) throws IOException {
        if ( !fileConfig.isAmazonEnabled() ) {
            return;
        } else if ( files == null || files.isEmpty() ) {
            return;
        }
        
        for ( File file : files ) {
            String key = modelType.getFolderName() + "/" + file.getName();
            try {
                ObjectMetadata metadata = new ObjectMetadata();
                metadata.setContentType(type.getMimeType());
                PutObjectRequest objectRequest = new PutObjectRequest(fileConfig.getAmazonBucket(), key, file);
                objectRequest.setCannedAcl(CannedAccessControlList.PublicRead);
                objectRequest.setMetadata(metadata);
                client.putObject(objectRequest);
            } catch ( Exception ex ) {
                logger.error("Exception thrown when trying to transfer files to amazon S3 (key: " + key + ")", ex);
                throw new IOException("Exception thrown when trying to transfer files to amazon S3 (key: " + key + ")", ex);
            }
        }
    }
    
    public void download(File file, ImageModelType modelType) throws IOException {
        if ( !fileConfig.isAmazonEnabled() ) {
            return;
        }
        
        String key = modelType.getFolderName() + "/" + file.getName();
        try {
            GetObjectRequest objectRequest = new GetObjectRequest(fileConfig.getAmazonBucket(), key);
            client.getObject(objectRequest, file);
        } catch ( Exception ex ) {
            logger.error("Exception thrown when trying to download from amazon S3 (key: " + key + ")", ex);
            throw new IOException("Exception thrown when trying to download from amazon S3 (key: " + key + ")", ex);
        }
    }
    
    public String getFolderInfo(String prefix) {
        if ( !fileConfig.isAmazonEnabled() ) {
            return "";
        }
        
        String bucketName = fileConfig.getAmazonBucket();
        String thisLevel;
        ListObjectsRequest request;

        if ( prefix != null && !prefix.isEmpty() ) {
            request = new ListObjectsRequest()
                    .withBucketName(bucketName)
                    .withDelimiter("/")
                    .withMaxKeys(50000)
                    .withPrefix(prefix);
        } else {
            request = new ListObjectsRequest()
                    .withBucketName(bucketName)
                    .withDelimiter("/")
                    .withMaxKeys(50000);
        }

        ObjectListing objResponse;
        StringBuilder sb = new StringBuilder();
        int objCount = 0;
        
        do {
            objResponse = client.listObjects(request);
            if ( !objResponse.getCommonPrefixes().isEmpty() ) {
                for ( String commonPrefix : objResponse.getCommonPrefixes() ) {
                    sb.append(getFolderInfo(commonPrefix));
                    sb.append("\n");
                }
            }
            
            request.setMarker(objResponse.getNextMarker());
            objCount = objCount + objResponse.getObjectSummaries().size();
        } while ( objResponse.isTruncated() );
        
        if ( prefix != null && !prefix.isEmpty() ) {
            objCount = objCount - 1;
            thisLevel = prefix.replace("/", "->");
        } else {
            thisLevel = "Root";
        }
        
        sb.append("Files at ").append(thisLevel).append(" ").append(objCount);
        sb.append("\n");
        return sb.toString();
    }
}
