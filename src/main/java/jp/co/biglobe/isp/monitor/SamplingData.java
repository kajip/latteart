package jp.co.biglobe.isp.monitor;

import lombok.AllArgsConstructor;
import lombok.ToString;

import javax.management.Attribute;
import javax.management.AttributeList;
import javax.management.ObjectName;
import javax.management.openmbean.CompositeDataSupport;
import java.util.AbstractMap;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collector;

/**
 * 抽出データ
 *
 * @todo 属性値がプリミティブ型、String型以外の場合の対処方法をどうするか
 */
@AllArgsConstructor
@ToString
public class SamplingData {

    public final ObjectName objectName;

    public final AttributeList attributes;

    /**
     * サンプリングエラー用コンストラクタ
     * @param objectName オブジェクト名
     * @param e 例外
     */
    public SamplingData(ObjectName objectName, Exception e) {
        this.objectName = objectName;
        this.attributes = new AttributeList();
        attributes.add(new Attribute("exception", e));
    }


    public Map<String,Object> asMap() {
        Map<String,Object> map = new LinkedHashMap<>();
        map.put(OutputData.OBJECT_NAME_KEY, objectName.getCanonicalName());
        map.put(OutputData.DOMAIN_KEY, objectName.getDomain());
        map.putAll(objectName.getKeyPropertyList());

        for(Attribute attribute : attributes.asList()) {
            if(attribute.getValue() instanceof  CompositeDataSupport) {
                map.putAll(parseCompositeDataSupport((CompositeDataSupport) attribute.getValue()));
            } else {
                map.put(attribute.getName(), attribute.getValue());
            }
        }

        return map;
    }

    /**
     * CompositeDataSupport を解析
     * @param compositeDataSupport
     * @return
     */
    public Map<String,Object> parseCompositeDataSupport(CompositeDataSupport compositeDataSupport) {
        return compositeDataSupport.getCompositeType().keySet().stream()
                .map(key -> new AbstractMap.SimpleImmutableEntry(key, compositeDataSupport.get(key)))
                .collect(Collector.<Map.Entry, Map>of(
                        () -> new HashMap<>(),
                        (map, entry) -> map.put(entry.getKey(), entry.getValue()),
                        (map01, map02) -> {
                            map01.putAll(map02);
                            return map01;
                        },
                        Collector.Characteristics.CONCURRENT
                ));
    }
}
