package eu.sn.scheduler;

import eu.sn.model.MessagingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class Scheduler {

    @Autowired
    MessagingService messagingService;

    Logger logger = LoggerFactory.getLogger(Scheduler.class);

    @Value("${count:1}")
    Integer count;

    @Scheduled(fixedDelayString = "${scheduler:5000}")
    public void scheduledJob() {
        logger.info("Scheduled job started");

        messagingService.sendMessage(count);
        logger.info("Scheduler job finished");
    }
}
