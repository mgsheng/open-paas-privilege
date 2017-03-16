package cn.com.open.pay.platform.manager.privilege.service;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import cn.com.open.pay.platform.manager.privilege.model.OesGroup;

public interface OesGroupService {
   
	List<OesGroup> findAllByPage(int startRow, int pageSize);

	int findAllCount();

	OesGroup findByCode(String groupCode);

	Boolean saveGroup(OesGroup g);
	
	List<OesGroup> findAll();

	List<OesGroup> findByPage(String groupName, String groupCode, int startRow,
			int pageSize);

	int findCount(String groupName, String groupCode);
    
	OesGroup findByTypeNameAndCode(String typeName, String groupCode);
	
	List<OesGroup> findByTypeName(String typeName);
	
}