package com.hust.bigdata;

import java.util.List;

import com.hust.algorithm.canopy.Canopy;
import com.hust.algorithm.kmeans.KMeans;
import com.hust.convertor.TFIDFConvertor;
import com.hust.utils.ExcelWriter;
import com.hust.segmentation.AnsjSegmentation;
import com.hust.utils.Config;
import com.hust.utils.ExcelReader;
import com.hust.utils.ClusterUtil;


public class KMeansTest{
	private static long start;
	private static long end;
	
	public static void main(String[] args) {
		start = System.currentTimeMillis();	
		//读取原始数据
		List<String> dataList = ExcelReader.read("E:/测试数据/聚类/原始数据/前程无忧一列数据.xls",0);
		
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
		
		
		//进行Canopy聚类
		canopy.cluster();
		
		//查看canopy阈值
		System.out.println("计算的平均阈值："+canopy.getT());
//		
//		
//		//聚类结果显示到控制台
//		ClusterUtil.showResult(canopy.getResultIndex(), dataList);
//		System.out.println("聚类个数："+canopy.getCanopy());
//
//		
		//初始化KMeans聚类参数 （K值--Canopy聚类的个数，向量集合，迭代次数）
		KMeans kmeans = new KMeans(10, vectors, 20);
		
		//进行KMeans聚类
		kmeans.cluster();
		//聚类结果显示到控制台
		ClusterUtil.showResult(kmeans.getResultIndex(), dataList);
		
		//聚类结果写入到文件
		List<List<String>> clusterlist = ClusterUtil.getClusters(kmeans.getResultIndex(), dataList);
		
		//
		ClusterUtil.delFolder(Config.KMEANS_RESULT_PATH);
		//把每个类的结果输出到一个Excel文件
		for(int i = 0 ; i < clusterlist.size() ; i++){
			//
			String fileName = ClusterUtil.stringFilter(clusterlist.get(i).get(0));
			
			ExcelWriter.colListToExcel(Config.KMEANS_RESULT_PATH+
					fileName+".xls", clusterlist.get(i));
			
		}
		
		end = System.currentTimeMillis();
		System.out.println("聚类总耗时："+(end-start)/1000+"s");
	}
}