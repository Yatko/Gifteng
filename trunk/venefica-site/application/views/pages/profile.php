<script langauge="javascript">
    $(function() {
        $('#avatar').on('file_selected', function() {
            var $this = $(this);
            var formData = new FormData($("#avatar_post_form").get(0));
            
            $.ajax({
                type: 'POST',
                url: '<?=base_url()?>profile/ajax/change_avatar',
                dataType: 'json',
                cache: false,
                data: formData,
                processData: false,
                contentType: false
            }).done(function(response) {
                if ( !response || response == '' ) {
                    //TODO: empty result
                } else if ( response.<?=Profile::AJAX_STATUS_ERROR?> ) {
                    //TODO
                } else if ( response.<?=Profile::AJAX_STATUS_RESULT?> ) {
                    $('#avatarImage').attr('src', response.<?=Profile::AJAX_STATUS_RESULT?>);
                } else {
                    //TODO: unknown response received
                }
                
                $this.text($this.attr('original_text'));
                $("#avatarContainer").modal('hide');
            }).fail(function(data) {
                //TODO
            });
        });
        
        $('#editProfileContainer').on('hidden', function () {
            //$(this).data('modal', null);
            $(this).removeData("modal");
        });
    });
</script>

<?
$user_avatar_img = $user->getAvatarUrl();
$user_full_name = $user->getFullName();
$user_joined = $user->getJoinDate();
$user_location = $user->getLocation();
$user_about = $user->about;

if ( trim($user_avatar_img) == '' ) $user_avatar_img = BASE_PATH.'temp-sample/ge-user.jpg';
if ( trim($user_full_name) == '' ) $user_full_name = '&nbsp;';
?>


<div id="avatarContainer" class="modal hide fade" tabindex="-1" role="dialog" aria-hidden="true">
    <div class="modal-header">
        <label class="control-label" for="fieldset">
            <blockquote>
                <p>
                    Choose your new avatar image
                    <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
                </p>
            </blockquote>
        </label>
    </div>
    <div class="modal-body">

        <?=form_open_multipart('/profile/ajax/change_avatar', array('id' => 'avatar_post_form'))?>

            <button id="avatar" for="image" type="button" class="btn btn-huge btn-block file">Upload a great photo<i class="fui-photo pull-right"></i></button>
            <input type="file" name="image" id="image" />

        <?=form_close()?>

    </div>
</div>


<div id="editProfileContainer" class="modal hide fade" data-remote="<?=base_url()?>edit_profile?modal">
    <div class="modal-body"></div>
</div>


<div class="container ge-topspace">	
    <div class="row ge-profile">
        <div class="span12">
            <div class="well ge-well">

                <div class="row-fluid ge-user">
                    <div class="span6">
                        <div class="ge-user-image">
                            <a data-toggle="modal" href="#avatarContainer">
                                <img id="avatarImage" src="<?=$user_avatar_img?>" class="img-rounded">
                            </a>
                        </div>
                        <div class="ge-details">
                            <div class="ge-name"><?=$user_full_name?> <a href="<?=base_url()?>edit_profile" data-target="#editProfileContainer" data-toggle="modal"><i class="fui-gear"></i></a></div>
                            <div class="ge-age">Giftenger since <?=$user_joined?></div>
                            <div class="ge-location"><?=$user_location?></div>
                            <div class="ge-points">xxx</div>
                        </div>
                    </div>
                    <div class="span6 ge-info">
                        <ul class="nav nav-tabs nav-append-content hidden-phone">
                            <li><a href="#tab_gifts"><i class="icon-ge-giftbox"></i> Gifts</a></li><!--TODO add icon to FLAT-->
                            <li><a href="#tab_relations"><i class="fui-heart"></i> Relations</a></li>
                            <li><a href="#tab_account"><i class="fui-user"></i> Account</a></li>
                            <li class="active hidden-tablet"><a href="#tab_bio"><i class="fui-bubble"></i> Bio</a></li>
                         </ul><!-- /tabs -->
                        <div class="tab-content">
                            <div class="tab-pane" id="tab_gifts">
                                <div class="row-fluid">
                                    <div class="span4">
                                        <button class="btn btn-small btn-block btn-ge">Giving<br /><?=$givings_num?></button>
                                    </div>
                                    <div class="span4">
                                        <button class="btn btn-small btn-block btn-ge">Receiving<br /><?=$receivings_num?></button>
                                    </div>
                                    <div class="span4">
                                        <button class="btn btn-small btn-block btn-ge">Favorites<br /><?=$bookmarks_num?></button>
                                    </div>
                                </div>
                            </div><!-- /tab-->
                            <div class="tab-pane" id="tab_relations">											
                                <div class="row-fluid">
                                    <div class="span4">
                                        <button class="btn btn-small btn-block btn-ge"><?=$followings_num?><br />Following</button>
                                    </div>
                                    <div class="span4">
                                        <button class="btn btn-small btn-block btn-ge"><?=$followers_num?><br />Followers</button>
                                    </div>
                                    <div class="span4">
                                        <button class="btn btn-small btn-block btn-ge"><?=$ratings_num?><br />Reviews</button>
                                    </div>
                                </div>
                            </div><!-- /tab-->
                            <div class="tab-pane" id="tab_account">
                                <div class="row-fluid">
                                    <div class="span4">
                                        <button class="btn btn-small btn-block btn-ge"><i class="fui-alert"></i><br />Notifications</button>
                                    </div>
                                    <div class="span4">
                                        <button class="btn btn-small btn-block btn-ge"><i class="fui-mail"></i><br />Messages</button>
                                    </div>
                                    <div class="span4">
                                        <button class="btn btn-small btn-block btn-ge"><i class="fui-gear"></i><br />Settings</button>
                                    </div>
                                </div>
                            </div><!-- /tab-->
                            <div class="tab-pane active" id="tab_bio">
                                <div class="row-fluid">
                                    <button class="btn btn-small btn-block btn-ge"><?=$user_about?></button>
                                </div>
                            </div><!-- /tab-->
                        </div><!-- /tab-content -->   
                    </div>
                </div><!--./ge-user-->	

            </div><!--./ge-well-->
        </div>
    </div><!--./ge-profile-->
