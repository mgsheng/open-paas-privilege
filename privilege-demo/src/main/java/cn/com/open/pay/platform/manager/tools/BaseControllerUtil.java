package cn.com.open.pay.platform.manager.tools;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;

import cn.com.open.pay.platform.manager.login.model.User;
import cn.com.open.pay.platform.manager.login.service.UserService;


public class BaseControllerUtil {
	
	/**
	 * 
	 * 检验参数是否为空
	 * @param params
	 * @return
	 */
	public boolean paraMandatoryCheck(List<String> params){
        for(String param:params){
            if(nullEmptyBlankJudge(param)){
                return false;
            }
        }
        return true;
    }
	 public static String getUUID(){ 
	        String s = UUID.randomUUID().toString(); 
	        //去掉“-”符号 
	        return s.substring(0,8)+s.substring(9,13)+s.substring(14,18)+s.substring(19,23)+s.substring(24); 
	    } 
	/**
	 * 检验字符串是否为空
	 * @param str
	 * @return
	 */
	 public boolean nullEmptyBlankJudge(String str){
	        return null==str||str.isEmpty()||"".equals(str.trim());
	  }
	  /**
	     * 
	     * 验证密码为 6～20位,字母、数字或者英文符号，最短6位，区分大小写
	     * @param value
	     * @return
	     */
	    public int judgeInputNotNo(String value){
	    	int returnValue=0;
	    	if(value.length()>20||value.length()<6){
	    		returnValue=1;
	    		return returnValue;
	    	}else{
	    	//Pattern p = Pattern.compile("[a-zA-Z][a-zA-Z0-9]{5,20}"); 
	    		Pattern p = Pattern.compile("[0-9A-Za-z_]*");
	    	//Pattern p = Pattern.compile("^[a-zA-Z]/w{5,17}$");
	    	Matcher m = p.matcher(value);
	    	boolean chinaKey = m.matches();
	    	if(chinaKey){
	    		returnValue=0;
	    	} else{
	    		returnValue=1;
	    		return returnValue;
	    	 }
	    	}
	    	return  returnValue;
	    }
	    /**
	     * 
	     * 验证密码为 6～20位
	     * @param value
	     * @return
	     */
	    public int judgePwdNo(String value){
	    	int returnValue=0;
	    	if(value.length()>20||value.length()<6){
	    		returnValue=1;
	    		return returnValue;
	    	}
	    	return  returnValue;
	    }
		/**
		 *验证密码不能为纯数字
		 * @param str
		 * @return
		 */
		public static boolean isNumeric(String str){ 
			Pattern pattern = Pattern.compile("[0-9]*"); 
			Matcher isNum = pattern.matcher(str);
			if(!isNum.matches()){
				return false; 
			} 
			return true; 
		}
		/**
	     * 验证用户名必须为英文、数字、下划线的组合
	     * @param value
	     * @return
	     */
	    public  int  judgeInput(String value){
	    	int returnValue=0;
	    	if(value.length()>50||value.length()<5){
	    		returnValue=1;
	    		return returnValue;
	    	}else{
	    	Pattern p = Pattern.compile("[0-9A-Za-z_]*");
	    	//Pattern p = Pattern.compile("^[a-zA-Z]/w{5,17}$");
	    	Matcher m = p.matcher(value);
	    	boolean chinaKey = m.matches();
	    	if(chinaKey){
	    		returnValue=0;
	    	} else{
	    		if(checkEmail(value)){
	    			returnValue=0;	
	    		}else{
	    			returnValue=2;	
	    		}
	    		
	    		return returnValue;
	    	}
	    	    String check2="^[1][1-9]{1}[0-9]{9}$";
	            //Pattern regex1 = Pattern.compile(check1);
	            Pattern regex2 = Pattern.compile(check2);
	    		if(regex2.matcher(value).matches()){//手机
	    			return 0;
				}else{
	    		Pattern pattern1 = Pattern.compile("[0-9]*"); 
				Matcher isNum = pattern1.matcher(value);
				if(!isNum.matches()){
					 returnValue=0 ; 
				} else{
					returnValue=3; 
					return  returnValue;
				}	
	    	}
	    	
	    	}
	    	return  returnValue;
	    	
	    }
	    /**
	     * 验证邮箱
	     * @param email
	     * @return
	     */
	    public boolean checkEmail(String email){
	     boolean flag = false;
	     try{
	       String check = "^([a-z0-9A-Z]+[-|_|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
	       Pattern regex = Pattern.compile(check);
	       Matcher matcher = regex.matcher(email);
	       flag = matcher.matches();
	      }catch(Exception e){
	       flag = false;
	      }
	     return flag;
	    }
	    
