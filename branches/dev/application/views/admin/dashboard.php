<?

/**
 * Input params:
 * 
 * users: array of User_model
 */

?>

<div class="container">
    <div class="span12">
        
        <? if( isset($unapprovedAds) && is_array($unapprovedAds) && count($unapprovedAds) > 0 ): ?>
        <label>Gift Dashboard</label>

        <table class="table table-bordered">
            <thead>
                <tr>
                    <th>*</th>
                    <th>Date</th>
                    <th>Gift</th>
                    <th>Accept</th>
                    <th>Decline & Reason</th>
                    <th>Online</th>
                </tr>
            </thead>
            <tbody>

            <? foreach ( $unapprovedAds as $ad ): ?>
                <?
                $id = $ad->id;
                $createdAt = $ad->getCreateDate();
                $view_link = $ad->getViewUrl();
                ?>

                <tr id="ad_<?=$id?>">
                    <td><?=$id?></td>
                    <td><?=$createdAt?></td>
                    <td><a href="<?=$view_link?>" target="_blank">Gift link</a></td>
                    <td><button type="button" id="approve_btn_<?=$id?>" onclick="approve_ad(<?=$id?>);" class="btn btn-mini btn-block btn-success">OK</button></td>
                    <td>
                        <div class="row-fluid">
                            <div class="span9">
                                <input id="reason_<?=$id?>" type="text" placeholder="Reason" class="span3">
                            </div>
                            <div class="span3">
                                <button type="button" id="unapprove_btn_<?=$id?>" onclick="unapprove_ad(<?=$id?>);" class="btn btn-mini btn-block btn-danger">NO</button>
                            </div>
                        </div>
                    </td>
                    <td><button type="button" id="online_btn_<?=$id?>" onclick="online_ad(<?=$id?>);" disabled="disabled" class="btn btn-mini btn-block btn-success">YES</button></td>
                </tr>

            <? endforeach; ?>

            </tbody>
        </table>
        <? endif; ?>

        <? if( isset($businessUsers) && is_array($businessUsers) && count($businessUsers) > 0 ): ?>
        <label>Business Dashboard</label>

        <table class="table table-bordered">
            <thead>
                <tr>
                    <th><label class="checkbox no-label toggle-all" for="user-table"><input type="checkbox" id="user-table" data-toggle="checkbox"></label></th>
                    <th>Company</th>
                    <th>Contact</th>
                    <th>Phone</th>
                    <th>Email</th>
                    <th>ZIP</th>
                    <th>Category</th>
                </tr>
            </thead>
            <tbody>

            <? foreach ( $businessUsers as $user ): ?>
                <?
                $id = $user->id;
                $company = $user->businessName;
                $contact = $user->contactName;
                $phone = $user->phoneNumber;
                $email = $user->email;
                $zipCode = $user->getZipCode();
                $category = $user->businessCategory;
                ?>

                <tr>
                    <td><label class="checkbox no-label" for="user-table-<?=$id?>"><input type="checkbox" id="user-table-<?=$id?>" data-toggle="checkbox"></label></td>
                    <td><?=$company?></td>
                    <td><?=$contact?></td>
                    <td><?=$phone?></td>
                    <td><a href="mailto:<?=$email?>"><?=$email?></a></td>
                    <td><?=$zipCode?></td>
                    <td><?=$category?></td>
                </tr>

            <? endforeach; ?>

            </tbody>
        </table>
        <? endif; ?>

    </div>
</div>