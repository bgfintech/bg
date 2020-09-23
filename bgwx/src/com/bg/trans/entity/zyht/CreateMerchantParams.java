package com.bg.trans.entity.zyht;

import java.math.BigDecimal;
import java.util.List;


public class CreateMerchantParams {
	private Merchant merchant;//	商户信息	Merchant	是	提交商户基本信息。
	private Terminal terminal;//终端信息	Terminal	是	提交终端基本信息
	private Qrcode qrcode;//台码信息	Qrcode	否	台码信息需要向我方提前申请，并给商户分配，合作方可不传
	private List<Image>	images;//影像资料	List<Image>	是	商户相关证照图片信息
	
	public Merchant getMerchant() {
		return merchant;
	}
	public void setMerchant(Merchant merchant) {
		this.merchant = merchant;
	}
	public Terminal getTerminal() {
		return terminal;
	}
	public void setTerminal(Terminal terminal) {
		this.terminal = terminal;
	}
	public Qrcode getQrcode() {
		return qrcode;
	}
	public void setQrcode(Qrcode qrcode) {
		this.qrcode = qrcode;
	}
	public List<Image> getImages() {
		return images;
	}
	public void setImages(List<Image> images) {
		this.images = images;
	}
	public class Merchant {
		private String isGroupMerShop;//商户属性	String	1	是	0：集团商户；	1：普通商户；	2：门店；
		private String merName;//商户名称	String	100	是	50中文100字符
		private String merShortName;//商户简称	String	40	是	20中文40字符
		private String merType;//商户类型	String	2	是	1：企业；	2：个体户；	4：自然人；
		private String addr;//商户注册地址	String	90	是	45中文90字符，商户注册地址,填写营业执照注册地址，个人请写住址，应真实、准确反映商户营业场所或门店所在地，一般应与受理装机地址一致。
		private String installAddr;//商户经营地址	String	60	是	30中文60字符。
		/*
		 * 	营业证明文件类型	String	2	是	01：营业执照；
			02：事业单位法人证书；
			03：身份证件；
			04：其他证明文件；
			05：营业执照(多证合一)；
			同一营业执照、事业单位法人证书、营业执照(多证合一)不能申请多个商户，同集团商户下的门店可使用同一证明文件
		 */
		private String netMchntSvcTp;
		private String comregid;//营业证明文件号码	String	40	是
		private String mcc;//经营类目	String	20	是	即银联MCC，应与跨行交易报文中传输的18域内容相同
		private String realOptScope;//实际经营范围	String	800	是
		/*
		 * 	证件类型	String	2	是	法人代表/个体户/自然人证件类型，必填。
			01：身份证(系统校验身份证)；
			02：军官证1；
			03：护照；
			04：港澳居民来往内地通行证（回乡证）；
			05：台湾同胞来往内地通行证（台胞证）；
			06：警官证；
			07：士兵证；
			08：户口簿；
			09：临时身份证；
			99：其他证件；
		 */
		private String artifCertifTp;
		private String legalName;//姓名	String	40	是	20中文40字符，法人代表/个体户/自然人姓名，会做身份核查
		private String legalId;//身份证号码	String	22	是	法人代表/个体户/自然人身份证号码
		private String mobilephone;//手机号码	String	11	是	法人代表/个体户/自然人手机号码
		private String areaCode;//银联地区号	String	4	是	银联地区号，必须到区县一级，系统会校验，请根据《银联卡跨行业务地区代码标准》选择
		private String contactor;//商户联系人	String	20	是	
		private String contactorTelephone;//商户联系人座机号码	String	20	是	商户联系人座机号码
		private String groupId;//集团商户编号	String	40	条件必输	IS_GROUP_MER_SHOP=2时，必须输入，应对应一个集团商户的MER_TEMP_ID
		private String enableMspfUser;//是否开通恒丰商户服务平台	String	1	是	1：启用；	0：停用；	门店无效；	外部收单机构可以不开通，如果开通必须是真实商户，法人联系人手机号校验唯一性，外网商户服务平台开通标志。
		private String receiveAccName;//结算账户名称	String	100	是
		private String receiveAcc;//结算账户帐号	String	20	是	
		private String openBank;//结算账户开户行行号	String	20	是	请参考开户行行号
		private String receiveAccType;//结算账户类型	String	1	是	1：对私；	2：对公；	需确保账户类型正确，否则影响清算
		private String receiveSettlementPeriod;//清算周期	String	4	是	T0、T1、T2...T15
		private String weixinFlag;//申请开通业务	String	1	是	1：恒星付；	2：POS收单；	3：恒星付+POS收单
		private BigDecimal payFee;//扫码手续费费率	decimal	4	是	扫码手续费费率%
		public String getIsGroupMerShop() {
			return isGroupMerShop;
		}
		public void setIsGroupMerShop(String isGroupMerShop) {
			this.isGroupMerShop = isGroupMerShop;
		}
		public String getMerName() {
			return merName;
		}
		public void setMerName(String merName) {
			this.merName = merName;
		}
		public String getMerShortName() {
			return merShortName;
		}
		public void setMerShortName(String merShortName) {
			this.merShortName = merShortName;
		}
		public String getMerType() {
			return merType;
		}
		public void setMerType(String merType) {
			this.merType = merType;
		}
		public String getAddr() {
			return addr;
		}
		public void setAddr(String addr) {
			this.addr = addr;
		}
		public String getInstallAddr() {
			return installAddr;
		}
		public void setInstallAddr(String installAddr) {
			this.installAddr = installAddr;
		}
		public String getNetMchntSvcTp() {
			return netMchntSvcTp;
		}
		public void setNetMchntSvcTp(String netMchntSvcTp) {
			this.netMchntSvcTp = netMchntSvcTp;
		}
		public String getComregid() {
			return comregid;
		}
		public void setComregid(String comregid) {
			this.comregid = comregid;
		}
		public String getMcc() {
			return mcc;
		}
		public void setMcc(String mcc) {
			this.mcc = mcc;
		}
		public String getRealOptScope() {
			return realOptScope;
		}
		public void setRealOptScope(String realOptScope) {
			this.realOptScope = realOptScope;
		}
		public String getArtifCertifTp() {
			return artifCertifTp;
		}
		public void setArtifCertifTp(String artifCertifTp) {
			this.artifCertifTp = artifCertifTp;
		}
		public String getLegalName() {
			return legalName;
		}
		public void setLegalName(String legalName) {
			this.legalName = legalName;
		}
		public String getLegalId() {
			return legalId;
		}
		public void setLegalId(String legalId) {
			this.legalId = legalId;
		}
		public String getMobilephone() {
			return mobilephone;
		}
		public void setMobilephone(String mobilephone) {
			this.mobilephone = mobilephone;
		}
		public String getAreaCode() {
			return areaCode;
		}
		public void setAreaCode(String areaCode) {
			this.areaCode = areaCode;
		}
		public String getContactor() {
			return contactor;
		}
		public void setContactor(String contactor) {
			this.contactor = contactor;
		}
		public String getContactorTelephone() {
			return contactorTelephone;
		}
		public void setContactorTelephone(String contactorTelephone) {
			this.contactorTelephone = contactorTelephone;
		}
		public String getGroupId() {
			return groupId;
		}
		public void setGroupId(String groupId) {
			this.groupId = groupId;
		}
		public String getEnableMspfUser() {
			return enableMspfUser;
		}
		public void setEnableMspfUser(String enableMspfUser) {
			this.enableMspfUser = enableMspfUser;
		}
		public String getReceiveAccName() {
			return receiveAccName;
		}
		public void setReceiveAccName(String receiveAccName) {
			this.receiveAccName = receiveAccName;
		}
		public String getReceiveAcc() {
			return receiveAcc;
		}
		public void setReceiveAcc(String receiveAcc) {
			this.receiveAcc = receiveAcc;
		}
		public String getOpenBank() {
			return openBank;
		}
		public void setOpenBank(String openBank) {
			this.openBank = openBank;
		}
		public String getReceiveAccType() {
			return receiveAccType;
		}
		public void setReceiveAccType(String receiveAccType) {
			this.receiveAccType = receiveAccType;
		}
		public String getReceiveSettlementPeriod() {
			return receiveSettlementPeriod;
		}
		public void setReceiveSettlementPeriod(String receiveSettlementPeriod) {
			this.receiveSettlementPeriod = receiveSettlementPeriod;
		}
		public String getWeixinFlag() {
			return weixinFlag;
		}
		public void setWeixinFlag(String weixinFlag) {
			this.weixinFlag = weixinFlag;
		}
		public BigDecimal getPayFee() {
			return payFee;
		}
		public void setPayFee(BigDecimal payFee) {
			this.payFee = payFee;
		}
		
	}
	public class Terminal {
		private String ptTermId;//直连外包平台终端号	String	20	是	需要在平台唯一，无终端，默认00000000
		private String serialNumber;//终端序列号	String	200	是	无终端可为空
		private String termStatus;//终端状态	String	2	是	0：注销；	1：启用；	默认为1，无终端可不填
		/*
		 * 	设备类型	String	4	是	01：ATM;
			02：传统POS;
			03：MPOS;
			04：智能POS;
			05：II型固话POS；
			无终端，可不填
		 */
		private String mkind;
		public String getPtTermId() {
			return ptTermId;
		}
		public void setPtTermId(String ptTermId) {
			this.ptTermId = ptTermId;
		}
		public String getSerialNumber() {
			return serialNumber;
		}
		public void setSerialNumber(String serialNumber) {
			this.serialNumber = serialNumber;
		}
		public String getTermStatus() {
			return termStatus;
		}
		public void setTermStatus(String termStatus) {
			this.termStatus = termStatus;
		}
		public String getMkind() {
			return mkind;
		}
		public void setMkind(String mkind) {
			this.mkind = mkind;
		}		
	}
	public class Qrcode{
		private String qrCodeType;//台码类型	String	1	是	1：聚合台码；		2：银联台码；
		private String qrCode;//台码编号	String	200	是	台码编号需要提前申请
		private String qrcodeStatus;//台码状态	String	12	是	0：未激活；		1：激活；		默认为1
		public String getQrCodeType() {
			return qrCodeType;
		}
		public void setQrCodeType(String qrCodeType) {
			this.qrCodeType = qrCodeType;
		}
		public String getQrCode() {
			return qrCode;
		}
		public void setQrCode(String qrCode) {
			this.qrCode = qrCode;
		}
		public String getQrcodeStatus() {
			return qrcodeStatus;
		}
		public void setQrcodeStatus(String qrcodeStatus) {
			this.qrcodeStatus = qrcodeStatus;
		}		
	}
	public class Image {
		private String imgName;//图像名称	String	40	是	图片名称
		/*
		 * 	文件类型	String	10	是	
		 * 	0800010：结算账户证明材料；
			0800012：经营证明文件；
			0800013：身份证正面；
			0800014：身份证反面；
			0800015：本人手持身份证；
			0800016：店主门头合照；
			0800017：营业场所内设照片；
			0800018：补充证明材料；
		 */
		private String fileType;
		private String imgData;//图像数据	String	不限	是	将图片转为base64格式
		public String getImgName() {
			return imgName;
		}
		public void setImgName(String imgName) {
			this.imgName = imgName;
		}
		public String getFileType() {
			return fileType;
		}
		public void setFileType(String fileType) {
			this.fileType = fileType;
		}
		public String getImgData() {
			return imgData;
		}
		public void setImgData(String imgData) {
			this.imgData = imgData;
		}
		
	}
}
