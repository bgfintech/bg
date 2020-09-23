package com.bg.trans.entity.gyzj;

public class Case {
	private String n_ajlx;//案件类型
	private String c_ah;//案号
	private String c_ah_ys;//原审案号
	private String c_ah_hx;//后续案号
	private String n_ajbs;//案件标识
	private String n_jbfy;//经办法院
	private String n_jbfy_cj;//法院所属层级
	private String n_ajjzjd;//案件进展阶段
	private String n_slcx;//审理程序
	private String c_ssdy;//所属地域
	private String d_larq;//立案时间
	private String n_laay;//立案案由	
	private String n_qsbdje;//起诉标的金额
	private String n_qsbdje_gj;//立案标的金额估计
	private String d_jarq;//结案时间
	private String n_jaay;//结案案由
	private String n_jabdje;//结案标的金额
	private String n_jabdje_gj;//结案标的金额估计
	private String n_jafs;//结案方式
	private String n_ssdw;//诉讼地位
	private String n_ssdw_ys;//一审诉讼地位
	private String c_gkws_glah;//相关案件号
	private String c_gkws_dsr;//当事人
	private String c_gkws_pjjg;//判决结果	
	/**
	 * 2.2
	 * n_laay_tag
	 * n_laay_tree
	 * n_qsbdje_level
	 * n_qsbdje_gj_level
	 * c_slfsxx
	 * n_jaay_tag
	 * n_jaay_tree
	 * n_jabdje_level
	 * n_jabdje_gj_level
	 * n_pj_victory
	 * n_dzzm
	 * n_dzzm_tree
	 * n_pcjg
	 * n_ccxzxje_level
	 * n_ccxzxje
	 * n_ccxzxje_gj_level
	 * n_ccxzxje_gj
	 * n_pcpcje_level
	 * n_pcpcje
	 * n_pcpcje_gj_level
	 * n_pcpcje_gj
	 * n_fzje_level
	 * n_fzje
	 * n_bqqpcje_level
	 * n_bqqpcje
	 */
	
