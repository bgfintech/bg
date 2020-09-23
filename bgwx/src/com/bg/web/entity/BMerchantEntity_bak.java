package com.bg.web.entity;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;


import javax.persistence.*;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Time;

@Entity
@Table(name = "b_merchant", schema = "")
@DynamicUpdate(true)
@DynamicInsert(true)
@PrimaryKeyJoinColumn(name = "id")
public class BMerchantEntity_bak implements java.io.Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**主键**/
    private Integer id;
    /**商户属性
     *  0：集团商户；
		1：普通商户；
		2：门店；**/
	private String isGroupMerShop;
	/**商户名称**/
	private String merName;
	/**商户简称**/
	private String merShortName;
	/**商户类型
	 *  3：企业；
		2：个体户；
		1：自然人；**/
	private String merType;
	/**商户注册地址**/
	private String addr;
	/**商户经营地址**/
    private String installAddr;
    /**营业证明文件类型
     * 	01：营业执照；
        02：事业单位法人证书；
        03：身份证件；
        04：其他证明文件；
        05：营业执照(多证合一)；
                   同一营业执照、事业单位法人证书、营业执照(多证合一)不能申请多个商户，同集团商户下的门店可使用同一证明文件
    **/
    private String netMchntSvcTp;
    /**营业证明文件号码**/
    private String comregid;
    /**经营类目**/
    private String mcc;
    /**实际经营范围**/
    private String realOptScope;
    /**证件类型**/
    private String artifCertifTp;
    /**姓名**/
    private String legalName;
    /**身份证号码**/
    private String legalId;
    /**手机号码	**/
    private String mobilephone;
    /**银联地区号**/
    private String areaCode;
    /**商户联系人**/
    private String contactor;
    /**商户联系人座机号码**/
    private String contactorTelephone;
    /**集团商户编号**/
    private String groupId;
    /**是否开通恒丰商户服务平台
     *  1：启用；
		0：停用；
		门店无效；
		外部收单机构可以不开通，如果开通必须是真实商户，法人联系人手机号校验唯一性，外网商户服务平台开通标志。
	**/
    private String enableMspfUser;
    /**结算账户名称**/
    private String receiveAccName;
    /**结算账户开户行行号**/
    private String openBank;
    /**结算账户帐号**/
    private String receiveAcc;
    /**结算账户类型
     *  1：对私；
		2：对公；
		需确保账户类型正确，否则影响清算
	**/
    private String receiveAccType;
    /**清算周期**/
    private String receiveSettlementPeriod;
    /**申请开通业务
     *  1：恒星付；
		2：POS收单；
		3：恒星付+POS收单
	**/
    private String weixinFlag;
    /**扫码手续费费率
     * 扫码手续费费率%**/
    private BigDecimal  payFee;
    /**
     * 创建时间
     */
    private String updtime;
    /**
     * 申请状态
     */
    private String status;
    
    private String merId;
    
    
    @Id
	@Column(name="id",nullable=false)
    public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	@Column(name="is_group_mer_shop",nullable=false)
	public String getIsGroupMerShop() {
		return isGroupMerShop;
	}
	public void setIsGroupMerShop(String isGroupMerShop) {
		this.isGroupMerShop = isGroupMerShop;
	}
	@Column(name="mer_name",nullable=false)
	public String getMerName() {
		return merName;
	}
	public void setMerName(String merName) {
		this.merName = merName;
	}
	@Column(name="mer_short_name",nullable=false)
	public String getMerShortName() {
		return merShortName;
	}
	public void setMerShortName(String merShortName) {
		this.merShortName = merShortName;
	}
	@Column(name="mer_type",nullable=false)
	public String getMerType() {
		return merType;
	}
	public void setMerType(String merType) {
		this.merType = merType;
	}
	@Column(name="addr",nullable=false)
	public String getAddr() {
		return addr;
	}
	public void setAddr(String addr) {
		this.addr = addr;
	}
	
	@Column(name="install_addr",nullable=false)
	public String getInstallAddr() {
		return installAddr;
	}
	public void setInstallAddr(String installAddr) {
		this.installAddr = installAddr;
	}
	
	@Column(name="net_mchnt_svc_tp",nullable=false)
	public String getNetMchntSvcTp() {
		return netMchntSvcTp;
	}
	public void setNetMchntSvcTp(String netMchntSvcTp) {
		this.netMchntSvcTp = netMchntSvcTp;
	}
	@Column(name="comreg_id",nullable=false)
	public String getComregid() {
		return comregid;
	}
	public void setComregid(String comregid) {
		this.comregid = comregid;
	}
	@Column(name="mcc",nullable=false)
	public String getMcc() {
		return mcc;
	}
	public void setMcc(String mcc) {
		this.mcc = mcc;
	}
	@Column(name="real_opt_scope",nullable=false)
	public String getRealOptScope() {
		return realOptScope;
	}
	public void setRealOptScope(String realOptScope) {
		this.realOptScope = realOptScope;
	}
	
	
	@Column(name="artif_certif_tp",nullable=false)
	public String getArtifCertifTp() {
		return artifCertifTp;
	}
	public void setArtifCertifTp(String artifCertifTp) {
		this.artifCertifTp = artifCertifTp;
	}
	
	@Column(name="legal_name",nullable=false)
	public String getLegalName() {
		return legalName;
	}
	public void setLegalName(String legalName) {
		this.legalName = legalName;
	}
	
	@Column(name="legal_id",nullable=false)
	public String getLegalId() {
		return legalId;
	}
	public void setLegalId(String legalId) {
		this.legalId = legalId;
	}
	
	@Column(name="mobile_phone",nullable=false)
	public String getMobilephone() {
		return mobilephone;
	}
	public void setMobilephone(String mobilephone) {
		this.mobilephone = mobilephone;
	}
	
	@Column(name="area_code",nullable=false)
	public String getAreaCode() {
		return areaCode;
	}
	
	
	public void setAreaCode(String areaCode) {
		this.areaCode = areaCode;
	}
	
	@Column(name="contactor",nullable=false)
	public String getContactor() {
		return contactor;
	}
	
	
	public void setContactor(String contactor) {
		this.contactor = contactor;
	}
	
	@Column(name="contactor_telephone",nullable=false)
	public String getContactorTelephone() {
		return contactorTelephone;
	}
	public void setContactorTelephone(String contactorTelephone) {
		this.contactorTelephone = contactorTelephone;
	}
	
	@Column(name="group_id",nullable=false)
	public String getGroupId() {
		return groupId;
	}
	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}
	
	@Column(name="enable_mspf_user",nullable=false)
	public String getEnableMspfUser() {
		return enableMspfUser;
	}
	public void setEnableMspfUser(String enableMspfUser) {
		this.enableMspfUser = enableMspfUser;
	}
	
	
	@Column(name="receive_acc_name",nullable=false)
	public String getReceiveAccName() {
		return receiveAccName;
	}
	public void setReceiveAccName(String receiveAccName) {
		this.receiveAccName = receiveAccName;
	}
	
	@Column(name="receive_acc",nullable=false)
	public String getReceiveAcc() {
		return receiveAcc;
	}
	public void setReceiveAcc(String receiveAcc) {
		this.receiveAcc = receiveAcc;
	}
	@Column(name="open_bank",nullable=false)
	public String getOpenBank() {
		return openBank;
	}
	public void setOpenBank(String openBank) {
		this.openBank = openBank;
	}
	@Column(name="receive_acc_type",nullable=false)
	public String getReceiveAccType() {
		return receiveAccType;
	}
	public void setReceiveAccType(String receiveAccType) {
		this.receiveAccType = receiveAccType;
	}
	
	@Column(name="receive_settlement_period",nullable=false)
	public String getReceiveSettlementPeriod() {
		return receiveSettlementPeriod;
	}
	public void setReceiveSettlementPeriod(String receiveSettlementPeriod) {
		this.receiveSettlementPeriod = receiveSettlementPeriod;
	}
	
	@Column(name="weixin_flag",nullable=false)
	public String getWeixinFlag() {
		return weixinFlag;
	}
	public void setWeixinFlag(String weixinFlag) {
		this.weixinFlag = weixinFlag;
	}
	
	@Column(name="pay_fee",nullable=false)
	public BigDecimal getPayFee() {
		return payFee;
	}
	public void setPayFee(BigDecimal payFee) {
		this.payFee = payFee;
	}
	@Column(name="upd_time",nullable=false)
	public String getUpdtime() {
		return updtime;
	}
	public void setUpdtime(String updtime) {
		this.updtime = updtime;
	}
	@Column(name="status",nullable=false)
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	@Column(name="mer_id",nullable=false)
	public String getMerId() {
		return merId;
	}
	
	public void setMerId(String merId) {
		this.merId = merId;
	}
	
    
	
}
