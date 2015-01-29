# Docker file for ArchimedesQA

# NOTE:
# Build y using the following command: docker build -t cegme/archimedesqa .
# Run a bash instance using the following command: docker run -t -i cegme/archimedesqa
# Log into postgres: psql -d madlib -U postgres

FROM ubuntu
MAINTAINER christangrant@gmail.com

# Standard upgrade
RUN apt-get -y update &&\
  apt-get -y upgrade &&\
  apt-get -y --fix-missing dist-upgrade

# Tools to help building
RUN apt-get -y --fix-missing install subversion git zip make cmake htop tmux curl wget bison flex openjdk-7-jdk vim doxygen bc gawk gzip scala vim automake gawk gperf libtool bison flex libssl-dev maven ruby ssh-client 

RUN apt-get -y install libboost-all-dev libboost-dev libboost-dbg libopenblas-dev liblapack-dev libarpack2-dev libarpack2-dbg libarpack+++2-dev libarmadillo-dev libeigen2-dev libstdc++-4.8-dev libstdc++-4.8-doc g++ gcc sqlite3 libkrb5-dev

# PostgreSQL libraries
RUN apt-get -y install postgresql-9.3 pgadmin3 postgresql-client-9.3 postgresql-contrib-9.3 postgresql-server-dev-9.3 libpq-dev postgresql-plpython-9.3 python-software-properties software-properties-common

# Download and configure SEMPRE for questions answering

RUN mkdir -p /home/dsr/ && cd /home/dsr/ &&\
  git clone https://github.com/percyliang/sempre &&\
  cd sempre/ &&\
  ./download-dependencies core &&\
  ./download-dependencies emnlp2013 &&\
  ./download-dependencies acl2014 &&\
  ./download-dependencies geofreebase_vdb &&\
  ./download-dependencies geofreebase_ttl &&\
  ./download-dependencies fullfreebase_vdb &&\
  ./download-dependencies fullfreebase_ttl &&\
  make

# Install Virtuoso for sempre
# Clone into sempre folder
RUN cd /home/dsr/sempre/ &&\
  git clone https://github.com/openlink/virtuoso-opensource &&\
  cd virtuoso-opensource &&\
  git checkout 042f142 &&\
  ./autogen.sh &&\
  ./configure --prefix=$PWD/install &&\
  make &&\
  make install &&\
  cd ..


# Install Elastic search

RUN mkdir -p /home/dsr/ && cd /home/dsr &&\
  git clone https://github.com/elasticsearch/elasticsearch &&\
  cd /home/dsr/elasticsearch &&\
  mvn clean package -DskipTests &&\
  mvn install -DskipTests &&\
  mvn package -DskipTests &&\
  dpkg -i target/releases/elasticsearch*.deb


# TODO Install the Kun's Prob KB
# TODO Install Yang's data set



# Install Quepy and dependencies (from source)
RUN cd /home/dsr/ &&\
  git clone https://github.com/machinalis/quepy.git &&\
  cd quepy &&\
  apt-get -y install python-pip python-numpy-dbg &&\
  pip install docopt==0.6.1 &&\
  python setup.py install 
  pip install quepy


# Install nltk
RUN cd /home/dsr/ &&\
  git clone https://github.com/nltk/nltk.git &&\
  cd nltk &&\
  apt-get -y install python-yaml python-matplotlib &&\
  git checkout tags/3.0.0 &&\
  pip install -U nltk &&\
  python -m nltk.downloader -d /usr/share/nltk_data all
  


  
USER postgres

RUN /etc/init.d/postgresql start &&\
  createdb -O postgres madlib

# EXTREMELY unsafe hack for easy DB access
RUN sed -i 's/peer/trust/' /etc/postgresql/9.3/main/pg_hba.conf &&\
  echo "host all  all    0.0.0.0/0  md5" >> /etc/postgresql/9.3/main/pg_hba.conf &&\
  echo "listen_addresses='*'" >> /etc/postgresql/9.3/main/postgresql.conf


RUN /etc/init.d/postgresql start &&\
  createlang plpythonu madlib &&\
  apt-get -y install ipython


USER root

# Add servces 
RUN echo "/etc/init.d/elasticsearch restart\n" >> /home/dsr/startup.sh &&\
  echo "/etc/init.d/postgresql start" >> /home/dsr/startup.sh

# Create a file to start virtuoso, elasticsearch, ...
CMD cd /home/dsr/ && bash startup.sh && /bin/bash


