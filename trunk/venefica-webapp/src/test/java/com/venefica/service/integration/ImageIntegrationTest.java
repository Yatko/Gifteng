/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.venefica.service.integration;

import com.venefica.common.AmazonUpload;
import com.venefica.common.DumpErrorTestExecutionListener;
import com.venefica.common.FileUpload;
import com.venefica.common.ImageUtils;
import com.venefica.model.ImageModelType;
import com.venefica.model.ImageType;
import java.io.File;
import java.io.IOException;
import java.util.List;
import javax.inject.Inject;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 *
 * @author gyuszi
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "/IntegrationTest-context.xml")
@TestExecutionListeners({DumpErrorTestExecutionListener.class})
@Category(IntegrationTestMarker.class)
public class ImageIntegrationTest {
    
    @Inject
    private FileUpload fileUpload;
    @Inject
    private ImageUtils imageUtils;
    @Inject
    private AmazonUpload amazonUpload;
    
    @Test
    @Ignore(value = "Image resize ignored to speed up unit tests")
    public void testImageResizeAndUpload() throws IOException {
        Long[] imageIds = new Long[] {893L, 894L, 901L, 903L, 906L, 911L, 912L, 914L, 920L, 922L, 923L, 924L, 925L, 927L, 929L, 930L, 931L, 933L, 934L, 935L};
        ImageModelType modelType = ImageModelType.AD;
        ImageType type = ImageType.JPEG;
        for ( Long imageId : imageIds ) {
            byte[] data = fileUpload.getData(imageId, modelType);
            List<File> files = imageUtils.save(imageId, data, modelType, type);
            amazonUpload.transfer(files, modelType, type);
        }
    }
    
    @Test
    public void testInfo() {
        //String userInfo = amazonUpload.getFolderInfo(ImageModelType.USER.getFolderName());
        //String adInfo = amazonUpload.getFolderInfo(ImageModelType.AD.getFolderName());
        String allInfo = amazonUpload.getFolderInfo(null);
        
        //System.out.println("USER info:\n" + userInfo);
        //System.out.println("AD info:\n" + adInfo);
        System.out.println("Info:\n" + allInfo);
    }
}
