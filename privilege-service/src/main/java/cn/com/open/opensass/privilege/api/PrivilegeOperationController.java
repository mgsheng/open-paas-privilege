package cn.com.open.opensass.privilege.api;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.com.open.opensass.privilege.model.PrivilegeOperation;
import cn.com.open.opensass.privilege.service.PrivilegeOperationService;
import cn.com.open.opensass.privilege.tools.BaseControllerUtil;
/**
 */
@Controller
@RequestMapping("/operation/")
public class PrivilegeOperationController extends BaseControllerUtil{
	private static final Logger log = LoggerFactory.getLogger(PrivilegeOperationController.class);
	@Autowired
	private PrivilegeOperationService privilegeOperationService;
	@RequestMapping("getOperation")
	public void getOperationName(HttpServletRequest request,HttpServletResponse response){
		String optId=request.getParameter("optId").trim();
		Map<String, Object> map = new HashMap<String, Object>();
		PrivilegeOperation opt=privilegeOperationService.findById(optId);
		if(opt!=null){
			map.put("optName", opt.getName());
		}
		writeSuccessJson(response, map);
		return;
	}
}
