package dotDash;


/*
 *  Author: Firdavsii Majidzoda
 *  Date: 01/26/2019
 *  Project: NSS-TODOList v 1.1
 *  Purpose: Automate DotDash testing challenge (https://github.com/Dotdashcom/qa-exercise)
 */

import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.concurrent.TimeUnit;
import javax.imageio.ImageIO;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Point;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

public class MainClass {
	// Global fields:

	// Selenium WebDriver
	static WebDriver webDriver;

	// Path to WebDriver
		final static String webDriverPath = "/Users/firdavsiimajidzoda/Documents/chromedriver";
		// Base url
		final static String baseUrl = "http://127.0.0.1/www/index.php";

		// Destination to save picture reports
		static final String reportDest = "/Applications/XAMPP/htdocs/www/test-results/";

		// Source file to for WebElement's xpath
		static final String xpath = "/Applications/XAMPP/htdocs/www/automation/xpath.txt";

		// Source file to archive test reports
		static final String fullReportDirectory = "/Applications/XAMPP/htdocs/www/test-results/test-report.txt";
		
		String aa = "aa";

	// Formatting strings for report
	static final String tcFormat = "| %1$-20s | ";
	static final String statusFormat = " %2$tb %2$td, %2$tY  | ";
	static final String dateFormat = " %3$3s |%n";
	static final String format = tcFormat.concat(statusFormat).concat(dateFormat);
	static final String line = new String(new char[83]).replace('\0', '-');

	public static void main(String[] args) throws Exception {
		// Set path to Chrome Web Driver. (Replace webDriverPath with your path to Web Driver )
		
		System.setProperty("webdriver.chrome.driver", webDriverPath);
		
		// Initialize Web driver
		webDriver = new ChromeDriver();

		// Write header table for reports
		writeReportHeader();

		// Test all User Interface
		// Test cases: TC1.1 - TC1.17
		getUserInterfaceTests();

		// Test cases: TC2.1-TC2.14
		isWebElementsEnabled();

		// Test cases: TC3.1-TC7.
		addButtonTest();

		addCategoryTests();

		editRowTests();

		completeButtonTests();

		removeButtonTests();
		
		// Quit and close WebDriver
		webDriver.close();
		webDriver.quit();
		
		testAPI();
		

	}

	/**
	 * Executes all test cases for User Interface testing (TC1.1 - TC1.22)
	 * @throws Exception
	 */
	public static void getUserInterfaceTests() throws Exception {
		// Test main page
		loadMainPage(true);

		// TODO: uncomment getWebElementsTestCases() after all test cases are done.
		// Test all WebElements within main page
		webElementsUItest();
	}

	/**
	 * Test case: TC1.1 Load main page: 127.0.0.1/nss-todo-automation/index.php Take a screen shot
	 * of the page and save picture for report.
	 * 
	 * @throws Exception
	 */
	public static void loadMainPage(boolean archive) throws Exception {
		// Load main page
		webDriver.get(baseUrl);

		// Wait for page to load
		webDriver.manage().timeouts().implicitlyWait(3, TimeUnit.SECONDS);

		// TODO: check if web page is loaded

		// Archive the load page ?
		if (archive) {
			// Hold the file name
			String fileName = getTimeForFileName();
			// Take a screen shot of the page and save it in directory
			takeFullScreenShot(webDriver, reportDest + "TC1.1/TC1.1-" + fileName);

			// Archive report with passed status
			writeToFile(new String[] { "TC1.1", "Passed", fileName });

		}
	}

	/**
	 * Test case: TC1.2-1.22 (Graphic User Interface test) 
	 * 1) This method reads file with xPath for all web elements. 
	 * 2) Extract xPath and Test cases from file 
	 * 3) Check if WebElement is visible on the page 
	 * 4) Take screenshot of WebElement
	 * 5) Write report on the file
	 * 
	 * @throws Exception
	 */
	public static void webElementsUItest() throws Exception {
		// Load main page
		loadMainPage(false);

		// Read file for xpath
		// Get file from directory
		File xpathFileDirectory = new File(xpath);

		// Read file: xpathFileDirectory
		FileReader fileReader = new FileReader(xpathFileDirectory);

		// Buffer Read the fileReader
		BufferedReader bufferReader = new BufferedReader(fileReader);

		// String container
		String line;

		// Iterate over each line
		while ((line = bufferReader.readLine()) != null) {
			// Process the line

			// Split line with spaces
			String[] arrayLine = line.split(" ");
			// Check if WebElement is displayed
			if (webDriver.findElement(By.xpath(arrayLine[2])).isDisplayed()) {
				// Hold the file name
				String fileName = getTimeForFileName();
				// Take a screenshot of an WebElement and save to designated directory
				takeElementScreenSthot(webDriver, webDriver.findElement(By.xpath(arrayLine[2])),
						reportDest + arrayLine[1] + "/" + arrayLine[1] + "-" + fileName);

				// Archive report with passed status
				writeToFile(new String[] { arrayLine[1], "Passed", fileName });
				
			} else { // WebElement is not enabled
				// Archive report with failed status
				writeToFile(new String[] { arrayLine[1], "Failed", getTimeForFileName() });
			}

		}

		// Close BufferReader
		bufferReader.close();
	}

	/**
	 * Test case: TC2.1-2.14 (Functionality validation) This method check all the
	 * functionalities if the are valid (enabled)
	 * 
	 * @throws Exception
	 */
	public static void isWebElementsEnabled() throws Exception {
		// Open given URL on browser
		webDriver.get(baseUrl);

		// Wait for page to load
		webDriver.manage().timeouts().implicitlyWait(3, TimeUnit.SECONDS);

		// Read file for xpath
		// Get file from directory
		File xpathFileDirectory = new File(xpath);

		// Read file: xpathFileDirectory
		FileReader fileReader = new FileReader(xpathFileDirectory);

		// Buffer Read the fileReader
		BufferedReader bufferReader = new BufferedReader(fileReader);

		// String container
		String line;

		// Functional test case sequence number
		// Start from 2 (To-Do list is 1)
		int funSeqNum = 2;

		// Iterate over each line
		while ((line = bufferReader.readLine()) != null) {
			// Process the line
			// Split line with spaces
			String[] arrayLine = line.split(" ");

			if (arrayLine[1].equals("TC1.2") || arrayLine[1].equals("TC1.3")) { // Check for labels 1.2 and 1.3 and
																				// ignore them
				// Iterate over to the next
				// Test Case: TC1.5 Has bug. 13 or more elements, unmark all elements click
				// remove button
				continue;
			} else if (arrayLine[1].equals("TC1.5")) { // TODO: Test Case: TC2.2Bug found (in the list 13 or more,
				// unmark all, cl)
				// // Archive the report
				writeToFile(new String[] { "TC2." + funSeqNum, "Failed, Steps: 13 elements, unmark, click Remove",
				getTimeForFileName() });
				funSeqNum++;
			} else if (arrayLine[1].equals("TC1.4")) { // Check if it is a to-do list WebElement

				// Get list of TO DO list
				WebElement todoList = webDriver.findElement(By.xpath(getXpath(arrayLine[1])));
				List<WebElement> rows = todoList.findElements(By.tagName("li"));

				// Initiate status string
				String status = "";

				// Hold the file name
				String fileName = getTimeForFileName();

				// If list has rows
				if (rows.size() != 0) {
					// iterate over rows in the list
					for (WebElement rowWebElement : rows) {
						// If checkBox is enabled
						if (rowWebElement.findElement(By.tagName("input")).isEnabled()) {
							// Mark check box
							rowWebElement.findElement(By.tagName("input")).click();
							// Update status
							status = "Passed";

						} else {
							// If check box is not enabled, failure and break the while loop
							// Update status
							status = "Failed";
							break;
						}
					}

					// Take a screen shot of the list and save it to the directory
					takeFullScreenShot(webDriver, reportDest + "TC2.1/TC2.1-" + fileName);

					// Uncheck all the elements on the list not to impact in next functionalities
					// validation
					unCheckToDoList();
				} else {
					// Update status
					status = "List is empty";
				}

				// Archive the report
				writeToFile(new String[] { "TC2.1", status, fileName });
			} else {
				// All other WebElements
				WebElement webElement = webDriver.findElement(By.xpath(arrayLine[2]));

				// Initiate status string
				String status = "";

				// Hold the file name
				String fileName = getTimeForFileName();

				// Check if WebElement is enabled
				if (webElement.isEnabled()) {
					// Click on WebElement and hold (to take screen shot for proof)
					clickAndHoldWebElement(webElement);

					// Take a screenshot
					takeFullScreenShot(webDriver,
							reportDest + "TC2." + funSeqNum + "/TC2." + funSeqNum + "-" + fileName);

					// Release the click from WebElement
					releaseClickWebElement(webElement);

					// Update status
					status = "Passed";
					

				} else { // Test Case failed, WebElement is not enabled
					// Update status
					status = "Failed";
					
				}

				// // Archive the report
				writeToFile(new String[] { "TC2." + funSeqNum, status, fileName });

				// Increment file name
				funSeqNum++;
			}
		}

		// Close BufferReader
		bufferReader.close();
	}
	
	/**
	 * Test case: TC3.1-3.15
	 * This method executes test cases: 3.1 - 3.15
	 * @throws Exception
	 */
	public static void addButtonTest() throws Exception {
		// Test case 3.1
		addButtonTC3_1();

		// Test case 3.2
		addButtonTC3_2();

		loadMainPage(false);

		// Test Case 3.3
		addButtonTC3_3to3_5(webDriver.findElement(By.xpath(getXpath("TC1.12"))), "TC1.12", "1", "TC3.3");

		// Test Case 3.4
		addButtonTC3_3to3_5(webDriver.findElement(By.xpath(getXpath("TC1.13"))), "TC1.13", "Jan", "TC3.4");

		// Test Case 3.5
		addButtonTC3_3to3_5(webDriver.findElement(By.xpath(getXpath("TC1.14"))), "TC1.14", "2020", "TC3.5");

		// Test Case 3.6
		addButtonTC3_6();

		// Test Case 3.7
		addButtonTC3_7();

		// Test Case 3.8
		addButtonTC3_8();

		// Test Case 3.9
		addButtonTC3_9();

		// Test Case 3.10
		addButtonTC3_10();

		// Test Case 3.11
		addButtonTC3_11();

		// Test Case 3.12
		addButtonTC3_12();

		// Test Case 3.13
//		addButtonTC3_13();

		// Test Case 3.14
//		addButtonTC3_14();

		// Test Case 3.15
//		addButtonTC3_15();
	}

	/**
	 * Test case: TC3.1 Input text to add-text-field and click Add button
	 * @throws Exception
	 */
	public static void addButtonTC3_1() throws Exception {
		
		// Open given URL on browser
		loadMainPage(false);

		// Get all web elements related to Add functionality
		// Get add-text-field WebELement
		WebElement addTextFieldWebElement = webDriver.findElement(By.xpath(getXpath("TC1.9")));

		// Get Add button WebElement
		WebElement addButtonWebElement = webDriver.findElement(By.xpath(getXpath("TC1.10")));

		// Get list of TO DO list
		WebElement todoList = webDriver.findElement(By.xpath(getXpath("TC1.4")));
		List<WebElement> rows = todoList.findElements(By.tagName("li"));

		// report status
		String status = "";

		// Sum of the total number of elements on to-do list
		int oldRowSum = rows.size();

		// Input string test
		String inputText = getTimeForFileName();

		// Input a to-do task string
		inputTextWebElement(addTextFieldWebElement, inputText);

		// Click and hold Add button to take screen shot
		clickAndHoldWebElement(addButtonWebElement);

		// Hold the file name
		String fileName = getTimeForFileName();

		// Take screen shot of inputed text and clicking Add button
		takeFullScreenShot(webDriver, reportDest + "TC3.1/TC3.1" + "-" + fileName + "-1");

		// Release Add button
		releaseClickWebElement(addButtonWebElement);

		// Wait for page to load
		webDriver.manage().timeouts().implicitlyWait(3, TimeUnit.SECONDS);

		// Back button is available ? Duplicate found
		if (webDriver.findElement(By.xpath("/html/body")).getText()
				.equals("Sorry that TODO item already exists. Back")) {
			// Check for error message
			if (webDriver.findElement(By.xpath("/html/body")).getText()
					.equals("Sorry that TODO item already exists. Back")) { // Ecpected error
				// Take screen shot of inputed text and clicking Add button

				// Update status
				status = "Duplicate found";
				
			}
		} else { // Item added
			// Get updates from to-do list
			todoList = webDriver.findElement(By.xpath(getXpath("TC1.4")));
			rows = todoList.findElements(By.tagName("li"));

			if (oldRowSum + 1 != rows.size()) {
				status = "Failed";
			} else {
				// Iterate over elements to find added task
				for (WebElement webElement : rows) {
					// If row contains the added tast
					if (webElement.getText().contains(inputText)) { // Found it
						// Update status
						status = "Passed";
						
					} else { // Failure, no added task was found
						// Update status
						status = "Failed";
						
					}
				}
			}
		}

		// Take screen shot of inputed text and clicking Add button
		takeFullScreenShot(webDriver, reportDest + "TC3.1/TC3.1" + "-" + fileName + "-2");

		// Archive the report
		writeToFile(new String[] { "TC3.1", status, fileName });
	}

