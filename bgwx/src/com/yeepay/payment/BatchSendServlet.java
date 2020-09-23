package com.yeepay.payment;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import com.yeepay.g3.sdk.yop.client.YopClient3;
import com.yeepay.g3.sdk.yop.client.YopRequest;
import com.yeepay.g3.sdk.yop.client.YopResponse;

public class BatchSendServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public BatchSendServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		doPost(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		// request.setCharacterEncoding("utf-8");
		response.setCharacterEncoding("utf-8");
		request.setCharacterEncoding("utf-8");
		String customerNumber = format(Config.getInstance().getValue("customerNumber"));
		String groupNumber = format(Config.getInstance().getValue("groupNumber"));
		String totalAmount = format(request.getParameter("totalAmount"));
		String totalCount = format(request.getParameter("totalCount"));
		String product = format(request.getParameter("product"));
		String detailJson = format(request.getParameter("detailJson"));
		System.out.println("detailJson" + detailJson);

		/*
		 * SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMdd"); String
		 * merchantNo = format(Config.getInstance().getValue("merchantNo"));
		 * String parentMerchantNo=format(Config.getInstance().getValue(
		 * "parentMerchantNo")); String
		 * privateKey=Config.getInstance().getValue("privatekey"); String
		 * orderId = format(sdf.format(new Date())+getRandom(7));
		 * 
		 * YopRequest yoprequest = new
		 * YopRequest("OPR:"+merchantNo,privateKey,Config.getInstance().getValue
		 * ("baseURL"));
		 */

		Map<String, Object> params = new HashMap<>();
		params.put("customerNumber", customerNumber);
		params.put("groupNumber", groupNumber);
		params.put("totalAmount", totalAmount);
		params.put("totalCount", totalCount);
		params.put("product", product);
		params.put("detailJson", detailJson);
		String uri = YeepayService.getUrl(YeepayService.batchsend_URL);

		Map<String, Object> yopresponsemap = YeepayService.yeepayYOP(params, uri);

		request.setAttribute("yopresponsemap", yopresponsemap == null ? "系统异常" : yopresponsemap);
		RequestDispatcher view = request.getRequestDispatcher("/jsp/21batchSendResponse.jsp");
		view.forward(request, response);

	}

	public static String format(String text) {
		return text == null ? "" : text.trim();
	}

	public static String getRandom(int length) {
		Random random = new Random();
		StringBuilder ret = new StringBuilder();
		for (int i = 0; i < length; i++) {
			ret.append(Integer.toString(random.nextInt(10)));
		}
		return ret.toString();
	}

}
