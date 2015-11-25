package jp.co.biglobe.isp.monitor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class MonitoringTask implements Runnable {

    private final Logger logger = LoggerFactory.getLogger(getClass());


    private final JMXServer jmxServer;

    private final List<Output> outputs;

    private TargetList targetList;

    public MonitoringTask(JMXServer jmxServer, QueryList queryList, List<Output> outputs) {
        this.jmxServer = jmxServer;
        this.targetList = queryList.findTargets(jmxServer);
        this.outputs = outputs;

        logger.trace("targets size:" + targetList.size());
    }

    public void run() {
        logger.debug("start");

        SamplingDataList samplingDataList = targetList.sampling(jmxServer);
        outputs.forEach(samplingDataList::send);

        logger.debug("end");
    }
}
