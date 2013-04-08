<?php

/**
 * Common helper.
 * 
 * Various useful functions.
 */

if ( ! function_exists('display')) {
    function display($value, $expected) {
        $display = isset($value) && $value == $expected ? "block" : "none";
        return " style=\"display: $display;\"";
    }
}

if ( ! function_exists('getField')) {
    function getField($obj, $fieldName) {
        if ( property_exists($obj, $fieldName) ) {
            return $obj->$fieldName;
        }
        return null;
    }
}
