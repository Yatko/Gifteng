<?php

class Venefica_Upload extends CI_Upload {

    var $prefix = UPLOAD_FILE_PREFIX;
    
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
}