	/**
	 * Test case: TC3.2 Input text to add-text-field, add College category, and
	 * click Add button
	 * @throws Exception
	 */
	public static void addButtonTC3_2() throws Exception {
		
		// Open given URL on browser
		loadMainPage(false);

		// Get all web elements related to Add functionality
		// Get add-text-field WebELemen
		WebElement addTextFieldWebElement = webDriver.findElement(By.xpath(getXpath("TC1.9")));

		// Get Add button WebElement
		WebElement addButtonWebElement = webDriver.findElement(By.xpath(getXpath("TC1.10")));

		// Get category drop-down
		WebElement categoryDrowDownWebElement = webDriver.findElement(By.xpath(getXpath("TC1.11")));

		// Get list of TO DO list
		WebElement todoList = webDriver.findElement(By.xpath(getXpath("TC1.4")));
		List<WebElement> rows = todoList.findElements(By.tagName("li"));

		// report status
		String status = "";

		// Sum of the total number of elements on to-do list
		int oldRowSum = rows.size();

		// Input string test
		String inputText = getTimeForFileName();

		// Input a to-do task string
		inputTextWebElement(addTextFieldWebElement, inputText);

		// Choose College from category drop down
		selectFromDropDown(categoryDrowDownWebElement, getXpath("TC1.11"), "College");

		// Click and hold Add button to take screen shot
		clickAndHoldWebElement(addButtonWebElement);

		// Hold the file name
		String fileName = getTimeForFileName();

		// Take screen shot of inputed text and clicking Add button
		takeFullScreenShot(webDriver, reportDest + "TC3.2/TC3.2-" + fileName + "-1");

		// Release Add button
		releaseClickWebElement(addButtonWebElement);

		// Wait for page to load
		webDriver.manage().timeouts().implicitlyWait(3, TimeUnit.SECONDS);

		// Back button is available ? Duplicate found
		if (webDriver.findElement(By.xpath("/html/body")).getText()
				.equals("Sorry that TODO item already exists. Back")) {
			// Check for error message
			if (webDriver.findElement(By.xpath("/html/body")).getText()
					.equals("Sorry that TODO item already exists. Back")) { // Ecpected error
				// Take screen shot of inputed text and clicking Add button

				// Update status
				status = "Duplicate found";
			}
		} else { // Item added
			// Get updates from to-do list
			todoList = webDriver.findElement(By.xpath(getXpath("TC1.4")));
			rows = todoList.findElements(By.tagName("li"));

			if (oldRowSum + 1 != rows.size()) {
				status = "Failed";
			} else {
				// Iterate over elements to find added task
				for (WebElement webElement : rows) {
					// If row contains the added tast
					if (webElement.getText().contains(inputText)) { // Found it
						// Get the span
						WebElement textWebElement = webElement.findElement(By.tagName("span"));

						// Check if color is green (College)
						if (rgbaToHex(textWebElement.getCssValue("color")).toUpperCase().equals("#00FF00")) { // Matches
																												// green
																												// color
							// Update status
							status = "Passed";
						} else { // Doesn't match green color
							// Update status
							status = "Failed";
						}
					} else { // Failure, no added task was found
						// Update status
						status = "Failed";
					}
				}
			}
		}

		// Take screen shot of inputed text and clicking Add button
		takeFullScreenShot(webDriver, reportDest + "TC3.2/TC3.2-" + fileName + "-2");

		// Archive the report
		writeToFile(new String[] { "TC3.2", status, fileName });
	}

	/**
	 * This method completes test cases: TC3.3-TC3.5 Input text into add-text-field,
	 * chooses from drop down menu (day or month or year), clicks add, checks for
	 * result
	 * 
	 * @param dropDownWebElement - (day or month or year)
	 * @param xPath              - xpath to get it from file source
	 * @param optionSelect       - value to be selected from drop down menu
	 * @param TC                 - Test case number
	 * @throws Exception
	 */
	public static void addButtonTC3_3to3_5(WebElement dropDownWebElement, String xPath, String optionSelect, String TC)throws Exception {
		
		// Get all web elements related to Add functionality
		// Get add-text-field WebELemen
		WebElement addTextFieldWebElement = webDriver.findElement(By.xpath(getXpath("TC1.9")));

		// Get Add button WebElement
		WebElement addButtonWebElement = webDriver.findElement(By.xpath(getXpath("TC1.10")));

		// Get list of TO DO list
		WebElement todoList = webDriver.findElement(By.xpath(getXpath("TC1.4")));
		List<WebElement> rows = todoList.findElements(By.tagName("li"));

		// report status
		String status = "";

		// Sum of the total number of elements on to-do list
		int oldRowSum = rows.size();

		// Input string test
		String inputText = getTimeForFileName();

		// Input a to-do task string
		inputTextWebElement(addTextFieldWebElement, inputText);

		// Choose College from category drop down
		selectFromDropDown(dropDownWebElement, getXpath(xPath), optionSelect);

		// Click and hold Add button to take screen shot
		clickAndHoldWebElement(addButtonWebElement);

		// Hold the file name
		String fileName = getTimeForFileName();

		// Take screen shot of inputed text and clicking Add button
		takeFullScreenShot(webDriver, reportDest + TC + "/" + TC + "-" + fileName + "-1");

		// Release Add button
		releaseClickWebElement(addButtonWebElement);

		// Wait for page to load
		webDriver.manage().timeouts().implicitlyWait(3, TimeUnit.SECONDS);

		// Back button is available ? Duplicate found
		if (webDriver.findElement(By.xpath("/html/body")).getText()
				.equals("Sorry that TODO item already exists. Back")) {
			// Check for error message
			if (webDriver.findElement(By.xpath("/html/body")).getText()
					.equals("Sorry that TODO item already exists. Back")) { // Expected error
				// Take screen shot of inputed text and clicking Add button

				// Update status
				status = "Duplicate found";
				
			}
		} else { // Item added
			// Get updates from to-do list
			todoList = webDriver.findElement(By.xpath(getXpath("TC1.4")));
			rows = todoList.findElements(By.tagName("li"));

			if (oldRowSum + 1 != rows.size()) {
				status = "Failed";
			} else {
				// Iterate over elements to find added task
				for (WebElement webElement : rows) {
					// If row contains the added tast
					if (webElement.getText().contains(inputText)) { // Found it
						// Check if element ends with (None)
						if (webElement.getText().endsWith("(None)")) { // Found it
							// Update status
							status = "Passed";
							
						} else { // Doesn't match
							// Update status
							status = "Failed";
							
						}
					} else { // Failure, no added task was found
						// Update status
						status = "Failed";
						
					}
				}
			}
		}

		// Take screen shot of inputed text and clicking Add button
		takeFullScreenShot(webDriver, reportDest + TC + "/" + TC + "-" + fileName + "-2");

		// Archive the report
		writeToFile(new String[] { TC, status, fileName });
	}

	/**
	 * Test case: TC3.6 Input text into add-text-field, chooses category and due
	 * date from drop down menu, clicks add, checks for result
	 * 
	 * @throws Exception
	 */
	public static void addButtonTC3_6() throws Exception {
		
		// Load the main page
		loadMainPage(false);

		// Get all web elements related to Add functionality
		// Get add-text-field WebELemen
		WebElement addTextFieldWebElement = webDriver.findElement(By.xpath(getXpath("TC1.9")));

		// Get Add button WebElement
		WebElement addButtonWebElement = webDriver.findElement(By.xpath(getXpath("TC1.10")));

		// Get list of TO DO list
		WebElement todoList = webDriver.findElement(By.xpath(getXpath("TC1.4")));
		List<WebElement> rows = todoList.findElements(By.tagName("li"));

		// Get category drop-down
		WebElement categoryDrowDownWebElement = webDriver.findElement(By.xpath(getXpath("TC1.11")));

		// Get due date drop-down
		WebElement dueDateDrowDownWebElement = webDriver.findElement(By.xpath(getXpath("TC1.12")));

		// report status
		String status = "";

		// Sum of the total number of elements on to-do list
		int oldRowSum = rows.size();

		// Input string test
		String inputText = getTimeForFileName();

		// Input a to-do task string
		inputTextWebElement(addTextFieldWebElement, inputText);

		// Choose College from category drop down
		selectFromDropDown(categoryDrowDownWebElement, getXpath("TC1.11"), "College");

		// Choose 1 from due date drop down
		selectFromDropDown(dueDateDrowDownWebElement, getXpath("TC1.12"), "1");

		// Click and hold Add button to take screen shot
		clickAndHoldWebElement(addButtonWebElement);

		// Hold the file name
		String fileName = getTimeForFileName();

		// Take screen shot of inputed text and clicking Add button
		takeFullScreenShot(webDriver, reportDest + "TC3.6/TC3.6" + "-" + fileName + "-1");

		// Release Add button
		releaseClickWebElement(addButtonWebElement);

		// Wait for page to load
		webDriver.manage().timeouts().implicitlyWait(3, TimeUnit.SECONDS);

		// Back button is available ? Duplicate found
		if (webDriver.findElement(By.xpath("/html/body")).getText()
				.equals("Sorry that TODO item already exists. Back")) {
			// Check for error message
			if (webDriver.findElement(By.xpath("/html/body")).getText()
					.equals("Sorry that TODO item already exists. Back")) { // Ecpected error
				// Take screen shot of inputed text and clicking Add button

				// Update status
				status = "Duplicate found";
				
			}
		} else { // Item added
			// Get updates from to-do list
			todoList = webDriver.findElement(By.xpath(getXpath("TC1.4")));
			rows = todoList.findElements(By.tagName("li"));

			if (oldRowSum + 1 != rows.size()) {
				status = "Failed";
				
			} else {
				// Iterate over elements to find added task
				for (WebElement webElement : rows) {
					// If row contains the added tast
					if (webElement.getText().contains(inputText)) { // Found it
						// Get the span
						WebElement textWebElement = webElement.findElement(By.tagName("span"));

						// Check if color is green (College)
						if (rgbaToHex(textWebElement.getCssValue("color")).toUpperCase().equals("#00FF00")) { // Matches
																												// green
																												// color
							// Date still incomplete. Should end with (None)
							if (webElement.getText().endsWith("(None)")) {
								// Update status
								status = "Passed";
								
							} else { // Does not end with (None)
								// Update status
								status = "Failed";
								
							}

						} else { // Doesn't match green color
							// Update status
							status = "Failed";
							
						}
					} else { // Failure, no added task was found
						// Update status
						status = "Failed";
						
					}
				}
			}
		}

		// Take screen shot of inputed text and clicking Add button
		takeFullScreenShot(webDriver, reportDest + "TC3.6/TC3.6" + "-" + fileName + "-2");

		// Archive the report
		writeToFile(new String[] { "TC3.6", status, fileName });
	}

	/**
	 * Test case: TC3.7 Input text into add-text-field, chooses category, due date
	 * and due month from drop down menu, clicks add, checks for result
	 * 
	 * @throws Exception
	 */
	public static void addButtonTC3_7() throws Exception {
		
		// Load the main page
		loadMainPage(false);

		// Get all web elements related to Add functionality
		// Get add-text-field WebELemen
		WebElement addTextFieldWebElement = webDriver.findElement(By.xpath(getXpath("TC1.9")));

		// Get Add button WebElement
		WebElement addButtonWebElement = webDriver.findElement(By.xpath(getXpath("TC1.10")));

		// Get list of TO DO list
		WebElement todoList = webDriver.findElement(By.xpath(getXpath("TC1.4")));
		List<WebElement> rows = todoList.findElements(By.tagName("li"));

		// Get category drop-down
		WebElement categoryDrowDownWebElement = webDriver.findElement(By.xpath(getXpath("TC1.11")));

		// Get due date drop-down
		WebElement dueDateDrowDownWebElement = webDriver.findElement(By.xpath(getXpath("TC1.12")));

		// Get due month drop-down
		WebElement dueMonthDrowDownWebElement = webDriver.findElement(By.xpath(getXpath("TC1.13")));

		// report status
		String status = "";

		// Sum of the total number of elements on to-do list
		int oldRowSum = rows.size();

		// Input string test
		String inputText = getTimeForFileName();

		// Input a to-do task string
		inputTextWebElement(addTextFieldWebElement, inputText);

		// Choose College from category drop down
		selectFromDropDown(categoryDrowDownWebElement, getXpath("TC1.11"), "College");

		// Choose 1 from due date drop down
		selectFromDropDown(dueDateDrowDownWebElement, getXpath("TC1.12"), "1");

		// Choose Jan from due month drop down
		selectFromDropDown(dueMonthDrowDownWebElement, getXpath("TC1.13"), "Jan");

		// Click and hold Add button to take screen shot
		clickAndHoldWebElement(addButtonWebElement);

		// Hold the file name
		String fileName = getTimeForFileName();

		// Take screen shot of inputed text and clicking Add button
		takeFullScreenShot(webDriver, reportDest + "TC3.7/TC3.7" + "-" + fileName + "-1");

		// Release Add button
		releaseClickWebElement(addButtonWebElement);

		// Wait for page to load
		webDriver.manage().timeouts().implicitlyWait(3, TimeUnit.SECONDS);

		// Back button is available ? Duplicate found
		if (webDriver.findElement(By.xpath("/html/body")).getText()
				.equals("Sorry that TODO item already exists. Back")) {
			// Check for error message
			if (webDriver.findElement(By.xpath("/html/body")).getText()
					.equals("Sorry that TODO item already exists. Back")) { // Ecpected error
				// Take screen shot of inputed text and clicking Add button

				// Update status
				status = "Duplicate found";
				
			}
		} else { // Item added
			// Get updates from to-do list
			todoList = webDriver.findElement(By.xpath(getXpath("TC1.4")));
			rows = todoList.findElements(By.tagName("li"));

			if (oldRowSum + 1 != rows.size()) {
				status = "Failed";
				
			} else {
				// Iterate over elements to find added task
				for (WebElement webElement : rows) {
					// If row contains the added tast
					if (webElement.getText().contains(inputText)) { // Found it
						// Get the spans
						List<WebElement> textWebElement = webElement.findElements(By.tagName("span"));
						// Iterate over spans to find College
						for (WebElement span : textWebElement) {
							// Check College span with color green
							if (span.getText().equals("College")) { // Found College
								// Check if color is green (College)
								if (rgbaToHex(span.getCssValue("color")).toUpperCase().equals("#00FF00")) { // Matches
																											// green
																											// color
									// Category should be (College)
									if (webElement.getText().endsWith("(College)")) {
										// Date should be complete, fails if has (None)
										if (webElement.getText().contains("None")) {
											// Update status
											status = "Failed";
											
										} else { // Date is complete
											// Update status
											status = "Passed";
											
										}

									} else { // Ends with (None)
										// Update status
										status = "Failed";
										
									}

								} else { // Doesn't match green color
									// Update status
									status = "Failed";
									
								}
								// Break the loop
								break;
							} else { // Did not found College, failure
								// Update status
								status = "Failed";
								
							}

							// Check for due date and color
							if (span.getText().contains(inputText)) {
								if (!checkDate(span.getText())) {
									// Past due
									// Color should be red, check
									if (rgbaToHex(span.getCssValue("color")).toUpperCase().equals("#FF0000")) { // Color
																												// is
																												// red
										// Update status
										status = "Passed";
										
									} else if (rgbaToHex(span.getCssValue("color")).toUpperCase().equals("#00FF00")) { // Color
																														// is
																														// green
																														// failed
										// Update status
										status = "Failed";
										
									} else {
										// Any other color, fail
										// Update status
										status = "Failed";
										
									}
								} else {
									// Not past due
									// Color should be green (College)
									// Past due
									// Color should be green, check
									if (rgbaToHex(span.getCssValue("color")).toUpperCase().equals("#FF0000")) { // Color
																												// is
																												// red
										// Update status
										status = "Failed";
										
									} else if (rgbaToHex(span.getCssValue("color")).toUpperCase().equals("#00FF00")) { // Color
																														// is
																														// green
																														// passed
										// Update status
										status = "passed";
										
									} else {
										// Any other color, fail
										// Update status
										status = "Failed";
										
									}
								}
							} else { // Failed, element is not on the list
								// Update status
								status = "Failed";
								
							}

						}
						// break the loop
						break;
					} else { // Failure, no added task was found
						// Update status
						status = "Failed";
						
					}
				}
			}
		}

		// Take screen shot of inputed text and clicking Add button
		takeFullScreenShot(webDriver, reportDest + "TC3.7/TC3.7" + "-" + fileName + "-2");

		// Archive the report
		writeToFile(new String[] { "TC3.7", status, fileName });
	}

