package com.hust.utils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import com.hust.algorithm.bayes.Training.ClusterInfo;

public class TxtWriter {
	/**
	 * 将先验概率写入result/classify/训练器输出/训练集先验概率.txt中
	 */
	public static void writePriorPRToTxt(ClusterInfo clusterInfo, double[] priorPRsOfCluster) {
		try {
			File dir = new File("result/classify/训练器输出");
			if (!dir.exists()) {
				dir.mkdirs();
			}
			File file = new File("result/classify/训练器输出/训练集先验概率.txt");
			FileWriter fw = new FileWriter(file, false);
			PrintWriter pw = new PrintWriter(fw);
			for (int i = 0; i < priorPRsOfCluster.length; i++) {
				pw.print(clusterInfo.getClustersName().get(i) + "\t");
				pw.println(String.valueOf(priorPRsOfCluster[i]));
			}
			pw.close();
			fw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 将所有单词在某类下的条件概率写入result/classify/训练器输出/训练集条件概率.txt中
	 */
	public static void writeWordConditionalPRToTxt(HashMap<String, double[]> wordConditionalPRMap) {
		try {
			File dir = new File("result/classify/训练器输出");
			if (!dir.exists()) {
				dir.mkdirs();
			}
			File file = new File("result/classify/训练器输出/训练集条件概率.txt");
			FileWriter fw = new FileWriter(file, false);
			PrintWriter pw = new PrintWriter(fw);
			for (Map.Entry<String, double[]> map : wordConditionalPRMap.entrySet()) {
				pw.print(map.getKey()+"\t");
				for(double d:map.getValue()){
					pw.print(String.valueOf(d)+"\t");
				}
				pw.println();
			}
			pw.close();
			fw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
