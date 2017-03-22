package com.hust.algorithm.bayes;

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

	/**
	 * 构造函数，对类成员变量进行初始化
	 */
	public NativeBayes() {
		priorPRsOfCluster = null;
		wordConitionalPRMap = new HashMap<String, double[]>();
	}

	public ClusterInfo getClusterInfo() {
		return clusterInfo;
	}

	public double[] getPriorPRsOfCluster() {
		return priorPRsOfCluster;
	}

	public HashMap<String, double[]> getWordConitionalPRMap() {
		return wordConitionalPRMap;
	}

	/**
	 * 对训练集进行训练，将得到的类信息、先验概率、条件概率存储在本类中
	 */
	public void training(String filePath, int index) {
		// 利用Training类对训练集进行训练，filePath为测试集目录的绝对路径，index为读取excel
		Training training = new Training(filePath, index);
		clusterInfo = training.getClusterInfo();
		priorPRsOfCluster = training.priorPRsOfCluster();
		wordConitionalPRMap = training.getWordConditionalPRMap();
	}

	/**
	 * 对一条数据进行分类
	 */
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

	/**
	 * 对一个文件的数据进行分类
	 */
	public void classify(String fileName, int index) {
		HashMap<String,List<List<String>>> readyToWriteIn = new HashMap<String,List<List<String>>>();
		// 读取完整excel文件
		List<List<String>> rawData = ExcelReader.read(fileName);
		// 读取excel文件的指定一列
		List<String> specifiedColData = ExcelReader.read(fileName, index);
		// 对指定列进行分词
		AnsjSegmentation seg = new AnsjSegmentation();
		seg.setWordList(specifiedColData);
		seg.segment();
		List<String[]> segList = seg.getSegList();
		for (int i = 0; i < segList.size(); i++) {
			// 对该条数据利用训练集进行分类，返回类的索引
			int ci = classifySingleData(segList.get(i));
			String ciName = "result/classify/分类结果/" + clusterInfo.getClustersName().get(ci);
			List<List<String>> value = readyToWriteIn.get(ciName);
			if(value == null){
				value = new ArrayList<List<String>>();
				readyToWriteIn.put(ciName, value);
			}
			value.add(rawData.get(i));
			readyToWriteIn.put(ciName, value);
			// 将该条数据写入对应的类excel
		}
		for(Map.Entry<String, List<List<String>>> entry:readyToWriteIn.entrySet()){
			ExcelWriter.twolistToExcel(entry.getKey(), entry.getValue());
		}
	}
}
