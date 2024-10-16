package edu.austincollege.acvote.functional.ballot;

import org.openqa.selenium.interactions.Actions;
import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.Duration;
import java.util.List;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import edu.austincollege.acvote.functional.SeleniumTest;
import edu.austincollege.acvote.unit.StringUtil;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Tag("FunctionalTest")
public class BallotTests extends SeleniumTest {

	/**
	 * This method will navigate to the ballots page from log in
	 * 
	 * @throws Exception
	 */
	protected void adminNavigateToBallotListView() throws Exception {
		// log in and start the application
		this.loginAsAdmin();
		assertEquals(base + "/home?continue", driver.getCurrentUrl());

		// find nav button element using xpath
		WebElement ballotButton = driver.findElement(By.xpath(StringUtil.dquote("//a[@id='ballotLink']")));
		assertNotNull(ballotButton);

		// clicking on the ballots button in the nav bar
		ballotButton.click();
		assertEquals(base + "/ballots", driver.getCurrentUrl());
		Thread.sleep(500);
	}

	/**
	 * This method will navigate as an admin user to the selected ballot page of
	 * ballot id: 1
	 * 
	 * @throws Exception
	 */
	protected void adminNavigateToSelectedBallot(char id) throws Exception {
		adminNavigateToBallotListView();

		// find row element contining the ballot with id 1
		WebElement row = driver.findElement(By.xpath(StringUtil.dquote("//tr[@bid=" + id + "]")));
		assertNotNull(row);
		row.click();
		Thread.sleep(500);

		// find dropdown div element using xpath
		WebElement dropdownButton = row.findElement(By.className("dropdown")).findElement(By.id("dropdownBut"));
		assertNotNull(dropdownButton);
		dropdownButton.click();
		Thread.sleep(500);

		// find view details element using xpath
		WebElement dropdownViewBallotButton = row.findElement(By.className("dropdown"))
				.findElement(By.id("viewDetailsBut"));
		assertNotNull(dropdownViewBallotButton);

		dropdownViewBallotButton.click();
		assertEquals(base + "/ballot?bid=" + String.valueOf(id), driver.getCurrentUrl());
	}

	/**
	 * Method to test reaching ballot list view
	 * 
	 * @throws Exception
	 */
	@Test
	public void testAdminViewBallotsPage() throws Exception {
		adminNavigateToBallotListView();
	}

	/**
	 * This method will test reaching the selected ballot page
	 * 
	 * @throws Exception
	 */
	@Test
	public void testAdminViewSelectedBallotPage() throws Exception {
		adminNavigateToSelectedBallot('1');
	}

	@Test
	public void testAdminAddBallot() throws Exception {
		// start off at ballot list view with variables we will compare at the end
		adminNavigateToBallotListView();
		int startSize = 0;
		int endSize = 0;

		// find the table of ballots
		WebElement ballotList = driver.findElement(By.id("ballotTable"));
		assertNotNull(ballotList);

		// get the starting size of the list of ballots
		List<WebElement> ballots = ballotList.findElements(By.xpath("./child::*"));
		startSize = ballots.size();

		// click the dropdown menu
		WebElement createBallotDropdown = driver.findElement(By.id("dropdownCreateBallot"));
		assertNotNull(createBallotDropdown);
		createBallotDropdown.click();

		// once menu shows up then click create from scratch
		waitForDialogById(driver, MAX_DIALOG_WAIT_SECONDS, "createMenu");
		WebElement createBallotScratch = driver.findElement(By.id("btnCreateBallot"));
		assertNotNull(createBallotScratch);
		createBallotScratch.click();
		Thread.sleep(500);

		// after creating find nav button element using xpath
		WebElement ballotNavButton = driver.findElement(By.xpath(StringUtil.dquote("//a[@id='ballotLink']")));
		assertNotNull(ballotNavButton);

		// clicking on the ballots button in the nav bar
		ballotNavButton.click();
		assertEquals(base + "/ballots", driver.getCurrentUrl());

		// find the table again
		ballotList = driver.findElement(By.id("ballotTable"));
		assertNotNull(ballotList);

		// collect the ending size of the list
		ballots = ballotList.findElements(By.xpath("./child::*"));
		endSize = ballots.size();

		// compare the start size + 1 and the end size
		assertEquals(startSize + 1, endSize);
	}

