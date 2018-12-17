package com.autotest.portfolio.selectors;

import org.openqa.selenium.By;

public class SiteSelectors {

	// Login Inputs
	public static By userNameTxt = By.xpath(".//*[@id='login-email']");
	public static By passwordTxt = By.xpath(".//*[@id='login-password']");

	// Login Output
	public static By meIcon = By.xpath(".//*[@id='nav-settings__dropdown-trigger']");

	// Search
	public static By searchBox = By.className("nav-search-typeahead");
	public static By searchResults = By.cssSelector(".name.actor-name");

	// Profile
	public static By experInfo = By.className("pv-entity__summary-info ");
}
