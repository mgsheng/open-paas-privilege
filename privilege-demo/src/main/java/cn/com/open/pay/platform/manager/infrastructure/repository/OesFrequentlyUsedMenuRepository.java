package cn.com.open.pay.platform.manager.infrastructure.repository;


import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import cn.com.open.pay.platform.manager.privilege.model.OesFrequentlyUsedMenu;
import cn.com.open.pay.platform.manager.privilege.model.OesLatestVisit;




/**
 * 
 */
public interface OesFrequentlyUsedMenuRepository extends Repository {

	void saveOesFrequentlyUsedMenu(OesFrequentlyUsedMenu oesFrequentlyUsedMenu );
	OesFrequentlyUsedMenu getOesFrequentlyUsedMenuByUserId(@Param("userId") String userId);
	void updateOesFrequentlyUsedMenuByUserId(OesFrequentlyUsedMenu oesFrequentlyUsedMenu);
}