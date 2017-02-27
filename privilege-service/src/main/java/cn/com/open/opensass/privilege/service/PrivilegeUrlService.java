package cn.com.open.opensass.privilege.service;

import cn.com.open.opensass.privilege.dao.PrivilegeUrl;
import cn.com.open.opensass.privilege.model.PrivilegeUser;
import cn.com.open.opensass.privilege.vo.PrivilegeAjaxMessage;

/**
 * URL REDIS 操作类
 */
public interface PrivilegeUrlService {
	/**
	 * 获取redis url 数据
	 * 
	 * @param appId应用id
	 * @param appUserId 对应应用id的用户id
	 * @return 相应的提示信息
	 */
	PrivilegeAjaxMessage getRedisUrl(String appId, String appUserId);

	/**
	 * 获取redis url 数据
	 * 
	 * @param appId应用id
	 * @param appUserId对应应用id的用户id
	 * @return 相应的提示信息
	 */
	PrivilegeAjaxMessage updateRedisUrl(String appId, String appUserId);

	/**
	 * 获取redis url 数据
	 * 
	 * @param appId 应用id
	 * @param appUserId 对应应用id的用户id
	 * @return 相应的提示信息
	 */
	PrivilegeAjaxMessage deleteRedisUrl(String appId, String appUserId);

	/**
	 *
	 * @param appId应用id
	 * @param appUserId 对应应用id的用户id
	 * @param url传入的网址
	 * @return 相应的提示信息
	 */
	PrivilegeAjaxMessage existRedisUrl(String appId, String appUserId, String url);

	/**
	 * 获取redis url 数据
	 * 
	 * @param appId应用id
	 * @param appUserId 对应应用id的用户id
	 * @return 相应的提示信息
	 */

	PrivilegeAjaxMessage existRedisKey(String appId, String appUserId);

	/**
	 * 获取相应的输出信息
	 * 
	 * @param appId应用id
	 * @param appUserId对应应用id的用户id
	 * @param privilegeUser传入类，获取相关信息
	 * @return
	 */
	PrivilegeUrl getPrivilegeUrl(String appId, String appUserId, PrivilegeUser privilegeUser);
	
	PrivilegeUrl getAllPrivilegeUrl(String appId);
	/**
	 * 获取组织机构 url 数据
	 * 
	 * @param appId应用id
	 * @param groupId 对应组织的id
	 * @return 相应的提示信息
	 */
	PrivilegeUrl getGroupPrivilegeUrl(String appId, String groupId);

}
