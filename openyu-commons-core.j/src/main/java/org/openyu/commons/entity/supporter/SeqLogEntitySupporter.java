package org.openyu.commons.entity.supporter;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.hibernate.search.annotations.DateBridge;
import org.hibernate.search.annotations.Analyze;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Index;
import org.hibernate.search.annotations.Resolution;
import org.hibernate.search.annotations.Store;
import org.openyu.commons.entity.SeqLogEntity;
import org.openyu.commons.entity.LogEntity;
import org.openyu.commons.mark.Supporter;

@MappedSuperclass
public abstract class SeqLogEntitySupporter extends SeqEntitySupporter implements SeqLogEntity, Supporter {

	private static final long serialVersionUID = -6426960474200705837L;

	private LogEntity log = new LogEntitySupporter();

	public SeqLogEntitySupporter() {
	}

	@Column(name = "log_date", updatable = false)
	@Field(store = Store.YES, index = Index.YES, analyze = Analyze.NO)
	@DateBridge(resolution = Resolution.DAY)
	public Date getLogDate() {
		return log.getLogDate();
	}

	public void setLogDate(Date logDate) {
		this.log.setLogDate(logDate);
	}

	public String toString() {
		ToStringBuilder builder = new ToStringBuilder(this);
		builder.appendSuper(super.toString());
		builder.append("logDate", (log != null ? getLogDate() : null));
		return builder.toString();
	}

	public Object clone() {
		SeqLogEntitySupporter copy = null;
		copy = (SeqLogEntitySupporter) super.clone();
		copy.log = clone(log);
		return copy;
	}
}
