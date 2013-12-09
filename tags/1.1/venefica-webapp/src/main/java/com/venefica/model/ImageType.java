package com.venefica.model;

public enum ImageType {
    
    JPEG,
    PNG,
    ;
    
    private static final String JPEG_MIME_TYPE = "image/jpeg";
    private static final String PNG_MIME_TYPE = "image/x-png";
    
    private static final String JPEG_FORMAT_NAME = "jpg";
    private static final String PNG_FORMAT_NAME = "png";
    
    public String getMimeType() {
        switch ( this ) {
            case JPEG:
                return JPEG_MIME_TYPE;
            case PNG:
                return PNG_MIME_TYPE;
            default:
                return JPEG_MIME_TYPE;
        }
    }
    
    public String getFormatName() {
        switch ( this ) {
            case JPEG:
                return JPEG_FORMAT_NAME;
            case PNG:
                return PNG_FORMAT_NAME;
            default:
                return JPEG_FORMAT_NAME;
        }
    }
    
}
