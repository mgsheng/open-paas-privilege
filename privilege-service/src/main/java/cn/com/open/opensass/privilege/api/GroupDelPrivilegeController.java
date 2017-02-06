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

import cn.com.open.opensass.privilege.model.App;
import cn.com.open.opensass.privilege.model.PrivilegeGroupResource;
import cn.com.open.opensass.privilege.redis.impl.RedisClientTemplate;
import cn.com.open.opensass.privilege.redis.impl.RedisConstant;
import cn.com.open.opensass.privilege.service.AppService;
import cn.com.open.opensass.privilege.service.PrivilegeGroupResourceService;
import cn.com.open.opensass.privilege.service.PrivilegeGroupService;
import cn.com.open.opensass.privilege.tools.BaseControllerUtil;
import cn.com.open.opensass.privilege.tools.OauthSignatureValidateHandler;
import cn.com.open.opensass.privilege.tools.WebUtils;
import cn.com.open.opensass.privilege.vo.PrivilegeAjaxMessage;

/**
 * 组织机构权限删除接口（删除当前组织机构所有权限）
 */
@Controller
@RequestMapping("/group/")
public class GroupDelPrivilegeController extends BaseControllerUtil{
	private static final Logger log = LoggerFactory.getLogger(GroupDelPrivilegeController.class);
	@Autowired
	private PrivilegeGroupResourceService privilegeGroupResourceService;
	@Autowired
	private AppService appService;
	@Autowired
	private RedisClientTemplate redisClient;
	@Autowired
	private PrivilegeGroupService privilegeGroupService;
    /**
     * 组织机构权限删除接口（删除当前组织机构所有权限）
     * @return Json
     */
    @RequestMapping("delPrivilege")
    public void delPrivilege(HttpServletRequest request,HttpServletResponse response) {
    	String groupId=request.getParameter("groupId");
    	String appId=request.getParameter("appId");
    	Map<String, Object> map=new HashMap<String, Object>();
    	log.info("====================delete start======================");
    	if(!paraMandatoryCheck(Arrays.asList(groupId,appId))){
    		  paraMandaChkAndReturn(10000, response,"必传参数中有空值");
              return;	
    	}
    	App app = (App) redisClient.getObject(RedisConstant.APP_INFO+appId);
	    if(app==null)
		   {
			   app=appService.findById(Integer.parseInt(appId));
			   redisClient.setObject(RedisConstant.APP_INFO+appId, app);
		  }
	     //认证
    	Boolean f=OauthSignatureValidateHandler.validateSignature(request,app);
    	
		if(!f){
			WebUtils.paraMandaChkAndReturn(5, response,"认证失败");
			return;
		}
    	List<PrivilegeGroupResource> lists=privilegeGroupResourceService.getPgrs(groupId);
    	if(lists!=null&&lists.size()>0){
    		privilegeGroupResourceService.deleteByGroupId(groupId);
    		//更新缓存
    		PrivilegeAjaxMessage message=privilegeGroupService.updateGroupPrivilegeCache(groupId, appId);
    		if (message.getCode().equals("1")) {
    			map.put("status","1");
    		} else {
    			map.put("status", message.getCode());
    			map.put("error_code", message.getMessage());/* 数据不存在 */
    			writeErrorJson(response, map);
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