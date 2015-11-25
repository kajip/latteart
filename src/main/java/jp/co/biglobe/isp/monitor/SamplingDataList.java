package jp.co.biglobe.isp.monitor;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.time.Instant;
import java.util.List;

/**
 * 抽出データ集合
 */
public class SamplingDataList {

    private final Instant samplingTime;

    private final String hostname;

    private final List<SamplingData> samplingDatas;

    public SamplingDataList(List<SamplingData> samplingDatas) {
        this.samplingTime = Instant.now();
        this.hostname = lookupHostname();
        this.samplingDatas = samplingDatas;
    }

    private String lookupHostname() {
        try {
            return InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException e) {
            return "<<unknown>>";
        }
    }

    public int size() {
        return samplingDatas.size();
    }

    public void send(Output output) {
        samplingDatas.forEach(samplingData -> output.write(samplingTime, hostname, samplingData));
    }
}
