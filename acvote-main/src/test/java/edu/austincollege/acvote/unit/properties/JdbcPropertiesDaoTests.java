package edu.austincollege.acvote.unit.properties;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

import edu.austincollege.acvote.properties.AcProperties;
import edu.austincollege.acvote.properties.dao.PropertiesDao;
import edu.austincollege.acvote.properties.dao.JdbcPropertiesDao;



@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@RunWith(SpringRunner.class)
@JdbcTest
@ComponentScan("acvote.config")
class JdbcPropertiesDaoTests {

	@Autowired
	JdbcTemplate jdbcTemplate;
	PropertiesDao dao;

	List<AcProperties> fac;
	AcProperties fac1;
	AcProperties fac2;
	AcProperties fac3;

	@BeforeEach
	void setUp() throws Exception {
		JdbcPropertiesDao jdao = new JdbcPropertiesDao();
		jdao.setJdbcTemplate(jdbcTemplate);
		dao = jdao;

		fac1 = new AcProperties("faculty.last.import", "2023-06-08T13:23:05");
		fac2 = new AcProperties("faculty.import.june", "2023-06-08T13:23:05");
		fac3 = new AcProperties("faculty.last.may", "2023-06-08T13:23:05");

		fac = new ArrayList<>();
		fac.add(fac1);
		fac.add(fac2);
		fac.add(fac3);
	}



	@Test
	void testListAll() throws Exception{
		List<AcProperties> results = dao.listProperties();
		assertNotNull(results);
		assertEquals(3, results.size());
		for(int i =0; i<results.size(); i++) {
			//System.err.println(results.get(i));
			assertTrue(fac.contains(results.get(i)));
		}

	}

	@Test
	void  testreturnProperty() throws Exception{
		AcProperties p = dao.returnProperty("faculty.last.import");
		System.out.println(p);

		assertNotNull(p);
		assertEquals(p,fac1);
	}
	@Test
	void  testSetProperty() throws Exception{
		try {

			dao.setProperty("faculty.new.import", "2023-06-08T13:23:05");
		}
		catch (Exception e) {

			fail("Exception not expected");	}}
		@Test
		void  testDelete() throws Exception{
			int prevSize = dao.listProperties().size();
			try{
				dao.delete("faculty.last.import");
				assertNotEquals(prevSize, dao.listProperties().size());
			}
			catch(Exception e) {
				fail("Delete failed.");
			}
		}

	}
