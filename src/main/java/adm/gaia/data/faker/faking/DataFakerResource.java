package adm.gaia.data.faker.faking;

import adm.gaia.data.faker.DataFakerConfiguration;
import adm.gaia.data.faker.rabbitmq.RabbitmqConfiguration;
import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.Mustache;
import com.github.mustachejava.MustacheFactory;
import io.dropwizard.setup.Environment;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.io.StringReader;
import java.io.StringWriter;

/**
 * Created by tsadok on 01/07/2015.
 *
 */
@Path("/fake-data")
public class DataFakerResource {

    private DataFakerConfiguration configuration;
    private Environment env;

    static final String DEFAULT_TEMPLATE = "my_time_series,status={{status}} value=92 {{time}}";

    public DataFakerResource(DataFakerConfiguration configuration, Environment env) {
        this.configuration = configuration;
        this.env = env;
    }

    @POST
    public Response generateData(@DefaultValue("1") @QueryParam("repeat") int repeat,
                             @DefaultValue("db1") @QueryParam("dbname") String dbname,
                             @DefaultValue(MessagePublisherFactory.RABBITMQ) @QueryParam("sendto") String sendTo,
                             @QueryParam("exchangename") String exchangeName, @QueryParam("routingkey") String routingKey,
                             String jsonTemplate) throws Exception
    {
        try {
            if (jsonTemplate == null || jsonTemplate.equals(""))
                jsonTemplate = DEFAULT_TEMPLATE;

            if (exchangeName != null) {
                configuration.getRabbitmqConfiguration().setExchangeName(exchangeName);

                if (exchangeName.equals("EMPTYSTRING")) {
                    configuration.getRabbitmqConfiguration().setExchangeName("");
                }
            }

            if (routingKey != null)
                configuration.getRabbitmqConfiguration().setRoutingKey(routingKey);

            MustacheFactory mf = new DefaultMustacheFactory();
            Mustache mustache = mf.compile(new StringReader(jsonTemplate), "data-faker-compiler");



            for (int i = 0; i < repeat; i++) {
                StringWriter writer = new StringWriter();
                mustache.execute(writer, new Scope()).flush();

                MessagePublisherFactory.getPublisher(sendTo).
                        publishMessage(configuration, env, dbname, writer.toString());
            }
        }
        catch (Exception ex)
        {
            return Response.status(500).entity(ex.getMessage()).build();
        }

        return Response.status(200).build();
    }

}



