package edu.austincollege.acvote.unit.faculty;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import edu.austincollege.acvote.faculty.Faculty;
import edu.austincollege.acvote.faculty.FacultyIO;
import junit.framework.Assert;

public class FacultyIOTest {

	FacultyIO tIO = new FacultyIO();
	InputStream file;
	/*
	 * Tests to make sure that we can create a FacultyIO file.
	 */
	@Test
	void testCreation() {
		FacultyIO f = new FacultyIO();
		assertNotNull(f);
	}
	
	/*
	 * Makes sure we can read faculty when it is in the same order as the constructor parameters.
	 */
	@Test
	void readFacTest_0() throws FileNotFoundException {
		file = FacultyIOTest.class.getResourceAsStream("factest_0.csv");
		ArrayList<Faculty> testList = tIO.readFacultyFile(file);
		Faculty tPerson = testList.get(9);
		//Need to fill all of this information in from our test file. Should probably do multiple people.
		assertTrue(tPerson.getAcId().equals("1690238"));
		assertTrue(tPerson.getLastName().equals("Meyer"));
		assertTrue(tPerson.getFirstName().equals("Wayne"));
		assertTrue(tPerson.getDept().equals("BIOL"));
		assertTrue(tPerson.getDiv().equals("SC"));
		assertTrue(tPerson.getRank().equals("ASOP"));
		assertTrue(tPerson.getEmail().equals("wmeyer@austincollege.edu"));
		assertTrue(tPerson.getTenure().equals("T")); //Could be assertFalse
		assertTrue(tPerson.isVoting()); //Could be assertFalse
		
		tPerson = testList.get(3);
		assertEquals("0907378", tPerson.getAcId());
		assertTrue(tPerson.getLastName().equals("Nguyen"));
		assertTrue(tPerson.getFirstName().equals("Huy"));
		assertTrue(tPerson.getDept().equals("MACS"));
		assertTrue(tPerson.getDiv().equals("SC"));
		assertTrue(tPerson.getRank().equals("ASIP"));
		assertTrue(tPerson.getEmail().equals("hnguyen@austincollege.edu"));
		assertTrue(tPerson.getTenure().equals("TT"));
		assertTrue(tPerson.isVoting());
		
		tPerson = testList.get(5);
		assertTrue(tPerson.getAcId().equals("3944880"));
		assertTrue(tPerson.getLastName().equals("Hoops"));
		assertTrue(tPerson.getFirstName().equals("Terry"));
		assertTrue(tPerson.getDept().equals("SOCAN"));
		assertTrue(tPerson.getDiv().equals("SS"));
		assertTrue(tPerson.getRank().equals("ASOP"));
		assertTrue(tPerson.getEmail().equals("thoops@austincollege.edu"));
		assertTrue(tPerson.getTenure().equals("T"));
		assertTrue(tPerson.isVoting());
		
		//Checks for a isVoting false
		tPerson = testList.get(6);
		assertTrue(tPerson.getAcId().equals("8684581"));
		assertTrue(tPerson.getLastName().equals("Snell"));
		assertTrue(tPerson.getFirstName().equals("Steve"));
		assertTrue(tPerson.getDept().equals("PSCI"));
		assertTrue(tPerson.getDiv().equals("SS"));
		assertTrue(tPerson.getRank().equals("ADJU"));
		assertTrue(tPerson.getEmail().equals("ssnell@austincollege.edu"));
		assertTrue(tPerson.getTenure().equals(""));
		assertFalse(tPerson.isVoting());
		
		for(int i=0; i<testList.size();i++) {
			assertNotNull(tPerson.getAcId());
		}
		
	}
	
	/*
	 * Checks to see if FacultyIO can read a file that has extra columns not in our Faculty parameters.
	 */

