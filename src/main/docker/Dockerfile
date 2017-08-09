FROM frolvlad/alpine-oraclejdk8:slim
VOLUME /tmp
ADD digag-server-0.0.1-SNAPSHOT.jar app.jar
RUN mkdir /digag
RUN sh -c 'touch /digag/app.jar'
ENV JAVA_OPTS=""
ENTRYPOINT [ "sh", "-c", "java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -jar /digag/app.jar" ]