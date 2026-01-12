package com.epiis.app.dto;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.epiis.app.generic.DtoGeneric;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DtoPerson extends DtoGeneric {
	private String idPerson;
	
	private String firstName;
	
	private String surName;
	
	private String dni;
	
	private boolean gender;
	
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date birthDate;
}
