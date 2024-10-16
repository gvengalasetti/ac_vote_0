package edu.austincollege.acvote.integration.faculty;

import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.xpath;

import org.junit.Ignore;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import edu.austincollege.acvote.faculty.DuplicateFacultyException;
import jakarta.servlet.ServletException;

import static edu.austincollege.acvote.unit.StringUtil.dquote;

@RunWith(SpringRunner.class)
@SpringBootTest // needed to load the application context (ie, the entire stack of important
				// objects)
@AutoConfigureMockMvc
@WithMockUser(username = "admin", roles = { "ADMIN" })
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class FacultyTests {

	@Autowired
	private MockMvc mvc;

	/*
	 * View faculty tests
	 */

	@Test
	void testViewFaculty() throws Exception {
		mvc.perform(get("/faculty/list").with(csrf()).contentType(MediaType.TEXT_HTML))

				.andExpect(status().isOk())

				.andDo(MockMvcResultHandlers.print())

				// Should see html contents of get request like a h1 with text "Faculty List"
				.andExpect(xpath("//h1").string("Faculty List"))

				// or table with IDed rows and text
				.andExpect(xpath(dquote("//table")).exists()).andExpect(xpath(dquote("//tr[@acId='0298799']")).exists())
				.andExpect(xpath(dquote("//tr[@acId='0468076']")).exists())
				.andExpect(xpath(dquote("//tr[@acId='1299808']")).exists())

				.andExpect(
						xpath(dquote("//tr[@acId='0298799']/td[2]/text()")).string(containsString("Cornelison-Brown")))

				.andExpect(xpath(dquote("//tr[@acId='0298799']/td[3]/text()")).string(containsString("Shannon")));

	}

	/*
	 * View details tests
	 */

	@Test
	void testViewFacultyDetails() throws Exception {
		mvc.perform(get("/faculty/details?acId=0298799").with(csrf()).contentType(MediaType.TEXT_HTML))
				.andExpect(status().isOk())

				.andExpect(xpath(dquote("//div[@acId='0298799']")).exists())
				.andExpect(xpath(dquote("//p[@id='acIdBox']/text()")).string(containsString("0298799")))
				.andExpect(xpath(dquote("//select[@id='activeStatusBox']")).exists());
	}

	@Ignore
	void testViewFacultyDetailsFailure() throws Exception {
		mvc.perform(get("/faculty/details?acId=-1").contentType(MediaType.TEXT_HTML)).andExpect(status().isOk());

		// TODO add more to this test
	}

	/*
	 * Delete tests
	 */

	@Test
	void testDeleteFacultySuccess() throws Exception {
		mvc.perform(delete("/faculty/ajax/delete?acId=0298799").with(csrf()).contentType(MediaType.TEXT_HTML))
				.andExpect(status().isOk())

				.andExpect(content().string(containsString("deleted faculty #0298799")));
	}

	@Test
	void testDeleteFacultyFailure() throws Exception {
		mvc.perform(delete("/faculty/ajax/delete?acId=-1").with(csrf()).contentType(MediaType.TEXT_HTML)).andExpect(status().isOk())

				.andExpect(content().string("failed to delete faculty #-1"));
	}

	/*
	 * Edit tests
	 */

	@Test
	void testEditFacultySuccess() throws Exception {
		mvc.perform(put(
				"/faculty/ajax/editfaculty?acId=0298799&lastName=Cornelison-Brown&firstName=Shannon&dept=ECBA&div=SS&rank=ASIP&email=sbrown%40austincollege.edu&tenure=TT&voting=true&active=true")
				.with(csrf()).contentType(MediaType.TEXT_HTML)).andExpect(status().isOk())

				.andExpect(content().string(containsString("successfully edited faculty #0298799")));
	}

	@Test
	void testEditFacultyFailure() throws Exception {
		mvc.perform(put(
				"/faculty/ajax/editfaculty?acId=-1&lastName=Cornelison-Brown&firstName=Shannon&dept=ECBA&div=SS&rank=ASIP&email=sbrown%40austincollege.edu&tenure=TT&voting=true&active=true")
				.with(csrf()).contentType(MediaType.TEXT_HTML)).andExpect(status().isOk())

				.andExpect(content().string(containsString("failed to edit faculty #-1")));
	}

	/*
	 * Create tests
	 */

	@Test
	void testCreateFacultySuccess() throws Exception {
		mvc.perform(post("/faculty/ajax/create?acId=111112").with(csrf()).contentType(MediaType.TEXT_HTML))
				.andExpect(status().isOk())

				.andExpect(content().string(containsString("111112")));
	}

	@Test
	void testCreateFaultyDuplicateID() throws Exception {
		
		 assertThrows(ServletException.class, () ->{
		mvc.perform(post("/faculty/ajax/create?acId=0298799").with(csrf()).contentType(MediaType.TEXT_HTML))
				.andExpect(status().isOk());

				
		  });
	}

	@Test
	void testCreateFacultyInvalidID() throws Exception {
		mvc.perform(
				post("/faculty/ajax/create?acId=-1").with(csrf()).contentType(MediaType.TEXT_HTML))
				.andExpect(status().isOk())

				.andExpect(content().string(containsString("-1")));
	}

}
