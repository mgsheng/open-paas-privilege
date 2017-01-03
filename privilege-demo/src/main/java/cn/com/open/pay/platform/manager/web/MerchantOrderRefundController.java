package cn.com.open.pay.platform.manager.web;

import java.io.UnsupportedEncodingException;

import org.springframework.beans.factory.annotation.Autowired;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.com.open.pay.platform.manager.tools.SysUtil;
import cn.com.open.pay.platform.manager.department.model.DictTradePayment;
import cn.com.open.pay.platform.manager.department.model.MerchantInfo;
import cn.com.open.pay.platform.manager.department.service.DictTradePaymentService;
import cn.com.open.pay.platform.manager.department.service.MerchantInfoService;
import cn.com.open.pay.platform.manager.log.service.PrivilegeLogService;
import cn.com.open.pay.platform.manager.login.model.User;
import cn.com.open.pay.platform.manager.order.model.MerchantOrderInfo;
import cn.com.open.pay.platform.manager.order.model.MerchantOrderOffline;
import cn.com.open.pay.platform.manager.order.model.MerchantOrderRefund;
import cn.com.open.pay.platform.manager.order.service.MerchantOrderInfoService;
import cn.com.open.pay.platform.manager.order.service.MerchantOrderOfflineService;
import cn.com.open.pay.platform.manager.order.service.MerchantOrderRefundService;
import cn.com.open.pay.platform.manager.paychannel.model.ChannelRate;
import cn.com.open.pay.platform.manager.paychannel.model.PayChannelDictionary;
import cn.com.open.pay.platform.manager.paychannel.service.PayChannelDictionaryService;
import cn.com.open.pay.platform.manager.privilege.model.PrivilegeModule;
import cn.com.open.pay.platform.manager.privilege.model.PrivilegeResource;
import cn.com.open.pay.platform.manager.privilege.service.PrivilegeModuleService;
import cn.com.open.pay.platform.manager.privilege.service.PrivilegeResourceService;
import cn.com.open.pay.platform.manager.tools.BaseControllerUtil;
import cn.com.open.pay.platform.manager.tools.OrderDeriveExport;
import cn.com.open.pay.platform.manager.tools.WebUtils;
/**
 * 线下收费管理
 * @author lvjq
 *
 */
@Controller
@RequestMapping("/manage/")
public class MerchantOrderRefundController extends BaseControllerUtil{
	private static final Logger log = LoggerFactory.getLogger(UserLoginController.class);
	@Autowired
	private MerchantOrderRefundService merchantOrderRefundService;
	@Autowired
	private MerchantInfoService merchantInfoService;
	@Autowired
	private MerchantOrderInfoService merchantOrderInfoService;
	@Autowired
	private MerchantOrderOfflineService merchantOrderOfflineService;
	@Autowired
	private PrivilegeLogService privilegeLogService;
	@Autowired
	private PrivilegeModuleService privilegeModuleService;
	@Autowired
	private PrivilegeResourceService privilegeResourceService;
	
	/**
	 * 跳转到退费单据维护页面
	 * @return
	 */
	@RequestMapping(value="refundOrderPages")
	public String refundOrderPages(){
		log.info("---------------refundOrderPages----------------");
		return "usercenter/merchantOrderRefund";
	}
	
