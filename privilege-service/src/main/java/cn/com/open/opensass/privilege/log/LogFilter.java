package cn.com.open.opensass.privilege.log;

import cn.com.open.opensass.privilege.dev.PrivilegeServiceDev;
import cn.com.open.opensass.privilege.tools.DateUtils;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Date;
import java.util.Map;

public class LogFilter implements Filter {

    private PrivilegeServiceDev privilegeServiceDev;

    @Override
    public void init(FilterConfig config) throws ServletException {
        ApplicationContext ctx = WebApplicationContextUtils.getRequiredWebApplicationContext(config.getServletContext());
        this.privilegeServiceDev = ctx.getBean(PrivilegeServiceDev.class);
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws ServletException, IOException {
        if (response.getCharacterEncoding() == null) {
            response.setCharacterEncoding("UTF-8");
        }
        HttpServletRequest req = (HttpServletRequest) request;
        Map parameterMap = req.getParameterMap();
        //接口名  只记录 /AppMenu/getAppMenuRedis 中的 /getAppMenuRedis
        String interfaceName = req.getRequestURI().substring(req.getRequestURI().lastIndexOf("/"));

        long startTime = System.currentTimeMillis(); //请求开始时间
        PrivilegeServiceLog requestLog = getLogObject(parameterMap, interfaceName);
        requestLog.setLogName(interfaceName.substring(interfaceName.lastIndexOf("/") +1) + "_start");
        requestLog.setInvokeStatus("1"); //1正常或成功 0异常或失败
        PrivilegeServiceLogUtil.log(requestLog, privilegeServiceDev); //记录请求日志

        HttpServletResponseCopier responseCopier = new HttpServletResponseCopier((HttpServletResponse) response);
        try {
            chain.doFilter(request, responseCopier);
            responseCopier.flushBuffer();
        }  finally {
            byte[] copy = responseCopier.getCopy();
            long endTime = System.currentTimeMillis();
            PrivilegeServiceLog responseLog = getLogObject(parameterMap, interfaceName);
            if (copy.length > 0) { //出现异常则 copy数组长度为0
                String result = new String(copy, response.getCharacterEncoding());
                JSONObject jsonObject = JSONObject.parseObject(result);
                responseLog.setLogName(interfaceName.substring(interfaceName.lastIndexOf("/") +1) + "_end");
                responseLog.setInvokeStatus(jsonObject.get("status") == null ? "1" : (String)jsonObject.get("status"));
                responseLog.setErrorCode(jsonObject.get("error_code") == null ? null : String.valueOf(jsonObject.get("error_code")));
                responseLog.setErrorMessage((String)jsonObject.get("errMsg"));
                responseLog.setExecutionTime(endTime - startTime);
                PrivilegeServiceLogUtil.log(responseLog, privilegeServiceDev); //记录响应日志
            } else { //处理出现异常
                responseLog.setLogName(interfaceName.substring(interfaceName.lastIndexOf("/") +1) + "_end");
                responseLog.setInvokeStatus("0");
                responseLog.setErrorCode(String.valueOf(10000));
                responseLog.setExecutionTime(endTime - startTime);
                responseLog.setErrorMessage("系统异常");
                PrivilegeServiceLogUtil.log(responseLog, privilegeServiceDev);
            }
        }
    }

    @Override
    public void destroy() {
    }

    private PrivilegeServiceLog getLogObject(Map map, String interfaceName) {
        PrivilegeServiceLog privilegeServiceLog = new PrivilegeServiceLog();
        try {
            BeanUtils.populate(privilegeServiceLog, map);
        } catch (IllegalAccessException | InvocationTargetException ex) {
            ex.printStackTrace();
        }
        privilegeServiceLog.setCreateTime(DateUtils.toDateText(new Date(), DateUtils.DEFAULT_DATE_TIME_FORMAT));
        privilegeServiceLog.setInterfaceName(interfaceName);
        return privilegeServiceLog;
    }

}