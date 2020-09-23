package com.yeepay.sqkkseperator.config;

import java.util.ResourceBundle;

public class Config {
     public static Object object=new Object();
     public static Config config=null;
     public static ResourceBundle  rb=null;//读取资源文件的类 
     public static final String  File_Name="ybzf/merchantInfo";
   
     public Config(){
    	    rb=ResourceBundle.getBundle(File_Name);
     }
     
     public static Config getInstance(){
    	 synchronized(object){
    		 if(config==null){
    			 config=new Config();
    		 }
    		 return config;
    	 }
		
    	 
     }
     
 	public String getValue(String key) {
		return (rb.getString(key));
	}
//     public static void main(String[] args) {
//    	 System.out.println("begin:");
//    	 System.out.println(Config.getInstance().getValue("p1_MerId"));
//	System.out.println(Config.getInstance().getValue("privateKey"));
//     System.out.println(Config.getInstance().getValue("payurl")+'\n'
//    		 +Config.getInstance().getValue("queryurl"));
//     
//     }
}
