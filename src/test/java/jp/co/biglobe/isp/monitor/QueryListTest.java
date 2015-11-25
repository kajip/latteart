package jp.co.biglobe.isp.monitor;

import jp.co.biglobe.isp.monitor.Query;
import jp.co.biglobe.isp.monitor.QueryList;
import jp.co.biglobe.isp.monitor.Target;
import jp.co.biglobe.isp.monitor.TargetList;
import org.junit.Test;

import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import java.util.Arrays;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class QueryListTest {

    QueryList queryList = new QueryList(Arrays.asList(
                        new Query("test:type=Object,name=01", null),
                        new Query("test:type=Object,name=02", null),
                        new Query("test:type=Object,name=03", null)));

    @Test
    public void findTargetsTest() throws Exception {

        TargetList targetList = queryList.findTargets(target -> {
            try {
                return Arrays.asList(
                        new Target(new ObjectName("test:type=Object,name=01"), null),
                        new Target(new ObjectName("test:type=Object,name=02"), null),
                        new Target(new ObjectName("test:type=Object,name=03"), null)
                );
            } catch (MalformedObjectNameException e) {
                throw new RuntimeException(e);
            }
        });

        assertThat(targetList.size(), is(9));

    }
}