	/*
	 * Checks to see if FacultyIO can read a file that has the columns not in order of the faculty parameters.
	 */
	@Test
	void readFacTest_1() {
		file = FacultyIOTest.class.getResourceAsStream("factest_1.csv");
		ArrayList<Faculty> tCol = tIO.readFacultyFile(file);
		Faculty tPerson = tCol.get(0);
		
		//Need to pull from the file
		assertEquals("3987132", tPerson.getAcId());
		assertTrue(tPerson.getLastName().equals("Rohmer"));
		assertTrue(tPerson.getFirstName().equals("Frank"));
		assertTrue(tPerson.getDept().equals("PSCI"));
		assertTrue(tPerson.getDiv().equals("SS"));
		assertTrue(tPerson.getRank().equals("ASOP"));
		assertTrue(tPerson.getTenure().equals("T"));
		assertTrue(tPerson.isVoting());
		
		tPerson = tCol.get(1);
		assertEquals("0938416", tPerson.getAcId());
		assertTrue(tPerson.getLastName().equals("Dollar"));
		assertTrue(tPerson.getFirstName().equals("Tamra"));
		assertTrue(tPerson.getDept().equals("EDUC"));
		assertTrue(tPerson.getDiv().equals("SS"));
		assertTrue(tPerson.getRank().equals("VSTG"));
		assertTrue(tPerson.getTenure().equals(""));
		assertFalse(tPerson.isVoting());
		
		tPerson = tCol.get(3);
		assertEquals("0907378", tPerson.getAcId());
		assertTrue(tPerson.getLastName().equals("Nguyen"));
		assertTrue(tPerson.getFirstName().equals("Huy"));
		assertTrue(tPerson.getDept().equals("MACS"));
		assertTrue(tPerson.getDiv().equals("SC"));
		assertTrue(tPerson.getRank().equals("ASIP"));
		assertTrue(tPerson.getEmail().equals("hnguyen@austincollege.edu"));
		assertTrue(tPerson.getTenure().equals("TT"));
		assertTrue(tPerson.isVoting());
		
		tPerson = tCol.get(6);
		assertTrue(tPerson.getAcId().equals("8684581"));
		assertTrue(tPerson.getLastName().equals("Snell"));
		assertTrue(tPerson.getFirstName().equals("Steve"));
		assertTrue(tPerson.getDept().equals("PSCI"));
		assertTrue(tPerson.getDiv().equals("SS"));
		assertTrue(tPerson.getRank().equals("ADJU"));
		assertTrue(tPerson.getEmail().equals("ssnell@austincollege.edu"));
		assertTrue(tPerson.getTenure().equals(""));
		assertFalse(tPerson.isVoting());
		
		tPerson = tCol.get(8);
		assertTrue(tPerson.getAcId().equals("4443407"));
		assertTrue(tPerson.getLastName().equals("Countryman"));
		assertTrue(tPerson.getFirstName().equals("Renee"));
		assertTrue(tPerson.getDept().equals("PSY"));
		assertTrue(tPerson.getDiv().equals("SS"));
		assertTrue(tPerson.getRank().equals("PROF"));
		assertTrue(tPerson.getEmail().equals("rcountryman@austincollege.edu"));
		assertTrue(tPerson.getTenure().equals("T"));
		assertTrue(tPerson.isVoting());
		
		tPerson = tCol.get(9);
		assertTrue(tPerson.getAcId().equals("1690238"));
		assertTrue(tPerson.getLastName().equals("Meyer"));
		assertTrue(tPerson.getFirstName().equals("Wayne"));
		assertTrue(tPerson.getDept().equals("BIOL"));
		assertTrue(tPerson.getDiv().equals("SC"));
		assertTrue(tPerson.getRank().equals("ASOP"));
		assertTrue(tPerson.getEmail().equals("wmeyer@austincollege.edu"));
		assertTrue(tPerson.getTenure().equals("T")); 
		assertTrue(tPerson.isVoting());
	}
	
