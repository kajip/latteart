package jp.co.biglobe.isp.monitor;

import java.time.Instant;

public interface Output {
    void write(Instant samplingTime, String hostname, SamplingData samplingData);
}
