package br.com.biblioteca.rest.api.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.biblioteca.rest.api.dtos.BookDTO;
import br.com.biblioteca.rest.api.enuns.ValidacaoEnum;
import br.com.biblioteca.rest.api.models.Author;
import br.com.biblioteca.rest.api.models.Book;
import br.com.biblioteca.rest.api.models.Publisher;
import br.com.biblioteca.rest.api.models.Status;
import br.com.biblioteca.rest.api.repositories.AuthorRepository;
import br.com.biblioteca.rest.api.repositories.BookRepository;
import br.com.biblioteca.rest.api.repositories.PublisherRepository;
import br.com.biblioteca.rest.api.repositories.StatusRepository;

@Service
public class BookService {

	private BookRepository repository;
	private AuthorRepository authorRepository;
	private PublisherRepository publisherRepository;
	private StatusRepository statusRepository;

	@Autowired
	public BookService(BookRepository repository, AuthorRepository authorRepository,
			PublisherRepository publisherRepository, StatusRepository statusRepository) {
		super();
		this.repository = repository;
		this.authorRepository = authorRepository;
		this.publisherRepository = publisherRepository;
		this.statusRepository = statusRepository;
	}

	public BookDTO salvar(Book book) {
		String validacao = validarBook(book);
		if (validacao.equals(ValidacaoEnum.OK.getDescricao())) {
			return validarBookExistente(book);
		} else {
			return montarDTOErro(book, validacao);
		}
	}

	public List<BookDTO> listarTodos() {
		List<Book> todasEditoras = repository.findAll();
		List<BookDTO> dtos = new ArrayList<BookDTO>();
		for (Book book : todasEditoras) {
			dtos.add(montarDTOSucesso(book, false, null));
		}
		return dtos;
	}

	public BookDTO listarPorId(long id) {
		Book book = repository.findById(id);
		if (book == null) {
			return montarDTOErro(null, ValidacaoEnum.NAO_EXISTE.getDescricao());
		} else {
			return montarDTOSucesso(book, false, null);
		}
	}

	public BookDTO editar(long id, Book book) {
		Book bookEditar = repository.findById(id);
		if (bookEditar == null) {
			return montarDTOErro(null, ValidacaoEnum.NAO_EXISTE.getDescricao());
		} else {
			if (validarBook(book) == ValidacaoEnum.OK.getDescricao()) {
				bookEditar.setTitle(book.getTitle());
				bookEditar.setSynopsis(book.getSynopsis());
				bookEditar.setAuthor(book.getAuthor());
				bookEditar.setPublisher(book.getPublisher());
				bookEditar.setStatus(book.getStatus());
				bookEditar.setTotalAmount(book.getTotalAmount());
				bookEditar.setAvailableQuantity(book.getAvailableQuantity());
				return montarDTOSucesso(repository.save(bookEditar), true, ValidacaoEnum.EDITAR.getDescricao());
			} else {
				return montarDTOErro(null, validarBook(book));
			}
		}
	}

	public BookDTO deletar(long id) {
		Book book = repository.findById(id);
		if (book == null) {
			return montarDTOErro(null, ValidacaoEnum.NAO_EXISTE.getDescricao());
		} else {
			repository.deleteById(id);
			return montarDTOSucesso(null, true, ValidacaoEnum.DELETAR.getDescricao());
		}
	}

	public BookDTO montarDTOSucesso(Book book, Boolean preencheMsgSucesso, String validacao) {
		BookDTO dto = new BookDTO();
		if (book != null) {
			dto.setId(book.getId());
			dto.setTitle(book.getTitle());
			dto.setSynopsis(book.getSynopsis());
			dto.setAuthor(book.getAuthor());
			dto.setPublisher(book.getPublisher());
			dto.setStatus(book.getStatus());
			dto.setTotalAmount(book.getTotalAmount());
			dto.setAvailableQuantity(book.getAvailableQuantity());
			dto.setRegistrationDate(book.getRegistrationDate());
			if (preencheMsgSucesso && validacao.equals(ValidacaoEnum.SALVAR.getDescricao())) {
				dto.setMsgSuccess("Livro " + book.getTitle() + " cadastrado com sucesso.");
			} else if (preencheMsgSucesso && validacao.equals(ValidacaoEnum.EDITAR.getDescricao())) {
				dto.setMsgSuccess("Livro " + book.getTitle() + " editado com sucesso.");
			}
		} else if (preencheMsgSucesso && validacao.equals(ValidacaoEnum.DELETAR.getDescricao())) {
			dto.setMsgSuccess("Livro deletado com sucesso.");
		}
		return dto;
	}

