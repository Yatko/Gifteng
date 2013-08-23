<?php

/**
 * Source URLS:
 * http://getsparks.org/packages/RememberMe/versions/HEAD/show
 * 
 * CodeIgniter Remember Me Spark
 *
 * This CodeIgniter Spark provides a library for creating and verifying non-forgeable cookies suitable for "Remember Me?" type login checkboxes.
 *
 * By Joe Auty @ http://www.netmusician.org
 * 
 * http://getsparks.org/packages/RememberMe/show
 * 
 * 
 * SQLITE:
 * 
 * CREATE TABLE venefica_cookies (
 *  id INTEGER PRIMARY KEY AUTOINCREMENT,
 *  cookie_id varchar(255) default NULL,
 *  netid varchar(255) default NULL,
 *  ip_address varchar(255) default NULL,
 *  user_agent varchar(255) default NULL,
 *  orig_page_requested varchar(120) default NULL,
 *  php_session_id varchar(40) default NULL,
 *  created_at datetime default NULL,
 *  updated_at datetime default NULL
 * );
 * 
 */
class Remember_me {

    private $CI;
    private $table_name = 'venefica_cookies';

    function __construct() {
        $this->CI =& get_instance();
    }

    public function setCookie($netid = "", $nocookie = false) {
        if (!$netid && !$nocookie) {
            show_error("setCookie request missing netid");
            return;
        }

        //session_start();
        
        // delete any existing table entries belonging to user
        $nocookie ? $this->CI->db->where('php_session_id', session_id()) : $this->CI->db->where('netid', $netid);
        $this->CI->db->delete($this->table_name);

        if ($nocookie) {
            // record landing page
            $cookie_id = "";
            $orig_page_requested = $this->CI->uri->uri_string();
        } else {
            $cookie_id = uniqid('', true);
            $orig_page_requested = "";

            // delete temporary landing page record, if it exists,
            // but salvage orig_page_requested var
            $query = $this->CI->db->get_where($this->table_name, array(
                'php_session_id' => session_id()
            ));
            if ($query->num_rows()) {
                $row = $query->row();
                $orig_page_requested = $row->orig_page_requested;
            }
            $this->CI->db->delete($this->table_name, array(
                'php_session_id' => session_id()
            ));
        }

        $ip_address = ($_SERVER['SERVER_NAME'] == "localhost") ? '127.0.0.1' : $_SERVER['REMOTE_ADDR'];

        $insertdata = array(
            'cookie_id' => $cookie_id,
            'ip_address' => $ip_address,
            'user_agent' => $this->CI->agent->agent_string(),
            'netid' => $netid,
            'created_at' => date('Y-m-d H:i:s'),
            'orig_page_requested' => $orig_page_requested,
            'php_session_id' => session_id()
        );
        $this->CI->db->insert($this->table_name, $insertdata);

        // set cookie for TLD, not subdomains
        $host = explode('.', $_SERVER['SERVER_NAME']);
        $segments = count($host) - 1;
        $domain = ($_SERVER['SERVER_NAME'] == "localhost") ? false : $host[($segments - 1)] . "." . $host[$segments];

        if (!$nocookie) {
            // set cookie for 1 year
            $cookie = array(
                'name' => 'rmtoken_' . str_replace('.', '_', $_SERVER['SERVER_NAME']),
                'value' => $cookie_id,
                'expire' => 31557600,
                'domain' => $domain,
                'path' => preg_replace('/^(http|https):\/\/(www\.)?' . $_SERVER['SERVER_NAME'] . '/', '', preg_replace('/\/$/', '', base_url())),
                'secure' => isset($_SERVER['HTTPS']) ? $_SERVER['HTTPS'] : 0
            );
            $this->CI->input->set_cookie($cookie);

            // establish session
            $this->CI->session->set_userdata('rememberme_session', $netid);
        }
    }

    public function recordOrigPage() {
        $this->setCookie("", true);
    }

    public function getOrigPage() {
        session_start();
        $query = $this->CI->db->get_where($this->table_name, array(
            'php_session_id' => session_id()
        ));
        if ($query->num_rows()) {
            $row = $query->row();
            return $row->orig_page_requested;
        } else {
            return false;
        }
    }

    public function deleteCookie() {
        $this->CI->session->sess_destroy();

        $query = $this->CI->db->get_where($this->table_name, array(
            'cookie_id' => $this->CI->input->cookie('rmtoken_' . str_replace('.', '_', $_SERVER['SERVER_NAME']))
        ));
        if (!$query->num_rows()) {
            // no cookie to destroy, return
            return;
        }
        $row = $query->row();
        if ( !isset($row) || $row == null ) {
            return;
        }

        $this->CI->db->where('netid', $row->netid);
        $this->CI->db->delete($this->table_name);
        delete_cookie('rememberme_token');
    }

    public function verifyCookie() {
        if (!$this->CI->input->cookie('rmtoken_' . str_replace('.', '_', $_SERVER['SERVER_NAME']))) {
            return false;
        }

        $query = $this->CI->db->get_where($this->table_name, array(
            'cookie_id' => $this->CI->input->cookie('rmtoken_' . str_replace('.', '_', $_SERVER['SERVER_NAME']))
        ));
        //print $this->CI->db->last_query();

        if ($query->num_rows()) {
            $row = $query->row();
            if ( !isset($row) || $row == null ) {
                return false;
            }

            // authorize user, if this option is set
            if ($this->CI->config->item('authfunc')) {
                if ($this->CI->config->item('requiremodel')) {
                    $this->CI->load->model($this->CI->config->item('requiremodel'));
                }
                if ($this->CI->config->item('requirelibrary')) {
                    $this->CI->load->library($this->CI->config->item('requirelibrary'));
                }

                $authorize = call_user_func($this->CI->config->item('authfunc'), $row->netid);

                if (!$authorize) {
                    $this->deleteCookie();
                    return false;
                }
            }
            
            // valid cookie
            if ($this->CI->session->userdata('rememberme_session')) {
                // session active, make sure cookie and session netids match
                if ( $this->CI->session->userdata('rememberme_session').'' !== $row->netid.'' ) {
                    return false;
                }
            } else {
                // create new session
                $this->CI->session->set_userdata('rememberme_session', $row->netid);
            }

            // return netid
            return $row->netid;
        } else {
            return false;
        }
    }

}