package com.hust.bigdata;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.hust.utils.ClusterUtil;
import com.hust.utils.Config;
import com.hust.utils.ExcelWriter;



public class ExcelTest {

	public static void main(String[] args) {
		List<String> test = new ArrayList<>();
		test.add("111");
		test.add("222");
		test.add("333");
				
//		ExcelWriter.rowListToExcel("E:/四川项目/测试数据/list.xls", test);
//		ClusterUtil.delFolder(Config.CANOPY_RESULT_PATH);
		
		String s = "销售助理/文员";
		// 清除掉所有特殊字符 
		String regEx="[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]"; 
		Pattern p = Pattern.compile(regEx); 
		Matcher m = p.matcher(s);
		s = m.replaceAll("").trim();
		System.out.println(s);
		
	}
}
