package edu.thu.ss.spec.lang.pojo;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import org.w3c.dom.Node;

/**
 * class for vocabulary
 * 
 * @author luochen
 * 
 */
public class Vocabulary {

	protected Info info = new Info();

	/**
	 * path of base vocabulary
	 */
	protected URI basePath;

	protected UserContainer userContainer = new UserContainer();

	protected DataContainer dataContainer = new DataContainer();

	/**
	 * only used during parsing
	 */
	protected Node rootNode;

	protected URI path;

	protected boolean resolved = false;

	public Vocabulary() {
	}

	public Vocabulary(String id) {
		info.setId(id);
	}

	public URI getPath() {
		return path;
	}

	public void setPath(URI path) {
		this.path = path;
	}

	public boolean isResolved() {
		return resolved;
	}

	public void setResolved(boolean resolved) {
		this.resolved = resolved;
	}

	public void setRootNode(Node rootNode) {
		this.rootNode = rootNode;
	}

	public Node getRootNode() {
		return rootNode;
	}

	public Info getInfo() {
		return info;
	}

	public void setInfo(Info info) {
		this.info = info;
	}

	public URI getBase() {
		return basePath;
	}

	public void setBase(URI base) {
		this.basePath = base;
	}

	public UserContainer getUserContainer() {
		return userContainer;
	}

	public DataContainer getDataContainer() {
		return dataContainer;
	}
}