package edu.austincollege.acvote.unit.users;

import static org.junit.jupiter.api.Assertions.*;

import java.util.*;

import org.junit.Rule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.MockitoRule;

import edu.austincollege.acvote.faculty.dao.FacultyDAO;
import edu.austincollege.acvote.users.AcUser;
import edu.austincollege.acvote.users.UserService;
import edu.austincollege.acvote.users.dao.UserDao;

@RunWith(MockitoJUnitRunner.class)
class UserServiceTests {

	@Rule
	public MockitoRule initRule = MockitoJUnit.rule();
	
	@Mock
	private UserDao mockUserDao;
	
	@InjectMocks
	private UserService us = new UserService();
	
	List<AcUser> users;
	
	@BeforeEach
	void setUp() throws Exception {
		
		users = new ArrayList<>();
		users.add(new AcUser("arosenberg20", "ADMIN"));
		users.add(new AcUser("gvengalasetti19", "ADMIN"));
		users.add(new AcUser("kleahy20", "ADMIN"));
		users.add(new AcUser("bhill20", "ADMIN"));
		users.add(new AcUser("mhiggs", "ADMIN"));
		users.add(new AcUser("ablock", "ADMIN"));
		
		MockitoAnnotations.openMocks(this);
	}
	
	/*
	 * Testing deleteUser
	 */
	
	/**
	 * Normal test case
	 * 
	 * @throws Exception
	 */
	@Test
	void deleteUserTestNormal() throws Exception {
		
		assertNotNull(mockUserDao);
		Mockito.doNothing().when(mockUserDao).delete("ablock");
		
		assertTrue(us.deleteUser("ablock"));
	}
	
	/**
	 * Test case in which dao fails to delete and throws exception
	 * 
	 * @throws Exception
	 */
	@Test
	void deleteUserTestFail() throws Exception {
		
		assertNotNull(mockUserDao);
		Mockito.doThrow(new Exception("what the fork")).when(mockUserDao).delete("ablock");
		
		assertFalse(us.deleteUser("ablock"));
	}
	
	/*
	 * Testing listAllUsers
	 */
	
	/**
	 * Normal test case
	 * 
	 * @throws Exception
	 */
	@Test
	void listAllUsersTestNormal() throws Exception {
		
		assertNotNull(mockUserDao);
		Mockito.when(mockUserDao.listAll()).thenReturn(users);
		
		List<AcUser> list = us.listAllUsers();
		
		assertNotNull(list);
		assertFalse(list.isEmpty());
		assertEquals(list, users);
	}
	
	/**
	 * Test case in which dao fails to list users and throws exception
	 * 
	 * @throws Exception
	 */
	@Test
	void listAllUsersTestFail() throws Exception {
		
		assertNotNull(mockUserDao);
		Mockito.when(mockUserDao.listAll()).thenThrow(new Exception("darn"));
		
		List<AcUser> list = us.listAllUsers();
		
		assertNotNull(list);
		assertTrue(list.isEmpty());
		assertNotEquals(list, users);
	}
	/*
	 * Test addUser 
	 * @throws Excption
	 */
	@Test
	void testAddUser()  throws Exception {

		mockUserDao = (UserDao) Mockito.mock(UserDao.class);
		us.setUserDao(mockUserDao);

		
		Mockito.doThrow(Exception.class).when(mockUserDao).create("ablock", "admin");
		assertThrows(Exception.class, () -> {
			mockUserDao.create("ablock", "admin");
		});
		
		
		
	}
}
