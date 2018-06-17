package org.infinispan.visualizer.rest;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.infinispan.client.hotrod.RemoteCache;
import org.infinispan.visualizer.cdi.Resources;
import org.infinispan.visualizer.internal.VisualizerRemoteCacheManager;
import org.infinispan.visualizer.poller.PollerManager;
import org.infinispan.visualizer.poller.jmx.JmxCacheEntriesPollerManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("rest/nodes")
public class NodeInfoRestService {

	@Autowired
	private VisualizerRemoteCacheManager cacheManager;

	@Autowired
	private Resources resources;

	private Map<String, PollerManager<NodeInfo>> pollerManagers = new HashMap<>();

	@GetMapping("/{cacheName}")
	public <K, V> Collection<NodeInfo> getAllNodeInfo(@PathVariable(value = "cacheName") String cacheName,
			@RequestParam(value = "clear", defaultValue = "false") Boolean clear,
			@RequestParam(value = "refresh", defaultValue = "false") Boolean refresh) throws Exception {

		if (cacheName == null || "".equals(cacheName)) {
			cacheName = "default(dist_sync)";
		}

		if (!pollerManagers.containsKey(cacheName)) {
			JmxCacheEntriesPollerManager manager = resources.cacheEntriesPollerManager(cacheManager);
			manager.setCacheName(cacheName);
			manager.init();
			pollerManagers.put(cacheName, manager);
		}

		String[] parts = cacheName.split("\\(");
		if (parts.length > 0) {
			String name = parts[0];
			RemoteCache<K, V> cache = cacheManager.getCache(name);
			if (cache != null) {

				if (clear) {
					cache.clearAsync();
				}

				if (refresh) {
					Set<K> keys = cache.keySet();
					for (K key : keys) {
						V value = cache.get(key);
						cache.put(key, value);
					}
				}
			}
		}

		PollerManager<NodeInfo> manager = pollerManagers.get(cacheName);
		return manager.getAllInfos();
	}
	

}
