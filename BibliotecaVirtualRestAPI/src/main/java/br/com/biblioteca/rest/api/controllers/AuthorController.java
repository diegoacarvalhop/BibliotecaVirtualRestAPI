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

import br.com.biblioteca.rest.api.dtos.AuthorDTO;
import br.com.biblioteca.rest.api.models.Author;
import br.com.biblioteca.rest.api.services.AuthorService;

@RestController
@RequestMapping(value = "/biblioteca/editora")
public class AuthorController {

	@Autowired
	private AuthorService service;

	@PostMapping("/create")
	public AuthorDTO salvar(@RequestBody Author author) {
		return service.salvar(author);
	}

	@GetMapping("/listAll")
	public List<AuthorDTO> listarTodos() {
		List<AuthorDTO> dtos = service.listarTodos();
		return dtos;
	}

	@GetMapping("/{id}")
	public AuthorDTO listarPorId(@PathVariable(value = "id") long id) {
		return service.listarPorId(id);
	}

	@PutMapping("/edit/{id}")
	public AuthorDTO editar(@PathVariable(value = "id") long id, @RequestBody Author author) {
		return service.editar(id, author);
	}

	@DeleteMapping("/delete/{id}")
	public AuthorDTO deletar(@PathVariable(value = "id") long id) {
		return service.deletar(id);
	}

}
