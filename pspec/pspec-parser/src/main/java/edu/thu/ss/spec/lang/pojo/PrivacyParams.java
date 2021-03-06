package edu.thu.ss.spec.lang.pojo;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import edu.thu.ss.spec.lang.parser.ParserConstant;

public class PrivacyParams implements Parsable {

	private PrivacyBudget<?> budget;

	private double probability = 0.0;

	private double noiseRatio = 1.0;

	private boolean checkAccuracy = false;

	@Override
	public void parse(Node budgetNode) {
		NodeList list = budgetNode.getChildNodes();
		for (int i = 0; i < list.getLength(); i++) {
			Node node = list.item(i);
			String name = node.getLocalName();
			if (ParserConstant.Ele_Policy_DP_Global_Budget.equals(name)) {
				budget = new GlobalBudget();
				budget.parse(node);
			} else if (ParserConstant.Ele_Policy_DP_Fine_Budget.equals(name)) {
				budget = new FineBudget();
				budget.parse(node);
			} else if (ParserConstant.Ele_Policy_DP_Accuracy.equals(name)) {
				parseAccuracy(node);
				checkAccuracy = true;
			}
		}
	}

	private void parseAccuracy(Node accuracyNode) {
		NodeList list = accuracyNode.getChildNodes();
		for (int i = 0; i < list.getLength(); i++) {
			Node node = list.item(i);
			String name = node.getLocalName();
			if (ParserConstant.Ele_Policy_DP_Accuracy_Probability.equals(name)) {
				probability = Double.valueOf(node.getTextContent());
			} else if (ParserConstant.Ele_Policy_DP_Accuracy_Noise.equals(name)) {
				noiseRatio = Double.valueOf(node.getTextContent());
			}
		}
	}
	
	public boolean isCheckAccuracy() {
		return checkAccuracy;
	}

	public double getProbability() {
		return probability;
	}

	public double getNoiseRatio() {
		return noiseRatio;
	}

	public PrivacyBudget<?> getPrivacyBudget() {
		return budget;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(budget);
		sb.append("Accuracy bound: ");
		sb.append("in probability [");
		sb.append(probability);
		sb.append("] noise ratio within [");
		sb.append(noiseRatio);
		sb.append("]");
		return sb.toString();

	}
}
