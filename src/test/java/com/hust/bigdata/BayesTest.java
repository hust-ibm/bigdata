package com.hust.bigdata;

import com.hust.algorithm.bayes.NativeBayes;

public class BayesTest {

	public static void main(String[] args) {
		NativeBayes bayes = new NativeBayes();
		// 对cluster目录下的若干类进行训练
		bayes.training("result/cluster", 0);
		// 利用训练集的结果对test.xls文件里的数据进行分类
		bayes.classify("C:/Users/Chan/Desktop/test.xls", 0);
	}

}
