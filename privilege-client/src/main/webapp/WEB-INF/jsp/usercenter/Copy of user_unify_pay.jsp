<%--
 * 
 * @author Shengzhao Li
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE HTML>
<html>
<head>
    <title>user-unify-pay(interface)</title>
    <script src="${contextPath}/js/jquery-1.7.1.min.js"></script>
</head>

<body>
<a href="${contextPath}/">_Home</a>


<div class="panel panel-default">
    <div class="panel-body">
        <div ng-controller="AuthorizationCodeCtrl" class="col-md-10">

            <form action="unifyPay" method="post" class="form-horizontal">
            <%-- <form action="${userCenterRegUri}" method="post" class="form-horizontal"> --%>
                <input type="hidden" name="unifyPayUri" id="unifyPayUri" value="${unifyPayUri}"/>
                <a href="javascript:void(0);" ng-click="showParams()">显示请求参数</a>

                <div ng-show="visible">
                    <div class="form-group">
                        <label class="col-sm-2 control-label">outTradeNo</label>

                        <div class="col-sm-10">
                            <input type="text" name="outTradeNo" id="outTradeNo"
                                   class="form-control" ng-model="outTradeNo"/>

                            <p class="help-block">业务方唯一订单号（必填）</p>
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="col-sm-2 control-label">username</label>

                        <div class="col-sm-10">
                            <input type="text" name="username" id="username"
                                   class="form-control" ng-model="username"/>
                           <p class="help-block">用户名（接入用户中心时可以传递）</p>
                        </div>
                    </div>
                                        
                    <div class="form-group">
                        <label class="col-sm-2 control-label">userId</label>

                        <div class="col-sm-10">
                            <input type="text" name="userId" id="userId"
                                   class="form-control" ng-model="userId"/>
                                    <p class="help-block">业务方用户Id（必传）</p>
                        </div>
                        
                    </div>
                    
                                     
                                        
                    <div class="form-group">
                        <label class="col-sm-2 control-label">merchantId</label>

                        <div class="col-sm-10">
                            <input type="text" name="merchantId" id="merchantId" class="form-control"
                                   size="50" ng-model="merchantId"/>

                            <p class="help-block">商户号（必传）接入时会首先确认,10001-测试商户1</p>
                        </div>
                    </div>

					<div class="form-group">  
                        <label class="col-sm-2 control-label">appId</label>

                        <div class="col-sm-10">
                            <input type="text" name="appId" size="50" id="appId" class="form-control"
                                   ng-model="appId"/>

                            <p class="help-block">应用Id,23-测试应用Id</p>
                        </div>
                    </div>
                    
                    <div class="form-group">
                        <label class="col-sm-2 control-label">goodsId</label>

                        <div class="col-sm-10">
                            <input type="text" name="goodsId" id="goodsId" size="50" class="form-control"
                                   ng-model="goodsId"/>

                            <p class="help-block">商品Id</p>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-2 control-label">goodsName</label>

                        <div class="col-sm-10">
                            <input type="text" name="goodsName" id="goodsName"size="50" class="form-control"
                                   ng-model="goodsName"/>
                                    <p class="help-block">商品名称（必传）</p>
                        </div> 
                        
                    </div>
                    
                    <div class="form-group">
                        <label class="col-sm-2 control-label">goodsDesc</label>

                        <div class="col-sm-10">
                            <input type="text" name="goodsDesc" id="goodsDesc" size="50" class="form-control"
                                   ng-model="goodsDesc"/>

                            <p class="help-block">商品描述</p>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-2 control-label">goodsTag</label>

                        <div class="col-sm-10">
                            <input type="text" name="goodsTag" id="goodsTag" size="50" class="form-control"
                                   ng-model="goodsTag"/>

                            <p class="help-block">商品标记（商品优惠、打折等）</p>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-2 control-label">showUrl</label>

                        <div class="col-sm-10">
                            <input type="text" name="showUrl" id="showUrl" size="50" class="form-control"
                                   ng-model="showUrl"/>

                            <p class="help-block">商品展示网址</p>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-2 control-label">buyerRealName</label>

                        <div class="col-sm-10">
                            <input type="text" name="buyerRealName" id="buyerRealName" size="50" class="form-control"
                                   ng-model="buyerRealName"/>

                            <p class="help-block">买家真实姓名</p>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-2 control-label">buyerCertNo</label>

                        <div class="col-sm-10">
                            <input type="text" name="buyerCertNo" id="buyerCertNo" size="50" class="form-control"
                                   ng-model="buyerCertNo"/>

                            <p class="help-block">买家证件号码</p>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-2 control-label">inputCharset</label>

                        <div class="col-sm-10">
                            <input type="text" name="inputCharset" id="inputCharset" size="50" class="form-control" ng-model="inputCharset"/>

                            <p class="help-block">字符集，默认为（UTF-8）</p>
                        </div>
                    </div>
                      <div class="form-group">
                        <label class="col-sm-2 control-label">paymentOutTime</label>

                        <div class="col-sm-10">
                            <input type="text" name="paymentOutTime" id="paymentOutTime" size="50" class="form-control"
                                   ng-model="paymentOutTime"/>

                            <p class="help-block">支付超时时间（超过支付时间，该笔交易自动关闭）取值范围：1m～15d</p>
                        </div>
                    </div> 
                       <div class="form-group">
                        <label class="col-sm-2 control-label">paymentType</label>

                        <div class="col-sm-10">
                           <select name="paymentType" id="paymentType" class="form-control" ng-mode="paymentType">
                                <option value=""></option>
								<option value="ALI_PAY">支付宝-即时到账</option>
								<option value="ALI_FAF">支付宝-当面付</option>
								
								<option value="ALI_ICBCBTB">支付宝-工行(企业)</option>
								<option value="ALI_ABCBTB">支付宝-农行(企业)</option>
								<option value="ALI_CCBBTB">支付宝-建行(企业)</option>
								<option value="ALI_SPDBB2B">支付宝-上海浦东银行(企业)</option>
								<option value="ALI_BOCB2C">支付宝-中国银行</option>
								<option value="ALI_ICBCB2C">支付宝-中国工商银行</option>
								<option value="ALI_CMB">支付宝-招商银行</option>
								<option value="ALI_CCB">支付宝-建设银行</option>
								<option value="ALI_ABC">支付宝-农业银行</option>
								<option value="ALI_SPDB">支付宝-上海浦东发展银行</option>
								<option value="ALI_CIB">支付宝-兴业银行</option>
								<option value="ALI_GDB">支付宝-广东发展银行</option>
								<option value="ALI_SDB">支付宝-深圳发展银行</option>
								<option value="ALI_CMBC">支付宝-中国民生银行</option>
								<option value="ALI_COMM">支付宝-交通银行</option>
								<option value="ALI_CITIC">支付宝-中信银行</option>
								<option value="ALI_HZCBB2C">支付宝-杭州银行</option>
								<option value="ALI_CEBBANK">支付宝-中国光大银行</option>
								<option value="ALI_SHBANK">支付宝-上海银行</option>
								<option value="ALI_NBBANK">支付宝-宁波银行</option>
								<option value="ALI_SPABANK">支付宝-平安银行</option>
								<option value="ALI_BJRCB">支付宝-北京农村商业银行</option>
								<option value="ALI_FDB">支付宝-富滇银行</option>
								<option value="ALI_POSTGC">支付宝-中国邮政储蓄银行</option>
								<option value="ALI_abc1003">支付宝-visa</option>
								<option value="ALI_abc1004">支付宝-master</option>
								<option value="ALI_CMB-DEBIT">支付宝-招商银行(借)</option>
								<option value="ALI_CCB-DEBIT">支付宝-中国建设银行(借)</option>
								<option value="ALI_ICBC-DEBIT">支付宝-中国工商银行(借) </option>
								<option value="ALI_COMM-DEBIT">支付宝-交通银行(借)</option>
								<option value="ALI_GDB-DEBIT">支付宝-广发银行(借)</option>
								<option value="ALI_BOC-DEBIT">支付宝-中国银行(借)</option>
								<option value="ALI_CEB-DEBIT">支付宝-中国光大银行(借)</option>
								<option value="ALI_SPDB-DEBIT">支付宝-上海浦东发展银行(借)</option>
								<option value="ALI_PSBC-DEBIT">支付宝-中国邮政储蓄银行(借)</option>
								<option value="ALI_BJBANK">支付宝-北京银行(借)</option>
								<option value="ALI_SHRCB">支付宝-上海农商银行(借)</option>
								<option value="ALI_WZCBB2C-DEBIT">支付宝-温州银行(借)</option>
								<option value="ALI_COMM">支付宝-交通银行(借)</option>
								<option value="ALI_CMBC">支付宝-中国民生银行(借)</option>
								<option value="ALI_BJRCB">支付宝-北京农村商业银行(借) </option>
								<option value="ALI_SPA-DEBIT">支付宝-平安银行(借)</option>
								<option value="ALI_CITIC-DEBIT">支付宝-中信银行(借)</option>
								<option value="WEIXIN">微信-扫码支付</option>
								<option value="TCL_ALIPAY">TCL-支付宝</option>
								<option value="TCL_WEIXIN">TCL-微信</option>
								<option value="TCL_UPOP">TCL-银联</option>
								<option value="TCL_CMB">TCL-招行</option>
								<option value="TCL_ICBC">TCL-工行</option>
								<option value="TCL_CCB">TCL-建行</option>
								<option value="TCL_ABC">TCL-农行</option>
								<option value="TCL_BOC">TCL-中国银行</option>
								<option value="TCL_PSBC">TCL-邮政</option>
								<option value="TCL_CGB">TCL-广发</option>
								<option value="TCL_SPDB">TCL-浦发</option>
								<option value="TCL_CEB">TCL-光大</option>
								<option value="TCL_PAB">TCL-平安</option>
								<option value="TCL_ECITIC">TCL-中信</option>
								<option value="TCL_BOS">TCL-上海</option>
								<option value="TCL_SDB">TCL-深圳</option>
								<option value="TCL_BOB">TCL-北京</option>
								<option value="TCL_HXB">TCL-华夏</option>
								<option value="TCL_CMBC">TCL-民生</option>
								<option value="TCL_CIB">TCL-兴业</option>
								<option value="TCL_M_ICBC">TCL-工行企业</option>
								<option value="TCL_M_CMB">TCL-招商企业</option>
								<option value="TCL_M_ABC">TCL-农行企业</option>
								<option value="TCL_M_CCB">TCL-建行企业</option>
								<option value="TCL_M_CEB">TCL-光大企业</option>
								<option value="TCL_M_BOC">TCL-中行企业</option>
								<option value="TCL_M_SPDB">TCL-浦发企业</option>
								<option value="TCL_M_BCOM">TCL-交行企业</option>
								<option value="TCL_M_PAB">TCL-平安企业</option>
							</select>
                            <p class="help-block">支付方式</p>
                        </div>
                    </div> 
                    <div class="form-group">
                        <label class="col-sm-2 control-label">paymentChannel</label>

                        <div class="col-sm-10">
                            <!-- <input type="text" name="paymentChannel" id="paymentChannel" size="50" class="form-control"
                                   ng-model="paymentChannel"/> -->
							<select name="paymentChannel" id="paymentChannel" class="form-control" ng-mode="paymentChannel">
							     <option value=""></option>
								<option value="10001">支付宝</option>
								<option value="10002">微信</option>
								<option value="10004">TCL</option>
							</select>
                            <p class="help-block">支付渠道:10001-支付宝渠道   10002-微信渠道</p>
                        </div>
                    </div> 
                    <div class="form-group">
                        <label class="col-sm-2 control-label">businessType</label>

                        <div class="col-sm-10">
                            <!-- <input type="text" name="paymentChannel" id="paymentChannel" size="50" class="form-control"
                                   ng-model="paymentChannel"/> -->
							<select name="businessType" id="businessType" class="form-control" ng-mode="businessType">
								<option value="1">直消</option>
								<option value="2">充值</option>
							</select>
                            <p class="help-block">业务类型（1：直消2:充值）</p>
                        </div>
                    </div> 
                    
                    <div class="form-group">
                        <label class="col-sm-2 control-label">totalFee</label>

                        <div class="col-sm-10">
                            <input type="text" name="totalFee" id="totalFee" size="50" class="form-control"
                                   ng-model="totalFee"/>

                            <p class="help-block">订单金额（单位元，精确到分）如：100元=10000分（必传）,1=0.01元</p>
                        </div>
                    </div> 
                    
                    <div class="form-group">
                        <label class="col-sm-2 control-label">feeType</label>

                        <div class="col-sm-10">
                            <input type="text" name="feeType" id="feeType" size="50" class="form-control" ng-model="feeType"/>

                            <p class="help-block">货币类型（CNY）</p>
                        </div>
                    </div> 
                    
                    <div class="form-group">
                        <label class="col-sm-2 control-label">clientIp</label>

                        <div class="col-sm-10">
                            <input type="text" name="clientIp" id="clientIp" size="50" class="form-control"
                                   ng-model="clientIp"/>

                            <p class="help-block">客户端Ip</p>
                        </div>
                    </div>
                    
                    <div class="form-group">
                        <label class="col-sm-2 control-label">parameter</label>

                        <div class="col-sm-10">
                            <input type="text" name="parameter" id="parameter" size="50" class="form-control"
                                   ng-model="parameter"/>

                            <p class="help-block">扩展字段（可以传递自定义参数，支付成功后会回传）</p>
                        </div>
                    </div>
                    
                    <div class="well well-sm">
                         <span class="text-muted">最终发给 pay-service-server的 URL:</span>
                        <br/>

                        <div class="text-primary" id="userUnifyPayUri"></div> 
                    </div>
                </div>
                <br/>
                <br/>
                <button type="submit" class="btn btn-primary">调用即时转账接口</button>
                <button type="button"  class="btn btn-primary" onclick="btnSubmit();">获取接口调用地址</button>
                
            </form>

        </div>
    </div>
