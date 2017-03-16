package com.hust.algorithm.bayes;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.hust.segmentation.AnsjSegmentation;
import com.hust.utils.ExcelReader;

/**
 * 对训练集进行训练
 * 
 * @author Chan
 *
 */
public class Training {
	/**
	 * 类簇信息
	 */
	private ClusterInfo clusterInfo;
	/**
	 * 各类簇的先验概率。有n个类，double数组长度就为n。
	 */
	private double[] priorPRsOfCluster;
	/**
	 * 各类簇下单词的条件概率。有n个类，double数组长度就为n。
	 */
	private HashMap<String, double[]> wordConditionalPRMap;

	/**
	 * 构造函数，进行初始化
	 */
	public Training(String fileDir, int index) {
		if (!init(fileDir, index)) {
			System.err.println("检查训练集文件夹结构是否合理。");
			System.exit(0);
		}
	}

	public class ClusterInfo {
		/**
		 * 类簇个数
		 */
		int numOfClusters;
		/**
		 * 各类下的单词总数
		 */
		int[] clusterTotalWords;
		/**
		 * 词典大小
		 */
		int wordBook;
		/**
		 * 存储各类簇的内容。
		 */
		List<List<String[]>> clustersContent;
		/**
		 * 存储各类簇的名字
		 */
		List<String> clustersName;

		public int getNumOfClusters() {
			return numOfClusters;
		}

		public void setNumOfClusters(int numOfClusters) {
			this.numOfClusters = numOfClusters;
		}

		public int[] getClusterTotalWords() {
			return clusterTotalWords;
		}

		public void setClusterTotalWords(int[] clusterTotalWords) {
			this.clusterTotalWords = clusterTotalWords;
		}

		public int getWordBook() {
			return wordBook;
		}

		public void setWordBook(int wordBook) {
			this.wordBook = wordBook;
		}

		public List<List<String[]>> getClustersContent() {
			return clustersContent;
		}

		public void setClustersContent(List<List<String[]>> clustersContent) {
			this.clustersContent = clustersContent;
		}

		public List<String> getClustersName() {
			return clustersName;
		}

		public void setClustersName(List<String> clustersName) {
			this.clustersName = clustersName;
		}
	}

	/**
	 * 获取各类单词的条件概率
	 */
	public HashMap<String, double[]> getWordConditionalPRMap() {
		HashMap<String, int[]> wordFrequencyMap = getWordFrequencyMap();
		// 字典大小
		clusterInfo.setWordBook(wordFrequencyMap.size());
		// 类别的词条总数集合
		int[] clusterTotalWords = new int[clusterInfo.getNumOfClusters()];
		// 遍历词频map的所有value值,统计各个类下的单词总数
		for (int[] value : wordFrequencyMap.values()) {
			for (int i = 0; i < clusterInfo.getNumOfClusters(); i++) {
				clusterTotalWords[i] += value[i];
			}
		}
		clusterInfo.setClusterTotalWords(clusterTotalWords);
		// 遍历词频map，运用条件概率公式将条件概率求出并存储在wordConPMap中
		for (Map.Entry<String, int[]> entry : wordFrequencyMap.entrySet()) {
			String word = entry.getKey();
			int[] freqs = entry.getValue();
			double[] conPs = new double[clusterInfo.getNumOfClusters()];
			for (int i = 0; i < clusterInfo.getNumOfClusters(); i++) {
				conPs[i] = (double) (freqs[i] + 1) / (clusterTotalWords[i] + clusterInfo.getWordBook());
			}
			wordConditionalPRMap.put(word, conPs);
		}
		return wordConditionalPRMap;
	}

	/**
	 * 统计各类簇的词频
	 */
	private HashMap<String, int[]> getWordFrequencyMap() {
		HashMap<String, int[]> wordFreqMap = new HashMap<String, int[]>();
		for (int i = 0; i < clusterInfo.getNumOfClusters(); i++) {
			List<String[]> splitClusterContent = clusterInfo.getClustersContent().get(i);
			// 遍历该类下的每一条分词结果集
			for (String[] words : splitClusterContent) {
				// 遍历每一个分词结果集的单个单词
				for (String word : words) {
					if (wordFreqMap.get(word) != null) {
						wordFreqMap.get(word)[i]++;
					} else {
						int[] wordCount = new int[clusterInfo.getNumOfClusters()];
						wordCount[i] = 1;
						wordFreqMap.put(word, wordCount);
					}
				}
			}
		}

		return wordFreqMap;
	}

	/**
	 * 获取类的信息
	 */
	public ClusterInfo getClusterInfo() {
		return clusterInfo;
	}

	/**
	 * 获取各类簇的先验概率
	 */
	public double[] priorPRsOfCluster() {
		return priorPRsOfCluster;
	}

	/**
	 * 初始化
	 */
	private boolean init(String fileDir, int index) {
		File rootDir = new File(fileDir);
		if (!rootDir.isDirectory()) {
			return false;
		}
		File[] clusters = rootDir.listFiles();

		clusterInfo = new ClusterInfo();
		//获取类的个数
		clusterInfo.setNumOfClusters(clusters.length);
		this.priorPRsOfCluster = new double[clusters.length];
		this.wordConditionalPRMap = new HashMap<String, double[]>();

		//dataSize表示所有类加起来有多少条数据
		int dataSize = 0;
		List<String> clustersName = new ArrayList<String>();
		List<List<String[]>> clustersContent = new ArrayList<List<String[]>>();
		for (File cluster : clusters) {
			if (!cluster.isFile()) {
				return false;
			}
			clustersName.add(cluster.getName());
			// 读取excel某列的内容
			List<String> clusterContent = ExcelReader.read(cluster.getAbsolutePath(), 
					index);
			if (clusterContent == null) {
				System.err.println("读取excel文件出错...");
				System.exit(0);
			}
			// 将该excel的每一行进行分词
			AnsjSegmentation seg = new AnsjSegmentation();
			seg.setWordList(clusterContent);
			seg.segment();
			clustersContent.add(seg.getSegList());
			dataSize += clusterContent.size();
		}
		//存储类的名字
		clusterInfo.setClustersName(clustersName);
		//存储类的内容
		clusterInfo.setClustersContent(clustersContent);

		for (int i = 0; i < clustersContent.size(); i++) {
			//计算类的先验概率，该类的数据数除以总的数据条数
			priorPRsOfCluster[i] = (double) clustersContent.get(i).size() / 
					dataSize;
		}

		return true;
	}
}
