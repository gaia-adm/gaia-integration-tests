FROM maven:3.3.3-jdk-8

# Bundle app source
COPY . /src

# Set the working directory
WORKDIR /src

LABEL test=true
LABEL test.run.interval=300000
LABEL test.results.dir=/src/results
LABEL test.results.file=TestSuite.txt
LABEL test.container.settings={\"Config\":{\"Env\":[\"gaiaUrl=http://boris.gaiahub.io:88\"]}, \"HostConfig\":{\"Binds\":[\"/etc/environment=/etc/environment\"]}}

RUN ["mvn","clean","install"]

CMD ["mvn","clean","install"]