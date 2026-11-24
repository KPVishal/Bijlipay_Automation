package com.bijlipay.Controller;

import com.bijlipay.Security.UserPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;

public class BaseController {
	public UserPrincipal getUser() {
		return (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	}
}
