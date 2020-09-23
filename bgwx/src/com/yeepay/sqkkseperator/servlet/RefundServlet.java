package com.yeepay.sqkkseperator.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.yeepay.sqkkseperator.config.Config;
import com.yeepay.sqkkseperator.service.YeepayService;


public class RefundServlet extends HttpServlet{
	String refundUri=			Config.getInstance().getValue("refundUri");
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		super.doPost(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setCharacterEncoding("utf-8");
		request.setCharacterEncoding("utf-8");
		
		
		String merchantno	=Config.getInstance().getValue("merchantno");
		String requestno		=format(request.getParameter("requestno"));
		String paymentyborderid=format(request.getParameter("paymentyborderid"));
		String amount		=format(request.getParameter("amount"));
		String callbackurl	=format(request.getParameter("callbackurl"));
		String requesttime	=format(request.getParameter("requesttime"));
		String remark		=format(request.getParameter("remark"));
		
		
		
		Map<String,String> map=new HashMap<String,String>();
		   map.put("merchantno", merchantno);
		   map.put("requestno", requestno);
		   map.put("paymentyborderid", paymentyborderid);
		   map.put("amount", amount);
		   map.put("callbackurl", callbackurl);
		   map.put("requesttime", requesttime);
		 map.put("remark", remark);
		 Map<String,String> yopresponsemap=YeepayService.yeepayYOP(map,refundUri);
		  
		   request.setAttribute("yopresponsemap",yopresponsemap==null?"系统异常":yopresponsemap);
	       RequestDispatcher view	= request.getRequestDispatcher("/jsp/44refundresponse.jsp");
	       view.forward(request, response);
	}
	public static String format(String text){
		return text!=null?text.trim():"";
	}


}
