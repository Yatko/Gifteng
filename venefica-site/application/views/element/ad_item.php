<?

/**
 * Input params:
 * 
 * ad: Ad_model
 * canBookmark: boolean (default: false)
 * canComment: boolean (default: false)
 * canShare: boolean (default: false)
 */

if ( !isset($canBookmark) ) $canBookmark = false;
if ( !isset($canComment) ) $canComment = false;
if ( !isset($canShare) ) $canShare = false;
$is_bookmarked = $ad->inBookmarks;
$is_owner = $ad->owner;
$id = $ad->id;
$img = $ad->getImageUrl();
$view_link = $ad->getViewUrl();

if ( $ad->statistics != null ) {
    $num_bookmarks = $ad->statistics->numBookmarks;
    $num_comments = $ad->statistics->numComments;
    $num_shares = $ad->statistics->numShares;
} else {
    $num_bookmarks = 0;
    $num_comments = 0;
    $num_shares = 0;
}
?>

<div class="row-fluid ge-item-image">
    <a href="<?= $view_link ?>"><img src = "<?= $img ?>" class="img" /></a>
    
    <div class="row-fluid">
        <div class="ge-icon-ribbon"></div>
    </div>
    
    <? if( !$canBookmark && !$canComment && !$canShare ): ?>
        
    <? else: ?>
        <div class="row-fluid">
            <div class="span12 ge-action">
                <div class="row-fluid">
                    <div class="span4">
                        <? if( !$canBookmark || $is_bookmarked || $is_owner ): ?>
                            <button type="button" class="btn btn-small btn-block btn-ge disabled">
                                <i class="fui-star-2"></i>
                                <span class="ad_bookmark_<?= $id ?>"><?= $num_bookmarks ?></span>
                            </button>
                        <? else: ?>
                            <button onclick="bookmark(this, <?= $id ?>);" type="button" class="btn btn-small btn-block btn-ge">
                                <i class="fui-star-2"></i>
                                <span class="ad_bookmark_<?= $id ?>"><?= $num_bookmarks ?></span>
                            </button>
                        <? endif; ?>
                    </div>
                    <div class="span4">
                        <? if( !$canComment ): ?>
                            <button type="button" class="btn btn-small btn-block btn-ge disabled">
                                <i class="fui-bubble"></i>
                                <span class="ad_comment_<?= $id ?>"><?= $num_comments ?></span>
                            </button>
                        <? else: ?>
                            <button onclick="startCommentModal(this, <?= $id ?>);" type="button" class="btn btn-small btn-block btn-ge">
                                <i class="fui-bubble"></i>
                                <span class="ad_comment_<?= $id ?>"><?= $num_comments ?></span>
                            </button>
                        <? endif; ?>
                    </div>
                    <div class="span4">
                        <? if( !$canShare ): ?>
                            <button type="button" class="btn btn-small btn-block btn-ge disabled">
                                <i class="ge-icon-share"></i>
                                <? /** ?>
                                <?= $num_shares ?>
                                <? /**/ ?>
                            </button>
                        <? else: ?>
                            
                            <?
                            $title = safe_parameter($ad->title);
                            $itemUrl = $ad->getViewUrl();
                            $imgUrl = $ad->getImageUrl();
                            ?>
                            
                            <button onclick="startSocialModal('<?=$title?>', '<?=$itemUrl?>', '<?=$imgUrl?>');" type="button" class="btn btn-small btn-block btn-ge">
                                <i class="ge-icon-share"></i>
                                <? /** ?>
                                <?= $num_shares ?>
                                <? /**/ ?>
                            </button>
                        <? endif; ?>
                    </div>
                </div>
            </div>
        </div>
    <? endif; ?>
</div><!--./ge-item-image-->
