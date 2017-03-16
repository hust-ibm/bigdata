package com.hust.convertor;

import java.util.ArrayList;
import java.util.List;

public class TFIDFConvertor{
	//分词结果集
	private List<String[]> segList;
	//构成向量模板的基本单词集合
	private List<String> vectorBase;
	
	
	public TFIDFConvertor() {
		super();
	}
	public TFIDFConvertor(List<String[]> list){
		super();
		segList = list;
		Init();
	}

	public void setSegList(List<String[]> segList) {
		this.segList = segList;
		Init();
	}
	//初始化 向量模板的单词集合
	private void Init(){
		vectorBase = new ArrayList<String>();
		for (String[] array : segList) {
			for (String word : array) {
				//如果该词语不再集合内，则加入集合
				if(!vectorBase.contains(word)){
					vectorBase.add(word);
				}
			}
		}
	}
	
	//根据分词后的list集合得到每个文本的向量
	public List<double[]> getVector() {
		// TODO Auto-generated method stub
		List<double[]> list = new ArrayList<double[]>();
	//	List<String[]> filterList = Filter(segList);
		List<String[]> filterList = segList;
		for (String[] array : filterList) {
			//生成向量模版
			double[] vector = new double[vectorBase.size()];
			for (String word : array) {
				//计算tf值
				double tf = (double)CountInArticle(word,array)/(double)array.length;
				//计算idf值
				double idf = Math.log((float)filterList.size()/((float)CountInCorpus(word)+1));
				//将tfidf值存入向量模板中
				vector[vectorBase.indexOf(word)] = tf*idf;
			}
			//添加向量到集合中（存放顺序与文本分词存放顺序一致，方便后面的数据处理）
			list.add(vector);
		}
		return list;
	}
//根据给定词 得到包含该词的文档的个数
	private int CountInCorpus(String word) {
		// TODO Auto-generated method stub
		int count = 0;
		for (String[] array : segList) {
			for (int i = 0; i < array.length; i++) {
				if(array[i].equals(word)){
					count++;
					break;
				}
			}
		}
		return count;
	}
//根据给定词和文档，得到该词在文档中的个数
	private int CountInArticle(String word, String[] array) {
		// TODO Auto-generated method stub
		int count = 0;
		for (int i = 0; i < array.length; i++) {
			if(array[i].equals(word)){
				count++;
			}
		}
		return count;
	}

//	private List<String[]> Filter(List<String[]> segList) {
//		// TODO Auto-generated method stub
//		List<String[]> result = new ArrayList<String[]>();
//        if (null == segList || segList.size() == 1) {
//            return result;
//        }
//        for (String[] array : segList) {
//            List<String> tmpList = new ArrayList<String>();
//            for (String word : array) {
//                if (!tmpList.contains(word)) {
//                    tmpList.add(word);
//                }
//            }
//            String[] newArray = (String[]) tmpList.toArray(new String[tmpList.size()]);
//            result.add(newArray);
//        }
//        return result;
//	}

}
