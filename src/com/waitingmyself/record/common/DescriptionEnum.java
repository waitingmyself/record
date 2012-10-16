package com.waitingmyself.record.common;

public enum DescriptionEnum {

	BLANK, P, S, FSN;
	
	public static DescriptionEnum getInstance(Object value) {
		if(value == null) {
			return BLANK;
		}
		String v = "";
		if(value instanceof String) {
			v = (String)value;
		} else if(value instanceof Integer) {
			v = value + "";
		} else  {
			v = value.toString();
		}
		for (DescriptionEnum model : DescriptionEnum.values()) {
			if(v.equals(model.ordinal() + "")) {
				return model;
			}
		}
		return BLANK;
	}
	
	public String toDisplayStr() {
		return "[" + this.name() + "]";
	}

}
