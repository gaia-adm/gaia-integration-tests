FROM maven:3.3.3-jdk-8

# Bundle app source
COPY . /src

# Set the working directory
WORKDIR /src

LABEL test=true
LABEL test.run.interval=300000
LABEL test.results.dir=/src/results
LABEL test.results.file=TestSuite.txt
LABEL test.container.settings={\"Config\":{\"Env\":[\"etcdUrl=http://10.60.4.229:4001\",\"gaiaUrl=boris.gaiahub.io\"]}}"

RUN ["mvn","clean","install"]

CMD ["mvn","clean","install"]