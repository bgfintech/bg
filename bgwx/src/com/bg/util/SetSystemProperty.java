package com.bg.util;
import java.io.BufferedInputStream;   
import java.io.FileInputStream;   
import java.io.FileNotFoundException;   
import java.io.IOException;   
import java.io.InputStream;   
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;  
/**  
* @author  
* @version  
*/   
public class SetSystemProperty {   
    /**  
    * 采用静态方法  
    */   
    private static Map<String,Properties> proMap = new HashMap<String,Properties>();
   
  
    /**  
    * 读取属性文件中相应键的值  
    * @param key  
    *            主键  
    * @return String  
     * @throws IOException 
     * @throws FileNotFoundException 
    */   
    public static String getKeyValue(String key,String profilepath) {   
    	Properties props = proMap.get(profilepath);
    	if(props==null){
    		String path=SetSystemProperty.class.getResource("/").getPath();
        	System.out.println("---------------------"+path);
            try {
            	props = new Properties();
				props.load(new FileInputStream(path+profilepath));
				proMap.put(profilepath, props);
			}catch (Exception e) {
				e.printStackTrace();
			}   
    	}    	
        return props.getProperty(key);   
    }   
  
    /**  
    * 根据主键key读取主键的值value  
    * @param filePath 属性文件路径  
    * @param key 键名  
    */   
    public static String readValue(String filePath, String key) {   
    	
    	
    	Properties props = new Properties();   
        try {   
            InputStream in = new BufferedInputStream(new FileInputStream(   
                    filePath));   
            props.load(in);   
            String value = props.getProperty(key);   
            System.out.println(key +"键的值是："+ value);   
            return value;   
        } catch (Exception e) {   
            e.printStackTrace();   
            return null;   
        }   
    }   
      
    //测试代码   
    public static void main(String[] args) {  
    	System.out.println();
        readValue("mail.properties", "MAIL_SERVER_PASSWORD");       
        System.out.println("操作完成");   
    }   
} 