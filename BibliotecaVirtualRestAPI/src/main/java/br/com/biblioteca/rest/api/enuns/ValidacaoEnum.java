package br.com.biblioteca.rest.api.enuns;

import lombok.Getter;

@Getter
public enum ValidacaoEnum {

	EXISTE("existe"), NAO_EXISTE("naoExiste"), NULL("nulo"), BRANCO("branco"), OK("ok"), SALVAR("salvar"),
	EDITAR("editar"), DELETAR("deletar"), SIM("S"), NAO("N");

	private String descricao;

	ValidacaoEnum(String descricao) {
		this.descricao = descricao;
	}

}
