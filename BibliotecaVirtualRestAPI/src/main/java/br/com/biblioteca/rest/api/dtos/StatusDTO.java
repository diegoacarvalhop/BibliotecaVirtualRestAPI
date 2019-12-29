package br.com.biblioteca.rest.api.dtos;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StatusDTO {

	private long id;
	private String description;
	private String nemotechnic;
	private String isActive;
	private Date registrationDate;
	private String msgSuccess;
	private String msgError;
}
