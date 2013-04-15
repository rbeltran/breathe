package com.jphat.Breathe.util;

import com.jphat.Breathe.R;

public enum RawEnum {
	
	RAW_ENUM_GOOD_AFTERNOON(R.raw.good_afternoon),
	RAW_ENUM_GOOD_EVENING(R.raw.good_evening),
	RAW_ENUM_GOOD_MORNING(R.raw.good_morning),
	RAW_ENUM_HAVE_A_GOOD_DAY(R.raw.have_a_good_day),
	RAW_ENUM_IM_SORRY(R.raw.im_sorry),
	RAW_ENUM_PLEASE(R.raw.please),
	RAW_ENUM_THANK_PATIENCE(R.raw.thank_you_for_your_patience),
	RAW_ENUM_THANK_YOU(R.raw.thank_you);
	
	private final int id;
	RawEnum( int id ) {
		this.id = id;
	}
	public int getValue() {
		return this.id;
	}
}
