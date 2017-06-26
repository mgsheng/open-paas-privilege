package cn.com.open.opensass.privilege.api;

import cn.com.open.opensass.privilege.model.App;
import cn.com.open.opensass.privilege.model.PrivilegeMenu;
import cn.com.open.opensass.privilege.model.PrivilegeUser;
import cn.com.open.opensass.privilege.redis.impl.RedisClientTemplate;
import cn.com.open.opensass.privilege.redis.impl.RedisConstant;
import cn.com.open.opensass.privilege.service.AppService;
import cn.com.open.opensass.privilege.service.PrivilegeMenuService;
import cn.com.open.opensass.privilege.service.PrivilegeUserService;
import cn.com.open.opensass.privilege.tools.BaseControllerUtil;
import cn.com.open.opensass.privilege.tools.OauthSignatureValidateHandler;
import cn.com.open.opensass.privilege.tools.WebUtils;
import cn.com.open.opensass.privilege.vo.PrivilegeAjaxMessage;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 *  菜单修改接口
 */
@Controller
@RequestMapping("/menu/")
public class MenuModifyPrivilegeController extends BaseControllerUtil{
	private static final Logger log = LoggerFactory.getLogger(MenuModifyPrivilegeController.class);
	@Autowired
	private PrivilegeMenuService privilegeMenuService;
	@Autowired
	private AppService appService;
	@Autowired
	private RedisClientTemplate redisClient;
	@Autowired
	private PrivilegeUserService privilegeUserService;
    /**
     * 菜单修改接口
     * @return Json
     */
    @RequestMapping("modifyMenu")
    public void modifyMenu(HttpServletRequest request,HttpServletResponse response) {
    	String menuName = request.getParameter("menuName");
    	String menuRule=request.getParameter("menuRule");    	
    	String menuId=request.getParameter("menuId");
    	String menuCode=request.getParameter("menuCode");
    	String appId=request.getParameter("appId");
    	String menuLevel=request.getParameter("menuLevel");
    	String parentId=request.getParameter("parentId");
    	String dislayOrder=request.getParameter("dislayOrder");
    	String status=request.getParameter("status");
    	Map<String, Object> map=new HashMap<String, Object>();
    	log.info("====================modify start======================");
    	if(!paraMandatoryCheck(Arrays.asList(menuId,appId))){
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
    	PrivilegeMenu pm=privilegeMenuService.findByMenuId(menuId, appId);
    	if(pm!=null){
    		pm.setAppId(appId);
        	pm.setMenuName(menuName);
        	if(nullEmptyBlankJudge(menuLevel)){
        		pm.setMenuLevel(0);
        	}else{
        		pm.setMenuLevel(Integer.parseInt(menuLevel));
        	}
        	pm.setMenuRule(menuRule);
        	if(nullEmptyBlankJudge(parentId)){
        		pm.setParentId("0");
        	}else{
        		pm.setParentId(parentId);
        	}
        	if(nullEmptyBlankJudge(dislayOrder)){
        		pm.setDisplayOrder(0);
        	}else{
        		pm.setDisplayOrder(Integer.parseInt(dislayOrder));
        	}
        	if(nullEmptyBlankJudge(status)){
        		pm.setStatus(0);
        	}else{
        		pm.setStatus(Integer.parseInt(status));
        	}
        	pm.setMenuCode(menuCode);
        	pm.setCreateTime(new Date());
        	Boolean uf =privilegeMenuService.updatePrivilegeMenu(pm);
        	if(uf){
        		//更新缓存
        		PrivilegeAjaxMessage message=privilegeMenuService.updateAppMenuRedis(appId);
        		if (message.getCode().equals("1")) {
        			map.put("status", 1);
        		} else {
        			map.put("status", message.getCode());
        			map.put("error_code", message.getMessage());/* 数据不存在 */
        		}
        	}else{
        		map.put("status", 0);
        		map.put("error_code","10002");
        	}
    	}else{
    		map.put("status", 0);
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
	/**
	 * 菜单排序接口
	 */
	@RequestMapping("displayOrder")
	public void displayOrder(HttpServletRequest request,HttpServletResponse response) {
		String appId = request.getParameter("appId");
		String appUserId = request.getParameter("appUserId");
		String businessData = request.getParameter("businessData");
		if(!paraMandatoryCheck(Arrays.asList(appId, appUserId, businessData))){
			paraMandaChkAndReturn(10000, response,"必传参数中有空值");
			return;
		}
		App app = (App) redisClient.getObject(RedisConstant.APP_INFO + appId);
		if(app==null) {
			app=appService.findById(Integer.parseInt(appId));
			redisClient.setObject(RedisConstant.APP_INFO + appId, app);
		}
		Boolean f=OauthSignatureValidateHandler.validateSignature(request, app);
		if(!f){
			WebUtils.paraMandaChkAndReturn(5, response,"认证失败");
			return;
		}
		Map<String, Object> map=new HashMap<String, Object>();
		/* 验证用户是否存在 */
		PrivilegeUser privilegeUser = privilegeUserService.findByAppIdAndUserId(appId, appUserId);
		if (null == privilegeUser) {
			map.put("status", "0");
			map.put("error_code","10002");
			map.put("errMsg", "User Is Null");
			writeErrorJson(response,map);
			return;
		}
		List<PrivilegeMenu> privilegeMenus;
		try{ 	//将json转换为list
			JSONObject jsonObject = JSONObject.parseObject(businessData);
			JSONArray array = (JSONArray) jsonObject.get("menuList");
			privilegeMenus = array.toJavaList(PrivilegeMenu.class);
		} catch (Exception e) {
			map.put("status", "0");
			map.put("error_code","10000");
			map.put("errMsg", "json解析错误");
			writeErrorJson(response,map);
			return;
		}
		if (privilegeMenus != null && privilegeMenus.size() > 0) {
			Boolean flag = privilegeMenuService.updatePrivilegeMenuBatch(privilegeMenus, appId); //批量更新
			if(flag){
				//更新缓存
				PrivilegeAjaxMessage message = privilegeMenuService.updateAppMenuRedis(appId);
				if (message.getCode().equals("1")) {
					map.put("status", 1);
				} else {
					map.put("status", message.getCode());
					map.put("error_code", message.getMessage());/* 数据不存在 */
				}
			}else{
				map.put("status", 0);
				map.put("error_code","10002");
			}
		} else {
			map.put("status", 0);
			map.put("error_code","10001");
		}
		if(map.get("status")=="0"){
			writeErrorJson(response,map);
		}else{
			writeSuccessJson(response,map);
		}
	}
}