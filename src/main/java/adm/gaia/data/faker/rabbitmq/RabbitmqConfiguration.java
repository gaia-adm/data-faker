package adm.gaia.data.faker.rabbitmq;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.rabbitmq.client.ConnectionFactory;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

/**
 * Created by tsadok on 17/06/2015.
 */
public class RabbitmqConfiguration {

    /**
     * Key that clients will use to send to send events
     */
    @JsonProperty
    private String routingKey = "event.1234.datasource.datatype";

    @JsonProperty
    private String host = "rabbitmq";//"192.168.59.103";

    //exchange name
    @JsonProperty
    private String exchangeName = "events-to-index";

    @Min(1)
    @Max(65535)
    @JsonProperty
    private int port = ConnectionFactory.DEFAULT_AMQP_PORT;

    @JsonProperty
    private String username = ConnectionFactory.DEFAULT_USER;

    @JsonProperty
    private String password = ConnectionFactory.DEFAULT_PASS;

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

    public String getRoutingKey() {
        return routingKey;
    }

    public void setRoutingKey(String routingKey)
    {
        this.routingKey = routingKey;
    }

    public String getExchangeName() {
        return exchangeName;
    }

    public void setExchangeName(String exchangeName) {
        this.exchangeName = exchangeName;
    }


}