	/**
	 * 将从数据库查询的数据封装为json，返回到前端页面
	 * @param request
	 * @param response
	 * @throws UnsupportedEncodingException
	 */
	@RequestMapping("getMerchantOrderRefund")
	public void getMerchantOrderOffline(HttpServletRequest request,HttpServletResponse response)throws UnsupportedEncodingException{
		log.info("---------------getMerchantOrderRefund----------------");
		SimpleDateFormat sdf1=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String merchantOrderId = request.getParameter("merchantOrderId");
		String sourceUserName = request.getParameter("sourceUserName");
		String merchantName = request.getParameter("merchantName");
		String appId = request.getParameter("appId");
		String startDate = request.getParameter("startDate");
		String endDate = request.getParameter("endDate");
		System.out.println("merchantOrderId:"+merchantOrderId+"  sourceUserName:"+sourceUserName+"  merchantName:"+merchantName+"  appId:"+appId+" startDate:"+startDate+" endDate:"+endDate);
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
	    MerchantOrderRefund refund = new MerchantOrderRefund();
	    refund.setPageSize(pageSize);
	    refund.setStartRow(startRow);
	    
	    refund.setMerchantOrderId(merchantOrderId);
	    refund.setSourceUserName(sourceUserName);
	    if(merchantName!=null && merchantName!=""){
	    	refund.setMerchantId(Integer.parseInt(merchantName));
	    }
	    refund.setAppId(appId);
	    if(startDate!=null && startDate!=""){
	    	try {
				refund.setStartDate(sdf1.parse(startDate+" 00:00:00"));
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    }
	    if(endDate!=null && endDate!=""){
	    	try {
				refund.setEndDate(sdf1.parse(endDate+" 23:59:59"));
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    }
	    	    
	    List<MerchantOrderRefund> refunds = merchantOrderRefundService.findRefundAll(refund);
	    int total = merchantOrderRefundService.findRefundAllCount(refund);
	    JSONObject json =  new JSONObject();
	    json.put("total", total);
	    List<Map<String,Object>> maps = new ArrayList<Map<String,Object>>();
	    MerchantInfo merchantInfo=null;
	    if(refunds != null){
	    	Map<String,Object> map = null;
	    	for(MerchantOrderRefund r : refunds){
	    		map = new LinkedHashMap<String,Object>();
	    		map.put("merchantOrderId", r.getMerchantOrderId());  
	    		if(r.getCreateTime()!=null){
	    			map.put("createTime", sdf1.format(r.getCreateTime()));
	    		}
	    		map.put("refundMoney", r.getRefundMoney());
	    		map.put("sourceUID", r.getSourceUid());
	    		map.put("sourceUserName", r.getSourceUserName());
	    		map.put("realName", r.getRealName());
	    		map.put("phone", r.getPhone());
	    		merchantInfo=merchantInfoService.findNameById(r.getMerchantId());
	    		if(merchantInfo!=null){
		    		map.put("merchantId", merchantInfo.getMerchantName());
	    		}
	    		map.put("appId", r.getAppId());
	    		map.put("remark", r.getRemark());
	    		maps.add(map);
	    	}
	    	JSONArray jsonArr = JSONArray.fromObject(maps);
	    	json.put("rows", jsonArr);
	    	WebUtils.writeJson(response, json);
	    }
		return;
	}
	
	/**
	 * 提交添加退费单据
	 * @return 返回到前端json数据
	 * @throws UnsupportedEncodingException 
	 */
	@RequestMapping(value="submitAddOrderRefund")
	public void submitAddOrderRefund(HttpServletRequest request,HttpServletResponse response,Model model) throws UnsupportedEncodingException{
		log.info("-------------------------submitAddOrderRefund         start------------------------------------");
		request.setCharacterEncoding("utf-8");
		String addMerchantOrderId = request.getParameter("addMerchantOrderId");
		Double addRefundMoney = Double.parseDouble(request.getParameter("addRefundMoney"));
		String addSourceUserName = request.getParameter("addSourceUserName");
		String addRealName = request.getParameter("addRealName");
		String addPhone = request.getParameter("addPhone");
		String addMerchantName = request.getParameter("addMerchantName");
		Integer merchantId = Integer.parseInt(addMerchantName);
		String addAppId = request.getParameter("addAppId");
		String addRemark = request.getParameter("addRemark");
		String addSourceUID = request.getParameter("addSourceUID");
		
		System.out.println("-----------addMerchantOrderId : "+addMerchantOrderId+"     addRefundMoney : "+addRefundMoney+"    " +
				"   addSourceUserName : "+addSourceUserName+"      addRealName:"+addRealName+"      addPhone : "+addPhone+
				"   addMerchantName : "+addMerchantName+"   addAppId : "+addAppId+"   addRemark : "+addRemark+"-----------");
		
		JSONObject json =  new JSONObject();
		//封装参数
		MerchantOrderRefund merchantOrderRefund = new MerchantOrderRefund();
		merchantOrderRefund.setMerchantOrderId(addMerchantOrderId);
		merchantOrderRefund.setRefundMoney(addRefundMoney);
		merchantOrderRefund.setSourceUserName(addSourceUserName);
		merchantOrderRefund.setRealName(addRealName);
		merchantOrderRefund.setPhone(addPhone);
		merchantOrderRefund.setMerchantId(merchantId);
		merchantOrderRefund.setAppId(addAppId);
		merchantOrderRefund.setRemark(addRemark);
		merchantOrderRefund.setSourceUid(addSourceUID);
		//result = 1 添加成功  result = 2 该记录(商户订单号)不存在  result = 3退费金额超过收费金额  result = 4商户订单号已退费 result = 0 添加失败 
		int result = -1;
		//先查询该商户订单号是否已经存在
		MerchantOrderOffline orderOffline = merchantOrderOfflineService.findByMerchantOrderId(addMerchantOrderId);
		MerchantOrderInfo orderInfo = merchantOrderInfoService.findByMerchantOrderId(addMerchantOrderId);
		MerchantOrderRefund orderRefund = merchantOrderRefundService.findByMerchantOrderId(addMerchantOrderId);
		if(orderRefund != null){
			result = 4;
		}else if(orderOffline == null && orderInfo == null){
			result = 2;
		}else if((orderOffline != null && orderOffline.getMoney() < addRefundMoney) || (orderInfo != null && orderInfo.getPayAmount() < addRefundMoney)){
			result = 3;
		}else{
			//添加退费单据
			boolean isSuccess = merchantOrderRefundService.addOrderRefund(merchantOrderRefund);
			//添加日志
			PrivilegeModule privilegeModule = privilegeModuleService.getModuleById(82);
			PrivilegeModule privilegeModule1 = privilegeModuleService.getModuleById(privilegeModule.getParentId());
			String towLevels = privilegeModule.getName();
			String  oneLevels = privilegeModule1.getName();
			User user1 = (User)request.getSession().getAttribute("user");
			String operator = user1.getUsername(); //操作人
			String operatorId = user1.getId()+""; //操作人Id
			PrivilegeResource privilegeResource = privilegeResourceService.findByCode("add");
			privilegeLogService.addPrivilegeLog(operator,privilegeResource.getName(),"经营分析","退费单据维护",privilegeResource.getId()+"",operator+"添加了退费单据，商户订单号为"+addMerchantOrderId,operatorId);
			
			if(isSuccess){
				result = 1;
			}else{
				result = 0;
			}
		}
		json.put("result", result);
		WebUtils.writeJson(response, json);
		return ;
	}
	
	/**
	 * 下载为excel
	 * @param request
	 * @param response
	 * @return 
	 * @throws UnsupportedEncodingException
	 */
	@RequestMapping("refundDownloadSubmit")
	public void refundDownloadSubmit(HttpServletRequest request,HttpServletResponse response)throws UnsupportedEncodingException{
		log.info("---------------getMerchantOrderRefundDownloadSubmit----------------");
		SimpleDateFormat sdf1=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String merchantOrderId = request.getParameter("merchantOrderId");
		String sourceUserName = request.getParameter("sourceUserName");
		String merchantName = request.getParameter("merchantName");
		String appId = request.getParameter("appId");
		String startDate = request.getParameter("startDate");
		String endDate = request.getParameter("endDate");
		System.out.println("merchantOrderId:"+merchantOrderId+"  sourceUserName:"+sourceUserName+"  merchantName:"+merchantName+"  appId:"+appId+" startDate:"+startDate+" endDate:"+endDate);
		
	    MerchantOrderRefund refund = new MerchantOrderRefund();
	    
	    refund.setMerchantOrderId(merchantOrderId);
	    refund.setSourceUserName(sourceUserName);
	    if(merchantName!=null && merchantName!=""){
	    	refund.setMerchantId(Integer.parseInt(merchantName));
	    }
	    refund.setAppId(appId);
	    if(startDate!=null && startDate!=""){
	    	try {
				refund.setStartDate(sdf1.parse(startDate+" 00:00:00"));
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    }
	    if(endDate!=null && endDate!=""){
	    	try {
				refund.setEndDate(sdf1.parse(endDate+" 23:59:59"));
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    }
	    
	    List<MerchantOrderRefund> refunds = merchantOrderRefundService.findRefundAllNoPage(refund);
	    
	    DateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss"); 
	    MerchantInfo merchantInfo = null;
	    for(MerchantOrderRefund r : refunds){
			 Date createDate1 = r.getCreateTime();
			 r.setFoundDate(df.format(createDate1));
			 String appId1=r.getAppId();
			 String appName="";
			 if(appId1!=null && appId1!="" && appId1!="0"){
				 if(appId1.equals("1")){
					 appName = "OES学历";
				 }else if(appId1.equals("10026")){
					 appName = "mooc2u";
				 }
			 }
			 r.setAppName(appName);
			 Integer merchantId1=r.getMerchantId();
			 String merchantName1="";
			 if(merchantId1!=null){				 
				merchantInfo=merchantInfoService.findNameById(merchantId1);
	    		if(merchantInfo!=null){
		    		merchantName1=merchantInfo.getMerchantName();
	    		}
	    		r.setMerchantName(merchantName1);
			 }
	    }
	    OrderDeriveExport.exportOrderRefund(response, refunds);
	    //return "usercenter/merchantOrderRefund";
	}
}
