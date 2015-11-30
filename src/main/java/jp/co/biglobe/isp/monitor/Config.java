package jp.co.biglobe.isp.monitor;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jp.co.biglobe.isp.monitor.spi.outbound.LoggingOutput;
import org.slf4j.LoggerFactory;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown=true)
public class Config {

    public final QueryList queries;

    public final List<Output> outputs;

    public static Config load(String url) {
        try(InputStream inputStream = new URL(url).openStream()) {
            return _load(inputStream);

        } catch (MalformedURLException e) {
            LoggerFactory.getLogger(Config.class).warn(e.getMessage(), e);
            return load();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static Config load() {
        try(InputStream inputStream = ClassLoader.getSystemClassLoader().getResourceAsStream("monitor.json")) {
            return _load(inputStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static Config _load(InputStream inputStream) throws IOException {
        try(BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream)) {

            ObjectMapper objectMapper = new ObjectMapper()
                    // JSON にコメントが使えるようにする
                    .enable(JsonParser.Feature.ALLOW_COMMENTS)
                    .enable(JsonParser.Feature.ALLOW_YAML_COMMENTS);

            return objectMapper.readValue(bufferedInputStream, new TypeReference<Config>() {
            });
        }
    }

    @JsonCreator
    public Config(@JsonProperty(value = "queries", required = true) List<Query> queries) {
        this.queries = new QueryList(queries);

        // 出力先を取得
        // @todo 出力先の取得方法（Spring Boot使うか。）
        this.outputs = Arrays.asList(new LoggingOutput());
    }
}
