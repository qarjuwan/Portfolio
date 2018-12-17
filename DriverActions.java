package com.autotest.portfolio.setup;

import java.io.File;
import org.openqa.selenium.By;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class DriverActions extends Base {

	/***
	 * Initialises Chrome browser by WebDriver Object
	 * 
	 * 
	 * @return Pass if the browser is initialised and Fail else where
	 * 
	 */
	public static void initializeBrowser() {
		try {
			File server = new File("src/main/java/config/" + "chromedriver.exe");
			String path = server.getAbsolutePath().replaceAll("\\\\", "/");
			System.setProperty("webdriver.chrome.driver", path);
			driver = new ChromeDriver();
			driver.manage().window().maximize();
			driver.navigate().to("https://www.linkedin.com/");

		} catch (Throwable t) {
			System.out.print(t.getMessage());
		}
	}

	/***
	 * Terminates the Browser
	 * 
	 * 
	 * 
	 * 
	 */
	public static void closeBrowser() {
		try {
			if (driver != null) {
				driver.quit();
				driver = null;
			}
			Runtime.getRuntime().exec("taskkill /F /IM chrome.exe");
			Runtime.getRuntime().exec("taskkill /F /IM chromedriver.exe");
			Keywords.wait(10);

		} catch (Throwable t) {
			System.out.print(t.getMessage());
		}
	}

	/**
	 * Wait for the element to be click able .
	 *
	 * @param locator
	 * @return WebElement
	 * 
	 */
	public static void waitForElementBeClickable(By locator) {

		try {
			WebDriverWait wait = new WebDriverWait(driver, TIMEOUT);
			wait.until(ExpectedConditions.elementToBeClickable(locator));

		} catch (Throwable t) {
			System.out.print(t.getMessage());
		}
	}
}
