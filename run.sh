#!/bin/bash

echo "Démarrage de MongoDB..."
sudo systemctl start mongod
trap "sudo systemctl stop mongod" SIGINT

sleep 3

echo "Démarrage de l'application Spring Boot..."
mvn spring-boot:run

