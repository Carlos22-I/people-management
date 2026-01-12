package com.epiis.app.controller.reqresp;

import com.epiis.app.dto.DtoPerson;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RequestPersonInsert {
	@Getter
	@Setter
	public class Dto {
		private DtoPerson person;
	}
	
	private Dto dto = new Dto();
}
