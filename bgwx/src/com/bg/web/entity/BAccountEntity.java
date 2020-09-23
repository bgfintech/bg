package com.bg.web.entity;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

@Entity
@Table(name = "b_account", schema = "")
@DynamicUpdate(true)
@DynamicInsert(true)
@PrimaryKeyJoinColumn(name = "id")
public class BAccountEntity implements java.io.Serializable{

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/*主键ID*/
    private String id;
    /*注册手机号*/
    private String account;
  
    /*登录口令*/
    private String passwd;
    
    private String name;
    
    private String idcard;
 
    @Column(name="name",nullable=false)
    public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	@Column(name="idcard",nullable=false)
	public String getIdcard() {
		return idcard;
	}

	public void setIdcard(String idcard) {
		this.idcard = idcard;
	}

	@Column(name="passwd",nullable=false)
    public String getPasswd() {
		return passwd;
	}

	public void setPasswd(String passwd) {
		this.passwd = passwd;
	}

	@Id
    @Column(name="ID",nullable=false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)  
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    @Column(name="account",nullable=false)
	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

   
    
}
