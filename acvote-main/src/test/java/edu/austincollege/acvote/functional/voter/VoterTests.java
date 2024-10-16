package edu.austincollege.acvote.functional.voter;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import edu.austincollege.acvote.functional.SeleniumTest;
import edu.austincollege.acvote.unit.StringUtil;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Tag("FunctionalTest")
public class VoterTests extends SeleniumTest {

	/**
	 * Helps to navigate to the IRV vote page
	 * 
	 * @throws Exception
	 */
	protected void adminNavigateToVoteIRV() throws Exception {

		// log in adn start app
		this.loginAsAdmin();
		assertEquals(base + "/home?continue", driver.getCurrentUrl());

		// find link to example vote page
		WebElement voteButton = driver.findElement(By.xpath(StringUtil.dquote("//a[@id='voteIRVLink']")));
		assertNotNull(voteButton);

		// clicking link
		voteButton.click();
		assertEquals(base + "/voter/vote?bid=3&token=0d354582ca957a881ac85b7dfdbe8163", driver.getCurrentUrl());
		Thread.sleep(500);
	}

	/**
	 * Helps to navigate to the IRV2 vote page
	 * 
	 * @throws Exception
	 */
	protected void adminNavigateToVoteIRV2() throws Exception {

		// log in adn start app
		this.loginAsAdmin();
		assertEquals(base + "/home?continue", driver.getCurrentUrl());

		// find link to example vote page
		WebElement voteButton = driver.findElement(By.xpath(StringUtil.dquote("//a[@id='voteIRV2Link']")));
		assertNotNull(voteButton);

		// clicking link
		voteButton.click();
		assertEquals(base + "/voter/vote?bid=4&token=0cfd26b73161d6d2d1b76832a2a72a32", driver.getCurrentUrl());
		Thread.sleep(500);
	}

	/**
	 * Testing helper method to navigate to IRV vote page
	 * 
	 * @throws Exception
	 */
//	@Test
//	public void testAdminNavigateToVoteIRV() throws Exception {
//		adminNavigateToVoteIRV();
//	}
//
//	/**
//	 * Testing helper method to navigate to IRV2 vote page
//	 * 
//	 * @throws Exception
//	 */
//	@Test
//	public void testAdminNavigateToVoteIRV2() throws Exception {
//		adminNavigateToVoteIRV2();
//	}

	/**
	 * Testing that user can drag and drop rank items on vote page
	 * 
	 * @throws Exception
	 */
	@Test
	public void testDragAndDropOptionsIRV() throws Exception {

		// navigating to vote page
		adminNavigateToVoteIRV();

		// grabbing list of items, picking 2nd and 3rd
		List<WebElement> list = driver.findElements(By.className("sort-item"));
		WebElement secondElement = list.get(1);
		WebElement thirdElement = list.get(2);
		assertNotNull(secondElement);
		assertNotNull(thirdElement);

		// dragging and dropping second item onto third
		Actions actions = new Actions(driver);
		actions.dragAndDrop(secondElement, thirdElement).build().perform();

		// grabbing list again and harvesting oid from 2nd and 3rd items
		list = driver.findElements(By.className("sort-item"));
		String secondOID = secondElement.getAttribute("oid");
		String thirdOID = thirdElement.getAttribute("oid");

		// should not be the same anymore because items traded places in list
		assertFalse(list.get(1).getAttribute("oid").equals(secondOID));
		assertFalse(list.get(2).getAttribute("oid").equals(thirdOID));
		assertTrue(list.get(1).getAttribute("oid").equals(thirdOID));
		assertTrue(list.get(2).getAttribute("oid").equals(secondOID));

	}

	/**
	 * Testing that voters can cast votes as expected
	 * 
	 * @throws Exception
	 */
	@Test
	public void testCastVoteIRV() throws Exception {

		// navigating to vote page
		adminNavigateToVoteIRV();

		// finding and clicking submit button
		WebElement submitButton = driver.findElement(By.id("btnSubmit"));
		assertNotNull(submitButton);
		submitButton.click();
		Thread.sleep(500);

		// should pull up review modal
		WebElement modal = driver.findElement(By.id("submitModal"));
		assertNotNull(modal);

		// finding and clicking cast vote button
		WebElement castVoteButton = driver.findElement(By.id("btnSubmitBallot"));
		assertNotNull(castVoteButton);
		castVoteButton.click();
		Thread.sleep(5000);
		assertEquals(base + "/voter/voted", driver.getCurrentUrl());

		// should be at voted page with thank you message
		WebElement thankYouMsg = driver.findElement(By.xpath("//div[@id='message']/h1"));
		assertNotNull(thankYouMsg);

	}

	/**
	 * Testing that voters can cast votes on irv2 ballots as expected
	 * 
	 * @throws Exception
	 */
	@Test
	public void testCastVoteIRV2() throws Exception {

		// navigating to irv2 vote page
		adminNavigateToVoteIRV2();

		// should be presented with filter modal immediately
		WebElement filterModal = driver.findElement(By.id("filterModal"));
		assertNotNull(filterModal);

		// finding and clicking done button
		WebElement doneButton = driver.findElement(By.id("btnSubmitOptions"));
		assertNotNull(doneButton);
		try {

			// clicking done should be impossible right now
			doneButton.click();
			fail("Button should not be clickable at this time");
		} catch (Exception e) {
		}

		// finding and clicking select all button
		WebElement selectAllBtn = driver.findElement(By.id("btnSelectAll"));
		assertNotNull(selectAllBtn);
		selectAllBtn.click();

		// done button should be clickable now
		try {

			doneButton.click();
			Thread.sleep(500);
		}
		catch (Exception e) {

			fail("Button should be clickable now");
		}
		
		/*
		 * Now we follow the same process as in IRV
		 */
				
		// finding and clicking submit button
		WebElement submitButton = driver.findElement(By.id("btnSubmit"));
		assertNotNull(submitButton);
		submitButton.click();
		Thread.sleep(500);

		// should pull up review modal
		WebElement modal = driver.findElement(By.id("submitModal"));
		assertNotNull(modal);

		// finding and clicking cast vote button
		WebElement castVoteButton = driver.findElement(By.id("btnSubmitBallot"));
		assertNotNull(castVoteButton);
		castVoteButton.click();
		Thread.sleep(5000);
		assertEquals(base + "/voter/voted", driver.getCurrentUrl());

		// should be at voted page with thank you message
		WebElement thankYouMsg = driver.findElement(By.xpath("//div[@id='message']/h1"));
		assertNotNull(thankYouMsg);

	}
}
