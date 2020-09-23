package org.jeecgframework.web.system.controller.core;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.jeecgframework.core.common.controller.BaseController;
import org.jeecgframework.core.common.model.json.AjaxJson;
import org.jeecgframework.core.constant.Globals;
import org.jeecgframework.core.extend.datasource.DataSourceContextHolder;
import org.jeecgframework.core.extend.datasource.DataSourceType;
import org.jeecgframework.core.util.*;
import org.jeecgframework.web.system.manager.ClientManager;
import org.jeecgframework.web.system.pojo.base.*;
import org.jeecgframework.web.system.service.SystemService;
import org.jeecgframework.web.system.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;
import weixin.guanjia.account.entity.WeixinAccountEntity;
import weixin.guanjia.account.service.WeixinAccountServiceI;
import weixin.util.MenuTools;
import weixin.util.WeiXinConstants;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.*;

/**
 * 登陆初始化控制器
 * 
 * @author 张代浩
 * 
 */
@Scope("prototype")
@Controller
@RequestMapping("/loginController")
public class LoginController extends BaseController {
	private Logger log = Logger.getLogger(LoginController.class);
	private SystemService systemService;
	@Autowired
	private WeixinAccountServiceI weixinAccountService;
	private UserService userService;
	private String message = null;

	@Autowired
	public void setSystemService(SystemService systemService) {
		this.systemService = systemService;
	}

	@Autowired
	public void setUserService(UserService userService) {

		this.userService = userService;
	}

	@RequestMapping(params = "goPwdInit")
	public String goPwdInit() {
		return "login/pwd_init";
	}
	@RequestMapping(params = "default")
	public String goDefault() {
		return "weixin/merchant/default";
	}
	@RequestMapping(params = "goOldSys")
	public String goOldSys() {
		return "main/main";
	}
	/**
	 * admin账户密码初始化
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(params = "pwdInit")
	public String pwdInit(HttpServletRequest request) {
		return "login/pwd_init";
	}

	/**
	 * 检查用户名称
	 * 
	 * @param user
	 * @param req
	 * @return
	 */
	@RequestMapping(params = "checkuser")
	@ResponseBody
	public AjaxJson checkuser(TSUser user, HttpServletRequest req) {
		HttpSession session = ContextHolderUtils.getSession();
		DataSourceContextHolder.setDataSourceType(DataSourceType.dataSource_jeecg);
		AjaxJson j = new AjaxJson();
      
		TSUser u = userService.checkUserExits(user);
		if (u == null) {
			j.setMsg("用户名或密码错误!");
			j.setSuccess(false);
			return j;
		}
		if (u != null && u.getStatus() != 0) {
			
			message = "用户: " + user.getUserName() + "[" 	+ u.getTSDepart().getDepartname() + "]" + "登录成功";
			Client client = new Client();
			client.setIp(IpUtil.getIpAddr(req));
			client.setLogindatetime(new Date());
			client.setUser(u);
			ClientManager.getInstance().addClinet(session.getId(),	client);
			j.setMsg(message);
			j.setSuccess(true);
			// 添加登陆日志
			systemService.addLog(message, Globals.Log_Type_LOGIN,Globals.Log_Leavel_INFO);

		} else {
			j.setMsg("该用户已锁定，请联系管理员！");
			j.setSuccess(false);
		}

		return j;
	}

