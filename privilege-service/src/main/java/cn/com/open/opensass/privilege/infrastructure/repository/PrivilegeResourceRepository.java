package cn.com.open.opensass.privilege.infrastructure.repository;

import cn.com.open.opensass.privilege.model.PrivilegeResource;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by jh on 2016/12/13.
 */
public interface PrivilegeResourceRepository extends Repository {
    List<PrivilegeResource> findByUidAppIdAndUserId(String appId, String appUserId);
}
