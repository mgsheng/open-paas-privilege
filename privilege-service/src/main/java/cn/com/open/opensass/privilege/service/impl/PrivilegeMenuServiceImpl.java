package cn.com.open.opensass.privilege.service.impl;

import java.util.*;

import cn.com.open.opensass.privilege.dao.PrivilegeUrl;
import cn.com.open.opensass.privilege.dao.cache.RedisDao;
import cn.com.open.opensass.privilege.redis.impl.RedisClientTemplate;
import cn.com.open.opensass.privilege.redis.impl.RedisConstant;
import cn.com.open.opensass.privilege.vo.PrivilegeAjaxMessage;
import cn.com.open.opensass.privilege.vo.PrivilegeMenuVo;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.com.open.opensass.privilege.infrastructure.repository.PrivilegeMenuRepository;
import cn.com.open.opensass.privilege.model.PrivilegeMenu;
import cn.com.open.opensass.privilege.model.PrivilegeRole;
import cn.com.open.opensass.privilege.model.PrivilegeRoleResource;
import cn.com.open.opensass.privilege.model.PrivilegeUser;
import cn.com.open.opensass.privilege.service.PrivilegeGroupService;
import cn.com.open.opensass.privilege.service.PrivilegeMenuService;
import cn.com.open.opensass.privilege.service.PrivilegeRoleResourceService;
import cn.com.open.opensass.privilege.service.PrivilegeRoleService;
import cn.com.open.opensass.privilege.service.PrivilegeUserService;

/**
 * 
 */
@Service("privilegeMenuService")
public class PrivilegeMenuServiceImpl implements PrivilegeMenuService {
	private static final Logger log = LoggerFactory.getLogger(PrivilegeMenuServiceImpl.class);
	private static final String prefix = RedisConstant.USERMENU_CACHE;
	private static final String AppMenuRedisPrefix = RedisConstant.APPMENU_CACHE;
	private static final String SIGN = RedisConstant.SIGN;
	private static final String appMenuVersionCache = RedisConstant.APPMENUVERSIONCACHE;
	@Autowired
	private RedisClientTemplate redisClientTemplate;
	@Autowired
	private PrivilegeMenuRepository privilegeMenuRepository;
	@Autowired
	private RedisDao redisDao;
	@Autowired  
	private PrivilegeRoleService privilegeRoleService;
	@Autowired
	private PrivilegeUserService privilegeUserService;
	@Autowired
	private PrivilegeRoleResourceService privilegeRoleResourceService;
	@Autowired
	private PrivilegeGroupService privilegeGroupService;
	
	@Override
	public Boolean savePrivilegeMenu(PrivilegeMenu privilegeMenu) {
		try {
			privilegeMenuRepository.savePrivilegeMenu(privilegeMenu);
			return true;
		} catch (Exception e) {
			return false;
		}

	}

	@Override
	public PrivilegeMenu findByMenuId(String menuId, String appId) {
		return privilegeMenuRepository.findByMenuId(menuId, appId);
	}

	@Override
	public List<PrivilegeMenu> findMenuPage(String menuId, String appId, int startRow, int pageSize) {
		return privilegeMenuRepository.findMenuPage(menuId, appId, startRow, pageSize);
	}

