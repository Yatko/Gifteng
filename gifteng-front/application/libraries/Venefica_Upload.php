<?php

class Venefica_Upload extends CI_Upload {

    var $prefix = UPLOAD_FILE_PREFIX;
    
    public $min_width = 0;
    public $min_height = 0;
    
    /**
     * Initialize preferences
     *
     * @param	array
     * @return	void
     */
    public function initialize($config = array()) {
        $defaults = array(
            'max_size' => 0,
            'max_width' => 0,
            'max_height' => 0,
            'min_width' => 0,
            'min_height' => 0,
            'max_filename' => 0,
            'allowed_types' => "",
            'file_temp' => "",
            'file_name' => "",
            'orig_name' => "",
            'file_type' => "",
            'file_size' => "",
            'file_ext' => "",
            'upload_path' => "",
            'overwrite' => FALSE,
            'encrypt_name' => FALSE,
            'is_image' => FALSE,
            'image_width' => '',
            'image_height' => '',
            'image_type' => '',
            'image_size_str' => '',
            'error_msg' => array(),
            'mimes' => array(),
            'remove_spaces' => TRUE,
            'xss_clean' => FALSE,
            'temp_prefix' => "temp_file_",
            'client_name' => ''
        );


        foreach ($defaults as $key => $val) {
            if (isset($config[$key])) {
                $method = 'set_' . $key;
                if (method_exists($this, $method)) {
                    $this->$method($config[$key]);
                } else {
                    $this->$key = $config[$key];
                }
            } else {
                $this->$key = $val;
            }
        }

        // if a file_name was provided in the config, use it instead of the user input
        // supplied file name for all uploads until initialized again
        $this->_file_name_override = $this->file_name;
    }
    
    /**
     * Verify that the image is within the allowed width/height
     *
     * @return	bool
     */
    public function is_allowed_dimensions() {
        if (!$this->is_image()) {
            return TRUE;
        }

        if (function_exists('getimagesize')) {
            $D = @getimagesize($this->file_temp);

            if ($this->max_width > 0 AND $D['0'] > $this->max_width) {
                return FALSE;
            }

            if ($this->max_height > 0 AND $D['1'] > $this->max_height) {
                return FALSE;
            }
            
            if ($this->min_width > 0 AND $D['0'] < $this->min_width) {
                return FALSE;
            }

            if ($this->min_height > 0 AND $D['1'] < $this->min_height) {
                return FALSE;
            }
            
            return TRUE;
        }

        return TRUE;
    }
    
    /**
     * Set the file name
     *
     * This function takes a filename/path as input and looks for the
     * existence of a file with the same name. If found, it will append a
     * number to the end of the filename to avoid overwriting a pre-existing file.
     *
     * @param	string
     * @param	string
     * @return	string
     */
    public function set_filename($path, $filename) {
        if ($this->encrypt_name == TRUE) {
            mt_srand();
            $filename = md5(uniqid(mt_rand())) . $this->file_ext;
        }

        if ( $this->prefix != null && !empty($this->prefix) ) {
            $filename = $this->prefix . $filename;
        }
        
        if (!file_exists($path . $filename)) {
            return $filename;
        }

        $filename = str_replace($this->file_ext, '', $filename);

        $new_filename = '';
        for ($i = 1; $i < 100; $i++) {
            if (!file_exists($path . $filename . $i . $this->file_ext)) {
                $new_filename = $filename . $i . $this->file_ext;
                break;
            }
        }

        if ($new_filename == '') {
            $this->set_error('upload_bad_filename');
            return FALSE;
        } else {
            return $new_filename;
        }
    }
    
    /**
     * Set Minimum Image Width
     *
     * @param  integer
     * @return  void
     */
    public function set_min_width($n) {
        $this->min_width = ((int) $n < 0) ? 0 : (int) $n;
    }

    /**
     * Set Minimum Image Height
     *
     * @param  integer
     * @return  void
     */
    public function set_min_height($n) {
        $this->min_height = ((int) $n < 0) ? 0 : (int) $n;
    }
    
}
