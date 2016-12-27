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
import cn.com.open.opensass.privilege.service.PrivilegeMenuService;

/**
 * 
 */
@Service("privilegeMenuService")
public class PrivilegeMenuServiceImpl implements PrivilegeMenuService {
	private static final Logger log = LoggerFactory.getLogger(PrivilegeMenuServiceImpl.class);
	private static final String prefix = RedisConstant.USERMENU_CACHE;
	private static final String AppMenuRedisPrefix = RedisConstant.APPMENU_CACHE;
	@Autowired
	private RedisClientTemplate redisClientTemplate;
    @Autowired
    private PrivilegeMenuRepository privilegeMenuRepository;
    @Autowired
    private RedisDao redisDao;

	@Override
	public Boolean savePrivilegeMenu(PrivilegeMenu privilegeMenu) {
	  try {
		  privilegeMenuRepository.savePrivilegeMenu(privilegeMenu);
		  return true;
	  } catch (Exception e) {
		// TODO: handle exception
		  return false;
	  }
		
		
	}

	@Override
	public PrivilegeMenu findByMenuId(String menuId,String appId) {
		// TODO Auto-generated method stub
		return privilegeMenuRepository.findByMenuId(menuId,appId);
	}

	@Override
	public List<PrivilegeMenu> findMenuPage(String menuId, String appId,
			int startRow, int pageSize) {
		// TODO Auto-generated method stub
		return privilegeMenuRepository.findMenuPage(menuId, appId, startRow, pageSize);
	}

	@Override
	public Boolean updatePrivilegeMenu(PrivilegeMenu privilegeMenu) {
		// TODO Auto-generated method stub
		try {
			privilegeMenuRepository.updatePrivilegeMenu(privilegeMenu);
			return true;
		} catch (Exception e) {
			// TODO: handle exception
			return false;
		}
	}

	@Override
	public Boolean deleteByMenuId(String menuId) {
		// TODO Auto-generated method stub
		try {
			privilegeMenuRepository.deleteByMenuId(menuId);
			return true;
		} catch (Exception e) {
			// TODO: handle exception
			return false;
		}
	}

	@Override
	public List<PrivilegeMenu> getMenuListByUserId(String appUserId,String appId) {
		return privilegeMenuRepository.getMenuListByUserId(appUserId,appId);
	}

	

