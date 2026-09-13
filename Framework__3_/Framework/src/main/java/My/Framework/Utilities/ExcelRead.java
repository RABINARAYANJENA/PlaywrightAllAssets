package My.Framework.Utilities;

import java.io.FileInputStream;
import java.util.Hashtable;

import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelRead {
	static Hashtable<String, String> table=new Hashtable<String, String>();
	public static String readExcelSuit() throws Exception {
		String testCaseID="";
		FileInputStream fi = new FileInputStream(System.getProperty("user.dir")+"\\TestData\\TestSuite.xlsx");
		XSSFWorkbook wb =new XSSFWorkbook(fi);
		XSSFSheet sheet = wb.getSheet("Suite");
		for(int i=0;i<sheet.getLastRowNum();i++) {
			if(sheet.getRow(i).getCell(1).getStringCellValue().equalsIgnoreCase("Yes")) {
				testCaseID = sheet.getRow(i).getCell(0).getStringCellValue();
			}
		}
		wb.close();
		return testCaseID;
	}
	
	public static void readExcelData(String testCaseID) throws Exception {
		FileInputStream fi = new FileInputStream(System.getProperty("user.dir")+"\\TestData\\TestData.xlsx");
		XSSFWorkbook wb =new XSSFWorkbook(fi);
		XSSFSheet sheet = wb.getSheet("Data");
		for(int i=0;i<sheet.getLastRowNum();i++) {
			if(sheet.getRow(i).getCell(0).getStringCellValue().equalsIgnoreCase(testCaseID)) {
				for(int j=0;j<sheet.getRow(i).getLastCellNum();j++) {
					table.put(sheet.getRow(0).getCell(j).getStringCellValue(), sheet.getRow(i).getCell(j).getStringCellValue());
				}
			}
		}
	}
	
	public static String getDataValue(String key) {
		String value=table.get(key);
		return value;
	}
}
