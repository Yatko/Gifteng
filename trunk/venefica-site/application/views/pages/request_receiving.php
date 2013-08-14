<?

/**
 * Input params:
 * 
 * ad: Ad_model
 * request: Request_model
 * messages: array of Message_model
 * currentUser: User_model
 * is_modal: boolean
 * userId: long
 */

$ad_id = $ad->id;
$request_id = $request->id;
$ad_creator = $ad->creator;
$ad_title = $ad->getSafeTitle();
$view_link = $ad->getViewUrl();

?>

<div class="span6">
	<div class="well ge-well">
		
		<div class="row-fluid">
			<div class="ge-user">
                            <? $this->load->view('element/user', array('user' => $ad_creator, 'canEdit' => false, 'small' => true)); ?>
			</div><!--./ge-user-->
		</div>
		
<? if( $ad->sold ): ?>


<? elseif( $request->accepted ): ?>
		<div class="row-fluid">
			<div class="ge-action">
				<div class="span6">
					<button onclick="request_receive(<?=$request_id?>, <?=$ad_id?>, <?=$userId?>);" type="button" class="btn btn-small btn-block btn-ge">Mark Received</button>
				</div>
			</div>
		</div><!--./ge-action-->

<? endif; ?>

		
		<div class="row-fluid">
					<div class="ge-messages">
						<div class="span12">
							
							<div class="row-fluid">
								<div class="ge-subject">
									<a href="<?=$view_link?>" class="ge-title"><?=$ad_title?></a>
								</div><!--./ge-subject-->
							</div>
							
							<? $this->load->view('element/messages', array('messages' => $messages, 'request' => $request, 'to' => $ad_creator, 'canMessage' => true)); ?>
				
						</div>
					</div><!--./ge-messages-->
				</div>
		
	</div><!--./ge-well-->
</div>
