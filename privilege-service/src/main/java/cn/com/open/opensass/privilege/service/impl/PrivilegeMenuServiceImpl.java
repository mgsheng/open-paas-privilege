package cn.com.open.opensass.privilege.service.impl;

import java.util.List;

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
	
    @Autowired
    private PrivilegeMenuRepository privilegeMenuRepository;

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

  

}