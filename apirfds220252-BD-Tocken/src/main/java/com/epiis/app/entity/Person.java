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
	@Column(name = "idperson")
	private String idPerson;

	@Column(name = "firstname")
	private String firstName;

	@Column(name = "surname")
	private String surName;

	@Column(name = "dni")
	private String dni;

	@Column(name = "gender")
	private boolean gender;

	@Column(name = "birthdate")
	private Date birthDate;
}
