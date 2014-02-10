<? if( isset($is_modal) && $is_modal ): ?>

<iframe src="<?=$address?>" frameborder="0" width="100%" height="100%" style="height: 100%; width: 100%;"></iframe>

<? else: ?>

<!DOCTYPE HTML>
<html>
<head>
    <title>Gifteng</title>
    <style type="text/css">
        html, body {
            margin: 0;
            padding: 0;
            height: 100%;
        }
        iframe {
            position: absolute;
            top: 0;
            left: 0;
            width: 100%;
            height: 100%;
            border: none;
            box-sizing: border-box; -moz-box-sizing: border-box; -webkit-box-sizing: border-box;
        }
    </style>
</head>
<body>
    <iframe src="<?=$address?>"></iframe>
</body>
</html>

<? endif; ?>