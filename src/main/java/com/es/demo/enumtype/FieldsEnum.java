package com.es.demo.enumtype;

public enum FieldsEnum {

	USER(new String[] { "userName^8" }, 1);

	private String[] fields;
	private int flag;// 查询类型

	private FieldsEnum(String[] fields, int flag) {
		this.fields = fields;
		this.flag = flag;
	}

	public static String[] getFields(int flag) {

		FieldsEnum[] fieldsEnums = FieldsEnum.values();
		for (FieldsEnum fieldsEnum : fieldsEnums) {
			if (fieldsEnum.getFlag() == flag) {
				return fieldsEnum.getFields();
			}
		}

		return null;
	}

	public String[] getFields() {
		return fields;
	}

	public void setFields(String[] fields) {
		this.fields = fields;
	}

	public int getFlag() {
		return flag;
	}

	public void setFlag(int flag) {
		this.flag = flag;
	}

}
