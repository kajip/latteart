package jp.co.biglobe.isp.monitor;

import java.util.List;

@FunctionalInterface
public interface Directory {
    List<Target> findTarget(Query query);
}
