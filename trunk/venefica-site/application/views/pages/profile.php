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

$active_menu = Profile::getActiveMenu();
$active_tab = Profile::getActiveTab($active_menu);

?>


<div class="row">
    <div class="span12">
        <div class="ge-profile ge-detail-view">
            <div class="well ge-well">
            	<? if( !empty($_GET) ) : ?>
                    <?
                    if ( $active_menu == Profile::MENU_MESSAGE ) {
                        $menu_text = 'Messages';
                    } else if ( $active_menu == Profile::MENU_NOTIFICATION ) {
                        $menu_text = 'Notifications';
                    } else if ( $active_menu == Profile::MENU_FAVORITE ) {
                        $menu_text = 'Favorites';
                    } else if ( $active_menu == Profile::MENU_FOLLOWING || $active_menu == Profile::MENU_FOLLOWER ) {
                        $menu_text = 'Connections';
                    } else if ( $active_menu == Profile::MENU_GIVING ) {
                        $menu_text = 'Giving';
                    } else if ( $active_menu == Profile::MENU_RECEIVING ) {
                        $menu_text = 'Receiving';
                    }
                    //else if ( $active_menu == Profile::MENU_RATING ) {
                    //    $menu_text = 'Rating';
                    //}
                    else if ( $active_menu == Profile::MENU_SETTING ) {
                        $menu_text = 'Setting';
                    } else if ( $active_menu == Profile::MENU_ABOUT ) {
                        $menu_text = 'About';
                    } else {
                        $menu_text = '';
                    }
                    ?>
                    
                    <h3 class="visible-phone text-center mobile-header"><?=$menu_text?></h3>
                <? endif; ?>
                
                <div class="row-fluid ge-user">
                    <div class="span6 <?= empty($_GET) ? '' : 'hidden-phone'?>">
                        <? $this->load->view('element/user', array('user' => $user, 'canEdit' => true, 'small' => false)); ?>
                    </div>
                    <div class="span6 mobile-four <?= ($active_tab == Profile::TAB_CONNECTIONS) ? '' : 'hidden-phone' ?>">
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
                                    <a href="#tab_bio"><i class="fui-bubble"></i> About</a>
                                </li>
                            </ul><!-- /tabs -->
                            <div class="tab-content">
                                <div id="tab_gifts" class="tab-pane <?= ($active_tab == Profile::TAB_GIFTS ? 'active' : '') ?>">
                                    <div class="row-fluid">
                                        <div class="span4 mobile-one">
                                            <a href="<?= current_url() ?>?<?= Profile::MENU_GIVING ?>" class="btn btn-small btn-block btn-ge <?= $active_menu == Profile::MENU_GIVING ? 'active-tab' : '' ?>">
                                                Giving<br />
                                                <span class="user_giving"><?= $givings_num ?></span>
                                            </a>
                                        </div>
                                        <div class="span4 mobile-one">
                                            <a href="<?= current_url() ?>?<?= Profile::MENU_RECEIVING ?>" class="btn btn-small btn-block btn-ge <?= $active_menu == Profile::MENU_RECEIVING ? 'active-tab' : '' ?>">
                                                Receiving<br />
                                                <span class="user_receiving"><?= $receivings_num ?></span>
                                            </a>
                                        </div>
                                        
<? if( $is_owner ): ?>
                                            <div class="span4 mobile-one">
                                                <a href="<?= current_url() ?>?<?= Profile::MENU_FAVORITE ?>" class="btn btn-small btn-block btn-ge <?= $active_menu == Profile::MENU_FAVORITE ? 'active-tab' : '' ?>">
                                                    Favorites<br />
                                                    <span class="user_bookmark"><?= $bookmarks_num ?></span>
                                                </a>
                                            </div>
<? endif; ?>
                                        
                                    </div>
                                </div><!-- /tab-->
                                <div id="tab_connections" class="tab-pane <?= ($active_tab == Profile::TAB_CONNECTIONS ? 'active' : '') ?>">
                                    <div class="row-fluid">
                                        <div class="span4 mobile-two">
                                            <a href="<?= current_url() ?>?<?= Profile::MENU_FOLLOWING ?>" class="btn btn-small btn-block btn-ge <?= $active_menu == Profile::MENU_FOLLOWING? 'active-tab' : '' ?>">
                                                <span class="user_following"><?= $followings_num ?></span><br />
                                                Following
                                            </a>
                                        </div>
                                        <div class="span4 mobile-two">
                                            <a href="<?= current_url() ?>?<?= Profile::MENU_FOLLOWER ?>" class="btn btn-small btn-block btn-ge <?= $active_menu == Profile::MENU_FOLLOWER ? 'active-tab' : '' ?>">
                                                <span class="user_follower"><?= $followers_num ?></span><br />
                                                Followers
                                            </a>
                                        </div>
                                        <? /** ?>
                                        <div class="span4">
                                            <a href="<?= current_url() ?>?<?= Profile::MENU_RATING ?>" class="btn btn-small btn-block btn-ge <?= $active_menu == Profile::MENU_RATING ? 'active-tab' : '' ?>">
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
                                        <div class="span4 mobile-two">
                                            <a href="<?= current_url() ?>?<?= Profile::MENU_NOTIFICATION ?>" class="btn btn-small btn-block btn-ge <?= $active_menu == Profile::MENU_NOTIFICATION ? 'active-tab' : '' ?>">
                                                <i class="fui-alert"></i>
                                                <br />Notifications
                                            </a>
                                        </div>
                                        <div class="span4 mobile-two">
                                            <a href="<?= current_url() ?>?<?= Profile::MENU_MESSAGE ?>" class="btn btn-small btn-block btn-ge <?= $active_menu == Profile::MENU_MESSAGE ? 'active-tab' : '' ?>">
                                                <i class="fui-mail"></i>
                                                <br />Messages
                                            </a>
                                        </div>
                                        <? /** ?>
                                        <div class="span4">
                                            <a href="<?= current_url() ?>?<?= Profile::MENU_SETTING ?>" class="btn btn-small btn-block btn-ge <?= $active_menu == Profile::MENU_SETTING ? 'active-tab' : '' ?>">
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