	/**
	 * Test case: TC3.8 Input text into add-text-field, chooses category, due date,
	 * due month and due year from drop down menu, clicks add, checks for result
	 * 
	 * @throws Exception
	 */
	public static void addButtonTC3_8() throws Exception {
		
		// Load the main page
		loadMainPage(false);

		// Get all web elements related to Add functionality
		// Get add-text-field WebELemen
		WebElement addTextFieldWebElement = webDriver.findElement(By.xpath(getXpath("TC1.9")));

		// Get Add button WebElement
		WebElement addButtonWebElement = webDriver.findElement(By.xpath(getXpath("TC1.10")));

		// Get list of TO DO list
		WebElement todoList = webDriver.findElement(By.xpath(getXpath("TC1.4")));
		List<WebElement> rows = todoList.findElements(By.tagName("li"));

		// Get category drop-down
		WebElement categoryDrowDownWebElement = webDriver.findElement(By.xpath(getXpath("TC1.11")));

		// Get due date drop-down
		WebElement dueDateDrowDownWebElement = webDriver.findElement(By.xpath(getXpath("TC1.12")));

		// Get due month drop-down
		WebElement dueMonthDrowDownWebElement = webDriver.findElement(By.xpath(getXpath("TC1.13")));

		// Get due year drop-down
		WebElement dueYearDrowDownWebElement = webDriver.findElement(By.xpath(getXpath("TC1.14")));

		// report status
		String status = "";

		// Sum of the total number of elements on to-do list
		int oldRowSum = rows.size();

		// Input string test
		String inputText = getTimeForFileName();

		// Input a to-do task string
		inputTextWebElement(addTextFieldWebElement, inputText);

		// Choose College from category drop down
		selectFromDropDown(categoryDrowDownWebElement, getXpath("TC1.11"), "College");

		// Choose 1 from due date drop down
		selectFromDropDown(dueDateDrowDownWebElement, getXpath("TC1.12"), "1");

		// Choose Jan from due month drop down
		selectFromDropDown(dueMonthDrowDownWebElement, getXpath("TC1.13"), "Jan");

		// Choose 2020 from due year drop down
		selectFromDropDown(dueYearDrowDownWebElement, getXpath("TC1.14"), "2020");

		// Click and hold Add button to take screen shot
		clickAndHoldWebElement(addButtonWebElement);

		// Hold the file name
		String fileName = getTimeForFileName();

		// Take screen shot of inputed text and clicking Add button
		takeFullScreenShot(webDriver, reportDest + "TC3.8/TC3.8" + "-" + fileName + "-1");

		// Release Add button
		releaseClickWebElement(addButtonWebElement);

		// Wait for page to load
		webDriver.manage().timeouts().implicitlyWait(3, TimeUnit.SECONDS);

		// Back button is available ? Duplicate found
		if (webDriver.findElement(By.xpath("/html/body")).getText()
				.equals("Sorry that TODO item already exists. Back")) {
			// Check for error message
			if (webDriver.findElement(By.xpath("/html/body")).getText()
					.equals("Sorry that TODO item already exists. Back")) { // Ecpected error
				// Take screen shot of inputed text and clicking Add button

				// Update status
				status = "Duplicate found";
				
			}
		} else { // Item added
			// Get updates from to-do list
			todoList = webDriver.findElement(By.xpath(getXpath("TC1.4")));
			rows = todoList.findElements(By.tagName("li"));

			if (oldRowSum + 1 != rows.size()) {
				status = "Failed";
				
			} else {
				// Iterate over elements to find added task
				for (WebElement webElement : rows) {
					// If row contains the added tast
					if (webElement.getText().contains(inputText)) { // Found it
						// Get the spans
						List<WebElement> textWebElement = webElement.findElements(By.tagName("span"));
						// Iterate over spans to find College
						for (WebElement span : textWebElement) {
							if (span.getText().equals("College")) { // Found College
								// Check if color is green (College)
								if (rgbaToHex(span.getCssValue("color")).toUpperCase().equals("#00FF00")) { // Matches
																											// green
																											// color
									// Category should be (College)
									if (webElement.getText().endsWith("(College)")) {
										// Date should be complete, fails if has (None)
										if (webElement.getText().contains("None")) {
											// Update status
											status = "Failed";
											
										} else { // Date is complete
											// Update status
											status = "Passed";
											
										}

									} else { // Ends with (None)
										// Update status
										status = "Failed";
										
									}

								} else { // Doesn't match green color
									// Update status
									status = "Failed";
									
								}
								// Break the loop
								break;
							} else { // Did not found College, failure
								// Update status
								status = "Failed";
								
							}

							// Check for due date and color
							if (span.getText().contains(inputText)) {
								if (!checkDate(webElement.getText())) {
									// Not past due
									// Color should be green, check (College)
									if (rgbaToHex(span.getCssValue("color")).toUpperCase().equals("#FF0000")) { // Color
																												// is
																												// red

										// Update status
										status = "Failed";
										
									} else if (rgbaToHex(span.getCssValue("color")).toUpperCase().equals("#00FF00")) { // Color
																														// is
																														// green
																														// passed
										// Update status
										status = "Passed";
										
									} else {
										// Any other color, fail
										// Update status
										status = "Failed";
										
									}
								} else {
									// past due
									// Color should be red
									if (rgbaToHex(span.getCssValue("color")).toUpperCase().equals("#FF0000")) { // Color
																												// is
																												// red
										// Update status
										status = "Passed";
										
									} else if (rgbaToHex(span.getCssValue("color")).toUpperCase().equals("#00FF00")) { // Color
																														// is
																														// green
										// Update status
										status = "Failed";
										
									} else {
										// Any other color, fail
										// Update status
										status = "Failed";
										
									}
								}
							} else { // Failed, element is not on the list
								// Update status
								status = "Failed";
								
							}
						}
						// break the loop
						break;
					} else { // Failure, no added task was found
						// Update status
						status = "Failed";
						
					}
				}
			}
		}

		// Take screen shot of inputed text and clicking Add button
		takeFullScreenShot(webDriver, reportDest + "TC3.8/TC3.8" + "-" + fileName + "-2");

		// Archive the report
		writeToFile(new String[] { "TC3.8", status, fileName });
	}
	 
	/**
	 * Test case: TC3.9 Input text into add-text-field, chooses due date and due
	 * month from drop down menu, clicks add, checks for result
	 * 
	 * @throws Exception
	 */
	public static void addButtonTC3_9() throws Exception {
		
		// Load the main page
		loadMainPage(false);

		// Get all web elements related to Add functionality
		// Get add-text-field WebELemen
		WebElement addTextFieldWebElement = webDriver.findElement(By.xpath(getXpath("TC1.9")));

		// Get Add button WebElement
		WebElement addButtonWebElement = webDriver.findElement(By.xpath(getXpath("TC1.10")));

		// Get list of TO DO list
		WebElement todoList = webDriver.findElement(By.xpath(getXpath("TC1.4")));
		List<WebElement> rows = todoList.findElements(By.tagName("li"));

		// Get due date drop-down
		WebElement dueDateDrowDownWebElement = webDriver.findElement(By.xpath(getXpath("TC1.12")));

		// Get due month drop-down
		WebElement dueMonthDrowDownWebElement = webDriver.findElement(By.xpath(getXpath("TC1.13")));

		// report status
		String status = "";

		// Sum of the total number of elements on to-do list
		int oldRowSum = rows.size();

		// Input string test
		String inputText = getTimeForFileName();

		// Input a to-do task string
		inputTextWebElement(addTextFieldWebElement, inputText);

		// Choose College from category drop down

		// Choose 1 from due date drop down
		selectFromDropDown(dueDateDrowDownWebElement, getXpath("TC1.12"), "1");

		selectFromDropDown(dueMonthDrowDownWebElement, getXpath("TC1.13"), "Jan");

		// Click and hold Add button to take screen shot
		clickAndHoldWebElement(addButtonWebElement);

		// Hold the file name
		String fileName = getTimeForFileName();

		// Take screen shot of inputed text and clicking Add button
		takeFullScreenShot(webDriver, reportDest + "TC3.9/TC3.9" + "-" + fileName + "-1");

		// Release Add button
		releaseClickWebElement(addButtonWebElement);

		// Wait for page to load
		webDriver.manage().timeouts().implicitlyWait(3, TimeUnit.SECONDS);

		// Back button is available ? Duplicate found
		if (webDriver.findElement(By.xpath("/html/body")).getText()
				.equals("Sorry that TODO item already exists. Back")) {
			// Check for error message
			if (webDriver.findElement(By.xpath("/html/body")).getText()
					.equals("Sorry that TODO item already exists. Back")) { // Expected error
				// Update status
				status = "Duplicate found";
				
			}
		} else { // Item added
			// Get updates from to-do list
			todoList = webDriver.findElement(By.xpath(getXpath("TC1.4")));
			rows = todoList.findElements(By.tagName("li"));

			if (oldRowSum + 1 != rows.size()) {
				status = "Failed";
				
			} else {
				// Iterate over elements to find added task
				for (WebElement webElement : rows) {
					// If row contains the added task
					if (webElement.getText().contains(inputText)) { // Found it
						// Get the text
						if (webElement.getText().endsWith("(None)")) {// Should end with date
							// Update status
							status = "Failed";
							
							break;
						} else {// Fail has a date
							// Update status
							status = "Passed";
							
							break;
						}
					} else { // Failure, no added task was found
						// Update status
						status = "Failed";
						
					}
				}
			}
		}

		// Take screen shot of inputed text and clicking Add button
		takeFullScreenShot(webDriver, reportDest + "TC3.9/TC3.9" + "-" + fileName + "-2");

		// Archive the report
		writeToFile(new String[] { "TC3.9", status, fileName });
	}

	/**
	 * Test case: TC3.10 Input text into add-text-field, chooses due day and due
	 * year from drop down menu, clicks add, checks for result
	 * 
	 * @throws Exception
	 */
	public static void addButtonTC3_10() throws Exception {
		
		// Load the main page
		loadMainPage(false);

		// Get all web elements related to Add functionality
		// Get add-text-field WebELemen
		WebElement addTextFieldWebElement = webDriver.findElement(By.xpath(getXpath("TC1.9")));

		// Get Add button WebElement
		WebElement addButtonWebElement = webDriver.findElement(By.xpath(getXpath("TC1.10")));

		// Get list of TO DO list
		WebElement todoList = webDriver.findElement(By.xpath(getXpath("TC1.4")));
		List<WebElement> rows = todoList.findElements(By.tagName("li"));

		// Get due year drop-down
		WebElement dueYearDrowDownWebElement = webDriver.findElement(By.xpath(getXpath("TC1.14")));

		// report status
		String status = "";

		// Sum of the total number of elements on to-do list
		int oldRowSum = rows.size();

		// Input string test
		String inputText = getTimeForFileName();

		// Input a to-do task string
		inputTextWebElement(addTextFieldWebElement, inputText);

		// Get due date drop-down
		WebElement dueDateDrowDownWebElement = webDriver.findElement(By.xpath(getXpath("TC1.12")));

		// Choose 1 from due date drop down
		selectFromDropDown(dueDateDrowDownWebElement, getXpath("TC1.12"), "1");

		// Choose 1 from due date drop down
		selectFromDropDown(dueYearDrowDownWebElement, getXpath("TC1.14"), "2020");

		// Click and hold Add button to take screen shot
		clickAndHoldWebElement(addButtonWebElement);

		// Hold the file name
		String fileName = getTimeForFileName();

		// Take screen shot of inputed text and clicking Add button
		takeFullScreenShot(webDriver, reportDest + "TC3.10/TC3.10" + "-" + fileName + "-1");

		// Release Add button
		releaseClickWebElement(addButtonWebElement);

		// Wait for page to load
		webDriver.manage().timeouts().implicitlyWait(3, TimeUnit.SECONDS);

		// Back button is available ? Duplicate found
		if (webDriver.findElement(By.xpath("/html/body")).getText()
				.equals("Sorry that TODO item already exists. Back")) {
			// Check for error message
			if (webDriver.findElement(By.xpath("/html/body")).getText()
					.equals("Sorry that TODO item already exists. Back")) { // Expected error
				// Update status
				status = "Duplicate found";
			}
		} else { // Item added
			// Get updates from to-do list
			todoList = webDriver.findElement(By.xpath(getXpath("TC1.4")));
			rows = todoList.findElements(By.tagName("li"));

			if (oldRowSum + 1 != rows.size()) {
				status = "Failed";
				
			} else {
				// Iterate over elements to find added task
				for (WebElement webElement : rows) {
					// If row contains the added task
					if (webElement.getText().contains(inputText)) { // Found it
						// Get the text
						if (webElement.getText().endsWith("(None)")) {// Should end with None (no DUe date)
							// Update status
							status = "Passed";
							
						} else {// Fail has a date
							// Update status
							status = "Fail";
							
						}
					} else { // Failure, no added task was found
						// Update status
						status = "Failed";
						
					}
				}
			}
		}

		// Take screen shot of inputed text and clicking Add button
		takeFullScreenShot(webDriver, reportDest + "TC3.10/TC3.10" + "-" + fileName + "-2");

		// Archive the report
		writeToFile(new String[] { "TC3.10", status, fileName });
	}

