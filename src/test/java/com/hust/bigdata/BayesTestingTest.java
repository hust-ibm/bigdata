package com.hust.bigdata;

import com.hust.algorithm.bayes.NativeBayes;
import com.hust.utils.TxtWriter;

public class BayesTestingTest {

	public static void main(String[] args) {
		NativeBayes bayes = new NativeBayes();
		// 对cluster目录下的若干类进行训练
		bayes.training("result/classify/TrainingSet",1);
		TxtWriter.writePriorPRToTxt(bayes.getClusterInfo(), bayes.getPriorPRsOfCluster());
		TxtWriter.writeWordConditionalPRToTxt(bayes.getWordConitionalPRMap());
		// 利用训练集的结果对test.xls文件里的数据进行分类
		bayes.classify("C:/Users/Chan/Desktop/招聘.xls", 1);
	}

}