	@Override
	public Boolean updatePrivilegeMenu(PrivilegeMenu privilegeMenu) {
		try {
			privilegeMenuRepository.updatePrivilegeMenu(privilegeMenu);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	@Override
	public Boolean deleteByMenuId(String[] menuIds) {
		try {
			privilegeMenuRepository.deleteByMenuId(menuIds);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	@Override
	public List<PrivilegeMenu> getMenuListByUserId(String appUserId, String appId) {
		return privilegeMenuRepository.getMenuListByUserId(appUserId, appId);
	}

	@Override
	public PrivilegeAjaxMessage getMenuRedis(String appId, String appUserId) {
		PrivilegeAjaxMessage ajaxMessage = new PrivilegeAjaxMessage();
		/* 验证用户是否存在 */
		PrivilegeUser user = privilegeUserService.findByAppIdAndUserId(appId, appUserId);
		if (null == user) {
			ajaxMessage.setCode("0");
			ajaxMessage.setMessage("User Is Null");
			return ajaxMessage;
		}
		Integer version = (Integer) redisClientTemplate.getObject(appMenuVersionCache+appId);
		int Type=1; // 角色类型，1-普通用户，2-系统管理员，3-组织机构管理员
		//判断用户角色是否是系统管理员  
		List<PrivilegeRole> roles = privilegeRoleService.getRoleListByUserIdAndAppId(appUserId, appId);
		for (PrivilegeRole role : roles) {
			if(role.getRoleType()!=null){
				if (role.getRoleType() == 2) {//若角色为系统管理员  
					if (role.getGroupId()!=null&&!role.getGroupId().isEmpty()) {//若该管理员为组织机构管理员
						Type =3;
					}else{
						PrivilegeAjaxMessage message = getAppMenuRedis(appId);
						if ("1".equals(message.getCode())) {
							String redisJson = message.getMessage();
							JSONObject json=	JSONObject.fromObject(redisJson);
							json.put("version", version);
							redisClientTemplate.setString(prefix + appId + SIGN + appUserId, json.toString());
						}
						return message;
					}
				}
			}
			
		}
		//应用菜单版本号
		
//		String menuJedis = redisDao.getUrlRedis(prefix, appId, appUserId);
		String menuJedis = redisClientTemplate.getString(prefix+appId+  SIGN+appUserId);
		if (null == menuJedis || menuJedis.length() <= 0)

		{	
			List<PrivilegeMenu> privilegeMenuList = getMenuListByUserId(appUserId, appId);
			//通过查找RoleResource  查找相应的菜单
			List<String> FunIds=new ArrayList<String>();
			List<PrivilegeRoleResource> rivilegeRoleResources=privilegeRoleResourceService.findUserRoleResources(appId, appUserId);
			for(PrivilegeRoleResource roleResource:rivilegeRoleResources){
				if (roleResource.getPrivilegeFunId()==null||("").equals(roleResource.getPrivilegeFunId())) {
					List<PrivilegeMenu> menus=getMenuListByResourceId(roleResource.getResourceId(),appId);
					privilegeMenuList.addAll(menus);
				}else{
					//RoleResource 中functionId
					FunIds.add(roleResource.getPrivilegeFunId());
				}
			}
			
			//根据user表中functionId resourceId 查询菜单
			String functionIds = user.getPrivilegeFunId();
			String resourceIds = user.getResourceId();
			if (functionIds != null && !("").equals(functionIds)) {
				FunIds.add(functionIds);
			}
			
			if (FunIds != null&&FunIds.size()>0) {
				for (String funIds : FunIds) {
					String[] functIds = funIds.split(",");
					List<PrivilegeMenu> menus = getMenuListByFunctionId(functIds,appId);
					privilegeMenuList.addAll(menus);
				}
			}
			
			if (resourceIds != null && !("").equals(resourceIds)) {
				String[] resIds = resourceIds.split(",");
//				for (String id : resIds) {
//					List<PrivilegeMenu> menus = getMenuListByResourceId(id,appId);
//					privilegeMenuList.addAll(menus);
//					System.out.println(menus.size());
//				}
				List<PrivilegeMenu> menus = getMenuListByResourceId2(resIds,appId);
				privilegeMenuList.addAll(menus);
				//System.out.println(menus.size());
			}
			if (privilegeMenuList.size() <= 0) {
				ajaxMessage.setCode("0");
				ajaxMessage.setMessage("MENU-IS-NULL");
				return ajaxMessage;
			}
			Set<PrivilegeMenuVo> privilegeMenuListReturn = new HashSet<PrivilegeMenuVo>();

			Set<PrivilegeMenuVo> privilegeMenuListData = getAllMenuByUserId(privilegeMenuList,
					privilegeMenuListReturn); /* 缓存中是否存在 */
			
			if (Type == 3) {//如果用户角色为组织机构管理员
				//把该组织机构拥有的菜单放入缓存
				PrivilegeAjaxMessage message = privilegeGroupService.findGroupPrivilege(user.getGroupId(), appId);
				if (message.getCode().equals("1")) {
					JSONObject object=JSONObject.fromObject(message.getMessage());
					JSONArray array=object.getJSONArray("menuList");
					List<PrivilegeMenuVo> groupMenuList=JSONArray.toList(array, PrivilegeMenuVo.class);
					privilegeMenuListData.addAll(groupMenuList);
				}
			}
			if (privilegeMenuListData.size() <= 0) {
				ajaxMessage.setCode("0");
				ajaxMessage.setMessage("MENU-IS-NULL");
				return ajaxMessage;
			}

//			PrivilegeUrl privilegeUrl = new PrivilegeUrl();
			Map<Object, Object> map = new HashMap<Object, Object>();
			map.put("menuList", privilegeMenuListData);
			if (version != null) {
				map.put("version", version);
			}
			menuJedis = new JSONObject().fromObject(map).toString();

//			privilegeUrl.setPrivilegeUrl(json);
			/* 写入redis */
			log.info("getMenu接口获取数据并写入redis数据开始");
			 redisClientTemplate.setString(prefix+appId+  SIGN+appUserId,menuJedis);
			//redisDao.putUrlRedis(prefix, privilegeUrl, appId, appUserId);
			/* 读取redis */
//			menuJedis = redisDao.getUrlRedis(prefix, appId, appUserId);
			log.info("getMenu接口获取数据并写入，读取redis数据开始：" + menuJedis);
		}
		if (null != menuJedis && menuJedis.length() > 0) {
			//从缓存中获取应用菜单版本，与用户菜单缓存版本号对比，若版本号不相同，更新用户菜单缓存
			if (version != null) {
				JSONObject object = JSONObject.fromObject(menuJedis);
				Integer userMenuCacheVersions = (Integer) object.get("version");
				if (userMenuCacheVersions == null) {
					 ajaxMessage = updateMenuRedis(appId, appUserId);
				} else {
					if (version.equals(userMenuCacheVersions)) {
						ajaxMessage.setCode("1");
						ajaxMessage.setMessage(menuJedis);
					} else {
						ajaxMessage = updateMenuRedis(appId, appUserId);
					}
				}
				return ajaxMessage;
			}
			ajaxMessage.setCode("1");
			ajaxMessage.setMessage(menuJedis);
			return ajaxMessage;
		} else {
			ajaxMessage.setCode("0");
			ajaxMessage.setMessage("NULL");
			return ajaxMessage;
		}
	}

	@Override
	public PrivilegeAjaxMessage updateMenuRedis(String appId, String appUserId) {
		boolean menuKeyExist = redisDao.existKeyRedis(prefix, appId, appUserId);
		if (menuKeyExist) {
			redisDao.deleteRedisKey(prefix, appId, appUserId);
		}
		return getMenuRedis(appId, appUserId);
	}

	@Override
	public PrivilegeAjaxMessage delMenuRedis(String appId, String appUserId) {
		PrivilegeAjaxMessage ajaxMessage = new PrivilegeAjaxMessage();
		boolean menuKeyExist = redisDao.deleteRedisKey(prefix, appId, appUserId);
		ajaxMessage.setCode("1");
		ajaxMessage.setMessage(menuKeyExist ? "Success" : "Failed");
		log.info("delMenuRedis接口删除key：" + menuKeyExist);
		return ajaxMessage;

	}

	@Override
	public PrivilegeAjaxMessage existMenuKeyRedis(String appId, String appUserId) {
		PrivilegeAjaxMessage ajaxMessage = new PrivilegeAjaxMessage();
		/* 获取用户UID */
		log.info("existMenuKey是否存在redis数据");
		boolean exist = redisDao.existKeyRedis(prefix, appId, appUserId);
		ajaxMessage.setCode("1");
		ajaxMessage.setMessage(exist ? "TRUE" : "FALSE");
		return ajaxMessage;
	}

	@Override
	public Set<PrivilegeMenuVo> getAllMenuByUserId(List<PrivilegeMenu> privilegeMenuList,
			Set<PrivilegeMenuVo> privilegeMenuVoSet) {
		for (PrivilegeMenu privilegeMenu : privilegeMenuList) {
			if (privilegeMenu!=null&&null != privilegeMenu.getParentId() && !("").equals(privilegeMenu.getParentId())) {
				PrivilegeMenuVo privilegeMenuVo = new PrivilegeMenuVo();
				privilegeMenuVo.setMenuId(privilegeMenu.id());
				privilegeMenuVo.setParentId(privilegeMenu.getParentId());
				privilegeMenuVo.setMenuName(privilegeMenu.getMenuName());
				privilegeMenuVo.setMenuRule(privilegeMenu.getMenuRule());
				privilegeMenuVo.setMenuLevel(privilegeMenu.getMenuLevel());
				privilegeMenuVo.setDisplayOrder(privilegeMenu.getDisplayOrder());
				/* 如果是最父级目录，则添加到返回列表中，否则递归获取数据 */
				if (privilegeMenu.getParentId().equals("0")) {
					privilegeMenuVoSet.add(privilegeMenuVo);
				} else {
					privilegeMenuVoSet.add(privilegeMenuVo);
					PrivilegeMenu privilegeMenuListParents = getMenuById(privilegeMenu.getParentId(),privilegeMenu.getAppId());
					if(privilegeMenuListParents!=null){
						getAllMenuByUserId(Arrays.asList(privilegeMenuListParents), privilegeMenuVoSet);
					}
				}
			}

		}
		return privilegeMenuVoSet;
	}

	/*
	 * @Override public Map<String, Object> findByMenuId(String menuId) { //
	 * TODO Auto-generated method stub return
	 * privilegeMenuRepository.getMenuById(menuId); }
	 */

	@Override
	public PrivilegeMenu getMenuById(String menuId,String appId) {

		return privilegeMenuRepository.getMenuById(menuId,appId);
	}
	
	@Override
	public List<PrivilegeMenuVo> getMenuVoListByAppId(String appId) {
		List<PrivilegeMenu> privilegeMenus=privilegeMenuRepository.getMenuListByAppId(appId);
		List<PrivilegeMenuVo> privilegeMenuVos=new ArrayList<PrivilegeMenuVo>();
		for(PrivilegeMenu privilegeMenu:privilegeMenus){
			if (privilegeMenu!=null) {
				PrivilegeMenuVo privilegeMenuVo=new PrivilegeMenuVo();
				privilegeMenuVo.setMenuId(privilegeMenu.id());
				privilegeMenuVo.setParentId(privilegeMenu.getParentId());
				privilegeMenuVo.setMenuName(privilegeMenu.getMenuName());
				privilegeMenuVo.setMenuRule(privilegeMenu.getMenuRule());
				privilegeMenuVo.setMenuLevel(privilegeMenu.getMenuLevel());
				privilegeMenuVo.setMenuCode(privilegeMenu.getMenuCode());
				privilegeMenuVo.setDisplayOrder(privilegeMenu.getDisplayOrder());
				privilegeMenuVo.setStatus(privilegeMenu.getStatus());
				privilegeMenuVo.setAppId(privilegeMenu.getAppId());
				privilegeMenuVos.add(privilegeMenuVo);
			}
		}
		return privilegeMenuVos;
	}

	@Override
	public PrivilegeAjaxMessage getAppMenuRedis(String appId) {
		PrivilegeAjaxMessage ajaxMessage = new PrivilegeAjaxMessage();
		Map<String, Object> MenuMap = new HashMap<String, Object>();

		// redis key
		String AppMenuRedisKey = AppMenuRedisPrefix + appId;
		/* 缓存中是否存在 存在返回 */
		log.info("获取缓存");
		String jsonString = redisClientTemplate.getString(AppMenuRedisKey);
		if (null != jsonString && jsonString.length() > 0) {
			ajaxMessage.setCode("1");
			ajaxMessage.setMessage(jsonString);
			return ajaxMessage;
		}

		log.info("从数据库获取数据");
		List<PrivilegeMenuVo> menuList = getMenuVoListByAppId(appId);
		//List<PrivilegeMenu> menuList=getMenuListByAppId(appId);
		if (menuList.size() <= 0) {
			ajaxMessage.setCode("0");
			ajaxMessage.setMessage("MENU-IS-NULL");
			return ajaxMessage;
		}
		
		Set<PrivilegeMenuVo> menus=new HashSet<PrivilegeMenuVo>();
		menus.addAll(menuList);
		MenuMap.put("menuList", menus);
		redisClientTemplate.setString(AppMenuRedisKey, JSONObject.fromObject(MenuMap).toString());
		ajaxMessage.setCode("1");
		ajaxMessage.setMessage(JSONObject.fromObject(MenuMap).toString());
		return ajaxMessage;
	}

	@Override
	public PrivilegeAjaxMessage delAppMenuRedis(String appId) {
		PrivilegeAjaxMessage ajaxMessage = new PrivilegeAjaxMessage();
		Boolean RoleKeyExist = redisDao.deleteRedisKey(AppMenuRedisPrefix, appId);
		ajaxMessage.setCode("1");
		ajaxMessage.setMessage(RoleKeyExist ? "Success" : "Failed");
		log.info("delMenuRedis接口删除：" + RoleKeyExist);
		return ajaxMessage;
	}

	@Override
	public PrivilegeAjaxMessage updateAppMenuRedis(String appId) {
		//更新应用菜单版本号
		Integer version = (Integer) redisClientTemplate.getObject(appMenuVersionCache+appId);
		if (version == null) {
			version = 1;
		} else {
			version += 1;
		}
		redisClientTemplate.setObject(appMenuVersionCache+appId,version);
		boolean RedisKeyExist = redisDao.existKeyRedis(AppMenuRedisPrefix, appId);
		if (RedisKeyExist) {
			delAppMenuRedis(appId);
		}
		log.info("更新redis");
		return getAppMenuRedis(appId);
	}

	@Override
	public List<PrivilegeMenu> getMenuListByResourceId(String resourceId,String appId) {
		return privilegeMenuRepository.getMenuListByResourceId(resourceId,appId);
	}
	@Override
	public List<PrivilegeMenu> getMenuListByResourceId2(String resourceId[],String appId) {
		return privilegeMenuRepository.getMenuListByResourceId2(resourceId,appId);
	}
	@Override
	public List<PrivilegeMenu> getMenuListByFunctionId(String[] functionIds,String appId) {
		return privilegeMenuRepository.getMenuListByFunctionId(functionIds,appId);
	}



	@Override
	public List<PrivilegeMenu> findByParentId(String parentId, String appId) {
		return privilegeMenuRepository.getMenuListByParentId(parentId,appId);
	}

	@Override
	public List<PrivilegeMenu> getMenuListByAppId(String appId) {
		return privilegeMenuRepository.getMenuListByAppId(appId);
	}

	@Override
	public List<PrivilegeMenuVo> findMenuByResourceType(Integer resourceType) {
		List<PrivilegeMenu> menuList=privilegeMenuRepository.findMenuByResourceType(resourceType);
		List<PrivilegeMenuVo> menuVoList=new ArrayList<PrivilegeMenuVo>();
		for (PrivilegeMenu privilegeMenu : menuList) {
			if (privilegeMenu!=null) {
				PrivilegeMenuVo privilegeMenuVo=new PrivilegeMenuVo();
				privilegeMenuVo.setMenuId(privilegeMenu.id());
				privilegeMenuVo.setParentId(privilegeMenu.getParentId());
				privilegeMenuVo.setMenuName(privilegeMenu.getMenuName());
				privilegeMenuVo.setMenuRule(privilegeMenu.getMenuRule());
				privilegeMenuVo.setMenuLevel(privilegeMenu.getMenuLevel());
				privilegeMenuVo.setMenuCode(privilegeMenu.getMenuCode());
				privilegeMenuVo.setDisplayOrder(privilegeMenu.getDisplayOrder());
				privilegeMenuVo.setStatus(privilegeMenu.getStatus());
				menuVoList.add(privilegeMenuVo);
			}
		}	
		return menuVoList;
	}
	


}