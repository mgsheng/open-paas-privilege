package cn.com.open.opensass.privilege.model;

import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONObject;

public class TestPrivilege {
	public static void main(String[] args) {
    PrivilegeRole role=new PrivilegeRole();
    role.setAppId("1");
    role.setDeptId("1003");
    role.setDeptName("教学部");
    role.setPrivilegeRoleId("336378e5ea5d81a88093b74db88b2d6d");
    role.setRemark("助理角色");
    role.setParentRoleId("10001");
    role.setRoleLevel(2);
    //role.setRolePrivilege("123,1241,451");
    role.setRoleName("助理");
    PrivilegeRole role2=new PrivilegeRole();
    role2.setAppId("1");
    role2.setDeptId("1003");
    role2.setDeptName("教学部");
    role2.setPrivilegeRoleId("336378e5ea5d81a88093b74db88b2d6d");
    role2.setRemark("管理员");
    role2.setParentRoleId("10001");
    role2.setRoleLevel(2);
    //role2.setRolePrivilege("123,1241,451");
    role2.setRoleName("管理员");
    
    PrivilegeRole role3=new PrivilegeRole();
    role3.setAppId("1");
    role3.setDeptId("1003");
    role3.setDeptName("教学部");
    role3.setPrivilegeRoleId("fs3ahaa8e5ead8809334db88b23412d");
    role3.setRemark("产品经理角色");
    role3.setParentRoleId("10001");
    role3.setRoleLevel(2);
    //role3.setRolePrivilege("123,1241,451");
    role3.setRoleName("产品经理");
    
    PrivilegeUser user=new PrivilegeUser();
    user.setAppId("10001");
    user.setAppUsername("zhangsan");
    user.setDeptId("1");
    user.setGroupId("2");
    //user.setPrivilegeFunid("1,2,34");
    user.setAppUserid("89324522");
    //user.setId("10012");
    //user.setRoleId("10001,10003,10005");
    List<PrivilegeRole> roleList=new ArrayList<PrivilegeRole>();
    roleList.add(role);
    roleList.add(role2);
    roleList.add(role3);
    //user.setRoleList(roleList);
    String data=JSONObject.fromObject(user).toString();
    System.out.println(data);
    }
}
