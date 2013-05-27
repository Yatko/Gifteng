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

// making a safer world

if ( ! function_exists('safe_content')) {
    function safe_content($str) {
        $str = strip_tags($str);
        return $str;
    }
}
if ( ! function_exists('safe_parameter')) {
    function safe_parameter($str) {
        $str = strip_slashes($str);
        return $str;
    }
}

// object and properties related helpers

if ( ! function_exists('getField')) {
    function getField($obj, $fieldName) {
        if ( $obj == null || $fieldName == null ) {
            return null;
        }
        if ( property_exists($obj, $fieldName) ) {
            return $obj->$fieldName;
        }
        return null;
    }
}

if ( ! function_exists('hasField')) {
    function hasField($obj, $fieldName) {
        if ( $obj == null || $fieldName == null ) {
            return FALSE;
        }
        return property_exists($obj, $fieldName);
    }
}

// associative array related helpers

if ( ! function_exists('getElement')) {
    function getElement($array, $elem) {
        if ( $array == null || $elem == null ) {
            return null;
        }
        if (array_key_exists($elem, $array) ) {
            return $array[$elem];
        }
        return null;
    }
}

if ( ! function_exists('hasElement')) {
    function hasElement($array, $elem) {
        if ( $array == null || $elem == null ) {
            return FALSE;
        }
        return array_key_exists($elem, $array);
    }
}
