package com.example.booklist.batch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.listener.JobExecutionListenerSupport;

/**
 * Created by ka.wada on 2017/07/01.
 */
public class JobStartEndListner extends JobExecutionListenerSupport {

    private final Logger logger = LoggerFactory.getLogger(JobStartEndListner.class);

    @Override
    public void beforeJob(JobExecution jobExecution) {
        super.beforeJob(jobExecution);
        logger.info("開始");
    }

    @Override
    public void afterJob(JobExecution jobExecution) {
        super.afterJob(jobExecution);
        logger.info("終了");
    }
}