	@Test
	void readFacultyTest_2() {
		file = FacultyIOTest.class.getResourceAsStream("factest_2.csv");
		ArrayList<Faculty> tCol = tIO.readFacultyFile(file);
		Faculty tPerson = tCol.get(0);
		
		// Will it collect all of the same fields 
		assertEquals(tPerson.getAcId(),"3987132");
		assertTrue(tPerson.getLastName().equals("Rohmer"));
		assertTrue(tPerson.getFirstName().equals("Frank"));
		assertTrue(tPerson.getDept().equals("PSCI"));
		assertTrue(tPerson.getDiv().equals("SS"));
		assertTrue(tPerson.getRank().equals("ASOP"));
		assertTrue(tPerson.getTenure().equals("T"));
		assertTrue(tPerson.isVoting());
		
		tPerson = tCol.get(2);
		assertEquals(tPerson.getAcId(), "4336851");
		assertTrue(tPerson.getLastName().equals("Lueckel"));
		assertTrue(tPerson.getFirstName().equals("Wolfgang"));
		assertTrue(tPerson.getDept().equals("CLML"));
		assertTrue(tPerson.getDiv().equals("HU"));
		assertTrue(tPerson.getRank().equals("ASOP"));
		assertTrue(tPerson.getTenure().equals("T"));
		assertTrue(tPerson.isVoting());
		
		tPerson = tCol.get(6);
		assertTrue(tPerson.getAcId().equals("8684581"));
		assertTrue(tPerson.getLastName().equals("Snell"));
		assertTrue(tPerson.getFirstName().equals("Steve"));
		assertTrue(tPerson.getDept().equals("PSCI"));
		assertTrue(tPerson.getDiv().equals("SS"));
		assertTrue(tPerson.getRank().equals("ADJU"));
		assertTrue(tPerson.getEmail().equals("ssnell@austincollege.edu"));
		assertTrue(tPerson.getTenure().equals(""));
		assertFalse(tPerson.isVoting());
		
		tPerson = tCol.get(7);
		assertEquals("0598285", tPerson.getAcId());
		assertTrue(tPerson.getLastName().equals("Johnson-Cooper"));
		assertTrue(tPerson.getFirstName().equals("Jennifer"));
		assertTrue(tPerson.getDept().equals("CLML"));
		assertTrue(tPerson.getDiv().equals("HU"));
		assertTrue(tPerson.getRank().equals("ASOP"));
		assertTrue(tPerson.getEmail().equals("jtjohnson@austincollege.edu"));
		assertTrue(tPerson.getTenure().equals("T"));
		assertTrue(tPerson.isVoting());
		
		tPerson = tCol.get(9);
		assertTrue(tPerson.getAcId().equals("1690238"));
		assertTrue(tPerson.getLastName().equals("Meyer"));
		assertTrue(tPerson.getFirstName().equals("Wayne"));
		assertTrue(tPerson.getDept().equals("BIOL"));
		assertTrue(tPerson.getDiv().equals("SC"));
		assertTrue(tPerson.getRank().equals("ASOP"));
		assertTrue(tPerson.getEmail().equals("wmeyer@austincollege.edu"));
		assertTrue(tPerson.getTenure().equals("T")); 
		assertTrue(tPerson.isVoting());
	}
	