	/**
	 * Test case: TC3.11 Input text into add-text-field, chooses due month and due
	 * year from drop down menu, clicks add, checks for result
	 * 
	 * @throws Exception
	 */
	public static void addButtonTC3_11() throws Exception {
		
		// Load the main page
		loadMainPage(false);

		// Get all web elements related to Add functionality
		// Get add-text-field WebELemen
		WebElement addTextFieldWebElement = webDriver.findElement(By.xpath(getXpath("TC1.9")));

		// Get Add button WebElement
		WebElement addButtonWebElement = webDriver.findElement(By.xpath(getXpath("TC1.10")));

		// Get list of TO DO list
		WebElement todoList = webDriver.findElement(By.xpath(getXpath("TC1.4")));
		List<WebElement> rows = todoList.findElements(By.tagName("li"));

		// Get due month drop-down
		WebElement dueMonthDrowDownWebElement = webDriver.findElement(By.xpath(getXpath("TC1.13")));

		// Get due year drop-down
		WebElement dueYearDrowDownWebElement = webDriver.findElement(By.xpath(getXpath("TC1.14")));

		// report status
		String status = "";

		// Sum of the total number of elements on to-do list
		int oldRowSum = rows.size();

		// Input string test
		String inputText = getTimeForFileName();

		// Input a to-do task string
		inputTextWebElement(addTextFieldWebElement, inputText);

		// Choose 1 from due date drop down
		selectFromDropDown(dueMonthDrowDownWebElement, getXpath("TC1.13"), "Jan");

		// Choose 1 from due date drop down
		selectFromDropDown(dueYearDrowDownWebElement, getXpath("TC1.14"), "2020");

		// Click and hold Add button to take screen shot
		clickAndHoldWebElement(addButtonWebElement);

		// Hold the file name
		String fileName = getTimeForFileName();

		// Take screen shot of inputed text and clicking Add button
		takeFullScreenShot(webDriver, reportDest + "TC3.11/TC3.11" + "-" + fileName + "-1");

		// Release Add button
		releaseClickWebElement(addButtonWebElement);

		// Wait for page to load
		webDriver.manage().timeouts().implicitlyWait(3, TimeUnit.SECONDS);

		// Back button is available ? Duplicate found
		if (webDriver.findElement(By.xpath("/html/body")).getText()
				.equals("Sorry that TODO item already exists. Back")) {
			// Check for error message
			if (webDriver.findElement(By.xpath("/html/body")).getText()
					.equals("Sorry that TODO item already exists. Back")) { // Expected error
				// Update status
				status = "Duplicate found";
				
			}
		} else { // Item added
			// Get updates from to-do list
			todoList = webDriver.findElement(By.xpath(getXpath("TC1.4")));
			rows = todoList.findElements(By.tagName("li"));

			if (oldRowSum + 1 != rows.size()) {
				status = "Failed";
				
			} else {
				// Iterate over elements to find added task
				for (WebElement webElement : rows) {
					// If row contains the added task
					if (webElement.getText().contains(inputText)) { // Found it
						// Get the text
						if (webElement.getText().endsWith("(None)")) {// Should end with None (no DUe date)
							// Update status
							status = "Passed";
							
						} else {// Fail has a date
							// Update status
							status = "Fail";
							
						}
					} else { // Failure, no added task was found
						// Update status
						status = "Failed";
						
					}
				}
			}
		}

		// Take screen shot of inputed text and clicking Add button
		takeFullScreenShot(webDriver, reportDest + "TC3.11/TC3.11" + "-" + fileName + "-2");

		// Archive the report
		writeToFile(new String[] { "TC3.11", status, fileName });
	}

	/**
	 * Test case: TC3.12 negative testing clicks add, checks for result
	 * 
	 * @throws Exception
	 */
	public static void addButtonTC3_12() throws Exception {
		
		// Load the main page
		loadMainPage(false);

		// Get all web elements related to Add functionality

		// Get Add button WebElement
		WebElement addButtonWebElement = webDriver.findElement(By.xpath(getXpath("TC1.10")));

		// Get list of TO DO list
		WebElement todoList = webDriver.findElement(By.xpath(getXpath("TC1.4")));
		List<WebElement> rows = todoList.findElements(By.tagName("li"));

		// report status
		String status = "";

		// Sum of the total number of elements on to-do list
		int oldRowSum = rows.size();

		// Click and hold Add button to take screen shot
		clickAndHoldWebElement(addButtonWebElement);

		// Hold the file name
		String fileName = getTimeForFileName();

		// Take screen shot of inputed text and clicking Add button
		takeFullScreenShot(webDriver, reportDest + "TC3.12/TC3.12" + "-" + fileName + "-1");

		// Release Add button
		releaseClickWebElement(addButtonWebElement);

		// Wait for page to load
		webDriver.manage().timeouts().implicitlyWait(3, TimeUnit.SECONDS);

		// Get updates from to-do list
		todoList = webDriver.findElement(By.xpath(getXpath("TC1.4")));
		rows = todoList.findElements(By.tagName("li"));

		// If size of the list is not the same, failed
		if (oldRowSum != rows.size()) {
			// Update status
			status = "Failed";
			
		} else {
			status = "Passed";
			
		}

		// Take screen shot of inputed text and clicking Add button
		takeFullScreenShot(webDriver, reportDest + "TC3.12/TC3.12" + "-" + fileName + "-2");

		// Archive the report
		writeToFile(new String[] { "TC3.12", status, fileName });
	}

	// Customizing Categories tests.
	/**
	 * Test Case: TC4.1 - TC4.3
	 * This method executes TEST cases 4.1-4.3
	 * @throws Exception
	 */
	public static void addCategoryTests() throws Exception {
		addCategoryTC4_1();
		addCategoryTC4_2();
		addCategoryTC4_3();
	}

	/**
	 * This method inputs text to category-text-field and clicks Add Category button
	 * @throws Exception
	 */
	public static void addCategoryTC4_1() throws Exception {
		
		loadMainPage(false);

		// Get all web elements related to Add Category functionality
		WebElement controlsWebElement = webDriver.findElement(By.xpath("/html/body/div[3]"));
		List<WebElement> categoryList = controlsWebElement.findElements(By.tagName("span"));

		// Get text-field for Add Category
		WebElement addCategoryTextField = webDriver.findElement(By.xpath(getXpath("TC1.15")));

		// Get button for Add Category
		WebElement addCategoryButton = webDriver.findElement(By.xpath(getXpath("TC1.16")));

		// Size of categories before adding
		int oldCategorySize = categoryList.size();

		// Reserve file name
		String fileName = getTimeForFileName();

		// Status of TC
		String status = "";

		// Input text to Add Category- text-field
		inputTextWebElement(addCategoryTextField, fileName);

		// Click on Add Category Button
		clickAndHoldWebElement(addCategoryButton);

		// Take screen shot of inputed text and clicking Add Category button
		takeFullScreenShot(webDriver, reportDest + "TC4.1/TC4.1" + "-" + fileName + "-1");

		// release Add category button
		releaseClickWebElement(addCategoryButton);

		// Wait for page to load
		webDriver.manage().timeouts().implicitlyWait(3, TimeUnit.SECONDS);

		if (webDriver.findElement(By.xpath("/html/body")).getText()
				.contains("The category you want to add already exists")) {
			status = "Category already exist";
		} else {
			controlsWebElement = webDriver.findElement(By.xpath("/html/body/div[3]"));
			categoryList = controlsWebElement.findElements(By.tagName("span"));

			// Check for size increase
			if (oldCategorySize < categoryList.size()) { // Success
				// Iterate over category list to find just added category
				for (int i = 1; i < categoryList.size(); i++) { // Start from index 1 (Ignore Overdue)
					// Get Category name
					WebElement span = categoryList.get(i);
					// Check if it is the same as added category name
					if (span.getText().equals(fileName)) { // Passed
						status = "Passed";
						
						// Break the loop
						break;
					} else { // Failed, can't find it
						status = "Failed";
						
					}
				}
			} else { // fail
				status = "Failed";
				
			}
		}

		// Take screen shot of added category and clicking Add button
		takeFullScreenShot(webDriver, reportDest + "TC4.1/TC4.1" + "-" + fileName + "-2");

		// Archive the report
		writeToFile(new String[] { "TC4.1", status, fileName });
	}

	/**
	 * This method inputs text to category-text-field, chooses green color, and
	 * clicks Add Category button
	 * @throws Exception
	 */
	public static void addCategoryTC4_2() throws Exception {
		
		loadMainPage(false);

		// Get all web elements related to Add Category functionality
		WebElement controlsWebElement = webDriver.findElement(By.xpath("/html/body/div[3]"));
		List<WebElement> categoryList = controlsWebElement.findElements(By.tagName("span"));

		// Get text-field for Add Category
		WebElement addCategoryTextField = webDriver.findElement(By.xpath(getXpath("TC1.15")));

		// Get button for Add Category
		WebElement addCategoryButton = webDriver.findElement(By.xpath(getXpath("TC1.16")));

		// get category color drop-down menu
		WebElement categoryColorDropDown = webDriver.findElement(By.xpath(getXpath("TC1.17")));

		// Size of categories before adding
		int oldCategorySize = categoryList.size();

		// Reserve file name
		String fileName = getTimeForFileName();

		// Status of TC
		String status = "";

		// Input text to Add Category- text-field
		inputTextWebElement(addCategoryTextField, fileName);

		// select green color
		selectFromDropDown(categoryColorDropDown, getXpath("TC1.17"), "Blue");

		// Click on Add Category Button
		clickAndHoldWebElement(addCategoryButton);

		// Take screen shot of inputed text and clicking Add Category button
		takeFullScreenShot(webDriver, reportDest + "TC4.2/TC4.2" + "-" + fileName + "-1");

		// release Add category button
		releaseClickWebElement(addCategoryButton);

		// Wait for page to load
		webDriver.manage().timeouts().implicitlyWait(3, TimeUnit.SECONDS);

		if (webDriver.findElement(By.xpath("/html/body")).getText().contains("That colour is already being used by")) {
			status = "Color is already being used";
		} else {
			controlsWebElement = webDriver.findElement(By.xpath("/html/body/div[3]"));
			categoryList = controlsWebElement.findElements(By.tagName("span"));

			// Check for size increase
			if (oldCategorySize < categoryList.size()) { // Success
				// Iterate over category list to find just added category
				for (int i = 1; i < categoryList.size(); i++) { // Start from index 1 (Ignore Overdue)
					// Get Category name
					WebElement span = categoryList.get(i);
					// Check if it is the same as added category name
					if (span.getText().equals(fileName)) { // Passed
						status = "Passed";
						
						// Break the loop
						break;
					} else { // Failed, can't find it
						status = "Failed";
						
					}
				}
			} else { // fail
				status = "Failed";
				
			}
		}

		// Take screen shot of added category and clicking Add button
		takeFullScreenShot(webDriver, reportDest + "TC4.2/TC4.2" + "-" + fileName + "-2");

		// Archive the report
		writeToFile(new String[] { "TC4.2", status, fileName });
	}

	/**
	 * This method clicks Add Category button (Negative test) 
	 * @throws Exception
	 */
	public static void addCategoryTC4_3() throws Exception {
		
		loadMainPage(false);

		// Get all web elements related to Add Category functionality
		WebElement controlsWebElement = webDriver.findElement(By.xpath("/html/body/div[3]"));
		List<WebElement> categoryList = controlsWebElement.findElements(By.tagName("span"));

		// Get button for Add Category
		WebElement addCategoryButton = webDriver.findElement(By.xpath(getXpath("TC1.16")));

		// Size of categories before adding
		int oldCategorySize = categoryList.size();

		// Reserve file name
		String fileName = getTimeForFileName();

		// Status of TC
		String status = "";

		// Click on Add Category Button
		clickAndHoldWebElement(addCategoryButton);

		// Take screen shot of inputed text and clicking Add Category button
		takeFullScreenShot(webDriver, reportDest + "TC4.3/TC4.3" + "-" + fileName + "-1");

		// release Add category button
		releaseClickWebElement(addCategoryButton);

		// Wait for page to load
		webDriver.manage().timeouts().implicitlyWait(3, TimeUnit.SECONDS);

		// Update
		controlsWebElement = webDriver.findElement(By.xpath("/html/body/div[3]"));
		categoryList = controlsWebElement.findElements(By.tagName("span"));

		// Check for size increase
		if (oldCategorySize != categoryList.size()) { // Success
			status = "Failed";
			
		} else { // fail
			status = "Passed";
			
		}

		// Take screen shot of added category and clicking Add button
		takeFullScreenShot(webDriver, reportDest + "TC4.3/TC4.3" + "-" + fileName + "-2");

		// Archive the report
		writeToFile(new String[] { "TC4.3", status, fileName });
	}
	
	/**
	 * Test Case: TC5.1 - TC5.11
	 * This method executes all test cases for edit functionality
	 * @throws Exception
	 */
	public static void editRowTests() throws Exception {
		editRowTC5_1();
		editRowTC5_2();
		editRowTC5_3to5_5("/html/body/form/select[2]", "1", "TC5.1");
		editRowTC5_3to5_5("/html/body/form/select[3]", "Jan", "TC5.4");
		editRowTC5_3to5_5("/html/body/form/select[4]", "2020", "TC5.5");
		editRowTC5_6();
		editRowTC5_7();
		editRowTC5_8to5_10("/html/body/form/select[2]", "1", "TC5.8");
		editRowTC5_8to5_10("/html/body/form/select[3]", "1", "TC5.9");
		editRowTC5_8to5_10("/html/body/form/select[4]", "1", "TC5.10");
		editRowTC5_11();
	}