</div><!--./container (user-profile)-->


<? /** ?>
<div class="profile">
    <? if( $givings_num > 0 ): ?>
    <div class="giving">
        <div class="title">Giving</div>
        <? foreach( $givings as $ad ): ?>
            <?
            $ad_id = $ad->id;
            $ad_img = $ad->getImageUrl();
            $ad_title = $ad->title;
            ?>

            <div class="thumbnail"><a href="#"><img src="<?=$ad_img?>" alt="<?=safe_parameter($ad_title)?>"></a></div>
        <? endforeach; ?>
    </div>
    <? endif; ?>

    <? if( $followings_num > 0 ): ?>
    <div class="following">
        <div class="title">Following</div>
        <? foreach( $followings as $user ): ?>
            <?
            $user_img = $user->getAvatarUrl();
            $user_name = $user->getFullName();
            $user_joined = $user->getJoinDate();
            $user_location = $user->getLocation();

            if ( trim($user_name) == '' ) $user_name = '&nbsp;';
            if ( trim($user_location) == '' ) $user_location = '&nbsp;';
            ?>

            <div class="profileBox">
                <div class="profileImage" style="background: url(<?=$user_img?>);"></div>
                <div class="details">
                    <div class="username"><?=safe_content($user_name)?></div>
                    <div class="age">Giftenger since <?=$user_joined?></div>
                    <div class="location"><?=$user_location?></div>
                </div>
            </div>
        <? endforeach; ?>
    </div>
    <? endif; ?>

    <? if( $followers_num > 0 ): ?>
    <div class="follower">
        <div class="title">Followers</div>
        <? foreach( $followers as $user ): ?>
            <?
            $user_img = $user->getAvatarUrl();
            $user_name = $user->getFullName();
            $user_joined = $user->getJoinDate();
            $user_location = $user->getLocation();

            if ( trim($user_name) == '' ) $user_name = '&nbsp;';
            if ( trim($user_location) == '' ) $user_location = '&nbsp;';
            ?>

            <div class="profileBox">
                <div class="profileImage" style="background: url(<?=$user_img?>);"></div>
                <div class="details">
                    <div class="username"><?=safe_content($user_name)?></div>
                    <div class="age">Giftenger since <?=$user_joined?></div>
                    <div class="location"><?=$user_location?></div>
                </div>
            </div>
        <? endforeach; ?>
    </div>
    <? endif; ?>

    <? if( $ratings_num > 0 ): ?>
    <div class="review">
        <div class="title">Reviews</div>
        <? foreach( $ratings as $rating ): ?>
            <?
            $from_user_img = $rating->getFromAvatarUrl();
            $from_user_name = $rating->getFromFullName();
            $rating_date = $rating->getRateDate();
            $rating_text = $rating->text;

            if ( trim($from_user_name) == '' ) $from_user_name = '&nbsp;';
            ?>

            <div class="profileBox">
                <div class="profileImage" style="background: url(<?=$from_user_img?>);"></div>
                <div class="details">
                    <div class="username"><?=safe_content($from_user_name)?></div>
                    <div class="age"><?=$rating_date?></div>
                </div>
                <div class="details">
                    <div><?=safe_content($rating_text)?></div>
                </div>
            </div>
        <? endforeach; ?>
    </div>
    <? endif; ?>
</div>
<? /**/ ?>