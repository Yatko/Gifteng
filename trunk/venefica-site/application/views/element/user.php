<?

/**
 * Input params:
 * 
 * user: User_model
 * canEdit: boolean (default: false)
 * small: boolean (default: true)
 */

if ( !isset($small) ) $small = true;
if ( !isset($canEdit) ) $canEdit = false;

$is_owner = isOwner($user);
$is_in_followings = $user->inFollowings;

$id = $user->id;
$name = $user->getFullName();
$profile_link = $user->getProfileUrl();
$img = $user->getAvatarUrl();
$joined = $user->getJoinDateHumanTiming(); //$user->getJoinDate();
$location = $user->getLocation();
$points = $user->getPoints();

if ( $small == false ) {
    $label_css = 'label';
} else {
    $label_css = 'label label-small';
}

?>

<div class="ge-user-image">
    <? if( $canEdit && $is_owner ): ?>
        
        <? /** ?>
        <div class="row-fluid">
            <div class="span12 ge-action">
                <div class="row-fluid">
                    <div class="span12">
                        <button class="btn btn-small btn-block btn-ge">Your Profile Picture</button>
                    </div>
                </div>
            </div>
        </div>
        <? /**/ ?>
        
        <a data-toggle="modal" href="#avatarContainer">
            <img id="avatarImage" src="<?=$img?>" class="img-rounded">
        </a>
    <? else: ?>
        <a href="<?= $profile_link ?>"><img src="<?= $img ?>" class="img img-rounded"></a>
    <? endif; ?>
</div>
<div class="ge-detail">
    <div class="ge-name">
        <a href="<?= $profile_link ?>"><?= $name ?></a>
        <? if( $canEdit && $is_owner ): ?>
            <button onclick="startEditProfileModal()" class="btn btn-small btn-ge"><i class="ge-icon-pencil"></i></button>
        <? endif; ?>
    </div>
    <? if ($joined != ''): ?>
        <div class="ge-age">Giftenger since <?= $joined ?></div>
    <? endif; ?>
    <? if ($location != ''): ?>
        <div class="ge-location"><?= $location ?></div>
    <? endif; ?>
    <div class="ge-points">
    <? if ($points != ''): ?>
        <span class="<?= $label_css ?>">
        
        <? if ($is_owner): ?>
            <a href="http://help.gifteng.com/entries/25071768" target="_blank">Generosity Score <?= $points ?></a>
        <? else: ?>
            G <?= $points ?>
        <? endif; ?>
        
        </span>
    <? endif; ?>
    <? if (!$is_owner): ?>
        <? if ($is_in_followings): ?>
            <span onclick="follow_unfollow(<?= $id ?>);" class="user_<?= $id ?> ge-user-follow <?= $label_css ?> label-ge active link">Unfollow</span>
        <? else: ?>
            <span onclick="follow_unfollow(<?= $id ?>);" class="user_<?= $id ?> ge-user-unfollow <?= $label_css ?> label-ge active link">Follow</span>
        <? endif; ?>
    <? endif; ?>
    </div>
</div>
