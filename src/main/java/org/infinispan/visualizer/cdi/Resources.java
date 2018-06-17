package org.infinispan.visualizer.cdi;

import org.infinispan.visualizer.internal.VisualizerRemoteCacheManager;
import org.infinispan.visualizer.poller.PollerManager;
import org.infinispan.visualizer.poller.infinispan.JdgJmxCacheEntriesPollerManager;
import org.infinispan.visualizer.poller.infinispan.JdgJmxCacheNamesPollerManager;
import org.infinispan.visualizer.poller.jmx.JmxCacheEntriesPollerManager;
import org.infinispan.visualizer.poller.jmx.JmxCacheNamesPollerManager;
import org.infinispan.visualizer.rest.CacheNameInfo;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.ApplicationScope;

@Component
public class Resources {
	
	private String refreshRate = System.getProperty("infinispan.visualizer.refreshRate", "2000");
	private String jmxUsername = System.getProperty("infinispan.visualizer.jmxUser", "jdgadmin");
	private String jmxPassword = System.getProperty("infinispan.visualizer.jmxPass", "redhat");

	// JMX port offset is calculated by the formula: HotRod port -
	// jmxHotrodPortOffset
	// e.g. 11222 - 1232 = 9990
	private int jmxHotrodPortOffset = Integer
			.parseInt(System.getProperty("infinispan.visualizer.jmxPortOffset", "1232"));

	private String nodeColorAsString = System.getProperty("infinispan.visualizer.nodeColor");

	@Bean
	@ApplicationScope
	public VisualizerRemoteCacheManager cacheManager() {
		VisualizerRemoteCacheManager cm = new VisualizerRemoteCacheManager();
		cm.start();
		return cm;
	}

	@Bean
	public JmxCacheEntriesPollerManager cacheEntriesPollerManager(VisualizerRemoteCacheManager cacheManager) {
		JmxCacheEntriesPollerManager manager = new JdgJmxCacheEntriesPollerManager(cacheManager());
		manager.setJmxUsername(jmxUsername);
		manager.setJmxPassword(jmxPassword);
		manager.setJmxHotrodPortOffset(jmxHotrodPortOffset);
		manager.setRefreshRate(Long.valueOf(refreshRate));

		if (nodeColorAsString != null)
			manager.setMultiColor(false, Integer.parseInt(nodeColorAsString));
		else
			manager.setMultiColor(true, null);

		return manager;
	}

	@Bean
	@ApplicationScope
	public PollerManager<CacheNameInfo> cacheNamesPoller(VisualizerRemoteCacheManager cacheManager) {
		JmxCacheNamesPollerManager manager = new JdgJmxCacheNamesPollerManager(cacheManager);
		manager.setJmxUsername(jmxUsername);
		manager.setJmxPassword(jmxPassword);
		manager.setJmxHotrodPortOffset(jmxHotrodPortOffset);
		manager.setRefreshRate(Long.valueOf(refreshRate));

		manager.init();

		return manager;
	}

}
