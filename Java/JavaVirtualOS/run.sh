#!/usr/bin/env sh
set -eu
cd "$(dirname "$0")"
mkdir -p out
find src/main/java -name '*.java' -print > sources.txt
javac -d out @sources.txt
java -cp out com.virtualos.Main
