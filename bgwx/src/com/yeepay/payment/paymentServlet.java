package com.yeepay.payment;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.yeepay.g3.sdk.yop.client.YopRequest;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

public class paymentServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		super.doPost(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");
		response.setCharacterEncoding("utf-8");
		String customerNumber = format(Config.getInstance().getValue("customerNumber"));
		String groupNumber = format(Config.getInstance().getValue("groupNumber"));

		String batchNo = format(request.getParameter("batchNo"));
		String orderId = format(request.getParameter("orderId"));
		String amount = format(request.getParameter("amount"));
		String product = format(request.getParameter("product"));
		String urgency = format(request.getParameter("urgency"));
		String accountName = format(request.getParameter("accountName"));
		String accountNumber = format(request.getParameter("accountNumber"));
		String bankCode = format(request.getParameter("bankCode"));
		String bankName = format(request.getParameter("bankName"));
		String bankBranchName = format(request.getParameter("bankBranchName"));
		String provinceCode = format(request.getParameter("provinceCode"));
		String cityCode = format(request.getParameter("cityCode"));
		String feeType = format(request.getParameter("feeType"));
		String desc = format(request.getParameter("desc"));
		String leaveWord = format(request.getParameter("leaveWord"));
		String abstractInfo = format(request.getParameter("abstractInfo"));

		Map<String, Object> params = new HashMap<>();
		params.put("customerNumber", customerNumber);
		params.put("groupNumber", groupNumber);
		params.put("batchNo", batchNo);
		params.put("orderId", orderId);
		params.put("amount", amount);
		params.put("product", product);
		params.put("urgency", urgency);
		params.put("accountName", accountName);
		params.put("accountNumber", accountNumber);
		params.put("bankCode", bankCode);
		params.put("bankName", bankName);
		params.put("bankBranchName", bankBranchName);
		params.put("provinceCode", provinceCode);
		params.put("cityCode", cityCode);
		params.put("feeType", feeType);
		params.put("desc", desc);
		params.put("leaveWord", leaveWord);
		params.put("abstractInfo", abstractInfo);

		/*
		 * SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd"); String
		 * merchantNo = format(Config.getInstance().getValue("merchantNo"));
		 * String parentMerchantNo =
		 * format(Config.getInstance().getValue("parentMerchantNo")); String
		 * privateKey = Config.getInstance().getValue("privatekey"); YopRequest
		 * yoprequest = new YopRequest("OPR:" + merchantNo, privateKey,
		 * Config.getInstance().getValue("baseURL"));
		 */
		String uri = YeepayService.getUrl(YeepayService.PAYMENT_URL);

		Map<String, Object> yopresponsemap = YeepayService.yeepayYOP(params, uri);

		request.setAttribute("yopresponsemap", yopresponsemap == null ? "系统异常" : yopresponsemap);
		RequestDispatcher view = request.getRequestDispatcher("/jsp/11paymentResponse.jsp");
		view.forward(request, response);

	}

	public static String format(String text) {
		return text == null ? "" : text.trim();
	}
}
