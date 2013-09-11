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
 *  token BLOB NOT NULL,
 *  ip_address varchar(255) default NULL,
 *  user_agent varchar(255) default NULL,
 *  orig_page_requested varchar(120) default NULL,
 *  php_session_id varchar(40) default NULL,
 *  created_at datetime default NULL
 * );
 * 
 */
class Remember_me {

    private $CI;
    private $table_name = 'venefica_cookies';
    // if your authorize function is a model, specify the model name here so that 
    // this model will be instantiated
    private $requiremodel = 'user_model';
    // If your authorize function is a custom library, specify the library name here
    // so that this library will be instatiated
    private $requirelibrary = 'usermanagement_service';
    // Provide the reference to your authorize function here as a string
    private $authfunc = ''; //'Usermanagement_service::authorize';

    function __construct() {
        $this->CI =& get_instance();
        $this->CI->load->library('user_agent');
    }

    public function setCookie($netid = "", $token = "", $nocookie = false) {
        if (!$netid && !$nocookie) {
            show_error("setCookie request missing netid");
            return;
        }

        //session_start();
        
        // delete any existing table entries belonging to user
        $nocookie ? $this->CI->db->where('php_session_id', $this->getSessionId()) : $this->CI->db->where('netid', $netid);
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
                'php_session_id' => $this->getSessionId()
            ));
            if ($query->num_rows()) {
                $row = $query->row();
                if ( isset($row) && $row != null ) {
                    $orig_page_requested = $row->orig_page_requested;
                }
            }
            $this->CI->db->delete($this->table_name, array(
                'php_session_id' => $this->getSessionId()
            ));
        }

        $ip_address = ($_SERVER['SERVER_NAME'] == "localhost") ? '127.0.0.1' : $_SERVER['REMOTE_ADDR'];

        $insertdata = array(
            'cookie_id' => $cookie_id,
            'ip_address' => $ip_address,
            'user_agent' => $this->CI->agent->agent_string(),
            'netid' => $netid,
            'token' => $token,
            'created_at' => date('Y-m-d H:i:s'),
            'orig_page_requested' => $orig_page_requested,
            'php_session_id' => $this->getSessionId()
        );
        $this->CI->db->insert($this->table_name, $insertdata);

        $host = explode('.', $_SERVER['SERVER_NAME']);
        $segments = count($host) - 1;
        if ( $_SERVER['SERVER_NAME'] == "localhost" ) {
            $domain = false;
        } else if ( is_numeric($host[$segments]) ) {
            $domain = $_SERVER['SERVER_NAME'];
        } else {
            // set cookie for TLD, not subdomains
            $domain = $host[($segments - 1)] . "." . $host[$segments];
        }

        if (!$nocookie) {
            // set cookie for 1 year
            $cookie = array(
                'name' => $this->getCookieName(),
                'value' => $cookie_id,
                'expire' => 31557600, //1 year = 365.25 * 24 * 60 * 60
                'domain' => $domain,
                'path' => preg_replace('/^(http|https):\/\/(www\.)?' . $_SERVER['SERVER_NAME'] . '/', '', preg_replace('/\/$/', '', base_url())),
                'secure' => isset($_SERVER['HTTPS']) ? $_SERVER['HTTPS'] : 0
            );
            $this->CI->input->set_cookie($cookie);

            // establish session
            $this->CI->session->set_userdata('rememberme_session', $netid);
        }
    }

    public function deleteCookie() {
        $this->CI->session->sess_destroy();

        $query = $this->CI->db->get_where($this->table_name, array(
            'cookie_id' => $this->CI->input->cookie($this->getCookieName())
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
        $this->CI->input->set_cookie($this->getCookieName(), ''); //delete cookie
    }

    public function verifyCookie() {
        if (!$this->CI->input->cookie($this->getCookieName())) {
            return false;
        }

        $query = $this->CI->db->get_where($this->table_name, array(
            'cookie_id' => $this->CI->input->cookie($this->getCookieName())
        ));
        //print $this->CI->db->last_query();

        if ($query->num_rows()) {
            $row = $query->row();
            if ( !isset($row) || $row == null ) {
                return false;
            }

            // authorize user, if this option is set
            if ($this->authfunc != '') {
                if ($this->requiremodel != '') {
                    $this->CI->load->model($this->requiremodel);
                }
                if ($this->requirelibrary != '') {
                    $this->CI->load->library($this->requirelibrary);
                }
                
                $authorize = call_user_func($this->authfunc, $row->netid);

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
            return array(
                'netid' => $row->netid,
                'token' => $row->token
            );
        } else {
            return false;
        }
    }
    
    private function getCookieName() {
        return 'rmtoken_' . str_replace('.', '_', $_SERVER['SERVER_NAME']);
    }

    private function getSessionId() {
        //return session_id();
        return $this->CI->session->sess_id();
    }
    
    private function recordOrigPage() {
        $this->setCookie("", true);
    }

    private function getOrigPage() {
        //session_start();
        
        $query = $this->CI->db->get_where($this->table_name, array(
            'php_session_id' => $this->getSessionId()
        ));
        if ($query->num_rows()) {
            $row = $query->row();
            if ( !isset($row) || $row == null ) {
                return false;
            }
            return $row->orig_page_requested;
        } else {
            return false;
        }
    }

}