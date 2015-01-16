# Docker file for ArchimedesQA

# NOTE:
# Build y using the following command: docker build -t cegme/querydriven-probkb .
# Run a bash instance using the following command: docker run -t -i cegme/querydriven-probkb
# Log into postgres: psql -d madlib -U postgres

FROM ubuntu
MAINTAINER christangrant@gmail.com

# Standard upgrade
RUN apt-get -y update &&\
    apt-get -y upgrade &&\
    apt-get -y dist-upgrade

# Tools to help building
RUN apt-get -y install subversion git zip make cmake htop tmux curl wget bison flex openjdk-7-jdk vim doxygen bc gawk gzip scala vim 

RUN apt-get -y install libboost-all-dev libboost-dev libboost-dbg libopenblas-dev liblapack-dev libarpack2-dev libarpack2-dbg libarpack+++2-dev libarmadillo-dev libeigen2-dev libstdc++-4.8-dev libstdc++-4.8-doc g++ gcc sqlite3 libkrb5-dev

# PostgreSQL libraries
RUN apt-get -y install postgresql-9.3 pgadmin3 postgresql-client-9.3 postgresql-contrib-9.3 postgresql-server-dev-9.3 libpq-dev postgresql-plpython-9.3 python-software-properties software-properties-common

# SEMPRE dependencies
RUN apt-get -y install ruby1.8

# Download SEMPRE for questions answering

RUN mkdir -p /home/dsr/ && cd /home/dsr/ && git clone https://github.com/percyliang/sempre

# Configure SEMPRE

RUN cd /home/dsr/sempre/ &&\
  ./download-dependencies core &&\
  ./download-dependencies emnlp2013 &&\
  ./download-dependencies acl2014 &&\
  ./download-dependencies fullfreebase_vdb &&\
  ./download-dependencies geofreebase_vdb &&\
  ./download-dependencies geofreebase_ttl &&\
  ./download-dependencies fullfreebase_vdb &&\
  ./download-dependencies fullfreebase_ttl

# Install Virtuoso for sempre
RUN apt-get install -y automake gawk gperf libtool bison flex libssl-dev
# Clone into sempre folder
RUN cd /home/dsr/sempre/ &&\
  git clone https://github.com/openlink/virtuoso-opensource &&\
  cd virtuoso-opensource &&\
  git checkout tags/v7.0.0 &&\
  ./autogen.sh &&\
  ./configure --prefix=$PWD/install &&\
  make &&\
  make install &&\
  cd ..


# Install Elastic search

RUN mkdir -p /home/dsr/ && cd /home/dsr && git clone https://github.com/elasticsearch/elasticsearch

RUN cd /home/dsr/elasticsearch && bash bin/elasticsearch


# TODO Install the Kun's Prob KB
# TODO Install Yang's data set









# Create a file to start virtuoso, elasticsearch, ...


