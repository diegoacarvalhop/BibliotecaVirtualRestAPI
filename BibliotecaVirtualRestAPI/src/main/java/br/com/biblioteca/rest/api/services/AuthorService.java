package br.com.biblioteca.rest.api.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.biblioteca.rest.api.dtos.AuthorDTO;
import br.com.biblioteca.rest.api.enuns.ValidacaoEnum;
import br.com.biblioteca.rest.api.models.Author;
import br.com.biblioteca.rest.api.repositories.AuthorRepository;

@Service
public class AuthorService {

	private AuthorRepository repository;

	@Autowired
	public AuthorService(AuthorRepository repository) {
		super();
		this.repository = repository;
	}

	public AuthorDTO salvar(Author author) {
		String validacao = validarAuthor(author);
		if (validacao.equals(ValidacaoEnum.OK.getDescricao())) {
			return validarAuthorExistente(author);
		} else {
			return montarDTOErro(author, validacao);
		}
	}

	public List<AuthorDTO> listarTodos() {
		List<Author> todosAutores = repository.findAll();
		List<AuthorDTO> dtos = new ArrayList<AuthorDTO>();
		for (Author author : todosAutores) {
			dtos.add(montarDTOSucesso(author, false, null));
		}
		return dtos;
	}

	public AuthorDTO listarPorId(long id) {
		Author author = repository.findById(id);
		if (author == null) {
			return montarDTOErro(null, ValidacaoEnum.NAO_EXISTE.getDescricao());
		} else {
			return montarDTOSucesso(author, false, null);
		}
	}

	public AuthorDTO editar(long id, Author author) {
		Author authorEditar = repository.findById(id);
		if (authorEditar == null) {
			return montarDTOErro(null, ValidacaoEnum.NAO_EXISTE.getDescricao());
		} else {
			if (validarAuthor(author) == ValidacaoEnum.OK.getDescricao()) {
				authorEditar.setName(author.getName());
				authorEditar.setStatus(author.getStatus());
				authorEditar.setContact(author.getContact());
				return montarDTOSucesso(repository.save(authorEditar), true, ValidacaoEnum.EDITAR.getDescricao());
			} else {
				return montarDTOErro(null, validarAuthor(author));
			}
		}
	}

	public AuthorDTO deletar(long id) {
		Author author = repository.findById(id);
		if (author == null) {
			return montarDTOErro(null, ValidacaoEnum.NAO_EXISTE.getDescricao());
		} else {
			repository.deleteById(id);
			return montarDTOSucesso(null, true, ValidacaoEnum.DELETAR.getDescricao());
		}
	}

	public AuthorDTO montarDTOSucesso(Author author, Boolean preencheMsgSucesso, String validacao) {
		AuthorDTO dto = new AuthorDTO();
		if (author != null) {
			dto.setId(author.getId());
			dto.setName(author.getName());
			dto.setStatus(author.getStatus());
			dto.setContact(author.getContact());
			dto.setRegistrationDate(author.getRegistrationDate());
			if (preencheMsgSucesso && validacao.equals(ValidacaoEnum.SALVAR.getDescricao())) {
				dto.setMsgSuccess("Autor " + author.getName() + " cadastrado com sucesso.");
			} else if (preencheMsgSucesso && validacao.equals(ValidacaoEnum.EDITAR.getDescricao())) {
				dto.setMsgSuccess("Autor " + author.getName() + " editado com sucesso.");
			}
		} else if (preencheMsgSucesso && validacao.equals(ValidacaoEnum.DELETAR.getDescricao())) {
			dto.setMsgSuccess("Autor deletado com sucesso.");
		}
		return dto;
	}

	public AuthorDTO montarDTOErro(Author author, String validacao) {
		AuthorDTO dto = new AuthorDTO();
		if (validacao.equals(ValidacaoEnum.EXISTE.getDescricao())) {
			dto.setMsgError("Já existe um Autor com o nome " + author.getName() + ".");
		}
		if (validacao.equals(ValidacaoEnum.BRANCO.getDescricao())) {
			dto.setMsgError("Não pode existir campos em branco.");
		}
		if (validacao.equals(ValidacaoEnum.NULL.getDescricao())) {
			dto.setMsgError("Não pode conter valores nulos.");
		}
		if (validacao.equals(ValidacaoEnum.NAO_EXISTE.getDescricao())) {
			dto.setMsgError("O Autor não existe.");
		}
		return dto;
	}

	public AuthorDTO validarAuthorExistente(Author author) {
		Author authorValidado = repository.findByName(author.getName());
		if (authorValidado != null) {
			return montarDTOErro(author, ValidacaoEnum.EXISTE.getDescricao());
		} else {
			return montarDTOSucesso(repository.save(author), true, ValidacaoEnum.SALVAR.getDescricao());
		}
	}

	public String validarAuthor(Author author) {
		try {
			if (author.getName().trim().isEmpty() || author.getStatus() == null) {
				return ValidacaoEnum.BRANCO.getDescricao();
			} else {
				return ValidacaoEnum.OK.getDescricao();
			}
		} catch (NullPointerException erro) {
			return ValidacaoEnum.NULL.getDescricao();
		}
	}

}
