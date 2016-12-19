package cn.com.open.opensass.privilege.api;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.com.open.opensass.privilege.model.PrivilegeFunction;
import cn.com.open.opensass.privilege.model.PrivilegeGroup;
import cn.com.open.opensass.privilege.model.PrivilegeGroupResource;
import cn.com.open.opensass.privilege.model.PrivilegeMenu;
import cn.com.open.opensass.privilege.service.PrivilegeFunctionService;
import cn.com.open.opensass.privilege.service.PrivilegeGroupResourceService;
import cn.com.open.opensass.privilege.service.PrivilegeGroupService;
import cn.com.open.opensass.privilege.service.PrivilegeMenuService;
import cn.com.open.opensass.privilege.tools.BaseControllerUtil;

/**
 *  权限功能添加接口
 */
@Controller
@RequestMapping("/function/")
public class FunctionAddPrivilegeController extends BaseControllerUtil{
	private static final Logger log = LoggerFactory.getLogger(FunctionAddPrivilegeController.class);
	@Autowired
	private PrivilegeFunctionService privilegeFunctionService;

    /**
     * 权限功能添加接口
     * @return Json
     */
    @RequestMapping("addFunction")
    public void addFunction(HttpServletRequest request,HttpServletResponse response) {
    	String createUser = "";
    	try {
    	if(!nullEmptyBlankJudge(request.getParameter("createUser"))){
    		createUser = new String(request.getParameter("createUser").getBytes("iso-8859-1"),"utf-8");	
    	}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
    	String appId=request.getParameter("appId");
    	String resourceId=request.getParameter("resourceId");
    	String operationId=request.getParameter("operationId");
    	String optUrl=request.getParameter("optUrl");
    	String createUserid=request.getParameter("createUserid");
    	Map<String, Object> map=new HashMap<String, Object>();
    	log.info("====================add start======================");
    	if(!paraMandatoryCheck(Arrays.asList(resourceId,appId))){
    		  paraMandaChkAndReturn(10000, response,"必传参数中有空值");
              return;	
    	}
        PrivilegeFunction pf=new PrivilegeFunction();
        pf.setResourceId(resourceId);
        pf.setCreateTime(new Date());
        pf.setCreateUser(createUser);
        pf.setCreateUserId(createUserid);
        pf.setOptUrl(optUrl);
        pf.setOperationId(operationId);
    	Boolean f =privilegeFunctionService.savePrivilegeFunction(pf);
    	if(f){
    		map.put("status","1");
    		map.put("menuId", pf.id());
    	}else{
    		map.put("status","0");
    		map.put("error_code","10001");
    	}
    	if(map.get("status")=="0"){
    		writeErrorJson(response,map);
    	}else{
    		writeSuccessJson(response,map);
    	}
    	//OauthControllerLog.log(startTime, guid, source_id, app, map,userserviceDev);
        return;
    	
    }
}