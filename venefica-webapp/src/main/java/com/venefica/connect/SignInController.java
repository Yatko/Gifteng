package com.venefica.connect;

import java.util.List;

import javax.inject.Inject;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionFactory;
import org.springframework.social.connect.ConnectionFactoryLocator;
import org.springframework.social.connect.UsersConnectionRepository;
import org.springframework.social.connect.support.OAuth1ConnectionFactory;
import org.springframework.social.connect.support.OAuth2ConnectionFactory;
import org.springframework.social.connect.web.SignInAdapter;
import org.springframework.social.support.URIBuilder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.servlet.view.RedirectView;

/**
 * Implements "Sign In" behavior using credentials of well-known social network providers. This
 * controller is based on {@link org.springframework.social.connect.web.ProviderSignInController}.
 * 
 * @author Sviatoslav Grebenchukov
 */
@Controller
@RequestMapping("/signin")
public class SignInController {

	private final static Log logger = LogFactory.getLog(SignInController.class);

	@Inject
	private ConnectionFactoryLocator connectionFactoryLocator;

	@Inject
	private UsersConnectionRepository usersConnectionRepository;

	@Inject
	private SignInAdapter signInAdapter;

	@Inject
	private ConnectSupport connectSupport;

	private String signInErrorUrl = "/signin/error";

	/**
	 * Sets URL of application's sign-in error page.
	 * 
	 * @param signInErrorUrl
	 *            the sign-in error URL
	 */
	public void setSignInErrorUrl(String signInErrorUrl) {
		this.signInErrorUrl = signInErrorUrl;
	}

	/**
	 * Process a sign in request by commencing the process of establishing a connection to the
	 * provider on behalf of the user.
	 */
	@RequestMapping(value = "/{providerId}", method = RequestMethod.GET)
	public RedirectView signIn(@PathVariable String providerId, NativeWebRequest request) {
		ConnectionFactory<?> connectionFactory = connectionFactoryLocator
				.getConnectionFactory(providerId);
		try {
			return new RedirectView(connectSupport.buildOAuthUrl(connectionFactory, request));
		} catch (Exception e) {
			return new RedirectView(URIBuilder.fromUri(signInErrorUrl)
					.queryParam(ERROR_CODE, "provider").build().toString(), true);
		}
	}

	/**
	 * Process the authentication callback from an OAuth 1 service provider.
	 */
	@RequestMapping(value = "/{providerId}/" + ConnectSupport.CALLBACK_PATH, method = RequestMethod.GET, params = "oauth_token")
	public RedirectView oauth1Callback(@PathVariable String providerId, NativeWebRequest request) {
		try {
			OAuth1ConnectionFactory<?> connectionFactory = (OAuth1ConnectionFactory<?>) connectionFactoryLocator
					.getConnectionFactory(providerId);
			Connection<?> connection = connectSupport
					.completeConnection(connectionFactory, request);
			return handleSignIn(connection, request);
		} catch (Exception e) {
			logger.warn("Exception while handling OAuth1 callback (" + e.getMessage()
					+ "). Redirecting to " + signInErrorUrl + ".");
			return new RedirectView(URIBuilder.fromUri(signInErrorUrl)
					.queryParam(ERROR_CODE, "provider").build().toString(), true);
		}
	}

	/**
	 * Process the authentication callback when neither the oauth_token or code parameter is given,
	 * likely indicating that the user denied authorization with the provider.
	 */
	@RequestMapping(value = "/{providerId}/" + ConnectSupport.CALLBACK_PATH, method = RequestMethod.GET)
	public RedirectView cancelAuthorizationCallback(@PathVariable("providerId") String providerId) {
		return new RedirectView(URIBuilder.fromUri(signInErrorUrl).queryParam(ERROR_CODE, "reject")
				.build().toString(), true);
	}

	/**
	 * Handles sign-in errors
	 */
	@RequestMapping("/error")
	public void error() {
	}

	/**
	 * Process the authentication callback from an OAuth 2 service provider. Called after the user
	 * authorizes the authentication, generally done once by having her of she click "Allow" in
	 * their web browser at the provider's site.
	 */
	@RequestMapping(value = "/{providerId}/" + ConnectSupport.CALLBACK_PATH, method = RequestMethod.GET, params = "code")
	public RedirectView oath2Callback(@PathVariable String providerId,
			@RequestParam("code") String code, NativeWebRequest request) {

		try {
			OAuth2ConnectionFactory<?> connectionFactory = (OAuth2ConnectionFactory<?>) connectionFactoryLocator
					.getConnectionFactory(providerId);
			Connection<?> connection = connectSupport
					.completeConnection(connectionFactory, request);
			return handleSignIn(connection, request);
		} catch (Exception e) {
			logger.warn("Exception while handling OAut2 callback (" + e.getMessage()
					+ "). Redirecting to " + signInErrorUrl);
			return new RedirectView(URIBuilder.fromUri(signInErrorUrl)
					.queryParam(ERROR_CODE, "provider").build().toString(), true);
		}
	}

	private RedirectView handleSignIn(Connection<?> connection, NativeWebRequest request) {
		List<String> userIds = usersConnectionRepository.findUserIdsWithConnection(connection);

		if (userIds.size() == 1) {
			usersConnectionRepository.createConnectionRepository(userIds.get(0)).updateConnection(
					connection);
			// Generate session token and perform redirect to the "special" URL.
			String redirectUrl = signInAdapter.signIn(userIds.get(0), connection, request);
			return new RedirectView(redirectUrl, true);
		} else {
			return new RedirectView(URIBuilder.fromUri(signInErrorUrl)
					.queryParam(ERROR_CODE, "multiple_users").build().toString());
		}
	}

	private final static String ERROR_CODE = "error";
}
