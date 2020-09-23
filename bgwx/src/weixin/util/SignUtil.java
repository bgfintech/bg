package weixin.util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Formatter;

public class SignUtil {
	
	/**
	 * sha1转化
	 * @param jsapi_ticket
	 * @param nonceStr
	 * @param timestamp
	 * @param url
	 * @return
	 */
	public static String createSignature(String jsapi_ticket, String nonceStr, String timestamp, String url){
		String[] arr = new String[]{"jsapi_ticket=" + jsapi_ticket, "noncestr=" + nonceStr, "timestamp=" + timestamp, "url=" + url};
		Arrays.sort(arr);
		StringBuffer content = new StringBuffer();
		for(int i = 0; i < arr.length; i ++){
			content.append("&" + arr[i]);
		}
		MessageDigest md = null;
		String signature = null;
		try {
			md = MessageDigest.getInstance("SHA-1");
			md.reset();
			md.update(content.toString().substring(1).getBytes("UTF-8"));
			signature = byteToHex(md.digest());
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		content = null;
		return signature;
	}
	
	private static String byteToHex(final byte[] hash) {
        Formatter formatter = new Formatter();
        for (byte b : hash) {
            formatter.format("%02x", b);
        }
        String result = formatter.toString();
        formatter.close();
        return result;
    }

	/**
	 * 将字节数组转换为十六进制字符串
	 * @param byteArray
	 * @return
	 */
	private static String byteToStr(byte[] byteArray) {
		String strDigest = "";
		for (int i = 0; i < byteArray.length; i++) {
			strDigest += byteToHexStr(byteArray[i]);
		}
		return strDigest;
	}

	/**
	 * 将字节转换为十六进制字符串
	 * @param mByte
	 * @return
	 */
	private static String byteToHexStr(byte mByte) {
		char[] Digit = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };
		char[] tempArr = new char[2];
		tempArr[0] = Digit[(mByte >>> 4) & 0X0F];
		tempArr[1] = Digit[mByte & 0X0F];

		String s = new String(tempArr);
		return s;

	}
}
