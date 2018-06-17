package org.infinispan.visualizer.rest;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.infinispan.visualizer.poller.PollerManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("rest/names")
public class CacheNameInfoRestService {

	@Autowired
	private PollerManager<CacheNameInfo> manager;

	@GetMapping
	public Set<String> getAllNodeInfo() throws Exception {
		Set<String> names = new HashSet<>();
		Collection<CacheNameInfo> infos = manager.getAllInfos();
		for (CacheNameInfo info : infos) {
			if (info.getNames() != null) {
				names.addAll(Arrays.asList(info.getNames()));
			}
		}

		return names;
	}

}
