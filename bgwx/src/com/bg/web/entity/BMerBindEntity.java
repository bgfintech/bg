package com.bg.web.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
@Entity
@Table(name = "b_mer_bind", schema = "")
@DynamicUpdate(true)
@DynamicInsert(true)
@PrimaryKeyJoinColumn(name = "id")
public class BMerBindEntity {
	/**主键*/
	private String id;
	/**商户ID**/
	private String merId;
	/**账户ID**/
	private String acctId;
	
	@Id
    @Column(name="ID",nullable=false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)  
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	 @Column(name="mer_id",nullable=false)
	public String getMerId() {
		return merId;
	}
	public void setMerId(String merId) {
		this.merId = merId;
	}
	 @Column(name="acct_id",nullable=false)
	public String getAcctId() {
		return acctId;
	}
	public void setAcctId(String acctId) {
		this.acctId = acctId;
	}
	
	
	

}
