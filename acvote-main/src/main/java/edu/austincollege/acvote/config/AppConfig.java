package edu.austincollege.acvote.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


/**
 * This configuration file makes sure our custom user in model interceptor is
 * added to our service.  In doing so, we provide access to the authenticated 
 * user object for every controller endpoint that delivers an html page.  Thus,
 * every one of our site pages will be user aware.
 * 
 * @author mahiggs
 * @see UserInModelInterceptor
 */
@Configuration
public class AppConfig implements WebMvcConfigurer {


	  @Override
	   public void addInterceptors(InterceptorRegistry registry) {
	      registry.addInterceptor(new UserInModelInterceptor() );
	   }
	
}
