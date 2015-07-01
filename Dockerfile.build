FROM maven:3.3.3-jdk-8

ENV GAIA_HOME=/usr/local/gaia

RUN mkdir -p $GAIA_HOME
WORKDIR $GAIA_HOME

ADD . $GAIA_HOME

RUN ["mvn","clean","install"]