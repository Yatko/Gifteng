<?php

if ( !is_empty($obj) ) {
    $json = json_encode($obj);
}
if ( !is_empty($json) ) {
    echo $json;
}

?>