	/**
	 * This method clicks on the first element on the row (if list is not empty),
	 * updates the name and clicks Update
	 * 
	 * @throws Exception
	 */
	public static void editRowTC5_1() throws Exception {
		
		// Load the main page
		loadMainPage(false);

		// Get all web elements related to Add Category functionality
		WebElement toDoList = webDriver.findElement(By.xpath(getXpath("TC1.4")));
		List<WebElement> categoryList = toDoList.findElements(By.tagName("li"));

		String status = "";
		String fileName = getTimeForFileName();

		// Size of categories before adding
		int categorySize = categoryList.size();

		// If list has any item
		if (categorySize > 0) { // List is not empty
			// Get the first element on the list with the id-link
			WebElement idButton = null;
			String idNumber = "";
			String oldRowName = "";

			for (WebElement webElement : categoryList) {
				if (webElement.getText().endsWith("(None)")) {
					idButton = webElement.findElement(By.tagName("a"));
					idNumber = webElement.findElement(By.tagName("a")).getText();
					oldRowName = webElement.getText();
					break;
				} else {
					idButton = categoryList.get(0).findElement(By.tagName("a"));
				}

			}

			// Click on id and hold to take screen shot
			clickAndHoldWebElement(idButton);

			// Take screen shot of inputed text and clicking Add Category button
			takeFullScreenShot(webDriver, reportDest + "TC5.1/TC5.1" + "-" + fileName + "-1");

			// Release click
			releaseClickWebElement(idButton);

			// Wait for page to load
			webDriver.manage().timeouts().implicitlyWait(3, TimeUnit.SECONDS);

			// Get update-text-field
			WebElement updateTextField = webDriver.findElement(By.xpath("/html/body/form/input[3]"));

			// Get Update button
			WebElement updateButton = webDriver.findElement(By.xpath("/html/body/form/input[4]"));

			// Delete all characters on textfield
			for (int i = 0; i < oldRowName.length(); i++) {
				clearTextField(updateTextField);
			}

			// input new name
			inputTextWebElement(updateTextField, fileName);

			// click and hold on update button
			clickAndHoldWebElement(updateButton);

			// Take screen shot of inputed text and clicking Update button
			takeFullScreenShot(webDriver, reportDest + "TC5.1/TC5.1" + "-" + fileName + "-2");

			// Release update button
			releaseClickWebElement(updateButton);

			// Wait for page to load
			webDriver.manage().timeouts().implicitlyWait(3, TimeUnit.SECONDS);

			// update
			toDoList = webDriver.findElement(By.xpath(getXpath("TC1.4")));
			categoryList = toDoList.findElements(By.tagName("li"));

			// Iteraite over to do list to find updated list
			for (WebElement webElement : categoryList) {
				// look for id to find the updated row
				if (webElement.findElement(By.tagName("a")).getText().equals(idNumber)) { // Found a row with the same
																							// id
					// Check if name is new
					if (!webElement.getText().contains(oldRowName)) { // Different name
						// Update Status
						status = "Passed";
						
					} else { // Name was not changed
						// Update Status
						status = "Failed";
						
					}
					break;
				} else { // Row was not found with the same id
					// Update Status
					status = "Failed";
					
				}
			}
		} else { // List is empty
			// Update status
			status = "List is empty";
		}

		// Take screen shot of inputed text and clicking Add Category button
		takeFullScreenShot(webDriver, reportDest + "TC5.1/TC5.1" + "-" + fileName + "final");

		// Archive the report
		writeToFile(new String[] { "TC5.1", status, fileName });
	}

	/**
	 * This method clicks on the first element on the row (if list is not empty),
	 * updates the name and category and clicks Update
	 * 
	 * @throws Exception
	 */
	public static void editRowTC5_2() throws Exception {
		
		// Load the main page
		loadMainPage(false);

		// Get all web elements related to Add Category functionality
		WebElement toDoList = webDriver.findElement(By.xpath(getXpath("TC1.4")));
		List<WebElement> categoryList = toDoList.findElements(By.tagName("li"));

		String status = "";
		String fileName = getTimeForFileName();

		// Size of categories before adding
		int categorySize = categoryList.size();

		// If list has any item
		if (categorySize > 0) { // List is not empty
			// Get the first element on the list with the id-link
			WebElement idButton = null;
			String idNumber = "";
			String oldRowName = "";

			for (WebElement webElement : categoryList) {
				if (webElement.getText().endsWith("(None)")) {
					idButton = webElement.findElement(By.tagName("a"));
					idNumber = webElement.findElement(By.tagName("a")).getText();
					oldRowName = webElement.getText();
					break;
				} else {
					idButton = categoryList.get(0).findElement(By.tagName("a"));
				}
			}

			// Click on id and hold to take screen shot
			clickAndHoldWebElement(idButton);

			// Take screen shot of inputed text and clicking Add Category button
			takeFullScreenShot(webDriver, reportDest + "TC5.2/TC5.2" + "-" + fileName + "-1");

			// Release click
			releaseClickWebElement(idButton);

			// Wait for page to load
			webDriver.manage().timeouts().implicitlyWait(3, TimeUnit.SECONDS);

			// Get update-text-field
			WebElement updateTextField = webDriver.findElement(By.xpath("/html/body/form/input[3]"));

			// Get Update button
			WebElement updateButton = webDriver.findElement(By.xpath("/html/body/form/input[4]"));

			// Get a category drop-down
			WebElement categoryDropDown = webDriver.findElement(By.xpath("/html/body/form/select[1]"));

			// Choose College
			selectFromDropDown(categoryDropDown, "/html/body/form/select[1]", "College");

			// Delete all characters on textfield
			for (int i = 0; i < oldRowName.length(); i++) {
				clearTextField(updateTextField);
			}

			// input new name
			inputTextWebElement(updateTextField, fileName);

			// click and hold on update button
			clickAndHoldWebElement(updateButton);

			// Take screen shot of inputed text and clicking Update button
			takeFullScreenShot(webDriver, reportDest + "TC5.2/TC5.2" + "-" + fileName + "-2");

			// Release update button
			releaseClickWebElement(updateButton);

			// Wait for page to load
			webDriver.manage().timeouts().implicitlyWait(3, TimeUnit.SECONDS);

			// update
			toDoList = webDriver.findElement(By.xpath(getXpath("TC1.4")));
			categoryList = toDoList.findElements(By.tagName("li"));

			// Iteraite over to do list to find updated list
			for (WebElement webElement : categoryList) {
				// look for id to find the updated row
				if (webElement.findElement(By.tagName("a")).getText().equals(idNumber)) { // Found a row with the same
																							// id
					// Check if name is new
					if (!webElement.findElement(By.tagName("span")).getText().equals(oldRowName)) { // Different name
						// Update Status
						status = "Passed";
						
					} else { // Name was not changed
						// Update Status
						status = "Failed";
						
					}
					break;
				} else { // Row was not found with the same id
					// Update Status
					status = "Failed";
					
				}
			}
		} else { // List is empty
			// Update status
			status = "List is empty";
			
		}

		// Take screen shot of inputed text and clicking Add Category button
		takeFullScreenShot(webDriver, reportDest + "TC5.2/TC5.2" + "-" + fileName + "final");

		// Archive the report
		writeToFile(new String[] { "TC5.2", status, fileName });
	}

	/**
	 * This method clicks on the first element on the row (if list not empty)
	 * chooses College from drop down menu 1 - chooses 1 for due day 2 - chooses Jan
	 * for due month 3 - chooses 2020 for due year
	 * 
	 * @throws Exception
	 */
	public static void editRowTC5_3to5_5(String xPath, String optionSelect, String TC) throws Exception {
		
		// Load the main page
		loadMainPage(false);

		// Get all web elements related to Add Category functionality
		WebElement toDoList = webDriver.findElement(By.xpath(getXpath("TC1.4")));
		List<WebElement> categoryList = toDoList.findElements(By.tagName("li"));

		String status = "";
		String fileName = getTimeForFileName();

		// Size of categories before adding
		int categorySize = categoryList.size();

		// If list has any item
		if (categorySize > 0) { // List is not empty
			// Get the first element on the list with the id-link
			WebElement idButton = null;
			String idNumber = "";
			String oldRowName = "";

			for (WebElement webElement : categoryList) {
				if (webElement.getText().endsWith("(None)")) {
					idButton = webElement.findElement(By.tagName("a"));
					idNumber = webElement.findElement(By.tagName("a")).getText();
					oldRowName = webElement.getText();
					break;
				} else {
					idButton = categoryList.get(0).findElement(By.tagName("a"));
				}
			}

			// Click on id and hold to take screen shot
			clickAndHoldWebElement(idButton);

			// Take screen shot of inputed text and clicking Add Category button
			takeFullScreenShot(webDriver, reportDest + TC + "/" + TC + "-" + fileName + "-1");

			// Release click
			releaseClickWebElement(idButton);

			// Wait for page to load
			webDriver.manage().timeouts().implicitlyWait(3, TimeUnit.SECONDS);

			// Get update-text-field
			WebElement updateTextField = webDriver.findElement(By.xpath("/html/body/form/input[3]"));

			// Get Update button
			WebElement updateButton = webDriver.findElement(By.xpath("/html/body/form/input[4]"));

			// Get a category drop-down
			WebElement categoryDropDown = webDriver.findElement(By.xpath("/html/body/form/select[1]"));

			// Choose College-category
			selectFromDropDown(categoryDropDown, "/html/body/form/select[1]", "College");

			// Choose value from drop down
			selectFromDropDown(webDriver.findElement(By.xpath(xPath)), xPath, optionSelect);

			// Delete all characters on textfield
			for (int i = 0; i < oldRowName.length(); i++) {
				clearTextField(updateTextField);
			}

			// input new name
			inputTextWebElement(updateTextField, fileName);

			// click and hold on update button
			clickAndHoldWebElement(updateButton);

			// Take screen shot of inputed text and clicking Update button
			takeFullScreenShot(webDriver, reportDest + TC + "/" + TC + "-" + fileName + "-2");

			// Release update button
			releaseClickWebElement(updateButton);

			// Wait for page to load
			webDriver.manage().timeouts().implicitlyWait(3, TimeUnit.SECONDS);

			// update
			toDoList = webDriver.findElement(By.xpath(getXpath("TC1.4")));
			categoryList = toDoList.findElements(By.tagName("li"));

			// Iteraite over to do list to find updated list
			for (WebElement webElement : categoryList) {
				// look for id to find the updated row
				if (webElement.findElement(By.tagName("a")).getText().equals(idNumber)) { // Found a row with the same
																							// id
					// Check if name is new
					if (!webElement.findElement(By.tagName("span")).getText().equals(oldRowName)) { // Different name
						// Check the date, should be none
						if (webElement.getText().endsWith("(None)")) {
							// Update Status
							status = "Passed";
							
						} else { // Date appears, failure
							// Update Status
							status = "Failed";
							
						}
					} else { // Name was not changed
						// Update Status
						status = "Failed";
						
					}
					break;
				} else { // Row was not found with the same id
					// Update Status
					status = "Failed";
					
				}
			}
		} else { // List is empty
			// Update status
			status = "List is empty";
			
		}

		// Take screen shot of inputed text and clicking Add Category button
		takeFullScreenShot(webDriver, reportDest + TC + "/" + TC + "-" + fileName + "-final");

		// Archive the report
		writeToFile(new String[] { TC, status, fileName });
	}

	/**
	 * This method clicks on the first element on the row (if list not empty)
	 * chooses College from drop down menu 1 - chooses 1 for due day 2 - chooses Jan
	 * for due month
	 * 
	 * @throws Exception
	 */
	public static void editRowTC5_6() throws Exception {
		
		// Load the main page
		loadMainPage(false);

		// Get all web elements related to Add Category functionality
		WebElement toDoList = webDriver.findElement(By.xpath(getXpath("TC1.4")));
		List<WebElement> categoryList = toDoList.findElements(By.tagName("li"));

		String status = "";
		String fileName = getTimeForFileName();

		// Size of categories before adding
		int categorySize = categoryList.size();

		// If list has any item
		if (categorySize > 0) { // List is not empty
			// Get the first element on the list with the id-link
			WebElement idButton = categoryList.get(0).findElement(By.tagName("a"));

			String idNumber = categoryList.get(0).findElement(By.tagName("a")).getText();
			String oldRowName = categoryList.get(0).getText();

			// Click on id and hold to take screen shot
			clickAndHoldWebElement(idButton);

			// Take screen shot of inputed text and clicking Add Category button
			takeFullScreenShot(webDriver, reportDest + "TC5.6/TC5.6-" + fileName + "-1");

			// Release click
			releaseClickWebElement(idButton);

			// Wait for page to load
			webDriver.manage().timeouts().implicitlyWait(3, TimeUnit.SECONDS);

			// Get update-text-field
			WebElement updateTextField = webDriver.findElement(By.xpath("/html/body/form/input[3]"));

			// Get Update button
			WebElement updateButton = webDriver.findElement(By.xpath("/html/body/form/input[4]"));

			// Get a category drop-down
			WebElement categoryDropDown = webDriver.findElement(By.xpath("/html/body/form/select[1]"));

			// Get due day WebElemnt
			WebElement dueDayDropDown = webDriver.findElement(By.xpath("/html/body/form/select[2]"));

			// Get due Month
			WebElement dueMonthDropDown = webDriver.findElement(By.xpath("/html/body/form/select[3]"));

			// Choose College-category
			selectFromDropDown(categoryDropDown, "/html/body/form/select[1]", "College");

			// Choose 1 for due date
			selectFromDropDown(dueDayDropDown, "/html/body/form/select[2]", "1");

			// Choose Jan for month
			selectFromDropDown(dueMonthDropDown, "/html/body/form/select[3]", "Jan");

			// Delete all characters on textfield
			for (int i = 0; i < oldRowName.length(); i++) {
				clearTextField(updateTextField);
			}

			// input new name
			inputTextWebElement(updateTextField, fileName);

			// click and hold on update button
			clickAndHoldWebElement(updateButton);

			// Take screen shot of inputed text and clicking Update button
			takeFullScreenShot(webDriver, reportDest + "TC5.6/TC5.6-" + fileName + "-2");

			// Release update button
			releaseClickWebElement(updateButton);

			// Wait for page to load
			webDriver.manage().timeouts().implicitlyWait(3, TimeUnit.SECONDS);

			// update
			toDoList = webDriver.findElement(By.xpath(getXpath("TC1.4")));
			categoryList = toDoList.findElements(By.tagName("li"));

			// Iteraite over to do list to find updated list
			for (WebElement webElement : categoryList) {
				// look for id to find the updated row
				if (webElement.findElement(By.tagName("a")).getText().equals(idNumber)) { // Found a row with the same
																							// id
					// Check if name is new
					if (!webElement.getText().contains(oldRowName)) { // Different name
						// Check the date, if none, failure
						if (webElement.getText().endsWith("(None)")) {
							// Update Status
							status = "Failed";
							
						} else { // Date appears, success
							List<WebElement> span = webElement.findElements(By.tagName("span"));

							if (!checkDate(webElement.getText())) {

								// Past due
								// Color should be red, check
								if (rgbaToHex(webElement.getCssValue("color")).toUpperCase().equals("#FF0000")) { // Color
																													// is
																													// red
									// Update status
									status = "Passed";
									
								} else if (rgbaToHex(webElement.getCssValue("color")).toUpperCase().equals("#00FF00")) { // Color
																															// is
																															// green
																															// failed
									// Update status
									status = "Failed";
									
								} else {
									// Any other color, fail
									// Update status
									status = "Failed";
									
								}
							} else {
								// Not past due
								// Color should be green (College)
								// Color should be green, check
								if (rgbaToHex(webElement.getCssValue("color")).toUpperCase().equals("#FF0000")) { // Color
																													// is
																													// red
									// Update status
									status = "Failed";
									
								} else if (rgbaToHex(webElement.getCssValue("color")).toUpperCase().equals("#00FF00")) { // Color
																															// is
																															// green
																															// passed
									// Update status
									status = "Passed";
									
								} else {
									// Any other color, fail
									// Update status
									status = "Failed";
									
								}
							}

							// Update Status
							status = "Passed";
							
						}
					} else { // Name was not changed
						// Update Status
						status = "Failed";
						
					}

				} else { // Row was not found with the same id
					// Update Status
					status = "Failed";
					
				}

				// Breake the loop
				break;
			}
		} else { // List is empty
			// Update status
			status = "List is empty";
			
		}

		// Take screen shot of inputed text and clicking Add Category button
		takeFullScreenShot(webDriver, reportDest + "TC5.6/TC5.6-" + fileName + "-final");

		// Archive the report
		writeToFile(new String[] { "TC5.6", status, fileName });
	}

