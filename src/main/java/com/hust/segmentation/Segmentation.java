package com.hust.segmentation;

import java.util.ArrayList;
import java.util.List;

public abstract class Segmentation {
	protected List<String> wordList;
	protected List<String[]> segList;
	//初始化变量
	public Segmentation(){
		wordList  = new ArrayList<String>();
		segList = new ArrayList<String[]>();
	}
	public List<String[]> getSegList() {
		return segList;
	}
	public void setWordList(List<String> wordList) {
		this.wordList = wordList;
	}
	
	public abstract void segment();
	
}
