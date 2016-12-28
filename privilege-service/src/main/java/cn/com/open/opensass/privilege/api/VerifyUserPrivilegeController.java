package cn.com.open.opensass.privilege.api;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.com.open.opensass.privilege.dao.cache.RedisDao;
import cn.com.open.opensass.privilege.redis.impl.RedisConstant;
import cn.com.open.opensass.privilege.tools.BaseControllerUtil;
/**
 *  用户权限认证接口
 * @author Administrator
 *
 */
@Controller
@RequestMapping("/userRole/")
public class VerifyUserPrivilegeController extends BaseControllerUtil{
	  private static final Logger log = LoggerFactory.getLogger(VerifyUserPrivilegeController.class);
	  private static final String prifix = RedisConstant.USERPRIVILEGES_CACHE;
	  private static final String jsonKeyName = "urlList";
	  @Autowired
	    private RedisDao redisDao;
	  @RequestMapping("verifyUserPrivilege")
	  public void verifyUserPrivilege(HttpServletRequest request,HttpServletResponse response){
		  String appUserid=request.getParameter("appUserid").trim();
		  String appId=request.getParameter("appId").trim();
		  String optUrl=request.getParameter("optUrl").trim();
		  log.info("===================verifyUserPrivilege start======================");
	        if (!paraMandatoryCheck(Arrays.asList(appUserid, appId,optUrl))) {
	            paraMandaChkAndReturn(10000, response, "必传参数中有空值");
	            return;
	        }
	        Map<String, Object> map=new HashMap<String, Object>();
 	        Boolean states=redisDao.existUrlRedis(prifix, jsonKeyName, optUrl, appId, appUserid);
	        if (states) {
	        	map.put("status","1");
			}else{
				map.put("status","0");
	    		map.put("error_code","10001");
			}
	        
	        if(map.get("status")=="0"){
	    		writeErrorJson(response,map);
	    	}else{
	    		writeSuccessJson(response,map);
	    	}
	        return;
	        
	  }
}
