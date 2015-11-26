// 標準出力
appender("STDOUT", ConsoleAppender) {
    encoder(PatternLayoutEncoder) {
        pattern = "%msg%n"
    }
}

// デフォルト
root(INFO, ["STDOUT"])
