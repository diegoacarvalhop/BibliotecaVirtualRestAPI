package br.com.biblioteca.rest.api.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.biblioteca.rest.api.models.Publisher;
import br.com.biblioteca.rest.api.models.Status;

public interface PublisherRepository extends JpaRepository<Publisher, Long> {

	Publisher findByDescription(String description);

	List<Publisher> findByStatus(Status status);

	Publisher findById(long id);
}
