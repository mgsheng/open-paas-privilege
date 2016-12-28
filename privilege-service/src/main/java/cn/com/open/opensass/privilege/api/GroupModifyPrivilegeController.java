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
import cn.com.open.opensass.privilege.model.PrivilegeGroupResource;
import cn.com.open.opensass.privilege.redis.impl.RedisClientTemplate;
import cn.com.open.opensass.privilege.redis.impl.RedisConstant;
import cn.com.open.opensass.privilege.service.AppService;
import cn.com.open.opensass.privilege.service.PrivilegeGroupResourceService;
import cn.com.open.opensass.privilege.tools.BaseControllerUtil;
import cn.com.open.opensass.privilege.tools.OauthSignatureValidateHandler;
import cn.com.open.opensass.privilege.tools.WebUtils;

/**
 *  权限资源修改接口
 */
@Controller
@RequestMapping("/group/")
public class GroupModifyPrivilegeController extends BaseControllerUtil{
	private static final Logger log = LoggerFactory.getLogger(GroupModifyPrivilegeController.class);
	@Autowired
	private PrivilegeGroupResourceService privilegeGroupResourceService;
	@Autowired
	private AppService appService;
	@Autowired
	private RedisClientTemplate redisClient;
    /**
     * 权限资源修改接口
     * @return Json
     */
    @RequestMapping("modifyPrivilege")
    public void modifyPrivilege(HttpServletRequest request,HttpServletResponse response) {
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
    	String method=request.getParameter("method");
    	String groupPrivilege=request.getParameter("groupPrivilege");
    	String createUserid=request.getParameter("createUserid");
    	String status=request.getParameter("status");
    	Map<String, Object> map=new HashMap<String, Object>();
    	log.info("====================modify start======================");
    	if(!paraMandatoryCheck(Arrays.asList(groupId,method,appId))){
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
    	if(!method.equals("0")&&!method.equals("1")){
  		  paraMandaChkAndReturn(10001, response,"非法操作");
            return;	
  	     }
    	//添加 privilege_group_resource
    	String [] groupPrivileges=groupPrivilege.split(",");
    	String resourceId="";
    	for(int i=0;i<groupPrivileges.length;i++){
    		resourceId=groupPrivileges[i];
    			PrivilegeGroupResource pgr=privilegeGroupResourceService.getPrivilegeGroupResource(groupId, resourceId);
    			if(method.equals("0")){
    			if(pgr!=null){
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
    	    	 Boolean uf=	privilegeGroupResourceService.updatePrivilegeGroupResource(pgr);
    	    	 if (!uf){
    	    		 paraMandaChkAndReturn(10002, response,"更新失败");
    	    		 return;
    	    	 }
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
        	    		Boolean sf=privilegeGroupResourceService.saveprivilegeGroupResource(pgr);
        	    		 if (!sf){
            	    		 paraMandaChkAndReturn(10003, response,"保存失败");
            	    		 return;
            	    	 }
        	    		
    	    	    }
    			}else if(method.equals("1")&&pgr!=null){
    				Boolean df=privilegeGroupResourceService.deleteResource(groupId, resourceId);
    				 if (!df){
        	    		 paraMandaChkAndReturn(10004, response,"删除失败");
        	    		 return;
        	    	 }
    			}
    	}
    	map.put("status", "1");
    	writeSuccessJson(response,map);
    	//OauthControllerLog.log(startTime, guid, source_id, app, map,userserviceDev);
        return;
    	
    }
}