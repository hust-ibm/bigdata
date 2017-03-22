package com.hust.bigdata;

import javax.swing.filechooser.FileSystemView;

import com.hust.algorithm.bayes.NativeBayes;
import com.hust.utils.TxtWriter;

public class BayesTest {

	public static void main(String[] args) {
		NativeBayes bayes = new NativeBayes();
		// 对cluster目录下的若干类进行训练,按照excel的第二列进行分类
		bayes.training("result/classify/训练集", 1);
		
		// 将先验概率写入result/classify/训练器输出/训练集先验概率.txt中
		TxtWriter.writePriorPRToTxt(bayes.getClusterInfo(), bayes.getPriorPRsOfCluster());
		// 将所有单词在某类下的条件概率写入result/classify/训练器输出/训练集条件概率.txt中
		TxtWriter.writeWordConditionalPRToTxt(bayes.getWordConitionalPRMap());
		
		// 利用训练集的结果对test.xls文件里的数据进行分类
		//给定的test.xls文件放在桌面上，或者别的地方(那么请修改下列路径参数)
		bayes.classify(FileSystemView.getFileSystemView().getHomeDirectory()+"/test.xls", 1);
	}

}
