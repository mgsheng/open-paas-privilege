package cn.com.open.opensass.privilege.api;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSON;
import net.sf.json.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.com.open.opensass.privilege.model.PrivilegeFunction;
import cn.com.open.opensass.privilege.model.PrivilegeGroup;
import cn.com.open.opensass.privilege.model.PrivilegeGroupResource;
import cn.com.open.opensass.privilege.model.PrivilegeMenu;
import cn.com.open.opensass.privilege.model.PrivilegeResource;
import cn.com.open.opensass.privilege.service.PrivilegeFunctionService;
import cn.com.open.opensass.privilege.service.PrivilegeGroupResourceService;
import cn.com.open.opensass.privilege.service.PrivilegeGroupService;
import cn.com.open.opensass.privilege.service.PrivilegeMenuService;
import cn.com.open.opensass.privilege.service.PrivilegeResourceService;
import cn.com.open.opensass.privilege.tools.BaseControllerUtil;

/**
 *  权限资源查询接口
 */
@Controller
@RequestMapping("/resource/")
public class ResourceGetPrivilegeController extends BaseControllerUtil{
	private static final Logger log = LoggerFactory.getLogger(ResourceGetPrivilegeController.class);
	@Autowired
	private PrivilegeResourceService privilegeResourceService;
	@Autowired
	private PrivilegeFunctionService privilegeFunctionService;
	
    @RequestMapping("getResPrivilege")
    public void getResPrivilege(HttpServletRequest request,HttpServletResponse response) {
    	String menuId=request.getParameter("menuId");
    	String appId=request.getParameter("appId");
    	String start=request.getParameter("start");
    	String limit=request.getParameter("Limit");
    	Map<String, Object> map=new HashMap<String, Object>();
    	log.info("====================query start======================");
    	if(!paraMandatoryCheck(Arrays.asList(start,appId,limit))){
    		  paraMandaChkAndReturn(10000, response,"必传参数中有空值");
              return;	
    	}
    	List<PrivilegeResource>lists=privilegeResourceService.findResourcePage(menuId, appId,Integer.parseInt(start),Integer.parseInt(limit));
    	if(lists!=null&&lists.size()>0){
    		Map<String, Object> resourceMap=new HashMap<String, Object>();
    		map.put("status", "1");
    		map.put("count", lists.size());
    		for(int i=0;i<lists.size();i++){
    			resourceMap.put("appId", lists.get(i).getAppId());
    			resourceMap.put("resourceId", lists.get(i).getResourceId());
    			resourceMap.put("resourceLevel", lists.get(i).getResourceLevel());
    			resourceMap.put("resourceName", lists.get(i).getResourceName());
    			resourceMap.put("resourceRule", lists.get(i).getResourceRule());
    			resourceMap.put("dislayOrder", lists.get(i).getDisplayOrder());
    			resourceMap.put("menuId", lists.get(i).getMenuId());
    			resourceMap.put("baseUrl", lists.get(i).getBaseUrl());
    			resourceMap.put("status", lists.get(i).getStatus());
    			if(nullEmptyBlankJudge(lists.get(i).getResourceId())){
    				List<PrivilegeFunction>functionList=privilegeFunctionService.getFunctionByRId(lists.get(i).getResourceId());
        			if(functionList!=null&&functionList.size()>0){
        			 resourceMap.put("functionList", functionList);	
        			}
    			}
    			
    		}
    		JSON json =JSONObject.fromObject(resourceMap);
    		map.put("resourceList",json);
    	}else{
    		map.put("status", "0");
    		map.put("error_code", "10001");
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