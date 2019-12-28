package br.com.biblioteca.rest.api.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.biblioteca.rest.api.dtos.PublisherDTO;
import br.com.biblioteca.rest.api.enuns.ValidacaoEnum;
import br.com.biblioteca.rest.api.models.Publisher;
import br.com.biblioteca.rest.api.repositories.PublisherRepository;

@Service
public class PublisherService {

	private PublisherRepository repository;

	@Autowired
	public PublisherService(PublisherRepository repository) {
		super();
		this.repository = repository;
	}

	public PublisherDTO salvar(Publisher publisher) {
		String validacao = validarPublisher(publisher);
		if (validacao.equals(ValidacaoEnum.OK.getDescricao())) {
			return validarPublisherExistente(publisher);
		} else {
			return montarDTOErro(publisher, validacao);
		}
	}

	public List<PublisherDTO> listarTodos() {
		List<Publisher> todasEditoras = repository.findAll();
		List<PublisherDTO> dtos = new ArrayList<PublisherDTO>();
		for (Publisher publisher : todasEditoras) {
			dtos.add(montarDTOSucesso(publisher, false, null));
		}
		return dtos;
	}

	public PublisherDTO listarPorId(long id) {
		Publisher publisher = repository.findById(id);
		if (publisher == null) {
			return montarDTOErro(null, ValidacaoEnum.NAO_EXISTE.getDescricao());
		} else {
			return montarDTOSucesso(publisher, false, null);
		}
	}

	public PublisherDTO editar(long id, Publisher publisher) {
		Publisher publisherEditar = repository.findById(id);
		if (publisherEditar == null) {
			return montarDTOErro(null, ValidacaoEnum.NAO_EXISTE.getDescricao());
		} else {
			if (validarPublisher(publisher) == ValidacaoEnum.OK.getDescricao()) {
				publisherEditar.setDescription(publisher.getDescription());
				publisherEditar.setStatus(publisher.getStatus());
				publisherEditar.setContact(publisher.getContact());
				return montarDTOSucesso(repository.save(publisherEditar), true, ValidacaoEnum.EDITAR.getDescricao());
			} else {
				return montarDTOErro(null, validarPublisher(publisher));
			}
		}
	}

	public PublisherDTO deletar(long id) {
		Publisher publisher = repository.findById(id);
		if (publisher == null) {
			return montarDTOErro(null, ValidacaoEnum.NAO_EXISTE.getDescricao());
		} else {
			repository.deleteById(id);
			return montarDTOSucesso(null, true, ValidacaoEnum.DELETAR.getDescricao());
		}
	}

	public PublisherDTO montarDTOSucesso(Publisher publisher, Boolean preencheMsgSucesso, String validacao) {
		PublisherDTO dto = new PublisherDTO();
		if (publisher != null) {
			dto.setId(publisher.getId());
			dto.setDescription(publisher.getDescription());
			dto.setStatus(publisher.getStatus());
			dto.setContact(publisher.getContact());
			dto.setRegistration_date(publisher.getRegistration_date());
			if (preencheMsgSucesso && validacao.equals(ValidacaoEnum.SALVAR.getDescricao())) {
				dto.setMsgSuccess("Editora " + publisher.getDescription() + " cadastrada com sucesso.");
			} else if (preencheMsgSucesso && validacao.equals(ValidacaoEnum.EDITAR.getDescricao())) {
				dto.setMsgSuccess("Editora " + publisher.getDescription() + " editada com sucesso.");
			}
		} else if (preencheMsgSucesso && validacao.equals(ValidacaoEnum.DELETAR.getDescricao())) {
			dto.setMsgSuccess("Editora deletada com sucesso.");
		}
		return dto;
	}

	public PublisherDTO montarDTOErro(Publisher publisher, String validacao) {
		PublisherDTO dto = new PublisherDTO();
		if (validacao.equals(ValidacaoEnum.EXISTE.getDescricao())) {
			dto.setMsgError("Já existe uma Editora com a descrição " + publisher.getDescription() + ".");
		}
		if (validacao.equals(ValidacaoEnum.BRANCO.getDescricao())) {
			dto.setMsgError("Não pode existir campos em branco.");
		}
		if (validacao.equals(ValidacaoEnum.NULL.getDescricao())) {
			dto.setMsgError("Não pode conter valores nulos.");
		}
		if (validacao.equals(ValidacaoEnum.NAO_EXISTE.getDescricao())) {
			dto.setMsgError("A Editora não existe.");
		}
		return dto;
	}

	public PublisherDTO validarPublisherExistente(Publisher publisher) {
		Publisher publisherValidado = repository.findByDescription(publisher.getDescription());
		if (publisherValidado != null) {
			return montarDTOErro(publisher, ValidacaoEnum.EXISTE.getDescricao());
		} else {
			return montarDTOSucesso(repository.save(publisher), true, ValidacaoEnum.SALVAR.getDescricao());
		}
	}

	public String validarPublisher(Publisher publisher) {
		try {
			if (publisher.getDescription().trim().isEmpty() || publisher.getStatus() == null) {
				return ValidacaoEnum.BRANCO.getDescricao();
			} else {
				return ValidacaoEnum.OK.getDescricao();
			}
		} catch (NullPointerException erro) {
			return ValidacaoEnum.NULL.getDescricao();
		}
	}

}
