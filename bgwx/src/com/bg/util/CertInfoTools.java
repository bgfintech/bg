package com.bg.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Vector;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.tools.ant.taskdefs.Sleep;

import com.jcraft.jsch.ChannelSftp;

public class CertInfoTools {
	/**
	 * 生成上报zip文件名称规则：年月日时分秒+3位数字编码+.zip后缀
	 * 
	 * @return
	 */
	public static String genZipFileName() {
		Date current = new Date();
		java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat(
				"yyyyMMddHHmmss");
		String curtime = sdf.format(current);

		return curtime + createData(3);
	}

	/**
	 * 生成上报文件目录
	 */
	public static void genCertInfoFolder() {

	}
    /**
     * 生成压缩文件
     * @throws Exception
     */
	public static void zip(String zipFileName,String sourceFileName) throws Exception {
		// File zipFile = new File(zipFileName);
		System.out.println("压缩中...");

		// 创建zip输出流
		ZipOutputStream out = new ZipOutputStream(new FileOutputStream(
				zipFileName));

		// 创建缓冲输出流
		BufferedOutputStream bos = new BufferedOutputStream(out);

		File sourceFile = new File(sourceFileName);

		// 调用函数
		compress(out, bos, sourceFile, sourceFile.getName());

		bos.close();
		out.close();
		System.out.println("压缩完成");

	}

	public static void compress(ZipOutputStream out, BufferedOutputStream bos,
			File sourceFile, String base) throws Exception {
		// 如果路径为目录（文件夹）
		if (sourceFile.isDirectory()) {

			// 取出文件夹中的文件（或子文件夹）
			File[] flist = sourceFile.listFiles();

			if (flist.length == 0)// 如果文件夹为空，则只需在目的地zip文件中写入一个目录进入点
			{
				System.out.println(base + "/");
				out.putNextEntry(new ZipEntry(base + "/"));
			} else// 如果文件夹不为空，则递归调用compress，文件夹中的每一个文件（或文件夹）进行压缩
			{
				for (int i = 0; i < flist.length; i++) {
					compress(out, bos, flist[i],
							base + "/" + flist[i].getName());
				}
			}
		} else// 如果不是目录（文件夹），即为文件，则先写入目录进入点，之后将文件写入zip文件中
		{
			out.putNextEntry(new ZipEntry(base));
			FileInputStream fos = new FileInputStream(sourceFile);
			BufferedInputStream bis = new BufferedInputStream(fos);

			int tag;
			System.out.println(base);
			// 将源文件写入到zip文件中
			while ((tag = bis.read()) != -1) {
				bos.write(tag);
			}
			bis.close();
			fos.close();

		}
	}

	public static boolean createDir(String destDirName) {
		File dir = new File(destDirName);
		if (dir.exists()) {
			System.out.println("创建目录" + destDirName + "失败，目标目录已经存在");
			return false;
		}
		if (!destDirName.endsWith(File.separator)) {
			destDirName = destDirName + File.separator;
		}
		// 创建目录
		if (dir.mkdirs()) {
			System.out.println("创建目录" + destDirName + "成功！");
			return true;
		} else {
			System.out.println("创建目录" + destDirName + "失败！");
			return false;
		}
	}
	 public static File createFile(String path,String fileName){
	        File folder = new File(path);
	        if(!folder.exists()){
	            folder.mkdir();
	        }
	        File file = new File(path+fileName);
	        if(!file.exists()){
	            try {
	                file.createNewFile();
	            } catch (IOException e) {
	                // TODO Auto-generated catch block
	                e.printStackTrace();
	            }
	        }
	        return file;
	    }
	// 根据指定长度生成纯数字的随机数
	public static String createData(int length) {
		StringBuilder sb = new StringBuilder();
		Random rand = new Random();
		for (int i = 0; i < length; i++) {
			sb.append(rand.nextInt(10));
		}
		String data = sb.toString();
		System.out.println(length + " random data: " + data);
		return data;
	}
    public void uploadSftp(){ 
    	
    }
	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		// 创建目录
		String sourceFolder="D:/work/temp/"+CertInfoTools.genZipFileName();
		String dirName = sourceFolder+"/cert";
		// 生成文件
		CertInfoTools.createFile(sourceFolder+"/", "certInfo.txt");
		CertInfoTools.createDir(dirName);
		// 压缩文件
		CertInfoTools.zip(sourceFolder+".zip",sourceFolder);
		// 放SFTP
	

        Map<String, String> sftpDetails = new HashMap<String, String>();
        // 设置主机ip，端口，用户名，密码
        sftpDetails.put(SFTPConstants.SFTP_REQ_HOST, "218.17.35.123");
        sftpDetails.put(SFTPConstants.SFTP_REQ_USERNAME, "2017120800092260");
        sftpDetails.put(SFTPConstants.SFTP_REQ_PASSWORD, "admin1234");
        sftpDetails.put(SFTPConstants.SFTP_REQ_PORT, "22");
        
        String src = sourceFolder+".zip"; // 本地文件名
        String dst = "/cashier/mpp"; // 目标文件名
              
        SFTPChannel channel = new SFTPChannel();
        ChannelSftp chSftp = channel.getChannel(sftpDetails, 60000);
        
        /**
         * 代码段1
        OutputStream out = chSftp.put(dst, ChannelSftp.OVERWRITE); // 使用OVERWRITE模式
        byte[] buff = new byte[1024 * 256]; // 设定每次传输的数据块大小为256KB
        int read;
        if (out != null) {
            System.out.println("Start to read input stream");
            InputStream is = new FileInputStream(src);
            do {
                read = is.read(buff, 0, buff.length);
                if (read > 0) {
                    out.write(buff, 0, read);
                }
                out.flush();
            } while (read >= 0);
            System.out.println("input stream read done.");
        }
        **/
        Date current = new Date();
		java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat(
				"yyyyMMdd");
		String curtime = sdf.format(current);
        try{
        	
    		Vector content = chSftp.ls(dst+"/"+curtime);
    		
            
        }catch(Exception ex){
            System.out.println(ex.getMessage());
            chSftp.mkdir(dst+"/"+curtime);
        }
        System.out.println("在目标服务器上成功建立了文件夹: " + dst+"/"+curtime);
        dst = dst+"/"+curtime;
        chSftp.put(src, dst, ChannelSftp.OVERWRITE); // 代码段2
        
        // chSftp.put(new FileInputStream(src), dst, ChannelSftp.OVERWRITE); // 代码段3
        
        chSftp.quit();
        channel.closeChannel();
		
	}

}
