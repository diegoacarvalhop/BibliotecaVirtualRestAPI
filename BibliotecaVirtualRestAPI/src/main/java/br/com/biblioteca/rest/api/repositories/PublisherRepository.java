package br.com.biblioteca.rest.api.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.biblioteca.rest.api.models.Publisher;

public interface PublisherRepository extends JpaRepository<Publisher, Long> {

	Publisher findByDescription(String description);

	Publisher findById(long id);
}
