package edu.austincollege.acvote.config;

import java.io.IOException;

import javax.servlet.ServletException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import edu.austincollege.acvote.AppConstants;
import edu.austincollege.acvote.users.dao.UserDao;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfiguration {
	
	private static Logger log = LoggerFactory.getLogger(SecurityConfiguration.class);
	

	@Autowired
	CustomAuthenticationSuccessHandler acvoteHandler;
	
	
	/**
	 * During development, we will use a group of user ids to test the various roles.
	 * Eventually, this will be replaced by using Austin College single sign on for
	 * authentication.   NOTE that "special users" in our data base will add additional
	 * roles as the admin user sees fit.
	 * 
	 * @param passwordEncoder
	 * @return
	 */
    @Bean
    public InMemoryUserDetailsManager userDetailsService(PasswordEncoder passwordEncoder) {
    	
    	/*
    	 * This user does not appear as special user.  So if authenticated, they
    	 * will be granted VOTER role only (the default role by the 
    	 * CustomAuthenticationSuccessHandler.
    	 * 
    	 */
        UserDetails user = User.withUsername("voter")
            .password(passwordEncoder.encode("voter"))
            .roles(AppConstants.ROLE_VOTER)
            .build();

        UserDetails admin = User.withUsername("admin")
            .password(passwordEncoder.encode("admin"))
            .roles(AppConstants.ROLE_ADMIN)
            .build();
        
        /*
         * The following users match special users and their ROLES will be augmented 
         * according to the AcUser role in our database.
         */
        UserDetails mhiggs = User.withUsername("mhiggs")
                .password(passwordEncoder.encode("mhiggs"))
                .roles(AppConstants.ROLE_USER)
                .build();
        
        UserDetails ablock = User.withUsername("ablock")
                .password(passwordEncoder.encode("ablock"))
                .roles(AppConstants.ROLE_USER)
                .build();
        
        UserDetails jedge = User.withUsername("jedge")
                .password(passwordEncoder.encode("jedge"))
                .roles(AppConstants.ROLE_USER)
                .build();
        
        UserDetails arosenberg20 = User.withUsername("arosenberg20")
                .password(passwordEncoder.encode("arosenberg20"))
                .roles(AppConstants.ROLE_USER)
                .build();
        
        UserDetails gvengalasetti19 = User.withUsername("gvengalasetti19")
                .password(passwordEncoder.encode("gvengalasetti19"))
                .roles(AppConstants.ROLE_USER)
                .build();
        
        UserDetails bhill20 = User.withUsername("bhill20")
                .password(passwordEncoder.encode("bhill20"))
                .roles(AppConstants.ROLE_USER)
                .build();
        
        UserDetails kleahy20 = User.withUsername("kleahy20")
                .password(passwordEncoder.encode("kleahy20"))
                .roles(AppConstants.ROLE_USER)
                .build();

        return new InMemoryUserDetailsManager(user, admin, 
        		mhiggs, ablock, jedge, arosenberg20, gvengalasetti19, bhill20, kleahy20
        		);
    }

    
    
    
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

    	
    	log.info("filter chain");
    	
    	
        http
        	
        	.httpBasic()
        		.and()
        		
        	.authorizeHttpRequests()
        		.requestMatchers(new AntPathRequestMatcher("/logout-ok")).permitAll()
        		.requestMatchers(new AntPathRequestMatcher("/ballot/**")).hasAnyRole(AppConstants.ROLE_ADMIN, AppConstants.ROLE_EDITOR, AppConstants.ROLE_VIEWER)
        		.requestMatchers(new AntPathRequestMatcher("/ballots/**")).hasAnyRole(AppConstants.ROLE_ADMIN, AppConstants.ROLE_EDITOR, AppConstants.ROLE_VIEWER)
        		.requestMatchers(new AntPathRequestMatcher("/faculty/**")).hasAnyRole(AppConstants.ROLE_ADMIN, AppConstants.ROLE_EDITOR, AppConstants.ROLE_VIEWER)
        		.requestMatchers(new AntPathRequestMatcher("/users/**")).hasAnyRole(AppConstants.ROLE_ADMIN, AppConstants.ROLE_EDITOR, AppConstants.ROLE_VIEWER)
        		.requestMatchers(new AntPathRequestMatcher("/template/**")).hasAnyRole(AppConstants.ROLE_ADMIN, AppConstants.ROLE_EDITOR, AppConstants.ROLE_VIEWER)
        		.requestMatchers(new AntPathRequestMatcher("/templates")).hasAnyRole(AppConstants.ROLE_ADMIN, AppConstants.ROLE_EDITOR, AppConstants.ROLE_VIEWER)
        		.requestMatchers(new AntPathRequestMatcher("/voter/**")).hasAnyRole(AppConstants.ROLE_ADMIN, AppConstants.ROLE_VOTER)
        		.anyRequest().authenticated()
        		.and()
        		
            .formLogin()
            	.loginPage("/login").permitAll()
            	.failureUrl("/login?error")
            	.successHandler(acvoteHandler)
            	.and()
            	
            .logout( (lo) -> { 
            	lo
              		.logoutRequestMatcher(new AntPathRequestMatcher("/logout")).permitAll()
                    .logoutSuccessUrl("/logout-ok").permitAll()
                    
                    .invalidateHttpSession(true)
                    .clearAuthentication(true)
                    .deleteCookies("JSESSIONID");
            		}
            
            		);
            
            
        
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        PasswordEncoder encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
        return encoder;
    }
}