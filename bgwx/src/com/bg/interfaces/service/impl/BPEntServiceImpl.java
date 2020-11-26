package com.bg.interfaces.service.impl;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bg.interfaces.dao.IBPEntDao;
import com.bg.interfaces.entity.Trans1100Rsp;
import com.bg.interfaces.entity.Trans1101Rsp;
import com.bg.interfaces.entity.internal.BankruptDetail;
import com.bg.interfaces.entity.internal.CaseDetail;
import com.bg.interfaces.entity.internal.ImplementDetail;
import com.bg.interfaces.entity.internal.PreservationDetail;
import com.bg.interfaces.service.IBPEntService;
import com.bg.trans.dao.ITransDao;
import com.bg.trans.entity.PARA_CONSTATNTS;
import com.bg.trans.entity.TransactionEntity;
import com.bg.trans.entity.gyzj.Bankrupt;
import com.bg.trans.entity.gyzj.BankruptData;
import com.bg.trans.entity.gyzj.Case;
import com.bg.trans.entity.gyzj.CaseData;
import com.bg.trans.entity.gyzj.CaseT;
import com.bg.trans.entity.gyzj.CaseTree;
import com.bg.trans.entity.gyzj.CountDetail;
import com.bg.trans.entity.gyzj.EntoutEntity;
import com.bg.trans.entity.gyzj.EntstatEntity;
import com.bg.trans.entity.gyzj.Implement;
import com.bg.trans.entity.gyzj.ImplementData;
import com.bg.trans.entity.gyzj.Preservation;
import com.bg.trans.entity.gyzj.PreservationData;
import com.bg.trans.entity.gyzj.StatusData;
import com.bg.trans.entity.gyzj.Type;
import com.bg.trans.service.ITransBaseService;
import com.bg.trans.service.ITransGYZJService;
import com.bg.util.UtilData;
import com.bg.web.dao.IAccountDao;

