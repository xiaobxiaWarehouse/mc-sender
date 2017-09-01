#!/usr/bin/bash
mvn -f ../pom.xml clean compile -Dmaven.test.skip=true && pause
