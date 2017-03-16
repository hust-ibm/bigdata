package com.hust.algorithm.bayes;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.hust.algorithm.bayes.Training.ClusterInfo;
import com.hust.segmentation.AnsjSegmentation;
import com.hust.utils.ExcelReader;
import com.hust.utils.ExcelWriter;

/**
 * 朴素贝叶斯分类
 * 
 * @author Chan
 *
 */
public class NativeBayes {
	/**
	 * 存储各类的信息
	 */
	ClusterInfo clusterInfo;
	/**
	 * 各类簇的先验概率。有n个类，double数组长度就为n。
	 */
	private double[] priorPRsOfCluster;
	/**
	 * 各类簇下单词的条件概率。有n个类，double数组长度就为n。
	 */
	private HashMap<String, double[]> wordConitionalPRMap;

	public NativeBayes() {
		priorPRsOfCluster = null;
		wordConitionalPRMap = new HashMap<String, double[]>();
	}

	public void training(String filePath, int index) {
		Training training = new Training(filePath, index);
		clusterInfo = training.getClusterInfo();
		priorPRsOfCluster = training.priorPRsOfCluster();
		wordConitionalPRMap = training.getWordConditionalPRMap();
		for(Map.Entry<String, double[]> map : wordConitionalPRMap.entrySet()){
			System.out.print(map.getKey()+" ");
			for(double d:map.getValue()){
				System.out.print(d);
			}
			System.out.println();
		}
	}

	private int classifySingleData(String[] cell) {
		// 文档d属于类Ci的概率
		double[] prOfDBelongsToCi = new double[clusterInfo.getNumOfClusters()];
		for (int i = 0; i < prOfDBelongsToCi.length; i++) {
			// 使用分词将该cell分词
			for (String word : cell) {
				double[] values = wordConitionalPRMap.get(word);
				if (values != null) {
					prOfDBelongsToCi[i] += Math.log(values[i]);
				} else {
					prOfDBelongsToCi[i] += (double) 1
							/ (clusterInfo.getClusterTotalWords()[i] + clusterInfo.getWordBook());
				}
			}
		}

		int max = 0;
		prOfDBelongsToCi[0] += Math.log(priorPRsOfCluster[0]);
		for (int i = 1; i < prOfDBelongsToCi.length; i++) {
			prOfDBelongsToCi[i] += Math.log(priorPRsOfCluster[i]);
			if (prOfDBelongsToCi[max] < prOfDBelongsToCi[i]) {
				max = i;
			}
		}

		return max;
	}

	public void classify(String fileName, int index) {
		//读取excel
		List<List<String>> rawData = ExcelReader.read(fileName);
		List<String> specifiedColData = ExcelReader.read(fileName, index);
		AnsjSegmentation seg = new AnsjSegmentation();
		seg.setWordList(specifiedColData);
		seg.segment();
		List<String[]> segList = seg.getSegList();
		for (int i = 0; i < segList.size(); i++) {
			int ci = classifySingleData(segList.get(i));
			ExcelWriter.rowListToExcel("result/classify/"+clusterInfo.getClustersName().get(ci), rawData.get(i));
		}
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		NativeBayes bayes = new NativeBayes();
		bayes.training("result/cluster", 0);
		bayes.classify("C:/Users/Chan/Desktop/1.xls", 0);
		}

}