	/**
	 * This method clicks on the first element on the row (if list not empty)
	 * chooses College from drop down menu 1 - chooses 1 for due day 2 - chooses Jan
	 * for due month 3 - chooses 2020 for due year
	 * 
	 * @throws Exception
	 */
	public static void editRowTC5_7() throws Exception {
		
		// Load the main page
		loadMainPage(false);

		// Get all web elements related to Add Category functionality
		WebElement toDoList = webDriver.findElement(By.xpath(getXpath("TC1.4")));
		List<WebElement> categoryList = toDoList.findElements(By.tagName("li"));

		String status = "";
		String fileName = getTimeForFileName();

		// Size of categories before adding
		int categorySize = categoryList.size();

		// If list has any item
		if (categorySize > 0) { // List is not empty
			// Get the first element on the list with the id-link
			WebElement idButton = null;
			String idNumber = "";
			String oldRowName = "";

			for (WebElement webElement : categoryList) {
				if (webElement.getText().endsWith("(None)")) {
					idButton = webElement.findElement(By.tagName("a"));
					idNumber = webElement.findElement(By.tagName("a")).getText();
					oldRowName = webElement.getText();
					break;
				} else {
					idButton = categoryList.get(0).findElement(By.tagName("a"));
				}
			}

			// Click on id and hold to take screen shot
			clickAndHoldWebElement(idButton);

			// Take screen shot of inputed text and clicking Add Category button
			takeFullScreenShot(webDriver, reportDest + "TC5.7/TC5.7-" + fileName + "-1");

			// Release click
			releaseClickWebElement(idButton);

			// Wait for page to load
			webDriver.manage().timeouts().implicitlyWait(3, TimeUnit.SECONDS);

			// Get update-text-field
			WebElement updateTextField = webDriver.findElement(By.xpath("/html/body/form/input[3]"));

			// Get Update button
			WebElement updateButton = webDriver.findElement(By.xpath("/html/body/form/input[4]"));

			// Get a category drop-down
			WebElement categoryDropDown = webDriver.findElement(By.xpath("/html/body/form/select[1]"));

			// Get due day WebElemnt
			WebElement dueDayDropDown = webDriver.findElement(By.xpath("/html/body/form/select[2]"));

			// Get due Month
			WebElement dueMonthDropDown = webDriver.findElement(By.xpath("/html/body/form/select[3]"));

			// Get due year dropdown
			WebElement dueYearDropDown = webDriver.findElement(By.xpath("/html/body/form/select[4]"));

			// Choose College-category
			selectFromDropDown(categoryDropDown, "/html/body/form/select[1]", "College");

			// Choose 1 for due date
			selectFromDropDown(dueDayDropDown, "/html/body/form/select[2]", "1");

			// Choose Jan for month
			selectFromDropDown(dueMonthDropDown, "/html/body/form/select[3]", "Jan");

			// Chose 2020 for year

			selectFromDropDown(dueYearDropDown, "/html/body/form/select[4]", "2020");

			// Delete all characters on textfield
			for (int i = 0; i < oldRowName.length(); i++) {
				clearTextField(updateTextField);
			}

			// input new name
			inputTextWebElement(updateTextField, fileName);

			// click and hold on update button
			clickAndHoldWebElement(updateButton);

			// Take screen shot of inputed text and clicking Update button
			takeFullScreenShot(webDriver, reportDest + "TC5.7/TC5.7-" + fileName + "-2");

			// Release update button
			releaseClickWebElement(updateButton);

			// Wait for page to load
			webDriver.manage().timeouts().implicitlyWait(3, TimeUnit.SECONDS);

			// update
			toDoList = webDriver.findElement(By.xpath(getXpath("TC1.4")));
			categoryList = toDoList.findElements(By.tagName("li"));

			// Iteraite over to do list to find updated list
			for (WebElement webElement : categoryList) {
				// look for id to find the updated row
				if (webElement.findElement(By.tagName("a")).getText().equals(idNumber)) { // Found a row with the same
																							// id
					// Check if name is new
					if (!webElement.getText().contains(oldRowName)) { // Different name
						// Check the date, if none, failure
						if (webElement.getText().endsWith("(None)")) {
							// Update Status
							status = "Failed";
							
						} else { // Date appears, success
							if (!checkDate(webElement.getText())) {
								// Not Past due
								// Color should be green, check
								if (rgbaToHex(webElement.findElement(By.tagName("span")).getCssValue("color"))
										.toUpperCase().equals("#FF0000")) { // Color is red
									// Update status
									status = "Failed";
									

								} else if (rgbaToHex(webElement.findElement(By.tagName("span")).getCssValue("color"))
										.toUpperCase().equals("#00FF00")) { // Color is green failed
									// Update status
									status = "Passed";
									

								} else {
									// Any other color, fail
									// Update status
									status = "Failed";
									
								}
							} else {
								// past due
								// Color should be red
								if (rgbaToHex(webElement.findElement(By.tagName("span")).getCssValue("color"))
										.toUpperCase().equals("#FF0000")) { // Color is red
									// Update status
									status = "Failed";
									
								} else if (rgbaToHex(webElement.findElement(By.tagName("span")).getCssValue("color"))
										.toUpperCase().equals("#00FF00")) { // Color is green passed
									// Update status

									status = "Passed color, date is wrong";
									

								} else {
									// Any other color, fail
									// Update status
									status = "Failed";
									
								}
							}
						}
					} else { // Name was not changed
						// Update Status
						status = "Failed";
						
						
					}
					
					// Breake the loop
					break;
				} else { // Row was not found with the same id
					// Update Status
					status = "Failed";
					
				}
			}
		} else { // List is empty
			// Update status
			status = "List is empty";
			
		}

		// Take screen shot of inputed text and clicking Add Category button
		takeFullScreenShot(webDriver, reportDest + "TC5.7/TC5.7-" + fileName + "-final");

		// Archive the report
		writeToFile(new String[] { "TC5.7", status, fileName });
	}

	/**
	 * This method clicks on the first element on the row (if list not empty)
	 * chooses due day from drop down menu updates name, clicks update
	 * 
	 * @throws Exception
	 */
	public static void editRowTC5_8to5_10(String xPath, String optionSelect, String TC) throws Exception {
		
		// Load the main page
		loadMainPage(false);

		// Get all web elements related to Add Category functionality
		WebElement toDoList = webDriver.findElement(By.xpath(getXpath("TC1.4")));
		List<WebElement> categoryList = toDoList.findElements(By.tagName("li"));

		String status = "";
		String fileName = getTimeForFileName();

		// Size of categories before adding
		int categorySize = categoryList.size();

		// If list has any item
		if (categorySize > 0) { // List is not empty
			WebElement idButton = null;
			String idNumber = "";
			String oldRowName = "";

			for (WebElement webElement : categoryList) {
				if (webElement.getText().endsWith("(None)")) {
					idButton = webElement.findElement(By.tagName("a"));
					idNumber = webElement.findElement(By.tagName("a")).getText();
					oldRowName = webElement.getText();
					break;
				} else {
					idButton = categoryList.get(0).findElement(By.tagName("a"));
				}
			}

			// Click on id and hold to take screen shot
			clickAndHoldWebElement(idButton);

			// Take screen shot of inputed text and clicking Add Category button
			takeFullScreenShot(webDriver, reportDest + TC + "/" + TC + "-" + fileName + "-1");

			// Release click
			releaseClickWebElement(idButton);

			// Wait for page to load
			webDriver.manage().timeouts().implicitlyWait(3, TimeUnit.SECONDS);

			// Get update-text-field
			WebElement updateTextField = webDriver.findElement(By.xpath("/html/body/form/input[3]"));

			// Get Update button
			WebElement updateButton = webDriver.findElement(By.xpath("/html/body/form/input[4]"));

			// Choose value from drop down
			selectFromDropDown(webDriver.findElement(By.xpath(xPath)), xPath, optionSelect);

			// Delete all characters on textfield
			for (int i = 0; i < oldRowName.length(); i++) {
				clearTextField(updateTextField);
			}

			// input new name
			inputTextWebElement(updateTextField, fileName);

			// click and hold on update button
			clickAndHoldWebElement(updateButton);

			// Take screen shot of inputed text and clicking Update button
			takeFullScreenShot(webDriver, reportDest + TC + "/" + TC + "-" + fileName + "-2");

			// Release update button
			releaseClickWebElement(updateButton);

			// Wait for page to load
			webDriver.manage().timeouts().implicitlyWait(3, TimeUnit.SECONDS);

			// update
			toDoList = webDriver.findElement(By.xpath(getXpath("TC1.4")));
			categoryList = toDoList.findElements(By.tagName("li"));

			// Iteraite over to do list to find updated list
			for (WebElement webElement : categoryList) {
				// look for id to find the updated row
				if (webElement.findElement(By.tagName("a")).getText().equals(idNumber)) { // Found a row with the same
																							// id
					// Check if name is new
					if (!webElement.getText().equals(oldRowName)) { // Different name
						// Check the date, should be none
						if (webElement.getText().endsWith("(None)")) {
							// Update Status
							status = "Passed";
							
						} else { // Date appears, failure
							// Update Status
							status = "Failed";
							
						}
					} else { // Name was not changed
						// Update Status
						status = "Failed";
						
					}
					break;
				} else { // Row was not found with the same id
					// Update Status
					status = "Failed";
					
				}
			}
		} else { // List is empty
			// Update status
			status = "List is empty";
			
		}

		// Take screen shot of inputed text and clicking Add Category button
		takeFullScreenShot(webDriver, reportDest + TC + "/" + TC + "-" + fileName + "-final");

		// Archive the report
		writeToFile(new String[] { TC, status, fileName });
	}

	/**
	 * This method clicks on the first element on the row (if list not empty) 1 -
	 * chooses 1 for due day 2 - chooses Jan for due month
	 * 
	 * @throws Exception
	 */
	public static void editRowTC5_11() throws Exception {
		
		// Load the main page
		loadMainPage(false);

		// Get all web elements related to Add Category functionality
		WebElement toDoList = webDriver.findElement(By.xpath(getXpath("TC1.4")));
		List<WebElement> categoryList = toDoList.findElements(By.tagName("li"));

		String status = "";
		String fileName = getTimeForFileName();

		// Size of categories before adding
		int categorySize = categoryList.size();

		// If list has any item
		if (categorySize > 0) { // List is not empty
			WebElement idButton = null;
			String idNumber = "";
			String oldRowName = "";

			for (WebElement webElement : categoryList) {
				if (webElement.getText().endsWith("(None)")) {
					idButton = webElement.findElement(By.tagName("a"));
					idNumber = webElement.findElement(By.tagName("a")).getText();
					oldRowName = webElement.getText();
					break;
				} else {
					idButton = categoryList.get(0).findElement(By.tagName("a"));
				}
			}

			// Click on id and hold to take screen shot
			clickAndHoldWebElement(idButton);

			// Take screen shot of inputed text and clicking Add Category button
			takeFullScreenShot(webDriver, reportDest + "TC5.11/TC5.11-" + fileName + "-1");

			// Release click
			releaseClickWebElement(idButton);

			// Wait for page to load
			webDriver.manage().timeouts().implicitlyWait(3, TimeUnit.SECONDS);

			// Get update-text-field
			WebElement updateTextField = webDriver.findElement(By.xpath("/html/body/form/input[3]"));

			// Get Update button
			WebElement updateButton = webDriver.findElement(By.xpath("/html/body/form/input[4]"));

			// Get due day WebElemnt
			WebElement dueDayDropDown = webDriver.findElement(By.xpath("/html/body/form/select[2]"));

			// Get due Month
			WebElement dueMonthDropDown = webDriver.findElement(By.xpath("/html/body/form/select[3]"));

			// Choose 1 for due date
			selectFromDropDown(dueDayDropDown, "/html/body/form/select[2]", "1");

			// Choose Jan for month
			selectFromDropDown(dueMonthDropDown, "/html/body/form/select[3]", "Jan");

			// Delete all characters on textfield
			for (int i = 0; i < oldRowName.length(); i++) {
				clearTextField(updateTextField);
			}

			// input new name
			inputTextWebElement(updateTextField, fileName);

			// click and hold on update button
			clickAndHoldWebElement(updateButton);

			// Take screen shot of inputed text and clicking Update button
			takeFullScreenShot(webDriver, reportDest + "TC5.11/TC5.11-" + fileName + "-2");

			// Release update button
			releaseClickWebElement(updateButton);

			// Wait for page to load
			webDriver.manage().timeouts().implicitlyWait(3, TimeUnit.SECONDS);

			// update
			toDoList = webDriver.findElement(By.xpath(getXpath("TC1.4")));
			categoryList = toDoList.findElements(By.tagName("li"));

			// Iteraite over to do list to find updated list
			for (WebElement webElement : categoryList) {
				// look for id to find the updated row
				if (webElement.findElement(By.tagName("a")).getText().equals(idNumber)) { // Found a row with the same
																							// id
					// Check if name is new
					if (!webElement.getText().contains(oldRowName)) { // Different name
						// Check the date, if none, failure
						if (webElement.getText().endsWith("(None)")) {
							// Update Status
							status = "Failed";
							
						} else { // Date appears, success

							if (!checkDate(webElement.getText())) {

								// Past due
								// Color should be red, check
								if (rgbaToHex(webElement.getCssValue("color")).toUpperCase().equals("#FF0000")) { // Color
																													// is
																													// red
									// Update status
									status = "Passed";
									
								} else if (rgbaToHex(webElement.getCssValue("color")).toUpperCase().equals("#00FF00")) { // Color
																															// is
																															// green
																															// failed
									// Update status
									status = "Failed";
									
								} else {
									// Any other color, fail
									// Update status
									status = "Failed";
									
								}
							} else {
								// Not past due
								// Color should be green (College)
								// Color should be green, check
								if (rgbaToHex(webElement.getCssValue("color")).toUpperCase().equals("#FF0000")) { // Color
																													// is
																													// red
									// Update status
									status = "Failed";
									
								} else if (rgbaToHex(webElement.getCssValue("color")).toUpperCase().equals("#00FF00")) { // Color
																															// is
																															// green
																															// passed
									// Update status
									status = "Passed";
									
								} else {
									// Any other color, fail
									// Update status
									status = "Failed";
									
								}
							}

							// Update Status
							status = "Passed";
							
						}
					} else { // Name was not changed
						// Update Status
						status = "Failed";
						
					}

				} else { // Row was not found with the same id
					// Update Status
					status = "Failed";
					
				}

				// Breake the loop
				break;
			}
		} else { // List is empty
			// Update status
			status = "List is empty";
			
		}

		// Take screen shot of inputed text and clicking Add Category button
		takeFullScreenShot(webDriver, reportDest + "TC5.11/TC5.11-" + fileName + "-final");

		// Archive the report
		writeToFile(new String[] { "TC5.11", status, fileName });
	}

