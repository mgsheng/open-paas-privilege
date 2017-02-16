package cn.com.open.pay.platform.manager.infrastructure.repository;
/**
 * 
 */

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;


public interface OesUserRepository extends Repository {
	
	List<Map<String, Object>> getUserListByPage(@Param("groupId") String groupId,@Param("start") int start,@Param("limit") int limit);
	int getUserCountByGroupId(String groupId);
	
}