package jp.co.biglobe.isp.monitor;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.List;

public class Config {

    public final long interval;

    public final QueryList queries;


    public static Config load(URL url) throws IOException {
        try(InputStream inputStream = url.openStream()) {
            return _load(inputStream);
        }
    }

    public static Config load() throws IOException {
        try(InputStream inputStream = ClassLoader.getSystemClassLoader().getResourceAsStream("default.json")) {
            return _load(inputStream);
        }
    }

    private static Config _load(InputStream inputStream) throws IOException {
        try(BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream)) {

            ObjectMapper objectMapper = new ObjectMapper()
                    // JSON にコメントが使えるようにする
                    .enable(JsonParser.Feature.ALLOW_COMMENTS)
                    .enable(JsonParser.Feature.ALLOW_YAML_COMMENTS);

            return objectMapper.readValue(bufferedInputStream, new TypeReference<Config>() {});
        }
    }

    @JsonCreator
    public Config(
            @JsonProperty(value = "interval", required = true) long interval,
            @JsonProperty(value = "queries", required = true) List<Query> queries) {
        this.interval = interval;
        this.queries  = new QueryList(queries);
    }
}
