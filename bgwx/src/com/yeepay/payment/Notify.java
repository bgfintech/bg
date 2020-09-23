package com.yeepay.payment;

import java.io.IOException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import sun.misc.BASE64Decoder;

import com.yeepay.g3.sdk.yop.encrypt.CertTypeEnum;
import com.yeepay.g3.sdk.yop.encrypt.DigitalEnvelopeDTO;
import com.yeepay.g3.sdk.yop.utils.DigitalEnvelopeUtils;
import com.yeepay.g3.sdk.yop.utils.InternalConfig;



public class Notify {
	public static void main(String[] args) {
//		String response = "OiNpKjN10BJiivwtkdzmAKgViRMU-vX-t3n3laqpyaOWdQ-u68Edr5RIN5unnrHN087PzGPfyYaBNNgOkYP_Il4KgvM7j75Mta5tOsjT8Jc1ThSN1QXuYDeHVR2xGoynJm4WPFThw9Uz6Ag8ypt1zk23Gxs4ZOOffzhX2MzorkXy8IhGcGnvjcT0_kaCa2o3W_1Qnj760_Y2CPYROm-YdKRahMsDPVVxJV89CRkb3ht8p9FHIZX0dzkDgpSDD-KcdmKgDSrLE3J37s73oIfkOt3EKLnIl4xSXMed-FkkKDIlK5wkzg3nitxiGd9eNJW2sUng0AAjrrUCwsFRW2kw9A$BofSGv_AzwQVVRnHRiaLBfmj5cTdLEexItAU2-Fg_wK1jDblpxPfnVoedz2W3peQaMPOH2BIonwN6hQGt2oBOQuDYbBmBKBavKddUTvxGOXRX8TXtPAX5_STGobA57qczDZ7Lk1iB7kfLhU5ZEml6Pk8dkHOTM6Gs3nER5yKM7RZKyO0Q-kcQ_ZCjxaqZDd3cQLo8ihMHOJgmVdqRe2r5ZSKvIWIsoyih9XseRc6twP3yUXRp7y0v0UX_0JTRBzt2dNZD2_c6xs5gE-Gq-wJJUyZIo9afs9vY0kPkEeA7Y3Sm-81UTyVjU2EzOzt7LLTBafMTWhbK5jxf8wBRY5p6GMus5fUIhbHmrXB7W8juVp2jqV7Sabz8N5iiCKJVhulROotm6qQ6SpLOSVdilymMgLgU4OGaZZnDOL6qItydx3fnXjzFLZcrDZ_nopYKUFDVFwPf6d-raNzzJ2s3l2z5AToWLUNmOdBzCG19iwcFlBKDTWiqkxy1J5LKnIn-W38vX3UjsJ8cCnpauKBGQbAjBh6jHch_Tg891k_UuzUBQN2GS64-0l8oCWvytykiQ4AU955vqraD8s70Ke6f9A1wxkRGl-pi4p7Av00FmmRgGMkTIN59ooNsIKJUiSfeb9ecctB5t3apYKJgnbUT6WbpyK0hGpKkRvYBBk6cZ8KHjja5-zcNwlkx5k2Mq4TOo3KnwwH3fitNwqlcEA1yN6AWrh6FaK-5tQZLh7wxwJMnnlfDFx9FTQkpDHtxKnOLhJap6BACiDhdiGJ_aBF1_T4ABSJV1O_sPoepnicITUV_3C9sTq5DA1QSkIH-_Bu4mxpCLuTsGE8dbA0JOFwxBcO4pjcp_XrEii6J5KoNzlydPtussIuF73YIAQyNM2UnseSQLSj6zcIen2WacmhpXMS-wCVpo2GrMnqrGxgM2GgInD98XdjTzuKsvPehpo6DEfI-AXNDMnJA6b6IXYJnsQjPfd0A67cGNUi7BJi3AafUOy8rZmhab6_ui5bYH8U7huBLXzQNSRSKJyNdznwwhfJO5LbQL5qdqP9lmEYKr1q1N_1ZhJokUeoOzNizIsQWCIgllQyt9E0-cVNTMhhvTmMig$AES$SHA256";
		String response = "ctZc9UXEKBpgeQ6iTpfWlwhGxluBBNF28E4cI_jrmXZHzs-89wW19gqb0DXgdyTwzM3tesR3OZlZy--yxmQTtI2m-PUtIit0zJYvvawKuYjxt2E42gyEUTSv_vYUrSkC34H8wS864eQgP10hXo43ykvH1c_U1Id6Kr1xDWrc6UsLbcLNjEeQWG_rrZIDQU7aLat3hCJ0bPEKgoNjTKlDpbJzQ6ea7vCC8-VOMl3x4GfZAcww3sa3_HcNfgCaS44CKH6x8o7fj1NtQfnvnvHxkbzhLc4QrNh-8z4n7CcKv06NzMiTUTSixLuPUDE_mhjpOVFeDy-5N5LinPFUTKRnPA%24_Sv6QH3C684kF1HYJ4uO68tjMhkJkWtFbR3zyfetcNhZdRWhgD5kxKKf_Tqtl-yFTpT0v4I6E_7wIFzIHcNF76GJSqXU7dOwRiQv8PdnAuco3KBxDrWNd6rqb6X_CGS5pHps8Q3ZXFMjoeoIc6eP3bEJQNUuBJ0_hxeTlqKgUrSQTZsO5rTS_lwaX37uWd1rd_UZmfnmS2CGHQobKQqN7ljMaADPm4ZAxH12qLKnYfTLLeRmegCk1mRcOkwkpA7QGmxZ713asQuwCjxjFzBpKeVgb3pXZJb3GM41jW25fMPynd9i2UJQb79yuM3xPrEMEt9ji_NeUOT2bHJj9SDT6xEnKVb6rrBwJWugN1D_NXaMGGUwHEQIk5ihYpV03s1xEZPt3fwXYCsXvd8PlJ3X2XI6wtDP40nJek9zRkv9KzX8z5YyA8jwy876cHXgx68FD05BvOjwT8SLd3hncK-rOZJmFUq_2GDbZHlG30KwBqx5dERr0mGSHpVbrQdGXOVFiDU5lLIphwaHwEsZPvkTPZXy2leNdXWjtk7cMfmA9Fxt1jqLcnubtNS7PjOh1lXo_8UCYiaZU7ROztKYmvBqM2QK3THkMJX8l4Su-BymVIimpJw55gR8vTL4eCi2EMicd8LKnyX1M17D9taQK7VcLL0DrHOwoJUCYEs4aYpny_eppCk1kIGT4cew775ZsYc_yTUXShlBjbSaPLC8FXf8c7yeS8jnLogsuaHiEfF4dd1QA5fdxIGKsc8lJRVTTbTYYa_HScX5NcS97n34IOOHc49w9rwikBayttVD4smBbGguVvqPPSpQpIccMbHylGdf%24AES%24SHA256";
		try {
			// 开始解密
			Map<String, String> jsonMap = new HashMap<>();
			DigitalEnvelopeDTO dto = new DigitalEnvelopeDTO();
			dto.setCipherText(response);
			PrivateKey privateKey = getPrivateKey();
			System.out.println("privateKey: " + privateKey);
			PublicKey publicKey = getPubKey();
			System.out.println("publicKey: " + publicKey);

			dto = DigitalEnvelopeUtils.decrypt(dto, privateKey, publicKey);
			System.out.println("解密结果:" + dto.getPlainText());
			System.out.println(jsonMap);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("回调解密失败！");
		}
	}

