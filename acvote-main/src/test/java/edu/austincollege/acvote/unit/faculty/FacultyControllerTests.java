package edu.austincollege.acvote.unit.faculty;

import static org.hamcrest.CoreMatchers.containsString;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import edu.austincollege.acvote.faculty.Faculty;
import edu.austincollege.acvote.faculty.FacultyController;
import edu.austincollege.acvote.faculty.FacultyService;
import edu.austincollege.acvote.lut.LutDao;



@RunWith(SpringRunner.class)
@WebMvcTest(FacultyController.class)
@WithMockUser(value = "admin")
public class FacultyControllerTests {

	
	@Autowired
	private MockMvc mvc;
	
	@MockBean
	private FacultyService mockFacultySrv;
	
	@MockBean
	private LutDao mockLutDao;
	// Example Faculty Ballots

	private Faculty f1 = new Faculty("John", null, null, null, null, null, null, null, false, false);  
	private Faculty f2 = new Faculty("Mary", null, null, null, null, null, null, null, false, false);	 	
	private Faculty f3 = new Faculty("Logan", null, null, null, null, null, null, null, false, false);   //
	
	List<Faculty> Faculty = new ArrayList<Faculty>();
	
	
	/**
	 * Test that 
	 * @throws Exception
	 */
	@Test
	public void ajaxDeleteFaculty_whenFacultyExists() throws Exception {
		
		// train the mock service to fake the delete, but return true...pretending to be successful.
		Mockito.when(mockFacultySrv.deleteFaculty("12345")).thenReturn(true);
		
		// if the service is "successful", then we should see an OK return status with a string 
		// indicating that 'Block" was deleted.
		mvc.perform(delete("/faculty/ajax/delete?acId=12345").with(csrf())
				.contentType(MediaType.TEXT_HTML))
		.andExpect(status().isOk())
		.andExpect(content().string(containsString("deleted faculty #12345")));
		
		// now make sure the mock service was indeed consulted during this test
		Mockito.verify(mockFacultySrv).deleteFaculty("12345");

	}
	
	
	
	/**
	 * Test that 
	 * @throws Exception
	 */
	@Test
	public void ajaxDeleteFaculty_whenFacultyDoesNotExist() throws Exception {
		
		// train the mock service to fake the delete, but return true...pretending to be successful.
		Mockito.when(mockFacultySrv.deleteFaculty("12345")).thenReturn(false);
		
		// if the service is "successful", then we should see an OK return status with a string 
		// indicating that 'Block" was deleted.
		mvc.perform(delete("/faculty/ajax/delete?acId=12345").with(csrf())
				.contentType(MediaType.TEXT_HTML))
		.andExpect(status().isOk())
		.andExpect(content().string(containsString("failed to delete faculty #12345")));
		
		// now make sure the mock service was indeed consulted during this test
		Mockito.verify(mockFacultySrv).deleteFaculty("12345");

	}
}
