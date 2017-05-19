package cn.com.open.opensass.privilege.api;

import java.util.ArrayList;
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
import cn.com.open.opensass.privilege.model.PrivilegeMenu;
import cn.com.open.opensass.privilege.redis.impl.RedisClientTemplate;
import cn.com.open.opensass.privilege.redis.impl.RedisConstant;
import cn.com.open.opensass.privilege.service.AppService;
import cn.com.open.opensass.privilege.service.PrivilegeMenuService;
import cn.com.open.opensass.privilege.tools.BaseControllerUtil;
import cn.com.open.opensass.privilege.tools.OauthSignatureValidateHandler;
import cn.com.open.opensass.privilege.tools.WebUtils;
import cn.com.open.opensass.privilege.vo.PrivilegeMenuVo;

/**
 *  菜单查询接口
 */
@Controller
@RequestMapping("/menu/")
public class MenuGetPrivilegeController extends BaseControllerUtil{
	private static final Logger log = LoggerFactory.getLogger(MenuGetPrivilegeController.class);
	@Autowired
	private PrivilegeMenuService privilegeMenuService;
	@Autowired
	private AppService appService;
	@Autowired
	private RedisClientTemplate redisClient;

    /**
     * 菜单查询接口
     * @return Json
     */
    @RequestMapping("getMenus")
    public void getMenus(HttpServletRequest request,HttpServletResponse response) {
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
    	App app = (App) redisClient.getObject(RedisConstant.APP_INFO+appId);
	    if(app==null)
		   {
			   app=appService.findById(Integer.parseInt(appId));
			   redisClient.setObject(RedisConstant.APP_INFO+appId, app);
		  }
		boolean b = OauthSignatureValidateHandler.validateSignature(request, app);
		Boolean f= b;
		if(!f){
			WebUtils.paraMandaChkAndReturn(5, response,"认证失败");
			return;
		}
    	List<PrivilegeMenu>lists=privilegeMenuService.findMenuPage(menuId, appId,Integer.parseInt(start),Integer.parseInt(limit));
    	List<PrivilegeMenu> mlist=new ArrayList<PrivilegeMenu>();
    	if(lists!=null&&lists.size()>0){
    		for(PrivilegeMenu menu:lists){
    			PrivilegeMenu m = new PrivilegeMenu();
    			m.setMenuId(menu.id());
    			m.setParentId(menu.getParentId());
    			m.setMenuName(menu.getMenuName());
    			m.setMenuRule(menu.getMenuRule());
    			m.setMenuLevel(menu.getMenuLevel());
    			m.setResourceType(menu.getResourceType());
    			m.setMenuCode(menu.getMenuCode());
    			m.setDisplayOrder(menu.getDisplayOrder());
    			m.setStatus(menu.getStatus());
    			if(!("0").equals(menu.getParentId())){
        			m.setIsLeaf("1");//1-叶子        		
    			}else{
    				m.setIsLeaf("0");//0-根   
    			}
    			mlist.add(m);
    		}
    		map.put("status", "1");
    		map.put("count", lists.size());
    		map.put("menuList",mlist);
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