	@Test
	void readFaculty() {
		file = FacultyIOTest.class.getResourceAsStream("faculty.csv");
		ArrayList<Faculty> tCol = tIO.readFacultyFile(file);
		Faculty tPerson = tCol.get(0);
		
		assertTrue(tPerson.getAcId().equals("1608358"));
		assertTrue(tPerson.getLastName().equals("Aldridge"));
		assertTrue(tPerson.getFirstName().equals("Joyce"));
		assertTrue(tPerson.getDept().equals("CMT"));
		assertTrue(tPerson.getDiv().equals("HU"));
		assertTrue(tPerson.getRank().equals("SUST"));
		assertTrue(tPerson.getEmail().equals("jaldridge@austincollege.edu"));
		assertTrue(tPerson.getTenure().equals(""));
		assertFalse(tPerson.isVoting());
		
		int count = 0;
		while(count < 136) {
			tPerson = tCol.get(count);
			if((tPerson.getLastName().equals("")) || (tPerson.getFirstName().equals(""))) {
				fail("Empty Person was read");
			}
			count++;
		}
		
		tPerson = tCol.get(1);
		/*
		 * Tests to see if it skips over gaps(the professors that do not have last names)
		 */
		assertTrue(tPerson.getAcId().equals("6249582"));
		assertTrue(tPerson.getLastName().equals("Alvarez"));
		assertTrue(tPerson.getFirstName().equals("Adriana"));
		assertTrue(tPerson.getDept().equals("CLML"));
		assertTrue(tPerson.getDiv().equals("HU"));
		assertTrue(tPerson.getRank().equals("ADJU"));
		assertTrue(tPerson.getEmail().equals("aalvarez@austincollege.edu"));
		assertTrue(tPerson.getTenure().equals(""));
		assertFalse(tPerson.isVoting());
		
		tPerson = tCol.get(64);
		assertTrue(tPerson.getAcId().equals("3372158"));
		assertTrue(tPerson.getLastName().equals("Dryburgh"));
		assertTrue(tPerson.getFirstName().equals("Martinella"));
		assertTrue(tPerson.getDept().equals("LEAD"));
		assertTrue(tPerson.getDiv().equals("SS"));
		assertTrue(tPerson.getRank().equals("ASOP"));
		assertTrue(tPerson.getEmail().equals("mdryburgh@austincollege.edu"));
		assertTrue(tPerson.getTenure().equals(""));
		assertTrue(tPerson.isVoting());
		
		tPerson = tCol.get(65);
		assertTrue(tPerson.getAcId().equals("9416002"));
		assertTrue(tPerson.getLastName().equals("Duffey"));
		assertTrue(tPerson.getFirstName().equals("Patrick"));
		assertTrue(tPerson.getDept().equals("CLML"));
		assertTrue(tPerson.getDiv().equals("HU"));
		assertTrue(tPerson.getRank().equals("PROF"));
		assertTrue(tPerson.getEmail().equals("pduffey@austincollege.edu"));
		assertTrue(tPerson.getTenure().equals("T"));
		assertTrue(tPerson.isVoting());
		
		tPerson = tCol.get(115);
		assertEquals("0689248", tPerson.getAcId());
		assertTrue(tPerson.getLastName().equals("Norman"));
		assertTrue(tPerson.getFirstName().equals("David"));
		assertTrue(tPerson.getDept().equals("KINES"));
		assertTrue(tPerson.getDiv().equals("SS"));
		assertTrue(tPerson.getRank().equals("ADST"));
		assertTrue(tPerson.getEmail().equals("dnorman@austincollege.edu"));
		assertTrue(tPerson.getTenure().equals(""));
		assertTrue(tPerson.isVoting());
		
		tPerson = tCol.get(116);
		assertEquals(tPerson.getAcId(), "5471907");
		assertTrue(tPerson.getLastName().equals("Nuckols"));
		assertTrue(tPerson.getFirstName().equals("Dan"));
		assertTrue(tPerson.getDept().equals("ECBA"));
		assertTrue(tPerson.getDiv().equals("SS"));
		assertTrue(tPerson.getRank().equals("ASOP"));
		assertTrue(tPerson.getEmail().equals("dnuckols@austincollege.edu"));
		assertTrue(tPerson.getTenure().equals("T"));
		assertTrue(tPerson.isVoting());
		
		
		tPerson = tCol.get(134);
		assertTrue(tPerson.getAcId().equals("9708526"));
		assertTrue(tPerson.getLastName().equals("Storey"));
		assertTrue(tPerson.getFirstName().equals("Lisha"));
		assertTrue(tPerson.getDept().equals("ENG"));
		assertTrue(tPerson.getDiv().equals("HU"));
		assertTrue(tPerson.getRank().equals("ASIP"));
		assertTrue(tPerson.getEmail().equals("lstorey@austincollege.edu"));
		assertTrue(tPerson.getTenure().equals("TT"));
		assertTrue(tPerson.isVoting());
		
		tPerson = tCol.get(135);
		assertTrue(tPerson.getAcId().equals("7079870"));
		assertTrue(tPerson.getLastName().equals("Tharayil"));
		assertTrue(tPerson.getFirstName().equals("Ashley"));
		assertTrue(tPerson.getDept().equals("ECBA"));
		assertTrue(tPerson.getDiv().equals("SS"));
		assertTrue(tPerson.getRank().equals("ASOP"));
		assertTrue(tPerson.getEmail().equals("aatharayil@austincollege.edu"));
		assertTrue(tPerson.getTenure().equals("T"));
		assertTrue(tPerson.isVoting());
	}
}
