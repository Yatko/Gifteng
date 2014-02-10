<?php

if (!defined('BASEPATH'))
    exit('No direct script access allowed');

/**
 * Code Igniter
 *
 * An open source application development framework for PHP 4.3.2 or newer
 *
 * @package        CodeIgniter
 * @author        Dariusz Debowczyk
 * @copyright    Copyright (c) 2006, D.Debowczyk
 * @license        http://www.codeignitor.com/user_guide/license.html 
 * @link        http://www.codeigniter.com
 * @since        Version 1.0
 * @filesource
 */
// ------------------------------------------------------------------------

/**
 * Session class using native PHP session features and hardened against session fixation.
 * 
 * @package        CodeIgniter
 * @subpackage    Libraries
 * @category    Sessions
 * @author        Dariusz Debowczyk
 * @link        http://www.codeigniter.com/user_guide/libraries/sessions.html
 */
class Venefica_Session extends CI_Session {
    
    var $session_id_ttl = 360; // session id time to live (TTL) in seconds
    var $flash_key = 'flash'; // prefix for "flash" variables (eg. flash:new:message)

    public function Venefica_Session() {
        log_message(DEBUG, "Native_session Class Initialized");

        //NOTE:
        // any model that is stored into session should be read before session start
        // if not __php_incomplete_class error will be thrown when unserializing reference
        $CI = & get_instance();
        $CI->load->model("image_model");
        $CI->load->model("address_model");
        $CI->load->model("adstatistics_model");
        $CI->load->model("userstatistics_model");
        $CI->load->model("ad_model");
        $CI->load->model("user_model");
        $CI->load->model("request_model");
        $CI->load->model("filter_model");

        $this->_sess_run();
    }

    public function all_userdata() {
        return $_SESSION;
    }
    
    /**
     * Destroys the session and erases session storage
     */
    public function sess_destroy() {
        //unset($_SESSION);
        session_unset();
        if (isset($_COOKIE[session_name()])) {
            setcookie(session_name(), '', time() - 42000, '/');
        }
        
        if ( $this->session_is_active() ) {
            session_destroy();
        }
    }

    /**
     * @return bool
     */
    private function session_is_active() {
        $setting = 'session.use_trans_sid';
        $current = ini_get($setting);
        if (FALSE === $current) {
            throw new UnexpectedValueException(sprintf('Setting %s does not exists.', $setting));
        }
        $testate = "mix$current$current";
        $old = @ini_set($setting, $testate);
        $peek = @ini_set($setting, $current);
        $result = $peek === $current || $peek === FALSE;
        return $result;
    }
    
    /**
     * Reads given session attribute value
     */
    public function userdata($item) {
        if ($item == 'session_id') { //added for backward-compatibility
            return session_id();
        } else {
            return (!isset($_SESSION[$item])) ? false : $_SESSION[$item];
        }
    }

    /**
     * Sets session attributes to the given values
     */
    public function set_userdata($newdata = array(), $newval = '') {
        if (is_string($newdata)) {
            $newdata = array($newdata => $newval);
        }

        if (count($newdata) > 0) {
            foreach ($newdata as $key => $val) {
                $_SESSION[$key] = $val;
            }
        }
    }

    /**
     * Erases given session attributes
     */
    public function unset_userdata($newdata = array()) {
        if (is_string($newdata)) {
            $newdata = array($newdata => '');
        }

        if (count($newdata) > 0) {
            foreach ($newdata as $key => $val) {
                unset($_SESSION[$key]);
            }
        }
    }

    /**
     * Sets "flash" data which will be available only in next request (then it will
     * be deleted from session). You can use it to implement "Save succeeded" messages
     * after redirect.
     */
    public function set_flashdata($key, $value) {
        $flash_key = $this->flash_key . ':new:' . $key;
        $this->set_userdata($flash_key, $value);
    }

