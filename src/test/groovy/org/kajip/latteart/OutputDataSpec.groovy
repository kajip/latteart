package org.kajip.latteart

import spock.lang.Specification

import javax.management.AttributeList
import javax.management.ObjectName
import java.time.ZoneId
import java.time.ZonedDateTime

class OutputDataSpec extends Specification {

    static def zoneId = ZoneId.of("Asia/Tokyo")

    def "ナノ秒の値によってミリ秒以下の文字数は３文字固定させるテスト"() {
        when:
        def objectName = new ObjectName("test", "key", "value")
        def samplingData = new SamplingData(objectName, new AttributeList())
        def outputData = new OutputData(time, "test", samplingData)

        then:
            outputData.printLTSV() == result

        where:
            time                                                   || result
            ZonedDateTime.of(2016, 1, 1, 0, 0, 0,         0, zoneId) || "time:2016/01/01 00:00:00.000+09:00\thostname:test\tobjectName:test:key=value\tdomain:test\tkey:value"
            ZonedDateTime.of(2016, 1, 1, 0, 0, 0,         1, zoneId) || "time:2016/01/01 00:00:00.000+09:00\thostname:test\tobjectName:test:key=value\tdomain:test\tkey:value"
            ZonedDateTime.of(2016, 1, 1, 0, 0, 0,      1000, zoneId) || "time:2016/01/01 00:00:00.000+09:00\thostname:test\tobjectName:test:key=value\tdomain:test\tkey:value"
            ZonedDateTime.of(2016, 1, 1, 0, 0, 0,   1000000, zoneId) || "time:2016/01/01 00:00:00.001+09:00\thostname:test\tobjectName:test:key=value\tdomain:test\tkey:value"
            ZonedDateTime.of(2016, 1, 1, 0, 0, 0,  10000000, zoneId) || "time:2016/01/01 00:00:00.010+09:00\thostname:test\tobjectName:test:key=value\tdomain:test\tkey:value"
            ZonedDateTime.of(2016, 1, 1, 0, 0, 0, 100000000, zoneId) || "time:2016/01/01 00:00:00.100+09:00\thostname:test\tobjectName:test:key=value\tdomain:test\tkey:value"
    }
}
