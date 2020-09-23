package com.bg.trans.entity;



public class PARA_CONSTATNTS {
	/*----------------交易代码------------------------------------*/
//	public static final String TransCode_StaticCodePay = "1001";// 静态态支付
	public static final String TransCode_ZDPay = "1001";//客户主扫支付
	public static final String TransCode_BDPay = "1002";//客户被扫支付
	public static final String TransCode_QueryPay = "1003";//支付查询
	public static final String TransCode_PayRefund = "1004";//退款
	public static final String TransCode_SettledMerchant = "1005";//商户入驻
	public static final String TransCode_QueryMerchant = "1006";//商户查询
	public static final String TransCode_QuickPayApply = "1007";//快捷支付申请
	public static final String TransCode_QuickPayConfirm = "1008";//快捷支付确认
	public static final String TransCode_GbpQuickPayApply = "1009";//同名卡快捷支付申请
	public static final String TransCode_GbpQuickPayConfirm = "1010";//同名卡快捷支付确认
	public static final String TransCode_ThreeMessageVerify = "1011";//三要素认证
	public static final String TransCode_GbpPay = "1012";//同名卡代付
	public static final String TransCode_GbpTreatyApply = "1013";//同名卡快捷协议代扣协议申请
	public static final String TransCode_GbpTreatyConfirm = "1014";//同名卡快捷协议代扣协议确认
	public static final String TransCode_GbpTreatyCollect = "1015";//同名卡快捷协议代扣
	public static final String TransCode_H5 = "1016";//H5支付申请
	public static final String TransCode_AgentPay = "1017";//代付
	public static final String TransCode_Transfer = "1018";//转账
	public static final String TransCode_Cash = "1019";//交易提现
	public static final String TransCode_BalanceCash = "1020";//余额取现
	public static final String TransCode_GZHPay = "1021";//公众号支付
	public static final String TransCode_Collect = "1022";//代扣 祼扣
	public static final String TransCode_TreatyApply = "1023";//快捷协议代扣协议申请
	public static final String TransCode_TreatyConfirm = "1024";//快捷协议代扣协议确认
	public static final String TransCode_TreatyCollect = "1025";//快捷协议代扣
	public static final String TransCode_Loan="1026";//放款
	public static final String TransCode_Repay="1027";//协议还款
	public static final String TransCode_LoanCash="1028";//放款取现
	public static final String TransCode_QueryBalance="1029";//账户余额查询
	public static final String TransCode_QueryCardBin="1030";//卡BIN查询
	public static final String TransCode_QueryBindCardList="1031";//查询绑信信息
	public static final String TransCode_UnBindTreaty="1032";//协议解约
	public static final String TransCode_Recharge="1033";//用户充值
	public static final String TransCode_CreateAccount = "2001";//账户开户
	public static final String TransCode_BindCaseCard = "2002";//绑定取现卡
	public static final String TransCode_UNBindCard = "2003";//解绑银行卡
	/*******************非支付类交易代码*******************************************/
	public static final String TransCode_CourtDataCount = "1100";//司法数据统计查询
	public static final String TransCode_CourtDataDetails = "1101";//司法数据详情查询
	public static final String TransCode_CourtMonitor = "1102";//司法信息监控
	
	/*******************支付渠道****************************/
	public static final String PayChannel_WX="01";
	public static final String PayChannel_ZFB="02";
	public static final String PayChannel_YL="03";
	public static final String PayChannel_QJ="04";//快捷支付	
	/*******************业务通道**********************************/
	public static final String Channel_KFT="01";//快付通
	public static final String Channel_HFTX="02";//汇付天下
	public static final String Channel_SD="";//杉德
//	public static final String Channel_YZF="03";//亿支付
	public static final String Channel_YBZF="04";//易宝支付
	public static final String Channel_YJF="05";//易极付
	public static final String Channel_HFTXELOAN="06";//汇付天下消金
	public static final String Channel_HFTXNSPOS="07";//汇付天下NSPOS
	public static final String Channel_LD="08";//联动优势
	public static final String Channel_GYZJ="09";//全国最高法院 中经
	/*******************红包奖励类型**********************************/
	public static final String Reward_Spread="3001";//推广奖励
	public static final String Reward_Trans="3002";//交易奖励
	public static final String Reward_ChannelSpread="3003";//渠道推广奖励
}