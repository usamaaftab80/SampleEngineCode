package com.project.sample.scheduler.jobs;

import java.util.Date;
import java.util.logging.Logger;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class SampleQuartzJob implements Job {
	
	private static Logger LOGGER = Logger.getLogger(SampleQuartzJob.class.getName());

	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		logJobSummary(1, 1, 1, 1, new Date(), new Date());
	}

	private void logJobSummary(int statusRecords, int expiredRecords, int notExpiredRecords, int nullDateRecords, Date start, Date finish) {		
		LOGGER.info("----- ProductionLineItemStatusCleanupJob -----");
		LOGGER.info("Job Started:          " + start);
		LOGGER.info("Expired Records:      " + expiredRecords);
		LOGGER.info("Not-expired Records:  " + notExpiredRecords);
		LOGGER.info("Non-expiring Records: " + nullDateRecords);
		LOGGER.info("======================================");
		LOGGER.info("Total Records:        " + statusRecords);
		LOGGER.info("Job Finished:         " + finish);
		LOGGER.info("--------------------------------------");
	}

}
