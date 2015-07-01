package adm.gaia.data.faker.influxdb;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

/**
 * Created by tsadok on 18/06/2015.
 */
public class InfluxDBConfiguration {

    @JsonProperty
    private String protocol = "http";

    @JsonProperty
    private String host = "influxdb";//"192.168.59.103";

    @Min(1)
    @Max(65535)
    @JsonProperty
    private int port = 8086; // To check things with Fiddler use "8888";

    @JsonProperty
    private String username = "root";

    @JsonProperty
    private String password = "root";

    public String getProtocol() { return protocol; }

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

}
