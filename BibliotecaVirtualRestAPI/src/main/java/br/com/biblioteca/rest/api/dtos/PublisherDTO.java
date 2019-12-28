package br.com.biblioteca.rest.api.dtos;

import java.util.Date;

import br.com.biblioteca.rest.api.models.Status;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PublisherDTO {

	private long id;
	private String description;
	private Status status;
	private String contact;
	private Date registration_date;
	private String msgSuccess;
	private String msgError;

}
