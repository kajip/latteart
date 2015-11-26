package jp.co.biglobe.isp.monitor.spi.outbound;

import jp.co.biglobe.isp.monitor.Output;
import jp.co.biglobe.isp.monitor.OutputData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * LTSVフォーマットでファイル出力
 */
public class LoggingOutput implements Output {

    Logger  logger = LoggerFactory.getLogger(getClass());

    public void write(OutputData outputData) {
        logger.info(outputData.printLTSV());
    }
}
