package br.com.biblioteca.rest.api.models;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "BOOK")
public class Book implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@Column(nullable = false)
	private String title;

	@Column(nullable = false)
	private String synopsis;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "author_id", nullable = false, foreignKey = @ForeignKey(name = "fk_author"))
	private Author author;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "publisher_id", nullable = false, foreignKey = @ForeignKey(name = "fk_publisher"))
	private Publisher publisher;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "status_id", nullable = false, foreignKey = @ForeignKey(name = "fk_status"))
	private Status status;

	@Column(name = "total_amount", nullable = false)
	private int totalAmount;

	@Column(name = "available_quantity", nullable = false)
	private int availableQuantity;

	@CreationTimestamp
	@Column(name = "registration_date")
	private Date registrationDate;

}
