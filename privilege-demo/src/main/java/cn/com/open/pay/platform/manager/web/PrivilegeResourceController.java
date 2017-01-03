package cn.com.open.pay.platform.manager.web;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.com.open.pay.platform.manager.log.service.PrivilegeLogService;
import cn.com.open.pay.platform.manager.login.model.User;
import cn.com.open.pay.platform.manager.privilege.model.PrivilegeModule;
import cn.com.open.pay.platform.manager.privilege.model.PrivilegeResource;
import cn.com.open.pay.platform.manager.privilege.service.PrivilegeModuleService;
import cn.com.open.pay.platform.manager.privilege.service.PrivilegeResourceService;
import cn.com.open.pay.platform.manager.tools.BaseControllerUtil;
import cn.com.open.pay.platform.manager.tools.WebUtils;

/**
 * 资源管理
 * @author admin
 *
 */
@Controller
@RequestMapping("/resource/")
public class PrivilegeResourceController extends BaseControllerUtil {
	@Autowired
	private PrivilegeResourceService privilegeResourceService;
	
	
	@Autowired
	private PrivilegeLogService privilegeLogService;
	@Autowired
	private PrivilegeModuleService privilegeModuleService;
	
	 /**
     * 跳转资源管理页面
     * @param request
     * @param model
     * @param bool
     * @return
     */
    @RequestMapping(value = "index")
	public String index(HttpServletRequest request,HttpServletResponse response) {
    	return "privilege/resource/index";
    }
    /**
     * 添加资源
     * @param request
     * @param model
     * @param bool
     * @return
     * @throws UnsupportedEncodingException 
     */
    @RequestMapping(value = "add")
	public void add(HttpServletRequest request,HttpServletResponse response,String code,String status) throws UnsupportedEncodingException {
    	PrivilegeResource  privilegeResource=null;
    	Map<String,Object> map = new HashMap<String, Object>();
    	String name=new String(request.getParameter("name").getBytes("iso-8859-1"),"utf-8");
    	String id=request.getParameter("id");
    	//添加日志
		PrivilegeModule privilegeModule = privilegeModuleService.getModuleById(55);
		PrivilegeModule privilegeModule1 = privilegeModuleService.getModuleById(privilegeModule.getParentId());
		String  towLevels = privilegeModule.getName();
		String  oneLevels= privilegeModule1.getName();
		User user1 = (User)request.getSession().getAttribute("user");
		String operator = user1.getUsername(); //操作人
		String operatorId = user1.getId()+""; //操作人Id
		
    	if(nullEmptyBlankJudge(id)){
    		PrivilegeResource privilegeResource1 = privilegeResourceService.findByCode("add");
    		List <PrivilegeResource> list= privilegeResourceService.findByName(name);
        	if(list!=null&& list.size()>0){
        		map.put("returnMsg", "0");
        	}else{
        		privilegeResource=new PrivilegeResource();
        		privilegeResource.setName(name);
        		privilegeResource.setCode(code);
        		privilegeResource.setCreateTime(new Date());
        		if(nullEmptyBlankJudge(status)){
        			status="0";
        		}
        		privilegeResource.setStatus(Integer.parseInt(status));
        		privilegeResourceService.savePrivilegeResource(privilegeResource);
        		privilegeLogService.addPrivilegeLog(operator,privilegeResource1.getName(),oneLevels,towLevels,privilegeResource1.getId()+"",operator+"添加'"+name+"'资源",operatorId);
        		map.put("returnMsg", "1");
        	}
    	}else{
    		privilegeResource= privilegeResourceService.findById(Integer.parseInt(id));
    		PrivilegeResource privilegeResource2 = privilegeResourceService.findByCode("update");
    		privilegeResource.setName(name);
    		privilegeResource.setCode(code);
    		if(nullEmptyBlankJudge(status)){
    			status="0";
    		}
    		privilegeResource.setStatus(Integer.parseInt(status));
    		privilegeResourceService.updatePrivilegeResource(privilegeResource);
    		privilegeLogService.addPrivilegeLog(operator,privilegeResource2.getName(),oneLevels,towLevels,privilegeResource2.getId()+"",operator+"修改'"+name+"'资源",operatorId);
    		map.put("returnMsg", "2");
    		
    	}
    	
    	WebUtils.writeErrorJson(response, map);
    }
    /**
     * 编辑资源
     * @param request
     * @param model
     * @param bool
     * @return
     */
    @RequestMapping(value = "edit")
	public void edit(HttpServletRequest request,HttpServletResponse response,String id,String name,String code,String status) {
    	PrivilegeResource  privilegeResource=null;
    	Map<String,Object> map = new HashMap<String, Object>();
    	List <PrivilegeResource> list= privilegeResourceService.findByName(name);
    	if(list!=null&& list.size()>0){
    		privilegeResource=list.get(0);
    		privilegeResource.setName(name);
    		privilegeResource.setCode(code);
    		if(nullEmptyBlankJudge(status)){
    			status="0";
    		}
    		privilegeResource.setStatus(Integer.parseInt(status));
    		privilegeResourceService.updatePrivilegeResource(privilegeResource);
    		map.put("returnMsg", "1");//修改成功
    	}else{
    		map.put("returnMsg", "0");//不存在
    	}
    	WebUtils.writeErrorJson(response, map);
    }
    /**
     * 删除资源
     * @param request
     * @param model
     * @param bool
     * @return
     */
    @RequestMapping(value = "delete")
	public void delete(HttpServletRequest request,HttpServletResponse response,String id) {
    	Map<String,Object> map = new HashMap<String, Object>();
    	//添加日志
		PrivilegeModule privilegeModule = privilegeModuleService.getModuleById(55);
		PrivilegeModule privilegeModule1 = privilegeModuleService.getModuleById(privilegeModule.getParentId());
		String  towLevels = privilegeModule.getName();
		String  oneLevels = privilegeModule1.getName();
		User user1 = (User)request.getSession().getAttribute("user");
		String operator = user1.getUsername(); //操作人
		String operatorId = user1.getId()+""; //操作人Id
		PrivilegeResource privilegeResource = privilegeResourceService.findByCode("delete");
    	try {
    		if(nullEmptyBlankJudge(id)){
       		 map.put("returnMsg", "0");//不存在	
       		}else{
       			privilegeResourceService.deletePrivilegeResource(Integer.parseInt(id));
       			privilegeLogService.addPrivilegeLog(operator,privilegeResource.getName(),oneLevels,towLevels,privilegeResource.getId()+"",operator+"添加'"+id+"'资源",operatorId);
           		map.put("returnMsg", "1");//修改成功	
       		}
			} catch (Exception e) {
				 map.put("returnMsg", "0");//不存在	
			}
    	
    		
    	WebUtils.writeErrorJson(response, map);
    }
    /**
     * 查询资源
     * @param request
     * @param model
     * @param bool
     * @return
     * @throws UnsupportedEncodingException 
     */
    @RequestMapping(value = "findModuel")
	public void findResource(HttpServletRequest request,HttpServletResponse response) throws UnsupportedEncodingException {
    	String name=new String(request.getParameter("name").getBytes("iso-8859-1"),"utf-8");
    	//当前第几页
		String page=request.getParameter("page");
		//每页显示的记录数
	    String rows=request.getParameter("rows"); 
		//当前页  
		int currentPage = Integer.parseInt((page == null || page == "0") ? "1":page);  
		//每页显示条数  
		int pageSize = Integer.parseInt((rows == null || rows == "0") ? "10":rows);  
		//每页的开始记录  第一页为1  第二页为number +1   
	    int startRow = (currentPage-1)*pageSize;
    	PrivilegeResource privilegeResource=new PrivilegeResource();
    	privilegeResource.setName(name);
    	privilegeResource.setPageSize(pageSize);
    	privilegeResource.setStartRow(startRow);
    	List <PrivilegeResource> list= privilegeResourceService.getPageListByName(privilegeResource);	
    	int queryCount = privilegeResourceService.findQueryCount(name);
    	 DateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss"); 
		 for(int i=0;i<list.size();i++){
			 PrivilegeResource privilegeResourcenew = list.get(i);
			 Date createTime = privilegeResourcenew.getCreateTime();
			 privilegeResourcenew.setFoundDate(df.format(createTime));//交易时间
			 int status = privilegeResourcenew.getStatus();
				if(status==1){
					privilegeResourcenew.setStatusName("启用");
				}else{
					privilegeResourcenew.setStatusName("停用");
				}
		 }
    	 JSONArray jsonArr = JSONArray.fromObject(list);
		 JSONObject jsonObjArr = new JSONObject();  
		 jsonObjArr.put("total", queryCount);
		 jsonObjArr.put("rows", jsonArr);
	     WebUtils.writeJson(response,jsonObjArr);
    }


}
