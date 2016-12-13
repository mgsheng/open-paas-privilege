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
import cn.com.open.opensass.privilege.service.PrivilegeGroupResourceService;
import cn.com.open.opensass.privilege.service.PrivilegeGroupService;
import cn.com.open.opensass.privilege.tools.BaseControllerUtil;

/**
 *  菜单查询接口
 */
@Controller
@RequestMapping("/menu/")
public class MenuGetPrivilegeController extends BaseControllerUtil{
	private static final Logger log = LoggerFactory.getLogger(MenuGetPrivilegeController.class);
	@Autowired
	private PrivilegeGroupService privilegeGroupService;

    /**
     * 菜单查询接口
     * @return Json
     */
    @RequestMapping("getMenus")
    public void getMenus(HttpServletRequest request,HttpServletResponse response) {
    	String groupId=request.getParameter("groupId");
    	String appId=request.getParameter("appId");
    	String start=request.getParameter("start");
    	String limit=request.getParameter("Limit");
    	Map<String, Object> map=new HashMap<String, Object>();
    	log.info("====================query start======================");
    	if(!paraMandatoryCheck(Arrays.asList(groupId,start,appId,limit))){
    		  paraMandaChkAndReturn(10000, response,"必传参数中有空值");
              return;	
    	}
    	List<PrivilegeGroup>lists=privilegeGroupService.findGroupPage(groupId, appId, start, limit);
    	if(lists!=null&&lists.size()>0){
    		for(int i=0;i<lists.size();i++){
    			
    		}
    		map.put("status", "1");
    		map.put("count", lists.size());
    		map.put("groupList",lists);
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