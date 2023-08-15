set @jobName = '代码中定义的定时任务名称';
DELETE from qrtz_cron_triggers where TRIGGER_NAME = @jobName;
DELETE from qrtz_triggers where TRIGGER_NAME = @jobName;
DELETE from qrtz_job_details where JOB_NAME = @jobName;
DELETE from qrtz_fired_triggers where TRIGGER_NAME= @jobName;