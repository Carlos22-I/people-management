package com.epiis.app.business;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import com.epiis.app.dataaccess.PersonRepository;
import com.epiis.app.dto.DtoPerson;
import com.epiis.app.entity.Person;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import java.util.Optional;

@Service
public class PersonBusiness {
	@Autowired
	private PersonRepository personRepository;

	public boolean insert(DtoPerson dtoPerson) {
		if (dtoPerson.getIdPerson() == null || dtoPerson.getIdPerson().trim().isEmpty()) {
			dtoPerson.setIdPerson(UUID.randomUUID().toString());
		}
		dtoPerson.setCreatedAt(new Date());
		dtoPerson.setUpdatedAt(dtoPerson.getCreatedAt());

		Person person = new Person();

		person.setIdPerson(dtoPerson.getIdPerson());
		person.setFirstName(dtoPerson.getFirstName());
		person.setSurName(dtoPerson.getSurName());
		person.setDni(dtoPerson.getDni());
		person.setGender(dtoPerson.isGender());
		person.setBirthDate(new java.sql.Date(dtoPerson.getBirthDate().getTime()));
		person.setCreatedAt(new java.sql.Timestamp(dtoPerson.getCreatedAt().getTime()));
		person.setUpdatedAt(new java.sql.Timestamp(dtoPerson.getUpdatedAt().getTime()));

		this.personRepository.save(person);

		return true;
	}

	public List<DtoPerson> getAll() {
		return convertToDtoList(this.personRepository.findAll());
	}

	public List<DtoPerson> getFiltered(String search) {
		if (search == null || search.trim().isEmpty()) {
			return getAll();
		}
		return convertToDtoList(personRepository.findByFirstNameContainingOrSurNameContaining(search, search));
	}

	private List<DtoPerson> convertToDtoList(List<Person> listPerson) {
		List<DtoPerson> listDtoPerson = new ArrayList<>();

		for (Person item : listPerson) {
			DtoPerson dtoPersonTemp = new DtoPerson();

			dtoPersonTemp.setIdPerson(item.getIdPerson());
			dtoPersonTemp.setFirstName(item.getFirstName());
			dtoPersonTemp.setSurName(item.getSurName());
			dtoPersonTemp.setDni(item.getDni());
			dtoPersonTemp.setGender(item.isGender());
			dtoPersonTemp.setBirthDate(item.getBirthDate());
			dtoPersonTemp.setCreatedAt(item.getCreatedAt());
			dtoPersonTemp.setUpdatedAt(item.getUpdatedAt());

			listDtoPerson.add(dtoPersonTemp);
		}

		return listDtoPerson;
	}

	public Person getById(@NonNull String id) {
		return personRepository.findById(id).orElse(null);
	}

	public Optional<Person> findById(@NonNull String id) {
		return personRepository.findById(id);
	}

	public Page<Person> getAllPaged(int page, int size) {
		return personRepository.findAll(PageRequest.of(page, size));
	}

	public List<Person> findByFirstName(String name) {
		return personRepository.findByFirstNameContaining(name);
	}

	public List<Person> findBySurNamePrefix(String prefix) {
		return personRepository.findBySurNameStartingWith(prefix);
	}

	public List<Person> findByBirthDateRange(Date start, Date end) {
		return personRepository.findByBirthDateBetween(
				new java.sql.Date(start.getTime()),
				new java.sql.Date(end.getTime()));
	}

	/**
	 * Verifica si ya existe una persona registrada con el DNI proporcionado.
	 *
	 * @param dni Número de DNI a verificar.
	 * @return true si existe, false en caso contrario.
	 */
	public boolean existsDni(String dni) {
		return personRepository.existsByDni(dni);
	}

	/**
	 * Cuenta el número total de personas en la base de datos.
	 *
	 * @return Cantidad total de registros.
	 */
	public long countPersons() {
		return personRepository.count();
	}

	/**
	 * Elimina una persona de la base de datos por su ID.
	 *
	 * @param id Identificador de la persona a eliminar.
	 */
	public void deleteById(@NonNull String id) {
		personRepository.deleteById(id);
	}
}
