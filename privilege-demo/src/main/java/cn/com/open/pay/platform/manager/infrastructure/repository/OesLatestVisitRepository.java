package cn.com.open.pay.platform.manager.infrastructure.repository;


import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import cn.com.open.pay.platform.manager.privilege.model.OesLatestVisit;




/**
 * 
 */
public interface OesLatestVisitRepository extends Repository {

	void saveOesLatestVisit(OesLatestVisit oesLatestVisit);
	List<OesLatestVisit> getOesLastVisitByUserId(@Param("userId") String userId,@Param("startRow") int startRow, @Param("pageSize") int pageSize);
}