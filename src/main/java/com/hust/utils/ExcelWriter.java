package com.hust.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * 数据导入到Excel工具类
 * 支持List<String>,List<List<String>>格式数据
 * @author tankai
 *
 */
public class ExcelWriter {

	/**
	 * 功能：多行多列导入到Excel并且设置标题栏格式
	 * 如果文件存在就在后面追加
	 * @param fileName 输出的excel文件名
	 * @param rows	行数
	 * @param cells	列数
	 * @param value 要输出的数据，二维数组Object[][]
	 */
    public static void writeToExcel(String fileName,int rows,int cells,Object [][]value){
    	
    	//new 文件
        FileOutputStream fos=null;
        File f=new File(fileName);
    	
        Workbook wb = null;
        Sheet sheet = null;
        
        int lastRowNum = 0;
        		
        if(f.exists()){
        	try {
        		FileInputStream fs = new FileInputStream(fileName);  //获取文件
        		POIFSFileSystem ps;
			
				ps = new POIFSFileSystem(fs);
				//使用POI提供的方法得到excel的信息  
				if (fileName.endsWith("xls")) {
					wb = new HSSFWorkbook(fs);
					
				} else if (fileName.endsWith("xlsx")) {
					wb = new XSSFWorkbook(fs);
				} else {
					System.out.println();
					
				}
				
				sheet =  wb.getSheetAt(0);  //获取到工作表，因为一个excel可能有多个工作表  
				
				//如果表格存在就获取行数
				lastRowNum = sheet.getLastRowNum() + 1;
				
				ps.close();
				fs.close();
        	} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
        }else{
        	wb = new HSSFWorkbook();
        	sheet = wb.createSheet();
        }
        
        //设置表格默认宽度
//        sheet.setDefaultColumnWidth(10*256);
        
        Row row = null; 
        
         for(int i = 0 ; i < rows ; i++){
            //
        	 row = sheet.createRow(lastRowNum+i);
             for(int j= 0 ; j<cells ; j++){
                //
            	 row.createCell(j).setCellValue(convertString(value[i][j]));                 
             } 
         }
        
         //数据写入文件
         try {
             fos=new FileOutputStream(f);
             wb.write(fos);
                 
         } catch (FileNotFoundException e) {
            System.out.println("导入数据前请关闭工作表");

          } catch ( Exception e) {
         	 System.out.println("没有进行筛选");

          } finally{
             try {
                 if(fos!=null){
                     fos.close();
                 }
             } catch (IOException e) {
              }
         }
    }
	
	
    /**
     * 将List<List<String>>类型数据写入Excel文件
     * @param fileName 文件名
     * @param list 数据list
     */
    public static void twolistToExcel(String fileName,List<List<String>> list){
    	int rows = list.size();
    	int cols = 0;
    	for(int i = 0 ; i < list.size() ; i++){
    		if(list.get(i).size() > cols){
    			cols = list.get(i).size();
    		}
    	}
    	System.out.println(rows+";"+cols);
    	Object[][] value = new Object[rows][cols];
    	
    	for(int i = 0 ; i < rows ; i++){
    		
    		for(int j = 0 ; j < cols ; j++){
    			if(list.get(i).size() <= j){
    				value[i][j] = "";
    				continue;
    			}
    			if(list.get(i).get(j) != null || !list.get(i).get(j).equals("")){
    				value[i][j] = list.get(i).get(j);
    			}else{
    				value[i][j] = "";
    			}
    		}
    	}
    	
    	writeToExcel(fileName, rows, cols, value);
    }
    
    
    /**
     * 将List<String>类型数据写入Excel文件
     * 按列写入，即把数据写到第一列
     * @param fileName 文件名
     * @param list 数据list
     */
    public static void colListToExcel(String fileName,List<String> list){
    	int rows = list.size();
    	int cols = 1;
    	    	
    	Object[][] value = new Object[rows][cols];
    	
    	for(int i = 0 ; i < rows ; i++){
    		    		
			if(list.get(i) != null || !list.get(i).equals("")){
				value[i][0] = list.get(i);
			}else{
				value[i][0] = "";
			}
    	}    	
    	
    	writeToExcel(fileName, rows, cols, value);
    }
    
    /**
     * 将List<String>类型数据写入Excel文件
     * 按行写入，即把数据写到第一行
     * @param fileName
     * @param list
     */
    public static void rowListToExcel(String fileName,List<String> list){
    	int cols = list.size();
    	int rows = 1;
    	    	
    	Object[][] value = new Object[rows][cols];
    	
    	for(int i = 0 ; i < cols ; i++){
    		    		
			if(list.get(i) != null || !list.get(i).equals("")){
				value[0][i] = list.get(i);
			}else{
				value[0][i] = "";
			}
    	}    	
    	
    	writeToExcel(fileName, rows, cols, value);
    }
    
    public static String convertString(Object value) {
        if (value == null) {
            return "";
        } else {
            return value.toString();
        }
    }

    
}
