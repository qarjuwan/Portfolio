package com.autotest.portfolio.setup;

import java.util.ArrayList;
import java.util.Properties;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;

public class Base {

	public static WebDriver driver = null;
	public static Properties CONFIG = null;
	public static int TIMEOUT = 30;
	public static ArrayList<String> searchTerms = null;
	public static ArrayList<String> searchLinks = null;
	public static ArrayList<String> searchsInfo = null;
	public static int linksNum;
	public static boolean loginSucc;
	public static JavascriptExecutor js;
}
