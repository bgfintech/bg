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



public class AuthListqueryServlet extends HttpServlet {
	String authListqueryUri=Config.getInstance().getValue("authListqueryUri");
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		super.doPost(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setCharacterEncoding("utf-8");
		request.setCharacterEncoding("utf-8");
		
		
		String merchantno		=Config.getInstance().getValue("merchantno");
		String identityid  =format(request.getParameter("identityid"));
		String identitytype   		=format(request.getParameter("identitytype"));		
		
      
		Map<String,String> map	=	new HashMap<String,String>();
		   map.put("merchantno", merchantno);
		   map.put("identityid", identityid);
		   map.put("identitytype", identitytype);
		  
		   Map<String,String> yopresponsemap	=	YeepayService.yeepayYOP(map,authListqueryUri);
			

		   request.setAttribute("yopresponsemap",yopresponsemap==null?"系统异常":yopresponsemap);
	       RequestDispatcher view	= request.getRequestDispatcher("/jsp/15authListqueryResponse.jsp");
	       view.forward(request, response);
		
	}
 public static String format(String text){
	 return text==null?"":text.trim();
 }
}

		
		
		
	


