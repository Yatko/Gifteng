<?
$receivings_num = $user->statistics->numReceivings;
$givings_num = $user->statistics->numGivings;
$bookmarks_num = $user->statistics->numBookmarks;
$followers_num = $user->statistics->numFollowers;
$followings_num = $user->statistics->numFollowings;
$ratings_num = $user->statistics->numRatings;

$user_about = $user->about;
?>


<div class="row">
    <div class="span12">
        <div class="ge-profile ge-detail-view">
            <div class="well ge-well">

                <div class="row-fluid ge-user">
                    <div class="span6">
                        <? $this->load->view('element/user', array('user' => $user, 'canEdit' => true, 'small' => false)); ?>
                    </div>
                    <div class="span6">
                        <div class="ge-info">
                            <ul class="nav nav-tabs nav-append-content hidden-phone">
                                <li><a href="#tab_gifts"><i class="ge-icon-giftbox"></i> Gifts</a></li><!--TODO add icon to FLAT-->
                                <li><a href="#tab_connections"><i class="fui-heart"></i> Connections</a></li>
                                <li><a href="#tab_account"><i class="fui-user"></i> Account</a></li>
                                <li class="active hidden-tablet"><a href="#tab_bio"><i class="fui-bubble"></i> Bio</a></li>
                            </ul><!-- /tabs -->
                            <div class="tab-content">
                                <div class="tab-pane" id="tab_gifts">
                                    <div class="row-fluid">
                                        <div class="span4">
                                            <a href="<?= current_url() ?>?giving" class="btn btn-small btn-block btn-ge">Giving<br /><?= $givings_num ?></a>
                                        </div>
                                        <div class="span4">
                                            <a href="<?= current_url() ?>?receiving" class="btn btn-small btn-block btn-ge">Receiving<br /><?= $receivings_num ?></a>
                                        </div>
                                        <div class="span4">
                                            <a href="<?= current_url() ?>?favorit" class="btn btn-small btn-block btn-ge">Favorites<br /><?= $bookmarks_num ?></a>
                                        </div>
                                    </div>
                                </div><!-- /tab-->
                                <div class="tab-pane" id="tab_connections">											
                                    <div class="row-fluid">
                                        <div class="span4">
                                            <a href="<?= current_url() ?>?following" class="btn btn-small btn-block btn-ge"><?= $followings_num ?><br />Following</a>
                                        </div>
                                        <div class="span4">
                                            <a href="<?= current_url() ?>?follower" class="btn btn-small btn-block btn-ge"><?= $followers_num ?><br />Followers</a>
                                        </div>
                                        <? /** ?>
                                        <div class="span4">
                                            <a href="<?=current_url()?>?rating" class="btn btn-small btn-block btn-ge"><?=$ratings_num?><br />Reviews</a>
                                        </div>
                                        <? /* */ ?>
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
                                        <button class="btn btn-small btn-block btn-ge"><?= $user_about ?></button>
                                    </div>
                                </div><!-- /tab-->
                            </div><!-- /tab-content -->
                        </div><!--./ge-info-->
                    </div>
                </div><!--./ge-user-->	

            </div><!--./ge-well-->
        </div><!--./ge-profile-->
    </div>
</div><!-- ./row -->
