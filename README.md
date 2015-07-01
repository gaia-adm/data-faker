Motivation
==========

This Service was developed to allow faking of randomized data for testing purposes.

Usage
=====

This service was built using [dropwizard](https://dropwizard.github.io/dropwizard/index.html)
It exposes a single REST endpoint "fake-data", that accepts json template body with [mustache](https://github.com/spullara/mustache.java) parameters.

Supported __mustache parameters__:

| mustache parameter  | description |
| :-------------: | :-------------: |
| {{status}}  | randomize value of {"new", "in progress", "done"}  |
| {{time}}  | now in POSIX-time format (accepted by influxdb)   |

Supported __query parameters__:

| query parameter  | description |
| :-------------: | :------------- |
| repeat  | how many time to evaluate the template (each time with new values), for example: ?repeat=5 (default is 1) |
| dbname  | influxdb dbname to use (default is "db1") |
| sendto  | what is the target to send the data to. currently supported values are "influxdb" and "rabbitmq" (default is "rabbitmq") |

Here is example for how to use it:
```
curl -H "Content-Type: application/json" -X POST 
-d '[{"name":"my_time_series","points":[[{{time}},"{{status}}"]],"columns":["time", "status"]}]'
'http://localhost:8080/fake-data?repeat=2&sendto=influxdb'
```

Best way to run the service is using docker. In this example we link influxdb container with data-faker container
```
docker run -d -p 8083:8083 -p 8086:8086 -e PRE_CREATE_DB="db1" --name influxdb tutum/influxdb
docker run -d -p 8080:8080 -p 8081:8081 --link influxdb:influxdb --name facker gaiaadm/data-faker
```

data-faker ships with default configuration, here is an example how to override the configuration
```
docker run -d -p 11000:8080 -p 11001:8081 --name facker3 gaiaadm/data-faker 
java -Ddw.rabbitmq.host=OTHERHOST -Ddw.influxdb.port=OTHERPORT 
-jar /data/target/data-faker-1.0-SNAPSHOT.jar server 
```
To find out what are the values you can override, check out ```RabbitmqConfiguration.java``` and ```InfluxdbConfiguration.java```
