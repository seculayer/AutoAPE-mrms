FROM azul/zulu-openjdk-centos:11.0.7-11.39.15

RUN mkdir -p /eyeCloudAI/app/ape/mrms/lib

COPY ./target/lib /eyeCloudAI/app/ape/mrms/lib
COPY ./.libs /eyeCloudAI/app/ape/mrms
COPY ./mrms.sh /eyeCloudAI/app/ape/mrms
RUN chmod +x /eyeCloudAI/app/ape/mrms/mrms.sh

COPY ./target/automl-mrms-1.0.0.jar /eyeCloudAI/app/ape/mrms
WORKDIR /eyeCloudAI/app/ape/mrms
