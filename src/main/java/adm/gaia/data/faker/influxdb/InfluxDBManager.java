package adm.gaia.data.faker.influxdb;

import adm.gaia.data.faker.DataFakerConfiguration;
import io.dropwizard.lifecycle.Managed;
import io.dropwizard.setup.Environment;

import javax.ws.rs.client.Client;

/**
 * Created by tsadok on 18/06/2015.
 */
public class InfluxDBManager implements Managed {

    DataFakerConfiguration configuration;
    String influxDbBaseUrl;
    String influxDbQueryParams;
    Environment environment;
    Client jerseyClient;

    public InfluxDBManager(DataFakerConfiguration dataFakerConfiguration, Environment environment) {
        this.configuration = dataFakerConfiguration;
        this.environment = environment;

        InfluxDBConfiguration conf = dataFakerConfiguration.getInfluxDBConfiguration();
        StringBuilder baseBuilder = new StringBuilder();
        influxDbBaseUrl = baseBuilder.append(conf.getProtocol()).append("://").append(conf.getHost()).append(":").
                append(conf.getPort()).append("/db/").toString();

        StringBuilder paramsBuilder = new StringBuilder();
        influxDbQueryParams = paramsBuilder.append("?u=").append(conf.getUsername()).append("&p=").append(conf.getPassword()).toString();

    }

    public Client getJerseyClient() {
        return jerseyClient;
    }

    public String getInfluxDbBaseUrl() {
        return influxDbBaseUrl;
    }

    public String getInfluxDbQueryParams() {
        return influxDbQueryParams;
    }

    @Override
    public void start() throws Exception {


        /*JerseyClientConfiguration jerseyClientConfiguration = new JerseyClientConfiguration();
        jerseyClient = new JerseyClientBuilder(environment).using(jerseyClientConfiguration).
                build("influxdb-jersey-client"); */
    }

    @Override
    public void stop() throws Exception {
        if (jerseyClient != null)
            jerseyClient.close();
    }
}
