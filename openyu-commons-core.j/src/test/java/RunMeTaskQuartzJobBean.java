import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

public class RunMeTaskQuartzJobBean extends QuartzJobBean {

	protected void executeInternal(JobExecutionContext ctx)
			throws JobExecutionException {
		System.out.println("Spring 3 + Quartz 1.8.6 ~");
	}
}
