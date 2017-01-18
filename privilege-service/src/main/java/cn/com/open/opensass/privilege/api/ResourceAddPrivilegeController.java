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
import cn.com.open.opensass.privilege.model.PrivilegeResource;
import cn.com.open.opensass.privilege.redis.impl.RedisClientTemplate;
import cn.com.open.opensass.privilege.redis.impl.RedisConstant;
import cn.com.open.opensass.privilege.service.AppService;
import cn.com.open.opensass.privilege.service.PrivilegeResourceService;
import cn.com.open.opensass.privilege.tools.BaseControllerUtil;
import cn.com.open.opensass.privilege.tools.OauthSignatureValidateHandler;
import cn.com.open.opensass.privilege.tools.WebUtils;
import cn.com.open.opensass.privilege.vo.PrivilegeAjaxMessage;

/**
 *  菜单添加接口
 */
@Controller
@RequestMapping("/resource/")
public class ResourceAddPrivilegeController extends BaseControllerUtil{
	private static final Logger log = LoggerFactory.getLogger(ResourceAddPrivilegeController.class);
	@Autowired
	private PrivilegeResourceService privilegeResourceService;
	@Autowired
	private AppService appService;
	@Autowired
	private RedisClientTemplate redisClient;
    /**
     * 菜单添加接口
     * @return Json
     */
    @RequestMapping("addResource")
    public void addMenu(HttpServletRequest request,HttpServletResponse response) {
    	String resourceName = "";
    	String resourceRule="";
    	String createUser="";
    	try {
    	if(!nullEmptyBlankJudge(request.getParameter("resourceName"))){
    		resourceName = request.getParameter("resourceName");
    	}
    	if(!nullEmptyBlankJudge(request.getParameter("resourceRule"))){
    		resourceRule = new String(request.getParameter("resourceRule").getBytes("iso-8859-1"),"utf-8");	
    	}
    	if(!nullEmptyBlankJudge(request.getParameter("createUser"))){
    		createUser = new String(request.getParameter("createUser").getBytes("iso-8859-1"),"utf-8");	
    	}
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	String appId=request.getParameter("appId");
    	String resourceLevel=request.getParameter("resourceLevel");
    	String menuId=request.getParameter("menuId");
    	String baseUrl=request.getParameter("baseUrl");
    	String createUserId=request.getParameter("createUserId");
    	Map<String, Object> map=new HashMap<String, Object>();
    	log.info("====================add start======================");
    	if(!paraMandatoryCheck(Arrays.asList(resourceLevel,resourceName,appId))){
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
    	PrivilegeResource pr=new PrivilegeResource();
    	pr.setAppId(appId);
    	pr.setResourceName(resourceName);
    	if(!nullEmptyBlankJudge(resourceLevel)){
    		pr.setResourceLevel(Integer.parseInt(resourceLevel));
    	}
    	pr.setResourceRule(resourceRule);
    	pr.setMenuId(menuId);
    	pr.setBaseUrl(baseUrl);
    	pr.setCreateTime(new Date());
    	pr.setCreateUser(createUser);
    	pr.setCreateUserId(createUserId);
    	Boolean sf= privilegeResourceService.savePrivilegeResource(pr);
    	if(sf){
    		//添加缓存
    		PrivilegeAjaxMessage message=privilegeResourceService.updateAppResRedis(appId);
    		if (message.getCode().equals("1")) {
    			map.put("status","1");
        		map.put("resourceId", pr.id());
    		} else {
    			map.put("status", message.getCode());
    			map.put("error_code", message.getMessage());/* 数据不存在 */
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