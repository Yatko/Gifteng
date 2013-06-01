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
        get_instance()->load->model("image_model");
        get_instance()->load->model('address_model');
        get_instance()->load->model("user_model");
        
        parent::__construct($params);
    }
    
    /**
     * Fetch a specific flashdata item from the session array. First tries to extract
     * the 'old' data, and if not found tries with the 'new'.
     * 
     * @param string $key
     * @return object
     */
    public function flashdata($key) {
        $value = parent::flashdata($key);
        if ( $value != null && $value != FALSE && !empty($value) ) {
            return $value;
        }
        
        $flashdata_key = $this->flashdata_key.':new:'.$key;
        return $this->userdata($flashdata_key);
    }
}

?>
