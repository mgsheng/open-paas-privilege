package cn.com.open.opensass.privilege.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.AdaptableJobFactory;
import org.springframework.stereotype.Repository;

import cn.com.open.opensass.privilege.dao.cache.RedisDao;
import cn.com.open.opensass.privilege.model.PrivilegeUser;
import cn.com.open.opensass.privilege.redis.impl.RedisClientTemplate;
import cn.com.open.opensass.privilege.redis.impl.RedisConstant;
import cn.com.open.opensass.privilege.service.PrivilegeFunctionService;
import cn.com.open.opensass.privilege.service.PrivilegeResourceService;
import cn.com.open.opensass.privilege.service.PrivilegeRoleService;
import cn.com.open.opensass.privilege.service.PrivilegeUserRedisService;
import cn.com.open.opensass.privilege.service.PrivilegeUserService;
import cn.com.open.opensass.privilege.vo.PrivilegeAjaxMessage;
import net.sf.json.JSONObject;

@Repository("privilegeUserRedisService")
public class PrivilegeUserRedisServiceImpl implements PrivilegeUserRedisService {
	private static final String prefix = RedisConstant.USERROLE_CACHE;
	public static final String SIGN = RedisConstant.SIGN;
	private static final Logger log = LoggerFactory.getLogger(PrivilegeUserRedisServiceImpl.class);
	@Autowired
	private PrivilegeUserService privilegeUserService;
	@Autowired
	private PrivilegeRoleService privilegeRoleService;
	@Autowired
	private PrivilegeResourceService privilegeResourceService;
	@Autowired
	private PrivilegeFunctionService privilegeFunctionService;
	@Autowired
	private RedisClientTemplate redisClientTemplate;
	@Autowired
	private RedisDao redisDao;

	@Override
	public PrivilegeAjaxMessage getRedisUserRole(String appId, String appUserId) {

		PrivilegeAjaxMessage ajaxMessage = new PrivilegeAjaxMessage();
		/* 验证用户是否存在 */
		PrivilegeUser privilegeUser = privilegeUserService.findByAppIdAndUserId(appId, appUserId);
		if (null == privilegeUser) {
			ajaxMessage.setCode("0");
			ajaxMessage.setMessage("User Is Null");
			return ajaxMessage;
		}
		Map<String, Object> roleMap = new HashMap<String, Object>();

		// redis key
		String userCacheRoleKey = prefix + appId + SIGN + appUserId;
		/* 缓存中是否存在 存在返回 */
		log.info("获取缓存");
		String jsonString = redisClientTemplate.getString(userCacheRoleKey);
		if (null != jsonString && jsonString.length() > 0) {
			ajaxMessage.setCode("1");
			ajaxMessage.setMessage(jsonString);
			System.err.println("缓存");
			return ajaxMessage;
		}
		log.info("从数据库获取数据");
		List<Map<String, Object>> roles = privilegeRoleService.getRoleListByUserId(appUserId, appId);
		roleMap.put("roleList", roles);
		// resourceList
		List<Map<String, Object>> resourceList = privilegeResourceService.getResourceListByUserId(appUserId, appId);
		roleMap.put("resourceList", resourceList);
		// functionList
		List<Map<String, Object>> functionList = privilegeFunctionService.getFunctionListByUserId(appUserId, appId);
		roleMap.put("functionList", functionList);

		redisClientTemplate.setString(userCacheRoleKey, JSONObject.fromObject(roleMap).toString());
		ajaxMessage.setCode("1");
		ajaxMessage.setMessage(JSONObject.fromObject(roleMap).toString());
		return ajaxMessage;
	}

	@Override
	public PrivilegeAjaxMessage delUserRoleRedis(String appId, String appUserId) {
		PrivilegeAjaxMessage ajaxMessage = new PrivilegeAjaxMessage();
		Boolean UserRoleKeyExist = redisDao.deleteRedisKey(prefix, appId, appUserId);
		ajaxMessage.setCode("1");
		ajaxMessage.setMessage(UserRoleKeyExist ? "Success" : "Failed");
		log.info("delMenuRedis接口删除：" + UserRoleKeyExist);
		return ajaxMessage;
	}

	@Override
	public PrivilegeAjaxMessage updateUserRoleRedis(String appId, String appUserId) {
		boolean RedisKeyExist = redisDao.existKeyRedis(prefix, appId, appUserId);
		if (RedisKeyExist) {
			delUserRoleRedis(appId, appUserId);
		}
		log.info("更新redis");
		return getRedisUserRole(appId, appUserId);
	}

}
