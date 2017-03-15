package com.hust.entity;

import java.util.HashMap;
import java.util.List;

/**
 * 聚类后的结果实体
 * @author Chan
 *
 */
public class ClusterResult {
	
	private HashMap<String, List<String>> resultMap;

	public HashMap<String, List<String>> getResultMap() {
		return resultMap;
	}

	public void setResultMap(HashMap<String, List<String>> resultMap) {
		this.resultMap = resultMap;
	}
	
}
