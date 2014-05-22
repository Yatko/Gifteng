<?php namespace Mews\Useragent;

use Config, Request;

/*
 * This file is part of HTMLPurifier Bundle.
 * (c) 2012 Maxime Dizerens
 *
 * For the full copyright and license information, please view the LICENSE
 * file that was distributed with this source code.
 */

/**
 *
 * Modified
 * Laravel 4 Useragent package
 * @copyright Copyright (c) 2013 MeWebStudio
 * @version 1.0.0
 * @author Muharrem ERİN
 * @contact me@mewebstudio.com
 * @link http://www.mewebstudio.com
 * @date 2013-03-21
 * @license http://www.gnu.org/licenses/lgpl-2.1.html GNU Lesser General Public License, version 2.1
 *
 */

class Useragent {

    /**
     * @var  Useragent  singleton instance of the Useragent object
     */
    protected static $singleton;

    /**
     * @var  Useragent config instance of the Useragent::$config object
     */
    public static $config = array();

    public static $agent = NULL;

	public static $is_browser = FALSE;
	public static $is_robot	= FALSE;
	public static $is_mobile = FALSE;

	public static $languages = array();
	public static $charsets	= array();

	public static $platforms = array();
	public static $browsers	= array();
	public static $mobiles = array();
	public static $robots = array();

	public static $platform	= '';
	public static $browser = '';
	public static $version = '';
	public static $mobile = '';
	public static $robot = '';

    public static function instance()
    {

    	if ( ! Useragent::$singleton)
    	{

    		if ( ! self::$config)
    		{
    			self::$config = Config::get('useragent::config');
    		}

    		self::$agent = Request::server('HTTP_USER_AGENT');

			if ( ! is_null(self::$agent))
			{
				if (self::_load_agent_data())
				{
					self::_compile_data();
				}
			}

    		Useragent::$singleton = new Useragent();

    	}

    	return Useragent::$singleton;

    }

	/**
	 * Compile the User Agent Data
	 *
	 * @access	private
	 * @return	bool
	 */

	private static function _load_agent_data() {

		$return = FALSE;

		if(isset(self::$config['platforms']))
		{
			self::$platforms = self::$config['platforms'];
			$return = TRUE;
		}
		if(isset(self::$config['browsers']))
		{
			self::$browsers = self::$config['browsers'];
			$return = TRUE;
		}
		if(isset(self::$config['mobiles']))
		{
			self::$mobiles = self::$config['mobiles'];
			$return = TRUE;
		}
		if(isset(self::$config['robots']))
		{
			self::$robots = self::$config['robots'];
			$return = TRUE;
		}

		return $return;
	}

	// --------------------------------------------------------------------

	/**
	 * Compile the User Agent Data
	 *
	 * @access	private
	 * @return	bool
	 */
	private static function _compile_data() {

		self::_set_platform();

		foreach (array('_set_robot', '_set_browser', '_set_mobile') as $function)
		{
			if (self::$function() === TRUE)
			{
				break;
			}
		}

	}

	// --------------------------------------------------------------------

	/**
	 * Set the Platform
	 *
	 * @access	private
	 * @return	mixed
	 */
	private static function _set_platform() {
		if (is_array(self::$platforms) AND count(self::$platforms) > 0)
		{
			foreach (self::$platforms as $key => $val)
			{
				if (preg_match("|".preg_quote($key)."|i",self::$agent))
				{
					self::$platform = $val;
					return TRUE;
				}
			}
		}
		self::$platform = 'Unknown Platform';
	}

	// --------------------------------------------------------------------

	/**
	 * Set the Browser
	 *
	 * @access	private
	 * @return	bool
	 */
	private static function _set_browser() {
		if (is_array(self::$browsers) AND count(self::$browsers) > 0)
		{
			foreach (self::$browsers as $key => $val)
			{
				if (preg_match("|".preg_quote($key).".*?([0-9\.]+)|i", self::$agent, $match))
				{
					self::$is_browser = TRUE;
					self::$version = $match[1];
					self::$browser = $val;
					self::_set_mobile();
					return TRUE;
				}
			}
		}
		return FALSE;
	}

	// --------------------------------------------------------------------

	/**
	 * Set the Mobile Device
	 *
	 * @access	private
	 * @return	bool
	 */
	private static function _set_mobile() {
		if (is_array(self::$mobiles) AND count(self::$mobiles) > 0)
		{
			foreach (self::$mobiles as $key => $val)
			{
				if (FALSE !== (strpos(strtolower(self::$agent), $key)))
				{
					self::$is_mobile = TRUE;
					self::$mobile = $val;
					return TRUE;
				}
			}
		}
		return FALSE;
	}

	// --------------------------------------------------------------------

	/**
	 * Set the Robot
	 *
	 * @access	private
	 * @return	bool
	 */
	private static function _set_robot() {
		if (is_array(self::$robots) AND count(self::$robots) > 0)
		{
			foreach (self::$robots as $key => $val)
			{
				if (preg_match("|".preg_quote($key)."|i", self::$agent))
				{
					self::$is_robot = TRUE;
					self::$robot = $val;
					return TRUE;
				}
			}
		}
		return FALSE;
	}

	// --------------------------------------------------------------------

