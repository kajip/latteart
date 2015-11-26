package jp.co.biglobe.isp.monitor;

import java.util.Map;

public interface Output {
    void write(Map<String,Object> outputData);
}
