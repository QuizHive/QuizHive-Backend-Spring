#!/bin/sh
if ! command -v mvn &> /dev/null
then
	echo "Maven could not be found, please install it."
	exit 1
fi
mvn clean package
docker compose down
docker compose up --build
