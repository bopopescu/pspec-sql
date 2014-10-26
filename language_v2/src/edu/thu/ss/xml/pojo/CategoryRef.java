package edu.thu.ss.xml.pojo;

import java.util.HashSet;
import java.util.Set;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import edu.thu.ss.xml.parser.ParserConstant;

public abstract class CategoryRef<T extends HierarchicalObject<T>> extends ObjectRef {
	protected T category;

	protected Set<ObjectRef> excludeRefs = new HashSet<>();

	protected Set<T> excludes = new HashSet<>();

	protected Set<T> materialized;

	public Set<ObjectRef> getExcludeRefs() {
		return excludeRefs;
	}

	public Set<T> getExcludes() {
		return excludes;
	}

	public Set<T> getMaterialized() {
		return materialized;
	}

	public void materialize() {
		materialized = new HashSet<>(category.decesdants.size());
		for (T t : category.decesdants) {
			boolean include = true;
			for (T excluded : excludes) {
				if (excluded.decesdants.contains(t)) {
					include = false;
					break;
				}
			}
			if (include) {
				materialized.add(t);
			}
		}
	}

	@Override
	public void parse(Node refNode) {
		super.parse(refNode);

		NodeList list = refNode.getChildNodes();
		for (int i = 0; i < list.getLength(); i++) {
			Node node = list.item(i);
			String name = node.getLocalName();
			if (ParserConstant.Ele_Policy_Rule_Exclude.equals(name)) {
				parseExclude(node);
			}
		}
	}

	abstract protected void parseExclude(Node node);

	public T getCategory() {
		return category;
	}

}
