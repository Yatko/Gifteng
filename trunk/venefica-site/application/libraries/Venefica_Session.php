<?php

/**
 * Custom session, that helps fix issue of autoloading libraries before model.
 * In case that you store a model into session you cannot load it back, as it gives
 * __php_incomplete_class. This is a common PHP issue, as storeable classes should
 * be defined (known) before session start. This extender fixes this.
 * 
 * @author gyuszi
 */
class Venefica_Session extends CI_Session {
    
    public function __construct($params = array()) {
        //NOTE:
        // any model that is stored into session should be read before session start
        // if not __php_incomplete_class error will be thrown when unserializing reference
        $CI =& get_instance();
        $CI->load->model("image_model");
        $CI->load->model("address_model");
        $CI->load->model("adstatistics_model");
        $CI->load->model("userstatistics_model");
        $CI->load->model("ad_model");
        $CI->load->model("user_model");
        
        parent::__construct($params);
    }
    
    public function sess_id() {
        return $this->userdata('session_id');
    }
    
    /**
     * Fetch a specific flashdata item from the session array. First tries to extract
     * the 'old' data, and if not found tries with the 'new'.
     * 
     * @param string $key
     * @return object
     */
    public function flashdata($key) {
        $old_key = $this->flashdata_key.':old:'.$key;
        $new_key = $this->flashdata_key.':new:'.$key;
        
        $old_value = $this->userdata($old_key);
        $new_value = $this->userdata($new_key);
        
        //log_message('ERROR', 'OLD (key: '.$old_key.', value: '.print_r($old_value, true).')');
        //log_message('ERROR', 'NEW (key: '.$new_key.', value: '.print_r($new_value, true).')');
        
        if ( $new_value != null && $new_value != FALSE && !empty($new_value) ) {
            return $new_value;
        } else {
            return $old_value;
        }
        
        /**
        if ( $old_value != null && $old_value != FALSE && !empty($old_value) ) {
            return $old_value;
        } else {
            return $new_value;
        }
        /**/
    }
    
    /**
     * Keeps all the flash data present in the session for the next request.
     */
    public function keep_all_flashdata() {
        $all_userdata = parent::all_userdata();
        foreach ( $all_userdata as $key => $value ) {
            if ( startsWith($key, $this->flashdata_key.':new:') || startsWith($key, $this->flashdata_key.':old:') ) {
                //flashdata key
                $parts = explode(':', $key);
                $var = $parts[2];
                parent::keep_flashdata($var);
            }
        }
    }
}

?>
