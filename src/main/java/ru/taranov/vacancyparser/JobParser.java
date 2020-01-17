package ru.taranov.vacancyparser;

import org.apache.log4j.BasicConfigurator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

import java.util.List;

public class JobParser implements Job {

    private static final Logger LOG = LogManager.getLogger(JobParser.class.getName());
    private final String cron = new Config().getValue("cron.time");

    public JobParser() {
    }

    public void parse() {
        Parser parser = new Parser("https://www.sql.ru/forum/job-offers");
        DBConnect db = new DBConnect();
        List<Vacancy> vacanciesList = parser.parse();
        db.addVacancies(vacanciesList);
        db.deleteDuplicates();
    }


    public void start() {
        JobDetail jobDetail = JobBuilder.newJob(JobParser.class).build();
        Trigger trigger = TriggerBuilder.newTrigger().withIdentity("CronTrigger")
                .withSchedule(CronScheduleBuilder.cronSchedule(this.cron)).build();
        try {
            Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
            scheduler.scheduleJob(jobDetail, trigger);
            scheduler.start();
        } catch (Exception e) {
            LOG.warn(e.getMessage(), e);
        }
    }

    @Override
    public void execute(JobExecutionContext jobExecutionContext) {
        this.parse();
    }

    public static void main(String[] args) {
        BasicConfigurator.configure();
        JobParser x = new JobParser();
        x.parse();
        x.start();
    }
}
