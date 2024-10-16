package edu.austincollege.acvote.functional.faculty;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import edu.austincollege.acvote.functional.SeleniumTest;
import edu.austincollege.acvote.unit.StringUtil;
import org.openqa.selenium.JavascriptExecutor;



@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)

public class FacultyTests extends SeleniumTest {
	
	/**
	 * Grabs the count of records in a dataTable from a string of form
	 * 
	 * Showing 1 to 10 of 35 entries
	 * 
	 * (\\d+)(?= entries)
	 * 
	 * (\\d+) - finds one or more decimal numbers
	 * (?= entries) - looks ahead for ' entries'
	 * 
	 * so in this case, it would grab 35 entries
	 */
	private static final Pattern DATA_TABLE_LIST_ITEM_COUNT_REGEXER = Pattern.compile("(\\d+)(?= entries)");
	
	
	/**
	 * Navigates to the faculty list view on startup
	 * 
	 * @throws Exception
	 */
	protected void adminNavigateToFacultyListView() throws Exception
	{
		//log in and start the application
		this.loginAsAdmin();
		assertEquals(base+"/home?continue", driver.getCurrentUrl());
		
		// find nav button element using xpath 
		WebElement facultyButton = driver.findElement(By.xpath(StringUtil.dquote("//a[@id='facultyLink']")));
		assertNotNull(facultyButton);
		
		// clicking on the ballots button in the nav bar
		facultyButton.click();
		assertEquals(base+"/faculty/list", driver.getCurrentUrl());
		Thread.sleep(500);
	}
	
	/**
	 * Gets the count of the faculty in the datatable from the DataTables_Table_0_info using regex
	 * 
	 * @return count of faculty
	 */
	private int countFacultyInDataTable() {
		// Get the number of faculty in existence
		WebElement facultyListTableInfo = driver.findElement(By.id("DataTables_Table_0_info"));
		assertNotNull(facultyListTableInfo);
		
		// set up the matcher to get the count of faculty in the list from the table info
		Matcher facultyListCountMatcher = DATA_TABLE_LIST_ITEM_COUNT_REGEXER.matcher(facultyListTableInfo.getText());
		
		// if the datatable was initialized properly, and the regex is able to grab the count 
		if (facultyListCountMatcher.find()) {
			return Integer.valueOf(facultyListCountMatcher.group(0));		// get the value of the entire group found
		} else {
			fail("No datatable string found");									// otherwise throw a fit
			return 0;
		}
	}
	
	
	/**
	 * Method to test create function in faculty page.
	 * 
	 * Basic test for creation. Does not fill in values, 
	 * just checks that the buttons work and navigation 
	 * is as expected during a successful 
	 * 
	 * structure copied from edu.austincollege.acvote.functional.ballot.BallotTests.testAdminAddBallot()
	 * 
	 * @throws Exception
	 */
	@Test
	public void testAdminCreateFacultyNewId() throws Exception
	{
		adminNavigateToFacultyListView();
		int startSize = 0;
		int endSize = 0;

		
		startSize = countFacultyInDataTable();
		
		// click on the createFaculty button
		WebElement createFacultyBtn = driver.findElement(By.id("createFacBtn"));
		assertNotNull(createFacultyBtn);
		createFacultyBtn.click();
		Thread.sleep(500);
		
		// input the new faculty ID
		WebElement newFacultyIdInput = driver.findElement(By.id("facultyIdInput"));
		assertNotNull(newFacultyIdInput);
		newFacultyIdInput.sendKeys("324342");
		
		// submit the modal form
		WebElement addFacultyBtn = driver.findElement(By.id("btnAddFaculty"));
		assertNotNull(addFacultyBtn);
		addFacultyBtn.click();
		
		Thread.sleep(500);		// load the next page

		// check that the AC ID copied over to the next view
		WebElement acIdBox = driver.findElement(By.id("acIdBox"));
		assertNotNull(acIdBox);
		assertEquals("324342", acIdBox.getText());
		
		//after creating find nav button element using xpath 
		WebElement ballotNavButton = driver.findElement(By.xpath(StringUtil.dquote("//a[@id='facultyLink']")));
		assertNotNull(ballotNavButton);
				
		// clicking on the ballots button in the nav bar
		ballotNavButton.click();
		assertEquals(base+"/faculty/list", driver.getCurrentUrl());
		
		endSize = countFacultyInDataTable();
		
		//compare the start size + 1 and the end size
		assertEquals(startSize+1, endSize);
		}
	
	/**
	 * Method to test view details function in faculty page.
	 * @throws Exception
	 */
	@Test
	public void testAdminViewFaculty() throws Exception
	{
		//adminNavigateToSelectedFaculty('1');
	}
	
	/**
	 * Method to test delete function in faculty list view and detailed view.
	 * @throws Exception
	 */
	@Test
	public void testAdminDeleteFaculty() throws Exception
	{
		//adminNavigateToSelectedBallot('1');
	}
	
	/**
	 * Method to test selected faculty details edits in faculty detailed list view.
	 * @throws Exception
	 */
	@Test
	public void testAdminEditFaculty() throws Exception
	{
		//adminNavigateToSelectedBallot('1');
	}
	
	
	@Test
	public void testAdminCreateFacultyInvalid() throws Exception
	{
		//adminNavigateToSelectedBallot('1');
	}
	@Test
	public void testAdminCreateFacultyValid() throws Exception
	{
		//adminNavigateToSelectedBallot('1');
	}
	@Test
	public void testAdminCreateFacultyDuplicate() throws Exception
	{
		//adminNavigateToSelectedBallot('1');
	}
}
