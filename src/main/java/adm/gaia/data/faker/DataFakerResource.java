package adm.gaia.data.faker;

import adm.gaia.data.faker.facking.Scope;
import adm.gaia.data.faker.rabbitmq.RabbitmqManager;
import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.Mustache;
import com.github.mustachejava.MustacheFactory;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;

import javax.ws.rs.*;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by tsadok on 01/07/2015.
 */
@Path("/fake-data")
public class DataFakerResource {

    private DataFakerConfiguration configuration;
    static final String RABBITMQ = "rabbitmq";
    static final String INFLUXDB = "influxdb";

    public DataFakerResource(DataFakerConfiguration configuration) {
        this.configuration = configuration;
    }

    @POST
    public void generateData(@DefaultValue("1") @QueryParam("repeat") int repeat,
                             @DefaultValue("db1") @QueryParam("dbname") String dbname,
                             @DefaultValue(RABBITMQ) @QueryParam("sendto") String sendTo,
                             String jsonTemplate) throws Exception
    {
        MustacheFactory mf = new DefaultMustacheFactory();
        Mustache mustache = mf.compile(new StringReader(jsonTemplate), "example");

        for (int i=0; i< repeat; i++) {
            StringWriter writer = new StringWriter();
            mustache.execute(writer, new Scope()).flush();

            if (sendTo.equals(RABBITMQ))
                RabbitmqManager.getInstance().publishMessage(configuration, dbname, writer.toString());
        }
    }

}



