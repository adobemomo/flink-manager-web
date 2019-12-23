package ac.cn.iie.schedule;

import ac.cn.iie.service.InfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class FlinkUptodateSchedule {
    private final InfoService infoService;

    public FlinkUptodateSchedule(InfoService infoService) {
        this.infoService = infoService;
    }

    @Scheduled(fixedRate = 60000)
    public void updateInfoWithFlink() {
        log.info("Update info with Flink.");
        infoService.updateInfo();
    }
}
