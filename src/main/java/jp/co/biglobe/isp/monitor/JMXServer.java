package jp.co.biglobe.isp.monitor;

import lombok.AllArgsConstructor;
import org.slf4j.LoggerFactory;

import javax.management.*;
import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@AllArgsConstructor
public class JMXServer implements Directory,Monitor {

    private final MBeanServerConnection mBeanServerConnection;

    public Set<ObjectName> findAllObjectName() {
        try {
            return mBeanServerConnection.queryNames(new ObjectName("*:*"), null);

        } catch (IOException e) {
            throw new RuntimeException(e);

        } catch (MalformedObjectNameException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Target> findTarget(Query query) {
        try {
            LoggerFactory.getLogger(getClass()).info("query: " + query.query);
            Set<ObjectName> on = mBeanServerConnection.queryNames(new ObjectName(query.query), null);
            LoggerFactory.getLogger(getClass()).info("size: " + on.size());

            return mBeanServerConnection.queryNames(new ObjectName(query.query), null)
                    .parallelStream()
                    .map(objectName -> new Target(objectName, query.attributeNames))
                    .collect(Collectors.toList());

        } catch (IOException e) {
            throw new RuntimeException(e);

        } catch (MalformedObjectNameException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public SamplingData sampling(Target target) {
        try {
            AttributeList attributes =
                mBeanServerConnection.getAttributes(target.objectName, target.attributeNames);
            return new SamplingData(target.objectName, attributes);

        } catch (InstanceNotFoundException e) {
            return new SamplingData(target.objectName, e);

        } catch (ReflectionException e) {
            return new SamplingData(target.objectName, e);

        } catch (IOException e) {
            // JMXServerとの通信エラーなので例外を投げる
            throw new RuntimeException(e);
        }
    }
}
