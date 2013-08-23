<? if( $is_modal ): ?>

<label class="control-label">
    <blockquote>
        You NEED to log in!
    </blockquote>
</label>

<div class="row-fluid">
    <div class="span12">
        <div class="control-group control-form">
            <div class="controls">
                <button type="button" data-dismiss="modal" class="span4 btn btn-huge"><i class="fui-cross pull-left"></i>CLOSE</button>
            </div>
        </div>
    </div>
</div>

<? else: ?>

<?
$this->lang->load('authentication');
$this->load->view('pages/authentication_login');
?>

<? endif; ?>