	/**
	 * This method clicks on the first element on the row (if list not empty), click
	 * on update (Negative testing)
	 * 
	 * @throws Exception
	 */
	public static void editRowTC5_12() throws Exception {
		
		// Load the main page
		loadMainPage(false);

		// Get all web elements related to Add Category functionality
		WebElement toDoList = webDriver.findElement(By.xpath(getXpath("TC1.4")));
		List<WebElement> categoryList = toDoList.findElements(By.tagName("li"));

		String status = "";
		String fileName = getTimeForFileName();

		// Size of categories before adding
		int categorySize = categoryList.size();

		// If list has any item
		if (categorySize > 0) { // List is not empty
			WebElement idButton = null;
			String idNumber = "";
			String oldRowName = "";

			for (WebElement webElement : categoryList) {
				if (webElement.getText().endsWith("(None)")) {
					idButton = webElement.findElement(By.tagName("a"));
					idNumber = webElement.findElement(By.tagName("a")).getText();
					oldRowName = webElement.getText();
					System.out.println(oldRowName);
					break;
				} else {
					idButton = categoryList.get(0).findElement(By.tagName("a"));
				}
			}

			// Click on id and hold to take screen shot
			clickAndHoldWebElement(idButton);

			// Take screen shot of inputed text and clicking Add Category button
			takeFullScreenShot(webDriver, reportDest + "TC5.12/TC5.12-" + fileName + "-1");

			// Release click
			releaseClickWebElement(idButton);

			// Wait for page to load
			webDriver.manage().timeouts().implicitlyWait(3, TimeUnit.SECONDS);

			// Get Update button
			WebElement updateButton = webDriver.findElement(By.xpath("/html/body/form/input[4]"));

			// click and hold on update button
			clickAndHoldWebElement(updateButton);

			// Take screen shot of inputed text and clicking Update button
			takeFullScreenShot(webDriver, reportDest + "TC5.12/TC5.12-" + fileName + "-2");

			// Release update button
			releaseClickWebElement(updateButton);

			// Wait for page to load
			webDriver.manage().timeouts().implicitlyWait(3, TimeUnit.SECONDS);

			// update
			toDoList = webDriver.findElement(By.xpath(getXpath("TC1.4")));
			categoryList = toDoList.findElements(By.tagName("li"));

			// Iteraite over to do list to find updated list
			for (WebElement webElement : categoryList) {
				// look for id to find the updated row
				System.out.println(webElement.getText());
				if (webElement.findElement(By.tagName("a")).getText().contains(idNumber)) { // Found a row with the same
																							// id
					// Check if name is new
					if (!webElement.getText().contains(oldRowName)) {
						status = "Failed";
						
					} else { // Name was not changed
						// Update Status
						status = "Passed";
						

						// Breake the loop
						break;
					}
				} else { // Row was not found with the same id
					// Update Status
					status = "Failed";
					
				}
			}
		} else { // List is empty
			// Update status
			status = "List is empty";
			
		}

		// Take screen shot of inputed text and clicking Add Category button
		takeFullScreenShot(webDriver, reportDest + "TC5.12/TC5.12-" + fileName + "-final");

		// Archive the report
		writeToFile(new String[] { "TC5.12", status, fileName });
	}
	
	/**
	 * This method execute test cases: TC6.1 - TC6.3
	 * @throws Exception
	 */
	public static void completeButtonTests() throws Exception {
		completeButtonTC6_1();
		completeButtonTC6_2();
	}

	/**
	 * This method marks the first element on the list and clicks complete checks if
	 * element was changed from uncomplete to complete or vice versa
	 * 
	 * @throws Exception
	 */
	public static void completeButtonTC6_1() throws Exception {
		
		loadMainPage(false);

		// Get all web elements related to Add Category functionality
		WebElement toDoList = webDriver.findElement(By.xpath(getXpath("TC1.4")));
		List<WebElement> categoryList = toDoList.findElements(By.tagName("li"));
		WebElement completeButton = webDriver.findElement(By.xpath(getXpath("TC1.6")));

		String status = "";
		String fileName = getTimeForFileName();

		WebElement row = categoryList.get(0);
		boolean isCrossed = false;

		// Check if row is completed
		try {
			if (row.findElement(By.tagName("strike")) != null) {
				isCrossed = true;
			}
		} catch (Exception e) {
			// TODO: handle exception
			isCrossed = false;
		}

		// Mark the row
		clickOnWebElement(row.findElement(By.tagName("input")));

		clickAndHoldWebElement(completeButton);

		// Take screen shot of inputed text and clicking Update button
		takeFullScreenShot(webDriver, reportDest + "TC6.1/TC6.1-" + fileName + "-1");

		// release
		releaseClickWebElement(completeButton);

		// Wait for page to load
		webDriver.manage().timeouts().implicitlyWait(3, TimeUnit.SECONDS);

		boolean isCrossedAfter = false;

		// Check if row is completed
		try {
			if (row.findElement(By.tagName("strike")) != null) {
				isCrossedAfter = true;
			}
		} catch (Exception e) {
			// TODO: handle exception
			isCrossedAfter = false;
		}

		// Check if complete
		if (isCrossedAfter == isCrossed) {
			status = "Failed";
			
		} else {
			status = "Passed";
			
		}

		// Take screen shot of inputed text and clicking Update button
		takeFullScreenShot(webDriver, reportDest + "TC6.1/TC6.1-" + fileName + "-2");

		// Archive the report
		writeToFile(new String[] { "TC6.1", status, fileName });

	}

	/**
	 * This method marks all the elements and clicks complete button Checks each
	 * element if they were changed from uncomplete to complete vice versa
	 * 
	 * @throws Exception
	 */
	public static void completeButtonTC6_2() throws Exception {
		
		loadMainPage(false);

		// Get all web elements related to Add Category functionality
		WebElement toDoList = webDriver.findElement(By.xpath(getXpath("TC1.4")));
		List<WebElement> categoryList = toDoList.findElements(By.tagName("li"));

		WebElement completeButton = webDriver.findElement(By.xpath(getXpath("TC1.6")));
		WebElement toggleAll = webDriver.findElement(By.xpath(getXpath("TC1.7")));

		HashMap<String, String> map = new HashMap<>();
		for (WebElement webElement : categoryList) {
			String isCrossed = "no";
			// Check if row is completed
			try {
				if (webElement.findElement(By.tagName("strike")) != null) {
					isCrossed = "yes";
				}
			} catch (Exception e) {
				// TODO: handle exception
				isCrossed = "no";
			}

			map.put(webElement.findElement(By.tagName("a")).getText(), isCrossed);
		}

		String status = "";
		String fileName = getTimeForFileName();

		clickOnWebElement(toggleAll);

		clickAndHoldWebElement(completeButton);

		// Take screen shot of inputed text and clicking Update button
		takeFullScreenShot(webDriver, reportDest + "TC6.2/TC6.2-" + fileName + "-1");

		releaseClickWebElement(completeButton);

		// Wait for page to load
		webDriver.manage().timeouts().implicitlyWait(3, TimeUnit.SECONDS);

		// Update
		toDoList = webDriver.findElement(By.xpath(getXpath("TC1.4")));
		categoryList = toDoList.findElements(By.tagName("li"));

		for (WebElement webElement : categoryList) {
			String isCrossed = "no";
			// Check if row is completed
			try {
				if (webElement.findElement(By.tagName("strike")) != null) {
					isCrossed = "yes";
				}
			} catch (Exception e) {
				// TODO: handle exception
				isCrossed = "no";
			}

			if (isCrossed.equals(map.get(webElement.findElement(By.tagName("a")).getText()))) {
				status = "Failed";
				
			} else {
				status = "Passed";
				
			}
		}

		// Take screen shot of inputed text and clicking Update button
		takeFullScreenShot(webDriver, reportDest + "TC6.2/TC6.2-" + fileName + "-2");

		// Archive the report
		writeToFile(new String[] { "TC6.2", status, fileName });
	}

	/**
	 * This method just clicks on complete button (Negative test) checks if any
	 * elements have changed their state
	 * 
	 * @throws Exception
	 */
	public static void completeButtonTC6_3() throws Exception {
		
		loadMainPage(false);

		// Get all web elements related to Add Category functionality
		WebElement toDoList = webDriver.findElement(By.xpath(getXpath("TC1.4")));
		List<WebElement> categoryList = toDoList.findElements(By.tagName("li"));

		WebElement completeButton = webDriver.findElement(By.xpath(getXpath("TC1.6")));

		HashMap<String, String> map = new HashMap<>();
		for (WebElement webElement : categoryList) {
			String isCrossed = "no";
			// Check if row is completed
			try {
				if (webElement.findElement(By.tagName("strike")) != null) {
					isCrossed = "yes";
				}
			} catch (Exception e) {
				// TODO: handle exception
				isCrossed = "no";
			}

			map.put(webElement.findElement(By.tagName("a")).getText(), isCrossed);
		}

		String status = "";
		String fileName = getTimeForFileName();

		clickAndHoldWebElement(completeButton);

		// Take screen shot of inputed text and clicking Update button
		takeFullScreenShot(webDriver, reportDest + "TC6.3/TC6.3-" + fileName + "-1");

		releaseClickWebElement(completeButton);

		// Wait for page to load
		webDriver.manage().timeouts().implicitlyWait(3, TimeUnit.SECONDS);

		// Update
		toDoList = webDriver.findElement(By.xpath(getXpath("TC1.4")));
		categoryList = toDoList.findElements(By.tagName("li"));

		for (WebElement webElement : categoryList) {
			String isCrossed = "no";
			// Check if row is completed
			try {
				if (webElement.findElement(By.tagName("strike")) != null) {
					isCrossed = "yes";
				}
			} catch (Exception e) {
				// TODO: handle exception
				isCrossed = "no";
			}

			if (isCrossed.equals(map.get(webElement.findElement(By.tagName("a")).getText()))) {
				status = "Passed";
				
			} else {
				status = "Failed";
				
			}
		}

		// Take screen shot of inputed text and clicking Update button
		takeFullScreenShot(webDriver, reportDest + "TC6.3/TC6.3-" + fileName + "-2");

		// Archive the report
		writeToFile(new String[] { "TC6.3", status, fileName });
	}

	public static void removeButtonTests() throws Exception {
		removeButtonTC7_1();
		
		// Elements need for API test. Not to remove them all
//		removeButtonTC7_2();
	}

	/**
	 * This method marks first element on the list and clicks remove checks if
	 * removed element still exist on the list
	 * 
	 * @throws Exception
	 */
	public static void removeButtonTC7_1() throws Exception {
		
		loadMainPage(false);

		// Get all web elements related to Add Category functionality
		WebElement toDoList = webDriver.findElement(By.xpath(getXpath("TC1.4")));
		List<WebElement> categoryList = toDoList.findElements(By.tagName("li"));
		WebElement removeButton = webDriver.findElement(By.xpath(getXpath("TC1.5")));

		String status = "";
		String fileName = getTimeForFileName();
		int listSize = categoryList.size();

		WebElement row = categoryList.get(0);
		String idNum = row.findElement(By.tagName("a")).getText();

		// Mark the row
		clickOnWebElement(row.findElement(By.tagName("input")));

		clickAndHoldWebElement(removeButton);

		// Take screen shot of inputed text and clicking Update button
		takeFullScreenShot(webDriver, reportDest + "TC7.1/TC7.1-" + fileName + "-1");

		// release
		releaseClickWebElement(removeButton);

		// Wait for page to load
		webDriver.manage().timeouts().implicitlyWait(3, TimeUnit.SECONDS);

		// Update
		toDoList = webDriver.findElement(By.xpath(getXpath("TC1.4")));
		categoryList = toDoList.findElements(By.tagName("li"));

		if (categoryList.size() >= listSize) {
			status = "Failed";
			
		} else {
			for (WebElement webElement : categoryList) {
				if (webElement.findElement(By.tagName("a")).getText().equals(idNum)) {
					status = "Failed";
					
					break;
				} else {
					status = "Passed";
					
				}
			}
		}

		// Take screen shot of inputed text and clicking Update button
		takeFullScreenShot(webDriver, reportDest + "TC6.1/TC6.1-" + fileName + "-2");

		// Archive the report
		writeToFile(new String[] { "TC7.1", status, fileName });
	}

