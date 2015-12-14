package org.kajip.latteart;

import lombok.AllArgsConstructor;
import org.slf4j.LoggerFactory;

import javax.management.*;
import java.io.IOException;
import java.util.Arrays;
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

    public Set<MBeanAttributeInfo> findReadableAttributeInfoByObjectName(String objectName) {
        try {
            MBeanInfo mBeanInfo = mBeanServerConnection.getMBeanInfo(new ObjectName(objectName));
            return Arrays.asList(mBeanInfo.getAttributes()).stream()
                    .filter(MBeanAttributeInfo::isReadable)
                    .collect(Collectors.toSet());

        } catch (IOException e) {
            throw new RuntimeException(e);

        } catch (MalformedObjectNameException e) {
            throw new RuntimeException(e);

        } catch (IntrospectionException e) {
            throw new RuntimeException(e);

        } catch (ReflectionException e) {
            throw new RuntimeException(e);

        } catch (InstanceNotFoundException e) {
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
