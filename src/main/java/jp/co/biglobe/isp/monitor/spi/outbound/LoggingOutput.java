package jp.co.biglobe.isp.monitor.spi.outbound;

import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

import jp.co.biglobe.isp.monitor.Output;
import jp.co.biglobe.isp.monitor.SamplingData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoggingOutput implements Output {

    Logger  logger = LoggerFactory.getLogger(getClass());

    public void write(Instant samplingTime, String hostname, SamplingData samplingData) {

        Map<String,Object> map = new LinkedHashMap<>();
        map.put("samplingTime", samplingTime.toEpochMilli());
        map.put("hostname", hostname);
        map.putAll(samplingData.asMap());

        logger.info(map.entrySet().stream()
                .map(entry -> String.format("%s:%s", entry.getKey(), entry.getValue()))
                .collect(Collectors.joining("\t"))
        );
    }
}
