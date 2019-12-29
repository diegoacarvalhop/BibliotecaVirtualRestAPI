package br.com.biblioteca.rest.api.services;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.biblioteca.rest.api.controllers.StatusController;
import br.com.biblioteca.rest.api.dtos.StatusDTO;
import br.com.biblioteca.rest.api.enuns.ValidacaoEnum;
import br.com.biblioteca.rest.api.models.Author;
import br.com.biblioteca.rest.api.models.Publisher;
import br.com.biblioteca.rest.api.models.Status;
import br.com.biblioteca.rest.api.repositories.AuthorRepository;
import br.com.biblioteca.rest.api.repositories.PublisherRepository;
import br.com.biblioteca.rest.api.repositories.StatusRepository;

@Service
public class StatusService {

	private static final Logger logger = LogManager.getLogger(StatusController.class);

	private StatusRepository repository;
	private PublisherRepository repositoryPublisher;
	private AuthorRepository repositoryAuthor;

	@Autowired
	public StatusService(StatusRepository repository, PublisherRepository repositoryPublisher,
			AuthorRepository repositoryAuthor) {
		super();
		this.repository = repository;
		this.repositoryPublisher = repositoryPublisher;
		this.repositoryAuthor = repositoryAuthor;
	}

	public StatusDTO salvar(Status status) {
		logger.info("Salvando Status " + status.getDescription() + ".");
		String validacao = validarStatus(status, true);
		if (validacao.equals(ValidacaoEnum.OK.getDescricao())) {
			status.setNemotechnic(status.getNemotechnic().toUpperCase());
			status.setIsActive(ValidacaoEnum.SIM.getDescricao());
			return validarStatusExistente(status);
		} else {
			return montarDTOErro(status, validacao);
		}
	}

	public List<StatusDTO> listarTodos() {
		logger.info("Listando todos os Status.");
		List<Status> todosStatus = repository.findAll();
		List<StatusDTO> dtos = new ArrayList<StatusDTO>();
		for (Status status : todosStatus) {
			dtos.add(montarDTOSucesso(status, false, null));
		}
		return dtos;
	}

	public StatusDTO listarPorId(long id) {
		logger.info("Listando Status pelo ID.");
		Status status = repository.findById(id);
		if (status == null) {
			return montarDTOErro(null, ValidacaoEnum.NAO_EXISTE.getDescricao());
		} else {
			return montarDTOSucesso(status, false, null);
		}
	}

	public StatusDTO editar(long id, Status status) {
		Status statusEditar = repository.findById(id);
		if (statusEditar == null) {
			return montarDTOErro(null, ValidacaoEnum.NAO_EXISTE.getDescricao());
		} else {
			if (validarStatus(status, false) == ValidacaoEnum.OK.getDescricao()) {
				logger.info("Editando Status ." + status.getDescription() + ".");
				statusEditar.setDescription(status.getDescription());
				statusEditar.setNemotechnic(status.getNemotechnic().toUpperCase());
				statusEditar.setIsActive(status.getIsActive());
				return montarDTOSucesso(repository.save(statusEditar), true, ValidacaoEnum.EDITAR.getDescricao());
			} else {
				return montarDTOErro(null, validarStatus(status, false));
			}
		}
	}

	public StatusDTO deletar(long id) {
		Status status = repository.findById(id);
		if (status == null) {
			return montarDTOErro(null, ValidacaoEnum.NAO_EXISTE.getDescricao());
		} else {
			if (validarDeletar(status)) {
				logger.info("Deletando Status " + status.getDescription() + ".");
				repository.deleteById(id);
				return montarDTOSucesso(null, true, ValidacaoEnum.DELETAR.getDescricao());
			}
			return montarDTOErro(null, ValidacaoEnum.FK.getDescricao());
		}
	}

	public StatusDTO montarDTOSucesso(Status status, Boolean preencheMsgSucesso, String validacao) {
		logger.info("Montando objeto de sucesso.");
		StatusDTO dto = new StatusDTO();
		if (status != null) {
			dto.setId(status.getId());
			dto.setDescription(status.getDescription());
			dto.setNemotechnic(status.getNemotechnic());
			dto.setIsActive(status.getIsActive());
			dto.setRegistrationDate(status.getRegistrationDate());
			if (preencheMsgSucesso && validacao.equals(ValidacaoEnum.SALVAR.getDescricao())) {
				dto.setMsgSuccess("Status " + status.getDescription() + " cadastrado com sucesso.");
			} else if (preencheMsgSucesso && validacao.equals(ValidacaoEnum.EDITAR.getDescricao())) {
				dto.setMsgSuccess("Status " + status.getDescription() + " editado com sucesso.");
			}
		} else if (preencheMsgSucesso && validacao.equals(ValidacaoEnum.DELETAR.getDescricao())) {
			dto.setMsgSuccess("Status deletado com sucesso.");
		}
		return dto;
	}

	public StatusDTO montarDTOErro(Status status, String validacao) {
		logger.info("Montando objeto de erro.");
		StatusDTO dto = new StatusDTO();
		if (validacao.equals(ValidacaoEnum.EXISTE.getDescricao())) {
			dto.setMsgError("Já existe um Status com o nemotécnico " + status.getNemotechnic() + ".");
		}
		if (validacao.equals(ValidacaoEnum.BRANCO.getDescricao())) {
			dto.setMsgError("Não pode existir campos em branco.");
		}
		if (validacao.equals(ValidacaoEnum.NULL.getDescricao())) {
			dto.setMsgError("Não pode conter valores nulos.");
		}
		if (validacao.equals(ValidacaoEnum.NAO_EXISTE.getDescricao())) {
			dto.setMsgError("O Status não existe.");
		}
		if (validacao.equals(ValidacaoEnum.FK.getDescricao())) {
			dto.setMsgError("O Status não pode ser deletado por estar associado a outro retistro.");
		}
		return dto;
	}

	public StatusDTO validarStatusExistente(Status status) {
		logger.info("Validando se o Status " + status.getDescription() + " já existe.");
		Status statusValidado = repository.findByNemotechnic(status.getNemotechnic());
		if (statusValidado != null) {
			return montarDTOErro(status, ValidacaoEnum.EXISTE.getDescricao());
		} else {
			return montarDTOSucesso(repository.save(status), true, ValidacaoEnum.SALVAR.getDescricao());
		}
	}

	public String validarStatus(Status status, boolean salvar) {
		logger.info("Validando campos do Status.");
		try {
			if (salvar) {
				if (status.getDescription().trim().isEmpty() || status.getNemotechnic().trim().isEmpty()) {
					return ValidacaoEnum.BRANCO.getDescricao();
				} else {
					return ValidacaoEnum.OK.getDescricao();
				}
			} else {
				if (status.getDescription().trim().isEmpty() || status.getNemotechnic().trim().isEmpty()
						|| status.getIsActive().isEmpty()) {
					return ValidacaoEnum.BRANCO.getDescricao();
				} else {
					return ValidacaoEnum.OK.getDescricao();
				}
			}
		} catch (NullPointerException erro) {
			return ValidacaoEnum.NULL.getDescricao();
		}
	}

	public boolean validarDeletar(Status status) {
		logger.info("Validando se o Status " + status.getDescription() + " está sendo usado.");
		List<Publisher> publishers = repositoryPublisher.findByStatus(status);
		List<Author> authors = repositoryAuthor.findByStatus(status);
		if (publishers.size() != 0 || authors.size() != 0) {
			return false;
		} else {
			return true;
		}
	}

}
