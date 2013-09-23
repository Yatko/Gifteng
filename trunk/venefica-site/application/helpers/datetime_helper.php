<?php

// date and time helpers

if ( ! function_exists('convertDateToTimestamp')) {
    function convertDateToTimestamp($strDate) {
        $date = date_create_from_format('j F, Y', $strDate);
        $timestamp = date_format($date, "U");
        return $timestamp;
    }
}
if ( ! function_exists('convertTimestampToDate')) {
    function convertTimestampToDate($timestamp) {
        $strDate = date("j F, Y", $timestamp);
        return $strDate;
    }
}


// various datetime formatters based on its usage

if ( ! function_exists('convertTimestampToDateForProfile')) {
    function convertTimestampToDateForProfile($timestamp) {
        $strDate = date("F Y", $timestamp);
        return $strDate;
    }
}
if ( ! function_exists('convertTimestampToDateForComment')) {
    function convertTimestampToDateForComment($timestamp) {
        return humanTiming($timestamp);
    }
}
if ( ! function_exists('convertTimestampToDateForMessage')) {
    function convertTimestampToDateForMessage($timestamp) {
        //$strDate = date("F j, Y", $timestamp);
        $strDate = date("n/j, g:i a", $timestamp);
        return $strDate;
    }
}
if ( ! function_exists('convertTimestampToDate')) {
    function convertTimestampToDate($timestamp) {
        $strDate = date("F j, Y", $timestamp);
        return $strDate;
    }
}



if ( ! function_exists('convertHourToTimestamp')) {
    function convertHourToTimestamp($strDate) {
        $date = date_create_from_format('G', $strDate);
        $timestamp = date_format($date, "U");
        return $timestamp;
    }
}
if ( ! function_exists('convertTimestampToHour')) {
    function convertTimestampToHour($timestamp) {
        $strDate = date("G", $timestamp);
        return $strDate;
    }
}

if ( ! function_exists('humanTiming')) {
    function humanTiming($timestamp) {
        $server_timestamp = time();
        $timestamp = $server_timestamp - $timestamp; // to get the time since that moment
        
        $tokens = array (
            31536000 => 'year',
            2592000 => 'month',
            604800 => 'week',
            86400 => 'day',
            3600 => 'hour',
            60 => 'minute',
            1 => 'second'
        );

        foreach ($tokens as $unit => $text) {
            if ($timestamp < $unit) continue;
            $numberOfUnits = floor($timestamp / $unit);
            return $numberOfUnits . ' ' . $text . ($numberOfUnits > 1 ? 's' : '');
        }
    }
}
