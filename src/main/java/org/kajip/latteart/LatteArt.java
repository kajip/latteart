package org.kajip.latteart;

import org.apache.commons.cli.*;

import javax.management.ObjectName;
import java.util.Optional;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class LatteArt {

    private static final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    public static void main(String args[]) throws Exception {

        // コマンドラインパラメータの解析
        CommandLine cl = parseCommandLine(args);

        // JMXServer に接続
        JMXServerBuilder jmxServerBuilder = JMXServerBuilder.getInstance(cl.getArgs());
        JMXServer jmxServer = jmxServerBuilder.createJMXServer();

        // ObjectNameリストの表示
        if (cl.hasOption('l')) {
            printObjectNameList(jmxServer);
        }

        // 参照可能属性リストの表示
        Optional.ofNullable(cl.getOptionValue('a'))
                .ifPresent(objectName -> printReadableAttributeList(jmxServer, objectName));

        // 設定ファイルのパスを取得
        Config config = Optional.ofNullable(cl.getOptionValue("c"))
                .map(Config::load)
                .orElse(Config.load());

        // モニタリングサービス生成
        MonitoringService monitoringService = new MonitoringService(jmxServer, config.queries, config.outputs);

        // 定期実行オプションの取得
        Optional<Long> interval = Optional.ofNullable(cl.getOptionValue("i")).map(Long::parseLong);

        // モニタリング開始
        if (interval.isPresent()) {
            scheduler.scheduleAtFixedRate(() -> monitoringService.run(), 0L, interval.get(), TimeUnit.SECONDS);
        } else {
            monitoringService.run();
        }
    }

    private static CommandLine parseCommandLine(String[] args) throws Exception {

        Options options = new Options();
        options.addOption("h", "help", false, "print this message.");
        options.addOption("l", "list", false, "print all MBean name list");
        options.addOption("a", "attr", true, "print all readable attribute name list");
        options.addOption("c", "config", true, "config json file url.");
        options.addOption("i", "interval", true, "interval second");

        DefaultParser defaultParser = new DefaultParser();
        CommandLine cl = defaultParser.parse(options, args, true);

        // help情報の表示
        if (cl.hasOption('h')) {
            printHelp(options);
        }

        // プロセスID未指定ならhelp情報を表示して終了
        if (cl.getArgs().length <= 0) {
            printHelp(options);
        }

        return cl;
    }

    private static void printHelp(Options options) {
        HelpFormatter helpFormatter = new HelpFormatter();
        helpFormatter.printHelp("latteart [Options ...] pid", options);

        System.exit(0);
    }

    private static void printObjectNameList(JMXServer jmxServer) {
        jmxServer.findAllObjectName().stream()
                .map(ObjectName::getCanonicalName)
                .sorted()
                .forEach(System.out::println);

        System.exit(0);
    }


    private static void printReadableAttributeList(JMXServer jmxServer, String objectName) {
        jmxServer.findReadableAttributeInfoByObjectName(objectName).stream()
                .map(attributeInfo -> String.format("%s <type:%s>", attributeInfo.getName(), attributeInfo.getType()))
                .sorted()
                .forEach(System.out::println);

        System.exit(0);
    }
}
