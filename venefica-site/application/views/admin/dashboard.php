<div class="container ge-topspace">
    <div class="span12">
        
        <!--
        <div class="demo-row">
            <div class="demo-content-wide">
                <table class="table table-bordered">
                    <label>
                        Gift Dashboard
                    </label>
                    <thead>
                        <tr>
                            <th><label class="checkbox no-label toggle-all" for="checkbox-table-1"><input type="checkbox" value="" id="checkbox-table-1" data-toggle="checkbox"></label></th>
                            <th>*</th>
                            <th>Date</th>
                            <th>Gift</th>
                            <th>Decline</th>
                            <th>Accept</th>
                            <th>Reason</th>

                        </tr>
                    </thead>
                    <tbody>
                        <tr>
                            <td><label class="checkbox no-label" for="checkbox-table-2"><input type="checkbox" value="" id="checkbox-table-2" data-toggle="checkbox"></label></td>
                            <td></td>
                            <td>July 15, 2013</td>
                            <td><a href="#">Gift link</a></td>
                            <td><a href="#fakelink" class="btn btn-mini btn-block btn-danger">NO</a></td>
                            <td><a href="#fakelink" class="btn btn-mini btn-block btn-success">OK</a></td>
                            <td>
                                <input type="text" value="" placeholder="Reason" class="span3">
                                <a href="#fakelink" class="btn btn-mini btn-block btn-success">Send</a>
                            </td>
                        </tr>
                    </tbody>
                </table>
            </div>
        </div>
        -->

        <div class="demo-row">
            <div class="demo-content-wide">
                <table class="table table-bordered">
                    <label>
                        Business Dashboard
                    </label>
                    <thead>
                        <tr>
                            <th><label class="checkbox no-label toggle-all" for="checkbox-table-1"><input type="checkbox" value="" id="checkbox-table-1" data-toggle="checkbox"></label></th>
                            <th>Company</th>
                            <th>Contact</th>
                            <th>Phone</th>
                            <th>Email</th>
                            <th>ZIP</th>
                            <th>Category</th>
                        </tr>
                    </thead>
                    <tbody>
                    
                    <? foreach ( $users as $user ): ?>
                        <?
                        if ( !$user->businessAccount ) {
                            continue;
                        }
                        
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
            </div> <!-- /tables -->
        </div> <!-- /row -->

    </div>
</div>