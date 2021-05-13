#!/usr/bin/env bash
cd ../
mvn clean package -Dmaven.test.skip=true
docker build --no-cache -t dockerdist.bdmd.com/codegen:1.1 -f docker/Dockerfile .
#docker push dockerdist.bdmd.com/codegen:1.1
docker save dockerdist.bdmd.com/codegen:1.1 |gzip > /Users/qping/Documents/Cloud/work/ETL/deploy/latest/codegen.tar.gz