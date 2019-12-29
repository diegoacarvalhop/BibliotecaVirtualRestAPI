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

import br.com.biblioteca.rest.api.dtos.StatusDTO;
import br.com.biblioteca.rest.api.models.Status;
import br.com.biblioteca.rest.api.services.StatusService;

@RestController
@RequestMapping(value = "/biblioteca/status")
public class StatusController {

	@Autowired
	private StatusService service;

	@PostMapping("/create")
	public StatusDTO salvar(@RequestBody Status status) {
		return service.salvar(status);
	}

	@GetMapping("/listAll")
	public List<StatusDTO> listarTodos() {
		List<StatusDTO> dtos = service.listarTodos();
		return dtos;
	}

	@GetMapping("/{id}")
	public StatusDTO listarPorId(@PathVariable(value = "id") long id) {
		return service.listarPorId(id);
	}

	@PutMapping("/edit/{id}")
	public StatusDTO editar(@PathVariable(value = "id") long id, @RequestBody Status status) {
		return service.editar(id, status);
	}

	@DeleteMapping("/delete/{id}")
	public StatusDTO deletar(@PathVariable(value = "id") long id) {
		return service.deletar(id);
	}

}
