package cn.com.open.opensass.privilege.api;

import java.util.Arrays;
import java.util.Date;
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

import cn.com.open.opensass.privilege.model.PrivilegeGroup;
import cn.com.open.opensass.privilege.model.PrivilegeGroupResource;
import cn.com.open.opensass.privilege.model.PrivilegeResource;
import cn.com.open.opensass.privilege.service.PrivilegeGroupResourceService;
import cn.com.open.opensass.privilege.service.PrivilegeGroupService;
import cn.com.open.opensass.privilege.service.PrivilegeResourceService;
import cn.com.open.opensass.privilege.service.PrivilegeResourceService;
import cn.com.open.opensass.privilege.tools.BaseControllerUtil;

/**
 * 菜单删除接口
 */
@Controller
@RequestMapping("/Resource/")
public class ResourceDelPrivilegeController extends BaseControllerUtil{
	private static final Logger log = LoggerFactory.getLogger(ResourceDelPrivilegeController.class);
	@Autowired
	private PrivilegeResourceService privilegeResourceService;

    /**
     * 资源删除接口
     * @return Json
     */
    @RequestMapping("delResource")
    public void delResource(HttpServletRequest request,HttpServletResponse response) {
    	String resourceId=request.getParameter("resourceId");
    	String appId=request.getParameter("appId");
    	Map<String, Object> map=new HashMap<String, Object>();
    	log.info("====================delete start======================");
    	if(!paraMandatoryCheck(Arrays.asList(resourceId,appId))){
    		  paraMandaChkAndReturn(10000, response,"必传参数中有空值");
              return;	
    	}
    	PrivilegeResource pm=privilegeResourceService.findByResourceId(resourceId, appId);
    	if(pm!=null){
    		Boolean f= privilegeResourceService.deleteByResourceId(resourceId);
    		if(f){
    			map.put("status","1");
    		}else{
    			map.put("status","0");
        		map.put("error_code","10002");	
    		}
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