</div>
<script>
    var AuthorizationCodeCtrl = ['$scope', function ($scope) {
		$scope.outTradeNo="test20160517";
		$scope.userId="36133476-3827-4188-AE4A-0B9DBFC6AC64";
		$scope.merchantId="10001";
		$scope.appId="10026";
		$scope.goodsId="1";
		$scope.businessType="1";
		$scope.goodsName="testGoodsName";
		$scope.goodsDesc="testGoodsDesc";
		$scope.paymentType="10001";
		$scope.paymentChannel="10001";
		$scope.totalFee="1";
		$scope.inputCharset="utf-8";
		$scope.feeType="CNY";

        $scope.visible = false;

        $scope.showParams = function () {
            $scope.visible = !$scope.visible;
        };
    }];
</script>
<script type="text/javascript">
	function btnSubmit(){
		var outTradeNo=$("#outTradeNo").val();
		var userName=$("#username").val();
		var userId=$("#userId").val();
		var appId=$("#appId").val();
		var merchantId=$("#merchantId").val();
		var goodsId=$("#goodsId").val();
		var goodsName=$("#goodsName").val();
		var goodsTag=$("#goodsTag").val();
		var goodsDesc=$("#goodsDesc").val();
		var showUrl=$("#showUrl").val();	
		var buyerRealName=$("#buyerRealName").val();
		var buyerCertNo=$("#buyerCertNo").val();
		var inputCharset=$("#inputCharset").val();
		var paymentOutTime=$("#paymentOutTime").val();
		var paymentType=$("#paymentType").val();
		var paymentChannel=$("#paymentChannel").val();
		var totalFee=$("#totalFee").val();
		var feeType=$("#feeType").val();
		var clientIp=$("#clientIp").val();
		var parameter=$("#parameter").val();
		var userUnifyPayUri=$("#unifyPayUri").val();
		
		if(outTradeNo==''){
		    alert("请输入outTradeNo业务方唯一订单号");
			return;
		}if(userId==''){
		    alert("请输入userId业务方用户Id");
			return;
		}
		if(merchantId==''){
		    alert("请输入merchantId商户号");
			return;
		}
		if(appId==''){
		    alert("请输入appId公共参数");
			return;
		}
		if(goodsName==''){
		    alert("请输入goodsName商品名称");
			return;
		}
		if(totalFee==''){
		    alert("请输入totalFee订单金额（输入整数）");
			return;
		}
		$.post("${contextPath}/getSignature",
			{
				appId:appId
			},
			function(data){
				if(data.flag){
				    var signature=data.signature;
				    var timestamp=data.timestamp;
				    var signatureNonce=data.signatureNonce;
				    var regUri=userUnifyPayUri+"?"+"outTradeNo="+outTradeNo+"&userName="+userName+"&userId="+userId+"&appId="+appId+"&merchantId="+merchantId+"&goodsId="+goodsId+"&goodsName="+goodsName+"&goodsDesc="+goodsDesc+"&goodsTag="+goodsTag+"&showUrl="+showUrl+"&buyerRealName="+buyerRealName+"&buyerCertNo="+buyerCertNo+"&inputCharset="+inputCharset+"&paymentOutTime="+paymentOutTime+"&paymentType="+paymentType+"&paymentChannel="+paymentChannel+"&totalFee="+totalFee+"&feeType="+feeType+"&parameter="+parameter+"&clientIp="+clientIp+"&signature="+signature+"&amptimestamp="+timestamp+"&signatureNonce="+signatureNonce;
					$("#userUnifyPayUri").html(regUri);
				}else{
				    jQuery("#userUnifyPayUri").html('无效数据，请重新申请');
				}
			}
 		);
	}
</script>

</body>
</html>