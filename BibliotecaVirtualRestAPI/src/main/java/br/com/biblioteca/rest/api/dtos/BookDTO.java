package br.com.biblioteca.rest.api.dtos;

import java.util.Date;

import br.com.biblioteca.rest.api.models.Author;
import br.com.biblioteca.rest.api.models.Publisher;
import br.com.biblioteca.rest.api.models.Status;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BookDTO {

	private long id;
	private String title;
	private String synopsis;
	private Author author;
	private Publisher publisher;
	private Status status;
	private int totalAmount;
	private int availableQuantity;
	private Date registrationDate;
	private String msgSuccess;
	private String msgError;

}
