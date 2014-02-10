<?php

if (!defined('BASEPATH')) exit('No direct script access allowed');

/**
 * Source urls:
 * https://github.com/Areson/CodeIgniter/blob/245bef58eea045b1fcf0584049dc430bcb8abc20/system/libraries/Session.php
 * http://stackoverflow.com/questions/7980193/codeigniter-session-bugging-out-with-ajax-calls
 * 
 * CodeIgniter
 *
 * An open source application development framework for PHP 5.2.4 or newer
 *
 * NOTICE OF LICENSE
 *
 * Licensed under the Open Software License version 3.0
 *
 * This source file is subject to the Open Software License (OSL 3.0) that is
 * bundled with this package in the files license.txt / license.rst.  It is
 * also available through the world wide web at this URL:
 * http://opensource.org/licenses/OSL-3.0
 * If you did not receive a copy of the license and are unable to obtain it
 * through the world wide web, please send an email to
 * licensing@ellislab.com so we can send you a copy immediately.
 *
 * @package		CodeIgniter
 * @author		EllisLab Dev Team
 * @copyright	Copyright (c) 2008 - 2012, EllisLab, Inc. (http://ellislab.com/)
 * @license		http://opensource.org/licenses/OSL-3.0 Open Software License (OSL 3.0)
 * @link		http://codeigniter.com
 * @since		Version 1.0
 * @filesource
 */

/**
 * Session Class
 *
 * @package		CodeIgniter
 * @subpackage	Libraries
 * @category	Sessions
 * @author		EllisLab Dev Team
 * @link		http://codeigniter.com/user_guide/libraries/sessions.html
 */
class Venefica_Session extends CI_Session {

    public $sess_encrypt_cookie = FALSE;
    public $sess_use_database = FALSE;
    public $sess_table_name = '';
    public $sess_expiration = 7200;
    public $sess_expire_on_close = FALSE;
    public $sess_match_ip = FALSE;
    public $sess_match_useragent = TRUE;
    public $sess_cookie_name = 'ci_session';
    public $sess_use_multisessions = FALSE;
    public $sess_multisession_expiration = 10;
    public $cookie_prefix = '';
    public $cookie_path = '';
    public $cookie_domain = '';
    public $cookie_secure = FALSE;
    public $cookie_httponly = FALSE;
    public $sess_time_to_update = 300;
    public $encryption_key = '';
    public $flashdata_key = 'flash';
    public $time_reference = 'time';
    public $gc_probability = 5;
    public $userdata = array();
    public $CI;
    public $now;
    private $prevent_update = FALSE;
    private $multisession_expired = FALSE;

    /**
     * Session Constructor
     *
     * The constructor runs the session routines automatically
     * whenever the class is instantiated.
     */
    public function __construct($params = array()) {
        log_message('debug', 'Session Class Initialized');




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




        // Set the super object to a local variable for use throughout the class
        $this->CI = & get_instance();

        // Set all the session preferences, which can either be set
        // manually via the $params array above or via the config file
        foreach (array('sess_encrypt_cookie', 'sess_use_database', 'sess_table_name', 'sess_expiration', 'sess_expire_on_close', 'sess_match_ip', 'sess_match_useragent', 'sess_cookie_name', 'cookie_path', 'cookie_domain', 'cookie_secure', 'cookie_httponly', 'sess_time_to_update', 'time_reference', 'cookie_prefix', 'encryption_key', 'sess_use_multisessions', 'sess_multisession_expiration') as $key) {
            $this->$key = (isset($params[$key])) ? $params[$key] : $this->CI->config->item($key);
        }

        if ($this->encryption_key == '') {
            show_error('In order to use the Session class you are required to set an encryption key in your config file.');
        }

        // Load the string helper so we can use the strip_slashes() function
        $this->CI->load->helper('string');

        // Do we need encryption? If so, load the encryption class
        if ($this->sess_encrypt_cookie == TRUE) {
            $this->CI->load->library('encrypt');
        }

        // Are we using a database? If so, load it
        if ($this->sess_use_database === TRUE && $this->sess_table_name != '') {
            $this->CI->load->database();
        }

        // Set the "now" time. Can either be GMT or server time, based on the
        // config prefs. We use this to set the "last activity" time
        $this->now = $this->_get_time();

        // Set the session length. If the session expiration is
        // set to zero we'll set the expiration two years from now.
        if ($this->sess_expiration == 0) {
            $this->sess_expiration = (60 * 60 * 24 * 365 * 2);
        }

        // Set the cookie name
        $this->sess_cookie_name = $this->cookie_prefix . $this->sess_cookie_name;

        // Run the Session routine. If a session doesn't exist we'll
        // create a new one. If it does, we'll update it.
        if (!$this->sess_read()) {
            $this->sess_create();
        } else {
            $this->sess_update();
        }

        // Delete 'old' flashdata (from last request)
        $this->_flashdata_sweep();

        // Mark all new flashdata as old (data will be deleted before next request)
        $this->_flashdata_mark();

        // Delete expired sessions if necessary
        $this->_sess_gc();

        log_message('debug', 'Session routines successfully run');
    }

