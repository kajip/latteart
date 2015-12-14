package org.kajip.latteart;

import lombok.AllArgsConstructor;

import java.util.List;

/**
 * 出力データ集合
 */
@AllArgsConstructor
public class OutputDataList {

    private final List<OutputData> outputDatas;

    public int size() {
        return outputDatas.size();
    }

    public void send(Output output) {
        outputDatas.stream().forEach(output::write);
    }
}
