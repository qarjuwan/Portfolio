package com.autotest.portfolio.setup;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import java.util.concurrent.TimeUnit;

import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;

import com.autotest.portfolio.selectors.SiteSelectors;

public class Keywords extends Base {

	/***
	 * Waits for specific amount of time, specified in seconds
	 * 
	 * @param seconds
	 * 
	 * 
	 */
	public static void wait(int seconds) {
		try {
			Thread.sleep(seconds * 1000);
		} catch (InterruptedException ex) {
			System.out.print(ex.getMessage());
		}
	}

	/***
	 * Check if element present.
	 *
	 * @param locator
	 * @return boolean
	 * 
	 */
	public static boolean isElementPresent(By locator) {
		boolean result = false;
		try {
			List<WebElement> elements;
			driver.manage().timeouts().setScriptTimeout(1, TimeUnit.SECONDS);
			elements = driver.findElements(locator);
			if (elements.size() != 0 && elements != null) {
				((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(false);", elements.get(0));
				result = true;
			}
			return result;
		} catch (Throwable t) {
			t.printStackTrace();
			System.out.print(t.getMessage());
			return result;
		}
	}

	/***
	 * Performs a click on element
	 * 
	 * @param locator
	 * 
	 * 
	 */
	public static void clickElement(By locator) {
		try {
			DriverActions.waitForElementBeClickable(locator);
			WebElement elementToClick = driver.findElement(locator);
			((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(false);", elementToClick);
			elementToClick.click();
		} catch (Throwable t) {
			System.out.print(t.getMessage());
		}
	}

	/***
	 * Retrieves the string value of an element.
	 * 
	 * @param locator
	 * @return String
	 * 
	 */
	public static String getElementText(By locator) {
		try {
			WebElement elementText = driver.findElement(locator);
			return elementText.getText().trim();

		} catch (Throwable t) {
			System.out.println(t.getMessage());
			return null;
		}
	}

	/***
	 * Inputs a specific String from the text variable
	 * 
	 * @param locator, inputData
	 * 
	 * 
	 */
	public static void inputString(By locator, String inputData) {
		try {
			DriverActions.waitForElementBeClickable(locator);
			WebElement inputField = driver.findElement(locator);

			((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(false);", inputField);

			Actions actions = new Actions(driver);
			actions.moveToElement(inputField);
			actions.click();
			actions.sendKeys(Keys.chord(Keys.CONTROL, "a"), inputData);
			actions.sendKeys(Keys.ENTER);
			actions.build().perform();

		} catch (Throwable t) {
			System.out.print(t.getMessage());
		}
	}

	/***
	 * Verifies if the currently opened URL contains a specific word
	 * 
	 * @param urlContetnt
	 * @return boolean
	 * 
	 */
	public static boolean checkCurrentUrlcontent(String urlContetnt) {
		try {
			String currentUrl = driver.getCurrentUrl();
			if (currentUrl.contains(urlContetnt)) {
				return true;
			} else {
				return false;
			}
		} catch (Throwable t) {
			System.out.print(t.getMessage());
			return false;
		}
	}

	/***
	 * Reads search terms from Excel file.
	 * 
	 * 
	 * @return ArrayList<String>
	 * 
	 */
	public static ArrayList<String> readSearchTerms() throws IOException, InvalidFormatException {

		InputStream inp = new FileInputStream("src/main/java/config/SearchTerms.xlsx");
		Workbook workbook = WorkbookFactory.create(inp);
		searchTerms = new ArrayList<>();

		Sheet sheet = workbook.getSheetAt(0);

		DataFormatter dataFormatter = new DataFormatter();
		for (Row row : sheet) {
			for (Cell cell : row) {
				String cellValue = dataFormatter.formatCellValue(cell);
				searchTerms.add(cellValue);
			}
		}
		return searchTerms;
	}

	/***
	 * Iterates over search members found in search results page and save their info
	 * to Excel file.
	 * 
	 * @param searchTerm, locator
	 * 
	 */
	public static void iterateOverSearchMembers(By locator) {
		try {
			scrollDown();
			searchLinks = new ArrayList<>();
			searchsInfo = new ArrayList<>();
			List<WebElement> CCTable = driver.findElements(locator);

			linksNum = CCTable.size();
			System.out.println("Number of clickable search links: " + linksNum);

			for (int i = 0; i < linksNum; i++) {
				WebElement linkClient = CCTable.get(i);
				searchLinks.add(i, linkClient.getText());
			}
			for (int j = 0; j < linksNum; j++) {
				Keywords.wait(10);
				WebElement client = driver.findElement(By.linkText(searchLinks.get(j)));
				Keywords.wait(10);
				js.executeScript("arguments[0].click();", client);
				Keywords.wait(5);
				scrollDown();

				if (Keywords.isElementPresent(SiteSelectors.experInfo) == false) {
					searchsInfo.add(j, " ");
					Keywords.wait(5);
				} else {
					Keywords.wait(5);
					searchsInfo.add(j, getElementText(SiteSelectors.experInfo));
					Keywords.wait(5);
				}
				driver.navigate().back();
				Keywords.wait(15);
				scrollDown();
			}

		} catch (Throwable t) {
			System.out.print(t.getMessage());
		}
	}

	/***
	 * Writes data to Excel file.
	 * 
	 * @param searchMember, searchInfo, rowNum
	 * 
	 * 
	 */
	public static void writeToEXcel(int sheetIndex) throws IOException, InvalidFormatException {

		File file = new File(System.getProperty("user.dir") + "\\" + "Search Results.xlsx");
		FileInputStream fis = new FileInputStream(file);
		XSSFWorkbook workbook = new XSSFWorkbook(fis);

		workbook.createSheet();
		XSSFSheet sheet = workbook.getSheetAt(sheetIndex);
		workbook.setSheetName(sheetIndex, searchTerms.get(sheetIndex));

		Row firstRow = sheet.createRow(0);
		Cell firstColTitle = firstRow.createCell(0);
		firstColTitle.setCellType(Cell.CELL_TYPE_STRING);
		firstColTitle.setCellValue("Search Member");

		Cell secondColTitle = firstRow.createCell(1);
		secondColTitle.setCellType(Cell.CELL_TYPE_STRING);
		secondColTitle.setCellValue("Member Info");

		for (int i = 0; i < linksNum; i++) {
			Row row = sheet.createRow(i + 1);
			Cell memberCell = row.createCell(0);
			memberCell.setCellType(Cell.CELL_TYPE_STRING);
			memberCell.setCellValue(searchLinks.get(i));

			Cell InfoCell = row.createCell(1);
			InfoCell.setCellType(Cell.CELL_TYPE_STRING);
			InfoCell.setCellValue(searchsInfo.get(i));
		}
		FileOutputStream fileOutput = new FileOutputStream(file);
		workbook.write(fileOutput);
		fileOutput.close();
	}

	/***
	 * Scroll down the page.
	 * 
	 * 
	 * 
	 * 
	 */
	public static void scrollDown() throws IOException, InvalidFormatException {

		js = (JavascriptExecutor) driver;
		js.executeScript("window.scrollBy(0,1000)");
		Keywords.wait(10);
	}
}