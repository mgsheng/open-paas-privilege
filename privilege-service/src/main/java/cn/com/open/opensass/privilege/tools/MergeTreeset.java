package cn.com.open.opensass.privilege.tools;

import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import cn.com.open.opensass.privilege.vo.PrivilegeMenuVo;
import cn.com.open.opensass.privilege.vo.PrivilegeResourceVo;
import cn.com.open.opensass.privilege.vo.UserMenuVo;


/**
 * Created by mulder on 2017/6/8.
 */
public class MergeTreeset {

    private  Set<PrivilegeMenuVo> menuSet;
    private  List<PrivilegeResourceVo> privilegeResourceList;
    private  Set<PrivilegeResourceVo> privilegeResourceSet;
    private  Set<UserMenuVo>  _userMenuVos = new TreeSet<UserMenuVo>();

    public MergeTreeset(Set<PrivilegeMenuVo> menuSet , List<PrivilegeResourceVo> privilegeResourceList){
        this.menuSet = menuSet;
        this.privilegeResourceList = privilegeResourceList;
    }
    public MergeTreeset(Set<PrivilegeMenuVo> menuSet , Set<PrivilegeResourceVo> privilegeResourceSet){
        this.menuSet = menuSet;
        this.privilegeResourceSet = privilegeResourceSet;
    }
    public UserMenuVo setUserMenuVo(PrivilegeMenuVo privilegeMenuVo){
        UserMenuVo userMenuVo = new UserMenuVo();
        userMenuVo.setAppId(privilegeMenuVo.getAppId());
        userMenuVo.setMenuId(privilegeMenuVo.getMenuId());
        userMenuVo.setMenuName(privilegeMenuVo.getMenuName());
        userMenuVo.setParentId(privilegeMenuVo.getParentId());
        userMenuVo.setDisplayOrder(privilegeMenuVo.getDisplayOrder());
        userMenuVo.setChildMenus(new TreeSet<UserMenuVo>());
        userMenuVo.setMenuCode(privilegeMenuVo.getMenuCode());
        for (PrivilegeResourceVo privilegeResourceVo : privilegeResourceSet) {
            if (privilegeMenuVo.getMenuId().equals(privilegeResourceVo.getMenuId())) {
                userMenuVo.setBaseUrl(privilegeResourceVo.getBaseUrl());
                break;
            }
        }
        return userMenuVo;
    }

    public void getChildren(String pid, UserMenuVo upuserMenuVo){
        for(Iterator<PrivilegeMenuVo> it = menuSet.iterator(); it.hasNext();) {
            PrivilegeMenuVo privilegeMenuVo = it.next();
            if(privilegeMenuVo.getParentId().equals(pid)){
                UserMenuVo userMenuVo = setUserMenuVo(privilegeMenuVo);
                upuserMenuVo.getChildMenus().add(userMenuVo);
                getChildren(privilegeMenuVo.getMenuId(),userMenuVo);
            }
        }

    }

    public void merge(String pid){
        for(Iterator<PrivilegeMenuVo> it = menuSet.iterator(); it.hasNext();) {
            PrivilegeMenuVo privilegeMenuVo = it.next();
            if(privilegeMenuVo.getParentId().equals("0")){
                UserMenuVo userMenuVo = setUserMenuVo(privilegeMenuVo);
                _userMenuVos.add(userMenuVo);
                 getChildren(privilegeMenuVo.getMenuId(),userMenuVo);
            }
        }
    }



    public  Set<UserMenuVo> get_userMenuVos() {
		return _userMenuVos;
	}
	public void getMergetResult(){
        System.out.println(_userMenuVos);
    }
}
