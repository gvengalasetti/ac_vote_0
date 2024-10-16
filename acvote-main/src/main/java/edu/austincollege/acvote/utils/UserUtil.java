package edu.austincollege.acvote.utils;

import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import edu.austincollege.acvote.AppConstants;

public class UserUtil {
	
	private static Logger log = LoggerFactory.getLogger(UserUtil.class);
	
	/**
	 * Utility method returning the authenticated user's userid.
	 * @return
	 */
	public static String userId() {
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		
		String username = "unknown";
		
		if (principal instanceof UserDetails) {
			  UserDetails usr = (UserDetails) principal;
			  username = usr.getUsername();
			  
			} else {
			  username = principal.toString();
			}
	
		return username;
	}
	
	
	/**
	 * Utility method returning the authenticated user's role.  Here we assume our users
	 * only have ONE role.  They are designed that way.
	 * 
	 * @return
	 */
	public static String userRole() {
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		
		String role = "unknown";
		
		if (principal instanceof UserDetails) {
			  UserDetails usr = (UserDetails) principal;

			  Collection<? extends GrantedAuthority> authorities = usr.getAuthorities();
			  if (authorities == null || authorities.isEmpty()) {
				  role = AppConstants.ROLE_VOTER;
			  } else {
				  for (GrantedAuthority ga : authorities) {
					  role = ga.getAuthority();
					  break;
				  }
			  }
			  
			} else {

			  role = AppConstants.ROLE_VOTER;
			}
		
		return role;
	}
}
