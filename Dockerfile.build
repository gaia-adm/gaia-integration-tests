FROM maven:3.3.3-jdk-8

# Bundle app source
COPY . /src

# Set the working directory
WORKDIR /src

LABEL test=
LABEL test.run.interval=300000
LABEL test.results.dir=/src/results
LABEL test.results.file=TestSuite.txt
LABEL test.publish.description="Gaia Integration Tests"

RUN ["mvn","clean","install"]

CMD ["mvn","clean","install"]