    // --------------------------------------------------------------------

    /**
     * Fetch the current session data if it exists
     *
     * @return	bool
     */
    public function sess_read() {
        // Fetch the cookie
        $session = $this->CI->input->cookie($this->sess_cookie_name);

        // No cookie?  Goodbye cruel world!...
        if ($session === FALSE) {
            log_message('debug', 'A session cookie was not found.');
            return FALSE;
        }

        // Decrypt the cookie data
        if ($this->sess_encrypt_cookie == TRUE) {
            $session = $this->CI->encrypt->decode($session);
        } else {
            // encryption was not used, so we need to check the md5 hash
            $hash = substr($session, strlen($session) - 32); // get last 32 chars
            $session = substr($session, 0, strlen($session) - 32);

            // Does the md5 hash match? This is to prevent manipulation of session data in userspace
            if ($hash !== md5($session . $this->encryption_key)) {
                log_message('error', 'The session cookie data did not match what was expected. This could be a possible hacking attempt.');
                $this->sess_destroy();
                return FALSE;
            }
        }

        // Unserialize the session array
        $session = $this->_unserialize($session);

        // Is the session data we unserialized an array with the correct format?
        if (!is_array($session) OR !isset($session['session_id'], $session['ip_address'], $session['user_agent'], $session['last_activity'])) {
            $this->sess_destroy();
            return FALSE;
        }

        // Is the session current?
        if (($session['last_activity'] + $this->sess_expiration) < $this->now) {
            $this->sess_destroy();
            return FALSE;
        }

        // Does the IP match?
        if ($this->sess_match_ip == TRUE && $session['ip_address'] !== $this->CI->input->ip_address()) {
            $this->sess_destroy();
            return FALSE;
        }

        // Does the User Agent Match?
        if ($this->sess_match_useragent == TRUE && trim($session['user_agent']) !== trim(substr($this->CI->input->user_agent(), 0, 120))) {
            $this->sess_destroy();
            return FALSE;
        }

        // Is there a corresponding session in the DB?
        if ($this->sess_use_database === TRUE) {
            // Are we using multi-sessions? If so, grab a lock on the session
            if ($this->sess_use_multisessions) {
                // Load the php session based on the current session id.
                $this->_get_multi_session($session['session_id']);
            }

            $this->CI->db->where('session_id', $session['session_id']);

            if ($this->sess_match_ip == TRUE) {
                $this->CI->db->where('ip_address', $session['ip_address']);
            }

            if ($this->sess_match_useragent == TRUE) {
                $this->CI->db->where('user_agent', $session['user_agent']);
            }

            $query = $this->CI->db->limit(1)->get($this->sess_table_name);

            // No result? Kill it!
            if ($query->num_rows() === 0) {
                $this->sess_destroy();

                // Kill the multi-session we started
                if ($this->sess_use_multisessions) {
                    session_destroy();
                }

                return FALSE;
            }

            // Is there custom data?  If so, add it to the main session array
            $row = $query->row();
            if (isset($row->user_data) && $row->user_data != '') {
                $custom_data = $this->_unserialize($row->user_data);

                if (is_array($custom_data)) {
                    foreach ($custom_data as $key => $val) {
                        $session[$key] = $val;
                    }
                }
            }

            // Are we in a multi-session scenario? If so, set whether the current
            // session id is allowed to be updated.
            if ($this->sess_use_multisessions) {
                $this->prevent_update = isset($row->prevent_update) ? $row->prevent_update : NULL;

                // Check to see if this session doesn't exist (previously destroyed) 
                //  If so, kill it.
                if (is_null($this->prevent_update)) {
                    $this->sess_destroy();

                    // Destroy the php session
                    session_destroy();
                    return FALSE;
                }
            }
        }

        // Session is valid!
        $this->userdata = $session;
        unset($session);

        return TRUE;
    }

    // --------------------------------------------------------------------

