package jp.co.biglobe.isp.monitor.spi.jmx_server_builder;

import com.sun.tools.attach.AgentInitializationException;
import com.sun.tools.attach.AgentLoadException;
import com.sun.tools.attach.AttachNotSupportedException;
import com.sun.tools.attach.VirtualMachine;
import jp.co.biglobe.isp.monitor.JMXServer;
import jp.co.biglobe.isp.monitor.JMXServerBuilder;

import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;
import java.io.File;
import java.io.IOException;
import java.util.Optional;
import java.util.Properties;

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

            String connectorAddress = getConnectorAddress(virtualMachine);

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

        } catch (AttachNotSupportedException e) {
            throw new RuntimeException(e);

        } catch (IOException e) {
            throw new RuntimeException(e);

        } finally {
            if (virtualMachine != null) {
            }
        }
    }

    final static String LOCAL_CONNECTOR_ADDR = "com.sun.management.jmxremote.localConnectorAddress";

    private String getConnectorAddress(VirtualMachine virtualMachine) {
        try {
            String connectorAddress = virtualMachine.getAgentProperties().getProperty(LOCAL_CONNECTOR_ADDR);

            if (connectorAddress == null) {
                connectorAddress = loadAgent(virtualMachine);
            }

            return connectorAddress;

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private String loadAgent(VirtualMachine virtualMachine) {
        try {
            Properties systemProperties = virtualMachine.getSystemProperties();

            // 管理エージェントのパス取得
            String javaHome = systemProperties.getProperty("java.home");
            String agent = String.join(File.separator, new String[]{javaHome, "lib", "management-agent.jar"});

            // 管理エージェントの読込
            virtualMachine.loadAgent(agent);

            return virtualMachine.getAgentProperties().getProperty(LOCAL_CONNECTOR_ADDR);

        } catch (IOException e) {
            throw new RuntimeException(e);

        } catch (AgentLoadException e) {
            throw new RuntimeException(e);

        } catch (AgentInitializationException e) {
            throw new RuntimeException(e);
        }
    }
}
