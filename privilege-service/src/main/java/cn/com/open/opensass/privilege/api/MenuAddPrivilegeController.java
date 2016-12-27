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

import cn.com.open.opensass.privilege.model.PrivilegeMenu;
import cn.com.open.opensass.privilege.service.PrivilegeMenuService;
import cn.com.open.opensass.privilege.tools.BaseControllerUtil;

/**
 *  菜单添加接口
 */
@Controller
@RequestMapping("/menu/")
public class MenuAddPrivilegeController extends BaseControllerUtil{
	private static final Logger log = LoggerFactory.getLogger(MenuAddPrivilegeController.class);
	@Autowired
	private PrivilegeMenuService privilegeMenuService;

    /**
     * 菜单添加接口
     * @return Json
     */
    @RequestMapping("addMenu")
    public void addMenu(HttpServletRequest request,HttpServletResponse response) {
    	String menuName = "";
    	String menuRule="";
    	try {
    	if(!nullEmptyBlankJudge(request.getParameter("menuName"))){
    		menuName = new String(request.getParameter("menuName").getBytes("iso-8859-1"),"utf-8");	
    	}
    	if(!nullEmptyBlankJudge(request.getParameter("menuRule"))){
    		menuRule = new String(request.getParameter("menuRule").getBytes("iso-8859-1"),"utf-8");	
    	}
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	String appId=request.getParameter("appId");
    	String menuLevel=request.getParameter("menuLevel");
    	String menuCode=request.getParameter("menuCode");
    	String parentId=request.getParameter("parentId");
    	String dislayOrder=request.getParameter("dislayOrder");
    	String status=request.getParameter("status");
    	Map<String, Object> map=new HashMap<String, Object>();
    	log.info("====================add start======================");
    	if(!paraMandatoryCheck(Arrays.asList(menuName,appId))){
    		  paraMandaChkAndReturn(10000, response,"必传参数中有空值");
              return;	
    	}
    	PrivilegeMenu pm=new PrivilegeMenu();
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
    	Boolean f =privilegeMenuService.savePrivilegeMenu(pm);
    	if(f){
    		map.put("status","1");
    		map.put("menuId", pm.id());
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