    /**
     * Write the session data
     *
     * @return	void
     */
    public function sess_write() {
        //Is this an expired multisession? If so, prevent updates
        if ($this->multisession_expired()) {
            return;
        }

        // Are we saving custom data to the DB?  If not, all we do is update the cookie
        if ($this->sess_use_database === FALSE) {
            $this->_set_cookie();
            return;
        }

        // If we have enabled multi-session and have one that
        // can no longer be updated, prevent the session write.
        if ($this->sess_use_multisessions && $this->prevent_update) {
            return;
        }

        // set the custom userdata, the session data we will set in a second
        $custom_userdata = $this->userdata;
        $cookie_userdata = array();

        // Before continuing, we need to determine if there is any custom data to deal with.
        // Let's determine this by removing the default indexes to see if there's anything left in the array
        // and set the session data while we're at it
        foreach (array('session_id', 'ip_address', 'user_agent', 'last_activity') as $val) {
            unset($custom_userdata[$val]);
            $cookie_userdata[$val] = $this->userdata[$val];
        }

        // Did we find any custom data? If not, we turn the empty array into a string
        // since there's no reason to serialize and store an empty array in the DB
        if (count($custom_userdata) === 0) {
            $custom_userdata = '';
        } else {
            // Serialize the custom data array so we can store it
            $custom_userdata = $this->_serialize($custom_userdata);
        }

        // Run the update query
        $this->CI->db->where('session_id', $this->userdata['session_id']);
        $this->CI->db->update($this->sess_table_name, array('last_activity' => $this->userdata['last_activity'], 'user_data' => $custom_userdata));

        // Write the cookie. Notice that we manually pass the cookie data array to the
        // _set_cookie() function. Normally that function will store $this->userdata, but
        // in this case that array contains custom data, which we do not want in the cookie.
        $this->_set_cookie($cookie_userdata);
    }

    // --------------------------------------------------------------------

    /**
     * Create a new session
     *
     * @return	void
     */
    public function sess_create() {
        $sessid = '';
        do {
            $sessid .= mt_rand(0, mt_getrandmax());
        } while (strlen($sessid) < 32);

        // To make the session ID even more secure we'll combine it with the user's IP
        $sessid .= $this->CI->input->ip_address();

        $this->userdata = array(
            'session_id' => md5(uniqid($sessid, TRUE)),
            'ip_address' => $this->CI->input->ip_address(),
            'user_agent' => substr($this->CI->input->user_agent(), 0, 120),
            'last_activity' => $this->now,
            'prevent_update' => 0,
            'user_data' => ''
        );

        // Save the data to the DB if needed
        if ($this->sess_use_database === TRUE) {
            $this->CI->db->query($this->CI->db->insert_string($this->sess_table_name, $this->userdata));

            //Are we using multiple sessions?
            if ($this->sess_use_multisessions) {
                /* Setup the php session to store information on whether
                 * or not the session can be updated
                 */
                $this->_get_multi_session($this->userdata['session_id']);
                $this->prevent_update = FALSE;
                $this->multisession_expired = FALSE;

                unset($this->userdata['prevent_update']);

                session_write_close();
            }
        }

        // Write the cookie
        $this->_set_cookie();
    }

    // --------------------------------------------------------------------

