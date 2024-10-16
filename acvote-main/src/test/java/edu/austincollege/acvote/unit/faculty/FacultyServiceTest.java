package edu.austincollege.acvote.unit.faculty;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.RETURNS_DEEP_STUBS;
import static org.mockito.Mockito.when;

import java.io.InputStream;
import java.util.List;
import java.util.ArrayList;

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

import edu.austincollege.acvote.faculty.Faculty;
import edu.austincollege.acvote.faculty.FacultyIO;
import edu.austincollege.acvote.faculty.FacultyService;
import edu.austincollege.acvote.faculty.dao.FacultyDAO;

@RunWith(MockitoJUnitRunner.class)
class FacultyServiceTest {
	
	@Rule
    public MockitoRule initRule = MockitoJUnit.rule();

	@Mock
	private FacultyIO mockFacultyIO;
	
	@Mock
	private FacultyDAO mockDao;
	
	@Mock
	private Faculty mockFaculty;
	
	private List<Faculty> facList;
	
	@InjectMocks
	private FacultyService bs = new FacultyService();
	
	private Faculty f1 = new Faculty("624682", "Vankirk", "Jaidyn", "CS", "SC", "STU", "jvankirk20@austincollege.edu", "", false, true);
	private Faculty f2 = new Faculty("613412", "Pineda", "Enrique", "CS", "SC", "STU", "epineda@austincollege.edu", "", false, true);
	private Faculty f3 = new Faculty("01101101", "Higgs", "Michael", "", "", "","","", true, true);
	private InputStream is; 
	
	FacultyService fsTest;
	
	@BeforeEach
	public void setUp() throws Exception {
		MockitoAnnotations.openMocks(this);
	}
	
	@Test
	void testCreation() {
		fsTest = new FacultyService();
		assertNotNull(fsTest);
	}
	

	
	//Test import and make sure it puts the list in the database?
	
	@Test
	public void test_findFaculty_whenIdIsValid() throws Exception{
		
		mockDao = (FacultyDAO) Mockito.mock(FacultyDAO.class);
		
		Mockito.when(mockDao.findFacultyByID("624682")).thenReturn(f1);
		Mockito.when(mockDao.findFacultyByID("613412")).thenReturn(f2);
		Mockito.when(mockDao.findFacultyByID("01101101")).thenReturn(f3);
		
		bs.setDao(mockDao);
		
		Faculty fac = bs.findFaculty("624682");
		assertNotNull(fac);
		assertEquals(fac, f1);
		
		Faculty fac1 = bs.findFaculty("613412");
		assertNotNull(fac1);
		assertEquals(fac1, f2);
		
		Faculty fac2 = bs.findFaculty("01101101");
		assertNotNull(fac2);
		assertEquals(fac2, f3);
	}
	
	@Test
	public void test_findFaculty_whenIdIsValide() throws Exception{
		mockDao = (FacultyDAO) Mockito.mock(FacultyDAO.class);
		
		Mockito.when(mockDao.findFacultyByID("0")).thenThrow(new Exception("Ouch"));
		Mockito.when(mockDao.findFacultyByID("0000000")).thenThrow(new Exception("Ouch"));
		Mockito.when(mockDao.findFacultyByID("0000010000000")).thenThrow(new Exception("Ouch"));
		
		bs.setDao(mockDao);
		
		Faculty f = bs.findFaculty("0");
		assertNull(f);
		
		f = bs.findFaculty("0000000");
		assertNull(f);
		
		f = bs.findFaculty("0000010000000");
		assertNull(f);
	}
	
	//Help
	//Need to change from regular list to normal list?
	@Test
	public void test_listAll() throws Exception{
		mockDao = (FacultyDAO) Mockito.mock(FacultyDAO.class);
		bs.setDao(mockDao);
		facList = new ArrayList<Faculty>();
		facList.add(f1);
		facList.add(f2);
		facList.add(f3);
		
		Mockito.when(mockDao.listAll()).thenReturn(facList);
		
		List<Faculty> testList = bs.listAll();
		assertNotNull(testList);
		//assertEquals(testList, facList);
		
		assertEquals(testList.get(0), f1);
		assertEquals(testList.get(1), f2);
		assertEquals(testList.get(2), f3);
	}
	//Need to test all methods in FacultyService
	
	@Test 
	public void test_deleteFaculty() throws Exception {
		mockDao = (FacultyDAO) Mockito.mock(FacultyDAO.class);

		bs.setDao(mockDao);
		
		Mockito.doThrow(Exception.class).when(mockDao).delete("00000");
	
		
		assertThrows(Exception.class, () -> {
			mockDao.delete("00000");
		});
		
		assertFalse(bs.deleteFaculty("00000"));
		assertTrue(bs.deleteFaculty("624682"));
	}
	
	@Test
	public void test_updateFaculty() throws Exception {
		mockDao = (FacultyDAO) Mockito.mock(FacultyDAO.class);
		bs.setDao(mockDao);
		mockDao.create(f2.getAcId(), f1.getLastName(), f1.getFirstName(), f2.getDept(), f2.getDiv(), f2.getRank(), f2.getEmail(), f2.getTenure(), f2.isVoting(), f2.isActive());
		Mockito.doNothing().when(mockDao).update(f2.getAcId(), f2.getLastName(), f2.getFirstName(), f2.getDept(), f2.getDiv(), f2.getRank(), f2.getEmail(), f2.getTenure(), f2.isVoting(), f2.isActive());
	}
	
	@Test
	public void test_addFaculty() throws Exception {
		mockDao = (FacultyDAO) Mockito.mock(FacultyDAO.class);
		bs.setDao(mockDao);
		
		Mockito.doThrow(Exception.class).when(mockDao).findFacultyByID("0");
		assertThrows(Exception.class, () -> {
			mockDao.findFacultyByID("0");
		});
		
		Mockito.doThrow(Exception.class).when(mockDao).create("0", "","","","","","","", false, false);
		assertThrows(Exception.class, () -> {
			mockDao.create("0", "","","","","","","", false, false);
		});
	}
	
	@Test
	public void test_DaoGetSet() {
		mockDao = (FacultyDAO) Mockito.mock(FacultyDAO.class);
		bs.setDao(mockDao);
		assertEquals(bs.getDao(),mockDao);
	}
	
//	@Test
//	public void test_FacultyGetSet() {
//		facList = new ArrayList<Faculty>();
//		facList.add(f1);
//		facList.add(f2);
//		facList.add(f3);
//		
//		bs.setFaculty(facList);;
//		List test = bs.getFaculty();
//		assertEquals(test,facList);
//		assertEquals(f1, test.get(0));
//		assertEquals(f2, test.get(1));
//		assertEquals(f3, test.get(2));
//	}
	

}