	/**
	 * Set the accepted languages
	 *
	 * @access	private
	 * @return	void
	 */
	private static function _set_languages()
	{
		if ((count(self::$languages) == 0) AND Request::server('HTTP_ACCEPT_LANGUAGE') AND Request::server('HTTP_ACCEPT_LANGUAGE') != '')
		{
			$languages = preg_replace('/(;q=[0-9\.]+)/i', '', strtolower(trim(Request::server('HTTP_ACCEPT_LANGUAGE'))));

			self::$languages = explode(',', $languages);
		}

		if (count(self::$languages) == 0)
		{
			self::$languages = array('Undefined');
		}
	}

	// --------------------------------------------------------------------

	/**
	 * Set the accepted character sets
	 *
	 * @access	private
	 * @return	void
	 */
	private static function _set_charsets()
	{
		if ((count(self::$charsets) == 0) AND Request::server('HTTP_ACCEPT_CHARSET') AND Request::server('HTTP_ACCEPT_CHARSET') != '')
		{
			$charsets = preg_replace('/(;q=.+)/i', '', strtolower(trim(Request::server('HTTP_ACCEPT_CHARSET'))));

			self::$charsets = explode(',', $charsets);
		}

		if (count(self::$charsets) == 0)
		{
			self::$charsets = array('Undefined');
		}
	}

	// --------------------------------------------------------------------

	/**
	 * Is Browser
	 *
	 * @access	public
	 * @return	bool
	 */
	public static function is_browser($key = NULL)
	{
		if ( ! self::$is_browser)
		{
			return FALSE;
		}

		// No need to be specific, it's a browser
		if ($key === NULL)
		{
			return TRUE;
		}

		// Check for a specific browser
		return array_key_exists($key, self::$browsers) AND self::$browser === self::$browsers[$key];
	}

	// --------------------------------------------------------------------

	/**
	 * Is Robot
	 *
	 * @access	public
	 * @return	bool
	 */
	public static function is_robot($key = NULL)
	{
		if ( ! self::$is_robot)
		{
			return FALSE;
		}

		// No need to be specific, it's a robot
		if ($key === NULL)
		{
			return TRUE;
		}

		// Check for a specific robot
		return array_key_exists($key, self::$robots) AND self::$robot === self::$robots[$key];
	}

	// --------------------------------------------------------------------

	/**
	 * Is Mobile
	 *
	 * @access	public
	 * @return	bool
	 */
	public  static function is_mobile($key = NULL)
	{
		if ( ! self::$is_mobile)
		{
			return FALSE;
		}

		// No need to be specific, it's a mobile
		if ($key === NULL)
		{
			return TRUE;
		}

		// Check for a specific robot
		return array_key_exists($key, self::$mobiles) AND self::$mobile === self::$mobiles[$key];
	}

	// --------------------------------------------------------------------

	/**
	 * Is this a referral from another site?
	 *
	 * @access	public
	 * @return	bool
	 */
	public static function is_referral()
	{
		if ( ! Request::server('HTTP_REFERER') OR Request::server('HTTP_REFERER') == '')
		{
			return FALSE;
		}
		return TRUE;
	}

	// --------------------------------------------------------------------

	/**
	 * Agent String
	 *
	 * @access	public
	 * @return	string
	 */
	public static function agent_string()
	{
		return self::$agent;
	}

	// --------------------------------------------------------------------

	/**
	 * Get Platform
	 *
	 * @access	public
	 * @return	string
	 */
	public static function platform()
	{
		return self::$platform;
	}

	// --------------------------------------------------------------------

	/**
	 * Get Browser Name
	 *
	 * @access	public
	 * @return	string
	 */
	public static function browser()
	{
		return self::$browser;
	}

	// --------------------------------------------------------------------

	/**
	 * Get the Browser Version
	 *
	 * @access	public
	 * @return	string
	 */
	public static function version()
	{
		return self::$version;
	}

	// --------------------------------------------------------------------

	/**
	 * Get The Robot Name
	 *
	 * @access	public
	 * @return	string
	 */
	public static function robot()
	{
		return self::$robot;
	}
	// --------------------------------------------------------------------

	/**
	 * Get the Mobile Device
	 *
	 * @access	public
	 * @return	string
	 */
	public static function mobile()
	{
		return self::$mobile;
	}

	// --------------------------------------------------------------------

	/**
	 * Get the referrer
	 *
	 * @access	public
	 * @return	bool
	 */
	public static function referrer()
	{
		return ( ! Request::server('HTTP_REFERER') OR Request::server('HTTP_REFERER') == '') ? '' : trim(Request::server('HTTP_REFERER'));
	}

	// --------------------------------------------------------------------

	/**
	 * Get the accepted languages
	 *
	 * @access	public
	 * @return	array
	 */
	public static function languages()
	{
		if (count(self::$languages) == 0)
		{
			self::_set_languages();
		}

		return self::$languages;
	}

	// --------------------------------------------------------------------

	/**
	 * Get the accepted Character Sets
	 *
	 * @access	public
	 * @return	array
	 */
	public static function charsets()
	{
		if (count(self::$charsets) == 0)
		{
			self::_set_charsets();
		}

		return self::$charsets;
	}

	// --------------------------------------------------------------------

	/**
	 * Test for a particular language
	 *
	 * @access	public
	 * @return	bool
	 */
	public static function accept_lang($lang = 'en')
	{
		return (in_array(strtolower($lang), self::languages(), TRUE));
	}

	// --------------------------------------------------------------------

	/**
	 * Test for a particular character set
	 *
	 * @access	public
	 * @return	bool
	 */
	public static function accept_charset($charset = 'utf-8')
	{
		return (in_array(strtolower($charset), self::charsets(), TRUE));
	}

    public static function run()
    {
    	print_r(self::$agent);
    }

}