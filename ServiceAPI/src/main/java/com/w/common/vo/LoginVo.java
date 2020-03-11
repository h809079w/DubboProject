package com.w.common.vo;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

public class LoginVo implements Serializable {

	@NotNull
	private Long id;

	@NotNull
	private String password;

	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	@Override
	public String toString() {
		return "LoginVo [id=" + id + ", password=" + password + "]";
	}
}
