package cn.com.open.opensass.privilege.web;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.com.open.opensass.privilege.tools.BaseControllerUtil;

/**
 *  用户信息绑定接口
 */
@Controller
@RequestMapping("/group/")
public class PrivilegeGroupResourceController extends BaseControllerUtil{
	private static final Logger log = LoggerFactory.getLogger(PrivilegeGroupResourceController.class);

    /**
     * 组织机构权限初始创建接口
     * @return Json
     */
    @RequestMapping("addPrivilege")
    public void addPrivilege(HttpServletRequest request,HttpServletResponse response) {
    	String groupId=request.getParameter("groupId");
    	String groupName=request.getParameter("groupName");
    	String appId=request.getParameter("appId");
    	String groupPrivilege=request.getParameter("groupPrivilege");
    	String createUser=request.getParameter("createUser");
    	String createUserid=request.getParameter("createUserid");
    	String status=request.getParameter("status");
    	Map<String, Object> map=new HashMap<String, Object>();
    	log.info("====================add start======================");
    	if(!paraMandatoryCheck(Arrays.asList(groupId,groupName,appId))){
    		  paraMandaChkAndReturn(3, response,"必传参数中有空值");
              return;	
    	}
    	
    }
}