package edu.austincollege.acvote.integration.template;

import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
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
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static edu.austincollege.acvote.unit.StringUtil.dquote;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser(username="admin",roles={"ADMIN"})
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class TemplateTests {
	
	@Autowired
	private MockMvc mvc;

	/**
	 * Testing edit template with a valid tid, expecting success
	 * @throws Exception
	 */
	@Test
	//this one
	void testEditTemplateSuccess() throws Exception {
		
		mvc.perform(put("/template/ajax/editTemplate")
				.param("tid", "1")
	            .param("tTitle", "TTitle")
	            .param("bTitle", "BTitle")
	            .param("instructions", "instr")
	            .param("description", "desc")
	            .param("voteType", "IRV")
	            .param("outcomes", "2")
	            .param("basis", "true")
	            .with(csrf())
				.contentType(MediaType.TEXT_HTML)).andExpect(status().isOk())
		.andExpect(content().string(containsString("updated template")));
		
	}
	
	/**
	 * Testing edit template with invalid tid, excpetion failure
	 * @throws Exception
	 */
	@Test
	//this one
	void testEditTemplateFailure() throws Exception {
		
		mvc.perform(put("/template/ajax/editTemplate?tid=-1&tTitle=TTitle&bTitle=BTitle&description=desc&instructions=instr&voteType=IRV&outcomes=2&basis=true")
				.with(csrf())
				.contentType(MediaType.TEXT_HTML)).andExpect(status().isOk())
		.andExpect(content().string(containsString("unable to update template")));
	}

}
