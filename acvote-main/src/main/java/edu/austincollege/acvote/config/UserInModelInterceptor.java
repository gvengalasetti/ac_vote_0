package edu.austincollege.acvote.config;

import java.security.Principal;
import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

/**
 * An instance of this class is used to define before and after methods during the handling
 * of an HTTP request.  
 * <p>
 * The <code>preHandle</code> method does nothing.  The <code>postHandle</code> method will be called
 * after all controller logic is done, but before our template engine (freemarker) gets a chance to 
 * render the HTML response.  The <code>afterCompletion</code> method does nothing.
 * </p>
 * <p>
 * Our interceptor will ensure that the model prepared by the controller for the view will contain
 * a handle on the current User authentication object (ie, always has a handle on the authenticate
 * user and her roles).
 * </p>
 * <p>
 * To reference the user...Use freemarkers interpolations in your view template as needed.
 * <ul>
 * <li> <code>${user}</code> ... for Springs principle object (the authenticated user auth object)</li>
 * <li> <code>${username}</code> ... for the current authenticated user id/name </li>
 * <li> <code>${userroles}</code> ... for a list of roles assigned to the authenticated user </li>
 * </ul>
 * </p>
 * 
 * @author mahiggs
 *
 */
@Component
public class UserInModelInterceptor implements HandlerInterceptor {

	private static Logger log = LoggerFactory.getLogger(UserInModelInterceptor.class);

	
	public UserInModelInterceptor() {
		
		log.info("using UserInModelInterceptor");
		
	}
	



	@Override
	public void postHandle(jakarta.servlet.http.HttpServletRequest aRequest,
			jakarta.servlet.http.HttpServletResponse aResponse, Object handler, ModelAndView aModelAndView)
			throws Exception {
		
		
		// log.info("intercept");
		
		// only insert auth user information if we HAVE a model with which to work
		if (aModelAndView != null) {

			// paranoid programming....make sure we have an authentication context
			// from which we can get the authenticated user.   For SOME parts of our
			// site (public) we may not have one.
			if (SecurityContextHolder.getContext() == null) {
				log.debug("no security context");
				return;
			}
			
			// our main authentication object has what we need.
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();

			if (auth == null) {
				log.debug("no auth");
				return;
			}

			if (aRequest == null) {
				log.debug("no request");
				return;
			}

			// log.debug("{}",aRequest);
			
			/*
			 * Now we are ready to stuff the user auth data into our view model.
			 */
			Principal user = aRequest.getUserPrincipal();
			aModelAndView.addObject("user", user);

			if (user != null)
				aModelAndView.addObject("userid", user.getName());

			// get the authorizations for the roles 
			Collection<GrantedAuthority> authorities = (Collection<GrantedAuthority>) auth.getAuthorities();

			if (!authorities.isEmpty())
				aModelAndView.addObject("userroles", authorities.toArray());
		} else {
			 // log.debug("no mav");
		}
		
	}


}