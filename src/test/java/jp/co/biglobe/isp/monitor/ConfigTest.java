package jp.co.biglobe.isp.monitor;

import jp.co.biglobe.isp.monitor.Config;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class ConfigTest {

    @Test
    public void loadTest() throws Exception {

        Config config = Config.load();

        assertThat(config.interval, is(10L));
        assertThat(config.queries.size(), is(1));
    }
}