	/**
	 * 用户登录
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(params = "login")
	public String login(ModelMap modelMap, HttpServletRequest request) {
		DataSourceContextHolder.setDataSourceType(DataSourceType.dataSource_jeecg);
		TSUser user = ResourceUtil.getSessionUserName();
		String roles = "";
		if (user != null) {
			WeixinAccountEntity weixinAccountEntity = weixinAccountService.findLoginWeixinAccount();
			request.getSession().setAttribute(WeiXinConstants.WEIXIN_ACCOUNT,weixinAccountEntity);
			List<TSRoleUser> rUsers = systemService.findByProperty(TSRoleUser.class, "TSUser.id", user.getId());
			for (TSRoleUser ru : rUsers) {
				TSRole role = ru.getTSRole();
				roles += role.getRoleName() + ",";
			}
			if (roles.length() > 0) {
				roles = roles.substring(0, roles.length() - 1);
			}
			modelMap.put("roleName", roles.length()>3?roles.substring(0,3)+"...":roles);
			modelMap.put("userName", user.getUserName());
			request.getSession().setAttribute("CKFinder_UserRole", "admin");
			// 默认风格
			String indexStyle = "shortcut";
			Cookie[] cookies = request.getCookies();
			for (Cookie cookie : cookies) {
				if (cookie == null || StringUtils.isEmpty(cookie.getName())) {
					continue;
				}
				if (cookie.getName().equalsIgnoreCase("JEECGINDEXSTYLE")) {
					indexStyle = cookie.getValue();
				}
			}
			request.setAttribute("menuMap", getFunctionMap(user));
			return "main/learun_main";
		} else {
			return "login/login";
		}

	}

	/**
	 * 退出系统
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(params = "logout")
	public ModelAndView logout(HttpServletRequest request) {
		HttpSession session = ContextHolderUtils.getSession();
		TSUser user = ResourceUtil.getSessionUserName();
		systemService.addLog("用户" + user.getUserName() + "已退出",Globals.Log_Type_EXIT, Globals.Log_Leavel_INFO);
		ClientManager.getInstance().removeClinet(session.getId());
		ModelAndView modelAndView = new ModelAndView(new RedirectView("/loginController.do?login"));
		return modelAndView;
	}
	
	/**
	 * HPLUS首页跳转
	 *
	 * @return
	 */
	@RequestMapping(params = "hplushome")
	public ModelAndView hplushome(HttpServletRequest request) {

//		SysThemesEnum sysTheme = SysThemesUtil.getSysTheme(request);
		//ACE ACE2 DIY时需要在home.jsp头部引入依赖的js及css文件
		/*if("ace".equals(sysTheme.getStyle())||"diy".equals(sysTheme.getStyle())||"acele".equals(sysTheme.getStyle())){
			request.setAttribute("show", "1");
		} else {//default及shortcut不需要引入依赖文件，所有需要屏蔽
			request.setAttribute("show", "0");
		}*/
		return new ModelAndView("main/hplushome");
	}

	/**
	 * 菜单跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "left")
	public ModelAndView left(HttpServletRequest request) {
		TSUser user = ResourceUtil.getSessionUserName();
		HttpSession session = ContextHolderUtils.getSession();
		ModelAndView modelAndView = new ModelAndView();
		// 登陆者的权限
		if (user.getId() == null) {
			session.removeAttribute(Globals.USER_SESSION);
			modelAndView.setView(new RedirectView("loginController.do?login"));
		} else {
			List<TSConfig> configs = userService.loadAll(TSConfig.class);
			for (TSConfig tsConfig : configs) {
				request.setAttribute(tsConfig.getCode(), tsConfig.getContents());
			}
			modelAndView.setViewName("main/left");
			request.setAttribute("menuMap", getFunctionMap(user));
		}
		return modelAndView;
	}

	/**
	 * 获取权限的map
	 * 
	 * @param user
	 * @return
	 */
	private Map<Integer, List<TSFunction>> getFunctionMap(TSUser user) {
		HttpSession session = ContextHolderUtils.getSession();
		Client client = ClientManager.getInstance().getClient(session.getId());
		if (client.getFunctionMap() == null || client.getFunctionMap().size() == 0) {
			Map<Integer, List<TSFunction>> functionMap = new HashMap<Integer, List<TSFunction>>();
			Map<String, TSFunction> loginActionlist = getUserFunction(user);
			if (loginActionlist.size() > 0) {
				Collection<TSFunction> allFunctions = loginActionlist.values();
				for (TSFunction function : allFunctions) {
//		            if(function.getFunctionType().intValue()==Globals.Function_TYPE_FROM.intValue()){
//						//如果为表单或者弹出 不显示在系统菜单里面
//						continue;
//					}
					if (!functionMap.containsKey(function.getFunctionLevel() + 0)) {
						functionMap.put(function.getFunctionLevel() + 0,
								new ArrayList<TSFunction>());
					}
					functionMap.get(function.getFunctionLevel() + 0).add(function);
				}
				// 菜单栏排序
				Collection<List<TSFunction>> c = functionMap.values();
				for (List<TSFunction> list : c) {
					Collections.sort(list, new NumberComparator());
				}
			}
			client.setFunctionMap(functionMap);
			//清空变量，降低内存使用
			loginActionlist.clear();
			return functionMap;
		}else{
			return client.getFunctionMap();
		}
	}

