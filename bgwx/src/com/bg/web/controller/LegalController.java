package com.bg.web.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.jeecgframework.core.util.ResourceUtil;
import org.jeecgframework.web.system.pojo.base.TSUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.bg.interfaces.service.IBPEntService;
import com.bg.trans.service.IWXNoticeService;



@Controller
@RequestMapping("/shesu")
public class LegalController {
	private static Logger logger = Logger.getLogger(LegalController.class);
	
	@Autowired
	private IBPEntService bpEntService;
	@Autowired
	private IWXNoticeService wxNoticeService;
	/**
	 * 跳转涉诉查询页面
	 * @param request
	 * @return
	 */
	@RequestMapping("gosearch")
	public ModelAndView goSearch(HttpServletRequest request) {	
		return new ModelAndView("shesu/search");
	}
	
	/**
	 * 跳转涉诉查询页面
	 * @param request
	 * @return
	 */
	@RequestMapping("gosearchstat")
	public ModelAndView goSearchstat(HttpServletRequest request) {	
		return new ModelAndView("shesu/search_stat");
	}
	
	/**
	 * 跳转涉诉提示页面
	 * @param request
	 * @return
	 */
	@RequestMapping("gomonitor")
	public ModelAndView goMonitor(HttpServletRequest request) {	
		return new ModelAndView("shesu/monitorlist");
	}
	
	
	@RequestMapping("getmonitorlist")
	@ResponseBody
	public Object getMonitorList(HttpServletRequest request){	
		TSUser user = ResourceUtil.getSessionUserName();
		String account = user.getUserName();
		String sl = request.getParameter("limit");
		String so = request.getParameter("offset");
		System.out.println("limit:"+account);
		
		int rowCount = bpEntService.countMonitorList(account);
		int limit = (Integer.parseInt(sl));
		int offset = (Integer.parseInt(so)-1)*limit;
		System.out.println("limit:"+limit);
		System.out.println("offset:"+offset);
		Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        Date d = calendar.getTime();		
		List<Map<String,Object>> list = bpEntService.findMonitorList(account, offset,limit);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		for(Map<String,Object> m:list){
			Date sd = (Date)m.get("SDATE");
			m.put("SDATE", sdf.format(sd));
			Date ed = (Date)m.get("EDATE");
			m.put("EDATE", sdf.format(ed));
			if(d.after(ed)){
				m.put("ISRUN", "否");
			}else{
				m.put("ISRUN", "是");
			}			
		}		
		Map<String, Object> msg = new HashMap<>();
		msg.put("total", rowCount);
		msg.put("rows",list);
		return msg;
	}
	
	@RequestMapping("setmonitor")
	@ResponseBody
	public Object setMonitor(HttpServletRequest request){	
		System.out.println("setmonitorsetmonitorsetmonitor");
		TSUser user = ResourceUtil.getSessionUserName();
		String account = user.getUserName();
		String name = request.getParameter("name");
		String id = request.getParameter("idcard");
		String type = request.getParameter("type");
		String org = request.getParameter("org");
		String sd = request.getParameter("sdate");
		String ds = request.getParameter("days");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		Date sdate = null;		
		try {
			sdate = sdf.parse(sd);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		String n = null;
		String idcard = null;
		if("1".equals(type)){
			n=name;
			idcard = id;
		}else{
			n=org;
			idcard = "";
		}
		String msg = bpEntService.setMonitor(n, idcard, type,sdate , Integer.parseInt(ds), account);		
		return msg;
	}
	
	
	
	@RequestMapping("entstat")
	@ResponseBody
	public Object entstat(HttpServletRequest request){	
		TSUser user = ResourceUtil.getSessionUserName();
		String name = request.getParameter("name");
		String id = request.getParameter("idcard");
		String type = request.getParameter("type");
		String org = request.getParameter("org");
		String account = user.getUserName();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		String t = sdf.format(new Date());
		String n = "1".equals(type)?name:org;
		
		return bpEntService.entstatService(n,id,type,t,account);		
	}
	
	@RequestMapping("entout")
	@ResponseBody
	public Object entout(HttpServletRequest request){	
		TSUser user = ResourceUtil.getSessionUserName();
		String name = request.getParameter("name");
		String id = request.getParameter("idcard");
		String org = request.getParameter("org");
		String type = request.getParameter("type");
		String n = "1".equals(type)?name:org;
		String account = user.getUserName();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		String t = sdf.format(new Date());
		return bpEntService.entoutService(n,id,type,t,account);
	}
	
}
