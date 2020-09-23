package com.bg.web.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

@Entity
@Table(name = "d_bank_code", schema = "")
@DynamicUpdate(true)
@DynamicInsert(true)
@PrimaryKeyJoinColumn(name = "bank_code")
public class DBankCodeEntity {

	
	/**行号**/
	private String bankCode;
	/**名称**/
	private String bankName;
	@Id
	@Column(name="bank_code",nullable=false)
	public String getBankCode() {
		return bankCode;
	}
	public void setBankCode(String bankCode) {
		this.bankCode = bankCode;
	}
	@Column(name="bank_name",nullable=false)
	public String getBankName() {
		return bankName;
	}
	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

}
