#!/bin/sh
java -Xmx2048M -cp target/classes:target/test-classes/:../bayeux/target/classes/:../api/target/classes/:../../../lib/ext/jetty-client-6.1-SNAPSHOT.jar:../../../lib/jetty-util-6.1-SNAPSHOT.jar:../../../lib/jetty-6.1-SNAPSHOT.jar:../../../lib/servlet-api-2.5-6.1-SNAPSHOT.jar org.mortbay.cometd.client.BayeuxLoadGenerator
