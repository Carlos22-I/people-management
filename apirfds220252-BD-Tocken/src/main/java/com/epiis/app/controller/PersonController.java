package com.epiis.app.controller;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.*;

import com.epiis.app.business.PersonBusiness;
import com.epiis.app.controller.reqresp.RequestPersonInsert;
import com.epiis.app.controller.reqresp.ResponsePersonInsert;
import com.epiis.app.dto.DtoPerson;
import com.epiis.app.entity.Person;

@RestController
@RequestMapping("person/")
public class PersonController {

	@Autowired
	private PersonBusiness personBusiness;

	/**
	 * Inserta o actualiza una persona en la base de datos.
	 * Recibe un objeto RequestPersonInsert que contiene los datos de la persona.
	 * Si el ID de la persona ya existe, se actualiza; si no, se crea uno nuevo.
	 */
	@PostMapping("insert")
	public ResponseEntity<ResponsePersonInsert> insert(@RequestBody RequestPersonInsert request) {
		ResponsePersonInsert response = new ResponsePersonInsert();
		personBusiness.insert(request.getDto().getPerson());
		response.success();
		response.listMessage.add("Operación realizada correctamente");
		return ResponseEntity.ok(response);
	}

	/**
	 * Obtiene la lista completa de personas registradas.
	 * Devuelve una lista de objetos DtoPerson.
	 */
	@GetMapping("getall")
	public ResponseEntity<List<DtoPerson>> getAll() {
		return ResponseEntity.ok(personBusiness.getAll());
	}

	/**
	 * Obtiene una persona por su ID.
	 * Devuelve el objeto Person correspondiente o null si no existe.
	 */
	@GetMapping("get/{id}")
	public ResponseEntity<Person> getById(@PathVariable @NonNull String id) {
		return ResponseEntity.ok(personBusiness.getById(id));
	}

	/**
	 * Busca una persona por su ID y devuelve un ResponseEntity con el resultado.
	 * Si la persona no se encuentra, devuelve un estado 404 (Not Found).
	 */
	@GetMapping("find/{id}")
	public ResponseEntity<?> findById(@PathVariable @NonNull String id) {
		return personBusiness.findById(id)
				.map(ResponseEntity::ok)
				.orElse(ResponseEntity.notFound().build());
	}

	/**
	 * Obtiene una lista paginada de personas.
	 * Recibe el número de página y el tamaño de la página como parámetros.
	 */
	@GetMapping("page")
	public ResponseEntity<?> paged(
			@RequestParam int page,
			@RequestParam int size) {
		return ResponseEntity.ok(personBusiness.getAllPaged(page, size));
	}

	/**
	 * Busca personas cuyo nombre contenga el valor especificado.
	 */
	@GetMapping("search-name")
	public ResponseEntity<?> searchName(@RequestParam String value) {
		return ResponseEntity.ok(personBusiness.findByFirstName(value));
	}

	/**
	 * Busca personas cuyo apellido comience con el prefijo especificado.
	 */
	@GetMapping("search-surname")
	public ResponseEntity<?> searchSurname(@RequestParam String value) {
		return ResponseEntity.ok(personBusiness.findBySurNamePrefix(value));
	}

	/**
	 * Busca personas nacidas dentro de un rango de fechas.
	 */
	@GetMapping("search-birth")
	public ResponseEntity<?> searchBirth(
			@RequestParam Date start,
			@RequestParam Date end) {
		return ResponseEntity.ok(personBusiness.findByBirthDateRange(start, end));
	}

	/**
	 * Verifica si existe una persona con el DNI especificado.
	 * Devuelve true si existe, false en caso contrario.
	 */
	@GetMapping("exists-dni/{dni}")
	public ResponseEntity<Boolean> existsDni(@PathVariable String dni) {
		return ResponseEntity.ok(personBusiness.existsDni(dni));
	}

	/**
	 * Devuelve el número total de personas registradas en la base de datos.
	 */
	@GetMapping("count")
	public ResponseEntity<Long> count() {
		return ResponseEntity.ok(personBusiness.countPersons());
	}

	/**
	 * Elimina una persona por su ID.
	 * Devuelve un estado 200 (OK) si la operación fue exitosa.
	 */
	@DeleteMapping("delete/{id}")
	public ResponseEntity<Void> delete(@PathVariable @NonNull String id) {
		personBusiness.deleteById(id);
		return ResponseEntity.ok().build();
	}
}
