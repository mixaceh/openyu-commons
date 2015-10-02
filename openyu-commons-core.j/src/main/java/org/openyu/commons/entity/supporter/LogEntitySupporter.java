package org.openyu.commons.entity.supporter;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.openyu.commons.entity.LogEntity;
import org.openyu.commons.mark.Supporter;

@MappedSuperclass
public class LogEntitySupporter extends BaseEntitySupporter implements LogEntity, Supporter
{

	private static final long serialVersionUID = 7611438859678437079L;

	private Date logDate;

	public LogEntitySupporter()
	{
		this.logDate = new Date();
	}

	@Column(name = "log_date", updatable = false)
	public Date getLogDate()
	{
		return logDate;
	}

	public void setLogDate(Date logDate)
	{
		this.logDate = logDate;
	}

	public String toString()
	{
		ToStringBuilder builder = new ToStringBuilder(this);
		builder.appendSuper(super.toString());
		builder.append("logDate", logDate);
		return builder.toString();
	}

	public Object clone()
	{
		LogEntitySupporter copy = null;
		copy = (LogEntitySupporter) super.clone();
		copy.logDate = clone(logDate);
		return copy;
	}
}
