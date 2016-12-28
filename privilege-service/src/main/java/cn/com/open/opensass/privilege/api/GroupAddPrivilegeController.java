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

import cn.com.open.opensass.privilege.model.App;
import cn.com.open.opensass.privilege.model.PrivilegeGroup;
import cn.com.open.opensass.privilege.model.PrivilegeGroupResource;
import cn.com.open.opensass.privilege.redis.impl.RedisClientTemplate;
import cn.com.open.opensass.privilege.redis.impl.RedisConstant;
import cn.com.open.opensass.privilege.service.AppService;
import cn.com.open.opensass.privilege.service.PrivilegeGroupResourceService;
import cn.com.open.opensass.privilege.service.PrivilegeGroupService;
import cn.com.open.opensass.privilege.tools.BaseControllerUtil;
import cn.com.open.opensass.privilege.tools.OauthSignatureValidateHandler;
import cn.com.open.opensass.privilege.tools.WebUtils;

/**
 *  组织机构权限初始创建接口
 */
@Controller
@RequestMapping("/group/")
public class GroupAddPrivilegeController extends BaseControllerUtil{
	private static final Logger log = LoggerFactory.getLogger(GroupAddPrivilegeController.class);
	@Autowired
	private PrivilegeGroupService privilegeGroupService;
	@Autowired
	private PrivilegeGroupResourceService privilegeGroupResourceService;
	@Autowired
	private AppService appService;
	@Autowired
	private RedisClientTemplate redisClient;
    /**
     * 组织机构权限初始创建接口
     * @return Json
     */
    @RequestMapping("addPrivilege")
    public void addPrivilege(HttpServletRequest request,HttpServletResponse response) {
    	String groupId=request.getParameter("groupId");
    	String groupName = "";
    	String createUser="";
    	try {
    	if(!nullEmptyBlankJudge(request.getParameter("groupName"))){
    		groupName = new String(request.getParameter("groupName").getBytes("iso-8859-1"),"utf-8");	
    	}
    	if(!nullEmptyBlankJudge(request.getParameter("createUser"))){
    		createUser = new String(request.getParameter("createUser").getBytes("iso-8859-1"),"utf-8");	
    	}
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	String appId=request.getParameter("appId");
    	String groupPrivilege=request.getParameter("groupPrivilege");
    	String createUserid=request.getParameter("createUserid");
    	String status=request.getParameter("status");
    	Map<String, Object> map=new HashMap<String, Object>();
    	log.info("====================add start======================");
    	if(!paraMandatoryCheck(Arrays.asList(groupId,groupName,appId))){
    		  paraMandaChkAndReturn(10000, response,"必传参数中有空值");
              return;	
    	}
    	App app = (App) redisClient.getObject(RedisConstant.APP_INFO+appId);
	    if(app==null)
		   {
			   app=appService.findById(Integer.parseInt(appId));
			   redisClient.setObject(RedisConstant.APP_INFO+appId, app);
		  }
    	Boolean f=OauthSignatureValidateHandler.validateSignature(request,app);
		if(!f){
			WebUtils.paraMandaChkAndReturn(5, response,"认证失败");
			return;
		}
    	//添加 privilege_group_resource
    	String [] groupPrivileges=groupPrivilege.split(",");
    	String resourceId="";
    	for(int i=0;i<groupPrivileges.length;i++){
    		resourceId=groupPrivileges[i];
    		if(nullEmptyBlankJudge(resourceId)){
    			map.put("status","0");
        		map.put("error_code","10002");
    		}else{
    			PrivilegeGroupResource pgr=privilegeGroupResourceService.getPrivilegeGroupResource(groupId, resourceId);
    			if(pgr!=null){
    	    		map.put("status","0");
    	    		map.put("error_code","10002");
    	    	}else{
    	    		pgr=new PrivilegeGroupResource();
    	    		pgr.setGroupId(groupId);
    	    		pgr.setResourceId(resourceId);
    	    		pgr.setCreateUser(createUser);
    	    		pgr.setCreateUserId(createUserid);
    	    		pgr.setCreateTime(new Date());
    	    		if(!nullEmptyBlankJudge(status)){
    	    			pgr.setStatus(Integer.parseInt(status));	
    	    		}else{
    	    			pgr.setStatus(1);	
    	    		}
    	    		privilegeGroupResourceService.saveprivilegeGroupResource(pgr);
    	    	}
    		}
    	}
    	
    	PrivilegeGroup pg=privilegeGroupService.findBygroupId(groupId,appId);
    	if(pg!=null){
    		map.put("status","0");
    		map.put("error_code","10001");
    	}else{
    		pg=new PrivilegeGroup();
    		pg.setGroupId(groupId);
    		pg.setGroupName(groupName);
    		pg.setAppId(appId);
    		pg.setCreateUser(createUser);
    		pg.setcreateUserId(createUserid);
    		pg.setCreateTime(new Date());
    		if(!nullEmptyBlankJudge(status)){
    			pg.setStatus(Integer.parseInt(status));	
    		}else{
    			pg.setStatus(1);	
    		}
    		privilegeGroupService.savePrivilegeGroup(pg);
    		map.put("status","1");
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