	/**
	 * 获取用户菜单列表
	 * 
	 * @param user
	 * @return
	 */
	private Map<String, TSFunction> getUserFunction(TSUser user) {
		HttpSession session = ContextHolderUtils.getSession();
		Client client = ClientManager.getInstance().getClient(session.getId());
		if (client.getFunctions() == null || client.getFunctions().size() == 0) {
			Map<String, TSFunction> loginActionlist = new HashMap<String, TSFunction>();
			List<TSRoleUser> rUsers = systemService.findByProperty(
					TSRoleUser.class, "TSUser.id", user.getId());
			for (TSRoleUser ru : rUsers) {
				TSRole role = ru.getTSRole();
				List<TSRoleFunction> roleFunctionList = systemService
						.findByProperty(TSRoleFunction.class, "TSRole.id",
								role.getId());
				for (TSRoleFunction roleFunction : roleFunctionList) {
					TSFunction function = roleFunction.getTSFunction();
					loginActionlist.put(function.getId(), function);
				}
			}
			client.setFunctions(loginActionlist);
		}
		return client.getFunctions();
	}

	/**
	 * 首页跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "home")
	public ModelAndView home(HttpServletRequest request) {
		return new ModelAndView("main/home");
	}

	/**
	 * 无权限页面提示跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "noAuth")
	public ModelAndView noAuth(HttpServletRequest request) {
		return new ModelAndView("common/noAuth");
	}
	
	/**
	 * session失效退出
	 * 
	 * @return
	 */
	@RequestMapping(params = "nosession")
	public String noSession(HttpServletRequest request) {
		System.out.println("onsession");
		return "login/timeout";
	}

	/**
	 * @Title: top
	 * @Description: bootstrap头部菜单请求
	 * @param request
	 * @return ModelAndView
	 * @throws
	 */
	@RequestMapping(params = "top")
	public ModelAndView top(HttpServletRequest request) {
		TSUser user = ResourceUtil.getSessionUserName();
		HttpSession session = ContextHolderUtils.getSession();
		// 登陆者的权限
		if (user.getId() == null) {
			session.removeAttribute(Globals.USER_SESSION);
			return new ModelAndView(
					new RedirectView("loginController.do?login"));
		}
		request.setAttribute("menuMap", getFunctionMap(user));
		List<TSConfig> configs = userService.loadAll(TSConfig.class);
		for (TSConfig tsConfig : configs) {
			request.setAttribute(tsConfig.getCode(), tsConfig.getContents());
		}
		return new ModelAndView("main/bootstrap_top");
	}

	/**
	 * @Title: top
	 * @author gaofeng
	 * @Description: shortcut头部菜单请求
	 * @param request
	 * @return ModelAndView
	 * @throws
	 */
	@RequestMapping(params = "shortcut_top")
	public ModelAndView shortcut_top(HttpServletRequest request) {
		TSUser user = ResourceUtil.getSessionUserName();
		HttpSession session = ContextHolderUtils.getSession();
		// 登陆者的权限
		if (user.getId() == null) {
			session.removeAttribute(Globals.USER_SESSION);
			return new ModelAndView(
					new RedirectView("loginController.do?login"));
		}
		request.setAttribute("menuMap", getFunctionMap(user));
		List<TSConfig> configs = userService.loadAll(TSConfig.class);
		for (TSConfig tsConfig : configs) {
			request.setAttribute(tsConfig.getCode(), tsConfig.getContents());
		}
		return new ModelAndView("main/shortcut_top");
	}

