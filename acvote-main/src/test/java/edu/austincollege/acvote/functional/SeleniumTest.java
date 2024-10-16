package edu.austincollege.acvote.functional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.util.concurrent.TimeUnit;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;

import edu.austincollege.acvote.unit.StringUtil;


@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public abstract class SeleniumTest {

	private static final boolean HEADLESS = true;
	
	
	
	protected static final Duration MAX_PAGE_WAIT_SECONDS = Duration.ofSeconds(4);
	protected static final Duration MAX_DIALOG_WAIT_SECONDS = Duration.ofSeconds(4);
	
	public static final String WIN_DRIVERPATH = "src/test/resources/chromedriver_win32/chromedriver.exe";
	public static final String MAC_64_DRIVERPATH = "src/test/resources/chromedriver_mac64/chromedriver";
	public static final String MAC_ARM_DRIVERPATH = "src/test/resources/chromedriver_arm64/chromedriver";
	
	private static Logger log = LoggerFactory.getLogger(SeleniumTest.class);

	@LocalServerPort
	protected int port;

	protected String base;


	protected static ChromeDriverService service;

	protected WebDriver driver;

	ChromeOptions options;
	
	@BeforeEach
	public void setUp() throws Exception {
		
		options = new ChromeOptions();
		options.addArguments("--no-sandbox"); // Bypass OS security model, MUST BE THE VERY FIRST OPTION
		
		if (HEADLESS)
			options.addArguments("--headless");
		
		//options.setExperimentalOption("useAutomationExtension", false);
		options.addArguments("start-maximized"); // open Browser in maximized mode
		options.addArguments("disable-infobars"); // disabling infobars
		options.addArguments("--disable-extensions"); // disabling extensions
		options.addArguments("--disable-gpu"); // applicable to windows os only
		options.addArguments("--disable-dev-shm-usage"); // overcome limited resource problems
		options.addArguments("--remote-allow-origins=*");
		options.addArguments("--window-size=2000,4000");


		driver = new ChromeDriver(service,options);

		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10)); 
		
		this.base = "http://localhost:" + port+"/acvote";

		log.info("base URL is:{}",base);
		
	}

	

	@BeforeAll
	public static void createAndStartService() throws Exception {
		String path = null;
		
		System.err.println(System.getProperty("os.name"));
		
		if(System.getProperty("os.name").toLowerCase().contains("win")) {
			System.err.println("windows");
			path = WIN_DRIVERPATH;

		} else if(System.getProperty("os.name").toLowerCase().contains("aarch64")) {
			System.err.println("mac arm");
			path = MAC_ARM_DRIVERPATH;
		}
		else if(System.getProperty("os.name").toLowerCase().contains("mac")) {
			System.err.println("mac intel");
			path = MAC_64_DRIVERPATH;

		}
		
		if (path == null) throw new Exception("unrecognized operating system.  cannot set chrome driver. ");

		
		System.setProperty("webdriver.chrome.driver", path);

		
		try {
			service = new ChromeDriverService.Builder()
					.usingDriverExecutable(new File(path))
					.usingAnyFreePort()
					.build();

			
			service.start();
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw e;
		}
	}

	@AfterAll
	public static void stopService() {

		service.stop();
	}


	@AfterEach
	public void quitDriver() {

		driver.close();
		
	}

	/**
	 * On run, this method cause the test system to wait until the dialog box is fully visible (based on the given ID for said Dialog box)
	 * before allowing the code following it to run
	 * 
	 * 
	 * @param driver
	 * @param waitTime
	 * @param dialogId
	 */
	public void waitForDialogById(WebDriver driver, Duration waitTime, String dialogId) {

		WebDriverWait wait = new WebDriverWait(driver, waitTime);

		wait.until(new ExpectedCondition<Boolean>() {

			@Override
			public Boolean apply(WebDriver driver) {

				Boolean isPresent;
				isPresent = driver.findElement(By.id(dialogId)).isDisplayed();

				return isPresent;
			}
		}
				);

	}
	
	/**
	 * On run, this method will cause the test system to wait until the dialog box is fully visible (based on the given xpath for said Dialog box)
	 * before allowing the code following it to run
	 * 
	 * 
	 * @param driver
	 * @param waitTime
	 * @param dialogXpath
	 */
	public void waitForDialogByXpath(WebDriver driver, Duration waitTime, String dialogXpath) {


		WebDriverWait wait = new WebDriverWait(driver, waitTime);

		wait.until(new ExpectedCondition<Boolean>() {

			@Override
			public Boolean apply(WebDriver driver) {
				Boolean isPresent;
				isPresent = driver.findElement(By.xpath(dialogXpath)).isDisplayed();
				return isPresent;
			}
		}
				);

	}
	
	/**
	 * On run, this method will cause the test system to wait until the dialog box is closed (based on the given xpath for said Dialog box)
	 * before allowing the code following it to run
	 * 
	 * 
	 * @param driver
	 * @param waitTime
	 * @param dialogXpath
	 */
	public void waitForDialogToCloseByXpath(WebDriver driver, Duration waitTime, String dialogXpath) {


		WebDriverWait wait = new WebDriverWait(driver, waitTime);

		wait.until(new ExpectedCondition<Boolean>() {

			@Override
			public Boolean apply(WebDriver driver) {
				Boolean isPresent = null;
				if(driver.findElement(By.xpath(dialogXpath)).isDisplayed() == true) {
					isPresent = false;
				} else {
					isPresent = true;
				}
				return isPresent;
			}
		}
				);

	}
	
	/**
	 * On run, this method cause the test system to wait until the dialog box is fully closed (based on the given ID for said Dialog box)
	 * before allowing the code following it to run
	 * 
	 * 
	 * @param driver
	 * @param waitTime
	 * @param dialogId
	 */
	public void waitForDialogToCloseById(WebDriver driver, Duration waitTime, String dialogId) {


		WebDriverWait wait = new WebDriverWait(driver, waitTime);

		wait.until(new ExpectedCondition<Boolean>() {

			@Override
			public Boolean apply(WebDriver driver) {

				Boolean isGone;

				isGone = !driver.findElement(By.id(dialogId)).isDisplayed();

				return isGone;
			}
		}
				);

	}

	
	/**
	 * Waits for a page to change;
	 * 
	 * @param driver
	 * @param oldPageUrl the URL of old page
	 * @param waitTime max time to wait in seconds
	 */
	protected void waitForPageChange(WebDriver driver, String oldPageUrl, Duration waitTimeOutSecs) {

		WebDriverWait wait = new WebDriverWait(driver, waitTimeOutSecs);
		
		wait.until(new ExpectedCondition<Boolean>() {

			@Override
			public Boolean apply(WebDriver driver) {

				boolean same = driver.getCurrentUrl().equals(oldPageUrl);
				
				return !same;

			}
		});

	}

	
	/**
	 * Convenient wrapper function for logging in as admin user.
	 * @throws InterruptedException 
	 */
	protected void loginAsAdmin() throws InterruptedException {
		loginAs("admin","admin","/home");
	}
	
	/**
	 * Performs authentication on our site starting at the site splash
	 * page.  Given the userid and password and 
	 * @param userid
	 * @param password
	 * @param home
	 * @throws InterruptedException 
	 */
	protected void loginAs(String userid, String password, String homeURL) throws InterruptedException {
		log.info("login as {},{}, at {}",userid,password,base+homeURL);
		driver.get(base + homeURL);
	
		String url = driver.getCurrentUrl();
		
//		WebElement link = driver.findElement(By.linkText("Log In"));
//		link.click();
//	
//		waitForPage(driver, url, MAX_PAGE_WAIT_SECONDS);
		
		
		
		/*
		 * should be at the login page now
		 */
		assertEquals(base+"/login", driver.getCurrentUrl());
	
		
		waitForDialogById(driver,MAX_PAGE_WAIT_SECONDS,"username");
		
		/*
		 * find and populate user text element
		 */
		WebElement txtUser = driver.findElement(By.id("username"));
		
		txtUser = driver.findElement(By.xpath(StringUtil.dquote("//input[@name='username']")));
			
		if (!options.is("--headless"))
			Thread.sleep(1000);
			
		txtUser.sendKeys(userid);

	
		/*
		 * find and populate password text element
		 */
		WebElement txtPw = driver.findElement(By.id("password"));
		assertNotNull(txtPw);
	
		log.debug("click in user password");
		txtPw.sendKeys(password);
	
	
		/*
		 * submit the form
		 */
		
		WebElement form = driver.findElement(By.className("form-signin"));		
		assertNotNull(form);
		form.submit();
		log.debug("login form submitted");
	
		/*
		 * should lead us to the admin's home page.
		 */

	
	}


}