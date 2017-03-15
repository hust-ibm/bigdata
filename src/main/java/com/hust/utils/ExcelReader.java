package com.hust.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * 读取excel工具类
 * 
 * @author Chan
 *
 */
public class ExcelReader {

	private ExcelReader() {
	}

	public static void main(String[] args) {
		List<String> r = read("C:\\Users\\Chan\\Desktop\\1.xlsx", 6);
		for (String str : r) {
			System.out.println(str);
		}
	}

	/**
	 * 读取excel文件，将excel文件过滤空行转换为list。限定一个excel文件只能拥有一个sheet。
	 * 
	 * @param filePath
	 *            完全限定文件名
	 * @return
	 */
	public static List<List<String>> read(String filePath) {
		List<List<String>> content = null;
		InputStream is = null;
		Workbook wb = null;
		try {
			// 判断文件是否为excel格式的文件
			is = new FileInputStream(filePath);
			if (filePath.endsWith("xls")) {
				wb = new HSSFWorkbook(is);
			} else if (filePath.endsWith("xlsx")) {
				wb = new XSSFWorkbook(is);
			} else {
				throw new Exception("读取的不是excel文件");
			}

			// 遍历excel，将结果存储在content中
			content = new ArrayList<List<String>>();
			Sheet sheet = wb.getSheetAt(0);
			List<String> rowList = null;
			for (int i = 0; i < sheet.getLastRowNum() + 1; i++) {
				Row row = sheet.getRow(i);
				if (row != null) {
					rowList = new ArrayList<String>();
					for (int j = 0; j < row.getLastCellNum(); j++) {
						if (row.getCell(j) == null) {
							rowList.add("");
						} else {
							rowList.add(row.getCell(j).getStringCellValue());
						}
					}
				}
				content.add(rowList);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (wb != null) {
					wb.close();
				}
				if (is != null) {
					is.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return content;
	}

	/**
	 * 读取excel文件的指定列，将excel文件过滤空行转换为list。限定一个excel文件只能拥有一个sheet。
	 * 
	 * @param filePath
	 *            文件路径
	 * @param index
	 *            指定列
	 * @return
	 */
	public static List<String> read(String filePath, int index) {
		List<String> content = null;
		InputStream is = null;
		Workbook wb = null;
		try {
			// 判断文件是否为excel格式的文件
			is = new FileInputStream(filePath);
			if (filePath.endsWith("xls")) {
				wb = new HSSFWorkbook(is);
			} else if (filePath.endsWith("xlsx")) {
				wb = new XSSFWorkbook(is);
			} else {
				throw new Exception("读取的不是excel文件");
			}

			// 遍历excel，将结果存储在content中
			content = new ArrayList<String>();
			Sheet sheet = wb.getSheetAt(0);
			int maxRowNum = 0;
			for (int i = 0; i < sheet.getLastRowNum() + 1; i++) {
				// 遍历每一行
				Row row = sheet.getRow(i);
				if (maxRowNum < row.getLastCellNum()) {
					maxRowNum = row.getLastCellNum();
				}
				// 过滤空行
				if (row != null) {
					if (index + 1 > maxRowNum) {
						System.out.println("读取的该行不存在。");
						return null;
					}

					if (row.getCell(index) != null) {
						content.add(row.getCell(index).getStringCellValue());
					} else {
						content.add("");
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (wb != null) {
					wb.close();
				}
				if (is != null) {
					is.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return content;
	}

}
