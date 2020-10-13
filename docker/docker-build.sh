#!/usr/bin/env bash
cd ../
mvn clean package -Dmaven.test.skip=true
docker build -t dockerdist.bdmd.com/codegen:1.0 -f docker/Dockerfile .
docker push dockerdist.bdmd.com/codegen:1.0