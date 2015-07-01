package adm.gaia.data.faker.facking;

import adm.gaia.data.faker.influxdb.InfluxDBManager;
import adm.gaia.data.faker.rabbitmq.RabbitmqManager;

/**
 * Created by tsadok on 01/07/2015.
 */
public class MessagePublisherFactory {

    public static final String RABBITMQ = "rabbitmq";
    public static final String INFLUXDB = "influxdb";

    public static MessagePublisher getPublisher(String sendTo)
    {
        if (sendTo.equals(INFLUXDB))
            return InfluxDBManager.getInstance();

        if (sendTo.equals(RABBITMQ))
            return RabbitmqManager.getInstance();

        throw new RuntimeException("unknown sendTo: " + sendTo);
    }
}
