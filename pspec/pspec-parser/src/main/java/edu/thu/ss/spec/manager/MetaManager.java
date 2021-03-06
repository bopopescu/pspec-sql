package edu.thu.ss.spec.manager;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import edu.thu.ss.spec.lang.pojo.Policy;
import edu.thu.ss.spec.lang.pojo.UserCategory;
import edu.thu.ss.spec.meta.MetaRegistry;

/**
 * manages all meta registries
 * 
 * @author luochen
 * 
 */
public class MetaManager {

	private static List<MetaRegistry> registries = new LinkedList<>();

	private static Set<String> extractOperations = new HashSet<>();

	private static Map<Policy, MetaRegistry> policyRegistries = new HashMap<>();
	/**
	 * database -> (table -> registry)
	 */
	private static Map<String, Map<String, MetaRegistry>> registryIndex = new HashMap<>();

	public static void addExtractOperation(String operation) {
		extractOperations.add(operation);
	}

	public static boolean isExtractOperation(String operation) {
		return extractOperations.contains(operation);
	}

	/**
	 * TODO: luochen determine proper user category
	 * @return current user category, unfinished implementation.
	 */
	public static synchronized UserCategory currentUser() {
		return new UserCategory("analyst", "default-user");
	}

	public static synchronized MetaRegistry get(String database, String table) {
		Map<String, MetaRegistry> map = registryIndex.get(database);
		if (map != null) {
			return map.get(table);
		} else {
			return null;
		}
	}

	public static synchronized MetaRegistry get(Policy policy) {
		return policyRegistries.get(policy);
	}

	/**
	 * return whether policy is applicable for {database, table}
	 * 
	 * @param policy
	 * @param database
	 * @param table
	 * @return
	 */
	public static synchronized boolean applicable(Policy policy, String database, String table) {
		MetaRegistry meta = get(database, table);
		if (meta == null) {
			return false;
		}
		return meta.getPolicy() == policy;
	}

	/**
	 * add a parsed registry, and update {@link MetaManager#registryIndex}
	 * 
	 * @param registry
	 * @throws RuntimeException
	 *           if a table is covered by multiple registries
	 */
	public static synchronized void add(MetaRegistry registry) {
		registries.add(registry);
		if (policyRegistries.containsKey(registry.getPolicy())) {
			throw new IllegalArgumentException("meta data for policy: " + registry.getPolicy().getPath()
					+ " is already defined.");
		}
		policyRegistries.put(registry.getPolicy(), registry);
		
		Map<String, Set<String>> scope = registry.getScope();
		for (Entry<String, Set<String>> e : scope.entrySet()) {
			Map<String, MetaRegistry> index = registryIndex.get(e.getKey());
			if (index == null) {
				index = new HashMap<>();
				registryIndex.put(e.getKey(), index);
			}
			for (String table : e.getValue()) {
				if (index.containsKey(table)) {
					throw new IllegalArgumentException("database: " + e.getKey() + " table: " + table
							+ " is already defined.");
				}
				index.put(table, registry);
			}
		}
	}

	public static synchronized boolean isDefined(String database, String table) {
		return get(database, table) != null;
	}

}
