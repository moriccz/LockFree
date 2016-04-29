package common;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelWriter {

	public static void writeStudentsListToExcel(String FILE_PATH, List<HashMap<String, String>> studentList) {

		// Using XSSF for xlsx format, for xls use HSSF

		Workbook workbook = new XSSFWorkbook();

		Sheet studentsSheet = workbook.getSheet("RemoveSearch");

		int rowIndex = 0;
		if (studentsSheet == null)
			studentsSheet = workbook.createSheet("RemoveSearch");

		Row row = studentsSheet.getRow(1);
		while (row != null) {
			rowIndex++;
			row = studentsSheet.getRow(rowIndex);
		}

		for (HashMap<String, String> student : studentList) {

			row = studentsSheet.createRow(rowIndex++);

			int cellIndex = 0;

			row.createCell(cellIndex++).setCellValue(Double.parseDouble(student.get("seconds")));
			row.createCell(cellIndex++).setCellValue(Double.parseDouble(student.get("size")));
			row.createCell(cellIndex++).setCellValue(Double.parseDouble(student.get("failCount")));
		}

		// write this workbook in excel file.

		try {
			FileOutputStream fos = new FileOutputStream(FILE_PATH);

			workbook.write(fos);

			fos.close();

			System.out.println(FILE_PATH + " is successfully written");

		} catch (FileNotFoundException e) {

			e.printStackTrace();

		} catch (IOException e) {

			e.printStackTrace();

		}

	}

}
