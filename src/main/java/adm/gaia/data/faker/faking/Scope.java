package adm.gaia.data.faker.faking;

import java.util.Random;

/**
 * Created by tsadok on 01/07/2015.
 */
public class Scope {

    static final String[] STATUS_ARR = {"'new'", "'in progress'", "'done'"};
    public String getStatus()
    {
        Random r = new Random();
        return STATUS_ARR[r.nextInt(STATUS_ARR.length)];
    }

    public long getTime()
    {
        return System.currentTimeMillis();
    }
}
