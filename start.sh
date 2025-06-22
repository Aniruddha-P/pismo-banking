#!/bin/bash
 docker build .
 docker-compose up -d
 #docker-compose up -d --scale pismo-banking-apis=2