	    /**
	     * 返回成功json
	     * @param response
	     * @param obj 数据集
	     */
	    public void writeSuccessJson(HttpServletResponse response, Map map){
	    	WebUtils.writeJson(response, JSONObject.fromObject(map));
	    }
	    
	    /**
	     * 返回失败json
	     * @param response
	     * @param error_code 错误码
	     */
	    public void writeErrorJson(HttpServletResponse response, Map map){
	    	WebUtils.writeJson(response, JSONObject.fromObject(map));
	    }
	    /**
	     * 向指定URL发送GET方法的请求
	     * 
	     * @param url
	     *            发送请求的URL
	     * @param param
	     *            请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
	     * @return URL 所代表远程资源的响应结果
	     */
	    public static String sendGet(String url, String param) {
	        String result = "";
	        BufferedReader in = null;
	        try {
	            String urlNameString = url + "?" + param;
	            URL realUrl = new URL(urlNameString);
	            // 打开和URL之间的连接
	            URLConnection connection = realUrl.openConnection();
	            // 设置通用的请求属性
	            connection.setRequestProperty("accept", "*/*");
	            connection.setRequestProperty("connection", "Keep-Alive");
	            connection.setRequestProperty("user-agent",
	                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
	            // 建立实际的连接
	            connection.connect();
	            // 获取所有响应头字段
	            Map<String, List<String>> map = connection.getHeaderFields();
	            // 遍历所有的响应头字段
	            for (String key : map.keySet()) {
	                System.out.println(key + "--->" + map.get(key));
	            }
	            // 定义 BufferedReader输入流来读取URL的响应
	            in = new BufferedReader(new InputStreamReader(
	                    connection.getInputStream(), "utf-8"));
	            String line;
	            while ((line = in.readLine()) != null) {
	               result += line;
	            }
	        } catch (Exception e) {
	            System.out.println("发送GET请求出现异常！" + e);
	            e.printStackTrace();
	        }
	        // 使用finally块来关闭输入流
	        finally {
	            try {
	                if (in != null) {
	                    in.close();
	                }
	            } catch (Exception e2) {
	                e2.printStackTrace();
	            }
	        }
	        return result;
	    }
	   
	    
	  
   
	
	 /**
	  * 判断字符串是否存在于字符串数组中
	  * @param stringArray
	  * @param source
	  * @return
	  */
	public static boolean contains(String[] stringArray, String source) {
	    	  // 转换为list
	    	  List<String> tempList = Arrays.asList(stringArray);
	    	  // 利用list的包含方法,进行判断
	    	  if(tempList.contains(source))
	    	  {
	    	   return true;
	    	  } else {
	    	   return false;
	    	  }
	  }     
	   
	 /**
	  * 根据错误码以及错误讯息向前端返回信息
	  * @param errorNum
	  * @param response
	  * @param errMsg
	  */
    public void paraMandaChkAndReturn(int errorNum,HttpServletResponse response,String errMsg){
        Map<String, Object> map=paraMandaChkAndReturnMap(errorNum, response, errMsg);
        writeErrorJson(response,map);
    }
    
    public Map<String, Object> paraMandaChkAndReturnMap(int errorNum,HttpServletResponse response,String errMsg){
        Map<String, Object> map=new HashMap<String,Object>();
        map.clear();
        map.put("status", "0");
        map.put("error_code", errorNum);
        map.put("errMsg", errMsg);
        return map;
    }
    
    /**
     * 
     * 验证密码强度
     * @param value
     * @return
     */
    public int verifyPassWordStrength(String value){
    	int returnValue=0;
    	if(verifyPassWordString(value)==0){
    		returnValue=1;
    		return returnValue;
    	}if(verifyPassWordSAI(value)==0){
    		returnValue=2;
    		return returnValue;
    	}if(verifyPassWordSAT(value)==0){
    		returnValue=3;
    		return returnValue;
    	}
    	return  returnValue;
    }
    
    /**
     * 
     * 验证密码是否为纯字母
     * @param value
     * @return
     */
    public int verifyPassWordString(String value){
    	int returnValue=0;
    
        Pattern p = Pattern.compile("[a-zA-Z]+");
    	Matcher m = p.matcher(value);
    	boolean chinaKey = m.matches();
    	if(chinaKey){
    		returnValue=0;
    	} else{
    		returnValue=1;
    		return returnValue;
    	 }
    	return  returnValue;
    }
    /**
     * 
     * 验证密码是否为数字和字母组合
     * @param value
     * @return ^\w+$
     */
    public int verifyPassWordSAI(String value){
    	int returnValue=0;
    
        Pattern p = Pattern.compile("^[A-Za-z0-9]+$");
    	Matcher m = p.matcher(value);
    	boolean chinaKey = m.matches();
    	if(chinaKey){
    		returnValue=0;
    	} else{
    		returnValue=1;
    		return returnValue;
    	 }
    	return  returnValue;
    }
    /**
     * 
     * 验证密码是否为数字、字符、特殊字符组成切密码大于6位
     * @param value
     * @return^\w+$
     */
    public int verifyPassWordSAT(String value){
    	int returnValue=0;
    
        Pattern p = Pattern.compile("(?!^\\d+$)(?!^[a-zA-Z]+$)(?!^[_#@]+$).{6,}");
    	Matcher m = p.matcher(value);
    	boolean chinaKey = m.matches();
    	if(chinaKey){
    		returnValue=0;
    	} else{
    		returnValue=1;
    		return returnValue;
    	 }
    	return  returnValue;
    }
    public static Safelevel GetPwdSecurityLevel(String pPasswordStr) {
        Safelevel safelevel = Safelevel.VERY_WEAK;
        if (pPasswordStr == null) {
            return safelevel;
        }
        int grade = 0;
        int index = 0;
        char[] pPsdChars = pPasswordStr.toCharArray();
 
        int numIndex = 0;
        int sLetterIndex = 0;
        int lLetterIndex = 0;
        int symbolIndex = 0;
 
        for (char pPsdChar : pPsdChars) {
            int ascll = pPsdChar;
            /*
             * 数字 48-57 <a href="https://www.baidu.com/s?wd=A-Z&tn=44039180_cpr&fenlei=mv6quAkxTZn0IZRqIHckPjm4nH00T1Y4njwBuHn3nHuWrARYuHfk0AP8IA3qPjfsn1bkrjKxmLKz0ZNzUjdCIZwsrBtEXh9GuA7EQhF9pywdQhPEUiqkIyN1IA-EUBtdP16LP163PWn" target="_blank" class="baidu-highlight">A-Z</a> 65 - 90 <a href="https://www.baidu.com/s?wd=a-z&tn=44039180_cpr&fenlei=mv6quAkxTZn0IZRqIHckPjm4nH00T1Y4njwBuHn3nHuWrARYuHfk0AP8IA3qPjfsn1bkrjKxmLKz0ZNzUjdCIZwsrBtEXh9GuA7EQhF9pywdQhPEUiqkIyN1IA-EUBtdP16LP163PWn" target="_blank" class="baidu-highlight">a-z</a> 97 - 122 !"#$%&'()*+,-./ (ASCII码：33~47)
             * :;<=>?@ (ASCII码：58~64) [\]^_` (ASCII码：91~96) {|}~
             * (ASCII码：123~126)
             */
            if (ascll >= 48 && ascll <= 57) {
                numIndex++;
            } else if (ascll >= 65 && ascll <= 90) {
                lLetterIndex++;
            } else if (ascll >= 97 && ascll <= 122) {
                sLetterIndex++;
            } else if ((ascll >= 33 && ascll <= 47)
                    || (ascll >= 58 && ascll <= 64)
                    || (ascll >= 91 && ascll <= 96)
                    || (ascll >= 123 && ascll <= 126)) {
                symbolIndex++;
            }
        }
        if(isContinuous(pPasswordStr)){
        	index = 25;
        }else{
        /*
         * 一、密码长度: 5 分: 小于等于 4 个字符 10 分: 5 到 7 字符 25 分: 大于等于 8 个字符
         */
        if (pPsdChars.length <= 8) {
            index = 5;
        } else if (pPsdChars.length <= 16) {
            index = 20;
        } else {
            index = 30;
        }
        grade += index;
        /**
         * 
         */
      
        /*
         * 二、字母: 0 分: 没有字母 10 分: 全都是小（大）写字母 20 分: 大小写混合字母
         */
        if (lLetterIndex == 0 && sLetterIndex == 0) {
            index = 0;
        } else if (lLetterIndex != 0 && sLetterIndex != 0) {
            index = 20;
        } else {
            index = 10;
        }
        grade += index;
        
        /*
         * 三、数字: 0 分: 没有数字 10 分: 1 个数字 20 分: 大于 1 个数字
         */
        if (numIndex == 0) {
            index = 0;
        } else {
            index = 20;
        }
        grade += index;
 
        /*
         * 四、符号: 0 分: 没有符号 20 分: 1 个符号 25 分: 大于 1 个符号
         */
        if (symbolIndex == 0) {
            index = 0;
        } else if (symbolIndex == 1) {
            index = 20;
        } else {
            index = 25;
        }
        grade += index;
        /*
         * 五、奖励: 2 分: 字母和数字 3 分: 字母、数字和符号 5 分: 大小写字母、数字和符号
         */
        if (sLetterIndex != 0 && lLetterIndex != 0 && numIndex != 0
                && symbolIndex != 0) {
            index = 5;
        } else if ((sLetterIndex != 0 || lLetterIndex != 0) && numIndex != 0
                && symbolIndex != 0) {
            index = 3;
        }else if ((sLetterIndex != 0 || lLetterIndex != 0) && numIndex != 0) {
            index = 2;
        } else{
        	index=0;
        }
        grade += index;
        }

        /*
         * 最后的评分标准: >= 90: 非常安全 >= 80: 安全（Secure） >= 70: 非常强 >= 60: 强（Strong） >=
         * 50: 一般（Average） >= 25: 弱（Weak） >= 0: 非常弱
         */    
        if(grade >=90){
            safelevel = Safelevel.VERY_SECURE;
        }else if(grade >= 70){
            safelevel = Safelevel.VERY_STRONG;
        }else if(grade >= 50){
            safelevel = Safelevel.STRONG;
        }else if(grade >= 10){
            safelevel = Safelevel.WEAK; 
        }
        return safelevel;
    }
     
    public enum Safelevel {
        VERY_WEAK, /* 非常弱 */
        WEAK, /* 弱 */
        AVERAGE, /* 一般 */
        STRONG, /* 强 */
        VERY_STRONG, /* 非常强 */
        SECURE, /* 安全 */
        VERY_SECURE /* 非常安全 */
    }
    public static Boolean isContinuous(String str){
        Boolean allEquals = true;  
        String element=str.substring(0,1);
        for ( int i = 0; i < str.length(); i++) {  
            if (str.substring(i,i+1) != element) {  
                allEquals = false;  
                break;  
            }  
            element = str.substring(i,i+1);  
        }  
    	return allEquals;
    }
    /**
     * IP地址黑白名单
     * @param request
     * @return
     */
    public String getRemortIP(HttpServletRequest request) { 
   	    if (request.getHeader("x-forwarded-for") == null) { 
   	        return request.getRemoteAddr(); 
   	    } 
   	    return request.getHeader("x-forwarded-for"); 
   	}  
    public String getIpAddr(HttpServletRequest request) { 
   	    String ip = request.getHeader("x-forwarded-for"); 
   	    if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) { 
   	        ip = request.getHeader("Proxy-Client-IP"); 
   	    } 
   	    if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) { 
   	        ip = request.getHeader("WL-Proxy-Client-IP"); 
   	    } 
   	    if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) { 
   	        ip = request.getRemoteAddr(); 
   	    } 
   	    return ip; 
   	}   
    
    
    /** 
     * 发送Put请求 
     *  
     * @param url 
     *            目的地址 
     * @param json 
     *            请求参数，JSONObject类型。 
     * @return 远程响应结果 
     */  
    public static String sendDelete(String url) {  
    	DefaultHttpClient client = new DefaultHttpClient();
		HttpDelete delete = new HttpDelete(url);
		delete.setHeader("Content-Type","text/html");
		HttpResponse res;
		String result = null;
		try {
			res = client.execute(delete);
			if (res.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				result = EntityUtils.toString(res.getEntity());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
    }
    /** 
     * 发送Put请求 
     *  
     * @param url 
     *            目的地址 
     * @param json 
     *            请求参数，JSONObject类型。 
     * @return 远程响应结果 
     */  
    public static String sendPutByJson(String url, JSONObject json) {  
    	DefaultHttpClient client = new DefaultHttpClient();
		HttpPut put = new HttpPut(url);
		JSONObject response = null;
		String result = null;
		try {
			StringEntity entity = new StringEntity(json.toString(), HTTP.UTF_8);
			entity.setContentEncoding("HTTP.UTF_8");  
			entity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json")); //发送json数据需要设置contentType
			put.setEntity(entity);
			HttpResponse res = client.execute(put);
			if(res.getStatusLine().getStatusCode() == HttpStatus.SC_OK){
				result = EntityUtils.toString(res.getEntity());// 返回json格式：
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return result;
    }
    /** 
     * 发送POST请求 
     *  
     * @param url 
     *            目的地址 
     * @param json 
     *            请求参数，JSONObject类型。 
     * @return 远程响应结果 
     */  
    public static String sendPostByJson(String url, JSONObject json) {  
    	DefaultHttpClient client = new DefaultHttpClient();
		HttpPost post = new HttpPost(url);
		JSONObject response = null;
		String result = null;
		try {
			StringEntity entity = new StringEntity(json.toString(), HTTP.UTF_8);
			entity.setContentEncoding("HTTP.UTF_8");  
			entity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json")); //发送json数据需要设置contentType
			post.setEntity(entity);
			HttpResponse res = client.execute(post);
			if(res.getStatusLine().getStatusCode() == HttpStatus.SC_OK){
				result = EntityUtils.toString(res.getEntity());// 返回json格式：
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return result;
    }
    /** 
     * 发送POST请求 
     *  
     * @param url 
     *            目的地址 
     * @param parameters 
     *            请求参数，Map类型。 
     * @return 远程响应结果 
     */  
    public static String sendPost(String url, Map<String, Object> map) {  
        String result = "";// 返回的结果  
        BufferedReader in = null;// 读取响应输入流  
        PrintWriter out = null;  
        StringBuffer sb = new StringBuffer();// 处理请求参数  
        String params="";
        try {  
		Set es = map.entrySet();//所有参与传参的参数按照accsii排序（升序）
		Iterator it = es.iterator();
		while(it.hasNext()) {
			Map.Entry entry = (Map.Entry)it.next();
			String k = (String)entry.getKey();
			Object v = entry.getValue();
			if(null != v && !"".equals(v) && !"sign".equals(k) && !"key".equals(k)) {
				sb.append(k + "=" + v + "&");
			}
		  }
		   String temp_params = sb.toString(); 
		   if (temp_params.length()>0) {
			   params = temp_params.substring(0, temp_params.length() - 1);  
		   }
            // 创建URL对象  
            java.net.URL connURL = new java.net.URL(url);  
            // 打开URL连接  
            java.net.HttpURLConnection httpConn = (java.net.HttpURLConnection) connURL  
                    .openConnection();  
            // 设置通用属性  
            httpConn.setRequestProperty("Accept", "*/*");  
            httpConn.setRequestProperty("Connection", "Keep-Alive");  
            httpConn.setRequestProperty("User-Agent",  
                    "Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 6.1)");  
            // 设置POST方式  
            httpConn.setDoInput(true);  
            httpConn.setDoOutput(true);  
            // 获取HttpURLConnection对象对应的输出流  
            out = new PrintWriter(httpConn.getOutputStream());  
            // 发送请求参数  
            //out.write(params); 
            out.write(params);  
            // flush输出流的缓冲  
            out.flush();  
            // 定义BufferedReader输入流来读取URL的响应，设置编码方式  
            in = new BufferedReader(new InputStreamReader(httpConn  
                    .getInputStream(), "utf-8"));  
            String line;  
            // 读取返回的内容  
            while ((line = in.readLine()) != null) {  
                result += line;  
                
            }  
        } catch (Exception e) {  
            e.printStackTrace();  
        } finally {  
            try {  
                if (out != null) {  
                    out.close();  
                }  
                if (in != null) {  
                    in.close();  
                }  
            } catch (IOException ex) {  
                ex.printStackTrace();  
            }  
        }  
        return result;  
    }  	
    /**
	 * 生成加密串
	 * @param characterEncoding
	 * @param parameters
	 * @return
	 */
	public static String createSign(SortedMap<Object,Object> parameters){
		StringBuffer sb = new StringBuffer();
		Set es = parameters.entrySet();//所有参与传参的参数按照accsii排序（升序）
		Iterator it = es.iterator();
		while(it.hasNext()) {
			Map.Entry entry = (Map.Entry)it.next();
			String k = (String)entry.getKey();
			Object v = entry.getValue();
			if(null != v && !"".equals(v)&& !"null".equals(v) 
					&& !"sign".equals(k) && !"key".equals(k)) {
				sb.append(k + "=" + v + "&");
			}
		}
		 String temp_params = sb.toString();  
		return sb.toString().substring(0, temp_params.length()-1);
	}
	 /**判断登录账号是Email或Phone或奥鹏卡号
     * 
     * @param username
     * @return
     */
    public User checkUsername(String username, UserService userService){//flag=1:email   flag=2:phone
    	User user=null;
        //先校验用户名字段
    	user=userService.findByUsername(username);
    	if(user==null) {
            //邮箱
//    		String check1="^([a-z0-9A-Z]+[-|_|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
        	//String check1="^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$";
            //手机//check="^[1][3,4,5,8][0-9]{9}$";
            String check2="^[1][1-9]{1}[0-9]{9}$";
            //Pattern regex1 = Pattern.compile(check1);
            Pattern regex2 = Pattern.compile(check2);
    		List<User> userList = null;
    		if(regex2.matcher(username).matches()){//手机
    			String desPhone=Help_Encrypt.encrypt(username);
				userList=userService.findByPhone(desPhone);
			}if(userList==null||userList.size()==0){//邮箱
				userList=userService.findByEmail(username);
			}
			if(userList==null||userList.size()==0){//纯数字可能为奥鹏卡号
				userList = userService.findByCardNo(username);
			}
			//有且只有一个手机号、邮箱、卡号的用户，否则不能算是有效用户
			if(userList!=null && userList.size()==1){
				user = userList.get(0);
			}
    	}
        return user;
    }

}
