<? if( isset($is_modal) && $is_modal ): ?>

<iframe src="<?=$address?>" frameborder="0" width="100%" height="100%" style="height: 100%; width: 100%;"></iframe>

<? else: ?>

<html>
<head>
    <title>Gifteng</title>
</head>
<body style="margin: 0px; padding: 0px;">
    <iframe src="<?=$address?>" frameborder="0" width="100%" height="100%" style="height: 100%; width: 100%;"></iframe>
</body>
</html>

<? endif; ?>