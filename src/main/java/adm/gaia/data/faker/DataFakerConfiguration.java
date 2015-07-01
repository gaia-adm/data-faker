package adm.gaia.data.faker;

import adm.gaia.data.faker.influxdb.InfluxDBConfiguration;
import adm.gaia.data.faker.rabbitmq.RabbitmqConfiguration;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.Configuration;

/**
 * Created by tsadok on 14/06/2015.
 */
public class DataFakerConfiguration extends Configuration{

    @JsonProperty("rabbitmq")
    private RabbitmqConfiguration rabbitmqConfiguration = new RabbitmqConfiguration();

    @JsonProperty("influxdb")
    private InfluxDBConfiguration influxDBConfiguration = new InfluxDBConfiguration();


    public RabbitmqConfiguration getRabbitmqConfiguration() {
        return rabbitmqConfiguration;
    }

    public void setRabbitmqConfiguration(RabbitmqConfiguration rabbitmqConfiguration) {
        this.rabbitmqConfiguration = rabbitmqConfiguration;
    }


    public InfluxDBConfiguration getInfluxDBConfiguration() {
        return influxDBConfiguration;
    }

    public void setInfluxDBConfiguration(InfluxDBConfiguration influxDBConfiguration) {
        this.influxDBConfiguration = influxDBConfiguration;
    }
}