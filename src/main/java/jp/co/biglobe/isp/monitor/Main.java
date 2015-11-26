package jp.co.biglobe.isp.monitor;

import jp.co.biglobe.isp.monitor.spi.outbound.LoggingOutput;
import org.apache.commons.cli.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


public class Main {

    private static final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    public static void main(String args[]) throws Exception {

        // コマンドラインパラメータの解析
        CommandLine cl = parseCommandLine(args);

        // 設定ファイルのパスを取得
        Config config = Optional.ofNullable(cl.getOptionValue("c"))
                .map(Config::load)
                .orElse(Config.load());

        // JMXServer に接続
        JMXServerBuilder jmxServerBuilder = JMXServerBuilder.getInstance(cl.getArgs());
        JMXServer jmxServer = jmxServerBuilder.createJMXServer();

        // 出力先を取得
        // @todo 出力先の取得方法（Spring Boot使うか。）
        List<Output> outputs = Arrays.asList(
                new LoggingOutput()
        );

        // モニタリング開始
        MonitoringTask monitor = new MonitoringTask(jmxServer, config.queries, outputs);
        scheduler.scheduleAtFixedRate(monitor, 0L, config.interval, TimeUnit.SECONDS);
    }


    private static CommandLine parseCommandLine(String[] args) throws Exception {

        Options options = new Options();
        options.addOption("h", "help", false, "print this message.");
        options.addOption("c", "config", true, "config json file url.");

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
        helpFormatter.printHelp("java-monitor [Options ...] pid", options);
        System.exit(0);
    }
}
