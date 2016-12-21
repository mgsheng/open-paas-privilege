package cn.com.open.test;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import cn.com.open.opensass.privilege.infrastructure.repository.PrivilegeGroupResourceRepository;
import cn.com.open.opensass.privilege.infrastructure.repository.PrivilegeResourceRepository;
import cn.com.open.opensass.privilege.model.PrivilegeGroupResource;
import cn.com.open.opensass.privilege.redis.impl.RedisClientTemplate;
import scala.tools.nsc.doc.model.PrivateInTemplate;

public class GroupTest {
	@Autowired
	private PrivilegeResourceRepository privilegeResourceRepository;
	@Autowired
	private PrivilegeGroupResourceRepository privilegeGroupResourceRepository;
	@Autowired
	private RedisClientTemplate redisClientTemplate;
	@Value("${PRIVILEGESERVICE_GROUPCACHE_APPID_GROUPID}")
	private String PRIVILEGESERVICE_GROUPCACHE_APPID_GROUPID;
	private String groupId;
	private String resourceId;
	private String appId;
	@Test
	public void findGroupbyId(){
		/*参数:组ID,APPID*/
		PrivilegeGroupResource groupResource = privilegeGroupResourceRepository.findByGroupIdAndResourceId(groupId, resourceId);
		//privilegeResourceRepository.
	}
}