@Service("bpEntService")
public class BPEntServiceImpl implements IBPEntService {
	private static Logger logger = Logger.getLogger(BPEntServiceImpl.class);
	@Autowired
	private ITransDao transDao;
	@Autowired
	private IAccountDao accountDao;
	@Autowired
	private IBPEntDao bpEntDao;	
	@Autowired
	private ITransBaseService transBaseService;
	@Autowired
	private ITransGYZJService transGYZJService;

	
	@Transactional
	@Override
	@CachePut(value = "entCache",key="'entstat:'+#account+'&'+#name+'&'+#id")
	public Object entstatService(String name, String id, String type, String t,String account) {		
		System.out.println("entstatCache#account+#name+#id:"+account+"&"+name+"&"+id);
		Trans1100Rsp rsp1100 = new Trans1100Rsp();	
		String resp_code="00";
		String resp_desc = "";	
		/************start 查询费率和账户余额********************************/
		//根据account和type查看客户的使用费用,不同客户折扣可能不同
		double fee = bpEntDao.findProductFee(account, PARA_CONSTATNTS.TransCode_CourtDataCount);
		if(fee==-1){
			logger.error("该账户 产品费率出错:"+account+":"+PARA_CONSTATNTS.TransCode_CourtDataCount);
			resp_desc="无该产品权限";
			resp_code="01";
			rsp1100.setResp_code(resp_code);
			rsp1100.setResp_desc(resp_desc);
			//String rspdata = JSONObject.toJSONString(rsp1101);
			return rsp1100;
		}
		//增加账户明细和修改账户余额
		double balance = accountDao.findAccountBalance(account);
		if(balance<fee){
			resp_desc="账户余额不足";
			resp_code="01";
			rsp1100.setResp_code(resp_code);
			rsp1100.setResp_desc(resp_desc);
			//String rspdata = JSONObject.toJSONString(rsp1101);
			return rsp1100;
		/************end 查询费率和账户余额********************************/
		}else{
			String oid = UtilData.createOnlyData();		
			TransactionEntity entity = new TransactionEntity();	
			entity.setReqno(oid);
			entity.setOrderno(oid);			
			entity.setTranstime(t);
			entity.setTrantype(PARA_CONSTATNTS.TransCode_CourtDataCount);	
			entity.setAccountid(account);
			EntstatEntity rsp = null;
			if("1".equals(type)){		
				rsp = transGYZJService.getEntstatPeople(name, id);
				entity.setName(name);
				entity.setIdcard(id);
			}else{
				rsp = transGYZJService.getEntstatOrg(name);
				entity.setName(name);
			}
			StatusData status = rsp.getStatus();			
			String trans_status = "";
			boolean isSuccess = false;
			boolean isConsume = false;
			if(status!=null){		
				int response_code = status.getError_code();
				if(response_code==0){
					trans_status="01";
					isSuccess = true;
					isConsume = true;
				}else if(response_code==-5||response_code==-7){
					trans_status="01";
					isConsume = true;
					resp_desc = "暂未查询到涉诉信息";
					resp_code="01";
					
				}else{
					trans_status="02";
					resp_desc = status.getError_message();
					resp_code="01";
				}				
			}else{
				trans_status="01";
				isSuccess = true;
				isConsume = true;
			}			
			if(isSuccess&&rsp.getData()!=null){
				Type civil = rsp.getData().getCivil();
				if(civil!=null&&civil.getCount()!=null){
					CountDetail cd = civil.getCount();
					com.bg.interfaces.entity.internal.CountDetail cdr = transformCount(cd);
					rsp1100.setCivil(cdr);
				}
				Type implement = rsp.getData().getImplement();
				if(implement!=null&&implement.getCount()!=null){
					CountDetail cd = implement.getCount();
					com.bg.interfaces.entity.internal.CountDetail cdr = transformCount(cd);
					rsp1100.setImplement(cdr);
				}
				Type preservation = rsp.getData().getPreservation();
				if(preservation!=null&&preservation.getCount()!=null){
					CountDetail cd = preservation.getCount();
					com.bg.interfaces.entity.internal.CountDetail cdr = transformCount(cd);
					rsp1100.setPreservation(cdr);
				}
				Type criminal = rsp.getData().getCriminal();
				if(criminal!=null&&criminal.getCount()!=null){
					CountDetail cd = criminal.getCount();
					com.bg.interfaces.entity.internal.CountDetail cdr = transformCount(cd);
					rsp1100.setCriminal(cdr);
				}
				Type administrative = rsp.getData().getAdministrative();
				if(administrative!=null&&administrative.getCount()!=null){
					CountDetail cd = administrative.getCount();
					com.bg.interfaces.entity.internal.CountDetail cdr = transformCount(cd);
					rsp1100.setAdministrative(cdr);
				}
				Type bankrupt = rsp.getData().getBankrupt();
				if(bankrupt!=null&&bankrupt.getCount()!=null){
					CountDetail cd = bankrupt.getCount();
					com.bg.interfaces.entity.internal.CountDetail cdr = transformCount(cd);
					rsp1100.setBankrupt(cdr);
				}
			}
			rsp1100.setResp_code(resp_code);
			rsp1100.setResp_desc(resp_desc);
			String rspdata = JSONObject.toJSONString(rsp1100);
	        entity.setStatus(trans_status);
	        String rspJson = JSONObject.toJSONString(rsp);	        
	        entity.setRespcontent(rspJson.substring(0,rspJson.length()<4999?rspJson.length():4999));
	        entity.setSecrespcontent(rspdata);
			Integer linkno = transBaseService.insertTransaction(entity);	
			if(isConsume){
				consume(account,PARA_CONSTATNTS.TransCode_CourtDataCount,linkno.toString(),balance,fee);
			}
			return rsp1100;
		}		
	}
	@Transactional
	@Override
	@CachePut(value = "entCache",key="'entout:'+#account+'&'+#name+'&'+#id")
	public Object entoutService(String name, String id, String type, String t,String account) {
		System.out.println("entoutCache#account+#name+#id:"+account+"&"+name+"&"+id);
		Trans1101Rsp rsp1101 = new Trans1101Rsp();	
		String resp_code="00";
		String resp_desc = "";		
		/************start 查询费率和账户余额********************************/
		//根据account和type查看客户的使用费用,不同客户折扣可能不同
		double fee = bpEntDao.findProductFee(account, PARA_CONSTATNTS.TransCode_CourtDataDetails);
		if(fee==-1){
			logger.error("该账户 产品费率出错:"+account+":"+PARA_CONSTATNTS.TransCode_CourtDataDetails);
			resp_desc="无该产品权限";
			resp_code="01";
			rsp1101.setResp_code(resp_code);
			rsp1101.setResp_desc(resp_desc);
			//String rspdata = JSONObject.toJSONString(rsp1101);
			return rsp1101;
		}
		//增加账户明细和修改账户余额
		double balance = accountDao.findAccountBalance(account);
		if(balance<fee){
			resp_desc="账户余额不足";
			resp_code="01";
			rsp1101.setResp_code(resp_code);
			rsp1101.setResp_desc(resp_desc);
			//String rspdata = JSONObject.toJSONString(rsp1101);
			return rsp1101;
		/************end 查询费率和账户余额********************************/
		}else{
			String oid = UtilData.createOnlyData();		
			TransactionEntity entity = new TransactionEntity();	
			entity.setReqno(oid);
			entity.setOrderno(oid);
			
			entity.setTranstime(t);
			entity.setTrantype(PARA_CONSTATNTS.TransCode_CourtDataDetails);		
			entity.setAccountid(account);
			EntoutEntity rsp = null;
			if("1".equals(type)){		
				rsp = transGYZJService.getEntoutPeople(name, id);
				entity.setName(name);
				entity.setIdcard(id);
			}else{
				rsp = transGYZJService.getEntoutOrg(name);
				entity.setName(name);
			}
			StatusData status = rsp.getStatus();			
			String trans_status = "";
			boolean isSuccess = false;
			boolean isConsume = false;
			if(status!=null){		
				int response_code = status.getError_code();
				if(response_code==0){
					trans_status="01";
					isSuccess = true;
					isConsume = true;
				}else if(response_code==-5||response_code==-7){
					trans_status="01";
					isConsume = true;
					resp_desc = "暂未查询到涉诉信息";
					resp_code="01";
					
				}else{
					trans_status="02";
					resp_desc = status.getError_message();
					resp_code="01";
				}				
			}else{
				trans_status="01";
				isSuccess = true;
				isConsume = true;
			}
			if(isSuccess&&rsp.getData()!=null){
				CaseData civil = rsp.getData().getCivil();
				if(civil!=null&&civil.getCount()!=null){
					CaseDetail caseDetail = new CaseDetail();
					CountDetail cd = civil.getCount();
					com.bg.interfaces.entity.internal.CountDetail cdr = transformCount(cd);
					caseDetail.setCount(cdr);
					List<Case> cases = civil.getCases();
					List<com.bg.interfaces.entity.internal.Case> rcases = transformCases(cases);
					caseDetail.setCases(rcases);
					rsp1101.setCivil(caseDetail);
				}
				ImplementData implement = rsp.getData().getImplement();
				if(implement!=null&&implement.getCount()!=null){
					ImplementDetail implementDetail = new ImplementDetail();
					CountDetail cd = implement.getCount();
					com.bg.interfaces.entity.internal.CountDetail cdr = transformCount(cd);
					implementDetail.setCount(cdr);
					List<Implement> cases = implement.getCases();
					List<com.bg.interfaces.entity.internal.Implement> rcases = transformImplement(cases);
					implementDetail.setCases(rcases);
					rsp1101.setImplement(implementDetail);
				}
				CaseData criminal = rsp.getData().getCriminal();
				if(criminal!=null&&criminal.getCount()!=null){
					CaseDetail caseDetail = new CaseDetail();
					CountDetail cd = criminal.getCount();
					com.bg.interfaces.entity.internal.CountDetail cdr = transformCount(cd);
					caseDetail.setCount(cdr);
					List<Case> cases = criminal.getCases();
					List<com.bg.interfaces.entity.internal.Case> rcases = transformCases(cases);
					caseDetail.setCases(rcases);
					rsp1101.setCriminal(caseDetail);
				}
				CaseData administrative = rsp.getData().getAdministrative();
				if(administrative!=null&&administrative.getCount()!=null){
					CaseDetail caseDetail = new CaseDetail();
					CountDetail cd = administrative.getCount();
					com.bg.interfaces.entity.internal.CountDetail cdr = transformCount(cd);
					caseDetail.setCount(cdr);
					List<Case> cases = administrative.getCases();
					List<com.bg.interfaces.entity.internal.Case> rcases = transformCases(cases);
					caseDetail.setCases(rcases);
					rsp1101.setAdministrative(caseDetail);
				}
				BankruptData bankrupt = rsp.getData().getBankrupt();
				if(bankrupt!=null&&bankrupt.getCount()!=null){
					BankruptDetail bankruptDetail = new BankruptDetail();
					CountDetail cd = bankrupt.getCount();
					com.bg.interfaces.entity.internal.CountDetail cdr = transformCount(cd);
					bankruptDetail.setCount(cdr);
					List<Bankrupt> cases = bankrupt.getCases();
					List<com.bg.interfaces.entity.internal.Bankrupt> rcases = transformBankrupt(cases);
					bankruptDetail.setCases(rcases);
					rsp1101.setBankrupt(bankruptDetail);
				}
				PreservationData preservation = rsp.getData().getPreservation();
				if(preservation!=null&&preservation.getCount()!=null){
					PreservationDetail preservationDetail = new PreservationDetail();
					CountDetail cd = preservation.getCount();
					com.bg.interfaces.entity.internal.CountDetail cdr = transformCount(cd);
					preservationDetail.setCount(cdr);
					List<Preservation> cases = preservation.getCases();
					List<com.bg.interfaces.entity.internal.Preservation> rcases = transformPreservation(cases);
					preservationDetail.setCases(rcases);
					rsp1101.setPreservation(preservationDetail);
				}
				CaseTree ect = rsp.getData().getCases_tree();
				if(ect!=null){
					List<com.bg.interfaces.entity.internal.CaseT> civilList = null;
					List<com.bg.interfaces.entity.internal.CaseT> criminalList = null;
					List<com.bg.interfaces.entity.internal.CaseT> adminList = null;
					List<com.bg.interfaces.entity.internal.CaseT> implementList = null;
					List<com.bg.interfaces.entity.internal.CaseT> bankruptList = null;
					List<com.bg.interfaces.entity.internal.CaseT> preservationList = null;
					if(ect.getCivil()!=null){
						List<CaseT> cs = ect.getCivil();
						civilList = transformCaseTree(cs);
					}
					if(ect.getCriminal()!=null){
						List<CaseT> cs = ect.getCriminal();
						criminalList = transformCaseTree(cs);
					}
					if(ect.getAdministrative()!=null){
						List<CaseT> cs = ect.getAdministrative();
						adminList = transformCaseTree(cs);
					}
					if(ect.getImplement()!=null){
						List<CaseT> cs = ect.getImplement();
						implementList = transformCaseTree(cs);
					}
					if(ect.getBankrupt()!=null){
						List<CaseT> cs = ect.getBankrupt();
						bankruptList = transformCaseTree(cs);
					}
					if(ect.getPreservation()!=null){
						List<CaseT> cs = ect.getPreservation();
						preservationList = transformCaseTree(cs);
					}
					if(civilList!=null||criminalList!=null||adminList!=null||implementList!=null||bankruptList!=null||preservationList!=null){
						com.bg.interfaces.entity.internal.CaseTree ct = new com.bg.interfaces.entity.internal.CaseTree();
						ct.setCivil(civilList);
						ct.setCriminal(criminalList);
						ct.setAdministrative(adminList);
						ct.setImplement(implementList);
						ct.setBankrupt(bankruptList);
						ct.setPreservation(preservationList);
						rsp1101.setCases_tree(ct);
					}
				}
				
			}
			rsp1101.setResp_code(resp_code);
			rsp1101.setResp_desc(resp_desc);
			String rspdata = JSONObject.toJSONString(rsp1101);
	        entity.setStatus(trans_status);
	        String rspJson = JSONObject.toJSONString(rsp);	        
	        entity.setRespcontent(rspJson.substring(0,rspJson.length()<4999?rspJson.length():4999));
	        entity.setSecrespcontent(rspdata);
			Integer linkno = transBaseService.insertTransaction(entity);	
			if(isConsume){
				consume(account,PARA_CONSTATNTS.TransCode_CourtDataDetails,linkno.toString(),balance,fee);
			}
			return rsp1101;
		}		
	}

	
	private List<com.bg.interfaces.entity.internal.CaseT> transformCaseTree(List<CaseT> cs) {
		List<com.bg.interfaces.entity.internal.CaseT> icts = new ArrayList<com.bg.interfaces.entity.internal.CaseT>();
		for(CaseT ct: cs){			
			com.bg.interfaces.entity.internal.CaseT ict = transformCaseT(ct);
			icts.add(ict);
		}		
		return icts;
	}
	
