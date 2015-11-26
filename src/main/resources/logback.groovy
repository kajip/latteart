// 標準出力
appender("STDOUT", ConsoleAppender) {
    encoder(PatternLayoutEncoder) {
        pattern = "%d %level %thread %mdc %logger - %m%n"
    }
}

// ファイル出力
def WEBAPP_DIR = "./build"

appender("FILE", RollingFileAppender) {
    encoder(PatternLayoutEncoder) {
        Pattern = "%m%n"
    }
    rollingPolicy(TimeBasedRollingPolicy) {
        FileNamePattern = "${WEBAPP_DIR}/log/translator-%d{yyyy-MM}.zip"
    }
}
// デフォルト
root(WARN, ["STDOUT"])


logger("jp.co.biglobe.isp.monitor.spi.outbound.LoggingOutput", INFO, ["FILE"], false)