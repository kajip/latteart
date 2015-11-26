package jp.co.biglobe.isp.monitor.spi.outbound;

import java.util.Map;
import java.util.stream.Collectors;

import jp.co.biglobe.isp.monitor.Output;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoggingOutput implements Output {

    Logger  logger = LoggerFactory.getLogger(getClass());

    public void write(Map<String,Object> outputData) {

        logger.info(outputData.entrySet().stream()
                .map(entry -> String.format("%s:%s", entry.getKey(), entry.getValue()))
                .collect(Collectors.joining("\t"))
        );
    }
}
