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
  python setup.py install &&\
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
  psql -U postgres --command "ALTER USER postgres WITH SUPERUSER PASSWORD 'mypass';" &&\
  createdb -O postgres -U postgres madlib

#psql -U postgres --command "ALTER USER postgres SET search_path = public;" &&\

# EXTREMELY unsafe hack for easy DB access
RUN sed -i 's/peer/trust/' /etc/postgresql/9.3/main/pg_hba.conf &&\
  echo "host all  all    0.0.0.0/0  md5" >> /etc/postgresql/9.3/main/pg_hba.conf &&\
  echo "listen_addresses='*'" >> /etc/postgresql/9.3/main/postgresql.conf


RUN /etc/init.d/postgresql start &&\
  createlang plpythonu madlib 


USER root

RUN apt-get -y install ipython

# Install jena
RUN cd /home/dsr &&\
  git clone https://github.com/apache/jena.git &&\
  cd jena/jena-sdb &&\
  mvn clean install -Dmaven.test.skip=true

ADD https://jdbc.postgresql.org/download/postgresql-9.3-1103.jdbc41.jar /home/dsr/jena/jena-sdb/


# Jena environment variables
#RUN /etc/init.d/postgresql start &&\
#  psql -U postgres --command "ALTER USER postgres SET search_path = public;"

# Make .ttl file
RUN echo "@prefix rdfs:     <http://www.w3.org/2000/01/rdf-schema#> ." > /home/dsr/jena/jena-sdb/sdb.ttl &&\
  echo "@prefix rdf:      <http://www.w3.org/1999/02/22-rdf-syntax-ns#> ." >> /home/dsr/jena/jena-sdb/sdb.ttl &&\
  echo "@prefix ja:       <http://jena.hpl.hp.com/2005/11/Assembler#> ." >> /home/dsr/jena/jena-sdb/sdb.ttl &&\
  echo "@prefix sdb:      <http://jena.hpl.hp.com/2007/sdb#> ." >> /home/dsr/jena/jena-sdb/sdb.ttl &&\
  echo "\n" >> /home/dsr/jena/jena-sdb/sdb.ttl &&\
  echo "[] ja:loadClass \"com.hp.hpl.jena.sdb.SDB\" ." >> /home/dsr/jena/jena-sdb/sdb.ttl &&\
  echo "sdb:DatasetStore  rdfs:subClassOf  ja:RDFDataset ." >> /home/dsr/jena/jena-sdb/sdb.ttl &&\
  echo "\n" >> /home/dsr/jena/jena-sdb/sdb.ttl &&\
  echo "<#dataset> rdf:type sdb:DatasetStore ;" >> /home/dsr/jena/jena-sdb/sdb.ttl &&\
  echo "    sdb:store <#store> ." >> /home/dsr/jena/jena-sdb/sdb.ttl &&\
  echo "<#store> rdf:type sdb:Store ;" >> /home/dsr/jena/jena-sdb/sdb.ttl &&\
  echo "    sdb:layout         \"layout2/hash\" ;" >> /home/dsr/jena/jena-sdb/sdb.ttl &&\
  echo "sdb:connection     <#conn> ." >> /home/dsr/jena/jena-sdb/sdb.ttl &&\
  echo "\n" >> /home/dsr/jena/jena-sdb/sdb.ttl &&\
  echo "<#conn> rdf:type sdb:SDBConnection ;" >> /home/dsr/jena/jena-sdb/sdb.ttl &&\
  echo "\n" >> /home/dsr/jena/jena-sdb/sdb.ttl &&\
  echo "     sdb:sdbType        \"postgresql\" ;" >> /home/dsr/jena/jena-sdb/sdb.ttl &&\
  echo "     sdb:sdbHost        \"localhost\" ;" >> /home/dsr/jena/jena-sdb/sdb.ttl &&\
  echo "     sdb:sdbName        \"madlib\" ;" >> /home/dsr/jena/jena-sdb/sdb.ttl &&\
  echo "    ." >> /home/dsr/jena/jena-sdb/sdb.ttl


ENV SDBROOT /home/dsr/jena/jena-sdb
ENV PATH $SDBROOT/bin:$PATH
ENV SDB_USER postgres
ENV SDB_PASSWORD mypass
ENV SDB_JDBC /home/dsr/jena/jena-sdb/postgresql-9.3-1103.jdbc41.jar
ENV CLASSPATH $SDB_JDBC/bin:$CLASSPATH

ENV JENAROOT /home/dsr/jena
ENV PATH $JENAROOT/bin:$PATH

RUN chmod 777 /home/dsr/jena/jena-sdb/postgresql-9.3-1103.jdbc41.jar

USER postgres

## Initialize jena
RUN /etc/init.d/postgresql start &&\
  cd /home/dsr/jena/jena-sdb &&\
  bin/sdbconfig --sdb sdb.ttl --format &&\
  bin/sdbconfig --create

# Download freebase
ADD http://commondatastorage.googleapis.com/freebase-public/rdf/freebase-rdf-latest.gz /home/dsr/jena/jena-sdb/
ADD http://people.apache.org/~andy/Freebase20121223/fixit /home/dsr/jena/jena-sdb/
ADD http://people.apache.org/~andy/Freebase20121223/dwim /home/dsr/jena/jena-sdb/
ADD http://people.apache.org/~andy/Freebase20121223/header.ttl /home/dsr/jena/jena-sdb/

RUN cd /home/dsr/jena/jena-sdb/ &&\
  time gzip -d < ../freebase-rdf-2012-12-23-00-00.gz | split -l 10000000 &&\
  time dwim ???
  


USER root

# Add servces 
RUN echo "/etc/init.d/elasticsearch restart\n" >> /home/dsr/startup.sh &&\
  echo "/etc/init.d/postgresql start" >> /home/dsr/startup.sh

# Create a file to start virtuoso, elasticsearch, ...
CMD cd /home/dsr/ && bash startup.sh && /bin/bash


