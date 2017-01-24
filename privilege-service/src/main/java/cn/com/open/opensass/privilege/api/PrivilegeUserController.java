package cn.com.open.opensass.privilege.api;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.com.open.opensass.privilege.model.PrivilegeOperation;
import cn.com.open.opensass.privilege.model.PrivilegeUser;
import cn.com.open.opensass.privilege.service.PrivilegeOperationService;
import cn.com.open.opensass.privilege.service.PrivilegeUserService;
import cn.com.open.opensass.privilege.tools.BaseControllerUtil;
/**
 */
@Controller
@RequestMapping("/user/")
public class PrivilegeUserController extends BaseControllerUtil{
	private static final Logger log = LoggerFactory.getLogger(PrivilegeUserController.class);
	@Autowired
	private PrivilegeUserService privilegeUserService;
	@RequestMapping("getUser")
	public void getOperationName(HttpServletRequest request,HttpServletResponse response){

    	String appId=request.getParameter("appId");
    	String start=request.getParameter("start");
    	String limit=request.getParameter("limit");
    	
    	if(!paraMandatoryCheck(Arrays.asList(appId,start,limit))){
    		  paraMandaChkAndReturn(10000, response,"必传参数中有空值");
              return;
    	}    
		List<PrivilegeUser> userList=privilegeUserService.findUserListByPage(appId, Integer.parseInt(start) ,  Integer.parseInt(limit));
		int count=privilegeUserService.getUserCountByAppId(appId);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("rows", userList);
		map.put("total", count);
		writeSuccessJson(response, map);
		return;
	}
	
}
