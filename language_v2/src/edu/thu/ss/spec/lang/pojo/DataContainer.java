package edu.thu.ss.spec.lang.pojo;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import edu.thu.ss.spec.lang.parser.ParserConstant;

public class DataContainer extends CategoryContainer<DataCategory> {

	@Override
	public void parse(Node categoryNode) {
		super.parse(categoryNode);

		NodeList list = categoryNode.getChildNodes();
		for (int i = 0; i < list.getLength(); i++) {
			Node node = list.item(i);
			String name = node.getLocalName();
			if (ParserConstant.Ele_Vocabulary_Data_Category.equals(name)) {
				DataCategory data = new DataCategory();
				data.parse(node);
				data.setContainerId(this.id);
				set(data.getId(), data);
			}
		}
	}

	@Override
	public DataContainer getBaseContainer() {
		return (DataContainer) baseContainer;
	}

}
