<?php


define('BASE_PATH', base_url().'assets/'.TEMPLATES.'/');
define('JS_PATH', BASE_PATH.'js/');
define('CSS_PATH', BASE_PATH.'css/');
define('IMG_PATH', BASE_PATH.'img/');

define('DEFAULT_USER_URL', BASE_PATH.'temp-sample/ge-no-profile-picture.png');
define('DEFAULT_AD_URL', BASE_PATH.'temp-sample/gifteng.png');



/**
 * Common helper.
 * 
 * Various useful functions.
 */

if ( ! function_exists('display')) {
    function display($value, $expected) {
        $display = isset($value) && $value == $expected ? "block" : "none";
        return " style=\"display: $display;\"";
    }
}

if ( ! function_exists('respond_ajax')) {
    function respond_ajax($status, $result) {
        $CI =& get_instance();
        
        $data = array();
        $data['obj'] = array($status => $result);
        
        $CI->load->view('json', $data);
    }
}

if ( ! function_exists('respond_ajax_array')) {
    function respond_ajax_array($responseArray) {
        $CI =& get_instance();
        
        $obj = array();
        foreach ( $responseArray as $status => $result ) {
            $obj[$status] = $result;
        }
        
        $data['obj'] = $obj;
        $CI->load->view('json', $data);
    }
}

if ( ! function_exists('safe_redirect')) {
    /**
     * Redirect page by copying the query string as well.
     * @param string $url
     */
    function safe_redirect($url = '') {
        $CI =& get_instance();
        $qs = $CI->input->server('QUERY_STRING');
        redirect($url . (trim($qs) == '' ? '' : '?'.$qs));
    }
}


// making a safer world

if ( ! function_exists('safe_content')) {
    function safe_content($str) {
        $str = strip_tags($str);
        return $str;
    }
}
if ( ! function_exists('safe_parameter')) {
    function safe_parameter($str) {
        $str = strip_slashes($str);
        return $str;
    }
}

if ( ! function_exists('is_empty')) {
    function is_empty($obj) {
        if ( $obj == null ) {
            return true;
        }
        if ( is_array($obj) ) {
            if ( sizeof($obj) == 0 ) {
                return true;
            }
            return false;
        }
        if ( is_object($obj) ) {
            $object_vars = get_object_vars($obj);
            if ( $object_vars == null || empty($object_vars) ) {
                return true;
            }
            return false;
        }
        return empty($obj);
    }
}

// object and properties related helpers

if ( ! function_exists('getField')) {
    function getField($obj, $fieldName) {
        if ( $obj == null || $fieldName == null ) {
            return null;
        }
        if ( property_exists($obj, $fieldName) ) {
            return $obj->$fieldName;
        }
        return null;
    }
}

if ( ! function_exists('hasField')) {
    function hasField($obj, $fieldName) {
        if ( $obj == null || $fieldName == null ) {
            return FALSE;
        }
        return property_exists($obj, $fieldName);
    }
}

// associative array related helpers

if ( ! function_exists('getElement')) {
    function getElement($array, $elem) {
        if ( $array == null || $elem == null ) {
            return null;
        }
        if (array_key_exists($elem, $array) ) {
            return $array[$elem];
        }
        return null;
    }
}

if ( ! function_exists('hasElement')) {
    function hasElement($array, $elem) {
        if ( $array == null || $elem == null ) {
            return FALSE;
        }
        return array_key_exists($elem, $array);
    }
}

// various converters

if ( ! function_exists('readFileAsString')) {
    function readFileAsString($file) {
        $content = fread(fopen($file, "r"), filesize($file));
        return $content;
    }
}


// various string related helpers

if ( ! function_exists('startsWith')) {
    function startsWith($haystack, $needle) {
        return !strncmp($haystack, $needle, strlen($needle));
    }
}
if ( ! function_exists('endsWith')) {
    function endsWith($haystack, $needle) {
        $length = strlen($needle);
        if ($length == 0) {
            return true;
        }
        return (substr($haystack, -$length) === $needle);
    }
}
