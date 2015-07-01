package adm.gaia.data.faker.faking;

import adm.gaia.data.faker.DataFakerConfiguration;
import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.Mustache;
import com.github.mustachejava.MustacheFactory;
import io.dropwizard.setup.Environment;

import javax.ws.rs.*;
import java.io.StringReader;
import java.io.StringWriter;

/**
 * Created by tsadok on 01/07/2015.
 */
@Path("/fake-data")
public class DataFakerResource {

    private DataFakerConfiguration configuration;
    private Environment env;

    static final String DEFAULT_TEMPLATE = "[{\"name\":\"my_time_series\",\"points\":[[{{time}},\"{{status}}\"]],\"columns\":[\"time\", \"status\"]}]";

    public DataFakerResource(DataFakerConfiguration configuration, Environment env) {
        this.configuration = configuration;
        this.env = env;
    }

    @POST
    public void generateData(@DefaultValue("1") @QueryParam("repeat") int repeat,
                             @DefaultValue("db1") @QueryParam("dbname") String dbname,
                             @DefaultValue(MessagePublisherFactory.RABBITMQ) @QueryParam("sendto") String sendTo,
                             String jsonTemplate) throws Exception
    {
        if (jsonTemplate == null || jsonTemplate.equals(""))
            jsonTemplate = DEFAULT_TEMPLATE;

        MustacheFactory mf = new DefaultMustacheFactory();
        Mustache mustache = mf.compile(new StringReader(jsonTemplate), "data-faker-compiler");

        for (int i=0; i< repeat; i++) {
            StringWriter writer = new StringWriter();
            mustache.execute(writer, new Scope()).flush();

            MessagePublisherFactory.getPublisher(sendTo).
                        publishMessage(configuration, env, dbname, writer.toString());
        }
    }

}


