package com.hust.segmentation;

import java.util.ArrayList;
import java.util.List;

import org.ansj.domain.Result;
import org.ansj.domain.Term;
import org.ansj.recognition.Recognition;
import org.ansj.recognition.impl.StopRecognition;
import org.ansj.splitWord.analysis.ToAnalysis;

import com.hust.utils.Config;
import com.hust.utils.TxtReader;

public class AnsjSegmentation extends Segmentation {
	//停用词表的存放路径
	private String stopWordsPath = Config.stopWordPath;
	//用来存放未过滤停用词的结果
	private List<String[]> listWithoutFilter = new ArrayList<String[]>();

	//分词实现（精准分词）
	@Override
	public void segment() {
		// TODO Auto-generated method stub
		Recognition recognition = getStopWordsFilter();
		//循环，处理一句一句的分词
		for (String word : wordList) {
			//用来存放分词后的结果
			Result result = new Result(null);
			//用精准分词模式得到一个未过滤的记过
			result = ToAnalysis.parse(word);
			//将result去掉词性后（toStringWithOutNature）按（","）切分为一个一个的单词
			listWithoutFilter.add(result.toStringWithOutNature().split(","));
			//过滤停用词
			result = result.recognition(recognition);
			segList.add(result.toStringWithOutNature().split(","));
		}
	}

	private Recognition getStopWordsFilter(){
		List<String> list = new TxtReader().getDataFromTxt(stopWordsPath);
		StopRecognition filter = new StopRecognition();
		filter.insertStopWords(list);
		System.out.println("停用词加载成功！");
		return filter;
	}

	public List<String[]> getListWithoutFilter() {
		return listWithoutFilter;
	}
	//将给定string经过过滤停用词分词为一个str数字（一个一个的词语）
	@Override
	public String[] segment(String str) {
		// TODO Auto-generated method stub		
		return ToAnalysis.parse(str).recognition(getStopWordsFilter()).toStringWithOutNature().split(",");
	}
	
	

}
