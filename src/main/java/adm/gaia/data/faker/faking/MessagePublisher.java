package adm.gaia.data.faker.faking;

import adm.gaia.data.faker.DataFakerConfiguration;
import io.dropwizard.setup.Environment;

/**
 * Created by tsadok on 01/07/2015.
 */
public interface MessagePublisher {

    public void publishMessage(DataFakerConfiguration configuration, Environment env, String dbname, String message) throws Exception;

}
