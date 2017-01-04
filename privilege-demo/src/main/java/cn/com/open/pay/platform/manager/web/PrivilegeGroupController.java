package cn.com.open.pay.platform.manager.web;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.com.open.pay.platform.manager.privilege.service.PrivilegeGroupService;


/**
 * 组织机构管理
 * @author admin
 *
 */
@Controller
@RequestMapping("/resource/")
public class PrivilegeGroupController {
	@Autowired
	private PrivilegeGroupService privilegeGroupService;
	
	@Value("#{properties['privilege-group-add-uri']}")
	private String privilegeGroupAddUrl;

	@Value("#{properties['privilege-group-modify-uri']}")
	private String privilegeGroupModifyUrl;

	@Value("#{properties['privilege-group-del-uri']}")
	private String privilegeGroupDelUrl;

	@Value("#{properties['privilege-group-query-uri']}")
	private String privilegeGroupQueryUrl;
	
    /**
     * 组织机构权限初始创建
     * @return Json
     */
    @RequestMapping("addGroup")
    public void addGroup(HttpServletRequest request,HttpServletResponse response) {
    	
    }
    /**
     * 权限资源修改
     * @return Json
     */
    @RequestMapping("modifyGroup")
    public void modifyGroup(HttpServletRequest request,HttpServletResponse response) {
    	
    }
    /**
     * 权限资源删除
     * @return Json
     */
    @RequestMapping("delGroup")
    public void delGroup(HttpServletRequest request,HttpServletResponse response){
    	
    }
    /**
     * 权限资源查询
     * @return Json
     */
    @RequestMapping("index")
    public void groupIndex(HttpServletRequest request,HttpServletResponse response) {
    	
    }
}