    /**
     * Update an existing session
     *
     * @return	void
     */
    public function sess_update() {
        // We only update the session every five minutes by default
        if (($this->userdata['last_activity'] + $this->sess_time_to_update) >= $this->now) {
            if ($this->sess_use_database && $this->sess_use_multisessions) {
                session_write_close();
            }

            return;
        }

        // Check if this session is no longer allowed to update and has exired.
        //  If so, flag it as expired so we can take action as appropriate.
        if ($this->prevent_update && ($this->userdata['last_activity'] + $this->sess_multisession_expiration) < $this->now) {
            $this->multisession_expired = TRUE;

            session_write_close();
            return;
        }

        // We only allow sessions to update if they are allowed
        if ($this->sess_use_database && $this->sess_use_multisessions && $this->prevent_update) {
            session_write_close();
            return;
        }

        // _set_cookie() will handle this for us if we aren't using database sessions
        // by pushing all userdata to the cookie.
        $cookie_data = NULL;

        // Changing the session ID during an AJAX call causes problems,
        // so we'll only update our last_activity, but only if we are not
        // using multiple sessions.
        if ($this->CI->input->is_ajax_request() && !$this->sess_use_multisessions) {
            $this->userdata['last_activity'] = $this->now;

            // Update the session ID and last_activity field in the DB if needed
            if ($this->sess_use_database === TRUE) {
                // set cookie explicitly to only have our session data
                $cookie_data = array();
                foreach (array('session_id', 'ip_address', 'user_agent', 'last_activity') as $val) {
                    $cookie_data[$val] = $this->userdata[$val];
                }

                $this->CI->db->query($this->CI->db->update_string($this->sess_table_name, array('last_activity' => $this->userdata['last_activity']), array('session_id' => $this->userdata['session_id'])));
            }

            return $this->_set_cookie($cookie_data);
        }

        // Save the old session id so we know which record to
        // update in the database if we need it
        $old_sessid = $this->userdata['session_id'];
        $new_sessid = '';
        do {
            $new_sessid .= mt_rand(0, mt_getrandmax());
        } while (strlen($new_sessid) < 32);

        // To make the session ID even more secure we'll combine it with the user's IP
        $new_sessid .= $this->CI->input->ip_address();

        // Turn it into a hash and update the session data array
        $this->userdata['session_id'] = $new_sessid = md5(uniqid($new_sessid, TRUE));
        $this->userdata['last_activity'] = $this->now;

        // Update the session ID and last_activity field in the DB if needed
        if ($this->sess_use_database === TRUE) {
            // set cookie explicitly to only have our session data
            $cookie_data = array();
            foreach (array('session_id', 'ip_address', 'user_agent', 'last_activity') as $val) {
                $cookie_data[$val] = $this->userdata[$val];
            }

            //Are we allowing multiple sessions?
            if ($this->sess_use_multisessions) {
                //Set the session as no longer allowing updates
                $this->prevent_update = TRUE;

                //Update the current session
                $this->CI->db->query($this->CI->db->update_string($this->sess_table_name, array('last_activity' => $this->now, 'prevent_update' => 1), array('session_id' => $old_sessid)));

                //Release the session lock so other requests can process
                session_write_close();

                /* Create a new entry for the updated session id. This will be the only
                 * session id that can continue to update.
                 */
                $this->_get_multi_session($new_sessid);
                $this->prevent_update = FALSE;

                //Write the new session id to the database 
                $this->CI->db->query($this->CI->db->insert_string($this->sess_table_name, $cookie_data + array('prevent_update' => 0)));

                //Make sure the user data is copied over
                $this->sess_write();

                //Release the session lock for the new session
                session_write_close();

                //Return immediately, as sess_write wrote the cookie
                return;
            } else {
                $this->CI->db->query($this->CI->db->update_string($this->sess_table_name, array('last_activity' => $this->now, 'session_id' => $new_sessid), array('session_id' => $old_sessid)));
            }
        }

        // Write the cookie
        $this->_set_cookie($cookie_data);
    }

    // --------------------------------------------------------------------

    /**
     * Destroy the current session
     *
     * @return	void
     */
    public function sess_destroy() {
        // Kill the session DB row
        if ($this->sess_use_database === TRUE && isset($this->userdata['session_id'])) {
            $this->CI->db->where('session_id', $this->userdata['session_id']);
            $this->CI->db->delete($this->sess_table_name);
        }

        // Kill the cookie
        setcookie(
                $this->sess_cookie_name, addslashes(serialize(array())), ($this->now - 31500000), $this->cookie_path, $this->cookie_domain, 0
        );
    }

    // --------------------------------------------------------------------

    /**
     * Indicates if the session is an expired multi-session
     *
     * @return  boolean
     */
    public function multisession_expired() {
        return ($this->sess_use_multisessions && $this->multisession_expired);
    }

    // --------------------------------------------------------------------

    /**
     * Fetch a specific item from the session array
     *
     * @param	string
     * @return	string
     */
    public function userdata($item) {
        return isset($this->userdata[$item]) ? $this->userdata[$item] : FALSE;
    }

    // --------------------------------------------------------------------

    /**
     * Fetch all session data
     *
     * @return	array
     */
    public function all_userdata() {
        return $this->userdata;
    }

    // --------------------------------------------------------------------------

    /**
     * Fetch all flashdata
     *
     * @return	array
     */
    public function all_flashdata() {
        $out = array();

        // loop through all userdata
        foreach ($this->all_userdata() as $key => $val) {
            // if it contains flashdata, add it
            if (strpos($key, 'flash:old:') !== FALSE) {
                $out[$key] = $val;
            }
        }
        return $out;
    }

    // --------------------------------------------------------------------

