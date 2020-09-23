package weixin.util;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSON;

public class MenuTools {

	/**
	 * @param args
	 */
	public static String convertJson() {
		List<TreeNode> menues = new ArrayList<>();
		TreeNode node1=new TreeNode(); 
		node1.setF_ModuleId("1");
		node1.setF_ParentId("0");
		node1.setF_EnCode("SysManager");
		node1.setF_FullName("系统管理");
		node1.setF_Icon("fa fa-desktop");
		menues.add(node1);
		
		node1=new TreeNode(); 
		node1.setF_ModuleId("2");
		node1.setF_ParentId("1");
		node1.setF_EnCode("SysManager");
		node1.setF_FullName("商户管理");
		node1.setF_UrlAddress("merController.do?mer");
		node1.setF_Icon("fa fa-binoculars");
		menues.add(node1);
		
		//System.out.println(JSON.toJSONString(menues));
		return JSON.toJSONString(menues);

	}
	
	public static void main(String args[]){
		System.out.println(MenuTools.convertJson());
	}

}
