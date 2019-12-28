package br.com.biblioteca.rest.api.controllers;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.biblioteca.rest.api.dtos.PublisherDTO;
import br.com.biblioteca.rest.api.models.Publisher;
import br.com.biblioteca.rest.api.services.PublisherService;

@RestController
@RequestMapping(value = "/biblioteca/editora")
public class PublisherController {

	private static final Logger logger = LogManager.getLogger(PublisherController.class);

	@Autowired
	private PublisherService service;

	@PostMapping("/create")
	public PublisherDTO salvar(@RequestBody Publisher publisher) {
		logger.info("Salvando Editora " + publisher.getDescription());
		return service.salvar(publisher);
	}

	@GetMapping("/listAll")
	public List<PublisherDTO> listarTodos() {
		List<PublisherDTO> dtos = service.listarTodos();
		return dtos;
	}

	@GetMapping("/{id}")
	public PublisherDTO listarPorId(@PathVariable(value = "id") long id) {
		return service.listarPorId(id);
	}

	@PutMapping("/edit/{id}")
	public PublisherDTO editar(@PathVariable(value = "id") long id, @RequestBody Publisher publisher) {
		return service.editar(id, publisher);
	}

	@DeleteMapping("/delete/{id}")
	public PublisherDTO deletar(@PathVariable(value = "id") long id) {
		return service.deletar(id);
	}

}
