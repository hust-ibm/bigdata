package com.hust.algorithm.canopy;

import java.util.ArrayList;
import java.util.List;

import com.hust.distance.CosDistance;
import com.hust.utils.CosSimilarity;

/**
 * Canopy聚类算法
 * @author tankai
 *
 */
public class Canopy {

	//原始文本
	private List<String> datalist;
	//分词后的文本
	private List<String[]> seglist;
	
	//原始文本对应向量
	private List<double[]> vectors;
	
	//聚类结果对应的下标
	private List<List<Integer>> resultIndex;

	public List<List<Integer>> getResultIndex() {
		return resultIndex;
	}

	public void setResultIndex(List<List<Integer>> resultIndex) {
		this.resultIndex = resultIndex;
	}

	//Canopy初始阈值
	private double T = 0f;
	
	//聚类结果类别数量
	private int canopy = 0;
	
	public List<String> getDatalist() {
		return datalist;
	}

	public void setDatalist(List<String> datalist) {
		this.datalist = datalist;
	}

	public List<String[]> getSeglist() {
		return seglist;
	}

	public void setSeglist(List<String[]> seglist) {
		this.seglist = seglist;
	}

	public List<double[]> getVectors() {
		return vectors;
	}

	public void setVectors(List<double[]> vectors) {
		this.vectors = vectors;
	}

	public double getT() {
		return T;
	}

	public void setT(double T) {
		this.T = T;
	}

	public int getCanopy() {
		return canopy;
	}

	//canopy算法 
	public void cluster(){		
		//
		CosDistance cosDistance = new CosDistance(vectors);
		setT(cosDistance.getThreshold());
		//
		resultIndex = new ArrayList<List<Integer>>();
		List<Integer> tmpIndex = null;
		
		//遍历向量集合
		for(int i = 0 ; i < vectors.size() ; i++){
//			double[] vector = vectors.get(i);
			//i = 0 一个类都没有时，直接添加进resultIndex。
			if(i == 0){
				//把第1个向量的索引添加到tmpIndex，作为第一个类
				tmpIndex = new ArrayList<Integer>();
				tmpIndex.add(i);
				//把第一个类加入到resultIndex
				resultIndex.add(tmpIndex);
				continue;
			}
			
			//找到符合相似度要求的类的标志
			boolean isFind = false;
			
			for(int j = 0 ; j < resultIndex.size() ; j++){
				//得到第j个类的向量组
//				List<double[]> tmpVectors = getTmpVector(resultIndex.get(j));
//				CosSimilarity sim = new CosSimilarity();
				//计算向量与已分的类的向量平均值是否大于阈值，大于则添加到该类
				if(cosDistance.getDistance(i, resultIndex.get(j)) > T){
					//获取待比较的那个类的所有元素的索引， 存放在tmpIndex
					tmpIndex = resultIndex.get(j);
					
					//把i(对应向量的索引值)加入到tmpIndex
					tmpIndex.add(i);
					
					//从resultIndex里删除旧的待比较的那个类
					resultIndex.remove(j);
					
					//添加加入了i的新类到resultIndex
					resultIndex.add(tmpIndex);
					//已加入相似类，退出当前循环
					isFind = true;
					break;
				}
			}
			
			//与前面的类相似度都不符合要求则新建一个类
			if(!isFind){
				tmpIndex = new ArrayList<Integer>();
				tmpIndex.add(i);
				resultIndex.add(tmpIndex);
			}
		}
		//获取聚类的数量
		canopy = resultIndex.size();
		
	}	
	
}