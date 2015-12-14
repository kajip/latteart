package org.kajip.latteart;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class ConfigTest {

    @Test
    public void loadTest() throws Exception {

        Config config = Config.load();

        assertThat(config.queries.size(), is(1));
        assertThat(config.outputs.size(), is(1));
    }
}
