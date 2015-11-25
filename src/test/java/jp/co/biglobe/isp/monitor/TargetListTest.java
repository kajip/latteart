package jp.co.biglobe.isp.monitor;

import org.junit.Test;

import javax.management.AttributeList;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import java.util.Arrays;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

public class TargetListTest {

    TargetList targetList;

    public TargetListTest() throws MalformedObjectNameException {
        targetList = new TargetList(Arrays.asList(
                new Target(new ObjectName("test:type=Object,name=01"), null),
                new Target(new ObjectName("test:type=Object,name=02"), null),
                new Target(new ObjectName("test:type=Object,name=03"), null)
        ));
    }

    @Test
    public void sampling() throws Exception {

        SamplingDataList samplingDataList =
            targetList.sampling(target -> new SamplingData(target.objectName, new AttributeList()));

        assertThat(samplingDataList.size(), is(3));

    }
}
