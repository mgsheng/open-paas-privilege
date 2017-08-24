package cn.com.open.opensass.privilege.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.com.open.opensass.privilege.dao.cache.RedisDao;
import cn.com.open.opensass.privilege.infrastructure.repository.PrivilegeRoleRepository;
import cn.com.open.opensass.privilege.model.PrivilegeRole;
import cn.com.open.opensass.privilege.redis.impl.RedisClientTemplate;
import cn.com.open.opensass.privilege.redis.impl.RedisConstant;
import cn.com.open.opensass.privilege.service.PrivilegeRoleService;
import cn.com.open.opensass.privilege.vo.PrivilegeAjaxMessage;
import net.sf.json.JSONObject;

/**
 * 
 */
@Service("PrivilegeRoleService")
public class PrivilegeRoleServiceImpl implements PrivilegeRoleService {

	private static final String AppRoleRedisPrefix = RedisConstant.APPROLE_CACHE;
	//间隔符
	public static final String SIGN = RedisConstant.SIGN;
	//角色缓存版本key
	private static final String roleVersionRedisPrefix = RedisConstant.ROLEVERSIONCACHE;
	private static final Logger log = LoggerFactory.getLogger(PrivilegeRoleServiceImpl.class);
    @Autowired
    private PrivilegeRoleRepository privilegeRoleRepository;
    @Autowired
    private RedisClientTemplate redisClientTemplate;
    @Autowired
    private RedisDao redisDao;
	@Override
	public Boolean savePrivilegeRole(PrivilegeRole privilegeRole) {
		try{
			privilegeRoleRepository.savePrivilegeRole(privilegeRole);
			return true;
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public PrivilegeRole findRoleById(String pararentRoleId) {
		return privilegeRoleRepository.findByRoleId(pararentRoleId);
	}

	@Override
	public Boolean delPrivilegeRoleById(String privilegeRoleId) {
		try{
			privilegeRoleRepository.delPrivilegeRoleById(privilegeRoleId);
			return true;
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public Boolean updatePrivilegeRole(PrivilegeRole privilegeRole) {
		try{
			privilegeRoleRepository.updatePrivilegeRole(privilegeRole);
			return true;
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public List<String> findRoleByAppId(String appId) {
		return privilegeRoleRepository.findRoleByAppId(appId);
	}

	@Override
	public List<PrivilegeRole> findRoleByPage(String privilegeRoleId,String appId, String deptId, String groupId, int start, int limit) {
		return privilegeRoleRepository.findRoleByPage(privilegeRoleId,appId, deptId, groupId, start,limit);
	}

	@Override
	public int findRoleNoPage(String privilegeRoleId, String appId, String deptId, String groupId) {
		List<PrivilegeRole> list = privilegeRoleRepository.findRoleNoPage(privilegeRoleId,appId, deptId, groupId);
		return list.size();
	}

	@Override
	public List<Map<String, Object>> getRoleListByUserId(String appUserId, String appId) {
		// TODO Auto-generated method stub
		return privilegeRoleRepository.getRoleListByUserId(appUserId, appId);
	}

	@Override
	public PrivilegeAjaxMessage getAppRoleRedis(String appId) {
		PrivilegeAjaxMessage ajaxMessage = new PrivilegeAjaxMessage();
		Map<String, Object> roleMap = new HashMap<String, Object>();
		// redis key
		String AppRoleRedisKey = AppRoleRedisPrefix + appId;
		String jsonString = redisClientTemplate.getString(AppRoleRedisKey);
		if (null != jsonString && jsonString.length() > 0) {
			ajaxMessage.setCode("1");
			ajaxMessage.setMessage(jsonString);
			return ajaxMessage;
		}
		log.info("从数据库获取数据");
		//roleList
		List<Map<String, Object>> RoleRedisList=getRoleListByAppId(appId);
		roleMap.put("roleList", RoleRedisList);
		redisClientTemplate.setString(AppRoleRedisKey, JSONObject.fromObject(roleMap).toString());
		ajaxMessage.setCode("1");
		ajaxMessage.setMessage(JSONObject.fromObject(roleMap).toString());
		return ajaxMessage;
	}

	@Override
	public List<Map<String, Object>> getRoleListByAppId(String appId) {
		List<PrivilegeRole> roles=privilegeRoleRepository.getRoleListByAppId(appId);
		List<Map<String, Object>> roleRedis=new ArrayList<Map<String,Object>>();
		for(PrivilegeRole role:roles){
			Map<String , Object> map=new HashMap<String, Object>();
			map.put("appId", role.getAppId());
			map.put("deptId", role.getDeptId());
			map.put("deptName", role.getDeptName());
			map.put("groupId", role.getGroupId());
			map.put("groupName", role.getGroupName());
			map.put("privilegeRoleId", role.getPrivilegeRoleId());
			map.put("parentRoleId", role.getParentRoleId());
			map.put("remark", role.getRemark());
			map.put("roleLevel", role.getRoleLevel());
			map.put("roleName",role.getRoleName());
			map.put("status", role.getStatus());
			roleRedis.add(map);
		}
		
		
		return roleRedis;
	}

	@Override
	public PrivilegeAjaxMessage delAppRoleRedis(String appId) {
		PrivilegeAjaxMessage ajaxMessage = new PrivilegeAjaxMessage();
		Boolean RoleKeyExist = redisDao.deleteRedisKey(AppRoleRedisPrefix, appId);
		ajaxMessage.setCode("1");
		ajaxMessage.setMessage(RoleKeyExist ? "Success" : "Failed");
		log.info("delMenuRedis接口删除：" + RoleKeyExist);
		return ajaxMessage;
	}

	@Override
	public PrivilegeAjaxMessage updateAppRoleRedis(String appId) {
		boolean RedisKeyExist = redisDao.existKeyRedis(AppRoleRedisPrefix, appId);
		if (RedisKeyExist) {
			delAppRoleRedis(appId);
		}
		log.info("更新redis");
		return getAppRoleRedis(appId);
	}

	@Override
	public List<PrivilegeRole> getRoleListByUserIdAndAppId(String appUserId, String appId) {
		return privilegeRoleRepository.getRoleListByUserIdAndAppId(appUserId, appId);
	}

	@Override
	public List<PrivilegeRole> getRoleListByAppIdAndGroupId(String appId, String groupId,String roleName,Integer roleType,int start, int limit) {
		return privilegeRoleRepository.getRoleListByAppIdAndGroupId(appId, groupId,roleName,roleType,start,limit);
	}

	@Override
	public int getRoleCountByAppIdAndGroupId(String appId, String groupId,Integer roleType,String roleName) {
		return privilegeRoleRepository.getRoleCountByAppIdAndGroupId(appId, groupId,roleType,roleName);
	}
	/**
    *	更新角色版本号
    * @param appId 应用Id
    * @param privilegeRoleId  角色Id
    * @return PrivilegeAjaxMessage
    */
	@Override
	public PrivilegeAjaxMessage updateRoleVersion(String appId,
			String privilegeRoleId) {
		log.info("----------updateRoleVersion start------");
		PrivilegeAjaxMessage message = new PrivilegeAjaxMessage();
		Integer roleVersion = (Integer) redisClientTemplate.getObject(roleVersionRedisPrefix + appId + SIGN
						+ privilegeRoleId);
		if (roleVersion == null) {
			roleVersion = 1;
		} else {
			roleVersion += 1;
		}
		String result = redisClientTemplate.setObject(roleVersionRedisPrefix + appId + SIGN + privilegeRoleId, roleVersion);
		if ("OK".equals(result)) {
			message.setCode("1");
		} else {
			message.setCode("0");
		}
		message.setMessage(result);
		return message;
	}

}