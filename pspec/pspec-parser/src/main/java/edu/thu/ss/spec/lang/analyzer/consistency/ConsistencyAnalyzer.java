package edu.thu.ss.spec.lang.analyzer.consistency;

import java.util.ArrayList;
import java.util.List;

import edu.thu.ss.spec.lang.analyzer.BasePolicyAnalyzer;
import edu.thu.ss.spec.lang.parser.event.EventTable;
import edu.thu.ss.spec.lang.pojo.ExpandedRule;
import edu.thu.ss.spec.lang.pojo.Policy;

public abstract class ConsistencyAnalyzer extends BasePolicyAnalyzer {
	
	public ConsistencyAnalyzer(EventTable table) {
		super(table);
	}

	public abstract boolean analyze(List<ExpandedRule> rules);

	@Override
	public boolean analyze(Policy policy) {
		List<ExpandedRule> rules = policy.getExpandedRules();
		List<ExpandedRule> restrictionRules = new ArrayList<>();
		List<ExpandedRule> filterRules = new ArrayList<>();

		for (ExpandedRule rule : rules) {
			if (rule.isFilter()) {
				filterRules.add(rule);
			} else if (!rule.getRestriction().isForbid()) {
				restrictionRules.add(rule);
			}
		}

		analyze(restrictionRules);
		//analyze(filterRules);
		return false;
	}
}