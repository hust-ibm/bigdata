package com.hust.bigdata;

import java.util.ArrayList;
import java.util.List;

import com.hust.algorithm.canopy.Canopy;
import com.hust.convertor.TFIDFConvertor;
import com.hust.utils.ExcelWriter;
import com.hust.segmentation.AnsjSegmentation;
import com.hust.utils.Config;
import com.hust.utils.ExcelReader;
import com.hust.utils.ShowUtil;


public class CanopyTest{
	
	public static void main(String[] args) {
			
		//读取原始数据
		List<String> dataList = ExcelReader.read("E:\\四川项目\\测试数据\\qianchengwuyou.xlsx",1);
		
//		ShowUtil.showDatalist(dataList);
		//分词
		AnsjSegmentation ansj = new AnsjSegmentation();
		ansj.setWordList(dataList);
		ansj.segment();
		
		//得到分词后的List集合
		List<String[]> seglist = ansj.getSegList(); 
		ShowUtil.showSeglist(seglist);	
		//向量转换
		TFIDFConvertor convertor = new TFIDFConvertor(seglist);
		List<double[]> vectors = convertor.getVector();
		Canopy canopy = new Canopy();
		canopy.setVectors(vectors);
		
		
		//初始化canopy阈值
//		canopy.setThreshold(thre);
		System.out.println("计算的平均阈值："+canopy.getT());
		
		//进行Canopy聚类
		canopy.cluster();
		
		//聚类结果写入到文件
		ShowUtil.showResult(canopy.getResultIndex(), dataList);
		System.out.println("聚类个数："+canopy.getCanopy());

		
		List<List<String>> clusterlist = ShowUtil.getClusters(canopy.getResultIndex(), dataList);
		//把每个类的结果输出到一个Excel文件
		for(int i = 0 ; i < clusterlist.size() ; i++){
			ExcelWriter.colListToExcel(Config.CANOPY_RESULT_PATH+clusterlist.get(i).get(0)+".xlsx", clusterlist.get(i));
			//+clusterlist.get(i).get(0)+ "E:\\四川项目\\测试数据\\aa\\"
		}
	}
}