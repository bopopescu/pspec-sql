package edu.thu.ss.spec.lang.pojo;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import edu.thu.ss.spec.lang.parser.ParserConstant;
import edu.thu.ss.spec.util.PSpecUtil;

/**
 * class for desensitization, requires some data categories must be desensitized with
 * given operations.
 * @author luochen
 *
 */
public class Desensitization implements Writable {

	protected String dataRefId = "";

	protected DataRef dataRef;

	/**
	 * materialized data categories referred in {@link #dataRef}
	 */
	protected Set<DataCategory> datas;

	/**
	 * required {@link DesensitizeOperation}s must be supported by all {@link DataCategory} in {@link #datas} 
	 */
	protected Set<DesensitizeOperation> operations = new LinkedHashSet<>();

	public Desensitization() {
	}

	public boolean effective() {
		return !operations.isEmpty();
	}

	public Desensitization(DataRef ref) {
		this.dataRefId = ref.refid;
		this.dataRef = ref;
	}

	public Desensitization clone() {
		Desensitization de = new Desensitization();
		de.dataRefId = dataRefId;
		de.dataRef = dataRef;
		if (this.datas != null) {
			de.datas = new HashSet<>(this.datas);
		}
		de.operations = new LinkedHashSet<>(this.operations);
		return de;
	}

	@Override
	public Element outputElement(Document document) {
		Element element = document.createElement(ParserConstant.Ele_Policy_Rule_Desensitize);

		if (this.dataRef != null) {
			Element refEle = document.createElement(ParserConstant.Ele_Policy_Rule_DataRef);
			refEle.setAttribute(ParserConstant.Attr_Refid, this.dataRef.refid);
			element.appendChild(refEle);
		}
		for (DesensitizeOperation op : operations) {
			Element opEle = document.createElement(ParserConstant.Ele_Policy_Rule_Desensitize_Operation);
			opEle.appendChild(document.createTextNode(op.name));
			element.appendChild(opEle);
		}

		return element;
	}

	@Override
	public Element outputType(Document document, String name) {
		throw new UnsupportedOperationException();
	}

	public void setOperations(Set<DesensitizeOperation> operations) {
		this.operations = operations;
	}

	public void setDataRef(DataRef dataRef) {
		this.dataRef = dataRef;
		if (dataRef != null) {
			this.dataRefId = dataRef.refid;
		} else {
			this.dataRefId = "";
		}
	}

	public DataRef getDataRef() {
		return dataRef;
	}

	public void setDataRefId(String dataRefId) {
		this.dataRefId = dataRefId;
	}

	public String getDataRefId() {
		return dataRefId;
	}

	public Set<DesensitizeOperation> getOperations() {
		return operations;
	}

	public Set<DataCategory> getDatas() {
		return datas;
	}

	public void materialize() {
		this.datas = new HashSet<>();
		this.datas.addAll(dataRef.getMaterialized());
	}

	public void materialize(Set<DataCategory> datas) {
		this.datas = datas;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("operation: ");
		if (operations != null) {
			sb.append(PSpecUtil.format(operations, " "));
		} else {
			sb.append("default");
		}
		return sb.toString();
	}

}
