<? /** ?>
<? if( !in_array("facebook", $networks) ): ?>
    <button onclick="connect_to_facebook();">Connect to Facebook</button>
    <button onclick="sign_in_facebook();">Sign in Facebook</button>
<? endif; ?>

<? if( !in_array("twitter", $networks) ): ?>
    <button onclick="connect_to_twitter();">Connect to Twitter</button>
    <button onclick="sign_in_twitter();">Sign in Twitter</button>
<? endif; ?>

<button onclick="shareMessage('test');">Share simple message</button>
<? /**/ ?>

<button onclick="shareOnFacebook();">Post to your wall</button>
