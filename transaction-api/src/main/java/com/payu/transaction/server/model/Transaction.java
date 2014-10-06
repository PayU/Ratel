package com.payu.transaction.server.model;

import javax.xml.bind.annotation.XmlRootElement;

import java.io.Serializable;
import java.math.BigDecimal;

@XmlRootElement
public class Transaction implements Serializable {
	
	private String paymentMethodBrand;

	private BigDecimal amount;
    
    private long oderId;

    public Transaction() {
    }

    public Transaction(BigDecimal amount, String paymentMethodBrand, long orderId) {
        this.amount = amount;
		this.paymentMethodBrand = paymentMethodBrand;
		oderId = orderId;
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

	public long getOderId() {
		return oderId;
	}

	public void setOderId(long oderId) {
		this.oderId = oderId;
	}

	@Override
	public String toString() {
		return "Transaction [paymentMethodBrand=" + paymentMethodBrand
				+ ", amount=" + amount + ", oderId=" + oderId + "]";
	}
    
    

}
