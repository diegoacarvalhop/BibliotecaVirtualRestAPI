package br.com.biblioteca.rest.api.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.biblioteca.rest.api.models.Book;
import br.com.biblioteca.rest.api.models.Status;

public interface BookRepository extends JpaRepository<Book, Long> {

	Book findByTitle(String title);

	List<Book> findByStatus(Status status);

	Book findById(long id);
}
