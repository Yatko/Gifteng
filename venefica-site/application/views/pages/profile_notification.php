<?

/**
 * Input params:
 * 
 * user: User_model
 * user_setting: UserSetting_model
 */

?>

<div class="row">
    <div class="span6 offset3">
        <div class="well ge-well ge-form">
            <div class="row-fluid">
                <div class="span12">
                    
                    <?=form_open('/profile?notification', '', array("userId" => $user->id))?>
                    
                    <table class="table table-hover">
                        <thead>
                            <tr>
                                <th>Notify me when..</th>
                                <th><label class="checkbox no-label toggle-all" for="notification-table"><input type="checkbox" id="notification-table" data-toggle="checkbox"></label></th>
                            </tr>
                        </thead>
                        <tbody>

<?
$index = 0;
?>
<? foreach( UserSetting_model::$NOTIFICATIONS as $notification ): ?>
    <?
    $index++;
    if (
        $user_setting != null &&
        $user_setting->notifiableTypes != null &&
        in_array($notification, $user_setting->notifiableTypes)
    ) {
        $checked = ' checked';
    } else {
        $checked = '';
    }
    ?>
                            
                            <tr>
                                <td><?=lang('notification_'.$notification)?></td>
                                <td><label class="checkbox no-label" for="checkbox-table-<?=$index?>"><input type="checkbox" name="notifiableTypes[]" value="<?=$notification?>" id="checkbox-table-<?=$index?>" data-toggle="checkbox" <?=$checked?>></label></td>
                            </tr>
<? endforeach; ?>
                            
                        </tbody>
                    </table>

                    <button type="submit" class="btn pull-right">SAVE</button>
                    
                    <?=form_close()?>
                    
                </div>
            </div>
        </div><!--./ge-well-->
    </div>
</div>