    /**
     * Add or change data in the "userdata" array
     *
     * @param	mixed
     * @param	string
     * @return	void
     */
    public function set_userdata($newdata = array(), $newval = '') {
        //Is this an expired multisession? If so, prevent updates
        if ($this->multisession_expired()) {
            return;
        }

        if (is_string($newdata)) {
            $newdata = array($newdata => $newval);
        }

        if (count($newdata) > 0) {
            foreach ($newdata as $key => $val) {
                $this->userdata[$key] = $val;
            }
        }

        $this->sess_write();
    }

    // --------------------------------------------------------------------

    /**
     * Delete a session variable from the "userdata" array
     *
     * @return	void
     */
    public function unset_userdata($newdata = array()) {
        //Is this an expired multisession? If so, prevent updates
        if ($this->multisession_expired()) {
            return;
        }

        if (is_string($newdata)) {
            $newdata = array($newdata => '');
        }

        if (count($newdata) > 0) {
            foreach ($newdata as $key => $val) {
                unset($this->userdata[$key]);
            }
        }

        $this->sess_write();
    }

    // ------------------------------------------------------------------------

    /**
     * Add or change flashdata, only available
     * until the next request
     *
     * @param	mixed
     * @param	string
     * @return	void
     */
    public function set_flashdata($newdata = array(), $newval = '') {
        //Is this an expired multisession? If so, prevent updates
        if ($this->multisession_expired()) {
            return;
        }

        if (is_string($newdata)) {
            $newdata = array($newdata => $newval);
        }

        if (count($newdata) > 0) {
            foreach ($newdata as $key => $val) {
                $this->set_userdata($this->flashdata_key . ':new:' . $key, $val);
            }
        }
    }

    // ------------------------------------------------------------------------

    /**
     * Keeps existing flashdata available to next request.
     *
     * @param	string
     * @return	void
     */
    public function keep_flashdata($key) {
        //Is this an expired multisession? If so, prevent updates
        if ($this->multisession_expired()) {
            return;
        }

        // 'old' flashdata gets removed. Here we mark all
        // flashdata as 'new' to preserve it from _flashdata_sweep()
        // Note the function will return FALSE if the $key
        // provided cannot be found
        $value = $this->userdata($this->flashdata_key . ':old:' . $key);

        $this->set_userdata($this->flashdata_key . ':new:' . $key, $value);
    }

    // ------------------------------------------------------------------------

    /**
     * Fetch a specific flashdata item from the session array
     *
     * @param	string
     * @return	string
     */
    public function flashdata($key) {


        $old_key = $this->flashdata_key . ':old:' . $key;
        $new_key = $this->flashdata_key . ':new:' . $key;

        $old_value = $this->userdata($old_key);
        $new_value = $this->userdata($new_key);

        //log_message('ERROR', 'OLD (key: '.$old_key.', value: '.print_r($old_value, true).')');
        //log_message('ERROR', 'NEW (key: '.$new_key.', value: '.print_r($new_value, true).')');

        if ($new_value != null && $new_value != FALSE && !empty($new_value)) {
            return $new_value;
        } else {
            return $old_value;
        }



//        return $this->userdata($this->flashdata_key.':old:'.$key);
    }
    
    /**
     * Keeps all the flash data present in the session for the next request.
     */
    public function keep_all_flashdata() {
        $all_userdata = $this->all_userdata();
        foreach ($all_userdata as $key => $value) {
            if (startsWith($key, $this->flashdata_key . ':new:') || startsWith($key, $this->flashdata_key . ':old:')) {
                //flashdata key
                $parts = explode(':', $key);
                $var = $parts[2];
                $this->keep_flashdata($var);
            }
        }
    }

    // ------------------------------------------------------------------------

    /**
     * Identifies flashdata as 'old' for removal
     * when _flashdata_sweep() runs.
     *
     * @return	void
     */
    public function _flashdata_mark() {
        $userdata = $this->all_userdata();
        foreach ($userdata as $name => $value) {
            $parts = explode(':new:', $name);
            if (is_array($parts) && count($parts) === 2) {
                $this->set_userdata($this->flashdata_key . ':old:' . $parts[1], $value);
                $this->unset_userdata($name);
            }
        }
    }

    // ------------------------------------------------------------------------

    /**
     * Removes all flashdata marked as 'old'
     *
     * @return	void
     */
    public function _flashdata_sweep() {
        $userdata = $this->all_userdata();
        foreach ($userdata as $key => $value) {
            if (strpos($key, ':old:')) {
                $this->unset_userdata($key);
            }
        }
    }

