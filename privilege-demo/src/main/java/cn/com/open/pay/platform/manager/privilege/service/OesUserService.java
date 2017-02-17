package cn.com.open.pay.platform.manager.privilege.service;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;


public interface OesUserService {
	/**
	 * 查询用户
	 * @param groupId
	 * @param start
	 * @param limit
	 * @return
	 */
	List<Map<String, Object>> getUserListByPage( String groupId,int start, int limit,String userName);
	/**
	 * 查询用户数量
	 * @param groupId
	 * @return
	 */
	int getUserCount(String groupId,String userName);
	
}
