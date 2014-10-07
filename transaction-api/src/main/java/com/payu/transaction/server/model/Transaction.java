package com.payu.transaction.server.model;

import javax.xml.bind.annotation.XmlRootElement;

import java.io.Serializable;
import java.math.BigDecimal;

@XmlRootElement
public class Transaction implements Serializable {
	
	private static final long serialVersionUID = -4695495473930510134L;

	private long id;
	
	private String paymentMethodBrand;

	private BigDecimal amount;
    
    private long orderId;

    public Transaction() {
    }

    public Transaction(BigDecimal amount, String paymentMethodBrand, long orderId) {
        this.amount = amount;
		this.paymentMethodBrand = paymentMethodBrand;
		this.orderId = orderId;
    }


    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

	public String getPaymentMethodBrand() {
		return paymentMethodBrand;
	}

	public void setPaymentMethodBrand(String paymentMethodBrand) {
		this.paymentMethodBrand = paymentMethodBrand;
	}




	public long getOrderId() {
		return orderId;
	}

	public void setOrderId(long orderId) {
		this.orderId = orderId;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	@Override
	public String toString() {
		return "Transaction [id=" + id + ", paymentMethodBrand="
				+ paymentMethodBrand + ", amount=" + amount + ", oderId="
				+ orderId + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((amount == null) ? 0 : amount.hashCode());
		result = prime * result + (int) (id ^ (id >>> 32));
		result = prime * result + (int) (orderId ^ (orderId >>> 32));
		result = prime
				* result
				+ ((paymentMethodBrand == null) ? 0 : paymentMethodBrand
						.hashCode());
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
		Transaction other = (Transaction) obj;
		if (amount == null) {
			if (other.amount != null)
				return false;
		} else if (!amount.equals(other.amount))
			return false;
		if (id != other.id)
			return false;
		if (orderId != other.orderId)
			return false;
		if (paymentMethodBrand == null) {
			if (other.paymentMethodBrand != null)
				return false;
		} else if (!paymentMethodBrand.equals(other.paymentMethodBrand))
			return false;
		return true;
	}
    
    
	
	
	

}
