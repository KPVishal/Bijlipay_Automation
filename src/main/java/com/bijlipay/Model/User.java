package com.bijlipay.Model;

import java.util.Collection;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "auto_users")
@JsonIgnoreProperties(value = { "created_at", "updated_at" }, allowGetters = true)
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "user_id", nullable = false)
	private long id;

	@NotBlank
	@Size(max = 40)
	@Column(name = "name", nullable = false)
	private String name;

	@NotBlank
	@Size(max = 15)
	@Column(name = "username", nullable = false)
	private String username;

	@JsonIgnore
	@NotBlank
	@Size(max = 100)
	@Column(name = "password", nullable = false)
	private String password;

	@JsonIgnore
	@Column(name = "is_active")
	private boolean isActive;

	@NotBlank
	@Column(name = "email_id", nullable = false)
	private String email;

	
	@CreationTimestamp
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "created_at")
	@JsonFormat(pattern = "yyyy-MM-dd")
	private Date created_at;

	
	@UpdateTimestamp
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "updated_at")
	@JsonFormat(pattern = "yyyy-MM-dd")
	private Date updated_at;

	@ManyToMany
	@JoinTable(name = "auto_users_roles", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns =
	@JoinColumn(name = "role_id"))
	private Collection<Role> role;
	
	public User() {
		super();
	}

	public User(@NotBlank @Size(max = 40) String name, @NotBlank @Size(max = 15) String username,
			@NotBlank @Size(max = 100) String email, @NotBlank @Size(max = 100) String password) {
		this.name = name;
		this.username = username;
		this.email = email;
		this.password = password;
	}

	public Date getUpdated_at() {
		return updated_at;
	}

	public void setUpdated_at(Date updated_at) {
		this.updated_at = updated_at;
	}

	public Date getCreated_at() {
		return created_at;
	}

	public void setCreated_at(Date created_at) {
		this.created_at = created_at;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public boolean isActive() {
		return isActive;
	}

	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}

	public Collection<Role> getRole() {
		return role;
	}

	public void setRole(Collection<Role> role) {
		this.role = role;
	}

}
