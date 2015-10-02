package org.openyu.commons.security.impl;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import org.openyu.commons.model.supporter.BaseModelSupporter;
import org.openyu.commons.security.AuthKey;

public class AuthKeyImpl extends BaseModelSupporter implements AuthKey {

	private static final long serialVersionUID = 8288777103024547716L;

	private String id;

	private long timeStamp;

	private long aliveMills;

	public AuthKeyImpl() {
		this(null, 0L);
	}

	public AuthKeyImpl(String id, long aliveMills) {
		this.id = id;
		this.timeStamp = System.currentTimeMillis();
		//
		this.aliveMills = aliveMills;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public long getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(long timeStamp) {
		this.timeStamp = timeStamp;
	}

	public long getAliveMills() {
		return aliveMills;
	}

	public void setAliveMills(long aliveMills) {
		this.aliveMills = aliveMills;
	}

	public boolean isExpired() {
		long expiredTime = timeStamp + aliveMills;
		return (System.currentTimeMillis() > expiredTime);
	}

	public boolean equals(Object object) {
		if (!(object instanceof AuthKeyImpl)) {
			return false;
		}
		if (this == object) {
			return true;
		}
		AuthKeyImpl other = (AuthKeyImpl) object;
		return new EqualsBuilder().append(id, other.id).isEquals();
	}

	public int hashCode() {
		return new HashCodeBuilder().append(id).toHashCode();
	}

	public Object clone() {
		AuthKeyImpl copy = null;
		copy = (AuthKeyImpl) super.clone();
		return copy;
	}

	public String toString() {
		ToStringBuilder builder = new ToStringBuilder(this);
		builder.append("id", id);
		builder.append("timeStamp", timeStamp);
		builder.append("aliveMills", aliveMills);
		return builder.toString();
	}

}
