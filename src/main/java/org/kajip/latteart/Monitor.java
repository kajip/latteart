package org.kajip.latteart;

@FunctionalInterface
public interface Monitor {
    SamplingData sampling(Target target);
}
