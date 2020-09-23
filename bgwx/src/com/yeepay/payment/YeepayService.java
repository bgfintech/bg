package com.yeepay.payment;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.yeepay.g3.sdk.yop.client.YopRequest;
import com.yeepay.g3.sdk.yop.client.YopResponse;
import com.yeepay.g3.sdk.yop.client.YopRsaClient;
import com.yeepay.g3.sdk.yop.error.YopSubError;
import com.yeepay.g3.yop.sdk.api.StdApi;

import java.io.*;
import java.util.*;
import java.util.Map.Entry;


public class YeepayService {


    public static final String BASE_URL = "baseURL";
    public static final String PAYMENT_URL = "paymentURI";
    public static final String PAYMENTQUERY_URL = "paymentqueryURI";
    public static final String customeramountQuery_URL = "customeramountQueryURI";
    public static final String batchsend_URL = "batchsendURI";

    //获取父商编
    public static String getParentMerchantNo() {
        return Config.getInstance().getValue("groupNumber");
    }

    //获取子商编
    public static String getMerchantNo() {
        return Config.getInstance().getValue("customerNumber");
    }

    public static String getUrl(String payType) {
        return Config.getInstance().getValue(payType);
    }

    public static Map<String, Object> yeepayYOP(Map<String, Object> map, String Uri) throws IOException {

        Map<String, Object> result = new HashMap<String, Object>();
        Map<String, YopSubError> erresult = new HashMap<String, YopSubError>();
        YopRequest yoprequest = new YopRequest("OPR:" + getMerchantNo());

        Set<Entry<String, Object>> entry = map.entrySet();
        for (Entry<String, Object> s : entry) {
            yoprequest.addParam(s.getKey(), s.getValue());
        }
        System.out.println("yoprequest:" + yoprequest.getParams());

        //向YOP发请求
        YopResponse yopresponse = YopRsaClient.post(Uri, yoprequest);


        System.out.println("请求YOP之后的结果：" + yopresponse.getStringResult());


       // System.out.println("+++++" + JSON.toJSONString(yopresponse));
//        	对结果进行处理
        if ("FAILURE".equals(yopresponse.getState())) {
            if (yopresponse.getError() != null) {
                result.put("errorcode", yopresponse.getError().getCode());
                result.put("errormsg", yopresponse.getError().getMessage());

                if (yopresponse.getError().getSubCode() != null && yopresponse.getError().getSubCode().length()>0) {
                	erresult.get("errorDetails");
                	//    erresult.put("errorDetails", yopresponse.getError().getSubCode().getBytes());
                } else {
                    erresult.put("errorDetails", null);
                }

                System.err.println("错误明细：" + yopresponse.getError().getSubCode());
                result.putAll(erresult);
                System.out.println("系统处理异常结果：" + result);
            }

            return result;
        }
        //成功则进行相关处理
        if (yopresponse.getStringResult() != null) {
            result = parseResponse(yopresponse.getStringResult());


        }

        return result;
    }


    //将获取到的response转换成json格式
    public static Map<String, Object> parseResponse(String yopresponse) {

        Map<String, Object> jsonMap = new HashMap<>();
        jsonMap = JSON.parseObject(yopresponse,
                new TypeReference<TreeMap<String, Object>>() {
                });
        System.out.println("将response转化为map格式之后: " + jsonMap);
        return jsonMap;
    }

    public static String getRandom(int length) {
        Random random = new Random();
        StringBuilder ret = new StringBuilder();
        for (int i = 0; i < length; i++) {
            ret.append(Integer.toString(random.nextInt(10)));
        }
        return ret.toString();
    }

    public static String yosFile(Map<String, String> params, String path) {
        StdApi apidApi = new StdApi();
        InputStream inputStream = null;
        OutputStream outputStream = null;

        String method = params.get("method");
        String date = params.get("date");
        String dataType = params.get("dataType");

        String fileName = "";
        String filePath = "";
        try {

            inputStream = apidApi.remitDayBillDownload(getMerchantNo(), date, dataType);
            fileName = "remitday-" + dataType + "-" + date + ".csv";

            filePath = path + File.separator + fileName;
            System.out.println("filePath=====" + filePath);
            outputStream = new FileOutputStream(new File(filePath));
             
            

            byte[] bs = new byte[1024];
            int readNum;
            while ((readNum = inputStream.read(bs)) != -1) {
                outputStream.write(bs, 0, readNum);
            }
        } catch (Exception e1) {
            e1.printStackTrace();
            return null;
        } finally {
            try {
                outputStream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                inputStream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return filePath;
    }


}

        	

        

