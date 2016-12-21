FROM maven:3.3.3-jdk-8

# Bundle app source
COPY . /src

# Set the working directory
WORKDIR /src

RUN ["mvn","clean","install"]

CMD ["mvn","clean","install"]

LABEL tugbot-test=true
LABEL tugbot-results-dir=/src/results/junitreports
LABEL tugbot-event-docker=
LABEL tugbot-event-docker-filter-type=container
LABEL tugbot-event-docker-filter-action=start
