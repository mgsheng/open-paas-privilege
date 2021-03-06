package cn.com.open.pay.platform.manager.interceptor;

import java.io.PrintWriter;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import cn.com.open.pay.platform.manager.dev.OesPrivilegeDev;
import cn.com.open.pay.platform.manager.privilege.service.PrivilegeGetSignatureService;
import cn.com.open.pay.platform.manager.tools.BaseControllerUtil;
import net.sf.json.JSONObject;

public class VerifyUserPrivilegeInterceptor extends BaseControllerUtil implements HandlerInterceptor {
	@Autowired
	private PrivilegeGetSignatureService privilegeGetSignatureService;
	@Autowired
	private OesPrivilegeDev oesPrivilegeDev;
	
	//不拦截uri
	private List<String> uncheckUrls;

	public List<String> getUncheckUrls() {
		return uncheckUrls;
	}

	public void setUncheckUrls(List<String> uncheckUrls) {
		this.uncheckUrls = uncheckUrls;
	}

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		String url = request.getRequestURI();
		url = url.replaceAll(request.getContextPath(), "");
		// 是否过滤
		boolean doFilter = true;
		for (String s : uncheckUrls) {
			if (url.indexOf(s) != -1) {
				// 如果uri中包含不过滤的uri，则不进行过滤
				doFilter = false;
				break;
			}
		}
		if (doFilter) {
			// 从session中获取登录者实体
			Map<String, Object> user = (Map<String, Object>) request.getSession().getAttribute("user");
			// 如果session中存在登录者实体，则继续
			if (null == user) {
				// 如果session中不存在登录者实体，则弹出框提示重新登录
				// 设置request和response的字符集，防止乱码
				request.setCharacterEncoding("UTF-8");
				response.setCharacterEncoding("UTF-8");
				PrintWriter out = response.getWriter();
				StringBuilder builder = new StringBuilder();
				if (request.getHeader("x-requested-with") != null) {
					response.setContentType("application/x-javascript;charset=utf-8");
					builder.append("alert('登陆超时！请重新登陆');");
				} else {
					builder.append("<script type=\"text/javascript\">");
					builder.append("alert('登陆超时！请重新登陆');");
					builder.append("</script>");
				}
				out.print(builder.toString());
				return false;
			}else {
				//若存在登录实体，检查权限
				Map<String, Object> map = privilegeGetSignatureService.getSignature((String) user.get("appId"));
				map.put("appId", user.get("appId"));
				map.put("appUserId", user.get("appUserId"));
				map.put("optUrl", url);
				String reult = sendPost(oesPrivilegeDev.getVerifyUserPrivilegeUrl(), map);
				JSONObject object = JSONObject.fromObject(reult);
				if (object.get("status").equals("1")) {
					return true;
				} else {
					request.setCharacterEncoding("UTF-8");
					response.setCharacterEncoding("UTF-8");
					PrintWriter out = response.getWriter();
					StringBuilder builder = new StringBuilder();
					if (request.getHeader("x-requested-with") != null) {
						response.setContentType("application/x-javascript;charset=utf-8");
						builder.append("alert('您不具备该操作权限！');");
					} else {
						response.setContentType("text/html;charset=utf-8");
						builder.append("<script type=\"text/javascript\">");
						builder.append("alert('您不具备该操作权限！');");
						builder.append("</script>");
					}
					out.print(builder.toString());
					return false;
				}
			}
		} else {
			return true;
		}

	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {

	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {

	}

}
