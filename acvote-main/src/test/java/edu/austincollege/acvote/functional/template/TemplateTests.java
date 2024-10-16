package edu.austincollege.acvote.functional.template;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.Ignore;
import org.junit.jupiter.api.Tag;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import edu.austincollege.acvote.functional.SeleniumTest;
import edu.austincollege.acvote.unit.StringUtil;
import org.openqa.selenium.JavascriptExecutor;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Tag("FunctionalTest")
public class TemplateTests extends SeleniumTest {

	/**
	 * Navigates to template list view page from log in
	 * @throws Exception
	 */
	protected void adminNavigateToTemplateListView() throws Exception {
		
		//log in and start app
		this.loginAsAdmin();
		assertEquals(base+"/home?continue", driver.getCurrentUrl());
		
		//find nav button element using xpath
		WebElement templateButton = driver.findElement(By.xpath(StringUtil.dquote("//a[@id='templateLink']")));
		assertNotNull(templateButton);
		
		//clicking template button in nav bar
		templateButton.click();
		assertEquals(base+"/templates", driver.getCurrentUrl());
		Thread.sleep(500);
	}
	
	/**
	 * Navigates to selected template page of template id: 1
	 * @param id
	 * @throws Exception
	 */
	protected void adminNavigateToSelectedTemplate(char id) throws Exception {
		
		adminNavigateToTemplateListView();
		
		//find row element containing template with id: 1
		WebElement row = driver.findElement(By.xpath(StringUtil.dquote("//tr[@tid="+id+"]")));
		assertNotNull(row);
		row.click();
		Thread.sleep(500);
		
		//find dropdown div element using xpath
		WebElement dropdownButton = row.findElement(By.className("dropdown")).findElement(By.id("dropdownBut"));
		assertNotNull(dropdownButton);
		dropdownButton.click();
		Thread.sleep(500);
		
		//find edit element using xpath
		WebElement dropdownViewDetailsButton = row.findElement(By.className("dropdown"))
				.findElement(By.id("viewDetailsBtn"));
		assertNotNull(dropdownViewDetailsButton);
		
		dropdownViewDetailsButton.click();
		assertEquals(base+"/template?tid="+String.valueOf(id), driver.getCurrentUrl());
	}
	
	/**
	 * Testing view all templates on template list view page
	 * @throws Exception
	 */
	@Test
	public void testAdminViewTemplatesPage() throws Exception {
		adminNavigateToTemplateListView();
	}
	
	/**
	 * Testing view details of template id: 1
	 * @throws Exception
	 */
	@Test
	public void testAdminViewSelectedTemplatePage() throws Exception {
		adminNavigateToSelectedTemplate('1');
	}
	
	/**
	 * Testing create template from scratch use case
	 * @throws Exception
	 */
	@Test
	public void testAdminAddTemplate() throws Exception {
		
		//start off at template list view with comparison variables
		adminNavigateToTemplateListView();
		int startSize = 0;
		int endSize = 0;
		
		//find table of templates
		WebElement templateList = driver.findElement(By.id("templateTable"));
		assertNotNull(templateList);
		
		//grabbing starting size of list of templates
		List<WebElement> templates = templateList.findElements(By.xpath("./child::*"));
		startSize = templates.size();
		
		//click create button
		WebElement createTemplateButton = driver.findElement(By.id("btnCreateTemplate"));
		assertNotNull(createTemplateButton);
		//createTemplateButton.click();
		
		// TODO finish implementation first, then come back and finish test
	}
	
	/**
	 * Testing edit template use case, template title
	 * @throws Exception
	 */
	@Test
	public void testAdminEditTemplateTitle() throws Exception {
		adminNavigateToSelectedTemplate('1');
		
		//find div containing paragraph element
		WebElement titleText = driver.findElement(By.xpath(StringUtil.dquote("//div[@id='tTitle']")));
		assertNotNull(titleText);
		titleText.click();
		Thread.sleep(500);
		
		//find generated input field
		WebElement titleInput = driver.findElement(By.xpath(StringUtil.dquote("//input[@id='tTitleInput']")));
		titleInput.sendKeys("New Template Title");
		Thread.sleep(500);
		
		//apply changes by clicking off input
		WebElement lineBreak = driver.findElement(By.xpath(StringUtil.dquote("//hr")));
		lineBreak.click();
		Thread.sleep(500);
		
		//check if changes persist
		titleText = driver.findElement(By.xpath(StringUtil.dquote("//p[@id='tTitleText']")));
		assertNotNull(titleText);
		Thread.sleep(500);
		assertEquals("New Template Title", titleText.getText());
	}
	
