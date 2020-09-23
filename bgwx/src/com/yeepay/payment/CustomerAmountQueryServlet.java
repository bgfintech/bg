package com.yeepay.payment;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


public class CustomerAmountQueryServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public CustomerAmountQueryServlet() {
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


        Map<String, Object> params = new HashMap<>();
        params.put("customerNumber", customerNumber);

        String uri = YeepayService.getUrl(YeepayService.customeramountQuery_URL);

        Map<String,Object> yopresponsemap	=	YeepayService.yeepayYOP(params,uri);
        request.setAttribute("yopresponsemap",yopresponsemap==null?"系统异常":yopresponsemap);
        RequestDispatcher view	= request.getRequestDispatcher("/jsp/22customeramountQueryResponse.jsp");
        view.forward(request, response);

    }
    public static String format(String text){
        return text==null?"":text.trim();
    }
}
