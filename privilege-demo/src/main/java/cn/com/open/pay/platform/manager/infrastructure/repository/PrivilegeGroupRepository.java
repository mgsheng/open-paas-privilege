package cn.com.open.pay.platform.manager.infrastructure.repository;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import cn.com.open.pay.platform.manager.privilege.model.PrivilegeGroup;



/**
 * 
 */
public interface PrivilegeGroupRepository extends Repository {

	
	void savePrivilegeGroup(PrivilegeGroup privilegeGroup);
	PrivilegeGroup findByGroupId(@Param("groupId")String groupId,@Param("appId")String appId);
	void updatePrivilegeGroup(PrivilegeGroup privilegeGroup);
	List<PrivilegeGroup> findGroupPage(@Param("groupId")String groupId,@Param("appId")String appId,@Param("startRow")int startRow,@Param("pageSize")int pageSize);
}