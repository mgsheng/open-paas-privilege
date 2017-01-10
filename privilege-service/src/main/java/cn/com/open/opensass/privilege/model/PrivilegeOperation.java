package cn.com.open.opensass.privilege.model;

import java.util.Date;

public class PrivilegeOperation extends AbstractDomain {
	/**
	 * 资源
	 */
	private static final long serialVersionUID = 1L;
	
	private String id;
	private String name;
	private String code;
	private int status;
	private Date createTime;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
}
