package org.kajip.latteart

import spock.lang.Specification

import javax.management.AttributeList
import javax.management.ObjectName
import java.time.ZoneId
import java.time.ZonedDateTime

class ConfigSpec extends Specification {

    def "コンフィグファイルの読込テスト"() {
        when:
        def config = Config.load()

        then:
        config.queries.size() == 1
        config.outputs.size() == 1
    }
}
