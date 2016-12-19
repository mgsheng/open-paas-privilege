package cn.com.open.opensass.privilege.service.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import cn.com.open.opensass.privilege.dao.PrivilegeUrl;
import cn.com.open.opensass.privilege.dao.cache.RedisDao;
import cn.com.open.opensass.privilege.redis.impl.RedisConstant;
import cn.com.open.opensass.privilege.vo.PrivilegeAjaxMessage;
import cn.com.open.opensass.privilege.vo.PrivilegeMenuVo;
import net.sf.json.JSONArray;
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
	
    @Autowired
    private PrivilegeMenuRepository privilegeMenuRepository;
    @Autowired
    private RedisDao redisDao;
    private static final String prefix = RedisConstant.USERMENU_CACHE;

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
			String startRow, String pageSize) {
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
	public PrivilegeMenu getMenuById(String menuId) {
		return privilegeMenuRepository.getMenuById(menuId);
	}

	@Override
	public PrivilegeAjaxMessage getMenuRedis(String appId, String appUserId) {
		PrivilegeAjaxMessage ajaxMessage = new PrivilegeAjaxMessage();
		String menuJedis = redisDao.getUrlRedis(RedisConstant.USERMENU_CACHE,appId, appUserId);
		if(null == menuJedis || menuJedis.length()<=0)
		{
			List<PrivilegeMenu> privilegeMenuList = getMenuListByUserId(appUserId,appId);

			Set<PrivilegeMenuVo> privilegeMenuListReturn = new HashSet<PrivilegeMenuVo>();

			Set<PrivilegeMenuVo> privilegeMenuListData = getAllMenuByUserId(privilegeMenuList,privilegeMenuListReturn);  /*缓存中是否存在*/

			PrivilegeUrl privilegeUrl = new PrivilegeUrl();
			String json=new JSONArray().fromObject(privilegeMenuListData).toString();

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
			ajaxMessage.setMessage("NO REDIS DATA");
			return ajaxMessage;
		}
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
					PrivilegeMenu privilegeMenuListParents = getMenuById(privilegeMenu.getParentId());List<PrivilegeMenu> privilegeMenuListp = new ArrayList<PrivilegeMenu>();
					privilegeMenuListp.add(privilegeMenuListParents);
					getAllMenuByUserId(privilegeMenuListp,privilegeMenuVoSet);
				}
			}

		}
		return privilegeMenuVoSet;
	}


}