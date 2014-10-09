package com.payu.user.server.model;

import javax.xml.bind.annotation.XmlRootElement;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@XmlRootElement
public class Pos implements Serializable {

	private static final long serialVersionUID = 1L;

	private long id;

	private String name;

	private Set<String> enabledPaymentMethodBrands = new HashSet<>();

	private boolean active = true;

	public Pos() {
		super();
	}

	public Pos(long id, String name, String... paymentMethodBrands) {
		super();
		this.id = id;
		this.name = name;
		for (String pmb : paymentMethodBrands) {
			enabledPaymentMethodBrands.add(pmb);
		}
	}

	public Pos(String name, String... paymentMethodBrands) {
		super();
		this.name = name;
		for (String pmb : paymentMethodBrands) {
			enabledPaymentMethodBrands.add(pmb);
		}
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public boolean isPaymetnMethodBrandActive(String o) {
		return enabledPaymentMethodBrands.contains(o);
	}

	public boolean enablePaymenMethodBrand(String e) {
		return enabledPaymentMethodBrands.add(e);
	}

	public boolean enablePaymenMethodBrand(Object o) {
		return enabledPaymentMethodBrands.remove(o);
	}

}
