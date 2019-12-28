package br.com.biblioteca.rest.api.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.biblioteca.rest.api.models.Status;

public interface StatusRepository extends JpaRepository<Status, Long> {

	Status findByNemotechnic(String nemotechnic);
	Status findById(long id);
}
