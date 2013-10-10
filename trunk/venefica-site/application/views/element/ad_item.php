<?

/**
 * Input params:
 * 
 * ad: Ad_model
 * canBookmark: boolean (default: false)
 * canComment: boolean (default: false)
 * canShare: boolean (default: false)
 * size: int (default: LIST_AD_IMAGE_SIZE)
 */

if ( !isset($canBookmark) ) $canBookmark = false;
if ( !isset($canComment) ) $canComment = false;
if ( !isset($canShare) ) $canShare = false;
if ( !isset($size) ) $size = LIST_AD_IMAGE_SIZE;

$is_bookmarked = $ad->inBookmarks;
$is_owner = $ad->owner;
$id = $ad->id;
$title = safe_parameter($ad->title);
$img = $ad->getImageUrl($size);
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
                    <div class="span4 mobile-one">
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
                    <div class="span4 mobile-one">
                        <? if( !$canComment ): ?>
                            <button type="button" class="btn btn-small btn-block btn-ge disabled">
                                <i class="fui-bubble"></i>
                                <span class="ad_comment_<?= $id ?>"><?= $num_comments ?></span>
                            </button>
                        <? else: ?>
                            <button onclick="startCommentModal(<?= $id ?>);" type="button" class="btn btn-small btn-block btn-ge">
                                <i class="fui-bubble"></i>
                                <span class="ad_comment_<?= $id ?>"><?= $num_comments ?></span>
                            </button>
                        <? endif; ?>
                    </div>
                    <div class="span4 mobile-one">
                        <? if( !$canShare ): ?>
                            <button type="button" class="btn btn-small btn-block btn-ge disabled">
                                <i class="ge-icon-share"></i>
                                <? /** ?>
                                <?= $num_shares ?>
                                <? /**/ ?>
                            </button>
                        <? else: ?>
                            <button onclick="startSocialModal('<?=$title?>', '<?=$view_link?>', '<?=$img?>');" type="button" class="btn btn-small btn-block btn-ge">
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
