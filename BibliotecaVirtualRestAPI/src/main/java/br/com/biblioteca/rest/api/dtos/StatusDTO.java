package br.com.biblioteca.rest.api.dtos;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StatusDTO {

	private Long id;
	private String description;
	private String nemotechnic;
	private Date registration_date;
	private String msgSuccess;
	private String msgError;
}
