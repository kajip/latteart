package jp.co.biglobe.isp.monitor;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode
public class Query {

    public final String query;

    public final String[] attributeNames;

    @JsonCreator
    public Query(@JsonProperty("query") String query, @JsonProperty("attributes") String[] attributeNames) {
        this.query = query;
        this.attributeNames = attributeNames;
    }
}
