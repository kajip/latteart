package org.kajip.latteart

import spock.lang.Specification

import javax.management.AttributeList
import javax.management.ObjectName

class TargetListSpec extends Specification {

    TargetList targetList = new TargetList(Arrays.asList(
            new Target(new ObjectName("test:type=Object,name=01"), null),
            new Target(new ObjectName("test:type=Object,name=02"), null),
            new Target(new ObjectName("test:type=Object,name=03"), null)
    ))

    def "コンフィグファイルの読込テスト"() {
        when:
        def samplingDataList = targetList.sampling(new Monitor() {
            @Override
            SamplingData sampling(Target target) {
                return new SamplingData(target.objectName, new AttributeList())
            }
        })

        then:
        samplingDataList.size() == 3
    }
}
