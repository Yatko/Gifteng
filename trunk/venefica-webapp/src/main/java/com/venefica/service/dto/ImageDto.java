package com.venefica.service.dto;

import com.venefica.model.Image;
import com.venefica.model.ImageType;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

/**
 * Image data transfer object.
 *
 * @author Sviatoslav Grebenchukov
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class ImageDto extends DtoBase {

    public static final String BASE_PATH = "/images/";
    public static final String NOIMAGE = "noimage";
    
    // out
    private Long id;
    // in
    private ImageType imgType;
    // in
    private byte[] data;
    // out
    private String url;

    // required for JAX-WS
    public ImageDto() {
    }

    public static String imageUrl(Image image) {
        return image != null ? BASE_PATH + "img" + image.getId().toString() : null;
    }

    public ImageDto(Image image) {
        this.id = image.getId();
        this.url = imageUrl(image);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public boolean isValid() {
        return imgType != null && data != null;
    }

    public Image toImage() {
        return new Image(imgType, data);
    }

    public void update(Image image) {
        image.setImgType(imgType);
        image.setData(data);
    }

    public ImageDto(ImageType imgType, byte[] data) {
        this.imgType = imgType;
        this.data = data;
    }

    public ImageType getImgType() {
        return imgType;
    }

    public void setImgType(ImageType imgType) {
        this.imgType = imgType;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
