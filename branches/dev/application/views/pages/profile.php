<?

/**
 * Input params:
 * 
 * user: User_model
 */

$receivings_num = $user->statistics->numReceivings;
$givings_num = $user->statistics->numGivings;
$bookmarks_num = $user->statistics->numBookmarks;
$followers_num = $user->statistics->numFollowers;
$followings_num = $user->statistics->numFollowings;
//$ratings_num = $user->statistics->numRatings;

$user_about = $user->about;
$is_owner = isOwner($user);

reset($_GET);
$active_menu = key($_GET);
if ( $active_menu == null ) {
    $active_menu = Profile::MENU_GIVING;
}

$active_tab = Profile::TAB_GIFTS;
if ( in_array($active_menu, array(Profile::MENU_GIVING, Profile::MENU_RECEIVING, Profile::MENU_FAVORITE)) ) {
    $active_tab = Profile::TAB_GIFTS;
} else if ( in_array($active_menu, array(Profile::MENU_FOLLOWING, Profile::MENU_FOLLOWER, Profile::MENU_RATING)) ) {
    $active_tab = Profile::TAB_CONNECTIONS;
} else if ( in_array($active_menu, array(Profile::MENU_NOTIFICATION, Profile::MENU_MESSAGE, Profile::MENU_SETTING)) ) {
    $active_tab = Profile::TAB_ACCOUNT;
} else if ( in_array($active_menu, array(Profile::MENU_ABOUT)) ) {
    $active_tab = Profile::TAB_BIO;
}

?>


<div class="row">
    <div class="span12">
        <div class="ge-profile ge-detail-view">
            <div class="well ge-well">

                <div class="row-fluid ge-user">
                    <div class="span6">
                        <? $this->load->view('element/user', array('user' => $user, 'canEdit' => true, 'small' => false)); ?>
                    </div>
                    <div class="span6 mobile-four">
                        <div class="ge-info">
                            <ul class="nav nav-tabs nav-append-content hidden-phone">
                                <li class="hidden-tablet <?= ($active_tab == Profile::TAB_GIFTS ? 'active' : '') ?>">
                                    <a href="#tab_gifts"><i class="ge-icon-giftbox"></i> Gifts</a>
                                </li>
                                <li class="hidden-tablet <?= ($active_tab == Profile::TAB_CONNECTIONS ? 'active' : '') ?>">
                                    <a href="#tab_connections"><i class="fui-heart"></i> Connections</a>
                                </li>
                                
<? if( $is_owner ): ?>
                                <li class="hidden-tablet <?= ($active_tab == Profile::TAB_ACCOUNT ? 'active' : '') ?>">
                                    <a href="#tab_account"><i class="fui-user"></i> Account</a>
                                </li>
<? endif; ?>
                                
                                <li class="hidden-tablet <?= ($active_tab == Profile::TAB_BIO ? 'active' : '') ?>">
                                    <a href="#tab_bio"><i class="fui-bubble"></i> Me</a>
                                </li>
                            </ul><!-- /tabs -->
                            <div class="tab-content">
                                <div id="tab_gifts" class="tab-pane <?= ($active_tab == Profile::TAB_GIFTS ? 'active' : '') ?>">
                                    <div class="row-fluid">
                                        <div class="span4">
                                            <a href="<?= current_url() ?>?<?= Profile::MENU_GIVING ?>" class="btn btn-small btn-block btn-ge <?= ($active_menu == Profile::MENU_GIVING) ? 'active-tab' : '' ?>">
                                                Giving<br />
                                                <span class="user_giving"><?= $givings_num ?></span>
                                            </a>
                                        </div>
                                        
<? if( $is_owner ): ?>
                                            <div class="span4">
                                                <a href="<?= current_url() ?>?<?= Profile::MENU_RECEIVING ?>" class="btn btn-small btn-block btn-ge <?= ($active_menu == Profile::MENU_RECEIVING) ? 'active-tab' : '' ?>">
                                                    Receiving<br />
                                                    <span class="user_receiving"><?= $receivings_num ?></span>
                                                </a>
                                            </div>
                                            <div class="span4">
                                                <a href="<?= current_url() ?>?<?= Profile::MENU_FAVORITE ?>" class="btn btn-small btn-block btn-ge <?= ($active_menu == Profile::MENU_FAVORITE) ? 'active-tab' : '' ?>">
                                                    Favorites<br />
                                                    <span class="user_bookmark"><?= $bookmarks_num ?></span>
                                                </a>
                                            </div>
<? endif; ?>
                                        
                                    </div>
                                </div><!-- /tab-->
                                <div id="tab_connections" class="tab-pane <?= ($active_tab == Profile::TAB_CONNECTIONS ? 'active' : '') ?>">
                                    <div class="row-fluid">
                                        <div class="span4">
                                            <a href="<?= current_url() ?>?<?= Profile::MENU_FOLLOWING ?>" class="btn btn-small btn-block btn-ge">
                                                <span class="user_following"><?= $followings_num ?></span><br />
                                                Following
                                            </a>
                                        </div>
                                        <div class="span4">
                                            <a href="<?= current_url() ?>?<?= Profile::MENU_FOLLOWER ?>" class="btn btn-small btn-block btn-ge">
                                                <span class="user_follower"><?= $followers_num ?></span><br />
                                                Followers
                                            </a>
                                        </div>
                                        <? /** ?>
                                        <div class="span4">
                                            <a href="<?= current_url() ?>?<?= Profile::MENU_RATING ?>" class="btn btn-small btn-block btn-ge">
                                                <?=$ratings_num?><br />
                                                Reviews
                                            </a>
                                        </div>
                                        <? /* */ ?>
                                    </div>
                                </div><!-- /tab-->
                                
<? if( $is_owner ): ?>
                                <div id="tab_account" class="tab-pane <?= ($active_tab == Profile::TAB_ACCOUNT ? 'active' : '') ?>">
                                    <div class="row-fluid">
                                        <div class="span4">
                                            <a href="<?= current_url() ?>?<?= Profile::MENU_NOTIFICATION ?>" class="btn btn-small btn-block btn-ge">
                                                <i class="fui-alert"></i>
                                                <br />Notifications
                                            </a>
                                        </div>
                                        <div class="span4">
                                            <a href="<?= current_url() ?>?<?= Profile::MENU_MESSAGE ?>" class="btn btn-small btn-block btn-ge">
                                                <i class="fui-mail"></i>
                                                <br />Messages
                                            </a>
                                        </div>
                                        <? /** ?>
                                        <div class="span4">
                                            <a href="<?= current_url() ?>?<?= Profile::MENU_SETTING ?>" class="btn btn-small btn-block btn-ge">
                                                <i class="fui-gear"></i>
                                                <br />Settings
                                            </a>
                                        </div>
                                        <? /**/ ?>
                                    </div>
                                </div><!-- /tab-->
<? endif; ?>
                                
                                <div id="tab_bio" class="tab-pane <?= ($active_tab == Profile::TAB_BIO ? 'active' : '') ?>">
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
