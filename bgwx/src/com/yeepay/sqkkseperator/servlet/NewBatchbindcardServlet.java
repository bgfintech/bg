package com.yeepay.sqkkseperator.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSON;

import com.yeepay.sqkkseperator.config.Config;
import com.yeepay.sqkkseperator.service.YeepayService;


public class NewBatchbindcardServlet extends HttpServlet {
	public static String[] batchdetailsparams = {"requestno", "bathprotocolno", "amount", "productname", "avaliabletime","identityid","identitytype", "cardtop","cardlast" , "free1", "free2", "free3","dividecallbackurl","dividelist"};	
	 String batchbindcardUri=		Config.getInstance().getValue("batchbindcardUri");
	public NewBatchbindcardServlet() {
		super();
		
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		super.doPost(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setCharacterEncoding("utf-8");
		request.setCharacterEncoding("utf-8");
		
		String merchantno		=format(request.getParameter("merchantno"));
		String merchantbatchno  =format(request.getParameter("merchantbatchno"));
		String batchcallbackurl =format(request.getParameter("batchcallbackurl"));
		String requesttime		=format(request.getParameter("requesttime"));
		String terminalno		=format(request.getParameter("terminalno"));
		String free1				=format(request.getParameter("free1"));
		String free2				=format(request.getParameter("free2"));
		String free3				=format(request.getParameter("free3"));
		String batchdetails		=format(request.getParameter("batchdetails"));
		System.out.println("批次信息："+batchdetails);
		List<Map<String, Object>> listparam=null;
		try {
			System.out.println("开始转换");
			listparam = toList(batchdetails);
		} catch (Exception e) {
			
			throw new RuntimeException("将批次明细转化为list集合出错！");
		}
		// batchdetails=JSONUtils.toJsonString(listparam);
		batchdetails=JSON.toJSONString(listparam);
		 System.out.println("输出一下json格式的details: "+batchdetails);
	Map<String,String> map	=	new HashMap<String,String>();
	   map.put("merchantno", merchantno);
	   map.put("merchantbatchno", merchantbatchno);
	   map.put("batchcallbackurl", batchcallbackurl);
	   map.put("requesttime", requesttime);
	   map.put("terminalno", terminalno);
	   map.put("batchdetails", batchdetails);
	   map.put("free1", free1);
	   map.put("free2", free2);
	   map.put("free3", free3);
	 
	   Map<String,String> yopresponsemap=YeepayService.yeepayYOP(map,batchbindcardUri);
		

	   request.setAttribute("yopresponsemap",yopresponsemap==null?"系统异常":yopresponsemap);
       RequestDispatcher view	= request.getRequestDispatcher("/jsp/41batchpayresponse.jsp");
       view.forward(request, response);
	}

	public static List<Map<String, Object>> toList(String batchdetails) throws Exception {
		 
		
		if (batchdetails == null) {
			
            throw new Exception("batchdetails is null");
        }
	
        batchdetails.trim();
       //将batchdetails分成一条条记录 
        String[] beans = batchdetails.split("\n");
        String[] tempBean = null;

        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

        for (int i = 0; i < beans.length; i++) {
        	   //Map<String, String> map = new HashMap<String, String>();
        		//每一条记录做trim，记录内容不做trim,将该条记录中每个字段拿出来
            tempBean = beans[i].trim().split("&");
            if (!(tempBean.length >= 14)) {
                throw new Exception("requestno:" + tempBean[0] + ",数据格式不对");
            }
           
            Map<String, Object> map = new HashMap<String, Object>();

            for (int j = 0; j < batchdetailsparams.length; j++) {
               
            	map.put(batchdetailsparams[j], tempBean[j]);
              
            }
        list.add(map);
        
        }
       
        System.out.println("list: "+list);
        return list;
    }

	
 public static String format(String text){
	 return text==null?"":text.trim();
 }
}
