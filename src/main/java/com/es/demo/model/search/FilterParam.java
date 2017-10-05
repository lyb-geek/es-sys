package com.es.demo.model.search;

public class FilterParam {

	private String key;

	private Object value;

	private int type = 1;// 过滤类型，1表示match，2表示term,3表示multi_match，4、range,5 terms(构造sql的in查询)

	private int multiMatchflag = 1;// type为3时使用

	private int rangeType;// 1表示大于等于小于等于，2表示大于等于小于，3表示大于小于等于，4表示大于小于
	private Object maxValue;
	private Object minValue;

	private boolean isDate = false;// 是否为日期匹配
	private String dateFormat;// 日期格式

	private boolean isNested;// 是否是内嵌

	private String nestedPath;// 假如是内嵌，则需要内嵌path；

	private byte boolType = 1;// 1表示must，2表示filter，3表示should

	private int minimumShouldMatch = 1;// should查询时使用

	private boolean isNestedBool = false;// 是否内嵌bool，这个标志位用来，做（A and B） or D

	public boolean isDate() {
		return isDate;
	}

	public void setDate(boolean isDate) {
		this.isDate = isDate;
	}

	public String getDateFormat() {
		return dateFormat;
	}

	public void setDateFormat(String dateFormat) {
		this.dateFormat = dateFormat;
	}

	public int getMultiMatchflag() {
		return multiMatchflag;
	}

	public void setMultiMatchflag(int multiMatchflag) {
		this.multiMatchflag = multiMatchflag;
	}

	public int getRangeType() {
		return rangeType;
	}

	public void setRangeType(int rangeType) {
		this.rangeType = rangeType;
	}

	public Object getMaxValue() {
		return maxValue;
	}

	public void setMaxValue(Object maxValue) {
		this.maxValue = maxValue;
	}

	public Object getMinValue() {
		return minValue;
	}

	public void setMinValue(Object minValue) {
		this.minValue = minValue;
	}

	public byte getBoolType() {
		return boolType;
	}

	public void setBoolType(byte boolType) {
		this.boolType = boolType;
	}

	public int getMinimumShouldMatch() {
		return minimumShouldMatch;
	}

	public void setMinimumShouldMatch(int minimumShouldMatch) {
		this.minimumShouldMatch = minimumShouldMatch;
	}

	public boolean isNested() {
		return isNested;
	}

	public void setNested(boolean isNested) {
		this.isNested = isNested;
	}

	public String getNestedPath() {
		return nestedPath;
	}

	public void setNestedPath(String nestedPath) {
		this.nestedPath = nestedPath;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public boolean isNestedBool() {
		return isNestedBool;
	}

	public void setNestedBool(boolean isNestedBool) {
		this.isNestedBool = isNestedBool;
	}

}
