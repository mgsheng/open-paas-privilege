package cn.com.open.opensass.privilege.api;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.com.open.opensass.privilege.dao.cache.RedisDao;
import cn.com.open.opensass.privilege.model.App;
import cn.com.open.opensass.privilege.model.PrivilegeMenu;
import cn.com.open.opensass.privilege.model.PrivilegeRole;
import cn.com.open.opensass.privilege.model.PrivilegeUser;
import cn.com.open.opensass.privilege.redis.impl.RedisClientTemplate;
import cn.com.open.opensass.privilege.redis.impl.RedisConstant;
import cn.com.open.opensass.privilege.service.AppService;
import cn.com.open.opensass.privilege.service.PrivilegeGroupService;
import cn.com.open.opensass.privilege.service.PrivilegeMenuService;
import cn.com.open.opensass.privilege.service.PrivilegeResourceService;
import cn.com.open.opensass.privilege.service.PrivilegeRoleResourceService;
import cn.com.open.opensass.privilege.service.PrivilegeRoleService;
import cn.com.open.opensass.privilege.service.PrivilegeUserRedisService;
import cn.com.open.opensass.privilege.service.PrivilegeUserService;
import cn.com.open.opensass.privilege.tools.BaseControllerUtil;
import cn.com.open.opensass.privilege.tools.MenuProcessUtil;
import cn.com.open.opensass.privilege.tools.OauthSignatureValidateHandler;
import cn.com.open.opensass.privilege.tools.StringTool;
import cn.com.open.opensass.privilege.vo.PrivilegeAjaxMessage;
import cn.com.open.opensass.privilege.vo.PrivilegeMenuVo;
import cn.com.open.opensass.privilege.vo.PrivilegeResourceVo;
import cn.com.open.opensass.privilege.vo.PrivilegeUserVo;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Controller
@RequestMapping("/userRole/")
public class UserGetMenusController extends BaseControllerUtil {
	public static final String SIGN = RedisConstant.SIGN;

	private static final Logger log = LoggerFactory.getLogger(UserGetMenusController.class);
	@Autowired
	private PrivilegeUserService privilegeUserService;
	@Autowired
	private PrivilegeRoleService privilegeRoleService;
	@Autowired
	private PrivilegeResourceService privilegeResourceService;
	@Autowired
	private PrivilegeMenuService privilegeMenuService;
	@Autowired
	private PrivilegeRoleResourceService privilegeRoleResourceService;
	@Autowired
	private AppService appService;
	@Autowired
	private PrivilegeUserRedisService privilegeUserRedisService;
	@Autowired
	private RedisClientTemplate redisClient;
	@Autowired
	private PrivilegeGroupService privilegeGroupService;

