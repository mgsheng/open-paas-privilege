package cn.com.open.pay.platform.manager.infrastructure.repository;
/**
 * 
 */

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;


public interface OesUserRepository extends Repository {
	
	List<Map<String, Object>> getUserListByPage(@Param("groupId") String groupId,@Param("start") int start,@Param("limit") int limit,@Param("userName") String userName);
	int getUserCount(@Param("groupId")String groupId,@Param("userName") String userName);
	
}