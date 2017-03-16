package com.hust.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;

/**
 * 数据导入到Excel工具类
 * 支持List<String>,List<List<String>>格式数据
 * @author tankai
 *
 */
public class ExcelWriter {

	/**
	 * 功能：多行多列导入到Excel并且设置标题栏格式
	 * @param fileName 输出的excel文件名
	 * @param rows	行数
	 * @param cells	列数
	 * @param value 要输出的数据，二维数组Object[][]
	 */
    public static void writeToExcel(String fileName,int rows,int cells,Object [][]value){
 
    	HSSFWorkbook wb = new HSSFWorkbook();
    	HSSFSheet sheet = wb.createSheet();
    	
         Row row[]=new HSSFRow[rows];
         Cell cell[]=new HSSFCell[cells];
     
         sheet.setColumnWidth(0, 20*256);
         
         HSSFCellStyle ztStyle =  (HSSFCellStyle)wb.createCellStyle();

         Font ztFont = wb.createFont();  
         ztFont.setBoldweight(Font.BOLDWEIGHT_BOLD);
         //ztFont.setItalic(true);                     // 设置字体为斜体字  
        // ztFont.setColor(Font.COLOR_RED);            // 将字体设置为“红色”  
         ztFont.setFontHeightInPoints((short)12);    // 将字体大小设置为18px  
         ztFont.setFontName("华文行楷");             // 将“华文行楷”字体应用到当前单元格上  
        // ztFont.setUnderline(Font.U_DOUBLE);
         ztStyle.setFont(ztFont);
         
         for(int i=0;i<row.length;i++){
             row[i]=sheet.createRow(i);

             for(int j=0;j<cell.length;j++){
                 cell[j]=row[i].createCell(j);
                 cell[j].setCellValue(convertString(value[i][j]));
                 
//                 第一行文字样式
//                 if(i==0)
//                   cell[j].setCellStyle(ztStyle);                  
             }
 
         }
         //
         FileOutputStream fos=null;
         File f=new File(fileName);
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
     * 将List<List<String>>类型数据写入Excel文件
     * @param fileName 文件名
     * @param list 数据list
     */
    public static void listToExcel(String fileName,List<String> list){
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
    
    public static String convertString(Object value) {
        if (value == null) {
            return "";
        } else {
            return value.toString();
        }
    }

    
}
