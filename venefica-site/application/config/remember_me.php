<?php

// if your authorize function is a model, specify the model name here so that 
// this model will be instantiated
$config['requiremodel'] = 'user_model';

// If your authorize function is a custom library, specify the library name here
// so that this library will be instatiated
$config['requirelibrary'] = 'usermanagement_service';

//// Provide the reference to your authorize function here as a string
//$config['authfunc'] = 'Usermanagement_service::authorize';

?>