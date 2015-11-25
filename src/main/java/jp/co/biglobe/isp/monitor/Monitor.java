package jp.co.biglobe.isp.monitor;

@FunctionalInterface
public interface Monitor {
    SamplingData sampling(Target target);
}
