package edu.austincollege.acvote.unit.template;

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

import edu.austincollege.acvote.template.dao.TemplateDao;
import edu.austincollege.acvote.template.BallotTemplate;
import edu.austincollege.acvote.template.TemplateService;

@RunWith(MockitoJUnitRunner.class)
class TemplateServiceTest {

	@Rule
	public MockitoRule initRule = MockitoJUnit.rule();
	
	@Mock
	private TemplateDao mockTemplateDao;
	
	@InjectMocks
	private TemplateService ts = new TemplateService();
	
	private BallotTemplate t1; 
	private BallotTemplate t2;
	private BallotTemplate t3;
	private List<BallotTemplate> templates;
	
	@BeforeEach
	void setUp() throws Exception {
		t1 = new BallotTemplate(1, "GOAT Contest", "Coolest CS Professor", "Drag and drop candidates to rank", "Lets find out who is the best", "IRV", 1, true);
		t2 = new BallotTemplate(2, "CS Profs", "Which ones are CS Professors?", "Please drag and drop these candidates around!", "This is a description for ballot 2!", "IRV", 2, true);
		t3 = new BallotTemplate(3, "Template Example", "This is a different example", "Please drag and drop these candidates around!", "This is a description for ballot 3!", "IRV", 4, false);
		
		templates = new ArrayList<>();
		templates.add(t1);
		templates.add(t2);
		templates.add(t3);
		
		MockitoAnnotations.openMocks(this);
	}
	
	/*
	 * Testing listTemplates
	 */
	
	/**
	 * Normal test case
	 * 
	 * @throws Exception
	 */
	@Test
	void listTemplatesTestNormal() throws Exception {
		
		assertNotNull(mockTemplateDao);
		Mockito.when(mockTemplateDao.listAll()).thenReturn(templates);
		
		List<BallotTemplate> list = ts.listTemplates();
		
		assertNotNull(list);
		assertFalse(list.isEmpty());
		assertEquals(list.size(), 3);
		assertEquals(list.get(0), t1);
		assertEquals(list.get(1), t2);
		assertEquals(list.get(2), t3);
	}
	
	/**
	 * Test case in which dao returns empty list
	 * 
	 * @throws Exception
	 */
	@Test
	void listTemplatesTestEmptyList() throws Exception {
		
		assertNotNull(mockTemplateDao);
		Mockito.when(mockTemplateDao.listAll()).thenReturn(new ArrayList<BallotTemplate>());
		
		List<BallotTemplate> list = ts.listTemplates();
		
		assertNotNull(list);
		assertTrue(list.isEmpty());
		assertEquals(list.size(), 0);
		assertNotEquals(list, templates);
	}
	
	/**
	 * Test case in which dao throws exception
	 * 
	 * @throws Exception
	 */
	@Test
	void listTemplatesTestException() throws Exception {
		
		assertNotNull(mockTemplateDao);
		Mockito.when(mockTemplateDao.listAll()).thenThrow(new Exception("darn"));
		
		List<BallotTemplate> list = ts.listTemplates();
		
		assertNull(list);
		assertNotEquals(list, templates);
	}
	
	/*
	 * Testing createTemplate
	 */
	
	/**
	 * Normal test case
	 * 
	 * @throws Exception
	 */
	@Test
	void createTemplateTestNormal() throws Exception {
		
		assertNotNull(mockTemplateDao);
		Mockito.when(mockTemplateDao.create("GOAT Contest", "Coolest CS Professor", "Drag and drop candidates to rank", "Lets find out who is the best", "IRV", 1, true)).thenReturn(t1);
		
		BallotTemplate returned = ts.createTemplate("GOAT Contest", "Coolest CS Professor", "Drag and drop candidates to rank", "Lets find out who is the best", "IRV", 1, true);
		
		assertNotNull(returned);
		assertEquals(returned, t1);
	}
	
	/**
	 * Test case in which dao throws exception
	 * 
	 * @throws Exception
	 */
	@Test
	void createTemplateTestException() throws Exception {
		
		assertNotNull(mockTemplateDao);
		Mockito.when(mockTemplateDao.create("GOAT Contest", "Coolest CS Professor", "Drag and drop candidates to rank", "Lets find out who is the best", "IRV", 1, true)).thenThrow(new Exception("what the fork"));
		
		BallotTemplate returned = ts.createTemplate("GOAT Contest", "Coolest CS Professor", "Drag and drop candidates to rank", "Lets find out who is the best", "IRV", 1, true);
		
		assertNull(returned);
	}
	
	/*
	 * Testing getTemplate
	 */
	
	/**
	 * Normal test case
	 * 
	 * @throws Exception
	 */
	@Test
	void getTemplateTestNormal() throws Exception {
		
		assertNotNull(mockTemplateDao);
		Mockito.when(mockTemplateDao.get(1)).thenReturn(t1);
		
		BallotTemplate returned = ts.getTemplate(1);
		
		assertNotNull(returned);
		assertEquals(returned, t1);
	}
	
	@Test
	void getTemplateTestException() throws Exception {
		
		assertNotNull(mockTemplateDao);
		Mockito.when(mockTemplateDao.get(1)).thenThrow(new Exception("shorts"));
		
		BallotTemplate returned = ts.getTemplate(1);
		
		assertNull(returned);
		assertNotEquals(returned, t1);
	}
	
	/*
	 * Testing editTemplate
	 */
	
	/**
	 * Normal test case
	 * 
	 * @throws Exception
	 */
	@Test
	void editTemplateTestNormal() throws Exception {
		
		assertNotNull(mockTemplateDao);
		Mockito.when(mockTemplateDao.edit(1, "GOAT Contest", "Coolest CS Professor", "Drag and drop candidates to rank", "Lets find out who is the best", "IRV", 1, true)).thenReturn(t1);
		
		BallotTemplate returned = ts.editTemplate(1, "GOAT Contest", "Coolest CS Professor", "Drag and drop candidates to rank", "Lets find out who is the best", "IRV", 1, true);
		
		assertNotNull(returned);
		assertEquals(returned, t1);
	}
	
	/**
	 * Test case in which dao throws exception
	 * 
	 * @throws Exception
	 */
	@Test
	void editTemplateTestException() throws Exception {
		
		assertNotNull(mockTemplateDao);
		Mockito.when(mockTemplateDao.edit(1, "GOAT Contest", "Coolest CS Professor", "Drag and drop candidates to rank", "Lets find out who is the best", "IRV", 1, true)).thenThrow(new Exception("fork this shorts"));
		
		BallotTemplate returned = ts.editTemplate(1, "GOAT Contest", "Coolest CS Professor", "Drag and drop candidates to rank", "Lets find out who is the best", "IRV", 1, true);
		
		assertNull(returned);
		assertNotEquals(returned, t1);
	}
	
	/**
	 * test delete success
	 * @throws Exception
	 */
	@Test
	void deleteTemplateTest() throws Exception {
		assertNotNull(mockTemplateDao);
		Mockito.when(mockTemplateDao.delete(1)).thenReturn(true);
		
		Boolean deleted = ts.deleteTemplate(1);
		
		assertEquals(true, deleted);
	}
	
	/**
	 * test delete fail
	 * @throws Exception
	 */
	@Test
	void deleteTemplateFailTest() throws Exception {
		assertNotNull(mockTemplateDao);
		Mockito.when(mockTemplateDao.delete(1)).thenThrow(new Exception("Delete failed"));
		
		Boolean deleted = ts.deleteTemplate(1);
		
		assertNotEquals(true, deleted);
	}

}
