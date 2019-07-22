package cn.edu.zsc.rms.schedule.job;

import cn.edu.zsc.rms.schedule.ScheduleUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * @author pengzheng
 */
@Component
public class SimulationJob {

    private static final Logger log = LoggerFactory.getLogger(SimulationJob.class);

    private static final String METHOD_KEY_LOG = "simulationJob-log";

    public void log() {
        ScheduleUtils.progressPercentage.put(METHOD_KEY_LOG, 0.0);
        log.warn("simulation job ...");
        ScheduleUtils.progressPercentage.put(METHOD_KEY_LOG, 100.0);
    }
}
