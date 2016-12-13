package cn.com.open.opensass.privilege.vo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class TestPrivilege {
	public static void main(String[] args) {
    PrivilegeRoleVo role=new PrivilegeRoleVo();
    role.setAppId("1");
    role.setDeptId("1003");
    role.setDeptName("教学部");
    role.setPrivilegeRoleId("336378e5ea5d81a88093b74db88b2d6d");
    role.setRemark("助理角色");
    //role.setRoleId("10001");
    //role.setRoleLevel(2);
    role.setRolePrivilege("123,1241,451");
    role.setRoleName("助理");
    role.setGroupId("1000");
    role.setGroupName("北京学习中心");
    PrivilegeRoleVo role2=new PrivilegeRoleVo();
    role2.setAppId("1");
    role2.setDeptId("1003");
    role2.setDeptName("教学部");
    role2.setPrivilegeRoleId("336378e5ea5d81a88093b74db88b2d6d");
    role2.setRemark("管理员");
    //role2.setRoleId("10001");
    //role2.setRoleLevel(2);
    role2.setRolePrivilege("123,1241,451");
    role2.setRoleName("管理员");
    role2.setGroupId("1000");
    role2.setGroupName("北京学习中心");
    role2.setStatus(0);
    PrivilegeRoleVo role3=new PrivilegeRoleVo();
    role3.setAppId("1");
    role3.setDeptId("1003");
    role3.setDeptName("教学部");
    role3.setPrivilegeRoleId("fs3ahaa8e5ead8809334db88b23412d");
    role3.setRemark("产品经理角色");
    //role3.setRoleId("10001");
    //role3.setRoleLevel(2);
    role3.setRolePrivilege("123,1241,451");
    role3.setRoleName("产品经理");
    role3.setGroupId("1000");
    role3.setGroupName("北京学习中心");
    role3.setStatus(0);
    PrivilegeUserVo user=new PrivilegeUserVo();
    user.setAppId("10001");
    //user.setAppUsername("zhangsan");
    user.setDeptId("1");
    user.setGroupId("2");
    //user.setPrivilegeFunid("1,2,34");
    //user.setAppUserid("89324522");
    //user.setRoleId("10001,10003,10005");
    List<PrivilegeRoleVo> roleList=new ArrayList<PrivilegeRoleVo>();
    roleList.add(role);
    roleList.add(role2);
    roleList.add(role3);
    //user.setRoleList(roleList);
    String data=JSONObject.fromObject(user).toString();
    System.out.println(data);
    PrivilegeGroupVo group =new PrivilegeGroupVo();
    group.setAppId("10001");
    group.setGroupId("g0001");
   // group.setGroupPrivilege("123,101,111");
    PrivilegeGroupVo group2 =new PrivilegeGroupVo();
    group2.setAppId("10001");
    group2.setGroupId("g0002");
   // group2.setGroupPrivilege("130,101,111");
    PrivilegeGroupVo group3 =new PrivilegeGroupVo();
    group3.setAppId("10001");
    group3.setGroupId("g0002");
   // group3.setGroupPrivilege("140,111,121");
    
    List<PrivilegeGroupVo> groupList=new ArrayList<PrivilegeGroupVo>();
    groupList.add(group);
    groupList.add(group2);
    groupList.add(group3);
    Map c=new HashMap();
    c.put("groupList", groupList);
    String data2=JSONObject.fromObject(c).toString();
    System.out.println(data2);
    Map a=new HashMap();
    a.put("roleList", roleList);
    String data3=JSONObject.fromObject(a).toString();
    System.out.println(data3);
    
   
    List<PrivilegeResourceVo> resourceList=new ArrayList<PrivilegeResourceVo>();
    PrivilegeResourceVo resource =new PrivilegeResourceVo();
    resource.setAppId("10001");
    resource.setResourceId("a001");
    resource.setResourceLevel("1");
    resource.setResourceName("添加");
    resource.setResourceRule("ssees");
    resource.setStatus(0);
    PrivilegeResourceVo resource2 =new PrivilegeResourceVo();
    resource2.setAppId("10001");
    resource2.setResourceId("a002");
    resource2.setResourceLevel("1");
    resource2.setResourceName("修改");
    resource2.setResourceRule("ssees");
    resource2.setStatus(0);
    PrivilegeResourceVo resource3 =new PrivilegeResourceVo();
    resource3.setAppId("10001");
    resource3.setResourceId("a003");
    resource3.setResourceLevel("1");
    resource3.setResourceName("删除");
    resource3.setResourceRule("ssees");
    resource3.setStatus(0);
    PrivilegeResourceVo resource4 =new PrivilegeResourceVo();
    resource4.setAppId("10001");
    resource4.setResourceId("a004");
    resource4.setResourceLevel("1");
    resource4.setResourceName("查找");
    resource4.setResourceRule("ssees");
    resource4.setStatus(0);
    resourceList.add(resource);
    resourceList.add(resource2);
    resourceList.add(resource3);
    resourceList.add(resource4);
    Map b=new HashMap();
    b.put("resourceList", resourceList);
    String data4=JSONObject.fromObject(b).toString();
    System.out.println(data4);
    }
}
