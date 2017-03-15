package com.hust.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class TxtReader {
	//从txt文本中获取语句集合
		public List<String> getDataFromTxt(String name) {
			List<String> list = new ArrayList<String>();
			File file = new File(name);
			try {
				if (file.isFile() && file.exists()) {

					InputStreamReader read = new InputStreamReader(
							new FileInputStream(file),"UTF8");
					BufferedReader buff = new BufferedReader(read);
					String lineTxt = null;
					while((lineTxt = buff.readLine()) != null){
						if(lineTxt.isEmpty())
							continue;
						list.add(lineTxt);
					}
					buff.close();
					read.close();

				} else {
					System.out.println("文件不存在！请确认输入的文件路径是否正确！");
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				System.out.println("文件读取出错！");
			}
			return list;
		}
}
