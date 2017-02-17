package cn.com.open.pay.platform.manager.privilege.service;

import java.util.List;

import cn.com.open.pay.platform.manager.privilege.model.OesGroup;

public interface OesGroupService {
   
	List<OesGroup> findAllByPage(int startRow, int pageSize);

	int findAllCount();

	OesGroup findByCode(String groupCode);

	Boolean saveGroup(OesGroup g);
	
	List<OesGroup> findAll();
    
}