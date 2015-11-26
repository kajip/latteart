package jp.co.biglobe.isp.monitor;

import lombok.AllArgsConstructor;
import lombok.ToString;

import javax.management.Attribute;
import javax.management.AttributeList;
import javax.management.ObjectName;
import java.util.LinkedHashMap;
import java.util.Map;

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
        map.put("objectName", objectName.getCanonicalName());
        map.put("domain", objectName.getDomain());
        map.putAll(objectName.getKeyPropertyList());

        for(Attribute attribute : attributes.asList()) {
            map.put(attribute.getName(), attribute.getValue());
        }

        return map;
    }
}
