package org.kajip.latteart;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 抽出データ集合
 */
public class SamplingDataList {

    private final ZonedDateTime samplingTime;

    private final String hostname;

    private final List<SamplingData> samplingDatas;

    public SamplingDataList(List<SamplingData> samplingDatas) {
        this.samplingTime = ZonedDateTime.now();
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

    public OutputDataList format() {
        return new OutputDataList(samplingDatas.stream()
                .map(this::format)
                .collect(Collectors.toList()));
    }

    private OutputData format(SamplingData samplingData) {
        return new OutputData(samplingTime, hostname, samplingData);
    }
}