	private com.bg.interfaces.entity.internal.CaseT transformCaseT(CaseT ct){
		com.bg.interfaces.entity.internal.CaseT ict = new com.bg.interfaces.entity.internal.CaseT();
		ict.setC_ah(ct.getC_ah());
		ict.setN_ajbs(ct.getN_ajbs());
		if(ct.getCase_type()==null){
			ict.setCase_type("");
		}else{
			switch (ct.getCase_type()) {
			case "200":
				ict.setCase_type("刑事");
				break;
			case "300":
				ict.setCase_type("民事");
				break;
			case "400":
				ict.setCase_type("行政");
				break;
			case "1000":
				ict.setCase_type("执行");
				break;
			case "1100":
				ict.setCase_type("清算破产");
				break;
			default:
				ict.setCase_type(ct.getCase_type());
				break;
			}
		}
		if(ct.getStage_type()==null){
			ict.setStage_type("");
		}else{
			switch (ct.getStage_type()) {
			case "1":
				ict.setStage_type("一审");
				break;
			case "2":
				ict.setStage_type("二审");
				break;
			case "3":
				ict.setStage_type("再审审查");
				break;
			case "4":
				ict.setStage_type("再审");
				break;
			case "5":
				ict.setStage_type("执行");
				break;
			default:
				ict.setStage_type(ct.getStage_type());
				break;
			}
		}	
		
		if(ct.getNext()!=null){
			com.bg.interfaces.entity.internal.CaseT nict = transformCaseT(ct.getNext());
			ict.setNext(nict);
		}		
		return ict;
	}
	
