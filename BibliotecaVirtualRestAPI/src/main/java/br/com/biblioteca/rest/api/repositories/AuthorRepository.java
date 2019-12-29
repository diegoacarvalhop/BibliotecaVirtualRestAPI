package br.com.biblioteca.rest.api.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.biblioteca.rest.api.models.Author;
import br.com.biblioteca.rest.api.models.Status;

public interface AuthorRepository extends JpaRepository<Author, Long> {

	Author findByName(String name);

	List<Author> findByStatus(Status status);

	Author findById(long id);
}
