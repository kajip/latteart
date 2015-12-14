package org.kajip.latteart;

import java.util.List;

@FunctionalInterface
public interface Directory {
    List<Target> findTarget(Query query);
}