	private com.bg.interfaces.entity.internal.CountDetail transformCount(CountDetail cd){
		com.bg.interfaces.entity.internal.CountDetail cdr = new com.bg.interfaces.entity.internal.CountDetail();
		cdr.setCount_yuangao(cd.getCount_yuangao()==null?null:cd.getCount_yuangao().toString());
		cdr.setCount_wei_yuangao(cd.getCount_wei_yuangao()==null?null:cd.getCount_wei_yuangao().toString());
		cdr.setCount_beigao(cd.getCount_beigao()==null?null:cd.getCount_beigao().toString());
		cdr.setCount_wei_beigao(cd.getCount_wei_beigao()==null?null:cd.getCount_wei_beigao().toString());
		cdr.setCount_other(cd.getCount_other()==null?null:cd.getCount_other().toString());
		cdr.setCount_wei_other(cd.getCount_wei_other()==null?null:cd.getCount_wei_other().toString());
		cdr.setMoney_yuangao(cd.getMoney_yuangao()==null?null:transGYZJService.transformMoney(cd.getMoney_yuangao()));
		cdr.setMoney_wei_yuangao(cd.getMoney_wei_yuangao()==null?null:transGYZJService.transformMoney(cd.getMoney_wei_yuangao()));
		cdr.setMoney_beigao(cd.getMoney_beigao()==null?null:transGYZJService.transformMoney(cd.getMoney_beigao()));
		cdr.setMoney_wei_beigao(cd.getMoney_wei_beigao()==null?null:transGYZJService.transformMoney(cd.getMoney_wei_beigao()));
		cdr.setMoney_other(cd.getMoney_other()==null?null:transGYZJService.transformMoney(cd.getMoney_other()));
		cdr.setMoney_wei_other(cd.getMoney_wei_other()==null?null:transGYZJService.transformMoney(cd.getMoney_wei_other()));
		cdr.setArea_stat(cd.getArea_stat());
		cdr.setAy_stat(cd.getAy_stat());
		cdr.setLarq_stat(cd.getLarq_stat());
		cdr.setJafs_stat(cd.getJafs_stat());	
		return cdr;
	}
	private List<com.bg.interfaces.entity.internal.Preservation> transformPreservation(List<Preservation> pr) {
		if(pr==null||pr.size()==0){
			return null;
		}
		List<com.bg.interfaces.entity.internal.Preservation> rpr = new ArrayList<com.bg.interfaces.entity.internal.Preservation>();		
		for(Preservation c:pr){
			com.bg.interfaces.entity.internal.Preservation rc = new com.bg.interfaces.entity.internal.Preservation();
			rc.setC_ah(c.getC_ah());
			rc.setN_ajlx(c.getN_ajlx());
			rc.setN_ajbs(c.getN_ajbs());
			String ahys = null;
			ahys = c.getC_ah_ys()==null||"暂无".equals(c.getC_ah_ys())?null:c.getC_ah_ys().indexOf(":")==-1?c.getC_ah_ys():c.getC_ah_ys().substring(0,c.getC_ah_ys().indexOf(":"));
			String ahhx = null;
			ahhx = c.getC_ah_hx()==null||"暂无".equals(c.getC_ah_hx())?null:c.getC_ah_hx().indexOf(":")==-1?c.getC_ah_hx():c.getC_ah_hx().substring(0,c.getC_ah_hx().indexOf(":"));
			rc.setC_ah_ys(ahys);
			rc.setC_ah_hx(ahhx);
			rc.setN_jbfy(c.getN_jbfy());
			rc.setN_ajjzjd(c.getN_ajjzjd());
			rc.setD_larq(c.getD_larq());
			
			rc.setN_sqbqse(c.getN_sqbqse()==null&&c.getN_sqbqse_level()!=null?transGYZJService.transformMoney(Integer.parseInt(c.getN_sqbqse_level())):c.getN_sqbqse());
			rc.setC_sqbqbdw(c.getC_sqbqbdw());			
			rc.setD_jarq(c.getD_jarq());			
			rc.setN_jafs(c.getN_jafs());
			rc.setN_ssdw(c.getN_ssdw());			
			rc.setC_gkws_glah(c.getC_gkws_glah());
			rc.setC_gkws_dsr(c.getC_gkws_dsr());
			rc.setC_gkws_pjjg(c.getC_gkws_pjjg());
			rpr.add(rc);
		}
		return rpr;
	}
	
