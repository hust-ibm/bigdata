package com.hust.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class TxtReader {
	// 从txt文本中获取语句集合
	public List<String> getDataFromTxt(String name) {
		List<String> list = new ArrayList<String>();
		File file = new File(name);
		try {
			if (file.isFile() && file.exists()) {

				InputStreamReader read = new InputStreamReader(new FileInputStream(file), "UTF8");
				BufferedReader buff = new BufferedReader(read);
				String lineTxt = null;
				while ((lineTxt = buff.readLine()) != null) {
					if (lineTxt.isEmpty())
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

	// 将List<String>写入txt文档不覆盖
	public int writeToTxt(String words, String filePath) {
		File file = new File(filePath);
		try {
			if (file.exists()) {
				System.out.println("文件已存在，内容将追加到已存在的文件中");
			} else {
				System.out.println("文件不存在，将创建文件");
				file.createNewFile();
			}
			BufferedReader input = new BufferedReader(new FileReader(file));
			// 存放原有内容
			String oldContent = "";
			String str = "";
			while ((str = input.readLine()) != null) {
				oldContent += str + "\n";
			}
			input.close();
			String newContent = oldContent + words;

			BufferedWriter output = new BufferedWriter(new FileWriter(file));
			output.write(newContent);
			output.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			System.out.println(e);
			return 0;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println(e);
			return 0;
		}
		return 1;
	}
	//写文件，覆盖
	public int write2Txt(String words, String filePath) {
		File file = new File(filePath);
		try {
			if (file.exists()) {
				System.out.println("文件已存在，将覆盖文件");
			} else {
				System.out.println("文件不存在，将创建文件");
				file.createNewFile();
			}			
			BufferedWriter output = new BufferedWriter(new FileWriter(file));
			output.write(words);
			output.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			System.out.println(e);
			return 0;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println(e);
			return 0;
		}
		return 1;
	}
}
