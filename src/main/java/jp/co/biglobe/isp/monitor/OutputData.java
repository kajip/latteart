package jp.co.biglobe.isp.monitor;

import lombok.AllArgsConstructor;

import java.util.Map;
import java.util.stream.Collectors;

@AllArgsConstructor
public class OutputData {

    public static final String SAMPLING_TIME_KEY = "time";

    // @caution tomcatの接続プール情報のMBean に host という値があり、上書きされる可能性があるので hostname にした。
    public static final String HOSTNAME_KEY = "hostname";

    public static final String  DOMAIN_KEY = "domain";

    public static final String  OBJECT_NAME_KEY = "objectName";

    private final Map<String,Object>  outputData;

    public String printLTSV() {
        return outputData.entrySet().stream()
                .map(entry -> String.format("%s:%s", entry.getKey(), entry.getValue()))
                .collect(Collectors.joining("\t"));
    }
}
