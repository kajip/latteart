package jp.co.biglobe.isp.monitor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class MonitoringService {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final JMXServer jmxServer;

    private TargetList targetList;

    private final List<Output> outputs;

    public MonitoringService(JMXServer jmxServer, QueryList queryList, List<Output> outputs) {
        this.jmxServer = jmxServer;
        this.targetList = queryList.findTargets(jmxServer);
        this.outputs = outputs;

        logger.trace("targets size:" + targetList.size());
    }

    public void run() {
        logger.debug("start");

        SamplingDataList samplingDataList = targetList.sampling(jmxServer);
        OutputDataList outputDataList = samplingDataList.format();
        outputs.forEach(outputDataList::send);

        logger.debug("end");
    }

}
