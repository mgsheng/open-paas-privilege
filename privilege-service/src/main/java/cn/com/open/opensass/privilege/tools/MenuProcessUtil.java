package cn.com.open.opensass.privilege.tools;

import java.util.HashSet;
import java.util.Set;

import cn.com.open.opensass.privilege.vo.PrivilegeMenuVo;
import cn.com.open.opensass.privilege.vo.PrivilegeMenusVo;

public class MenuProcessUtil {

	public  static Set<PrivilegeMenuVo> processMenuCode(Set<PrivilegeMenuVo> set,String menuCode){
		if(null==menuCode||menuCode.equals(""))
		return set;
		Set<PrivilegeMenuVo> set2=new HashSet<PrivilegeMenuVo>();
		for(PrivilegeMenuVo menu:set)
		{
			if(menu.getMenuCode()!=null&&menu.getMenuCode().equals(menuCode))
				set2.add(menu);
		}
		return set2;
	}
	
	public  static Set<PrivilegeMenusVo> processMenuCodes(Set<PrivilegeMenusVo> set,String menuCode){
		if(null==menuCode||menuCode.equals(""))
		return set;
		Set<PrivilegeMenusVo> set2=new HashSet<PrivilegeMenusVo>();
		for(PrivilegeMenusVo menu:set)
		{
			if(menu.getMenuCode()!=null&&menu.getMenuCode().equals(menuCode))
				set2.add(menu);
		}
		return set2;
	}
	
public static void main(String[] args) {
	
	Set<PrivilegeMenuVo> menuSet=	new HashSet<>();
	PrivilegeMenuVo vo1=new PrivilegeMenuVo(); 
	vo1.setAppId("8");
	vo1.setDisplayOrder(1);
	vo1.setMenuName("根目录1");
	vo1.setParentId("0");
	vo1.setMenuId("vo1");
	vo1.setMenuCode("1");
	PrivilegeMenuVo vo2=new PrivilegeMenuVo(); 
	vo2.setAppId("8");
	vo2.setDisplayOrder(1);
	vo2.setMenuName("二级目录");
	vo2.setParentId("vo1");
	vo2.setMenuId("vo2");
	PrivilegeMenuVo vo3=new PrivilegeMenuVo(); 
	vo3.setAppId("8");
	vo3.setDisplayOrder(1);
	vo3.setMenuName("三级目录");
	vo3.setParentId("vo2");
	vo3.setMenuId("vo3");
	vo3.setMenuCode("1");
	PrivilegeMenuVo vo4=new PrivilegeMenuVo(); 
	vo4.setAppId("8");
	vo4.setDisplayOrder(3);
	vo4.setMenuName("三级目录菜单1");
	vo4.setParentId("vo3");
	vo4.setMenuId("vo4");
	PrivilegeMenuVo vo5=new PrivilegeMenuVo(); 
	vo5.setAppId("8");
	vo5.setDisplayOrder(2);
	vo5.setMenuName("三级目录菜单2");
	vo5.setParentId("vo3");
	vo5.setMenuId("vo5");
	PrivilegeMenuVo vo6=new PrivilegeMenuVo(); 
	vo6.setAppId("8");
	vo6.setDisplayOrder(1);
	vo6.setMenuName("二级目录菜单1");
	vo6.setParentId("vo2");
	vo6.setMenuId("vo6");
	
	
	PrivilegeMenuVo bo1=new PrivilegeMenuVo(); 
	bo1.setAppId("8");
	bo1.setDisplayOrder(1);
	bo1.setMenuName("根目录2");
	bo1.setParentId("0");
	bo1.setMenuId("bo1");
	PrivilegeMenuVo bo2=new PrivilegeMenuVo(); 
	bo2.setAppId("8");
	bo2.setDisplayOrder(1);
	bo2.setMenuName("二级目录");
	bo2.setParentId("bo1");
	bo2.setMenuId("bo2");
	PrivilegeMenuVo bo3=new PrivilegeMenuVo(); 
	bo3.setAppId("8");
	bo3.setDisplayOrder(1);
	bo3.setMenuName("三级目录");
	bo3.setParentId("bo2");
	bo3.setMenuId("bo3");
	PrivilegeMenuVo bo4=new PrivilegeMenuVo(); 
	bo4.setAppId("8");
	bo4.setDisplayOrder(3);
	bo4.setMenuName("三级目录菜单1");
	bo4.setParentId("bo3");
	bo4.setMenuId("bo4");
	PrivilegeMenuVo bo5=new PrivilegeMenuVo(); 
	bo5.setAppId("8");
	bo5.setDisplayOrder(2);
	bo5.setMenuName("三级目录菜单2");
	bo5.setParentId("bo3");
	bo5.setMenuId("bo5");
	PrivilegeMenuVo bo6=new PrivilegeMenuVo(); 
	bo6.setAppId("8");
	bo6.setDisplayOrder(1);
	bo6.setMenuName("二级目录菜单1");
	bo6.setParentId("bo2");
	bo6.setMenuId("bo6");
	
	menuSet.add(vo1);
	menuSet.add(vo2);
	menuSet.add(vo3);
	menuSet.add(vo4);
	menuSet.add(vo5);
	menuSet.add(vo6);
	
	menuSet.add(bo1);
	menuSet.add(bo2);
	menuSet.add(bo3);
	menuSet.add(bo4);
	menuSet.add(bo5);
	menuSet.add(bo6);
	MenuProcessUtil util=new MenuProcessUtil();
	menuSet=	processMenuCode(menuSet,"");
	System.out.println(menuSet.size());
}
}
