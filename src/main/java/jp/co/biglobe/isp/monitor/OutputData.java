package jp.co.biglobe.isp.monitor;

import java.time.Instant;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;


public class OutputData {

    public static final String SAMPLING_TIME_KEY = "time";

    // @caution tomcatの接続プール情報のMBean に host という値があり、上書きされる可能性があるので hostname にした。
    public static final String HOSTNAME_KEY = "hostname";

    public static final String  DOMAIN_KEY = "domain";

    public static final String  OBJECT_NAME_KEY = "objectName";

    private final Map<String,Object>  outputData;


    public OutputData(Instant samplingTime, String hostname, SamplingData samplingData) {
        Map<String,Object> map = new LinkedHashMap<>();
        map.put(SAMPLING_TIME_KEY, samplingTime);
        map.put(HOSTNAME_KEY, hostname);
        map.putAll(samplingData.asMap());

        this.outputData = Collections.unmodifiableMap(map);
    }


    public String printLTSV() {
        return outputData.entrySet().stream()
                .map(entry -> String.format("%s:%s", entry.getKey(), entry.getValue()))
                .collect(Collectors.joining("\t"));
    }
}
