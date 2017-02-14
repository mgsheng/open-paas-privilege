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

import cn.com.open.opensass.privilege.model.PrivilegeGroup;
import cn.com.open.opensass.privilege.model.PrivilegeUser;
import cn.com.open.opensass.privilege.service.PrivilegeGroupService;
import cn.com.open.opensass.privilege.service.PrivilegeUserService;
import cn.com.open.opensass.privilege.tools.BaseControllerUtil;
import net.sf.json.JSONObject;
/**
 * 获取User数据getUser?appId=&groupId=&start=&start=
 * 根据appId,appUserId查找用户 findUser?appId=&appUserId=
 */
@Controller
@RequestMapping("/user/")
public class PrivilegeUserController extends BaseControllerUtil{
	private static final Logger log = LoggerFactory.getLogger(PrivilegeUserController.class);
	@Autowired
	private PrivilegeUserService privilegeUserService;
	@Autowired
	private PrivilegeGroupService privilegeGroupService;
	@RequestMapping("getUser")
	public void getUserList(HttpServletRequest request,HttpServletResponse response){
		log.info("----getUser start---");
    	String appId=request.getParameter("appId");
    	String groupId=request.getParameter("groupId");
    	String start=request.getParameter("start");
    	String limit=request.getParameter("limit");
    	
    	if(!paraMandatoryCheck(Arrays.asList(appId,start,limit))){
    		  paraMandaChkAndReturn(10000, response,"必传参数中有空值");
              return;
    	}  
    	List<PrivilegeGroup> groupList=privilegeGroupService.findByAppId(appId);
    	List<PrivilegeUser> userList=privilegeUserService.findUserListByPage(appId, Integer.parseInt(start) ,  Integer.parseInt(limit),groupId);
		List<JSONObject> objectList=new ArrayList<JSONObject>();
    	for (PrivilegeUser privilegeUser : userList) {
			JSONObject object=JSONObject.fromObject(privilegeUser);
			for(PrivilegeGroup group:groupList){
				if (group.getGroupId().equals(privilegeUser.getGroupId())) {
					object.put("groupName", group.getGroupName());
					
				}
			}
			objectList.add(object);
		}
		
		int count=privilegeUserService.getUserCountByAppId(appId,groupId);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("rows", objectList);
		map.put("total", count);
		writeSuccessJson(response, map);
		return;
	}
	@RequestMapping("findUser")
	public void findUser(HttpServletRequest request,HttpServletResponse response){
		log.info("----findUser start---");
    	String appId=request.getParameter("appId");
    	String appUserId=request.getParameter("appUserId");
    	if(!paraMandatoryCheck(Arrays.asList(appId,appUserId))){
  		  paraMandaChkAndReturn(10000, response,"必传参数中有空值");
            return;
    	}  
    	PrivilegeUser user=privilegeUserService.findByAppIdAndUserId(appId, appUserId);
    	Map<String, Object> map = new HashMap<String, Object>();
    	if (user==null) {
			map.put("status", "0");
		}else {
			map.put("status", "1");
		}
		writeSuccessJson(response, map);
		return;
	}
	
}