    /**
     * Keeps existing "flash" data available to next request.
     */
    public function keep_flashdata($key) {
        $old_flash_key = $this->flash_key . ':old:' . $key;
        $value = $this->userdata($old_flash_key);

        $new_flash_key = $this->flash_key . ':new:' . $key;
        $this->set_userdata($new_flash_key, $value);
    }

    /**
     * Returns "flash" data for the given key.
     */
    public function flashdata($key) {

        $old_key = $this->flash_key . ':old:' . $key;
        $new_key = $this->flash_key . ':new:' . $key;

        $old_value = $this->userdata($old_key);
        $new_value = $this->userdata($new_key);

        //log_message('ERROR', 'OLD (key: '.$old_key.', value: '.print_r($old_value, true).')');
        //log_message('ERROR', 'NEW (key: '.$new_key.', value: '.print_r($new_value, true).')');

        if ($new_value != null && $new_value != FALSE && !empty($new_value)) {
            return $new_value;
        } else {
            return $old_value;
        }
        
//        $flash_key = $this->flash_key.':old:'.$key;
//        return $this->userdata($flash_key);
    }

    /**
     * Keeps all the flash data present in the session for the next request.
     */
    public function keep_all_flashdata() {
        $all_userdata = $this->all_userdata();
        foreach ($all_userdata as $key => $value) {
            if (startsWith($key, $this->flash_key . ':new:') || startsWith($key, $this->flash_key . ':old:')) {
                //flashdata key
                $parts = explode(':', $key);
                $var = $parts[2];
                $this->keep_flashdata($var);
            }
        }
    }

    /**
     * Regenerates session id
     */
    private function regenerate_id() {
        // copy old session data, including its id
        $old_session_id = session_id();
        $old_session_data = $_SESSION;

        // regenerate session id and store it
        session_regenerate_id();
        $new_session_id = session_id();

        // switch to the old session and destroy its storage
        session_id($old_session_id);
        session_destroy();

        // switch back to the new session id and send the cookie
        session_id($new_session_id);
        session_start();

        // restore the old session data into the new session
        $_SESSION = $old_session_data;

        // update the session creation time
        $_SESSION['regenerated'] = time();

        // session_write_close() patch based on this thread
        // http://www.codeigniter.com/forums/viewthread/1624/
        // there is a question mark ?? as to side affects
        // end the current session and store session data.
        session_write_close();
    }
    
    /**
     * Starts up the session system for current request
     */
    private function _sess_run() {
        session_start();

        // check if session id needs regeneration
        if ($this->_session_id_expired()) {
            // regenerate session id (session data stays the
            // same, but old session storage is destroyed)
            $this->regenerate_id();
        }

        // delete old flashdata (from last request)
        $this->_flashdata_sweep();

        // mark all new flashdata as old (data will be deleted before next request)
        $this->_flashdata_mark();
    }
    
    /**
     * Checks if session has expired
     */
    private function _session_id_expired() {
        if (!isset($_SESSION['regenerated'])) {
            $_SESSION['regenerated'] = time();
            return false;
        }

        $expiry_time = time() - $this->session_id_ttl;

        if ($_SESSION['regenerated'] <= $expiry_time) {
            return true;
        }

        return false;
    }
    
    /**
     * Marks "flash" session attributes as 'old'
     */
    public function _flashdata_mark() {
        foreach ($_SESSION as $name => $value) {
            $parts = explode(':new:', $name);
            if (is_array($parts) && count($parts) == 2) {
                $new_name = $this->flash_key . ':old:' . $parts[1];
                $this->set_userdata($new_name, $value);
                $this->unset_userdata($name);
            }
        }
    }

    /**
     * Removes "flash" session marked as 'old'
     */
    public function _flashdata_sweep() {
        foreach ($_SESSION as $name => $value) {
            $parts = explode(':old:', $name);
            if (is_array($parts) && count($parts) == 2 && $parts[0] == $this->flash_key) {
                $this->unset_userdata($name);
            }
        }
    }

}

?>
