package com.bg.web.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

@Entity
@Table(name = "d_union_pay_area", schema = "")
@DynamicUpdate(true)
@DynamicInsert(true)
@PrimaryKeyJoinColumn(name = "id")
public class DUnionPayAreaEntity {
	/**索引ID**/
	private String id;
	/**省市县**/
	private String province;
	/**编码**/
	private String code;
	/**级别**/
	private String level;
	/**上级级别**/
	private String parent_level;
	@Id
	@Column(name="id",nullable=false)
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	@Column(name="province",nullable=false)
	public String getProvince() {
		return province;
	}
	public void setProvince(String province) {
		this.province = province;
	}
	@Column(name="code",nullable=false)
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	@Column(name="level",nullable=false)
	public String getLevel() {
		return level;
	}
	public void setLevel(String level) {
		this.level = level;
	}
	@Column(name="上级级别 ",nullable=false)
	public String getParent_level() {
		return parent_level;
	}
	public void setParent_level(String parent_level) {
		this.parent_level = parent_level;
	}

	

}
