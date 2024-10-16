package edu.austincollege.acvote.functional.home;


import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import edu.austincollege.acvote.functional.SeleniumTest;


@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Tag("FunctionalTest")
public class AdminUserLoginTest extends SeleniumTest {
	
	
	/**
	 * Test to ensure that the incomplete "/" endpoint redirects to
	 * a more proper "/home" ....which is really our admin site landing URL.
	 * 
	 * @throws Exception
	 */
    @Test
    public void testRedirectToHome() throws Exception {
        // driver.get(base + "/");
        this.loginAs("admin","admin","/");
        
        assertEquals("AC Vote | Home",driver.getTitle());
        assertTrue(driver.getCurrentUrl().contains(base+"/home"));
    }
    
    
    /**
     * Test to ensure that authenticating as an admin really does
     * navigate us to the admin's home page.
     * 
     * @throws Exception
     */
    @Test
    public void testAdminLogin() throws Exception {
    	
        this.loginAsAdmin();
				
		WebElement welcome = driver.findElement(By.xpath("//h1"));
		assertNotNull(welcome);
		assertTrue(welcome.getText().contains("Welcome"));
		
		WebElement link = driver.findElement(By.linkText("Manage Ballots:"));
		assertNotNull(link);
		

        
    }
    
}