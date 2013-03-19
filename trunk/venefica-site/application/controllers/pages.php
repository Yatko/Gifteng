<?php

class Pages extends CI_Controller {

    public function view($page = 'index') {
        if (!file_exists('application/views/pages/' . $page . '.php')) {
            // Whoops, we don't have a page for that!
            show_404();
        }
        
        //load langauge
        $this->lang->load('main');
        $this->lang->load('authentication');

        $this->load->view('templates/header');
        $this->load->view('pages/' . $page);
        $this->load->view('templates/footer');
    }

}
