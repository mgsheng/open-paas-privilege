package cn.com.open.opensass.privilege.api;

import java.util.Arrays;
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

import cn.com.open.opensass.privilege.model.PrivilegeGroupResource;
import cn.com.open.opensass.privilege.service.PrivilegeGroupResourceService;
import cn.com.open.opensass.privilege.service.PrivilegeResourceService;
import cn.com.open.opensass.privilege.tools.BaseControllerUtil;

/**
 *  组织机构权限查询接口
 */
@Controller
@RequestMapping("/group/")
public class GroupGetPrivilegeController extends BaseControllerUtil{
	private static final Logger log = LoggerFactory.getLogger(GroupGetPrivilegeController.class);
	@Autowired
	private PrivilegeGroupResourceService privilegeGroupResourceService;
	@Autowired
	private PrivilegeResourceService privilegeResourceService;

    /**
     * 组织机构权限查询接口
     * @return Json
     */
    @RequestMapping("getGroupPrivilege")
    public void getGroupPrivilege(HttpServletRequest request,HttpServletResponse response) {
    	String groupId=request.getParameter("groupId");
    	String appId=request.getParameter("appId");
    	String start=request.getParameter("start");
    	String limit=request.getParameter("limit");
    	Map<String, Object> map=new HashMap<String, Object>();
    	log.info("====================query start======================");
    	if(!paraMandatoryCheck(Arrays.asList(groupId,start,appId,limit))){
    		  paraMandaChkAndReturn(10000, response,"必传参数中有空值");
              return;	
    	}
    	List<PrivilegeGroupResource>lists=privilegeGroupResourceService.getPgrs(groupId,start, limit);
    	if(lists!=null&&lists.size()>0){

    		Map<String, Object> groupMap=new HashMap<String, Object>();
    		map.put("status", "1");
    		map.put("count", lists.size());
    		groupMap.put("appId", appId);
			groupMap.put("groupId", groupId);
			String groupPrivilege="";
    		for(int i=0;i<lists.size();i++){
    			groupPrivilege+=lists.get(i).getResourceId()+",";
    		}
    		if(!nullEmptyBlankJudge(groupPrivilege)){
    			String groupPrivileges[]=groupPrivilege.split(",");
    			groupMap.put("groupPrivilege", groupPrivilege.substring(0, groupPrivilege.length()-1));
    			List<Map<String, Object>> resourceLists=privilegeResourceService.findResourceMap(groupPrivileges);
    			if(resourceLists!=null&&resourceLists.size()>0){
    				groupMap.put("resourceList", resourceLists);	
        		}
    		}
    		JSON json =JSONObject.fromObject(groupMap);
    		map.put("groupList", json);
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