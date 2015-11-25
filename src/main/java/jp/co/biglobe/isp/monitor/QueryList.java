package jp.co.biglobe.isp.monitor;

import lombok.AllArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
public class QueryList {

    private final List<Query> queries;

    public int size() {
        return queries.size();
    }

    public TargetList findTargets(Directory directory) {
        return new TargetList(
                queries.parallelStream()
                        .flatMap(query -> directory.findTarget(query).stream())
                        .collect(Collectors.toList()));
    }
}
