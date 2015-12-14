package org.kajip.latteart;

import org.kajip.latteart.spi.jmx_server_builder.LocalJMXServerBuilder;

public interface JMXServerBuilder {

    static JMXServerBuilder getInstance(String args[]) {
        return new LocalJMXServerBuilder(args[0]);
    }

    JMXServer createJMXServer();
}
