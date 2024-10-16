package edu.austincollege.acvote.config;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import edu.austincollege.acvote.AppConstants;
import edu.austincollege.acvote.users.AcUser;
import edu.austincollege.acvote.users.dao.UserDao;
import jakarta.servlet.ServletException;

/**
 * An instance of this class is used by spring security authentication to allow us to augment the 
 * role of the authenticated user if they are a "special" acuser as determined by the admin user.
 * 
 * @author mahiggs
 *
 */
@Component
public class CustomAuthenticationSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

	private static Logger log = LoggerFactory.getLogger(CustomAuthenticationSuccessHandler.class);
	
	@Autowired
	private UserDao userDao;
	
	
	@Override
	public void onAuthenticationSuccess(jakarta.servlet.http.HttpServletRequest request,
			jakarta.servlet.http.HttpServletResponse response, Authentication auth)
			throws IOException, ServletException {
	
		
		super.onAuthenticationSuccess(request, response, auth);
		
		/*
		 * See if the user is special by looking in our backend data store managed by the admin user.
		 * if special we ADD the special user's role to the authenticated user's granted authorities. 
		 */
		try {
			AcUser theUser = userDao.userById(auth.getName());
			
			log.debug("special user [] found; adding role: {}",theUser.getUid(), theUser.getRole());
			GrantedAuthority ga = new SimpleGrantedAuthority("ROLE_"+theUser.getRole());
			

			List<GrantedAuthority> updatedAuthorities = new ArrayList<>(auth.getAuthorities());
			updatedAuthorities.add(ga); //add your role here [e.g., new SimpleGrantedAuthority("ROLE_NEW_ROLE")]

			Authentication newAuth = new UsernamePasswordAuthenticationToken(auth.getPrincipal(), auth.getCredentials(), updatedAuthorities);

			SecurityContextHolder.getContext().setAuthentication(newAuth);
			
			
		} catch (Exception e) {
			
			/*
			 * default case (authenticated user is not a special user), so
			 * we default to VOTER role only.
			 */
			
			log.debug("special user [{}] NOT found; adding role: {}", auth.getName(), AppConstants.ROLE_VOTER);
			GrantedAuthority ga = new SimpleGrantedAuthority("ROLE_"+AppConstants.ROLE_VOTER);

			List<GrantedAuthority> updatedAuthorities = new ArrayList<>(auth.getAuthorities());
			updatedAuthorities.add(ga); //add your role here [e.g., new SimpleGrantedAuthority("ROLE_NEW_ROLE")]

			Authentication newAuth = new UsernamePasswordAuthenticationToken(auth.getPrincipal(), auth.getCredentials(), updatedAuthorities);

			SecurityContextHolder.getContext().setAuthentication(newAuth);

		}
		
		
	}
}