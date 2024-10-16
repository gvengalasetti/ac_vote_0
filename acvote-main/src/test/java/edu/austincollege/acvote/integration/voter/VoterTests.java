package edu.austincollege.acvote.integration.voter;

import static edu.austincollege.acvote.unit.StringUtil.dquote;
import static org.hamcrest.CoreMatchers.containsString;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
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
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

@RunWith(SpringRunner.class)
@SpringBootTest							// needed to load the application context (ie, the entire stack of important objects) 
@AutoConfigureMockMvc					
@WithMockUser(username="voter",roles={"VOTER"})
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class VoterTests {

	@Autowired
	private MockMvc mvc;

	/**
	 * Testing that voter page presents and has the proper elements
	 * @throws Exception
	 */
	@WithUserDetails("voter")
	@Test
	void testVoterHome() throws Exception {

		//getting IRV ballot vote page
		mvc.perform(get("/voter/vote?bid=3&token=0d354582ca957a881ac85b7dfdbe8163").with(csrf()).contentType(MediaType.TEXT_HTML))

				.andExpect(status().isOk())
				
				.andDo(MockMvcResultHandlers.print())

				//should include heading and list of rankable items
				.andExpect(xpath("//h1").string("Favorite Foods"))
				.andExpect(xpath("//ol").exists())
				.andExpect(xpath(dquote("//li[@oid='ip']")).exists())
				.andExpect(xpath(dquote("//li[@oid='js']")).exists())
				.andExpect(xpath(dquote("//li[@oid='mt']")).exists())
				.andExpect(xpath(dquote("//li[@oid='th']")).exists());
	}
	
	/**
	 * Testing that voter IRV2 page presents and has the proper elements
	 * @throws Exception
	 */
	@WithUserDetails("voter")
	@Test
	void testVoterHomeIRV2() throws Exception {

		//getting IRV2 ballot vote page
		mvc.perform(get("/voter/vote?bid=4&token=0cfd26b73161d6d2d1b76832a2a72a32").with(csrf()).contentType(MediaType.TEXT_HTML))

				.andExpect(status().isOk())
				
				.andDo(MockMvcResultHandlers.print())
				
				//should include filter modal with options table
				.andExpect(xpath(dquote("//div[@id='filterModal']")).exists())
				.andExpect(xpath(dquote("//table[@id='optionTable']")).exists())

				//should include heading and list of rankable items
				.andExpect(xpath("//h1").string("More Favorite Foods"))
				.andExpect(xpath("//ol").exists())
				.andExpect(xpath(dquote("//li[@oid='ip']")).exists())
				.andExpect(xpath(dquote("//li[@oid='js']")).exists())
				.andExpect(xpath(dquote("//li[@oid='mt']")).exists())
				.andExpect(xpath(dquote("//li[@oid='th']")).exists())
				.andExpect(xpath(dquote("//li[@oid='va']")).exists())
				.andExpect(xpath(dquote("//li[@oid='sh']")).exists());
	}

	/**
	 * Testing end-point for casting vote success
	 * @throws Exception
	 */
	@Test
	// TODO fix
	void testCastVoteSuccess() throws Exception {

		mvc.perform(post("/voter/ajax/submitBallot?bid=3&token=0d354582ca957a881ac85b7dfdbe8163&options=ip,js,mt,th").with(csrf())
				.contentType(MediaType.TEXT_HTML))

				.andExpect(status().isOk())

				//should respond with message indicating success
				.andExpect(content().string(containsString("successfully submitted vote")));
	}

	/**
	 * Testing end-point for casting vote failure
	 * @throws Exception
	 */
	@Test
	// TODO fix
	void testCastVoteFailure() throws Exception {

		mvc.perform(post("/voter/ajax/submitBallot?bid=2&token=0d354582ca957a881ac85b7dfdbe8163&options=ip,js,mt,th").with(csrf())
				.contentType(MediaType.TEXT_HTML))

				.andExpect(status().isOk())

				//should respond with message indicating failure
				.andExpect(content().string(containsString("could not submit vote")));
	}

	/**
	 * Testing that voted page presents and has proper elements
	 * @throws Exception
	 */
	@Test
	void testVoted() throws Exception {

		mvc.perform(get("/voter/voted").contentType(MediaType.TEXT_HTML))

				.andExpect(status().isOk())

				.andDo(MockMvcResultHandlers.print())

				//should include header, message, and sticker
				.andExpect(xpath("//h1").string("Thank You for Voting!"))
				.andExpect(xpath("//p").string("Your vote has been successfully castYou may now close this page/tab/browser"))
				.andExpect(xpath(dquote("//img[@alt='I voted']")).exists());
	}
}
