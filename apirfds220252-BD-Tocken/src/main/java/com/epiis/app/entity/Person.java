package com.epiis.app.entity;

import java.sql.Date;

import com.epiis.app.generic.EntityGeneric;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "tperson")
@Getter
@Setter
public class Person extends EntityGeneric {
	@Id
	@Column(name = "idPerson")
	private String idPerson;
	
	@Column(name = "firstName")
	private String firstName;
	
	@Column(name = "surName")
	private String surName;
	
	@Column(name = "dni")
	private String dni;
	
	@Column(name = "gender")
	private boolean gender;
	
	@Column(name = "birthDate")
	private Date birthDate;
}
