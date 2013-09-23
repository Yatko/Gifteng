<?php

class Generator extends CI_Controller {
    
    private $initialized = false;
    
    function get_photo($file, $width, $height, $folder) {
        $this->init();
        
        //log_message(ERROR, 'file:     ' . $file);
        //log_message(ERROR, 'width:    ' . $width);
        //log_message(ERROR, 'height:   ' . $height);
        //log_message(ERROR, 'folder:   ' . $folder);
        
        $this->session->keep_all_flashdata();
        
        if ( !isLogged() ) {
            return;
        } else if ( $file == null ) {
            return;
        }
        
        if ( $folder == null || empty($folder) ) {
            $folder = TEMP_FOLDER;
        } else {
            $folder = './' . $folder;
        }
        
        //$this->image_lib->clear();
        $config['image_library'] = 'gd2';
        $config['quality'] = '90%'; // set quality
        $config['dynamic_output'] = true; // set to true to generate it dynamically
        $config['source_image'] = $folder .'/'. $file;
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
}
