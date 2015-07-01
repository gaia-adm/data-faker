Motivation
==========

This Service was developed to allow faking of randomized data for testing purposes.

Usage
=====

This service was built using [dropwizard](https://dropwizard.github.io/dropwizard/index.html)
It expose a single REST endpoint "fake-data", that accepts json template body with [mustache](https://github.com/spullara/mustache.java)
parameters.

Currently those are the supported ~~mustache parameters~~:
----------------------------------------------------------

| parameter  | description |
| ------------- | ------------- |
| {{status}}  | randomize value of {"new", "in progress", "done"}  |
| {{time}}  | now() in POSIX-time format (accepted by influxdb)   |

Here is example for how to use it:
```HTTP
curl -H "Content-Type: application/json" -X POST -d '[{"name":"my_time_series4","points":[[{{time}},"{{status}}"]],"columns":["time", "status"]}]' 'http://localhost:100
00/fake-data?repeat=2&sendto=influxdb'
```