	@Override
	public PrivilegeAjaxMessage getMenuRedis(String appId, String appUserId) {
		PrivilegeAjaxMessage ajaxMessage = new PrivilegeAjaxMessage();
		String menuJedis = redisDao.getUrlRedis(prefix,appId, appUserId);
		if(null == menuJedis || menuJedis.length()<=0)
		{
			List<PrivilegeMenu> privilegeMenuList = getMenuListByUserId(appUserId,appId);
			if(privilegeMenuList.size()<=0)
            {
                ajaxMessage.setCode("0");
                ajaxMessage.setMessage("MENU-IS-NULL");
                return ajaxMessage;
            }
			Set<PrivilegeMenuVo> privilegeMenuListReturn = new HashSet<PrivilegeMenuVo>();

			Set<PrivilegeMenuVo> privilegeMenuListData = getAllMenuByUserId(privilegeMenuList,privilegeMenuListReturn);  /*缓存中是否存在*/
            if(privilegeMenuListData.size()<=0)
            {
                ajaxMessage.setCode("0");
                ajaxMessage.setMessage("MENU-IS-NULL");
                return ajaxMessage;
            }

			PrivilegeUrl privilegeUrl = new PrivilegeUrl();
			Map<Object, Object> map = new HashMap<Object, Object>();
			map.put("menuList",privilegeMenuListData);
			String json=new JSONArray().fromObject(map).toString();

			privilegeUrl.setPrivilegeUrl(json);
            /*写入redis*/
			log.info("getMenu接口获取数据并写入redis数据开始");
			redisDao.putUrlRedis(prefix,privilegeUrl, appId, appUserId);
            /*读取redis*/
			menuJedis = redisDao.getUrlRedis(prefix,appId, appUserId);
			log.info("getMenu接口获取数据并写入，读取redis数据开始："+menuJedis);
		}
		if (null != menuJedis && menuJedis.length() > 0) {
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
		boolean menuKeyExist = redisDao.existKeyRedis(prefix,appId,appUserId);
		if(menuKeyExist)
		{
			redisDao.deleteRedisKey(prefix,appId,appUserId);
		}
		return getMenuRedis(appId,appUserId);
	}

	@Override
	public PrivilegeAjaxMessage delMenuRedis(String appId, String appUserId) {
		PrivilegeAjaxMessage ajaxMessage = new PrivilegeAjaxMessage();
		boolean menuKeyExist = redisDao.deleteRedisKey(prefix,appId,appUserId);
		ajaxMessage.setCode("1");
		ajaxMessage.setMessage(menuKeyExist?"Success":"Failed");
		log.info("delMenuRedis接口删除key："+menuKeyExist);
		return ajaxMessage;

	}

	@Override
	public PrivilegeAjaxMessage existMenuKeyRedis(String appId, String appUserId) {
		PrivilegeAjaxMessage ajaxMessage = new PrivilegeAjaxMessage();
         /*获取用户UID*/
		log.info("existMenuKey是否存在redis数据");
		boolean exist = redisDao.existKeyRedis(prefix,appId, appUserId);
		ajaxMessage.setCode("1");
		ajaxMessage.setMessage(exist?"TRUE":"FALSE");
		return ajaxMessage;
	}

	@Override
	public Set<PrivilegeMenuVo> getAllMenuByUserId(List<PrivilegeMenu> privilegeMenuList, Set<PrivilegeMenuVo> privilegeMenuVoSet) {

		for (PrivilegeMenu privilegeMenu : privilegeMenuList)
		{
			if(null != privilegeMenu.getParentId() && privilegeMenu.getParentId().length()>0)
			{
				PrivilegeMenuVo  privilegeMenuVo = new PrivilegeMenuVo();
				privilegeMenuVo.setMenuId(privilegeMenu.id());
				privilegeMenuVo.setParentId(privilegeMenu.getParentId());
				privilegeMenuVo.setMenuName(privilegeMenu.getMenuName());
				privilegeMenuVo.setMenuRule(privilegeMenu.getMenuRule());
				privilegeMenuVo.setMenuLevel(privilegeMenu.getMenuLevel());
				privilegeMenuVo.setDisplayOrder(privilegeMenu.getDisplayOrder());
                /*如果是最父级目录，则添加到返回列表中，否则递归获取数据*/
				if(privilegeMenu.getParentId().equals("0"))
				{
					privilegeMenuVoSet.add(privilegeMenuVo);
				}
				else
				{
					privilegeMenuVoSet.add(privilegeMenuVo);
					PrivilegeMenu privilegeMenuListParents = getMenuById(privilegeMenu.getParentId());
					getAllMenuByUserId(Arrays.asList(privilegeMenuListParents),privilegeMenuVoSet);
				}
			}

		}
		return privilegeMenuVoSet;
	}

	/*@Override
	public Map<String, Object> findByMenuId(String menuId) {
		// TODO Auto-generated method stub
		return privilegeMenuRepository.getMenuById(menuId);
	}*/

	@Override
	public PrivilegeMenu getMenuById(String menuId) {
		
		return privilegeMenuRepository.getMenuById(menuId);
	}

	@Override
	public List<PrivilegeMenu> getMenuListByAppId(String appId) {
		return privilegeMenuRepository.getMenuListByAppId(appId);
	}

	@Override
	public PrivilegeAjaxMessage getAppMenuRedis(String appId) {
		PrivilegeAjaxMessage ajaxMessage=new PrivilegeAjaxMessage();
		Map<String, Object> MenuMap = new HashMap<String, Object>();

		// redis key
		String AppMenuRedisKey = AppMenuRedisPrefix + appId;
		/* 缓存中是否存在 存在返回 */
		log.info("获取缓存");
		String jsonString = redisClientTemplate.getString(AppMenuRedisKey);
		if (null != jsonString && jsonString.length() > 0) {
			ajaxMessage.setCode("1");
			ajaxMessage.setMessage(jsonString);
			System.err.println("缓存");
			return ajaxMessage;
		}
		
		log.info("从数据库获取数据");
		List<PrivilegeMenu> menuList=getMenuListByAppId(appId);
		if(menuList.size()<=0)
        {
            ajaxMessage.setCode("0");
            ajaxMessage.setMessage("MENU-IS-NULL");
            return ajaxMessage;
        }
		Set<PrivilegeMenuVo> privilegeMenuListReturn = new HashSet<PrivilegeMenuVo>();

		Set<PrivilegeMenuVo> privilegeMenuListData = getAllMenuByUserId(menuList,privilegeMenuListReturn);  /*缓存中是否存在*/
        if(privilegeMenuListData.size()<=0)
        {
            ajaxMessage.setCode("0");
            ajaxMessage.setMessage("MENU-IS-NULL");
            return ajaxMessage;
        }
        MenuMap.put("menuList", privilegeMenuListData);
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
		boolean RedisKeyExist = redisDao.existKeyRedis(AppMenuRedisPrefix, appId);
		if (RedisKeyExist) {
			delAppMenuRedis(appId);
		}
		log.info("更新redis");
		return getAppMenuRedis(appId);
	}

	


}