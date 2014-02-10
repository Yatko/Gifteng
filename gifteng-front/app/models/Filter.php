<?php

class Filter {

	const TYPE_MEMBER = "MEMBER";
	const TYPE_BUSINESS = "BUSINESS";

	const FILTER_TYPE_ACTIVE = "ACTIVE";
	const FILTER_TYPE_GIFTED = "GIFTED";

	public $searchString;
	public $categories;
	public $distance;
	public $longitude;
	public $latitude;
	public $minPrice;
	public $maxPrice;
	public $hasPhoto;
	public $type;
	public $includeOwned;
	public $orderAsc;
	public $includeCannotRequest;
	public $includeOnlyCannotRequest;
	public $includeInactive;
	public $includeOnlyInactive;

}