	private String n_laay_tag;//立案案由标签
	private String n_laay_tree;//立案案由详细
	private String n_qsbdje_level;//起诉标的金额等级
	private String n_qsbdje_gj_level;//起诉标的金额估计等级
	private String c_slfsxx;//审理方式信息,厅次,时间,地点,是否公开审理
	private String n_jaay_tag;//结案案由标签
	private String n_jaay_tree;//结案案由详细
	private String n_jabdje_level;//结案标的金额等级
	private String n_jabdje_gj_level;//结案标的金额估计等级
	private String n_pj_victory;//胜诉估计
	private String n_dzzm;//定罪罪名
	private String n_dzzm_tree;//定罪罪名详细
	private String n_pcjg;//判处结果
	private String n_ccxzxje_level;//财产刑执金额等级
	private String n_ccxzxje;//财产刑执金额
	private String n_ccxzxje_gj_level;//财产刑执金额估计等级
	private String n_ccxzxje_gj;//财产刑执金额估计
	private String n_pcpcje_level;//判处赔偿金额等级
	private String n_pcpcje;//判处赔偿金额
	private String n_pcpcje_gj_level;//判处赔偿金额估计等级
	private String n_pcpcje_gj;//判处赔偿金额估计等级
	private String n_fzje_level;//犯罪金额等级
	private String n_fzje;//犯罪金额
	private String n_bqqpcje_level;//被请求赔偿金额等级
	private String n_bqqpcje;//被请求赔偿金额等级
	
	
	public String getN_fzje_level() {
		return n_fzje_level;
	}
	public void setN_fzje_level(String n_fzje_level) {
		this.n_fzje_level = n_fzje_level;
	}
	public String getN_fzje() {
		return n_fzje;
	}
	public void setN_fzje(String n_fzje) {
		this.n_fzje = n_fzje;
	}
	public String getN_bqqpcje_level() {
		return n_bqqpcje_level;
	}
	public void setN_bqqpcje_level(String n_bqqpcje_level) {
		this.n_bqqpcje_level = n_bqqpcje_level;
	}
	public String getN_bqqpcje() {
		return n_bqqpcje;
	}
	public void setN_bqqpcje(String n_bqqpcje) {
		this.n_bqqpcje = n_bqqpcje;
	}
	public String getN_dzzm() {
		return n_dzzm;
	}
	public void setN_dzzm(String n_dzzm) {
		this.n_dzzm = n_dzzm;
	}
	public String getN_dzzm_tree() {
		return n_dzzm_tree;
	}
	public void setN_dzzm_tree(String n_dzzm_tree) {
		this.n_dzzm_tree = n_dzzm_tree;
	}
	public String getN_pcjg() {
		return n_pcjg;
	}
	public void setN_pcjg(String n_pcjg) {
		this.n_pcjg = n_pcjg;
	}
	public String getN_ccxzxje_level() {
		return n_ccxzxje_level;
	}
	public void setN_ccxzxje_level(String n_ccxzxje_level) {
		this.n_ccxzxje_level = n_ccxzxje_level;
	}
	public String getN_ccxzxje() {
		return n_ccxzxje;
	}
	public void setN_ccxzxje(String n_ccxzxje) {
		this.n_ccxzxje = n_ccxzxje;
	}
	public String getN_ccxzxje_gj_level() {
		return n_ccxzxje_gj_level;
	}
	public void setN_ccxzxje_gj_level(String n_ccxzxje_gj_level) {
		this.n_ccxzxje_gj_level = n_ccxzxje_gj_level;
	}
	public String getN_ccxzxje_gj() {
		return n_ccxzxje_gj;
	}
	public void setN_ccxzxje_gj(String n_ccxzxje_gj) {
		this.n_ccxzxje_gj = n_ccxzxje_gj;
	}
	public String getN_pcpcje_level() {
		return n_pcpcje_level;
	}
	public void setN_pcpcje_level(String n_pcpcje_level) {
		this.n_pcpcje_level = n_pcpcje_level;
	}
	public String getN_pcpcje() {
		return n_pcpcje;
	}
	public void setN_pcpcje(String n_pcpcje) {
		this.n_pcpcje = n_pcpcje;
	}
	public String getN_pcpcje_gj_level() {
		return n_pcpcje_gj_level;
	}
	public void setN_pcpcje_gj_level(String n_pcpcje_gj_level) {
		this.n_pcpcje_gj_level = n_pcpcje_gj_level;
	}
	public String getN_pcpcje_gj() {
		return n_pcpcje_gj;
	}
	public void setN_pcpcje_gj(String n_pcpcje_gj) {
		this.n_pcpcje_gj = n_pcpcje_gj;
	}
	public String getN_jabdje_gj() {
		return n_jabdje_gj;
	}
	public void setN_jabdje_gj(String n_jabdje_gj) {
		this.n_jabdje_gj = n_jabdje_gj;
	}
	public String getN_laay_tag() {
		return n_laay_tag;
	}
	public void setN_laay_tag(String n_laay_tag) {
		this.n_laay_tag = n_laay_tag;
	}
	public String getN_laay_tree() {
		return n_laay_tree;
	}
	public void setN_laay_tree(String n_laay_tree) {
		this.n_laay_tree = n_laay_tree;
	}
	public String getN_qsbdje_level() {
		return n_qsbdje_level;
	}
	public void setN_qsbdje_level(String n_qsbdje_level) {
		this.n_qsbdje_level = n_qsbdje_level;
	}
	public String getN_qsbdje_gj_level() {
		return n_qsbdje_gj_level;
	}
	public void setN_qsbdje_gj_level(String n_qsbdje_gj_level) {
		this.n_qsbdje_gj_level = n_qsbdje_gj_level;
	}
	public String getC_slfsxx() {
		return c_slfsxx;
	}
	public void setC_slfsxx(String c_slfsxx) {
		this.c_slfsxx = c_slfsxx;
	}
	public String getN_jaay_tag() {
		return n_jaay_tag;
	}
	public void setN_jaay_tag(String n_jaay_tag) {
		this.n_jaay_tag = n_jaay_tag;
	}
	public String getN_jaay_tree() {
		return n_jaay_tree;
	}
	public void setN_jaay_tree(String n_jaay_tree) {
		this.n_jaay_tree = n_jaay_tree;
	}
	public String getN_jabdje_level() {
		return n_jabdje_level;
	}
	public void setN_jabdje_level(String n_jabdje_level) {
		this.n_jabdje_level = n_jabdje_level;
	}
	public String getN_jabdje_gj_level() {
		return n_jabdje_gj_level;
	}
	public void setN_jabdje_gj_level(String n_jabdje_gj_level) {
		this.n_jabdje_gj_level = n_jabdje_gj_level;
	}
	public String getN_pj_victory() {
		return n_pj_victory;
	}
	public void setN_pj_victory(String n_pj_victory) {
		this.n_pj_victory = n_pj_victory;
	}
	public String getN_ajlx() {
		return n_ajlx;
	}
	public void setN_ajlx(String n_ajlx) {
		this.n_ajlx = n_ajlx;
	}
	public String getC_ah() {
		return c_ah;
	}
	public void setC_ah(String c_ah) {
		this.c_ah = c_ah;
	}
	public String getC_ah_ys() {
		return c_ah_ys;
	}
	public void setC_ah_ys(String c_ah_ys) {
		this.c_ah_ys = c_ah_ys;
	}
	public String getC_ah_hx() {
		return c_ah_hx;
	}
	public void setC_ah_hx(String c_ah_hx) {
		this.c_ah_hx = c_ah_hx;
	}
	public String getN_ajbs() {
		return n_ajbs;
	}
	public void setN_ajbs(String n_ajbs) {
		this.n_ajbs = n_ajbs;
	}
	public String getN_jbfy() {
		return n_jbfy;
	}
	public void setN_jbfy(String n_jbfy) {
		this.n_jbfy = n_jbfy;
	}
	public String getN_jbfy_cj() {
		return n_jbfy_cj;
	}
	public void setN_jbfy_cj(String n_jbfy_cj) {
		this.n_jbfy_cj = n_jbfy_cj;
	}
	public String getN_ajjzjd() {
		return n_ajjzjd;
	}
	public void setN_ajjzjd(String n_ajjzjd) {
		this.n_ajjzjd = n_ajjzjd;
	}
	public String getN_slcx() {
		return n_slcx;
	}
	public void setN_slcx(String n_slcx) {
		this.n_slcx = n_slcx;
	}
	public String getC_ssdy() {
		return c_ssdy;
	}
	public void setC_ssdy(String c_ssdy) {
		this.c_ssdy = c_ssdy;
	}
	public String getD_larq() {
		return d_larq;
	}
	public void setD_larq(String d_larq) {
		this.d_larq = d_larq;
	}
	public String getN_laay() {
		return n_laay;
	}
	public void setN_laay(String n_laay) {
		this.n_laay = n_laay;
	}
	public String getN_qsbdje() {
		return n_qsbdje;
	}
	public void setN_qsbdje(String n_qsbdje) {
		this.n_qsbdje = n_qsbdje;
	}
	public String getN_qsbdje_gj() {
		return n_qsbdje_gj;
	}
	public void setN_qsbdje_gj(String n_qsbdje_gj) {
		this.n_qsbdje_gj = n_qsbdje_gj;
	}
	public String getD_jarq() {
		return d_jarq;
	}
	public void setD_jarq(String d_jarq) {
		this.d_jarq = d_jarq;
	}
	public String getN_jaay() {
		return n_jaay;
	}
	public void setN_jaay(String n_jaay) {
		this.n_jaay = n_jaay;
	}
	public String getN_jabdje() {
		return n_jabdje;
	}
	public void setN_jabdje(String n_jabdje) {
		this.n_jabdje = n_jabdje;
	}
	
	public String getN_jafs() {
		return n_jafs;
	}
	public void setN_jafs(String n_jafs) {
		this.n_jafs = n_jafs;
	}
	public String getN_ssdw() {
		return n_ssdw;
	}
	public void setN_ssdw(String n_ssdw) {
		this.n_ssdw = n_ssdw;
	}
	public String getN_ssdw_ys() {
		return n_ssdw_ys;
	}
	public void setN_ssdw_ys(String n_ssdw_ys) {
		this.n_ssdw_ys = n_ssdw_ys;
	}
	public String getC_gkws_glah() {
		return c_gkws_glah;
	}
	public void setC_gkws_glah(String c_gkws_glah) {
		this.c_gkws_glah = c_gkws_glah;
	}
	public String getC_gkws_dsr() {
		return c_gkws_dsr;
	}
	public void setC_gkws_dsr(String c_gkws_dsr) {
		this.c_gkws_dsr = c_gkws_dsr;
	}
	public String getC_gkws_pjjg() {
		return c_gkws_pjjg;
	}
	public void setC_gkws_pjjg(String c_gkws_pjjg) {
		this.c_gkws_pjjg = c_gkws_pjjg;
	}
		
}
