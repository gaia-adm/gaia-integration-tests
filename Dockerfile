FROM maven:3.3.3-jdk-8

# Bundle app source
COPY . /src

# Set the working directory
WORKDIR /src

LABEL test=true
LABEL test.environment.file=/etc/environment
LABEL test.container.settings={\"Config\":{\"Env\":[\"gaiaUrl=http://boris.gaiahub.io:88\"]}}
LABEL test.results.dir=/src/results
LABEL test.results.file=TestSuite.txt
LABEL test.run.interval=86400000

RUN ["mvn","clean","install"]

CMD ["mvn","clean","install"]
