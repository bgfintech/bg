package com.bg.web.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
@Entity
@Table(name = "d_mcc", schema = "")
@DynamicUpdate(true)
@DynamicInsert(true)
@PrimaryKeyJoinColumn(name = "mcc_code")
public class DMccEntity {

	/**mcccode**/
	private String mccCode;
	/**行业**/
	private String business;
	/**类别**/
	private String type;
	/****/
	private String mer_type;
	@Id
	@Column(name="mcc_code",nullable=false)
	public String getMccCode() {
		return mccCode;
	}
	public void setMccCode(String mccCode) {
		this.mccCode = mccCode;
	}
	@Column(name="business",nullable=false)
	public String getBusiness() {
		return business;
	}
	public void setBusiness(String business) {
		this.business = business;
	}
	@Column(name="type",nullable=false)
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	@Column(name="mer_type",nullable=false)
	public String getMer_type() {
		return mer_type;
	}
	public void setMer_type(String mer_type) {
		this.mer_type = mer_type;
	}
	

}
