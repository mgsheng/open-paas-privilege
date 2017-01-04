package cn.com.open.pay.platform.manager.privilege.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.com.open.pay.platform.manager.infrastructure.repository.PrivilegeModuleRepository;
import cn.com.open.pay.platform.manager.privilege.model.PrivilegeModule;
import cn.com.open.pay.platform.manager.privilege.model.PrivilegePublic;
import cn.com.open.pay.platform.manager.privilege.model.PrivilegeRoleDetails;
import cn.com.open.pay.platform.manager.privilege.model.TreeNode;
import cn.com.open.pay.platform.manager.privilege.service.PrivilegeModuleService;
@Service("privilegeModuleService")
public class PrivilegeModuleServiceImpl implements PrivilegeModuleService {
    @Autowired
	private PrivilegeModuleRepository privilegeModuleRepository;
	@Override
	public List<PrivilegeModule> findByName(String name) {
		return privilegeModuleRepository.findByName(name);
	}
	@Override
	public void savePrivilegeModule(PrivilegeModule privilegeModule) {
		privilegeModuleRepository.savePrivilegeModule(privilegeModule);
	}
	@Override
	public void updatePrivilegeModule(PrivilegeModule privilegeModule) {
		// TODO Auto-generated method stub
		privilegeModuleRepository.updatePrivilegeModule(privilegeModule);
	}
	@Override
	public void deletePrivilegeModule(Integer id) {
		// TODO Auto-generated method stub
		privilegeModuleRepository.deletePrivilegeModule(id);
	}
	@Override
	public List<TreeNode>  getDepartmentTree() {
		List<PrivilegeModule> modules = privilegeModuleRepository.findAllModules();//用于取出所有的部门对象的list集合
		return convertTreeNodeList(modules);
	}
	private List<TreeNode> convertTreeNodeList(List<PrivilegeModule> modules) {
			List<TreeNode> nodes = null;
			
			if(modules != null){
				nodes = new ArrayList<TreeNode>();
				for(PrivilegeModule department:modules){
					TreeNode node = convertTreeNode(department);
					if(node != null){
						nodes.add(node);
					}
				}
			}
			
			return nodes;
		}
	/**
	* @param departments
	* @return
	*/
	private TreeNode convertTreeNode(PrivilegeModule privilegeModule){
		TreeNode node = null;
		if(privilegeModule != null){
			node = new TreeNode();
			node.setId(String.valueOf(privilegeModule.getId()));//部门ID
			node.setChecked(false);
			node.setText(privilegeModule.getName());//部门名称
			node.setTarget("");
			node.setPid(String.valueOf(privilegeModule.getParentId()));//父级部门ID
			node.setResource(privilegeModule.getResources());
			Map<String,Object> map = new HashMap<String,Object>();
			node.setAttributes(map);
		}
		return node;
	}
	@Override
	public PrivilegeModule getModuleById(Integer id) {
		// TODO Auto-generated method stub
		return privilegeModuleRepository.findModuleById(id);
	}
	@Override
	public List<PrivilegeModule> findModuleByIds(List<Integer> ids) {
		// TODO Auto-generated method stub
		return privilegeModuleRepository.findModuleByIds(ids);
	}
	
	@Override
	public List<TreeNode> getDepartmentTree(
			List<PrivilegeRoleDetails> privilegeRoleDetailslist) {
		List<PrivilegeModule> modules = privilegeModuleRepository.findAllModules();//用于取出所有的部门对象的list集合
		return convertTreeNodeList1(modules,privilegeRoleDetailslist);
	}
	
	private List<TreeNode> convertTreeNodeList1(List<PrivilegeModule> modules,List<PrivilegeRoleDetails> privilegeRoleDetailslist) {
		StringBuffer module=new StringBuffer();
		if(privilegeRoleDetailslist.size()!=0){
			
			for(int i=0;i<privilegeRoleDetailslist.size();i++){
				PrivilegeRoleDetails privilegeRoleDetails = privilegeRoleDetailslist.get(i);
				String resources = privilegeRoleDetails.getResources();
				int moduleId=0;
				if(resources==null){
					moduleId = privilegeRoleDetails.getModuleId();
				    PrivilegeModule privilegeModule = privilegeModuleRepository.findModuleById(moduleId);
				    int parentId = privilegeModule.getParentId();
				    if(parentId!=0){
				    	module.append(moduleId+",");
				    }
				}
					
					
			}
		}
		List<TreeNode> nodes = null;
		
		if(modules != null){
			nodes = new ArrayList<TreeNode>();
			for(PrivilegeModule department:modules){
				TreeNode node = convertTreeNode1(department,module.toString());
				if(node != null){
					nodes.add(node);
				}
			}
		}
		
		return nodes;
	}
	
