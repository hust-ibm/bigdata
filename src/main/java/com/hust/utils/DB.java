package com.hust.utils;

import java.sql.Connection;  

import java.sql.DriverManager;  
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement; 

public class DB {  
    public static final String url = "jdbc:mysql://localhost:3306/crawl";  
    public static final String name = "com.mysql.jdbc.Driver";  
    public static final String user = "root";  
    public static final String password = "root";  
  
    public static Connection conn = null;  

    
    public void close() {  
        try {  
            this.conn.close();   
        } catch (SQLException e) {  
            e.printStackTrace();  
        }  
    }  
  
    public void open(String sql)
    {
        try {  
            Class.forName(name);//指定连接类型  
            conn = DriverManager.getConnection(url, user, password);//获取连接  
            Statement st = conn.createStatement();
            System.out.println("成功连接数据库");
            int rs = st.executeUpdate(sql);
     /*       String sql = "select * from user";
            String ss="insert into user(name,pwd) values('"+user.getName()+"','"+user.getPwd()+"')";
            String s="insert into user(username) values('eeeee')";
            
            int rs = st.executeUpdate(s);
            ResultSet r = st.executeQuery(sql);
            
            while(r.next())
            {
            	System.out.println(r.getString("username"));
            }*/
            
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
    } 
    
   
    
    
}  