	/**
	 * This method marks all elements and clicks on remove button Checks if any left
	 * on the list
	 * 
	 * @throws Exception
	 */
	public static void removeButtonTC7_2() throws Exception {
		
		loadMainPage(false);

		// Get all web elements related to Add Category functionality
		WebElement toDoList = webDriver.findElement(By.xpath(getXpath("TC1.4")));
		List<WebElement> categoryList = toDoList.findElements(By.tagName("li"));
		WebElement removeButton = webDriver.findElement(By.xpath(getXpath("TC1.5")));
		WebElement toggleAll = webDriver.findElement(By.xpath(getXpath("TC1.7")));

		String status = "";
		String fileName = getTimeForFileName();

		// Mark the row
		clickOnWebElement(toggleAll);

		clickAndHoldWebElement(removeButton);

		// Take screen shot of inputed text and clicking Update button
		takeFullScreenShot(webDriver, reportDest + "TC7.2/TC7.2-" + fileName + "-1");

		// release
		releaseClickWebElement(removeButton);

		// Wait for page to load
		webDriver.manage().timeouts().implicitlyWait(3, TimeUnit.SECONDS);

		// Update
		toDoList = webDriver.findElement(By.xpath(getXpath("TC1.4")));
		categoryList = toDoList.findElements(By.tagName("li"));

		if (categoryList.size() > 0) {
			status = "Failed";
			
		} else {
			status = "Passed";
			
		}

		// Take screen shot of inputed text and clicking Update button
		takeFullScreenShot(webDriver, reportDest + "TC7.2/TC7.2-" + fileName + "-2");

		// Archive the report
		writeToFile(new String[] { "TC7.2", status, fileName });
	}
	
	/**
	 * This method test API
	 * checks response code
	 * Count how many tasks have no category
	 * print task names only
	 * Print due dates in descending order.
	 */
	public static void testAPI() {
		try {
			String fileName = getTimeForFileName();
			URL url = new URL("http://127.0.0.1/www/fake-API-call.php");
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			
			conn.setRequestMethod("GET");
			conn.setRequestProperty("Accept", "application/json");
			
			
			// Check for status code. TC8.1
			if (conn.getResponseCode() != 200) {
				System.out.println("Status code: " + conn.getResponseCode());
			} else {
				System.out.println("Status code: " + conn.getResponseCode());
			}
			System.out.println();
			// Archive the report
			writeToFile(new String[] { "TC8.2", "Passed", fileName });
			
			
			
			Scanner scan = new Scanner(url.openStream());
			String entireResponse = new String();
			
			while (scan.hasNext()) entireResponse += scan.nextLine();
			scan.close();
			
			//  Calc how many items have no category TC8.2
			int categoriesSum = 0;
			JSONArray items = new JSONArray(entireResponse);
			for (int i = 0; i < items.length(); i++) {
				JSONObject item = (JSONObject) items.get(i);
				if (item.getString("category").equals("")) {
					continue;
				} else {
					categoriesSum++;
				}
				
			}
			System.out.println("Total categories: " + categoriesSum);
			System.out.println();
			writeToFile(new String[] { "TC8.3", "Passed", fileName });
			
			
			
			
			//  Print task names only. TC8.3
			System.out.println("Task names:");
			for (int i = 0; i < items.length(); i++) {
				JSONObject item = (JSONObject) items.get(i);	
			}
			System.out.println();	
			writeToFile(new String[] { "TC8.4", "Passed", fileName });
			
			
			
			
			// Print due dates in descending order. TC8.4
			ArrayList<String> tasksWithDates = new ArrayList<String>();
			ArrayList<String> tasksWithoutDates  = new ArrayList<String>();
			for (int i = 0; i < items.length(); i++) {
				JSONObject item = (JSONObject) items.get(i);
				String date = item.getString("due date");
				// No date
				if (date.equals("")) {
					tasksWithoutDates.add(item.getString("id"));
				} else {
					tasksWithDates.add(date);
				}
				tasksWithDates.add(item.getString("due date"));
			}
			
			// removes duplicates
			Set<String> set = new HashSet<>(tasksWithDates);
			tasksWithDates.clear();
			tasksWithDates.addAll(set);
			
			// Descending order
			Collections.sort(tasksWithDates, Collections.reverseOrder());
			
			// Print out
			System.out.println("Tasks in decending due date order:");
			for (String date : tasksWithDates) {
				for (int i = 0; i < items.length(); i++) {
					JSONObject item = (JSONObject) items.get(i);
					if (date.equals(item.getString("due date"))) {
						System.out.print(item.getString("task name")+ " ");
						if (!(item.getString("due date").trim().equals(""))) {
							System.out.println(new Date(Long.parseLong(item.getString("due date").trim())*1000+86400000));
						} else {
							System.out.println();
						}
					}
				}
			}
			System.out.println();
			writeToFile(new String[] { "TC8.1", "Passed", fileName });
			
			// TC8.5
			// How many tasks ?
			System.out.println("\nNumber of tasks in total: " + items.length());
			
			
			
			conn.disconnect();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

	// Accessories
	/**
	 * This function takes screen shot of the given WebElement
	 * 
	 * @param webDriver  - WebDricer to use
	 * @param webElement - WebElement to take a picture of
	 * @param directory  - directory to save the picture
	 * @throws IOException
	 */
	public static void takeElementScreenSthot(WebDriver webDriver, WebElement webElement, String directory)
			throws IOException {
		// Get full page screenshot
		File screenShot = ((TakesScreenshot) webDriver).getScreenshotAs(OutputType.FILE);
		BufferedImage fullImage = ImageIO.read(screenShot);

		// Get locations of weblement within page
		Point point = webElement.getLocation();

		// Get width and height of the element
		int eleWidth = webElement.getSize().getWidth();
		int eleHeight = webElement.getSize().getHeight();

		// Crop the entire page screenshot to get only element screenshot
		BufferedImage eleScreenshot = fullImage.getSubimage(point.getX() * 2, point.getY() * 2, eleWidth * 2,
				eleHeight * 2);
		ImageIO.write(eleScreenshot, "png", screenShot);

		// Copy the element screenshot to a directory
		File fileLocation = new File(directory);
		FileUtils.copyFile(screenShot, fileLocation);
	}

	/**
	 * This function will take screenshot
	 * 
	 * @param webdriver    - WebDriver
	 * @param fileWithPath - directory for file to be saved
	 * @throws Exception
	 */
	public static void takeFullScreenShot(WebDriver webDriver, String directory) throws Exception {
		// Cast webDriver to a ScreenShot
		TakesScreenshot scrShot = ((TakesScreenshot) webDriver);

		// call getScreenshotAs method to have an image file
		File SrcFile = scrShot.getScreenshotAs(OutputType.FILE);

		// Put file to your destination
		File DestFile = new File(directory);

		// Write and copy file to the destination
		FileUtils.copyFile(SrcFile, DestFile);
	}

	/**
	 * This function returns an xPath of the given Test Case (@param tesCaseNumber)
	 * for WebElement
	 * 
	 * @param tesCaseNumber - test case number of an WebElement
	 * @return xPath of WebElement
	 * @throws IOException
	 */
	public static String getXpath(String tesCaseNumber) throws IOException {
		// xPath container
		String xPath = "";

		// Read file for xpath
		// Get file from directory
		File xpathFileDirectory = new File(xpath);

		// Read file: xpathFileDirectory
		FileReader fileReader = new FileReader(xpathFileDirectory);

		// Buffer Read the fileReader
		BufferedReader bufferReader = new BufferedReader(fileReader);

		// String container
		String line;

		// Iterate over each line
		while ((line = bufferReader.readLine()) != null) {
			String[] arrayLine = line.split(" ");
			if (tesCaseNumber.equals(arrayLine[1])) {
				xPath = arrayLine[2];
				break;
			}
		}

		// Close BufferReader
		bufferReader.close();

		return xPath;
	}

	/**
	 * Gets current time
	 * 
	 * @return time
	 */
	public static String getTimeForFileName() {
		Calendar rightNow = Calendar.getInstance();
		return rightNow.get(Calendar.MONTH) + 1 + "" + rightNow.get(Calendar.DAY_OF_MONTH) + ""
				+ rightNow.get(Calendar.YEAR) + "" + rightNow.get(Calendar.HOUR) + "" + rightNow.get(Calendar.MINUTE)
				+ "" + rightNow.get(Calendar.SECOND);
	}

	/**
	 * Writes the following header: ------------------------------------------- | 
	 * TC| Status | Date 12620194410 | -------------------------------------------
	 * 
	 * @throws IOException
	 */
	public static void writeReportHeader() throws IOException {
		// Initialize PrintWriter
		PrintWriter writer = new PrintWriter(new FileWriter(fullReportDirectory, true));

		// Print line
		writer.println(line);

		// Print header with format
		writer.printf("|%s|%s|%s|%n", StringUtils.center("TC", 7), StringUtils.center("Status", 50),
				StringUtils.center("Date " + getTimeForFileName(), 22));

		// Print line
		writer.println(line);

		// Flush and close writer
		writer.flush();
		writer.close();
	}

	/**
	 * This method writes Test Case status to the report file
	 * 
	 * @param stringToWrite - array To get Test Case number and Status
	 * @throws IOException
	 */
	public static void writeToFile(String[] stringToWrite) throws IOException {
		// Initialize PrintWriter
		PrintWriter writer = new PrintWriter(new FileWriter(fullReportDirectory, true));

		// Append-write Test Case, Status and date, format it.
		writer.printf("|%s|%s|%s|%n", StringUtils.center(stringToWrite[0], 7), StringUtils.center(stringToWrite[1], 50),
				StringUtils.center(stringToWrite[2], 22));

		// Flush and close writer
		writer.flush();
		writer.close();
	}

	/**
	 * This method moves on WebElement and click-hold on it
	 * 
	 * @param webElement - to be click-hold
	 */
	public static void clickAndHoldWebElement(WebElement webElement) {
		Actions builder = new Actions(webDriver);
		org.openqa.selenium.interactions.Action clickAndHoldAction = builder.moveToElement(webElement).clickAndHold()
				.build();
		clickAndHoldAction.perform();
	}

	/**
	 * This method releases WebElement that been click-hold.
	 * 
	 * @param webElement - to be released.
	 */
	public static void releaseClickWebElement(WebElement webElement) {
		// Release remove button
		Actions builder = new Actions(webDriver);
		org.openqa.selenium.interactions.Action releaseAction = builder.moveToElement(webElement).release().build();
		releaseAction.perform();
	}

	/**
	 * This method un marks all the elements in the to do list
	 * 
	 * @throws Exception
	 */
	public static void unCheckToDoList() throws Exception {
		// Load main page
		loadMainPage(false);

		// Get list of TO DO list
		WebElement todoList = webDriver.findElement(By.xpath(getXpath("TC1.4")));
		List<WebElement> rows = todoList.findElements(By.tagName("li"));

		// If list has rows
		if (rows.size() != 0) {
			// iterate over rows in the list
			for (WebElement rowWebElement : rows) {
				// If checkBox is enabled
				if (rowWebElement.findElement(By.tagName("input")).isEnabled()) {
					if (rowWebElement.findElement(By.tagName("input")).isSelected()) {
						// Un-Mark check box
						rowWebElement.findElement(By.tagName("input")).click();
					}
				}
			}
		}
	}

	/**
	 * This method clicks on given WebElement
	 * 
	 * @param webElement - WebElement to be clicked on
	 */
	public static void clickOnWebElement(WebElement webElement) {

		Actions builder = new Actions(webDriver);
		Actions action = builder.moveToElement(webElement).click();
		action.perform();
	}

	/**
	 * This method inputs test in text field
	 * 
	 * @param webElement - Text Field WebElement
	 * @param inputText  - input text
	 */
	public static void inputTextWebElement(WebElement webElement, String inputText) {

		Actions builder = new Actions(webDriver);
		Actions action = builder.moveToElement(webElement).click().sendKeys(inputText);
		action.perform();
	}

	/**
	 * This method selects an option from a drop down menu
	 * 
	 * @param webElement        - DropDown WebElement
	 * @param xPath             - xPath to DropDown Element
	 * @param selectOptionValue - Value to be selected from drop-down enu
	 */

	public static void selectFromDropDown(WebElement webElement, String xPath, String selectOptionValue) {
		try {
			// Wait for 3 seconds
			WebDriverWait wait = new WebDriverWait(webDriver, 3);

			// Wait until drop down menu loads and size is more than 1
			wait.until((ExpectedCondition<Boolean>) new ExpectedCondition<Boolean>() {
				public Boolean apply(WebDriver driver) {
					Select select = new Select(driver.findElement(By.xpath(xPath)));
					return select.getOptions().size() > 1;
				}
			});

			// Select value
			Select select = new Select(webDriver.findElement(By.xpath(xPath)));
			select.selectByVisibleText(selectOptionValue);
		} catch (Throwable e) {

		}
	}

	/**
	 * This method recives RGB color and return it as a hex color
	 * 
	 * @param rgba - given Rgba color
	 * @return - hex
	 */

	public static String rgbaToHex(String rgba) {
		// rgba(0, 255, 0, 1)

		// Start string from index 5
		String s1 = rgba.substring(5); // 0, 255, 0, 1)

		// Replace ( with space
		rgba = s1.replace(')', ' '); // 0, 255, 0, 1

		// Trim and get colors
		StringTokenizer st = new StringTokenizer(rgba);
		int r = Integer.parseInt(st.nextToken(",").trim());
		int g = Integer.parseInt(st.nextToken(",").trim());
		int b = Integer.parseInt(st.nextToken(",").trim());

		Color c = new Color(r, g, b);
		String hex = "#" + Integer.toHexString(c.getRGB()).substring(2);
		return hex;
	}

	/**
	 * This method accepts given date and return true if given date is past present
	 * time
	 * 
	 * @param date - given date
	 * @return - true is time is past, false otherwise
	 * @throws ParseException
	 */
	public static boolean checkDate(String date) throws ParseException {
		// Clean date string
		String taskDueDate = date.substring((date.indexOf("(")) + 1, date.length() - 1);

		// Get calendar
		Calendar rightNow = Calendar.getInstance();

		// Get current date
		String currentDate = rightNow.get(Calendar.MONTH + 1) + "/" + rightNow.get(Calendar.DAY_OF_MONTH) + "/"
				+ rightNow.get(Calendar.YEAR);
		// Create formatter
		SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");

		// Return true if given date is past present time
		return formatter.parse(taskDueDate).after(formatter.parse(currentDate));
	}
	
	/**
	 * This method clears text-field's current text
	 * @param textField
	 */
	public static void clearTextField(WebElement textField) {
		Actions builder = new Actions(webDriver);
		Actions action = builder.moveToElement(textField).click().sendKeys(Keys.BACK_SPACE);

		action.perform();
	}
	
}
