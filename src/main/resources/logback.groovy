
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
        FileNamePattern = "${WEBAPP_DIR}/log/java-monitor-%d{yyyy-MM-dd}.gz"
        maxHistory = 30
    }
}
// デフォルト
root(WARN, ["STDOUT"])


logger("org.kajip.latteart.spi.outbound.LoggingOutput", INFO, ["FILE"], false)