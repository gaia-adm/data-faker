package adm.gaia.data.faker.rabbitmq;

import adm.gaia.data.faker.DataFakerConfiguration;
import adm.gaia.data.faker.facking.MessagePublisher;
import adm.gaia.data.faker.facking.Scope;
import com.rabbitmq.client.*;
import io.dropwizard.lifecycle.Managed;
import io.dropwizard.setup.Environment;

import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Taking care for the setup and tire down of RabbitMQ connections
 *
 * Created by tsadok on 17/06/2015.
 */
public class RabbitmqManager implements MessagePublisher {

    private static RabbitmqManager _instance = new RabbitmqManager();
    private Connection connection;
    private boolean init = false;

    private RabbitmqManager() {
    }

    public static RabbitmqManager getInstance()
    {
        return _instance;
    }

    @Override
    public void publishMessage(DataFakerConfiguration configuration, Environment env, String dbname, String message) throws Exception
    {
       Connection conn = getConnection(configuration);
       Channel producerChannel = conn.createChannel();

       AMQP.BasicProperties.Builder propsBuilder = new AMQP.BasicProperties.Builder();
       Map map = new HashMap<String,Object>();
       map.put("dbname", dbname);
       propsBuilder.headers(map);

       try {
           producerChannel.basicPublish("", configuration.getRabbitmqConfiguration().getRoutingKey(), propsBuilder.build(), message.getBytes());
           System.out.println("[x] Sent '" + message + "' to rabbitmq");
       }
       catch (IOException ex)
       {
           System.err.println("Failed to send to Rabbitmq, why: " + ex.getMessage());
       }

    }

    private Connection getConnection(DataFakerConfiguration configuration) throws Exception {

        Lock lock = new ReentrantLock();
        lock.lock();
        if (connection == null) {
            ConnectionFactory factory = new ConnectionFactory();
            factory.setHost(configuration.getRabbitmqConfiguration().getHost());
            factory.setPort(configuration.getRabbitmqConfiguration().getPort());
            factory.setUsername(configuration.getRabbitmqConfiguration().getUsername());
            factory.setPassword(configuration.getRabbitmqConfiguration().getPassword());

            factory.setAutomaticRecoveryEnabled(true); // connection that will recover automatically
            factory.setNetworkRecoveryInterval(10000); // attempt recovery to the max of 10 seconds

            // Setting heartbeat to 30 sec instead the default of 10 min
            // that way the client will know that server is unreachable
            // and will try to reconnect
            // The same is true for the server (the broker) - after 30 sec
            // he will close the consumers.
            factory.setRequestedHeartbeat(30);

            connection = factory.newConnection();
        }
        lock.unlock();

        return connection;
    }
}
