!#/usr/bin/bash

docker run --name some-zookeeper --restart always -d zookeeper
docker run -d --restart always --name some-nimbus --link some-zookeeper:zookeeper storm storm nimbus
docker run -d --restart always --name supervisor --link some-zookeeper:zookeeper --link some-nimbus:nimbus storm storm supervisor
docker run -d -p 8080:8080 --restart always --name ui --link some-nimbus:nimbus storm storm ui
docker run --link some-nimbus:nimbus -it --rm -v ./target/stormcrawlnlp-1.0-SNAPSHOT.jar:/stormcrawlnlp-1.0-SNAPSHOT.jar storm storm jar /stormcrawlnlp-1.0-SNAPSHOT.jar NlpCrawlerTopology
