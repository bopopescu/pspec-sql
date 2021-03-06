package edu.thu.ss.spec.manager;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import edu.thu.ss.spec.lang.pojo.Policy;

/**
 * manages all loaded policies
 * 
 * @author luochen
 * 
 */
public class PolicyManager {

	private static Map<URI, Policy> policies = new HashMap<>();
	private static boolean cache = false;

	public static Policy getPolicy(URI uri) {
		return policies.get(uri);
	}

	public static void addPolicy(Policy policy) {
		if (cache) {
			policies.put(policy.getPath(), policy);
		}
	}

	public static void clear() {
		policies.clear();
	}

	public static void setCache(boolean cache) {
		PolicyManager.cache = cache;
	}

}
