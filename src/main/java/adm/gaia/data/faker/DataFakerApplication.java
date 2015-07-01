package adm.gaia.data.faker;

import adm.gaia.data.faker.faking.DataFakerResource;
import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

/**
 * Created by tsadok on 14/06/2015.
 */
public class DataFakerApplication extends Application<DataFakerConfiguration>{


    public static void main(String[] args) throws Exception {
        new DataFakerApplication().run(args);
    }

    @Override
    public String getName() {
        return "adm-gaia-data-faker";
    }

    @Override
    public void initialize(Bootstrap<DataFakerConfiguration> bootstrap) {

    }

    @Override
    public void run(DataFakerConfiguration configuration,
                    Environment environment)  throws Exception
    {
        environment.jersey().register(new DataFakerResource(configuration, environment));
    }
}






