package br.com.biblioteca.rest.api.dtos;

import java.util.Date;

import br.com.biblioteca.rest.api.models.Status;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthorDTO {

	private long id;
	private String name;
	private Status status;
	private String contact;
	private Date registrationDate;
	private String msgSuccess;
	private String msgError;

}
