package jp.co.biglobe.isp.monitor;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class OutputData {

    public static final String SAMPLING_TIME_KEY = "time";

    // @caution tomcatの接続プール情報のMBean に host という値があり、上書きされる可能性があるので hostname にした。
    public static final String HOSTNAME_KEY = "hostname";

    public static final String  DOMAIN_KEY = "domain";

    public static final String  OBJECT_NAME_KEY = "objectName";

}
