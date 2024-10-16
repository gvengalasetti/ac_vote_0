package edu.austincollege.acvote.functional.users;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import edu.austincollege.acvote.functional.SeleniumTest;
import edu.austincollege.acvote.unit.StringUtil;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Tag("FunctionalTest")
public class UserTests extends SeleniumTest {

	/**
	 * This method will navigate to the users page from log in
	 * 
	 * @throws Exception
	 */
	protected void adminNavigateToUserListView() throws Exception {

		this.loginAsAdmin();
		assertEquals(base + "/home?continue", driver.getCurrentUrl());

		WebElement userButton = driver.findElement(By.xpath(StringUtil.dquote("//a[@id='userLink']")));
		assertNotNull(userButton);

		userButton.click();
		assertEquals(base + "/users", driver.getCurrentUrl());
		Thread.sleep(500);
	}

	/**
	 * Method to test reaching users list view
	 * 
	 * @throws Exception
	 */
	@Test
	public void testAdminViewUsersPage() throws Exception {
		adminNavigateToUserListView();
	}

	@Test
	public void testAdminCreateUser() throws Exception {

		// start at users list view
		adminNavigateToUserListView();

		// variables for comparison at the end
		int startSize = 0;
		int endSize = 0;

		// find table of users
		WebElement userList = driver.findElement(By.id("userTable"));
		assertNotNull(userList);

		// get starting size of list
		List<WebElement> users = userList.findElements(By.xpath("./child::*"));
		startSize = users.size();

		// click create button
		WebElement createButton = driver.findElement(By.id("startCreate"));
		assertNotNull(createButton);
		createButton.click();

		// find uid input field and give it uid
		WebElement uidInput = driver.findElement(By.xpath(StringUtil.dquote("//input[@id='uidInput']")));
		uidInput.sendKeys("arose007");

		// find role input field
		WebElement roleSelect = driver.findElement(By.xpath(StringUtil.dquote("//select[@id='roleSelect']")));
		assertNotNull(roleSelect);
		roleSelect.click();

		// selecting role
		WebElement roleOption = driver.findElement(By.xpath(StringUtil.dquote("//option[@value='EDITOR']")));
		roleOption.click();

		// finishing create by clicking on create button
		WebElement submitButton = driver.findElement(By.id("btnCreateUser"));
		submitButton.click();
		Thread.sleep(5000);

		// finding table again and getting final size
		userList = driver.findElement(By.id("userTable"));
		assertNotNull(userList);
		users = userList.findElements(By.xpath("./child::*"));
		endSize = users.size();

		// comparing sizes
		assertEquals(startSize + 1, endSize);
	}

	@Test
	public void testAdminDeleteUser() throws Exception {

		// start at users list view
		adminNavigateToUserListView();

		// variables for comparison at the end
		int startSize = 0;
		int endSize = 0;

		// find table of users
		WebElement userList = driver.findElement(By.id("userTable"));
		assertNotNull(userList);

		// get starting size of list
		List<WebElement> users = userList.findElements(By.xpath("./child::*"));
		startSize = users.size();

		// find row with user "arosenberg20"
		WebElement row = driver.findElement(By.xpath(StringUtil.dquote("//tr[@uid='arosenberg20']")));
		assertNotNull(row);
		row.click();

		// find dropdown elipse button and clicking
		WebElement dropdownButton = row.findElement(By.className("dropdown")).findElement(By.id("dropdownBut"));
		assertNotNull(dropdownButton);
		dropdownButton.click();

		// find dropdown delete button and clicking
		WebElement dropdownDeleteButton = row.findElement(By.className("dropdown")).findElement(By.id("deleteUserBtn"));
		assertNotNull(dropdownDeleteButton);
		dropdownDeleteButton.click();

		// wait until the modal shows up
		waitForDialogById(driver, MAX_DIALOG_WAIT_SECONDS, "deleteModal");

		// find delete button and click
		WebElement modalDeleteButton = driver.findElement(By.id("btnDeleteUser"));
		modalDeleteButton.click();
		Thread.sleep(5000);

		// assert we are still on list view page
		assertTrue(driver.getCurrentUrl().startsWith(base + "/users"),"cannot confirm at users list page");

		// finding table again and getting final size
		userList = driver.findElement(By.id("userTable"));
		assertNotNull(userList);
		users = userList.findElements(By.xpath("./child::*"));
		endSize = users.size();

		// comparing sizes
		assertEquals(startSize - 1, endSize);
	}
}
