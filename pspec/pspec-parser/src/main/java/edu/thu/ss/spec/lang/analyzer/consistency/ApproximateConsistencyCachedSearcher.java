package edu.thu.ss.spec.lang.analyzer.consistency;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.thu.ss.spec.lang.analyzer.stat.ConsistencyStat;
import edu.thu.ss.spec.lang.parser.event.EventTable;
import edu.thu.ss.spec.lang.pojo.Action;
import edu.thu.ss.spec.lang.pojo.DataCategory;
import edu.thu.ss.spec.lang.pojo.DesensitizeOperation;
import edu.thu.ss.spec.lang.pojo.UserCategory;
import edu.thu.ss.spec.util.PSpecUtil;

public class ApproximateConsistencyCachedSearcher extends ApproximateConsistencySearcher{
	private static Logger logger = LoggerFactory.getLogger(ApproximateConsistencyCachedSearcher.class);

	private Map<SearchKey, RuleObject> cache;
	private Map<SearchKey, RuleObject> nextCache = new HashMap<>();
	private int conflicts = 0;
	private ConsistencyStat stat;
	private int n;

	public ApproximateConsistencyCachedSearcher(EventTable table) {
		super(table);
	}

	public ApproximateConsistencyCachedSearcher(EventTable table, ConsistencyStat stat, int n) {
		super(table);
		this.stat = stat;
		this.n = n;
	}

	@Override
	protected void beginLevel(int level) {
		cache = nextCache;
		nextCache = new HashMap<>();
		conflicts = 0;
	}

	@Override
	protected void endLevel(int level) {
		logger.error("{} conflict rules detected in levle: {}", conflicts, level);
		if (stat != null) {
			stat.levels[n] = level;
			stat.conflicts[n] += conflicts;
		}
	};

	@Override
	protected boolean process(SearchKey key) {
		RuleObject[] objs = getRuleObjects(key);
		assert (objs.length == 2);
		RuleObject rule1 = objs[0];
		RuleObject rule2 = objs[1];

		RuleObject result = new RuleObject();

		Set<UserCategory> users = PSpecUtil.intersect(rule1.users, rule2.users);
		if (users.size() == 0) {
			return false;
		}
		
		result.users = users;
		List<Triple> triples = new LinkedList<>();
		boolean match = false;
		for (Triple t1 : rule1.triples) {
			for (Triple t2 : rule2.triples) {
				Action action = PSpecUtil.bottom(t1.action, t2.action);
				if (action == null) {
					continue;
				}
				Set<DataCategory> datas = PSpecUtil.intersect(t1.datas, t2.datas);
				if (datas.size() == 0) {
					continue;
				}
				match = true;
				List<Set<DesensitizeOperation>> list = checkRestriction(key, t1.list, t2.list, datas);
				if (list == null) {
					return false;
				}
				Triple triple = new Triple(action, datas, list);
				triples.add(triple);
				match = true;
			}
		}

		if (!match) {
			return false;
		}

		result.triples = triples.toArray(new Triple[triples.size()]);
		nextCache.put(key, result);

		return true;
	}

	private List<Set<DesensitizeOperation>> checkRestriction(SearchKey key,
			List<Set<DesensitizeOperation>> list1, List<Set<DesensitizeOperation>> list2,
			Set<DataCategory> datas) {
		if (list1 == null || list2 == null) {
			if (!(list1 == null && list2 == null)) {
				int index = (list1 == null) ? key.index[0] : key.index[1];
				logger
						.warn(
								"Possible conflicts between expanded rules: {}, since rule :#{} forbids the data access.",
								PSpecUtil.toString(key.index, sortedRules), sortedRules.get(index).getRuleId());
			}
			return null;
		}
		List<Set<DesensitizeOperation>> list = new LinkedList<>();
		for (Set<DesensitizeOperation> ops1 : list1) {
			for (Set<DesensitizeOperation> ops2 : list2) {
				if (ops1 == null) {
					PSpecUtil.mergeOperations(list, ops2);
				} else if (ops2 == null) {
					PSpecUtil.mergeOperations(list, ops1);
				} else {
					Set<DesensitizeOperation> ops = PSpecUtil.intersect(ops1, ops2);
					if (ops.size() == 0) {
						conflicts++;
						logger
								.warn(
										"Desensitize operation conflicts detected between expanded rules: #{} for data categories: {}.",
										PSpecUtil.toString(key.index, sortedRules), PSpecUtil.format(datas, ","));
						return null;
					}
					PSpecUtil.mergeOperations(list, ops);

				}
			}
		}
		return list;
	}

	private RuleObject[] getRuleObjects(SearchKey key) {
		int tmp = key.getFirst();
		RuleObject[] objs = new RuleObject[2];
		objs[0] = ruleObjects[tmp];
		key.setFirst(-1);
		objs[1] = cache.get(key);
		if (objs[1] == null) {
			if (key.index.length > 2) {
				throw new RuntimeException("Invalid cache state for key: " + Arrays.toString(key.index));
			}
			objs[1] = ruleObjects[key.getLast()];
		}
		key.setFirst(tmp);
		return objs;
	}
}
