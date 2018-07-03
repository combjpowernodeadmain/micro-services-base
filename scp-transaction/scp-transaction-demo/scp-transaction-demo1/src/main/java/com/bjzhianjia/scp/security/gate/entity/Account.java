/*
 *  This program is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation; either version 2 of the License, or
 *  (at your option) any later version.

 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.

 *  You should have received a copy of the GNU General Public License along
 *  with this program; if not, write to the Free Software Foundation, Inc.,
 *  51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 */

package com.bjzhianjia.scp.security.gate.entity;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;


/**
 * 
 * 
 * @author Mr.AG
 * @email 576866311@qq.com
 * @version 1.0 
 */
@Table(name = "account")
public class Account implements Serializable {
	private static final long serialVersionUID = 1L;
	
	    //
    @Id
    private Integer id;
	
	    //
    @Column(name= "user_id")
    private Integer userId;
	
	    //用户余额
    @Column(name = "balance")
    private Integer balance;
	
	    //冻结金额，扣款暂存余额
    @Column(name = "freeze_amount")
    private Integer freezeAmount;
	
	    //
    @Column(name = "create_time")
    private Date createTime;
	
	    //
    @Column(name = "update_time")
    private Date updateTime;
	

	/**
	 * 设置：
	 */
	public void setId(Integer id) {
		this.id = id;
	}
	/**
	 * 获取：
	 */
	public Integer getId() {
		return id;
	}
	/**
	 * 设置：
	 */
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	/**
	 * 获取：
	 */
	public Integer getUserId() {
		return userId;
	}
	/**
	 * 设置：用户余额
	 */
	public void setBalance(Integer balance) {
		this.balance = balance;
	}
	/**
	 * 获取：用户余额
	 */
	public Integer getBalance() {
		return balance;
	}
	/**
	 * 设置：冻结金额，扣款暂存余额
	 */
	public void setFreezeAmount(Integer freezeAmount) {
		this.freezeAmount = freezeAmount;
	}
	/**
	 * 获取：冻结金额，扣款暂存余额
	 */
	public Integer getFreezeAmount() {
		return freezeAmount;
	}
	/**
	 * 设置：
	 */
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	/**
	 * 获取：
	 */
	public Date getCreateTime() {
		return createTime;
	}
	/**
	 * 设置：
	 */
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	/**
	 * 获取：
	 */
	public Date getUpdateTime() {
		return updateTime;
	}
}
