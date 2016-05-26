package org.kajip.latteart

import spock.lang.Specification

import javax.management.MalformedObjectNameException
import javax.management.ObjectName

class QueryListSpec extends Specification {

    def queryList = new QueryList(Arrays.asList(
            new Query("test:type=Object,name=01", null),
            new Query("test:type=Object,name=02", null),
            new Query("test:type=Object,name=03", null)))

    def "コンフィグファイルの読込テスト"() {
        when:
        def targetList = queryList.findTargets(
                new Directory() {
                    @Override
                    List<Target> findTarget(Query query) {
                        try {
                            return Arrays.asList(
                                    new Target(new ObjectName("test:type=Object,name=01"), null),
                                    new Target(new ObjectName("test:type=Object,name=02"), null),
                                    new Target(new ObjectName("test:type=Object,name=03"), null)
                            )
                        } catch (MalformedObjectNameException e) {
                            throw new RuntimeException(e)
                        }
                    }
                }
        )

        then:
        targetList.size() == 9
    }
}
