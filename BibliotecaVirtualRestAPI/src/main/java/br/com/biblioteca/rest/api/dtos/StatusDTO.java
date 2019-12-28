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
	private Date registration_date;
	private String msgSuccess;
	private String msgError;
}
