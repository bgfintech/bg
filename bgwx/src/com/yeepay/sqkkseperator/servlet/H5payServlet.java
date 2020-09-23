package com.yeepay.sqkkseperator.servlet;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.yeepay.sqkkseperator.config.Config;
import com.yeepay.sqkkseperator.service.YeepayService;


public class H5payServlet extends HttpServlet{
String h5payUri=Config.getInstance().getValue("h5payUri");

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		super.doPost(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");
		response.setCharacterEncoding("utf-8");
		
		String merchantno=Config.getInstance().getValue("merchantno");
		String requestno=format(request.getParameter("requestno"));
		String h5directpaytype=format(request.getParameter("h5directpaytype"));
		String identityid=format(request.getParameter("identityid"));
		String identitytype=format(request.getParameter("identitytype"));
		String amount=format(request.getParameter("amount"));
		String avaliabletime=format(request.getParameter("avaliabletime"));
		String productname=format(request.getParameter("productname"));
		String callbackurl=format(request.getParameter("callbackurl"));
		String requesttime=format(request.getParameter("requesttime"));
		String dividecallbackurl=format(request.getParameter("dividecallbackurl"));
		String dividejstr=format(request.getParameter("dividejstr"));
		System.out.println("h5payUri:"+h5payUri);
		Map<String,String> map	=	new HashMap<String,String>();
		   map.put("merchantno", merchantno);
		   map.put("requestno", requestno);
		   map.put("h5directpaytype", h5directpaytype);
		   map.put("identityid", identityid);
		   map.put("identitytype", identitytype);
		   map.put("amount", amount);
		   map.put("avaliabletime", avaliabletime);
		   map.put("callbackurl", callbackurl);
		   map.put("requesttime", requesttime);
		   map.put("productname", productname);
		   map.put("dividecallbackurl", dividecallbackurl);
		   map.put("dividejstr", dividejstr);
	 
		   
		   Map<String,String> yopresponsemap	=	YeepayService.yeepayYOP(map,h5payUri);
			

		   request.setAttribute("yopresponsemap",yopresponsemap==null?"系统异常":yopresponsemap);
	       RequestDispatcher view	= request.getRequestDispatcher("/jsp/61h5payResponse.jsp");
	       view.forward(request, response);
		
	
	
	
	}
public static String format(String text){
	return text==null?"":text.trim();
}
}
