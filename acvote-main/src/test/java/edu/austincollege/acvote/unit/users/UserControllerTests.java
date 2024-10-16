package edu.austincollege.acvote.unit.users;

import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.xpath;

import java.util.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import edu.austincollege.acvote.users.AcUser;
import edu.austincollege.acvote.users.UserController;
import edu.austincollege.acvote.users.UserService;
import edu.austincollege.acvote.users.NullUIDException;
import edu.austincollege.acvote.users.DuplicateUIDException;

@RunWith(SpringRunner.class)
@WebMvcTest(UserController.class)
@WithMockUser(username = "admin", roles = { "ADMIN" })
class UserControllerTests {

	@Autowired
	private MockMvc mvc;

	@MockBean
	private UserService mockUserService;

	private List<AcUser> users;

	@BeforeEach
	void setUp() throws Exception {

		users = new ArrayList<>();
		users.add(new AcUser("arosenberg20", "ROLE_ADMIN"));
		users.add(new AcUser("gvengalasetti19", "ROLE_ADMIN"));
		users.add(new AcUser("kleahy20", "ROLE_ADMIN"));
		users.add(new AcUser("bhill20", "ROLE_ADMIN"));
		users.add(new AcUser("mhiggs", "ROLE_ADMIN"));
		users.add(new AcUser("ablock", "ROLE_ADMIN"));

		MockitoAnnotations.openMocks(this);
	}

	private String dquote(String str) {
		if (str == null)
			return null;
		return str.replaceAll("[']", "\"");
	}

	@Test
	public void test_replace_single_quotes_function() {
		String str = "tr/td[@id='xyz']";
		// System.err.println(dquote(str));
		assertEquals("tr/td[@id=\"xyz\"]", dquote(str));
	}

	/**
	 * Testing that controller shows allows admin to view users properly
	 * 
	 * @throws Exception
	 */
	@Test
	void testViewUsers() throws Exception {

		Mockito.when(mockUserService.listAllUsers()).thenReturn(users);

		mvc.perform(get("/users").with(csrf()).contentType(MediaType.TEXT_HTML))

				.andExpect(status().isOk())

				.andExpect(xpath(dquote("//table")).exists())
				.andExpect(xpath(dquote("//tr[@uid='arosenberg20']")).exists())
				.andExpect(xpath(dquote("//tr[@uid='gvengalasetti19']")).exists())
				.andExpect(xpath(dquote("//tr[@uid='kleahy20']")).exists())
				.andExpect(xpath(dquote("//tr[@uid='bhill20']")).exists())
				.andExpect(xpath(dquote("//tr[@uid='mhiggs']")).exists())
				.andExpect(xpath(dquote("//tr[@uid='ablock']")).exists());

		Mockito.verify(mockUserService).listAllUsers();
	}

	/**
	 * Testing that controller creates new user under normal circumstances
	 * 
	 * @throws Exception
	 */
	@Test
	void testCreateUser() throws Exception {

		Mockito.doNothing().when(mockUserService).addUser("arose007", "ROLE_VIEWER");

		mvc.perform(post("/users/ajax/createUser?uid=arose007&role=ROLE_VIEWER").with(csrf()).contentType(MediaType.TEXT_HTML))

				.andExpect(status().isOk())

				.andExpect(content().string(containsString("success")));

		Mockito.verify(mockUserService).addUser("arose007", "ROLE_VIEWER");
	}

	/**
	 * Testing that controller reacts properly to null uid on creation request
	 * 
	 * @throws Exception
	 */
	@Test
	void testCreateUserNullUID() throws Exception {

		Mockito.doThrow(new NullUIDException("fork")).when(mockUserService).addUser("", "ROLE_VIEWER");

		mvc.perform(post("/users/ajax/createUser?uid=&role=ROLE_VIEWER").with(csrf()).contentType(MediaType.TEXT_HTML))

				.andExpect(status().isOk())

				.andExpect(content().string(containsString("null uid")));

		Mockito.verify(mockUserService).addUser("", "ROLE_VIEWER");
	}

	/**
	 * Testing that controller reacts properly to duplicate uid on creation request
	 * 
	 * @throws Exception
	 */
	@Test
	void testCreateUserDuplicateUID() throws Exception {

		Mockito.doThrow(new DuplicateUIDException("shorts")).when(mockUserService).addUser("arosenberg20",
				"ROLE_VIEWER");

		mvc.perform(post("/users/ajax/createUser?uid=arosenberg20&role=ROLE_VIEWER").with(csrf()).contentType(MediaType.TEXT_HTML))

				.andExpect(status().isOk())

				.andExpect(content().string(containsString("duplicate uid")));

		Mockito.verify(mockUserService).addUser("arosenberg20", "ROLE_VIEWER");
	}

	/**
	 * Testing that controller deletes user under normal circumstances
	 * 
	 * @throws Exception
	 */
	@Test
	void testDeleteUser() throws Exception {

		Mockito.when(mockUserService.deleteUser("arosenberg20")).thenReturn(true);

		mvc.perform(delete("/users/ajax/deleteUser?uid=arosenberg20").with(csrf()).contentType(MediaType.TEXT_HTML))

				.andExpect(status().isOk())

				.andExpect(content().string(containsString("user arosenberg20 has been deleted")));

		Mockito.verify(mockUserService).deleteUser("arosenberg20");
	}

	/**
	 * Testing that controller reacts properly to failed delete request
	 * 
	 * @throws Exception
	 */
	@Test
	void testDeleteUserFailure() throws Exception {

		Mockito.when(mockUserService.deleteUser("arosenberg20")).thenReturn(false);

		mvc.perform(delete("/users/ajax/deleteUser?uid=arosenberg20").with(csrf()).contentType(MediaType.TEXT_HTML))

				.andExpect(status().isOk())

				.andExpect(content().string(containsString("could not delete user arosenberg20")));

		Mockito.verify(mockUserService).deleteUser("arosenberg20");
	}

}
