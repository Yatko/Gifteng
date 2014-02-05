<?php

/**
$conn = mysql_connect("gifteng.com", "venefica_mysqlu", "labs9901"); //LIVE
mysql_select_db("venefica_mysqldb", $conn);

for ( $i = 0; $i < 100; $i++ ) {
    $code = str_pad(200 + $i, 4, '0', STR_PAD_LEFT);
    
    $sql = "
    insert into invitation (
        `code`,
        `createdAt`,
        `email`,
        `expired`,
        `expiresAt`,
        `numAvailUse`,
        `source`,
        `otherSource`
    ) values (
        '".$code."',
        '2013-09-13 10:00:00',
        'ac@veneficalabs.com',
        '0',
        '2014-09-13 10:00:00',
        '5000',
        'other',
        'manual'
    );
    ";
    $res = mysql_query($sql, $conn);
    if ( !$res ) echo mysql_error($conn).'<br/>';
}
/**/

/**
$conn = mysql_connect("veneficalabs.com", "venefica_gifteng", "labs9901"); //DEV
mysql_select_db("venefica_gifteng", $conn);

$array = array('120', '121', '123', '126', '127', '129', '132', '135', '136', '140', '152', '155', '223', '224', '233', '234', '235', '236', '237', '237', '241', '242', '244', '245', '277');
foreach ( $array as $id ) {
    $res = mysql_query("DELETE FROM `image` WHERE `id` in (SELECT `images_id` FROM `ad_image` WHERE `addata_id` in (SELECT `adData_id` FROM `ad` WHERE `id`='".$id."'));", $conn);
    if ( !$res ) echo mysql_error($conn).'<br/>';
    $res = mysql_query("DELETE FROM `ad_image` WHERE `addata_id` in (SELECT `adData_id` FROM `ad` WHERE `id`='".$id."');", $conn);
    if ( !$res ) echo mysql_error($conn).'<br/>';
    $res = mysql_query("DELETE FROM `image` WHERE `id` in (SELECT `mainImage_id` FROM `addata` WHERE `id` in (SELECT `adData_id` FROM `ad` WHERE `id`='".$id."'));", $conn);
    if ( !$res ) echo mysql_error($conn).'<br/>';
    $res = mysql_query("DELETE FROM `image` WHERE `id` in (SELECT `thumbImage_id` FROM `addata` WHERE `id` in (SELECT `adData_id` FROM `ad` WHERE `id`='".$id."'));", $conn);
    if ( !$res ) echo mysql_error($conn).'<br/>';
    $res = mysql_query("DELETE FROM `image` WHERE `id` in (SELECT `barcodeImage_id` FROM `businessaddata` WHERE `id` in (SELECT `adData_id` FROM `ad` WHERE `id`='".$id."'));", $conn);
    if ( !$res ) echo mysql_error($conn).'<br/>';
    $res = mysql_query("DELETE FROM `businessaddata_available_days` WHERE `businessaddata_id` in (SELECT `id` FROM `businessaddata` WHERE `id` in (SELECT `adData_id` FROM `ad` WHERE `id`='".$id."'));", $conn);
    if ( !$res ) echo mysql_error($conn).'<br/>';
    $res = mysql_query("DELETE FROM `businessaddata` WHERE `id` in (SELECT `adData_id` FROM `ad` WHERE `id`='".$id."');", $conn);
    if ( !$res ) echo mysql_error($conn).'<br/>';
    $res = mysql_query("DELETE FROM `memberaddata` WHERE `id` in (SELECT `adData_id` FROM `ad` WHERE `id`='".$id."');", $conn);
    if ( !$res ) echo mysql_error($conn).'<br/>';
    $res = mysql_query("DELETE FROM `addata` WHERE `id`=(select `adData_id` from ad where `id`='".$id."');", $conn);
    if ( !$res ) echo mysql_error($conn).'<br/>';
    $res = mysql_query("delete from `viewer` where `ad_id`='".$id."';", $conn);
    if ( !$res ) echo mysql_error($conn).'<br/>';
    $res = mysql_query("delete from `rating` where `ad_id`='".$id."';", $conn);
    if ( !$res ) echo mysql_error($conn).'<br/>';
    $res = mysql_query("delete from `spammark` where `ad_id`='".$id."';", $conn);
    if ( !$res ) echo mysql_error($conn).'<br/>';
    $res = mysql_query("delete from `comment` where `ad_id`='".$id."';", $conn);
    if ( !$res ) echo mysql_error($conn).'<br/>';
    $res = mysql_query("DELETE FROM `user_transaction` WHERE `ad_id`='".$id."';", $conn);
    if ( !$res ) echo mysql_error($conn).'<br/>';
    $res = mysql_query("DELETE FROM `user_transaction` WHERE `request_id` in (select `id` from `request` where `ad_id`='".$id."');", $conn);
    if ( !$res ) echo mysql_error($conn).'<br/>';
    $res = mysql_query("DELETE FROM `useractivity` WHERE `ad_id`='".$id."';", $conn);
    if ( !$res ) echo mysql_error($conn).'<br/>';
    $res = mysql_query("DELETE FROM `useractivity` WHERE `request_id` in (select `id` from `request` where `ad_id`='".$id."');", $conn);
    if ( !$res ) echo mysql_error($conn).'<br/>';
    $res = mysql_query("DELETE FROM `message` WHERE `request_id` in (select `id` from `request` where `ad_id`='".$id."');", $conn);
    if ( !$res ) echo mysql_error($conn).'<br/>';
    $res = mysql_query("delete from `request` where `ad_id`='".$id."';", $conn);
    if ( !$res ) echo mysql_error($conn).'<br/>';
    $res = mysql_query("delete from `bookmark` where `ad_id`='".$id."';", $conn);
    if ( !$res ) echo mysql_error($conn).'<br/>';
    $res = mysql_query("DELETE FROM `ad` WHERE `id`='".$id."';", $conn);
    if ( !$res ) echo mysql_error($conn).'<br/>';
}
/**/

?>
