#!/bin/sh

cd /opt/config

terraform init     # init workspace
terraform validate # validate config
terraform fmt      # reformat config to default layout

until terraform plan --out savedPlan
do
  echo "**********************************"
  echo "retry after 10 seconds"
  sleep 10
done

terraform apply savedPlan

echo "**********************************"
echo "configuration done"
echo "**********************************"