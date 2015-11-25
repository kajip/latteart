package jp.co.biglobe.isp.monitor.spi.jmx_server_builder;

import com.sun.tools.attach.AttachNotSupportedException;
import com.sun.tools.attach.AttachOperationFailedException;
import com.sun.tools.attach.VirtualMachine;
import jp.co.biglobe.isp.monitor.JMXServer;
import jp.co.biglobe.isp.monitor.JMXServerBuilder;

import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;
import java.io.IOException;
import java.util.Optional;

public class LocalJMXServerBuilder implements JMXServerBuilder {

    private final String pid;

    public LocalJMXServerBuilder(String pid) {
        this.pid = pid;
    }

    @Override
    public JMXServer createJMXServer() {

        VirtualMachine virtualMachine = null;
        try {
            virtualMachine = VirtualMachine.attach(pid);
            String connectorAddress = virtualMachine.startLocalManagementAgent();

            JMXServiceURL jmxServiceURL = new JMXServiceURL(connectorAddress);
            return Optional.ofNullable(JMXConnectorFactory.connect(jmxServiceURL))
                    .map(jmxConnector -> {
                        try {
                            return jmxConnector.getMBeanServerConnection();
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    })
                    .map(JMXServer::new)
                    .get();

        } catch (AttachNotSupportedException | AttachOperationFailedException e) {
            throw new RuntimeException(e);

        } catch (IOException e) {
            throw new RuntimeException(e);

        } finally {
            if (virtualMachine != null) {
                try {
                    virtualMachine.detach();
                } catch (IOException e) {
                }
            }
        }
    }
}