    // --------------------------------------------------------------------

    /**
     * Get the "now" time
     *
     * @return	string
     */
    public function _get_time() {
        return (strtolower($this->time_reference) === 'gmt') ? mktime(gmdate('H'), gmdate('i'), gmdate('s'), gmdate('m'), gmdate('d'), gmdate('Y')) : time();
    }

    // --------------------------------------------------------------------

    /**
     * Write the session cookie
     *
     * @return	void
     */
    public function _set_cookie($cookie_data = NULL) {
        if (is_null($cookie_data)) {
            $cookie_data = $this->userdata;
        }

        // Serialize the userdata for the cookie
        $cookie_data = $this->_serialize($cookie_data);

        if ($this->sess_encrypt_cookie == TRUE) {
            $cookie_data = $this->CI->encrypt->encode($cookie_data);
        } else {
            // if encryption is not used, we provide an md5 hash to prevent userside tampering
            $cookie_data = $cookie_data . md5($cookie_data . $this->encryption_key);
        }

        $expire = ($this->sess_expire_on_close === TRUE) ? 0 : $this->sess_expiration + time();

        // Set the cookie
        setcookie(
                $this->sess_cookie_name, $cookie_data, $expire, $this->cookie_path, $this->cookie_domain, $this->cookie_secure, $this->cookie_httponly
        );
    }

    // --------------------------------------------------------------------

    /**
     * Serialize an array
     *
     * This function first converts any slashes found in the array to a temporary
     * marker, so when it gets unserialized the slashes will be preserved
     *
     * @param	array
     * @return	string
     */
    public function _serialize($data) {
        if (is_array($data)) {
            array_walk_recursive($data, array(&$this, '_escape_slashes'));
        } elseif (is_string($data)) {
            $data = str_replace('\\', '{{slash}}', $data);
        }
        return serialize($data);
    }

    /**
     * Escape slashes
     *
     * This function converts any slashes found into a temporary marker
     *
     * @param	string
     * @param	string
     * @return	void
     */
    protected function _escape_slashes(&$val, $key) {
        if (is_string($val)) {
            $val = str_replace('\\', '{{slash}}', $val);
        }
    }

    // --------------------------------------------------------------------

    /**
     * Unserialize
     *
     * This function unserializes a data string, then converts any
     * temporary slash markers back to actual slashes
     *
     * @param	array
     * @return	string
     */
    public function _unserialize($data) {
        $data = @unserialize(strip_slashes(trim($data)));

        if (is_array($data)) {
            array_walk_recursive($data, array(&$this, '_unescape_slashes'));
            return $data;
        }

        return is_string($data) ? str_replace('{{slash}}', '\\', $data) : $data;
    }

    // --------------------------------------------------------------------

    /**
     * Unescape slashes
     *
     * This function converts any slash markers back into actual slashes
     *
     * @param	string
     * @param	string
     * @return	void
     */
    protected function _unescape_slashes(&$val, $key) {
        if (is_string($val)) {
            $val = str_replace('{{slash}}', '\\', $val);
        }
    }

    // --------------------------------------------------------------------

    /**
     * Garbage collection
     *
     * This deletes expired session rows from database
     * if the probability percentage is met
     *
     * @return	void
     */
    public function _sess_gc() {
        if ($this->sess_use_database != TRUE) {
            return;
        }

        srand(time());
        if ((rand() % 100) < $this->gc_probability) {
            $expire = $this->now - $this->sess_expiration;

            $this->CI->db->where('last_activity < ' . $expire);
            $this->CI->db->delete($this->sess_table_name);

            log_message('debug', 'Session garbage collection performed.');
        }
    }

    /**
     * Multi-Sessions Setup
     * 
     * Sets up a php session to handle a flag which
     * indicates if a session id can update itself
     * or not.
     * 
     * @param string
     * @return void
     */
    protected function _get_multi_session($session_id) {
        /* This is a bit of a hack, but we need to pass around info on 
         * if the current session can be updated or not. Starting a php
         * session will effectively block all subsequent requests for the
         * same session id so that we can prevent race conditions that might
         * allow erroneous updates to the session id.
         */

        //Don't allow cookies for the php session
        ini_set('session.use_cookies', '0');
        ini_set('session.use_only_cookies', '0');

        //Start a session using our internally generated session id
        session_id($session_id);
        session_start();
    }

}

/* End of file Session.php */
/* Location: ./system/libraries/Session.php */