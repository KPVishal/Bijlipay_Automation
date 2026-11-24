package com.bijlipay.Model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "auto_report_logs")
public class ReportLog {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "log_id")
	private int logId;

	@Column(name = "username", nullable = false)
	private String username;

	@Column(name = "download_at")
	@JsonIgnore
	private String download_at;

	@Column(name = "description", nullable = false)
	private String description;

	public int getLogId() {
		return logId;
	}

	public void setLogId(int logId) {
		this.logId = logId;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getDownload_at() {
		return download_at;
	}

	public void setDownload_at(String download_at) {
		this.download_at = download_at;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public static ReportLog to(String username, String merchantname) {

		ReportLog log = new ReportLog();
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd");
		LocalDateTime now = LocalDateTime.now();
		log.setDownload_at(dtf.format(now));
		log.setDescription(merchantname);
		log.setUsername(username);

		return log;

	}

	public static ReportLog to(String username) {
		ReportLog log = new ReportLog();
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd");
		LocalDateTime now = LocalDateTime.now();
		log.setDownload_at(dtf.format(now));
		log.setDescription("ALL Merchant Name ..");
		log.setUsername(username);

		return log;
	}

}
