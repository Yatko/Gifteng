<script>
	$(function() {
		
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
		FB.api('/me/friends', function(response) {
		  $.each(response.data,function(index, value) {
		  	var friendbox = '<div class="span3"><div class="well ge-well">';
		  	friendbox += '<div class="ge-user">';
		  	friendbox += '<div class="ge-user-image"><img src="https://graph.facebook.com/'+value.id+'/picture" class="img-rounded"></div>';
		  	friendbox += '<div class="ge-detail">';
		  	friendbox += '<div class="ge-name"><a>'+value.name+'</a></div>';
		  	friendbox += '<div class="ge-points"><span id="btn_'+value.id+'" onclick="sendInvitation('+value.id+')" class="label link ge-user-unfollow">INVITE</span></div>'
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
			<div class="well ge-well friend-filter">
				<div class="row-fluid">
					<div class="span6 text-right">
						<label>Select from the list or search by name</label>
					</div>
					<div class="span5 text-left">
						<input type="text" id="filter_friends" placeholder="Search friends" />
					</div>
				</div>
			</div>
			<div class="row">
				<div id="friendlist">
					
				</div>
			</div>
    	</div>
    </div>
</div>