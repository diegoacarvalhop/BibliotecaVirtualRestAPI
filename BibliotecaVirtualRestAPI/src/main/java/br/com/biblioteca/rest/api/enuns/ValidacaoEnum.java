package br.com.biblioteca.rest.api.enuns;

import lombok.Getter;

@Getter
public enum ValidacaoEnum {

	EXISTE("existe"), NAO_EXISTE("naoExiste"), NULL("nulo"), BRANCO("branco"), OK("ok"), SALVAR("salvar"),
	EDITAR("editar"), DELETAR("deletar"), SIM("S"), NAO("N"), FK("foreignKey"), ZERO("zero"),
	STATUS_NAO_EXISTE("statusNaoExiste"), PUBLISHER_NAO_EXISTE("publisherNaoExiste"),
	AUTHOR_NAO_EXISTE("authorNaoExiste");

	private String descricao;

	ValidacaoEnum(String descricao) {
		this.descricao = descricao;
	}

}
