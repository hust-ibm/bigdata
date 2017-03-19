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
	
	private static long start;
	private static long end;
	
	public static void main(String[] args) {
		start = System.currentTimeMillis();
		//读取原始数据
		List<String> dataList = ExcelReader.read("E:/测试数据/聚类/原始数据/前程无忧9000一列.xls",0);
		end = System.currentTimeMillis();
		System.out.println("读数据耗时："+(end-start));
//		ClusterUtil.showDatalist(dataList);
		//分词
		AnsjSegmentation ansj = new AnsjSegmentation();
		ansj.setWordList(dataList);
		ansj.segment();
		
		//得到分词后的List集合
		List<String[]> seglist = ansj.getSegList(); 
//		ClusterUtil.showSeglist(seglist);
		
		//向量转换
		TFIDFConvertor convertor = new TFIDFConvertor(seglist);
		List<double[]> vectors = convertor.getVector();
		Canopy canopy = new Canopy();
		canopy.setVectors(vectors);

		//设置阈值
//		canopy.setT(0.15);
		start = System.currentTimeMillis();
		//进行Canopy聚类
		canopy.cluster();
		end = System.currentTimeMillis();
		System.out.println("聚类耗时："+(end-start));
		//查看canopy阈值
		System.out.println("计算的平均阈值："+canopy.getT());
		
		
		//聚类结果显示到控制台
//		ClusterUtil.showResult(canopy.getResultIndex(), dataList);
		System.out.println("聚类个数："+canopy.getCanopy());

		//聚类结果写入到文件
		List<List<String>> clusterlist = ClusterUtil.getClusters(canopy.getResultIndex(), dataList);
		
		//
		ClusterUtil.delFolder(Config.CANOPY_RESULT_PATH);
		//把每个类的结果输出到一个Excel文件
		for(int i = 0 ; i < clusterlist.size() ; i++){
			//
			String fileName = ClusterUtil.stringFilter(clusterlist.get(i).get(0));
			ExcelWriter.colListToExcel(Config.CANOPY_RESULT_PATH+
					fileName+".xls", clusterlist.get(i));
			
		}
	}
}