	public BookDTO montarDTOErro(Book book, String validacao) {
		BookDTO dto = new BookDTO();
		if (validacao.equals(ValidacaoEnum.EXISTE.getDescricao())) {
			dto.setMsgError("Já existe um Livro " + book.getTitle() + " com o memso autor " + book.getAuthor().getName()
					+ " e editora " + book.getPublisher().getDescription() + ".");
		}
		if (validacao.equals(ValidacaoEnum.BRANCO.getDescricao())) {
			dto.setMsgError("Não pode existir campos em branco.");
		}
		if (validacao.equals(ValidacaoEnum.NULL.getDescricao())) {
			dto.setMsgError("Não pode conter valores nulos.");
		}
		if (validacao.equals(ValidacaoEnum.NAO_EXISTE.getDescricao())) {
			dto.setMsgError("O Livro não existe.");
		}
		if (validacao.equals(ValidacaoEnum.ZERO.getDescricao())) {
			dto.setMsgError("Valor total precisa ser maior que 0 (Zero).");
		}
		if (validacao.equals(ValidacaoEnum.STATUS_NAO_EXISTE.getDescricao())) {
			dto.setMsgError("O Status informado não existe.");
		}
		if (validacao.equals(ValidacaoEnum.PUBLISHER_NAO_EXISTE.getDescricao())) {
			dto.setMsgError("A Editora informada não existe.");
		}
		if (validacao.equals(ValidacaoEnum.AUTHOR_NAO_EXISTE.getDescricao())) {
			dto.setMsgError("O Autor informado não existe.");
		}
		return dto;
	}

	public BookDTO validarBookExistente(Book book) {
		Book bookValidado = repository.findByTitle(book.getTitle());
		if (bookValidado != null && (bookValidado.getTitle().equals(book.getTitle())
				&& bookValidado.getAuthor().getId() == book.getAuthor().getId()
				&& bookValidado.getPublisher().getId() == book.getPublisher().getId())) {
			return montarDTOErro(bookValidado, ValidacaoEnum.EXISTE.getDescricao());
		} else {
			return montarDTOSucesso(repository.save(book), true, ValidacaoEnum.SALVAR.getDescricao());
		}
	}

	public String validarBook(Book book) {
		try {
			Author author = authorRepository.findById(book.getAuthor().getId());
			Publisher publisher = publisherRepository.findById(book.getPublisher().getId());
			Status status = statusRepository.findById(book.getStatus().getId());
			if (author == null) {
				book.setAuthor(null);
				return ValidacaoEnum.AUTHOR_NAO_EXISTE.getDescricao();
			} else if (publisher == null) {
				book.setPublisher(null);
				return ValidacaoEnum.PUBLISHER_NAO_EXISTE.getDescricao();
			} else if (status == null) {
				book.setStatus(null);
				return ValidacaoEnum.STATUS_NAO_EXISTE.getDescricao();
			} else {
				if (book.getTotalAmount() > 0) {
					book.setAvailableQuantity(book.getTotalAmount());
				}
				if (book.getTitle().trim().isEmpty() || book.getStatus() == null || book.getPublisher() == null
						|| book.getAuthor() == null || book.getSynopsis().trim().isEmpty()) {
					if (book.getTotalAmount() <= 0) {
						return ValidacaoEnum.ZERO.getDescricao();
					} else {
						return ValidacaoEnum.BRANCO.getDescricao();
					}
				} else {
					return ValidacaoEnum.OK.getDescricao();
				}
			}
		} catch (NullPointerException erro) {
			return ValidacaoEnum.NULL.getDescricao();
		}
	}

}
