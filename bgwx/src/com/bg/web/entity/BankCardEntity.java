package com.bg.web.entity;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

@Entity
@Table(name = "b_bankcard", schema = "")
@DynamicUpdate(true)
@DynamicInsert(true)
@PrimaryKeyJoinColumn(name = "id")
public class BankCardEntity implements java.io.Serializable{
    /**主键**/
	private Integer id;
	/**商户id**/
	private Integer merId;
	@Id
	@Column(name="ID",nullable=false)
	public Integer getId() {
		return id;
	}
	
	public void setId(Integer id) {
		this.id = id;
	}
	@Id
	@Column(name="mer_id",nullable=false)
	public Integer getMerId() {
		return merId;
	}

	public void setMerId(Integer merId) {
		this.merId = merId;
	}
}
