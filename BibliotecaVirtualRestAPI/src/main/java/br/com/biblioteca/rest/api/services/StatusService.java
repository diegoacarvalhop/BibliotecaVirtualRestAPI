package br.com.biblioteca.rest.api.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.biblioteca.rest.api.dtos.StatusDTO;
import br.com.biblioteca.rest.api.enuns.ValidacaoEnum;
import br.com.biblioteca.rest.api.models.Status;
import br.com.biblioteca.rest.api.repositories.StatusRepository;

@Service
public class StatusService {

	private StatusRepository repository;

	@Autowired
	public StatusService(StatusRepository repository) {
		super();
		this.repository = repository;
	}

	public StatusDTO salvar(Status status) {
		String validacao = validarStatus(status);
		if (validacao.equals(ValidacaoEnum.OK.getDescricao())) {
			status.setNemotechnic(status.getNemotechnic().toUpperCase());
			status.setIsActive(ValidacaoEnum.SIM.getDescricao());
			return validarStatusExistente(status);
		} else {
			return montarDTOErro(status, validacao);
		}
	}

	public List<StatusDTO> listarTodos() {
		List<Status> todosStatus = repository.findAll();
		List<StatusDTO> dtos = new ArrayList<StatusDTO>();
		for (Status status : todosStatus) {
			dtos.add(montarDTOSucesso(status, false, null));
		}
		return dtos;
	}

	public StatusDTO listarPorId(long id) {
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
			if (validarStatus(status) == ValidacaoEnum.OK.getDescricao()) {
				statusEditar.setDescription(status.getDescription());
				statusEditar.setNemotechnic(status.getNemotechnic().toUpperCase());
				statusEditar.setIsActive(status.getIsActive());
				return montarDTOSucesso(repository.save(statusEditar), true, ValidacaoEnum.EDITAR.getDescricao());
			} else {
				return montarDTOErro(null, validarStatus(status));
			}
		}
	}

	public StatusDTO deletar(long id) {
		Status status = repository.findById(id);
		if (status == null) {
			return montarDTOErro(null, ValidacaoEnum.NAO_EXISTE.getDescricao());
		} else {
			repository.deleteById(id);
			return montarDTOSucesso(null, true, ValidacaoEnum.DELETAR.getDescricao());
		}
	}

	public StatusDTO montarDTOSucesso(Status status, Boolean preencheMsgSucesso, String validacao) {
		StatusDTO dto = new StatusDTO();
		if (status != null) {
			dto.setId(status.getId());
			dto.setDescription(status.getDescription());
			dto.setNemotechnic(status.getNemotechnic());
			dto.setIsActive(status.getIsActive());
			dto.setRegistration_date(status.getRegistration_date());
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
		return dto;
	}

	public StatusDTO validarStatusExistente(Status status) {
		Status statusValidado = repository.findByNemotechnic(status.getNemotechnic());
		if (statusValidado != null) {
			return montarDTOErro(status, ValidacaoEnum.EXISTE.getDescricao());
		} else {
			return montarDTOSucesso(repository.save(status), true, ValidacaoEnum.SALVAR.getDescricao());
		}
	}

	public String validarStatus(Status status) {
		try {
			if (status.getDescription().trim().isEmpty() || status.getNemotechnic().trim().isEmpty()) {
				return ValidacaoEnum.BRANCO.getDescricao();
			} else {
				return ValidacaoEnum.OK.getDescricao();
			}
		} catch (NullPointerException erro) {
			return ValidacaoEnum.NULL.getDescricao();
		}
	}

}
