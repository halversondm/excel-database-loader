#!/bin/zsh
find . -not -path "./excel-database-loader-ui/node_modules/*" -name 'Dockerfile' | while read dockerfile; do
  printf "${dockerfile}\n"
  dir=$(dirname "$dockerfile")
  imagename=$(basename "$dir")
  docker build -t "halversondm/${imagename}:latest" "$dir"
done