package com.hust.bigdata;

import java.util.ArrayList;
import java.util.List;

import com.hust.utils.ExcelWriter;



public class ExcelTest {

	public static void main(String[] args) {
		List<String> test = new ArrayList<>();
		test.add("111");
		test.add("222");
		test.add("333");
				
		ExcelWriter.rowListToExcel("E:\\四川项目\\测试数据\\list.xlsx", test);
	}
}
