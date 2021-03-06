package edu.thu.ss.spec.lang.parser.event;

import java.util.ArrayList;
import java.util.List;

import edu.thu.ss.spec.lang.parser.event.PSpecListener.AnalysisType;
import edu.thu.ss.spec.lang.parser.event.PSpecListener.MetadataLabelType;
import edu.thu.ss.spec.lang.parser.event.PSpecListener.RefErrorType;
import edu.thu.ss.spec.lang.parser.event.PSpecListener.RestrictionErrorType;
import edu.thu.ss.spec.lang.parser.event.PSpecListener.VocabularyErrorType;
import edu.thu.ss.spec.lang.pojo.Category;
import edu.thu.ss.spec.lang.pojo.CategoryRef;
import edu.thu.ss.spec.lang.pojo.ExpandedRule;
import edu.thu.ss.spec.lang.pojo.Restriction;
import edu.thu.ss.spec.lang.pojo.Rule;

public class EventTable {
	private static EventTable dummy = new EventTable();

	public static EventTable getDummy() {
		return dummy;
	}

	private List<PSpecListener> listeners = new ArrayList<>();

	public void add(PSpecListener listener) {
		listeners.add(listener);
	}

	public void remove(PSpecListener listener) {
		listeners.remove(listener);
	}

	public void onVocabularyError(VocabularyErrorType type, Category<?> category, String refid) {
		for (PSpecListener listener : listeners) {
			listener.onVocabularyError(type, category, refid);
		}
	}

	public void onRuleRefError(RefErrorType type, Rule rule, CategoryRef<?> ref, String refid) {
		for (PSpecListener listener : listeners) {
			listener.onRuleRefError(type, rule, ref, refid);
		}
	}

	public void onRestrictionError(RestrictionErrorType type, Rule rule, Restriction res, String refid) {
		for (PSpecListener listener : listeners) {
			listener.onRestrictionError(type, rule, res, refid);
		}

	}

	public void onAnalysis(AnalysisType type, ExpandedRule... rules) {
		for (PSpecListener listener : listeners) {
			listener.onAnalysis(type, rules);
		}

	}

	public void onParseRule(Rule rule) {
		for (PSpecListener listener : listeners) {
			listener.onParseRule(rule);
		}

	}
	
	public void onMetadataLabelError(MetadataLabelType type, String location) {
		for (PSpecListener listener : listeners) {
			listener.onMetadataLabelError(type, location);
		}
		
	}

}
