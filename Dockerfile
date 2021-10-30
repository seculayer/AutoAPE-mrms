FROM maven:3.8-jdk-11 as builder
ARG app="/opt/app"

RUN mkdir -p $app
WORKDIR $app

COPY ./pom.xml ./pom.xml

RUN mvn dependency:go-offline -B

COPY ./src "$app/src"

RUN mvn package

FROM azul/zulu-openjdk-centos:11 as app
ARG app="/opt/app"
ENV LANG=en_US.UTF-8 LANGUAGE=en_US:en LC_ALL=en_US.UTF-8

RUN mkdir -p /eyeCloudAI/app/ape/mrms/lib
WORKDIR /eyeCloudAI/app/ape/mrms

COPY ./.libs /eyeCloudAI/app/ape/mrms

COPY ./mrms.sh /eyeCloudAI/app/ape/mrms
RUN chmod +x /eyeCloudAI/app/ape/mrms/mrms.sh

COPY --from=builder "$app/target/lib" /eyeCloudAI/app/ape/mrms/lib
COPY --from=builder "$app/target/automl-mrms-1.0.0.jar" /eyeCloudAI/app/ape/mrms

RUN groupadd -g 1000 aiuser
RUN useradd -r -u 1000 -g aiuser aiuser
RUN chown -R aiuser:aiuser /eyeCloudAI
USER aiuser

CMD ["/eyeCloudAI/app/ape/mrms/mrms.sh"]
