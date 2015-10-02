package org.openyu.commons.collector.impl;

import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.openyu.commons.bean.supporter.BaseBeanSupporter;
import org.openyu.commons.collector.CollectorStarter;

public class CollectorStarterImpl extends BaseBeanSupporter implements
		CollectorStarter {

	private static final long serialVersionUID = 7273145240949832866L;

	private List<String> names = new LinkedList<String>();

	public CollectorStarterImpl() {
	}

	/**
	 * collector類別名稱
	 * 
	 * @return
	 */
	public List<String> getNames() {
		return names;
	}

	public void setNames(List<String> names) {
		this.names = names;
	}

	public String toString() {
		ToStringBuilder builder = new ToStringBuilder(this);
		builder.appendSuper(super.toString());
		builder.append("names", names);
		return builder.toString();
	}

	public Object clone() {
		CollectorStarterImpl copy = null;
		copy = (CollectorStarterImpl) super.clone();
		copy.names = clone(names);
		return copy;
	}
}
