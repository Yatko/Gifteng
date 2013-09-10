<script>
	$(function() {
		$.ajaxSetup({ cache: true });
		$.getScript('//connect.facebook.net/en_UK/all.js', function(){
			FB.init({
				appId      : '285994388213208',
				status     : true,
				cookie     : true,
				oauth	   : true,
				xfbml      : true
			});
			FB.Event.subscribe('auth.authResponseChange', function(response) {
				if (response.status === 'connected') {
					showFriends();
				} else if (response.status === 'not_authorized') {
					FB.login();
				} else {
					FB.login();
				}
			});
		});
		
		$('#filter_friends').keydown(function() {
			var filter = $(this).val();
			$('#friendlist .span3').hide();
			$('#friendlist .span3').each(function() {
				if( $(".ge-name", this).text().search(new RegExp(filter, "i")) < 0 ) {
					
				}
				else {
					$(this).show();
				}
			});
		});
		
	});
	function showFriends() {
		$('#fblogin').remove();
		FB.api('/me/friends', function(response) {
		  $.each(response.data,function(index, value) {
		  	var friendbox = '<div class="span3"><div class="well ge-well">';
		  	friendbox += '<div class="ge-user">';
		  	friendbox += '<div class="ge-user-image"><img src="https://graph.facebook.com/'+value.id+'/picture"></div>';
		  	friendbox += '<div class="ge-detail">';
		  	friendbox += '<div class="ge-name"><a>'+value.name+'</a></div>';
		  	friendbox += '<div class="ge-points"><span id="btn_'+value.id+'" onclick="sendInvitation('+value.id+')" class="label link ge-user-unfollow">Invite</span></div>'
		  	friendbox += '</div></div></div></div>';
		  	$('#friendlist').append(friendbox);
		  });
		});
	}
	function sendInvitation(to) {
		FB.ui({
		  method: 'send',
		  to: to,
		  link: 'http://www.gifteng.com/'
		},
		function() {
			$('#btn_'+to).html('Invited').removeClass('ge-user-unfollow').addClass('ge-user-follow');
		});
	}
</script>
<div class="container">
    <div class="row">
    	<div class="span12">	
    		<div class="text-center">
				<fb:login-button size="large" id="fblogin">Login with Facebook to Invite Friends</fb:login-button>
				<div class="row">
					<div class="span6 offset3">
						<div class="well ge-well">
							<h4 style="padding-top:10px;">Invite friends</h4>
							<input type="text" id="filter_friends" placeholder="Search friends" />
						</div>
					</div>
				</div>
			</div>
			<div class="row" id="friendlist"></div>
    	</div>
    </div>
</div>