<div id="fb-root"></div>
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
		  	friendbox += '<div class="ge-points"><span onclick="sendInvitation('+value.id+')" class="label link ge-user-unfollow">Invite</span></div>'
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
		});
	}
</script>
<div class="container">
    <div class="row">
    	<div class="span12">	
    		<div class="text-center">
				<fb:login-button id="fblogin" show-faces="true" width="200" max-rows="1"></fb:login-button>
			</div>
			<div class="row" id="friendlist"></div>
    	</div>
    </div>
</div>