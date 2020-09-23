package com.yeepay.payment;

import com.alibaba.fastjson.JSONArray;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class paymentQueryServlet extends HttpServlet {     
	private static final long serialVersionUID = 1L;
    
    /**
     * @see HttpServlet#HttpServlet()
     */
    public paymentQueryServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doPost(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub	request.setCharacterEncoding("utf-8");
		response.setCharacterEncoding("utf-8");
		String customerNumber = format(Config.getInstance().getValue("customerNumber"));
		String batchNo = format(request.getParameter("batchNo"));
		String product = format(request.getParameter("product"));
		String orderId = format(request.getParameter("orderId"));
		String pageNo = format(request.getParameter("pageNo"));
		String pageSize =format( request.getParameter("pageSize"));
		
		
		Map<String, Object> params = new HashMap<>();
		params.put("customerNumber", customerNumber);
		params.put("batchNo", batchNo);
		params.put("product", product);
		params.put("orderId", orderId);
		params.put("pageNo", pageNo);
		params.put("pageSize", pageSize);

		String uri = YeepayService.getUrl(YeepayService.PAYMENTQUERY_URL);

		Map<String,Object> yopresponsemap	=	YeepayService.yeepayYOP(params,uri);
		List<JSONArray> li =new ArrayList<JSONArray>();
		if(yopresponsemap.get("list")!=null){


			li=(List)yopresponsemap.get("list");
			System.out.println("list----"+li);


		}
		request.setAttribute("li",li);
		 request.setAttribute("yopresponsemap",yopresponsemap==null?"系统异常":yopresponsemap);
	       RequestDispatcher view	= request.getRequestDispatcher("/jsp/12paymentQueryResponse12.jsp");
	       view.forward(request, response);

	}
public static String format(String text){
	return text==null?"":text.trim();
}
}
