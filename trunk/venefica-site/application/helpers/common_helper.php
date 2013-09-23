<?php


define('BASE_PATH', base_url().'assets/'.TEMPLATES.'/');
define('JS_PATH',   BASE_PATH.'js/');
define('CSS_PATH',  BASE_PATH.'css/');
define('IMG_PATH',  BASE_PATH.'img/');

//define('IMAGE_URL_PREFIX',  base_url().'get_photo/'); //live server
define('IMAGE_URL_PREFIX',  'http://veneficalabs.com/gifteng/'.'get_photo/'); //local dev server

define('DEFAULT_USER_URL',  BASE_PATH.'temp-sample/ge-no-profile-picture.png');
define('DEFAULT_AD_URL',    BASE_PATH.'temp-sample/gifteng.png');


if ( ! function_exists('get_image_url')) {
    function get_image_url($url) {
        //return IMAGE_SERVER_URL . $url;
        
        $img_num = str_replace("/images/img", "", $url);
        return IMAGE_URL_PREFIX . $img_num . '/0/0/cache';
    }
}


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

if ( !function_exists('clear_cache') ) {
    //References:
    //http://stackoverflow.com/questions/8860953/codeigniter-session-problems
    //http://stackoverflow.com/questions/4781737/how-to-avoid-browser-cache-using-codeigniter
    //http://stackoverflow.com/questions/5429386/browser-cache-issue-in-codeigniter
    function clear_cache() {
        $CI =& get_instance();
        
        $CI->output->set_header("HTTP/1.0 200 OK");
        $CI->output->set_header("HTTP/1.1 200 OK");
        $CI->output->set_header('Last-Modified: Sat, 26 Jul 1997 05:00:00 GMT'); //date in the  past
        $CI->output->set_header("Cache-Control: no-store, no-cache, must-revalidate, no-transform, max-age=0, post-check=0, pre-check=0");
        $CI->output->set_header("Pragma: no-cache");
    }
}


// making a safer world

//if ( ! function_exists('jsEscape')) {
//    function jsEscape($str) { 
//        return addcslashes($str, "\\\'\"&\n\r<>"); 
//    }
//}

if ( ! function_exists('safe_content')) {
    function safe_content($str) {
        if ( $str == null ) {
            return "";
        }
        
        $str = trim($str);
        $str = trim(strip_tags($str));
        $str = str_replace("\n\n", "\n", $str);
        $str = str_replace("\r\r", "\r", $str);
        $str = str_replace("\r\n\r\n", "\r\n", $str);
        $str = str_replace("\n\r\n\r", "\n\r", $str);
        $str = str_replace("\n", "<br />", $str);
        return $str;
    }
}
if ( ! function_exists('safe_parameter')) {
    function safe_parameter($str) {
        $str = safe_content($str);
        //$str = jsEscape($str);
        $str = str_replace("\"", "&quot;", $str); //&#34;
        $str = str_replace("'", "&#39;", $str);
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