	private List<com.bg.interfaces.entity.internal.Bankrupt> transformBankrupt(List<Bankrupt> bs) {
		if(bs==null||bs.size()==0){
			return null;
		}
		List<com.bg.interfaces.entity.internal.Bankrupt> rbs = new ArrayList<com.bg.interfaces.entity.internal.Bankrupt>();		
		for(Bankrupt c:bs){
			com.bg.interfaces.entity.internal.Bankrupt rc = new com.bg.interfaces.entity.internal.Bankrupt();
			rc.setC_ah(c.getC_ah());
			rc.setN_ajlx(c.getN_ajlx());
			rc.setN_ajbs(c.getN_ajbs());
			rc.setN_jbfy(c.getN_jbfy());
			rc.setN_ajjzjd(c.getN_ajjzjd());
			rc.setD_larq(c.getD_larq());
			rc.setD_jarq(c.getD_jarq());
			rc.setN_jafs(c.getN_jafs());
			rc.setN_ssdw(c.getN_ssdw());
			rc.setC_gkws_glah(c.getC_gkws_glah());
			rc.setC_gkws_dsr(c.getC_gkws_dsr());
			rc.setC_gkws_pjjg(c.getC_gkws_pjjg());
			rbs.add(rc);
		}
		return rbs;
	}

