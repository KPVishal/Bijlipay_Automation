package com.bijlipay.Model.DTO;

import com.bijlipay.Model.Role;

import javax.validation.constraints.NotBlank;

public class SignUpRequest {

    @NotBlank
    private String name;

    @NotBlank
    private String username;

    @NotBlank
    private String role;

    @NotBlank
    private String email;

    @NotBlank
    private String password;

    @NotBlank
    private String confirm_password;

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirm_password() {
        return confirm_password;
    }

    public void setConfirm_password(String confirm_password) {
        this.confirm_password = confirm_password;
    }

}
