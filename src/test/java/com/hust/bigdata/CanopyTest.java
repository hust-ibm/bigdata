package com.hust.bigdata;

import java.util.List;

import com.hust.algorithm.canopy.Canopy;
import com.hust.convertor.TFIDFConvertor;
import com.hust.utils.ExcelWriter;
import com.hust.segmentation.AnsjSegmentation;
import com.hust.utils.Config;
import com.hust.utils.ExcelReader;
import com.hust.utils.ClusterUtil;


public class CanopyTest{
	
	public static void main(String[] args) {
			
		//读取原始数据
		List<String> dataList = ExcelReader.read("E:\\四川项目\\测试数据\\qianchengwuyou.xlsx",1);
		
//		ClusterUtil.showDatalist(dataList);
		//分词
		AnsjSegmentation ansj = new AnsjSegmentation();
		ansj.setWordList(dataList);
		ansj.segment();
		
		//得到分词后的List集合
		List<String[]> seglist = ansj.getSegList(); 
		ClusterUtil.showSeglist(seglist);
		
		//向量转换
		TFIDFConvertor convertor = new TFIDFConvertor(seglist);
		List<double[]> vectors = convertor.getVector();
		Canopy canopy = new Canopy();
		canopy.setVectors(vectors);
		
		
		//查看canopy阈值
		System.out.println("计算的平均阈值："+canopy.getT());
		
		//进行Canopy聚类
		canopy.cluster();
		
		//聚类结果显示到控制台
//		ClusterUtil.showResult(canopy.getResultIndex(), dataList);
		System.out.println("聚类个数："+canopy.getCanopy());

		//聚类结果写入到文件
		List<List<String>> clusterlist = ClusterUtil.getClusters(canopy.getResultIndex(), dataList);
		//把每个类的结果输出到一个Excel文件
		for(int i = 0 ; i < clusterlist.size() ; i++){
			//
			ExcelWriter.colListToExcel(Config.CANOPY_RESULT_PATH+"canopy"+(i+1)+".xlsx", clusterlist.get(i));
			
		}
	}
}