	private List<com.bg.interfaces.entity.internal.Implement> transformImplement(List<Implement> imps){
		if(imps==null||imps.size()==0){
			return null;
		}
		List<com.bg.interfaces.entity.internal.Implement> rimps = new ArrayList<com.bg.interfaces.entity.internal.Implement>();		
		for(Implement c:imps){
			com.bg.interfaces.entity.internal.Implement rc = new com.bg.interfaces.entity.internal.Implement();
			rc.setN_ajlx(c.getN_ajlx());
			rc.setC_ah(c.getC_ah());
			rc.setN_ajlx(c.getN_ajlx());
			rc.setN_ajbs(c.getN_ajbs());
			String ahys = null;
			ahys = c.getC_ah_ys()==null||"暂无".equals(c.getC_ah_ys())?null:c.getC_ah_ys().indexOf(":")==-1?c.getC_ah_ys():c.getC_ah_ys().substring(0,c.getC_ah_ys().indexOf(":"));
			String ahhx = null;
			ahhx = c.getC_ah_hx()==null||"暂无".equals(c.getC_ah_hx())?null:c.getC_ah_hx().indexOf(":")==-1?c.getC_ah_hx():c.getC_ah_hx().substring(0,c.getC_ah_hx().indexOf(":"));
			rc.setC_ah_ys(ahys);
			rc.setC_ah_hx(ahhx);
			rc.setN_jbfy(c.getN_jbfy());
			rc.setN_ajjzjd(c.getN_ajjzjd());
			rc.setD_larq(c.getD_larq());
			rc.setN_laay(c.getN_laay());
			rc.setN_sqzxbdje(c.getN_sqzxbdje());
			rc.setD_jarq(c.getD_jarq());
			rc.setN_jaay(c.getN_jaay());
			rc.setN_jabdje(c.getN_jabdje());
			rc.setN_sjdwje(c.getN_sjdwje());
			rc.setN_jafs(c.getN_jafs());
			rc.setN_ssdw(c.getN_ssdw());
			rc.setC_gkws_glah(c.getC_gkws_glah());
			rc.setC_gkws_dsr(c.getC_gkws_dsr());
			rc.setC_gkws_pjjg(c.getC_gkws_pjjg());
			rimps.add(rc);
		}
		return rimps;
	}
	
	private List<com.bg.interfaces.entity.internal.Case> transformCases(List<Case> cases) {
		if(cases==null||cases.size()==0){
			return null;
		}
		List<com.bg.interfaces.entity.internal.Case> rcases = new ArrayList<com.bg.interfaces.entity.internal.Case>();		
		for(Case c:cases){
			com.bg.interfaces.entity.internal.Case rc = new com.bg.interfaces.entity.internal.Case();
			rc.setC_ah(c.getC_ah());
			rc.setN_ajlx(c.getN_ajlx());
			rc.setN_ajbs(c.getN_ajbs());
			String ahys = null;
			ahys = c.getC_ah_ys()==null||"暂无".equals(c.getC_ah_ys())?null:c.getC_ah_ys().indexOf(":")==-1?c.getC_ah_ys():c.getC_ah_ys().substring(0,c.getC_ah_ys().indexOf(":"));
			String ahhx = null;
			ahhx = c.getC_ah_hx()==null||"暂无".equals(c.getC_ah_hx())?null:c.getC_ah_hx().indexOf(":")==-1?c.getC_ah_hx():c.getC_ah_hx().substring(0,c.getC_ah_hx().indexOf(":"));
			rc.setC_ah_ys(ahys);
			rc.setC_ah_hx(ahhx);
			rc.setN_jbfy(c.getN_jbfy());
			rc.setN_ajjzjd(c.getN_ajjzjd());
			rc.setN_slcx(c.getN_slcx());
			rc.setD_larq(c.getD_larq());
			String laay = null;
			if(c.getN_laay_tree()!=null&&!"".equals(c.getN_laay_tree())){
				String[] lt = c.getN_laay_tree().split(",");
				laay = lt[lt.length-1];
			}else{
				laay = c.getN_laay();
			}
			
			rc.setN_laay(laay);			
			rc.setN_qsbdje(c.getN_qsbdje()==null&&c.getN_qsbdje_level()!=null?transGYZJService.transformMoney(Integer.parseInt(c.getN_qsbdje_level())):c.getN_qsbdje());
			rc.setD_jarq(c.getD_jarq());
			
			String jaay = null;
			if(c.getN_jaay_tree()!=null&&!"".equals(c.getN_jaay_tree())){
				String[] jt = c.getN_jaay_tree().split(",");
				jaay = jt[jt.length-1];
			}else{
				jaay = c.getN_jaay();
			}
			
			rc.setN_jaay(jaay);		
			rc.setN_jabdje(c.getN_jabdje()==null&&c.getN_jabdje_level()!=null?transGYZJService.transformMoney(Integer.parseInt(c.getN_jabdje_level())):c.getN_jabdje());
			rc.setN_jafs(c.getN_jafs());
			rc.setN_ssdw(c.getN_ssdw());
			rc.setN_ssdw_ys(c.getN_ssdw_ys());
			rc.setC_gkws_glah(c.getC_gkws_glah());
			rc.setC_gkws_dsr(c.getC_gkws_dsr());
			rc.setC_gkws_pjjg(c.getC_gkws_pjjg());
			/**
			 * 2.2
			 * 
			 */
			String ts = c.getC_slfsxx();
			if(ts!=null&&!"".equals(ts)){
				String tmp = "";
				String[] rs = ts.split(";");
				int i = 0;
				for(String rm:rs){					
					String[] ls = rm.split(",");
					if(i!=0){
						tmp+="<br>";
					}
					if(ls.length!=4){
						tmp += rm;
					}else{
						tmp+="厅次:["+ls[0]+"] 时间:["+ls[1]+"] 地点:["+ls[2]+"] 是否公开审理:["+("1".equals(ls[3])?"是":"否")+"]";
					}
					i++;
				}
				ts = tmp;
			}		
			rc.setC_slfsxx(ts);	
			rc.setN_pcjg(c.getN_pcjg());
			rc.setN_fzje(c.getN_fzje()==null&&c.getN_fzje_level()!=null?transGYZJService.transformMoney(Integer.parseInt(c.getN_fzje_level())):c.getN_fzje());
			rc.setN_bqqpcje(c.getN_bqqpcje()==null&&c.getN_bqqpcje_level()!=null?transGYZJService.transformMoney(Integer.parseInt(c.getN_bqqpcje_level())):c.getN_bqqpcje());
			rc.setN_ccxzxje(c.getN_ccxzxje()==null&&c.getN_ccxzxje_level()!=null?transGYZJService.transformMoney(Integer.parseInt(c.getN_ccxzxje_level())):c.getN_ccxzxje());
			rc.setN_pcpcje(c.getN_pcpcje()==null&&c.getN_pcpcje_level()!=null?transGYZJService.transformMoney(Integer.parseInt(c.getN_pcpcje_level())):c.getN_pcpcje());			
			rcases.add(rc);
		}
		return rcases;
	}
	/**
	 * 涉诉查询消费扣款
	 * @param account
	 * @param type
	 */
	private void consume(String account,String type,String linkno,double balance,double fee){				
		double after = UtilData.subtract(balance, fee);
		transDao.insertAAccountDetail(account,-fee,balance,after,type,linkno,"b_transaction");
		transDao.updateAccountBalance(account,after,new Date());		
	}

