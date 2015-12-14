package org.kajip.latteart.spi.outbound;

import org.kajip.latteart.Output;
import org.kajip.latteart.OutputData;
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
