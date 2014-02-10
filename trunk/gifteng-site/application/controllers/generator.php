<?php

class Generator extends CI_Controller {
    
    private $initialized = false;
    
    function get_photo($file, $width, $height, $folder, $size) {
        $this->init();
        
        /**
        log_message(ERROR, 'file: ' . $file);
        log_message(ERROR, 'width: ' . $width);
        log_message(ERROR, 'height: ' . $height);
        log_message(ERROR, 'folder: ' . $folder);
        log_message(ERROR, 'size: ' . $size);
        /**/
        
        $this->session->keep_all_flashdata();
        
        if ( $file == null ) {
            return;
        }
        
        if ( $folder == null || empty($folder) ) {
            $source_folder = TEMP_FOLDER;
        } else {
            $source_folder = base64_decode($folder);
        }
        
        $source_image = $source_folder .'/'. $file;
        if ( $size != null && $size != '' && $size > 0 ) {
            $source_image .= '_' . $size;
        }
        
        
        if ( !empty($folder) ) {
            //display image from a local accessible folder (no gd library usage for resize)
            $this->display_image($source_image);
            return;
        }
        
        
        $config['image_library'] = 'gd2';
        $config['quality'] = '90%'; // set quality
        $config['dynamic_output'] = true; // set to true to generate it dynamically
        $config['source_image'] = $source_image;
        $config['maintain_ratio'] = true;
        if ( $width ) {
            $config['width'] = $width;
        }
        if ( $height ) {
            $config['height'] = $height;
        }
        
        $this->image_lib->initialize($config);
        if ( !$this->image_lib->resize() ) {
            echo $this->image_lib->display_errors(); // print error if it fails
        }
        $this->image_lib->clear();
    }
    
    // internal
    
    private function init() {
        if ( !$this->initialized ) {
            $this->load->library('image_lib');
            
            $this->initialized = true;
        }
    }
    
    private function display_image($source_image) {
        $name_gif = 'gif';
        $name_jpeg = 'jpeg';
        $name_png = 'png';
        
        $types = array(1 => $name_gif, 2 => $name_jpeg, 3 => $name_png);
        $vals = @getimagesize($source_image);
        $type = $types[$vals['2']];
        $mime = (isset($type) ? 'image/'.$type : 'image/jpg');

        header("Content-Disposition: filename=".$source_image.";");
        header("Content-Type: ".$mime);
        header('Content-Transfer-Encoding: binary');
        header('Last-Modified: '.gmdate('D, d M Y H:i:s', time()).' GMT');

        switch ($type) {
            case $name_gif:
                $resource = imagecreatefromgif($source_image);
                imagegif($resource);
                break;
            case $name_jpeg:
                $resource = imagecreatefromjpeg($source_image);
                imagejpeg($resource, null, '90');
                break;
            case $name_png:
                $resource = imagecreatefrompng($source_image);
                imagepng($resource);
                break;
        }
    }
}
