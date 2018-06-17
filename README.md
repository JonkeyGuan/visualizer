# visualizer

This is the JDG(Infinispan) cache data visualizer, migrate from https://github.com/infinispan/visual to sprintboot application

run the visualizer:

mvn clean spring-boot:run -Dinfinispan.visualizer.jmxUser=jdgadmin -Dinfinispan.visualizer.jmxPass=redhat -Dinfinispan.visualizer.serverList="192.168.56.101:11222;192.168.56.101:11322;192.168.56.101:11422" -Dserver.port=18880


