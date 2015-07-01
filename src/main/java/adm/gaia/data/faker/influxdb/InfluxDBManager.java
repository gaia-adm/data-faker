package adm.gaia.data.faker.influxdb;

import adm.gaia.data.faker.DataFakerConfiguration;
import adm.gaia.data.faker.faking.MessagePublisher;
import io.dropwizard.client.JerseyClientBuilder;
import io.dropwizard.client.JerseyClientConfiguration;
import io.dropwizard.setup.Environment;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by tsadok on 18/06/2015.
 */
public class InfluxDBManager implements MessagePublisher {

    private static InfluxDBManager _instance = new InfluxDBManager();

    private String influxDbBaseUrl;
    private String influxDbQueryParams;
    private Client jerseyClient;

    private InfluxDBManager() {
    }

    public static InfluxDBManager getInstance()
    {
        return _instance;
    }

    @Override
    public void publishMessage(DataFakerConfiguration configuration, Environment env, String dbname, String message)
    {
        init(configuration, env);

        WebTarget webTarget = jerseyClient.target(influxDbBaseUrl + dbname + "/series" + influxDbQueryParams);
        Response response = webTarget.request(MediaType.APPLICATION_JSON_TYPE).
                post(Entity.entity(message, MediaType.APPLICATION_JSON_TYPE));

        if (response.getStatus() == 200) {
            System.out.println("[x] Sent '" + message + "' to influxdb");
        } else
        {
            System.err.println("Failed to post to InfluxDB: " + response.toString());
            throw new RuntimeException("Failed to post to InfluxDB: " + response.toString());
        }
    }

    private void init(DataFakerConfiguration configuration, Environment env)
    {
        Lock lock = new ReentrantLock();
        lock.lock();
        if (jerseyClient == null) {
            JerseyClientConfiguration jerseyClientConfiguration = new JerseyClientConfiguration();
            jerseyClient = new JerseyClientBuilder(env).using(jerseyClientConfiguration).
                    build("influxdb-jersey-client");

            InfluxDBConfiguration conf = configuration.getInfluxDBConfiguration();
            StringBuilder baseBuilder = new StringBuilder();
            influxDbBaseUrl = baseBuilder.append(conf.getProtocol()).append("://").append(conf.getHost()).append(":").
                    append(conf.getPort()).append("/db/").toString();

            StringBuilder paramsBuilder = new StringBuilder();
            influxDbQueryParams = paramsBuilder.append("?u=").append(conf.getUsername()).append("&p=").append(conf.getPassword()).toString();
        }
        lock.unlock();
    }
}
