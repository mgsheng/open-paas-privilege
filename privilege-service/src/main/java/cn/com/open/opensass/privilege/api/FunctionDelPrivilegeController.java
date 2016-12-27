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

import cn.com.open.opensass.privilege.service.PrivilegeFunctionService;
import cn.com.open.opensass.privilege.tools.BaseControllerUtil;

/**
 * 权限功能删除接口
 */
@Controller
@RequestMapping("/function/")
public class FunctionDelPrivilegeController extends BaseControllerUtil{
	private static final Logger log = LoggerFactory.getLogger(FunctionDelPrivilegeController.class);
	@Autowired
	private PrivilegeFunctionService privilegeFunctionService;

    /**
     * 权限功能删除接口
     * @return Json
     */
    @RequestMapping("delFunction")
    public void delFunction(HttpServletRequest request,HttpServletResponse response) {
    	String functionId=request.getParameter("functionId");
    	String appId=request.getParameter("appId");
    	Map<String, Object> map=new HashMap<String, Object>();
    	log.info("====================delete start======================");
    	if(!paraMandatoryCheck(Arrays.asList(functionId,appId))){
    		  paraMandaChkAndReturn(10000, response,"必传参数中有空值");
              return;	
    	}
    	String functionIds[]=functionId.split(",");
    	
    	if(functionIds!=null&&functionIds.length>0){
    		Boolean f=privilegeFunctionService.deleteByFunctionIds(functionIds);
    		if(f){
    			map.put("status","1");
    		}else{
    			map.put("status","0");
        		map.put("error_code","10001");
    		}
    	}else{
    		map.put("status","0");
    		map.put("error_code","10002");
    	}
    	if(map.get("status")=="0"){
    		writeErrorJson(response,map);
    	}else{
    		writeSuccessJson(response,map);
    	}
        return;
    	
    }
}