	/**
	 * @Title: top
	 * @author:gaofeng
	 * @Description: shortcut头部菜单一级菜单列表，并将其用ajax传到页面，实现动态控制一级菜单列表
	 * @return AjaxJson
	 * @throws
	 */
	@RequestMapping(params = "primaryMenu")
	@ResponseBody
	public String getPrimaryMenu() {
		List<TSFunction> primaryMenu = getFunctionMap(
				ResourceUtil.getSessionUserName()).get(0);
		String floor = "";
		for (TSFunction function : primaryMenu) {
			if (function.getFunctionLevel() == 0) {

				if ("Online 开发".equals(function.getFunctionName())) {

					floor += " <li><img class='imag1' src='plug-in/login/images/online.png' /> "
							+ " <img class='imag2' src='plug-in/login/images/online_up.png' style='display: none;' />"
							+ " </li> ";
				} else if ("统计查询".equals(function.getFunctionName())) {

					floor += " <li><img class='imag1' src='plug-in/login/images/guanli.png' /> "
							+ " <img class='imag2' src='plug-in/login/images/guanli_up.png' style='display: none;' />"
							+ " </li> ";
				} else if ("系统管理".equals(function.getFunctionName())) {

					floor += " <li><img class='imag1' src='plug-in/login/images/xtgl.png' /> "
							+ " <img class='imag2' src='plug-in/login/images/xtgl_up.png' style='display: none;' />"
							+ " </li> ";
				} else if ("常用示例".equals(function.getFunctionName())) {

					floor += " <li><img class='imag1' src='plug-in/login/images/cysl.png' /> "
							+ " <img class='imag2' src='plug-in/login/images/cysl_up.png' style='display: none;' />"
							+ " </li> ";
				} else if ("系统监控".equals(function.getFunctionName())) {

					floor += " <li><img class='imag1' src='plug-in/login/images/xtjk.png' /> "
							+ " <img class='imag2' src='plug-in/login/images/xtjk_up.png' style='display: none;' />"
							+ " </li> ";
				} else {
					// 其他的为默认通用的图片模式
					String s = "";
					if (function.getFunctionName().length() >= 5
							&& function.getFunctionName().length() < 7) {
						s = "<div style='width:67px;position: absolute;top:40px;text-align:center;color:#909090;font-size:12px;'><span style='letter-spacing:-1px;'>"
								+ function.getFunctionName() + "</span></div>";
					} else if (function.getFunctionName().length() < 5) {
						s = "<div style='width:67px;position: absolute;top:40px;text-align:center;color:#909090;font-size:12px;'>"
								+ function.getFunctionName() + "</div>";
					} else if (function.getFunctionName().length() >= 7) {
						s = "<div style='width:67px;position: absolute;top:40px;text-align:center;color:#909090;font-size:12px;'><span style='letter-spacing:-1px;'>"
								+ function.getFunctionName().substring(0, 6)
								+ "</span></div>";
					}
					floor += " <li style='position: relative;'><img class='imag1' src='plug-in/login/images/default.png' /> "
							+ " <img class='imag2' src='plug-in/login/images/default_up.png' style='display: none;' />"
							+ s + "</li> ";
				}
			}
		}

		return floor;
	}

	/**
	 * 返回数据
	 */
	@RequestMapping(params = "getPrimaryMenuForWebos")
	@ResponseBody
	public AjaxJson getPrimaryMenuForWebos() {
		AjaxJson j = new AjaxJson();
		// 将菜单加载到Session，用户只在登录的时候加载一次
		Object getPrimaryMenuForWebos = ContextHolderUtils.getSession()
				.getAttribute("getPrimaryMenuForWebos");
		if (oConvertUtils.isNotEmpty(getPrimaryMenuForWebos)) {
			j.setMsg(getPrimaryMenuForWebos.toString());
		} else {
			String PMenu = ListtoMenu.getWebosMenu(getFunctionMap(ResourceUtil
					.getSessionUserName()));
			ContextHolderUtils.getSession().setAttribute(
					"getPrimaryMenuForWebos", PMenu);
			j.setMsg(PMenu);
		}
		return j;
	}
}