	@Override
	public String expensesRecord(String account,String sdate, String edate) {
		System.out.println(account+sdate+edate);
		List<Map<String,Object>> list = bpEntDao.findAccountDetail(account, sdate, edate);
		RspRecord rr = new RspRecord();
		DecimalFormat df = new DecimalFormat("0.00");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
		String balance = "0.00";
		System.out.println(JSONObject.toJSONString("list:"+list));
		if(list.size()>0){
			List<TransJson> tl = new ArrayList<TransJson>();
			for(Map<String,Object> m:list){
				balance = df.format(m.get("BALANCE"));
				TransJson tj = new TransJson();
				tj.transtime = sdf.format(m.get("INPUTTIME"));
				rr.balance = balance;
				tj.amt = df.format(m.get("BALANCEAMT"));
				tj.type = transformType((String)m.get("OCCURTYPE"));
				tl.add(tj);
			}
			rr.tranlist = tl;	
			System.out.println("rrr:"+JSONObject.toJSONString(rr));
		}
		return JSONObject.toJSONString(rr);
	}
	
	private String transformType(String typeCode){
		String typename = "";
		switch (typeCode) {
		case "1100":
			typename = "风险信息统计";
			break;
		case "1101":
			typename = "风险信息详情";
			break;
		default:
			typename="交易";
			break;
		}		
		return typename;
	}
	
	class RspRecord {
		String balance;
		List<TransJson> tranlist;
		public String getBalance() {
			return balance;
		}
		public void setBalance(String balance) {
			this.balance = balance;
		}
		public List<TransJson> getTranlist() {
			return tranlist;
		}
		public void setTranlist(List<TransJson> tranlist) {
			this.tranlist = tranlist;
		}
		
	}
	
	class TransJson {
		String transtime;
		String type;
		String amt;
		public String getTranstime() {
			return transtime;
		}
		public void setTranstime(String transtime) {
			this.transtime = transtime;
		}
		public String getType() {
			return type;
		}
		public void setType(String type) {
			this.type = type;
		}
		public String getAmt() {
			return amt;
		}
		public void setAmt(String amt) {
			this.amt = amt;
		}
		
	}
	@Override
	@Cacheable(value = "entCache",key="'entstat:'+#account+'&'+#name+'&'+#id",condition="")
	public Object entstatCache(String account,String name,String id) {
		System.out.println("没有缓存:"+account+" name:"+name+" id:"+id);
		return null;
	}
	
	@Override
	@Cacheable(value = "entCache",key="'entout:'+#account+'&'+#name+'&'+#id",condition="")
	public Object entoutCache(String account,String name,String id) {
		System.out.println("没有缓存:"+account+" name:"+name+" id:"+id);
		return null;
	}
	
