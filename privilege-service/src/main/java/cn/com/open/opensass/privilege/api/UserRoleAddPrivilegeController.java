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

import cn.com.open.opensass.privilege.model.App;
import cn.com.open.opensass.privilege.model.PrivilegeUser;
import cn.com.open.opensass.privilege.model.PrivilegeUserRole;
import cn.com.open.opensass.privilege.redis.impl.RedisClientTemplate;
import cn.com.open.opensass.privilege.redis.impl.RedisConstant;
import cn.com.open.opensass.privilege.service.AppService;
import cn.com.open.opensass.privilege.service.PrivilegeMenuService;
import cn.com.open.opensass.privilege.service.PrivilegeUrlService;
import cn.com.open.opensass.privilege.service.PrivilegeUserRedisService;
import cn.com.open.opensass.privilege.service.PrivilegeUserRoleService;
import cn.com.open.opensass.privilege.service.PrivilegeUserService;
import cn.com.open.opensass.privilege.tools.BaseControllerUtil;
import cn.com.open.opensass.privilege.tools.OauthSignatureValidateHandler;
import cn.com.open.opensass.privilege.vo.PrivilegeAjaxMessage;
import cn.com.open.opensass.privilege.vo.PrivilegeUserVo;

@Controller
@RequestMapping("/userRole/")
public class UserRoleAddPrivilegeController extends BaseControllerUtil {
	private static final Logger log = LoggerFactory.getLogger(UserRoleAddPrivilegeController.class);
	@Autowired
	private PrivilegeUserService privilegeUserService;
	@Autowired
	private PrivilegeUserRoleService privilegeUserRoleService;
	@Autowired
	private PrivilegeUserRedisService privilegeUserRedisService;
	@Autowired
	private AppService appService;
	@Autowired
	private RedisClientTemplate redisClient;
	@Autowired
	private PrivilegeMenuService privilegeMenuService;
	/**
	 * 用户角色初始创建接口
	 */
	@RequestMapping(value = "addRole")
	public void addRole(HttpServletRequest request, HttpServletResponse response, PrivilegeUserVo privilegeUserVo) {
		Map<String, Object> map = new HashMap<String, Object>();
		log.info("====================add user role start======================");
		if (!paraMandatoryCheck(Arrays.asList(privilegeUserVo.getAppId(), privilegeUserVo.getAppUserId(),
				privilegeUserVo.getAppUserName()))) {
			paraMandaChkAndReturn(10000, response, "必传参数中有空值");
			return;
		}
		App app = (App) redisClient.getObject(RedisConstant.APP_INFO + privilegeUserVo.getAppId());
		if (app == null) {
			app = appService.findById(Integer.parseInt(privilegeUserVo.getAppId()));
			redisClient.setObject(RedisConstant.APP_INFO + privilegeUserVo.getAppId(), app);
		}
		// 认证
		Boolean f = OauthSignatureValidateHandler.validateSignature(request, app);

		if (!f) {
			paraMandaChkAndReturn(10004, response, "认证失败");
			return;
		}
		PrivilegeUser user = null;
		// 查询业务用户是否已存在
		user = privilegeUserService.findByAppIdAndUserId(privilegeUserVo.getAppId(), privilegeUserVo.getAppUserId());
		if (user != null) {
			paraMandaChkAndReturn(10001, response, "该业务用户已存在");
			return;
		}
		// 存储用户
		user = new PrivilegeUser();
		user.setAppId(privilegeUserVo.getAppId());
		user.setPrivilegeRoleId(privilegeUserVo.getPrivilegeRoleId());
		user.setAppUserId(privilegeUserVo.getAppUserId());
		user.setAppUserName(privilegeUserVo.getAppUserName());
		user.setDeptId(privilegeUserVo.getDeptId());
		user.setGroupId(privilegeUserVo.getGroupId());
		user.setResourceId(privilegeUserVo.getResourceId());
		user.setPrivilegeFunId(privilegeUserVo.getPrivilegeFunId());

		Boolean sf = privilegeUserService.savePrivilegeUser(user);
		if (privilegeUserVo.getPrivilegeRoleId() != null && !("").equals(privilegeUserVo.getPrivilegeRoleId())) {
			if (sf) {
				// 存储用户角色关系
				String[] roles = privilegeUserVo.getPrivilegeRoleId().split(",");
				for (String role : roles) {
					PrivilegeUserRole userRole = new PrivilegeUserRole();
					userRole.setUserId(user.getuId());
					userRole.setPrivilegeRoleId(role);
					userRole.setCreateUser(privilegeUserVo.getCreateUser());
					userRole.setCreateUserId(privilegeUserVo.getCreateUserId());
					Boolean f1 = privilegeUserRoleService.savePrivilegeUserRole(userRole);
					if (!f1) {
						paraMandaChkAndReturn(10003, response, "用户角色关系添加失败");
						return;
					}
				}
				// 添加缓存
				PrivilegeAjaxMessage message = privilegeUserRedisService.updateUserRoleRedis(privilegeUserVo.getAppId(),
						privilegeUserVo.getAppUserId());
				privilegeMenuService.updateMenuRedis(privilegeUserVo.getAppId(),
						privilegeUserVo.getAppUserId());
				if (message.getCode().equals("1")) {
					map.put("status", "1");
				} else {
					map.put("status", message.getCode());
					map.put("error_code", message.getMessage());/* 数据不存在 */
				}
			} else {
				paraMandaChkAndReturn(10002, response, "用户添加失败");
				return;
			}
		}

		if (map.get("status") == "0") {
			writeErrorJson(response, map);
		} else {
			writeSuccessJson(response, map);
		}
		return;
	}
}
