#!/bin/bash
sleep 10
echo "Checking url..."
until [ \
  "$(curl -s -w '%{http_code}' -o /dev/null "${1}")" \
  -eq 200 ]
do
  echo "ping ${1}"
  sleep 20
done
echo "It's alive!!!"