package edu.austincollege.acvote.integration.users;

import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.jupiter.api.Assertions.*;
import static edu.austincollege.acvote.unit.StringUtil.dquote;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.xpath;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser(username = "admin", roles = { "ADMIN" })
@ContextConfiguration
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class UserTests {

	@Autowired
	private MockMvc mvc;

	/**
	 * When viewing users, there should be a few in table
	 * 
	 * @throws Exception
	 */
	@Test
	void testViewUsers() throws Exception {

		mvc.perform(get("/users").with(csrf()).contentType(MediaType.TEXT_HTML))

				// should not be an error
				.andExpect(status().isOk())

				.andDo(MockMvcResultHandlers.print())

				// should see title element
				.andExpect(xpath("//h1").string("User List"))

				// should see table and its contents
				.andExpect(xpath("//table").exists()).andExpect(xpath(dquote("//tr[@uid='arosenberg20']")).exists())
				.andExpect(xpath(dquote("//tr[@uid='gvengalasetti19']")).exists())
				.andExpect(xpath(dquote("//tr[@uid='kleahy20']")).exists())
				.andExpect(xpath(dquote("//tr[@uid='bhill20']")).exists())
				.andExpect(xpath(dquote("//tr[@uid='mhiggs']")).exists())
				.andExpect(xpath(dquote("//tr[@uid='ablock']")).exists())

				// should see info about each user
				.andExpect(
						xpath(dquote("//tr[@uid='arosenberg20']/td[2]/text()")).string(containsString("ADMIN")));
	}

	/**
	 * When creating users under normal circumstances, controller should respond
	 * with "success"
	 * 
	 * @throws Exception
	 */
	@Test
	void testCreateUser() throws Exception {

		mvc.perform(post("/users/ajax/createUser?uid=arose007&role=ROLE_VIEWER").with(csrf()).contentType(MediaType.TEXT_HTML))

				// should not be an error
				.andExpect(status().isOk())

				// should respond with "success"
				.andExpect(content().string(containsString("success")));
	}

	/**
	 * When creating users with null uid, controller should respond with "null uid"
	 * 
	 * @throws Exception
	 */
	@Test
	void testCreateUserNullUID() throws Exception {

		mvc.perform(post("/users/ajax/createUser?uid=&role=ROLE_VIEWER").with(csrf()).contentType(MediaType.TEXT_HTML))

				// should not be an error
				.andExpect(status().isOk())

				// should respond with "null uid"
				.andExpect(content().string(containsString("null uid")));
	}

	/**
	 * When creating users with duplicate uid, controller should respond with
	 * "duplicate uid"
	 * 
	 * @throws Exception
	 */
	@Test
	void testCreateUserDuplicateUID() throws Exception {

		mvc.perform(post("/users/ajax/createUser?uid=arosenberg20&role=ROLE_VIEWER").with(csrf()).contentType(MediaType.TEXT_HTML))

				// should not be an error
				.andExpect(status().isOk())

				// should respond with "duplicate uid"
				.andExpect(content().string(containsString("duplicate uid")));
	}

	/**
	 * When deleting users under normal circumstances, controller should respond
	 * with "user arosenberg20 has been deleted"
	 * 
	 * @throws Exception
	 */
	@Test
	void testDeleteUser() throws Exception {

		mvc.perform(delete("/users/ajax/deleteUser?uid=arosenberg20").with(csrf()).contentType(MediaType.TEXT_HTML))

				// should not be an error
				.andExpect(status().isOk())

				// should respond with "user arosenberg20 has been deleted"
				.andExpect(content().string(containsString("user arosenberg20 has been deleted")));
	}

	/**
	 * When deleting users with invalid uid, controller should respond with "could
	 * not delete user arose007"
	 * 
	 * @throws Exception
	 */
	@Test
	void testDeleteUserFailure() throws Exception {

		mvc.perform(delete("/users/ajax/deleteUser?uid=arose007").with(csrf()).contentType(MediaType.TEXT_HTML))

				// should not be an error
				.andExpect(status().isOk())

				// should respond with "could not delete user arose007"
				.andExpect(content().string(containsString("could not delete user arose007")));
	}

}
