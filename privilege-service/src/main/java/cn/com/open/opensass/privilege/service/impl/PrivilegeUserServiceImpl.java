package cn.com.open.opensass.privilege.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.com.open.opensass.privilege.service.*;
import cn.com.open.opensass.privilege.tools.CommonUtils;
import cn.com.open.opensass.privilege.vo.PrivilegeAjaxMessage;
import cn.com.open.opensass.privilege.vo.PrivilegeBatchUserVo;
import cn.com.open.opensass.privilege.vo.PrivilegeUserVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.com.open.opensass.privilege.infrastructure.repository.PrivilegeRoleRepository;
import cn.com.open.opensass.privilege.infrastructure.repository.PrivilegeUserRepository;
import cn.com.open.opensass.privilege.model.PrivilegeRole;
import cn.com.open.opensass.privilege.model.PrivilegeUser;

/**
 * 
 */
@Service("PrivilegeUserService")
public class PrivilegeUserServiceImpl implements PrivilegeUserService {

    @Autowired
    private PrivilegeUserRepository privilegeUserRepository;

	@Override
	public Integer connectionTest() {
		return privilegeUserRepository.connectionTest();
	}

	@Override
	public Boolean savePrivilegeUser(PrivilegeUser privilegeUser) {
		try{
			privilegeUserRepository.savePrivilegeUser(privilegeUser);
			return true;
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public Boolean delPrivilegeUserByUid(String uId) {
		try{
			privilegeUserRepository.delPrivilegeUserByUid(uId);
			return true;
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}
	
	@Override
	public Boolean delUserByAppIdAndAppUserId(String appId, String appUserId) {
		try{
			privilegeUserRepository.delUserByAppIdAndAppUserId(appId,appUserId);
			return true;
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public PrivilegeUser findByAppIdAndUserId(String appId, String appUserId) {
		return privilegeUserRepository.findByAppIdAndAppUserId(appId, appUserId);
	}

	@Override
	public List<PrivilegeUser> findByAppIdAndUserIds(String appId, String appUserId) {
		return privilegeUserRepository.findByAppIdAndAppUserIds(appId,appUserId);
	}


	@Override
	public Boolean updatePrivilegeUser(PrivilegeUser user) {
		try{
			privilegeUserRepository.updatePrivilegeUser(user);
			return true;
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
		
	}

	@Override
	public ArrayList<String> findUserResources(String appId, String appUserId) {
		return privilegeUserRepository.findUserResources(appId, appUserId);
	}

	@Override
	public List<PrivilegeUser> findUserListByPage(String appId, int start, int limit,String groupId) {
		return privilegeUserRepository.findUserListByPage(appId, start, limit,groupId);
	}

	@Override
	public int getUserCountByAppId(String appId,String groupId) {
		return privilegeUserRepository.getUserCountByAppId(appId,groupId);
	}

	@Override
	public Boolean batchUpdateResourceIds(List<PrivilegeBatchUserVo> list) {
		try{
			privilegeUserRepository.batchUpdateResourceIds(list);
			return true;
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public Map<String, Object> batchUpdateGroupResourceFunction(String appId, String groupId, String resourceId, String functionId) {
		Map<String, Object> map = new HashMap<String, Object>();
		String[] groupIds = groupId.split(",");
		//获取所有groupIds数据
		List<PrivilegeUser> privilegeUserList = findByGroupIdAndAppId(appId,groupIds);
		List<PrivilegeBatchUserVo> privilegeBatchUserVoList = new ArrayList<PrivilegeBatchUserVo>();
		PrivilegeBatchUserVo privilegeBatchUserVo = null;
		StringBuilder stringBuilderUser = new StringBuilder(50);
		//用户Id列表
		StringBuilder userIdList = new StringBuilder(50);
		for (PrivilegeUser privilegeUser:privilegeUserList){
			userIdList.append(privilegeUser.getAppUserId()).append(",");
			//生成批量更新语句
			privilegeBatchUserVo = new PrivilegeBatchUserVo();
			privilegeBatchUserVo.setAppId(appId);
			privilegeBatchUserVo.setAppUserId(privilegeUser.getAppUserId());
			if (privilegeUser.getResourceId() != null && privilegeUser.getResourceId() != "" && resourceId != null && resourceId != "") {
				privilegeBatchUserVo.setResourceIds(CommonUtils.getReplaceLaterString(privilegeUser.getResourceId(), resourceId));
			} else {
				privilegeBatchUserVo.setResourceIds(privilegeUser.getResourceId());
			}
			if (privilegeUser.getPrivilegeFunId() != null && privilegeUser.getPrivilegeFunId() != "" && functionId != null && functionId != "") {

				privilegeBatchUserVo.setFunctionIds(CommonUtils.getReplaceLaterString(privilegeUser.getPrivilegeFunId(),privilegeUser.getPrivilegeFunId()));
			} else {
				privilegeBatchUserVo.setFunctionIds(privilegeUser.getPrivilegeFunId());
			}
			privilegeBatchUserVoList.add(privilegeBatchUserVo);
		}

        /*执行批量操作*/
		if (privilegeBatchUserVoList.size() > 0) {
			String[] userIds = null;
			if(userIdList != null && userIdList.length()>1){
				userIds = userIdList.toString().substring(0,userIdList.length()-1).split(",");
			}
			Boolean batchDel =batchUpdateResourceIds(privilegeBatchUserVoList);
			if(batchDel){
				map.put("status", "1");
				map.put("message", "批量删除成功!");
				map.put("userIdList", userIds);
			} else {
				map.put("status", "0");
				map.put("error_code", "10003");
				map.put("message", "批量删除失败!");
			}
		} else {
			map.put("status", "0");
			map.put("error_code", "10004");
			map.put("message", "没有找到相关数据!");
		}
		return map;
	}

	@Override
	public List<PrivilegeUser> findByGroupIdAndAppId(String appId, String[] groupId) {
		return privilegeUserRepository.findByGroupIdAndAppId(appId,groupId);
	}
}