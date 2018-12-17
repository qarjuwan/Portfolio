package com.autotest.portfolio;

import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import static org.testng.Assert.assertTrue;

import java.io.BufferedInputStream;
import java.io.FileInputStream;

import java.io.IOException;

import java.util.ArrayList;
import java.util.Properties;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

import com.autotest.portfolio.selectors.SiteSelectors;
import com.autotest.portfolio.setup.Base;
import com.autotest.portfolio.setup.DriverActions;
import com.autotest.portfolio.setup.Keywords;

public class RunnerTest extends Base {

	@BeforeTest

	public void init() {

		DriverActions.initializeBrowser();
	}

	@Test(priority = 1)

	public void Login() {
		try {
			// get the property values
			Properties CONFIG = new Properties();
			BufferedInputStream inputStream = new BufferedInputStream(
					new FileInputStream("src/main/java/config/config.properties"));
			CONFIG.load(inputStream);

			// login
			Keywords.inputString(SiteSelectors.userNameTxt, CONFIG.getProperty("logInEmail"));
			Keywords.inputString(SiteSelectors.passwordTxt, CONFIG.getProperty("logInPassword"));

			// Verify login success
			loginSucc = Keywords.isElementPresent(SiteSelectors.meIcon);
			assertTrue(loginSucc, "Login Falied");

		} catch (Throwable t) {
			System.out.print(t.getMessage());
		}
	}

	@DataProvider

	public static Object[] getSearchTerm() throws InvalidFormatException, IOException {

		return new Object[] { Keywords.readSearchTerms() };
	}

	@Test(priority = 2, dependsOnMethods = { "Login" }, dataProvider = "getSearchTerm")

	public void Search(ArrayList<String> searchTerms) {
		try {
			if (loginSucc) {
				// Iterates over the search terms read from Excel
				for (int i = 0; i < searchTerms.size(); i++) {
					String searchTerm = searchTerms.get(i);
					Keywords.inputString(SiteSelectors.searchBox, searchTerm);
					Keywords.wait(15);

					// Verify that the search results page is opened
					if (Keywords.checkCurrentUrlcontent(searchTerm.replaceAll(" ", "%20")) == false) {
						System.out.println("Search results page is not loaded");

						// If search result page is shown, iterates over search members
					} else {
						Keywords.iterateOverSearchMembers(SiteSelectors.searchResults);
						Keywords.wait(15);
						Keywords.writeToEXcel(i);
					}
				}
			} else {
				System.out.println("Cannot proceed, login failed.");
			}
		} catch (Throwable t) {
			System.out.print(t.getMessage());
		}
	}

	@AfterTest

	public void Teardown() {

		DriverActions.closeBrowser();
	}
}
