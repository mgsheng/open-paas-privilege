package cn.com.open.pay.platform.manager.infrastructure.repository;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import cn.com.open.pay.platform.manager.privilege.model.OesGroup;



/**
 * 
 */
public interface OesGroupRepository extends Repository {

	List<OesGroup> findAllByPage(@Param("startRow") int startRow, @Param("pageSize") int pageSize);
	
	int findAllCount();

	OesGroup findByCode(String groupCode);

	void saveGroup(OesGroup g);
	
	List<OesGroup> findAll();

	List<OesGroup> findByPage(@Param("groupName")String groupName, @Param("groupCode")String groupCode, @Param("startRow")int startRow,
			@Param("pageSize")int pageSize);

	int findCount(@Param("groupName")String groupName, @Param("groupCode")String groupCode);
	
	OesGroup findByTypeNameAndCode(@Param("typeName")String typeName, @Param("groupCode")String groupCode);
	
	List<OesGroup> findByTypeName(@Param("typeName")String typeName);
}