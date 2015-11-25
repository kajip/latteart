package jp.co.biglobe.isp.monitor;

import lombok.AllArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
public class TargetList {

    private final List<Target> targets;

    public int size() {
        return targets.size();
    }

    public SamplingDataList sampling(Monitor monitor) {
        return new SamplingDataList(
                targets.parallelStream()
                        .map(monitor::sampling)
                        .collect(Collectors.toList()));
    }
}
