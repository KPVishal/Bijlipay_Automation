package com.bijlipay.Model.DTO;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class ForgotPassword {

	private String username;
	
	private String oldPass;
	
	@NotBlank
	@Size(min = 6, max = 20)
	private String newPass;
	
	@NotBlank
	@Size(min = 6, max = 20)
	private String confirmNewPass;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getOldPass() {
		return oldPass;
	}

	public void setOldPass(String oldPass) {
		this.oldPass = oldPass;
	}

	public String getNewPass() {
		return newPass;
	}

	public void setNewPass(String newPass) {
		this.newPass = newPass;
	}

	public String getConfirmNewPass() {
		return confirmNewPass;
	}

	public void setConfirmNewPass(String confirmNewPass) {
		this.confirmNewPass = confirmNewPass;
	}

}