	/**
	 * This method will test editing the title from the home page
	 * 
	 * @throws Exception
	 */
	@Test
	public void testAdminEditBallotTitle() throws Exception {
		adminNavigateToSelectedBallot('1');

		// find div containing paragraph element
		WebElement titleText = driver.findElement(By.xpath(StringUtil.dquote("//div[@id='title']")));
		assertNotNull(titleText);
		titleText.click();
		Thread.sleep(500);

		// find the input field that is created
		WebElement titleTextField = driver.findElement(By.xpath(StringUtil.dquote("//input[@id='titleInput']")));
		titleTextField.sendKeys("New Title");
		Thread.sleep(500);

		// click in empty space to allow changes to be made
		WebElement lineBreak = driver.findElement(By.xpath(StringUtil.dquote("//hr")));
		lineBreak.click();
		Thread.sleep(500);

		// check if changes to title were made
		WebElement titleTextParagraph = driver.findElement(By.xpath(StringUtil.dquote("//p[@id='titleText']")));
		assertNotNull(titleTextParagraph);
		Thread.sleep(500);
		assertEquals("New Title", titleTextParagraph.getText());

	}

	/**
	 * This method will test editing the description from the home page
	 * 
	 * @throws Exception
	 */
	@Test
	public void testAdminEditBallotDescription() throws Exception {
		adminNavigateToSelectedBallot('1');

		// find div containing paragraph element
		WebElement descriptionText = driver.findElement(By.xpath(StringUtil.dquote("//div[@id='description']")));
		assertNotNull(descriptionText);
		descriptionText.click();
		Thread.sleep(500);

		// find the input field that is created
		WebElement descriptionTextField = driver
				.findElement(By.xpath(StringUtil.dquote("//div[@class='note-editable']")));
		descriptionTextField.sendKeys("New ");
		WebElement textFieldBoldButton = driver
				.findElement(By.xpath(StringUtil.dquote("//button[@class='note-btn note-btn-bold']")));
		textFieldBoldButton.click();
		descriptionTextField.sendKeys("Description");
		Thread.sleep(500);

		// click in empty space to allow changes to be made
		WebElement lineBreak = driver.findElement(By.xpath(StringUtil.dquote("//hr")));
		lineBreak.click();
		Thread.sleep(500);

		// check if changes to description were made
		WebElement descriptionTextParagraph = driver
				.findElement(By.xpath(StringUtil.dquote("//div[@id='descriptionText']")));
		assertNotNull(descriptionTextParagraph);
		Thread.sleep(500);
		assertEquals("New Description", descriptionTextParagraph.getText());
	}

	/**
	 * This method will test editing the instructions from the home page
	 * 
	 * @throws Exception
	 */
	@Test
	public void testAdminEditBallotInstructions() throws Exception {
		adminNavigateToSelectedBallot('1');

		// find div containing paragraph element
		WebElement instructionsText = driver.findElement(By.xpath(StringUtil.dquote("//div[@id='instructions']")));
		assertNotNull(instructionsText);
		instructionsText.click();
		Thread.sleep(500);

		// find the input field that is created
		WebElement instructionsTextField = driver
				.findElement(By.xpath(StringUtil.dquote("//div[@class='note-editable']")));
		instructionsTextField.sendKeys("New ");
		WebElement textFieldBoldButton = driver
				.findElement(By.xpath(StringUtil.dquote("//button[@class='note-btn note-btn-bold']")));
		textFieldBoldButton.click();
		instructionsTextField.sendKeys("Instructions");
		Thread.sleep(500);

		// click in empty space to allow changes to be made
		WebElement lineBreak = driver.findElement(By.xpath(StringUtil.dquote("//hr")));
		lineBreak.click();
		Thread.sleep(500);

		// check if changes to instructions were made
		WebElement instructionsTextParagraph = driver
				.findElement(By.xpath(StringUtil.dquote("//div[@id='instructionsText']")));
		assertNotNull(instructionsTextParagraph);
		Thread.sleep(500);
		assertEquals("New Instructions", instructionsTextParagraph.getText());

	}

	/**
	 * This method will test editing the basis from the home page
	 * 
	 * @throws Exception
	 */
	@Test
	public void testAdminEditBallotBasis() throws Exception {
		adminNavigateToSelectedBallot('1');

		// find div containing paragraph element
		WebElement comboBox = driver.findElement(By.xpath(StringUtil.dquote("//select[@id='selBasis']")));
		assertNotNull(comboBox);
		comboBox.click();
		Thread.sleep(500);

		// find the input field that is created
		WebElement comboBoxOption = comboBox.findElement(By.xpath(StringUtil.dquote("//option[@value='false']")));
		comboBoxOption.click();
		Thread.sleep(500);

		// check if changes to instructions were made
		comboBox = driver.findElement(By.xpath(StringUtil.dquote("//select[@id='selBasis']")));
		WebElement comboBoxOptionSel = comboBox
				.findElement(By.xpath(StringUtil.dquote("//option[@selected='selected']")));
		assertNotNull(comboBox);
		Thread.sleep(500);

		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(2));
		wait.until(new ExpectedCondition<Boolean>() {

			@Override
			public Boolean apply(WebDriver driver) {
				System.err.println("wait");
				return comboBoxOptionSel.isDisplayed();
			}
		});

