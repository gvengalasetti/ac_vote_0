package edu.austincollege.acvote.unit.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.Collections;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import edu.austincollege.acvote.utils.UserUtil;

import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.Authentication;


@RunWith(SpringRunner.class)
@WebMvcTest(UserUtil.class)
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
public class UserUtilTest {
	
	String adminUsername;
	String adminRole;
	
	String voterUsername;
	String voterRole;
	
	@BeforeEach
	void setUp() {
		
		adminUsername = "admin";
		adminRole = "ROLE_ADMIN";
		
		voterUsername = "voter";
		voterRole = "ROLE_VOTER";
	}
	
	@Test
	void creationTest() {
		UserUtil util = new UserUtil();
		assertNotNull(util);
	}
	
	
	@Test
	@WithMockUser(username = "admin", roles = { "ADMIN" })
	void userIdAdminTest()
	{
		assertEquals(adminUsername, UserUtil.userId());
	}
	
	@Test
	@WithMockUser(username = "admin", roles = { "ADMIN" })
	void userRoleAdminTest()
	{
		assertEquals(adminRole, UserUtil.userRole());
	}
	
	@Test
	@WithMockUser(username = "voter", roles = { "VOTER" })
	void userIdTest()
	{
		System.err.println(UserUtil.userId());
		assertEquals(voterUsername, UserUtil.userId());
	}
	
	@Test
	@WithMockUser(username = "voter", roles = { "VOTER" })
	void userRoleTest()
	{
		System.err.println(UserUtil.userRole());
		assertEquals(voterRole, UserUtil.userRole());
	}
	
	@Test
    void userIdUnknownTest() {
        // Set up a mock SecurityContext with a null principal
        SecurityContext securityContextMock = Mockito.mock(SecurityContext.class);
        Authentication authenticationMock = Mockito.mock(Authentication.class);
        Mockito.when(authenticationMock.getPrincipal()).thenReturn("unknown");
        Mockito.when(securityContextMock.getAuthentication()).thenReturn(authenticationMock);

        //set the mock SecurityContext to the SecurityContextHolder
        SecurityContextHolder.setContext(securityContextMock);

        //call the method under test
        String result = UserUtil.userId();

        //assert the result (should be "unknown" as defined in the else block)
        assertEquals("unknown", result);
    }
	
	@Test
    void userRoleVoterEmptyListTest() {
        //set up a mock UserDetails object with authorities as null or empty collection
        UserDetails userDetailsMock = new User("testUser", "password", Collections.emptyList()); // or Collections.emptyList()

        //set up a mock SecurityContext and mock Authentication
        SecurityContext securityContextMock = Mockito.mock(SecurityContext.class);
        Authentication authenticationMock = Mockito.mock(Authentication.class);
        Mockito.when(authenticationMock.getPrincipal()).thenReturn(userDetailsMock);
        Mockito.when(securityContextMock.getAuthentication()).thenReturn(authenticationMock);

        //set the mock SecurityContext to the SecurityContextHolder
        SecurityContextHolder.setContext(securityContextMock);

        String result = UserUtil.userRole();
        assertEquals("VOTER", result);
    }
	
	@Test
    void userRoleWithNonUserDetailsTest() {
        //set up a mock SecurityContext and mock Authentication with a non-UserDetails principal
        SecurityContext securityContextMock = Mockito.mock(SecurityContext.class);
        Authentication authenticationMock = Mockito.mock(Authentication.class);
        Mockito.when(authenticationMock.getPrincipal()).thenReturn("customUser");
        Mockito.when(securityContextMock.getAuthentication()).thenReturn(authenticationMock);

        //set the mock SecurityContext to the SecurityContextHolder
        SecurityContextHolder.setContext(securityContextMock);

        
        String result = UserUtil.userRole();
        assertEquals("VOTER", result);
    }

}
