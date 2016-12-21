FROM maven:3.3.3-jdk-8

# Bundle app source
COPY . /src

# Set the working directory
WORKDIR /src

RUN ["mvn","clean","install"]

CMD ["mvn","clean","install"]

LABEL tugbot_test=true
LABEL tugbot_results_dir=/src/results/junitreports
LABEL tugbot_event_docker=
LABEL tugbot_event_docker_filter_type=container
LABEL tugbot_event_docker_filter_action=start