	/**
	 * 实例化公钥
	 * 
	 * @return
	 */
	private static PublicKey getPubKey() {
		PublicKey publicKey = null;
		try {
			// 自己的公钥(测试)
			String publickey = Config.getInstance().getValue("publickey");
			java.security.spec.X509EncodedKeySpec bobPubKeySpec = new java.security.spec.X509EncodedKeySpec(
					new BASE64Decoder().decodeBuffer(publickey));
			// RSA对称加密算法
			java.security.KeyFactory keyFactory;
			keyFactory = java.security.KeyFactory.getInstance("RSA");
			// 取公钥匙对象
			publicKey = keyFactory.generatePublic(bobPubKeySpec);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (InvalidKeySpecException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return publicKey;
	}

	private static PrivateKey getPrivateKey() {
		PrivateKey privateKey = null;
		String priKey = Config.getInstance().getValue("privatekey");
		PKCS8EncodedKeySpec priPKCS8;
		try {
			priPKCS8 = new PKCS8EncodedKeySpec(
					new BASE64Decoder().decodeBuffer(priKey));
			KeyFactory keyf = KeyFactory.getInstance("RSA");
			privateKey = keyf.generatePrivate(priPKCS8);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (InvalidKeySpecException e) {
			e.printStackTrace();
		}
		return privateKey;
	}

}
