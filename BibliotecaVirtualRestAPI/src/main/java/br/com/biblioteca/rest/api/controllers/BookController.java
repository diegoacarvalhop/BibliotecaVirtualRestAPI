package br.com.biblioteca.rest.api.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.biblioteca.rest.api.dtos.BookDTO;
import br.com.biblioteca.rest.api.models.Book;
import br.com.biblioteca.rest.api.services.BookService;

@RestController
@RequestMapping(value = "/biblioteca/livro")
public class BookController {

	@Autowired
	private BookService service;

	@PostMapping("/create")
	public BookDTO salvar(@RequestBody Book book) {
		return service.salvar(book);
	}

	@GetMapping("/listAll")
	public List<BookDTO> listarTodos() {
		List<BookDTO> dtos = service.listarTodos();
		return dtos;
	}

	@GetMapping("/{id}")
	public BookDTO listarPorId(@PathVariable(value = "id") long id) {
		return service.listarPorId(id);
	}

	@PutMapping("/edit/{id}")
	public BookDTO editar(@PathVariable(value = "id") long id, @RequestBody Book book) {
		return service.editar(id, book);
	}

	@DeleteMapping("/delete/{id}")
	public BookDTO deletar(@PathVariable(value = "id") long id) {
		return service.deletar(id);
	}

}
