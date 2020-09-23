package com.yeepay.sqkkseperator.config;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.yeepay.g3.sdk.yop.encrypt.CertTypeEnum;
import com.yeepay.g3.sdk.yop.encrypt.DigitalEnvelopeDTO;
import com.yeepay.g3.sdk.yop.utils.DigitalEnvelopeUtils;
import com.yeepay.g3.sdk.yop.utils.InternalConfig;

public class NotifyTest {
public static void main(String[] args) {
	//String response="KhRBupLqDCpKpwc2t_y8WvQQ6Ut3wVjnCFrwKMcT8dwblS-d7cyH7Qaf2tiirl1SwBdqT6AOBCoA_0mKbOzCvC5JNMYd2bxHFSB5KcofTw2pKi3FCg-zrsvru0ZfkvEhS5DaCUAnn76qnC0VuqbFERl_ub-odEa9QhGacqBhxMK9upt6-OrKwwkwwfcMqHn32bR_X-Ymyaw8qVWbOMNc_QGRK9L6vYQBZh991RjliYfTRQjVK4gfCsy9QKRghlei11ZWxOVNh36Vo_l1Rk4WRa6GzYxQEG8lTRmkLXmzzvwOtCyWmWeM0eSIH_F-3PMB3SJ453DFTyED8td0UR52Tw$TEc8ZS__d-EnuTPEcNgrXwvqid7qY0wYutXIM0GGXjkzNSHXt8V1WWnj_7QRwIDQPWLpkn5OWidP912S0JNZZCnlFqzlg3_nb7ySfG93ylB_3C-WQDvDhfoiKi4PWUbUBVv7Bc5PzHlrQ9jXm6nu6gCxzWQJG26XVqEnumyAqWBiBCl7vg6h8LboyVfFMIgs2knxV5xhMrZ8tKinVw-xkC-aPFVCf3t0FBLnduzA9d663GtKykIta90qoiMz4SGpRXbF3iVCvFplAYjik5fefepy6N_5P63U2K970kziJw6hlPQfEGrhqI-DpycLPSi5wMotNCOo-gfL-WA8kMhSMG80eOwCfEgIo2L4YHUow4o6Pys-hznNorXvYkx2vedxb_hvm-aZ9pTc_lTQ7sNeDe-b2MiRoxMZstSyxxqpzRBI-mfRgqB8SktB1HRcocTbPKQJNxZMg1nrpYuNdrNXWCuW4pDeqM_qN03QbBSC05vrt-bXexENTu3-AxL2mPmLVWz_4iq-rwTlc5Kz7nrvuc8m5zOTkTyJHMa8iFMa2Wl48sQgsR3YVsX7jSq9nytktAVhAiszdCOXlW5RwX4dWN7bBN0NWJDwFmeg-STdXjgbxxuYKKjU7xAoLI9NvPUWZRBpf7VXastZiMl1FtGsRZ09fWly0uLfiGO0bgQVFchlHWcp-qIz416-cEf5TrcTNQwAzR9G2L7HHPDJd_qBzO2_GfDMhyc3kfx-QL_-2DaGfesYxgDY4pH5jNElWTQsOiLauufAofVPhEGOvo5KQ1svsnbloeYoHL9_UBtJJzU0KqHmwzHehRELB1tEs7Z9$AES$SHA256";
String response="U_eLCtj4Z8JjuD52Ny9c8ukC9kM81bBE4dwnajRFITBE5qq97IWGZ0ktpkJ59PrgNcRx0KsvrZo38xqfZsUdiWJwRfJV9COUgMiYCwMy7CB3eDiyJGnYOPEEJ8ojCE9BnI_WCsviX-iS2in0XzHOMyxmArKgrUKugjttApp2fCf-xiiQdARw7FmydfAsngO30TikIHCNVrAV7d3HluKz14jeqG88HgZZ7exoScrwNFI5FVq9OvOOY3245xqw97rBUxZ5wi7nzzAS3he4PV_g7uNTa91AubvwbnECg-5RHsXU_H7u5tyEAgQJdBRzsvfI133Phe2pqZTh4TBzrzMjRw$7x8JpF2aIV4B1GeCv-RDtdjvlpDV2EWWu1h2AG_2lV2nOe_w3OT3tJr9jpTmWq7LnhLJqHLdKV6OwIrBiiOB01RswE4Id9V1Q3R8JE0QLlzGZpfBOHDRdCHu81EHQHEooeNJV_2m6mVQDvvB2o--ebnJaBYZnCUAiFqhT0-FR3GiePnystIHCgsEpxLMR9ThXIoZUG1CC652lLKooXgd5RGM71HVAIQzXugx_T8uU9Kkqd_0IZBkgqV7VXZobmYg6z-Q9_H6EAYVzrX4GjJGHuX81gc75YMMKtzP-pLNsAiaihkarYfi2UXGngr_tOMGaot7epVomrrlsgbDTm1TQHTx1WAiV1xezX6pTyQ_G44IYF625eP0OO0ug-uQTktkAd2gIz6hgredqOldil4dEeVwksnrTA7WT6zB-KJTdHY41eDD_8Xtcj9s-BkGsU1af0ZEzAGk3_3NUEbqlVO4QCxd8a9osu6e9W9ac1dR90VxSx0zVBYm2f0PnIrXSILMICnVpKs8tkT4MmfLuo1rEGqtm9Fr44aIgAS2feBkXYhu1rTFG428ZihsperaAmqV5a7SxybKz1K7xawJ2YcS7V1GsdKZMYuR3iSaPz3icY3-WVHaNHLi8wo8ze0kDKHv8p5edjKfHmZkhzohUnFxvsvIw1O56NWmzkYprwIARmmrwqbUR4GOub4K-8ErbbSwP5djRgHlFQMjqpeUkov5P1nh_2WxMauTiss4CWuPRYMJEobOpgNYpGA4MJwBncNSLtQUY8ucG-fKU4DYfExfPF_M_uYBKH-E5uoVW9HFm0BWIagqMgc2ZtvfIf4XhsZF$AES$SHA256";
notify(response);
}
public static void notify(String response){
try {
	//开始解密
Map<String,String> jsonMap  = new HashMap<>();
	DigitalEnvelopeDTO dto = new DigitalEnvelopeDTO();
	dto.setCipherText(response);
	//InternalConfig internalConfig = InternalConfig.Factory.getInternalConfig();
	PrivateKey privateKey = InternalConfig.getISVPrivateKey(CertTypeEnum.RSA2048);
	System.out.println("privateKey: "+privateKey);
	PublicKey publicKey = InternalConfig.getYopPublicKey(CertTypeEnum.RSA2048);
	System.out.println("publicKey: "+publicKey);
	
	dto = DigitalEnvelopeUtils.decrypt(dto, privateKey, publicKey);
	System.out.println("解密结果:"+dto.getPlainText());
	jsonMap = parseResponse(dto.getPlainText());
	System.out.println(jsonMap);
} catch (Exception e) {
	throw new RuntimeException("回调解密失败！");
}
}


public static Map<String, String> parseResponse(String response){

Map<String,String> jsonMap  = new HashMap<>();
jsonMap	= JSON.parseObject(response, 
		new TypeReference<TreeMap<String,String>>() {});

return jsonMap;
}
}
