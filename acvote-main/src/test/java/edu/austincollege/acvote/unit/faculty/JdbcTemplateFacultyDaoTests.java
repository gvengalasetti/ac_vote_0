package edu.austincollege.acvote.unit.faculty;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

import java.util.List;
import java.util.ArrayList;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

import edu.austincollege.acvote.faculty.Faculty;
import edu.austincollege.acvote.faculty.dao.FacultyDAO;
import edu.austincollege.acvote.faculty.dao.JdbcTemplateFacultyDao;

@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@RunWith(SpringRunner.class)
@JdbcTest
@ComponentScan("acvote.config")
class JdbcTemplateFacultyDaoTests {
	
	@Autowired
	JdbcTemplate jdbcTemplate;
	
	FacultyDAO dao;

	@BeforeAll
	static void setUpBeforeClass() throws Exception {
	}

	@AfterAll
	static void tearDownAfterClass() throws Exception {
	}

	@BeforeEach
	void setUp() throws Exception {
		JdbcTemplateFacultyDao jdao = new JdbcTemplateFacultyDao();
		jdao.setJdbcTemplate(jdbcTemplate);
		dao = jdao;
	}

	@AfterEach
	void tearDown() throws Exception {
	}
	
	@Test
	public void testListAll() throws Exception {
		List<Faculty> facs = dao.listAll();
		
		assertEquals(35,facs.size());
		
		
	}

	@Test
	public void testCreate() throws Exception {
		
		List<Faculty> facs = dao.listAll();
		assertEquals(35,facs.size());
		System.out.println(facs);
		
		Faculty nf = dao.create("test","test","test","test","test","test","test","test",true,false);
		
		facs = dao.listAll();
		assertEquals(36,facs.size());
		
		assertTrue(facs.contains(nf));  // test faculty should be there now!
	}
	
	@Test
	public void testDelete() throws Exception {
		
		List<Faculty> facs = dao.listAll();
		assertEquals(35,facs.size());
		System.out.println(facs);
		
		dao.delete("4138778");
		
		facs = dao.listAll();
		assertEquals(34,facs.size());
		
		System.out.println(facs);
		
	}
	
	@Test
	public void testUpdate() throws Exception {
		
		dao.update("5854836", "Richardson_", "Cathy_", "MUSIC", "HUMAN", "SUST", "crichardson@ac.edu", "T", true, true);

		Faculty f = dao.findFacultyByID("5854836");
		
		assertNotNull(f);
		assertEquals("5854836",f.getAcId());
		assertEquals("Richardson_",f.getLastName());
		assertEquals("Cathy_",f.getFirstName());
		assertEquals("MUSIC",f.getDept());
		assertEquals("HUMAN",f.getDiv());
		assertEquals("SUST",f.getRank());
		assertEquals("crichardson@ac.edu",f.getEmail());
		assertEquals("T",f.getTenure());
		
		assertTrue(f.isVoting());
		assertTrue(f.isActive());
		
	}
	
	@Test
	public void testFindById() throws Exception {
		
		Faculty f = dao.findFacultyByID("5854836");
		assertNotNull(f);
		assertEquals("5854836",f.getAcId());

		f = dao.findFacultyByID("5488165");
		assertNotNull(f);
		assertEquals("5488165",f.getAcId());

		f = dao.findFacultyByID("8299895");
		assertNotNull(f);
		assertEquals("8299895",f.getAcId());

	}
	
	/**
	 * Ensures that we can clear all faculty from our 
	 * data store.
	 * 
	 * @throws Exception
	 */
	@Test 
	public void testDeleteAll() throws Exception {
		List<Faculty> fac = dao.listAll();
		assumeTrue( fac.size()>0 );
		
		dao.deleteAll();
		
		fac = dao.listAll();
		assertTrue( fac.size() == 0 );
		
		
	}
	
	@Test
	public void testInsertAll_whenEachIsUnique() throws Exception {
		List<Faculty> fac = dao.listAll();
		assumeTrue( fac.size()>0 );
		
		int oldsize = fac.size();
		
		Faculty nf1 = new Faculty("test1","test","test","test","test","test","test","test",true,false);
		Faculty nf2 = new Faculty("test2","test","test","test","test","test","test","test",true,false);
		Faculty nf3 = new Faculty("test3","test","test","test","test","test","test","test",true,false);
		
		List<Faculty> newlst = new ArrayList<>();
		newlst.add(nf1); newlst.add(nf2); newlst.add(nf3);
		
		dao.insertAll(newlst);
		
		List<Faculty> nfac = dao.listAll();
		assertEquals(fac.size()+3,nfac.size());
		assertTrue(nfac.contains(nf1));
		assertTrue(nfac.contains(nf2));
		assertTrue(nfac.contains(nf3));
		
		
	}

}
