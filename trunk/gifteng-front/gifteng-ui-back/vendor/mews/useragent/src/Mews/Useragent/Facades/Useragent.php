<?php namespace Mews\Useragent\Facades;

use Illuminate\Support\Facades\Facade;

class Useragent extends Facade {

    /**
     * Get the registered name of the component.
     *
     * @return string
     */
    protected static function getFacadeAccessor() { return 'useragent'; }

}