	@Override
	public List<Map<String, Object>> findTransListDesc(String account) {
		Date date = new Date();//获取当前时间    
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.HOUR, -1);
		Date d = calendar.getTime();
		return bpEntDao.findTransList(account, d);
	}
	
	public static void main(String[] arg){
		String s = "{\"status\":{\"status\":-1,\"error_code\":-5,\"error_message\":\"没有找到\"}}";
		EntoutEntity entity = JSONObject.parseObject(s, EntoutEntity.class);
		System.out.println(entity.getStatus().getError_code()==-5);
	}
	@Override
	public int countMonitorList(String account) {
		return bpEntDao.countMonitorList(account);
	}
	@Override
	public List<Map<String, Object>> findMonitorList(String account,int offset, int limit) {
		return bpEntDao.findMonitorList(account,offset,limit);
	}
	@Override
	public boolean isMonitorByCustomer(String name, String idcard) {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public String setMonitor(String name, String idcard,String type, Date sdate,int days,String account) {
		/***********判断费用是否够****************/
		double discount = bpEntDao.findProductFee(account, PARA_CONSTATNTS.TransCode_CourtMonitor);
		if(discount==-1){
			logger.error("该账户 产品费率出错:"+account+":"+PARA_CONSTATNTS.TransCode_CourtMonitor);
			return "没有该产品权限";
		}
		/*
		 * days 代表是月数
		 * 
		 * fees = {60,150,200};
		 * 
		 * 
		 */
		Date ed = null;
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(sdate);
		double fee = 0;
		switch (days) {
		case 1:
			fee = 60*discount;			
			calendar.add(Calendar.MONTH, 1);			
			ed = calendar.getTime();
			break;
		case 3:
			fee = 150*discount;
			calendar.add(Calendar.MONTH, 3);			
			ed = calendar.getTime();
			break;
		case 6:
			fee = 200*discount;
			calendar.add(Calendar.MONTH, 6);			
			ed = calendar.getTime();
			break;
		default:
			return "产品期数设置错误";
		}		
		double balance = accountDao.findAccountBalance(account);
		if(balance<fee){
			return "余额不足";			
		}		
		/*************查询该客户的统计信息***************/
		String resp_code=null;
		String resp_desc=null;
		Trans1100Rsp rsp1100 = new Trans1100Rsp();	
		Date d = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		String oid = UtilData.createOnlyData();		
		TransactionEntity entity = new TransactionEntity();	
		entity.setReqno(oid);
		entity.setOrderno(oid);
		entity.setName(name);
		entity.setIdcard(idcard);
		entity.setTranstime(sdf.format(d));
		entity.setTrantype(PARA_CONSTATNTS.TransCode_CourtDataCount);	
		entity.setAccountid(account);
		EntstatEntity rsp = null;
		if("1".equals(type)){		
			rsp = transGYZJService.getEntstatPeople(name, idcard);
		}else{
			rsp = transGYZJService.getEntstatOrg(name);
		}
		StatusData status = rsp.getStatus();		
		String trans_status = "";
		boolean isSuccess = false;
		if(status!=null){		
			int response_code = status.getError_code();
			if(response_code==0){
				trans_status="01";
				isSuccess = true;
			}else if(response_code==-5||response_code==-7){
				trans_status="01";
				resp_desc = status.getError_message();
				resp_code="01";				
			}else{
				trans_status="02";
				resp_desc = status.getError_message();
				resp_code="01";
				return "请检查数据是否正确,设置失败";
			}				
		}else{
			trans_status="01";
			isSuccess = true;
		}			
		if(isSuccess&&rsp.getData()!=null){
			Type civil = rsp.getData().getCivil();
			if(civil!=null&&civil.getCount()!=null){
				CountDetail cd = civil.getCount();
				com.bg.interfaces.entity.internal.CountDetail cdr = transformCount(cd);
				rsp1100.setCivil(cdr);
			}
			Type implement = rsp.getData().getImplement();
			if(implement!=null&&implement.getCount()!=null){
				CountDetail cd = implement.getCount();
				com.bg.interfaces.entity.internal.CountDetail cdr = transformCount(cd);
				rsp1100.setImplement(cdr);
			}
			Type criminal = rsp.getData().getCriminal();
			if(criminal!=null&&criminal.getCount()!=null){
				CountDetail cd = criminal.getCount();
				com.bg.interfaces.entity.internal.CountDetail cdr = transformCount(cd);
				rsp1100.setCriminal(cdr);
			}
			Type administrative = rsp.getData().getAdministrative();
			if(administrative!=null&&administrative.getCount()!=null){
				CountDetail cd = administrative.getCount();
				com.bg.interfaces.entity.internal.CountDetail cdr = transformCount(cd);
				rsp1100.setAdministrative(cdr);
			}
			Type bankrupt = rsp.getData().getBankrupt();
			if(bankrupt!=null&&bankrupt.getCount()!=null){
				CountDetail cd = bankrupt.getCount();
				com.bg.interfaces.entity.internal.CountDetail cdr = transformCount(cd);
				rsp1100.setBankrupt(cdr);
			}
		}
		rsp1100.setResp_code(resp_code);
		rsp1100.setResp_desc(resp_desc);
		String rspdata = JSONObject.toJSONString(rsp1100);
        entity.setStatus(trans_status);
        String rspJson = JSONObject.toJSONString(rsp);	        
        entity.setRespcontent(rspJson.substring(0,rspJson.length()<4999?rspJson.length():4999));
        entity.setSecrespcontent(rspdata.substring(0,rspdata.length()<4999?rspdata.length():4999));
		Integer linkno = transBaseService.insertTransaction(entity);
		/*************设置监控记录*****************/
		int mid = bpEntDao.setMonitor(name, idcard, sdate, ed, account);
		bpEntDao.insertMontiorLog(name,idcard,account,rspdata,new Date(),mid);		
		/*************账户扣费******************/
		consume(account,PARA_CONSTATNTS.TransCode_CourtMonitor,linkno.toString(),balance,fee);
		return "设置成功";
	}
	
	
}