	/**
	 * Testing edit template use case, ballot tite
	 * @throws Exception
	 */
	@Test
	public void testAdminEditBallotTitle() throws Exception {
		adminNavigateToSelectedTemplate('1');
		
		//find div containing paragraph element
		WebElement titleText = driver.findElement(By.xpath(StringUtil.dquote("//div[@id='bTitle']")));
		assertNotNull(titleText);
		titleText.click();
		Thread.sleep(500);
		
		//find generated input field
		WebElement titleInput = driver.findElement(By.xpath(StringUtil.dquote("//input[@id='bTitleInput']")));
		titleInput.sendKeys("New Ballot Title");
		Thread.sleep(500);
		
		//apply changes by clicking off input
		WebElement lineBreak = driver.findElement(By.xpath(StringUtil.dquote("//hr")));
		lineBreak.click();
		Thread.sleep(500);
		
		//check if changes persist
		titleText = driver.findElement(By.xpath(StringUtil.dquote("//p[@id='bTitleText']")));
		assertNotNull(titleText);
		Thread.sleep(500);
		assertEquals("New Ballot Title", titleText.getText());
	}
	
	/**
	 * Testing edit template use case, description
	 * @throws Exception
	 */
	@Test
	public void testAdminEditDescription() throws Exception {
		adminNavigateToSelectedTemplate('1');
		
		//find div containing paragraph element
		WebElement descriptionText = driver.findElement(By.xpath(StringUtil.dquote("//div[@id='description']")));
		assertNotNull(descriptionText);
		descriptionText.click();
		Thread.sleep(500);
		
		//find generated input field
		WebElement descriptionInput = driver.findElement(By.xpath(StringUtil.dquote("//div[@class='note-editable']")));
		descriptionInput.sendKeys("New Description");
		Thread.sleep(500);
		
		//apply changes by clicking off input
		WebElement lineBreak = driver.findElement(By.xpath(StringUtil.dquote("//hr")));
		lineBreak.click();
		Thread.sleep(500);
		
		//check if changes persist
		descriptionText = driver.findElement(By.xpath(StringUtil.dquote("//div[@id='descText']")));
		assertNotNull(descriptionText);
		Thread.sleep(500);
		assertEquals("New Description", descriptionText.getText());
	}
	
	/**
	 * Testing edit template use case, instructions
	 * @throws Exception
	 */
	@Test
	public void testAdminEditInstructions() throws Exception {
		adminNavigateToSelectedTemplate('1');
		
		//find div containing paragraph element
		WebElement instructionsText = driver.findElement(By.xpath(StringUtil.dquote("//div[@id='instructions']")));
		assertNotNull(instructionsText);
		instructionsText.click();
		Thread.sleep(500);
		
		//find generated input field
		WebElement instructionsInput = driver.findElement(By.xpath(StringUtil.dquote("//div[@class='note-editable']")));
		instructionsInput.sendKeys("New Instructions");
		Thread.sleep(1000);
		
		//apply changes by clicking off input
		WebElement label = driver.findElement(By.id("instructionsLabel"));
		label.click();
		Thread.sleep(500);
		
		//check if changes persist
		instructionsText = driver.findElement(By.xpath(StringUtil.dquote("//div[@id='instrText']")));
		assertNotNull(instructionsText);
		Thread.sleep(500);
		assertEquals("New Instructions", instructionsText.getText());
	}
	
	/**
	 * Testing edit template use case, outcomes
	 * @throws Exception
	 */
	@Test
	//fix this
	public void testAdminEditOutcomes() throws Exception {
		adminNavigateToSelectedTemplate('1');
		
		//find div containing paragraph element
		WebElement outcomesText = driver.findElement(By.xpath(StringUtil.dquote("//div[@id='outcomes']")));
		assertNotNull(outcomesText);
		outcomesText.click();
		Thread.sleep(500);
		
		//find generated input field
		WebElement outcomesInput = driver.findElement(By.xpath(StringUtil.dquote("//input[@id='outcomesInput']")));
		outcomesInput.sendKeys("5");
		Thread.sleep(500);
		
		//apply changes by clicking off input
		WebElement label = driver.findElement(By.id("voteTypeLabel"));
		label.click();
		Thread.sleep(500);
		
		//check if changes persist
		outcomesText = driver.findElement(By.xpath(StringUtil.dquote("//p[@id='outcomesText']")));
		assertNotNull(outcomesText);
		Thread.sleep(500);
		assertEquals("5", outcomesText.getText());
	}
	
	/**
	 * Testing edit template use case, basis
	 * @throws Exception
	 */
	@Test
	public void testAdminEditBasis() throws Exception {
		adminNavigateToSelectedTemplate('1');
				
		//find div containing paragraph element
		WebElement comboBox = driver.findElement(By.xpath(StringUtil.dquote("//div[@id='basis']")));
		assertNotNull(comboBox);
		comboBox.click();
		Thread.sleep(500);
		
		WebElement comboBoxOption = comboBox.findElement(By.xpath(StringUtil.dquote("//option[@value='false']")));
		comboBoxOption.click();
		Thread.sleep(500);
		
		comboBox = driver.findElement(By.xpath(StringUtil.dquote("//div[@id='basis']")));
		comboBoxOption = comboBox.findElement(By.xpath(StringUtil.dquote("//select[@id='basisSelect']")));
		assertNotNull(comboBox);
		Thread.sleep(500);
		assertEquals("false", comboBoxOption.getAttribute("init"));
	}
}
