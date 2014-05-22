# Useragent for Laravel 4

A simple [Laravel 4](http://four.laravel.com/) service provider for including the [Useragent for Laravel 4](https://github.com/mewebstudio/Useragent).

## Installation

The Useragent Service Provider can be installed via [Composer](http://getcomposer.org) by requiring the
`mews/useragent` package and setting the `minimum-stability` to `dev` (required for Laravel 4) in your
project's `composer.json`.

```json
{
    "require": {
        "laravel/framework": "4.0.*",
        "mews/useragent": "dev-master"
    },
    "minimum-stability": "dev"
}
```

Update your packages with ```composer update``` or install with ```composer install```.

## Usage

To use the Useragent Service Provider, you must register the provider when bootstrapping your Laravel application. There are
essentially two ways to do this.

Find the `providers` key in `app/config/app.php` and register the Useragent Service Provider.

```php
    'providers' => array(
        // ...
        'Mews\Useragent\UseragentServiceProvider',
    )
```

Find the `aliases` key in `app/config/app.php`.

```php
    'aliases' => array(
        // ...
        'Useragent' => 'Mews\Useragent\Facades\Useragent',
    )
```

This example attempts to determine whether the user agent browsing your site is a web browser, a mobile device, or a robot. It will also gather the platform information if it is available.
```
if (Useragent::is_browser())
{
    $agent = Useragent::browser().' '.Agent::version();
}
elseif (Useragent::is_robot())
{
    $agent = Useragent::robot();
}
elseif (Useragent::is_mobile())
{
    $agent = Useragent::mobile();
}
else
{
    $agent = 'Unidentified User Agent';
}

echo $agent;

echo Useragent::platform(); // Platform info (Windows, Linux, Mac, etc.)
```
# Method Reference
All methods in the Agent class are the same as the methods in the Codeigniter user agent library.
You can read the documentation <http://codeigniter.com/user_guide>

Note all method calls are static
Eg
``` 
Useragent::is_browser(); 
```



