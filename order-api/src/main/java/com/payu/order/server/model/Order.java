package com.payu.order.server.model;

import javax.xml.bind.annotation.XmlRootElement;

import java.io.Serializable;
import java.math.BigDecimal;

@XmlRootElement
public class Order implements Serializable {

	private long id;
	
	private long userId;
	
	private String what;
	
	private BigDecimal amount;

	public Order() {
	}

	public Order(BigDecimal amount, String what) {
		this.amount = amount;
		this.what = what;
	}

	public String getWhat() {
		return what;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setWhat(String what) {
		this.what = what;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	@Override
	public String toString() {
		return "Order [id=" + id + ", userId=" + userId + ", what=" + what
				+ ", amount=" + amount + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((amount == null) ? 0 : amount.hashCode());
		result = prime * result + (int) (id ^ (id >>> 32));
		result = prime * result + (int) (userId ^ (userId >>> 32));
		result = prime * result + ((what == null) ? 0 : what.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Order other = (Order) obj;
		if (amount == null) {
			if (other.amount != null)
				return false;
		} else if (!amount.equals(other.amount))
			return false;
		if (id != other.id)
			return false;
		if (userId != other.userId)
			return false;
		if (what == null) {
			if (other.what != null)
				return false;
		} else if (!what.equals(other.what))
			return false;
		return true;
	}
	
	
	
	

}