		assertEquals("Custom Options", comboBoxOptionSel.getText());

	}

	/**
	 * This method will test editing the voteType from the home page
	 * 
	 * @throws Exception
	 */
	@Test
	public void testAdminEditBallotVoteType() throws Exception {
		adminNavigateToSelectedBallot('1');

		// find select element
		WebElement comboBox = driver.findElement(By.xpath(StringUtil.dquote("//select[@id='typeOfVoteSelect']")));
		assertNotNull(comboBox);
		comboBox.click();
		Thread.sleep(500);

		// find the option for IRV2
		WebElement comboBoxOption = comboBox.findElement(By.xpath(StringUtil.dquote("//option[@value='IRV2']")));
		comboBoxOption.click();
		Thread.sleep(500);

		// check if changes to vote type were made
		comboBox = driver.findElement(By.xpath(StringUtil.dquote("//select[@id='typeOfVoteSelect']")));
		WebElement selectedOption = comboBox
				.findElement(By.xpath(StringUtil.dquote("//option[@selected='selected']")));
		assertNotNull(comboBox);

	}

	/**
	 * This method will test editing the ballot outcomes from the home page
	 * 
	 * @throws Exception
	 */
	@Test
	// this one
	public void testAdminEditBallotOutcomes() throws Exception {
		adminNavigateToSelectedBallot('1');

		// this object will help scroll
		JavascriptExecutor jse = (JavascriptExecutor) driver;

		// find div containing paragraph element and making sure it is on screen
		WebElement outcomesText = driver.findElement(By.xpath(StringUtil.dquote("//div[@id='outcomes']")));
		assertNotNull(outcomesText);
		WebElement bottomElement = driver.findElement(By.id("bottom"));
		jse.executeScript("arguments[0].scrollIntoView(true);", bottomElement); // if the element is on bottom.
		Thread.sleep(500);
		outcomesText.click();
		Thread.sleep(500);

		// find the input field that is created
		WebElement outcomesNumField = driver.findElement(By.xpath(StringUtil.dquote("//input[@id='outcomesInput']")));
		outcomesNumField.sendKeys("10");
		Thread.sleep(500);

		// click in empty space to allow changes to be made
		WebElement lineBreak = driver.findElement(By.id("voteTypeTitle"));
		lineBreak.click();
		Thread.sleep(500);

		// check if changes to outcomes were made
		WebElement outcomesTextParagraph = driver.findElement(By.xpath(StringUtil.dquote("//p[@id='outcomesNum']")));
		assertNotNull(outcomesTextParagraph);
		Thread.sleep(500);
		assertEquals("10", outcomesTextParagraph.getText());

	}

	/**
	 * This method will test editing the start date from the home page
	 * 
	 * @throws Exception
	 */
	@Test
	public void testAdminEditBallotStartDate() throws Exception {
		adminNavigateToSelectedBallot('1');

		// this object will help scroll
		JavascriptExecutor jse = (JavascriptExecutor) driver;

		// find div containing paragraph element and making sure it is on screen
		WebElement startDateDiv = driver.findElement(By.xpath(StringUtil.dquote("//div[@id='startTime']")));
		assertNotNull(startDateDiv);
		WebElement bottomElement = driver.findElement(By.id("bottom"));
		jse.executeScript("arguments[0].scrollIntoView(true);", bottomElement); // if the element is on bottom.
		Thread.sleep(500);
		startDateDiv.click();
		Thread.sleep(500);

		// find the input field that is created
		WebElement startTimeDateField = driver
				.findElement(By.xpath(StringUtil.dquote("//input[@id='startTimeInput']")));
		// startTimeDateField.sendKeys("2023-06-09 8:00:00");
		startTimeDateField.sendKeys("06-09-2023 8:00:00");
		Thread.sleep(500);

		// click in empty space to allow changes to be made
		WebElement emptySpace = driver.findElement(By.id("voteTypeTitle"));
		emptySpace.click();
		Thread.sleep(500);

		// check if changes to startDate were made
		WebElement startDateText = driver.findElement(By.xpath(StringUtil.dquote("//p[@id='startTimeDate']")));
		assertNotNull(startDateText);
		Thread.sleep(500);
		assertEquals("9 Jun 2023 16:00:00", startDateText.getText());

	}

	/**
	 * This method will test editing the end date from the home page
	 * 
	 * @throws Exception
	 */
	@Test
	public void testAdminEditBallotEndDate() throws Exception {
		adminNavigateToSelectedBallot('1');

		// this object will help scroll
		JavascriptExecutor jse = (JavascriptExecutor) driver;

		// find div containing paragraph element and making sure it is on screen
		WebElement endDateDiv = driver.findElement(By.xpath(StringUtil.dquote("//div[@id='endTime']")));
		assertNotNull(endDateDiv);
		assertNotNull(driver.findElement(By.id("bottom")));
		WebElement bottomElement = driver.findElement(By.id("bottom"));
		jse.executeScript("arguments[0].scrollIntoView(true);", bottomElement); // if the element is on bottom.
		Thread.sleep(500);
		endDateDiv.click();
		Thread.sleep(500);

		// find the input field that is created
		WebElement endTimeDateField = driver.findElement(By.xpath(StringUtil.dquote("//input[@id='endTimeInput']")));
		endTimeDateField.sendKeys("06-20-2023 8:00:00");
		Thread.sleep(500);

		// click in empty space to allow changes to be made
		WebElement emptySpace = driver.findElement(By.id("voteTypeTitle"));
		emptySpace.click();
		Thread.sleep(500);

		// check if changes to endDate were made
		WebElement endDateText = driver.findElement(By.xpath(StringUtil.dquote("//p[@id='endTimeDate']")));
		assertNotNull(endDateText);
		Thread.sleep(500);
		assertEquals("20 Jun 2023 16:00:00", endDateText.getText());

	}

	/**
	 * This method will test editing the voting group from the home page
	 * 
	 * @throws Exception
	 */
	@Test
	public void testAdminEditBallotVoters() throws Exception {
		adminNavigateToSelectedBallot('1');

		// this object will help scroll
		JavascriptExecutor jse = (JavascriptExecutor) driver;

		// find div containing paragraph element
		WebElement comboBox = driver.findElement(By.xpath(StringUtil.dquote("//div[@id='votingGroup']")));
		assertNotNull(comboBox);
		WebElement bottomElement = driver.findElement(By.id("bottom"));
		jse.executeScript("arguments[0].scrollIntoView(true);", bottomElement); // if the element is on bottom.
		Thread.sleep(500);
		comboBox.click();
		Thread.sleep(500);

		// find the input field that is created
		WebElement comboBoxOption = comboBox.findElement(By.xpath(StringUtil.dquote("//option[@value='SC']")));
		comboBoxOption.click();
		Thread.sleep(500);

		// check if changes to voting group were made
		comboBox = driver.findElement(By.xpath(StringUtil.dquote("//div[@id='votingGroup']")));
		comboBoxOption = comboBox.findElement(By.xpath(StringUtil.dquote("//select[@id='division']")));
		assertNotNull(comboBox);
		Thread.sleep(500);
		assertEquals("SC", comboBoxOption.getAttribute("init"));

	}

	/**
	 * This method will test deleting a ballot
	 * 
	 * @throws Exception
	 */
	// fix this
	@Test
	public void testAdminDeleteBallot() throws Exception {
		char id = '2';
		adminNavigateToSelectedBallot(id);

		// find the delete button that will bring up the modal
		WebElement deleteBallotButton = driver.findElement(By.id("deleteButton"));
		assertNotNull(deleteBallotButton);
		deleteBallotButton.click();

		// wait until the modal shows up
		waitForDialogById(driver, MAX_DIALOG_WAIT_SECONDS, "deleteModal");

		String page = driver.getCurrentUrl();
		System.err.println(page);

		// press delete on the modal
		WebElement modalDeleteButton = driver.findElement(By.id("btnDeleteBallot"));
		modalDeleteButton.click();

		this.waitForPageChange(driver, page, Duration.ofSeconds(10));

		System.err.println(driver.getCurrentUrl());

		// should be on the list page now....a different page...not the details page.
		assertEquals(base + "/ballots", driver.getCurrentUrl());

	}

	@Test
	public void testAdminCustomOptionID() throws Exception {
		JavascriptExecutor jse = (JavascriptExecutor) driver;
		char id = '3';
		adminNavigateToSelectedBallot(id);

		WebElement clear = driver.findElement(By.id("clearCan"));
		assertNotNull(clear);

		// Scroll clear button into view to ensure it is clickable
		jse.executeScript("arguments[0].scrollIntoView();", clear);

		Thread.sleep(500);
		clear.click();
		Thread.sleep(5000);

		WebElement btnAddOption = driver.findElement(By.id("btnAddOption"));
		assertNotNull(btnAddOption);

		// Scroll btnAddOption into view to ensure it is clickable
		WebElement bottomElement = driver.findElement(By.id("bottom"));
		jse.executeScript("arguments[0].scrollIntoView(true);", bottomElement); // if the element is on bottom.
		Thread.sleep(500);// waits only necessary for headless testing so the script has long enough to
							// scroll down
		btnAddOption.click();
		waitForDialogById(driver, MAX_DIALOG_WAIT_SECONDS, "newOptionDialog");

		WebElement optid = driver.findElement(By.id("optid"));
		assertNotNull(optid);

	}

}