	/**
	* @param departments
	* @return
	*/
	private TreeNode convertTreeNode1(PrivilegeModule privilegeModule,String module){
		String[] sourceStrArray = null;
		if(module!=""){
			sourceStrArray = module.split(",");
		}
		
		TreeNode node = null;
		if(privilegeModule != null){
			
			node = new TreeNode();
			node.setId(String.valueOf(privilegeModule.getId()));//部门ID
			node.setChecked(false);
			node.setText(privilegeModule.getName());//部门名称
			node.setTarget("");
			node.setPid(String.valueOf(privilegeModule.getParentId()));//父级部门ID
			node.setResource(privilegeModule.getResources());
			node.setIsmodule("0");
			for(int i=0;i<sourceStrArray.length;i++){
				String value = sourceStrArray[i];
				String id = privilegeModule.getId()+"";
				if(value.equals(id)){
					node.setChecked(true);
				}
			}
			Map<String,Object> map = new HashMap<String,Object>();
			node.setAttributes(map);
		}
		return node;
	}
	
	
	@Override
	public List<TreeNode> getDepartmentTree2(
			List<PrivilegePublic> privilegeRoleDetailslist) {
		List<PrivilegeModule> modules = privilegeModuleRepository.findAllModules();//用于取出所有的部门对象的list集合
		return convertTreeNodeList2(modules,privilegeRoleDetailslist);
	}
	
	private List<TreeNode> convertTreeNodeList2(List<PrivilegeModule> modules,List<PrivilegePublic> privilegeRoleDetailslist) {
		StringBuffer module=new StringBuffer();
		if(privilegeRoleDetailslist.size()!=0){
			
			for(int i=0;i<privilegeRoleDetailslist.size();i++){
				PrivilegePublic privilegeRoleDetails = privilegeRoleDetailslist.get(i);
				String resources = privilegeRoleDetails.getResources();
				int moduleId=0;
				if(resources==null){
					moduleId = privilegeRoleDetails.getModuleId();
				    PrivilegeModule privilegeModule = privilegeModuleRepository.findModuleById(moduleId);
				    int parentId = privilegeModule.getParentId();
				    if(parentId!=0){
				    	module.append(moduleId+",");
				    }
				}
					
					
			}
		}
		List<TreeNode> nodes = null;
		
		if(modules != null){
			nodes = new ArrayList<TreeNode>();
			for(PrivilegeModule department:modules){
				TreeNode node = convertTreeNode2(department,module.toString());
				if(node != null){
					nodes.add(node);
				}
			}
		}
		
		return nodes;
	}
	
	/**
	* @param departments
	* @return
	*/
	private TreeNode convertTreeNode2(PrivilegeModule privilegeModule,String module){
		String[] sourceStrArray = null;
		if(module!=""){
			sourceStrArray = module.split(",");
		}
		
		TreeNode node = null;
		if(privilegeModule != null){
			
			node = new TreeNode();
			node.setId(String.valueOf(privilegeModule.getId()));//部门ID
			node.setChecked(false);
			node.setText(privilegeModule.getName());//部门名称
			node.setTarget("");
			node.setPid(String.valueOf(privilegeModule.getParentId()));//父级部门ID
			node.setResource(privilegeModule.getResources());
			node.setIsmodule("0");
			for(int i=0;i<sourceStrArray.length;i++){
				String value = sourceStrArray[i];
				String id = privilegeModule.getId()+"";
				if(value.equals(id)){
					node.setChecked(true);
				}
			}
			Map<String,Object> map = new HashMap<String,Object>();
			node.setAttributes(map);
		}
		return node;
	}
	@Override
	public List<Map<String, Object>> getModuleListByroleId(String roleId) {
				// 顶级菜单集合
				List<Map<String, Object>> pMenus = new ArrayList<Map<String, Object>>();
				// 获取所有的菜单
				List<PrivilegeModule> sysFunctions = privilegeModuleRepository.getModulListByroleId(roleId);
				// 将顶级菜单筛选出，并且设置子菜单
				Map<String, Object> map=null;
				if(sysFunctions != null && sysFunctions.size() > 0){
					for(PrivilegeModule sysFunction:sysFunctions){
						List<Map<String, Object>> mList=null;
						if(sysFunction.getParentId()==0){
							mList=new ArrayList<Map<String, Object>>();
							map=new HashMap<String,Object>();
							map.put("menuid", sysFunction.getId());
							map.put("menuname", sysFunction.getName());
							map.put("icon", sysFunction.getIcon());
							pMenus.add(map);
						}
						for(PrivilegeModule _sysFunction:sysFunctions){
							if(_sysFunction.getParentId()==sysFunction.getId()){
								Map<String, Object> map2=new HashMap<String, Object>();
								map2.put("menuid", _sysFunction.getId());
								map2.put("menuname", _sysFunction.getName());
								map2.put("icon", _sysFunction.getIcon());
								map2.put("url", _sysFunction.getUrl());
								mList.add(map2);
								//sysFunction.getSysFunctions().add(_sysFunction);
								map.put("menus", mList);
							}
						}
					}
				}
				return pMenus;

		
		
		//return privilegeModuleRepository.getModulListByroleId(roleId);
	}
	
    
}