	/**
	 * 用户角色权限获取接口
	 */
	@RequestMapping(value = "getUserPrivilegeMenus")
	public void getPrivilege(HttpServletRequest request, HttpServletResponse response,
			PrivilegeUserVo privilegeUserVo) {

		Map<String, Object> map = new HashMap<String, Object>();
		log.info("====================get user privilege start======================");
		if (!paraMandatoryCheck(Arrays.asList(privilegeUserVo.getAppId(), privilegeUserVo.getAppUserId()))) {
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
			paraMandaChkAndReturn(10001, response, "认证失败");
			return;
		}

		// 获取当前用户信息
		PrivilegeUser user = privilegeUserService.findByAppIdAndUserId(privilegeUserVo.getAppId(),
				privilegeUserVo.getAppUserId());
		if (user == null) {
			paraMandaChkAndReturn(10002, response, "用户不存在");
			return;
		} else {
			map.put("status", "1");
			map.put("appId", user.getAppId());
			map.put("appUserId", user.getAppUserId());
			map.put("appUserName", user.getAppUserName());
			map.put("deptId", user.getDeptId());
			map.put("groupId", user.getGroupId());
			map.put("privilegeFunId", user.getPrivilegeFunId());
			map.put("resourceId", user.getResourceId());

		}


		//用户菜单缓存
	
		Boolean boo = false;// 存放是否有管理员角色标志 true-有，false-没有
		int Type = 1;// 角色类型标识，1-普通用户，2-管理员（应用资源级别），3-组织机构管理员（组织机构资源）
		String privilegeResourceIds = user.getResourceId();
		String privilegeFunctionIds = user.getPrivilegeFunId();
		List<PrivilegeRole> roleList = privilegeRoleService.getRoleListByUserIdAndAppId(user.getAppUserId(),
				user.getAppId());
		for (PrivilegeRole role : roleList) {
			if (role.getRoleType() != null) {
				if (role.getRoleType() == 2) {// 若角色为系统管理员 则把app拥有的所有资源放入缓存
					if (role.getGroupId() != null && !role.getGroupId().isEmpty()) {
						Type = 3;
                        //机构管理员继承权限
                        user.setResourceId("");
                        user.setPrivilegeFunId("");
					} else {
						Type = 2;
					}
					boo = true;
					break;
				}
			}
		}
		map.put("isManager", boo);
		map.put("Type", Type);


        //用户资源缓存
        PrivilegeAjaxMessage roleMessage = privilegeUserRedisService.getRedisUserRole(privilegeUserVo.getAppId(),
                privilegeUserVo.getAppUserId());
        PrivilegeAjaxMessage menuMessage = new PrivilegeAjaxMessage();
        if (privilegeUserVo.getMenuCode() != null && privilegeUserVo.getMenuCode().length() > 0) {
            Map<String, String> userMap = new HashMap();
            userMap.put("appId", user.getAppId());
            userMap.put("appUserId", user.getAppUserId());
            userMap.put("menuCode", privilegeUserVo.getMenuCode());
            menuMessage = privilegeMenuService.getMenuRedisByMap(userMap);
        } else {
            menuMessage = privilegeMenuService.getMenuRedis(privilegeUserVo.getAppId(),
                    privilegeUserVo.getAppUserId());
        }
		// redis中没有roleMap，从数据库中查询并存入redis
		Map<String, Object> roleMap = new HashMap<String, Object>();
		if (roleMessage.getCode().equals("0")) {// code为0该用户不存在
			map.put("status", "0");
			writeErrorJson(response, map);
			return;
		} else {
			//获取用户角色资源缓存，
			JSONObject obj1 = JSONObject.fromObject(roleMessage.getMessage());// 将json字符串转换为json对象
			JSONArray objArray = (JSONArray) obj1.get("roleList");
			roleMap.put("roleList", objArray);
			objArray = (JSONArray) obj1.get("functionList");
			roleMap.put("functionList", objArray);
			// 去重处理
			Set<PrivilegeResourceVo> privilegeResourceVos = new HashSet<PrivilegeResourceVo>();
			// 如果为管理员 返回应用所有资源
			if (Type == 2) {
				PrivilegeAjaxMessage message = privilegeResourceService.getAppResRedis(user.getAppId());
				obj1 = JSONObject.fromObject(message.getMessage());
				objArray = (JSONArray) obj1.get("resourceList");
				List<PrivilegeResourceVo> resources = JSONArray.toList(objArray, PrivilegeResourceVo.class);
				privilegeResourceVos.addAll(resources);
			} else {
				objArray = (JSONArray) obj1.get("resourceList");
				List<PrivilegeResourceVo> userResourceVos = JSONArray.toList(objArray, PrivilegeResourceVo.class);
				privilegeResourceVos.addAll(userResourceVos);
			}
			roleMap.put("resourceList", privilegeResourceVos);
			map.putAll(roleMap);
		}

		// redis中没有menuMap，从数据库中查询并存入redis
		Map<String, Object> menuMap = new HashMap<String, Object>();
		if (menuMessage.getCode().equals("0")) {
			List<PrivilegeMenu> privilegeMenuList = new ArrayList<PrivilegeMenu>();
			if (boo) {// 有管理员角色获取所有应用下菜单
				JSONObject obj1 = new JSONObject();
				JSONArray objArray = new JSONArray();
				Set<PrivilegeMenuVo> menuSet = new HashSet<PrivilegeMenuVo>();
				if (Type == 2) {
					// 如果为管理员 返回应用所有菜单
					PrivilegeAjaxMessage message = new PrivilegeAjaxMessage();
					if(privilegeUserVo.getMenuCode()!=null&&privilegeUserVo.getMenuCode().length()>0)
					{
						Map<String,String> userMap=new HashMap();
						userMap.put("appId", user.getAppId());
						userMap.put("menuCode", privilegeUserVo.getMenuCode());
						message=privilegeMenuService.getManagerMenuRedis(userMap);
					}
					else
					{
						message=privilegeMenuService.getAppMenuRedis(user.getAppId());
					}
					
					obj1 = JSONObject.fromObject(message.getMessage());
					JSONArray menuArray = obj1.getJSONArray("menuList");
					List<PrivilegeMenuVo> menuList = JSONArray.toList(menuArray, PrivilegeMenuVo.class);
					Set<PrivilegeMenuVo> set = new HashSet<PrivilegeMenuVo>();
					menuSet.addAll(menuList);
				} else {
					PrivilegeAjaxMessage message = privilegeGroupService.findGroupPrivilege(user.getGroupId(),
							user.getAppId());
					if (message.getCode().equals("1")) {
						obj1 = JSONObject.fromObject(message.getMessage());
						JSONArray menuArray = obj1.getJSONArray("menuList");
						List<PrivilegeMenuVo> menuList = JSONArray.toList(menuArray, PrivilegeMenuVo.class);
						menuSet.addAll(menuList);
					}
				}
				menuMap.put("menuList", menuSet);
			} else {// 无管理员角色获取相应权限菜单
				privilegeMenuList = privilegeMenuService.getMenuListByUserId(user.getAppUserId(), user.getAppId());
				// 根据roleResource表中functionId无resourceId 查询菜单
				List<String> FuncIds = privilegeRoleResourceService
						.findfindUserResourcesFunIdByResIsNull(user.getAppId(), user.getAppUserId());
				if (FuncIds != null && FuncIds.size() > 0) {
					String[] funcIds = StringTool.listToString(FuncIds).split(",");
					List<PrivilegeMenu> menus = privilegeMenuService.getMenuListByFunctionId(funcIds, user.getAppId());
					privilegeMenuList.addAll(menus);
				}
				// 根据user表中functionId resourceId 查询菜单
				if (privilegeFunctionIds != null && !("").equals(privilegeFunctionIds)) {
					String[] funIds = privilegeFunctionIds.split(",");
					List<PrivilegeMenu> menus = privilegeMenuService.getMenuListByFunctionId(funIds, user.getAppId());
					privilegeMenuList.addAll(menus);
				}
				if (privilegeResourceIds != null && !("").equals(privilegeResourceIds)) {
					String[] resIds = privilegeResourceIds.split(",");
					List<PrivilegeMenu> menus2 = privilegeMenuService.getMenuListByResourceId2(resIds, user.getAppId());
					privilegeMenuList.addAll(menus2);
				}
				Set<PrivilegeMenuVo> privilegeMenuListReturn = new HashSet<PrivilegeMenuVo>();
				Set<PrivilegeMenuVo> privilegeMenuListData = privilegeMenuService.getAllMenuByUserId(privilegeMenuList,
						privilegeMenuListReturn); /* 缓存中是否存在 */
				menuMap.put("menuList", privilegeMenuListData);
			}
			map.putAll(menuMap);
		} else {
			JSONObject obj1 = JSONObject.fromObject(menuMessage.getMessage());// 将json字符串转换为json对象
			JSONArray objArray = null;
			// 去重处理
			Set<PrivilegeMenuVo> menuSet = new HashSet<PrivilegeMenuVo>();
			if (Type == 2) {
				// 如果为管理员 返回应用所有菜单
				PrivilegeAjaxMessage message = new PrivilegeAjaxMessage() ;
				if(privilegeUserVo.getMenuCode()!=null&&privilegeUserVo.getMenuCode().length()>0)
				{
					Map<String,String> userMap=new HashMap();
					userMap.put("appId", user.getAppId());
					userMap.put("menuCode", privilegeUserVo.getMenuCode());
					message=privilegeMenuService.getManagerMenuRedis(userMap);
				}
				else
				{
					message=privilegeMenuService.getAppMenuRedis(user.getAppId());
				}
				
				obj1 = JSONObject.fromObject(message.getMessage());
				JSONArray menuArray = obj1.getJSONArray("menuList");
				List<PrivilegeMenuVo> menuList = JSONArray.toList(menuArray, PrivilegeMenuVo.class);
				// 公共的菜单
				menuSet.addAll(menuList);
			} else {
				objArray = (JSONArray) obj1.get("menuList");
				List<PrivilegeMenuVo> userMenuList=JSONArray.toList(objArray, PrivilegeMenuVo.class);
				menuSet.addAll(userMenuList);
			}
			menuMap.put("menuList", menuSet);
			map.putAll(menuMap);
		}
		  Set<PrivilegeMenuVo> menuSet=(Set<PrivilegeMenuVo>) map.get("menuList");
		    menuSet=MenuProcessUtil.processMenuCode(menuSet, privilegeUserVo.getMenuCode());
			menuMap.put("menuList", menuSet);
			map.putAll(menuMap);
		if (map.get("status") == "0") {
			writeErrorJson(response, map);
		} else {
			writeSuccessJson(response, map);
		}
		return;
	
	}
}
