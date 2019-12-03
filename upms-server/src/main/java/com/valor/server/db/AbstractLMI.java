package com.valor.server.db;

import com.valor.model.supperclz.AbstractPrintable;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;

@MappedSuperclass
public abstract class AbstractLMI extends AbstractPrintable {

	private static final long serialVersionUID = 6191746761874799049L;

	protected Date createTime;
	protected String lastModifyUser;
	protected Date lastModifyTime;

	@Column(name = "create_time",columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
	@Temporal(TemporalType.TIMESTAMP)
	public Date getCreateTime() {
		return createTime;
	}

	@Column(name = "last_modify_user", columnDefinition = "varchar(64) default 'sys'")
	public String getLastModifyUser() {
		return lastModifyUser;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "last_modify_time",columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP",updatable=false,insertable = false) //updatable=false,insertable = false目的是为了只用数据库的插入更新策略。
	public Date getLastModifyTime() {
		return lastModifyTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public void setLastModifyUser(String lastModifyUser) {
		this.lastModifyUser = lastModifyUser;
	}

	public void setLastModifyTime(Date lastModifyTime) {
		this.lastModifyTime = lastModifyTime;
	}

}
