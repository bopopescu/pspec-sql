package edu.thu.ss.spec.lang.analyzer;

import edu.thu.ss.spec.lang.analyzer.stat.AnalyzerStat;
import edu.thu.ss.spec.lang.pojo.Policy;

/**
 * a default implementation for {@link PolicyAnalyzer}
 * 
 * @author luochen
 * 
 */
public abstract class BasePolicyAnalyzer implements PolicyAnalyzer {

	@Override
	public boolean analyze(Policy policy, AnalyzerStat stat, int n) {
		return analyze(policy, null, 0);
	}

	@Override
	public String errorMsg() {
		return "";
	}

	@Override
	public boolean stopOnError() {
		return false;
	}


}