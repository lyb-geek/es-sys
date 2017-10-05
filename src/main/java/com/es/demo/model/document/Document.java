package com.es.demo.model.document;

public class Document {

	private String index;
	private String type;
	private String id;

	private Object content;

	public String getIndex() {
		return index;
	}

	public void setIndex(String index) {
		this.index = index;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Object getContent() {
		return content;
	}

	public void setContent(Object content) {
		this.content = content;
	}

}
