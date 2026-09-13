package day2;

import java.nio.file.Paths;

import org.testng.annotations.Test;

import com.microsoft.playwright.Page;

import utils.BaseClass;

public class FileUpload extends BaseClass{
	
	
	@Test
	public void fileUploadTest() throws InterruptedException {
		System.out.println(browser);
	Page page=	browser.newPage();
	
	page.navigate("https://testautomationpractice.blogspot.com/");
	
	page.setInputFiles("#singleFileInput", Paths.get("pom.xml"));
	
	
	Thread.sleep(5000);
		
	}

}
