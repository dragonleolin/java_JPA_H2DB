package com.example.demo.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.po.BookPO;

public interface BookDao extends CrudRepository<BookPO, Integer> {
	@Query(nativeQuery = true, value = "select * from books")
	public List<BookPO> findAllBook();

	@Query(nativeQuery = true, value = "select * from books order by name desc")
	public List<BookPO> sortName(String sort);

	@Query(nativeQuery = true, value = "select * from books order by author")
	public List<BookPO> sortAuthor(String sort);

	@Query(nativeQuery = true, value = "select * from books order by publicationDate desc")
	public List<BookPO> sortPublicationDate(String sort);

	@Query(nativeQuery = true, value = "select * from books where name like (%?1%)")
	Optional<BookPO> findBookByName(String name);

	@Transactional
	@Modifying
	@Query(nativeQuery = true, value = "INSERT INTO books (name, author, publicationdate) VALUES (?1, ?2, ?3)")
	public void addBook(String name, String author, String publicationDate);

	@Transactional
	@Modifying
	@Query(nativeQuery = true, value = "UPDATE books SET name=?1, author=?2, publicationDate=?3 WHERE name like (%?4%)")
	public void editBook(String name, String author, String publicationDate, String bookName);

	@Transactional
	@Modifying
	@Query(nativeQuery = true, value = "DELETE FROM books WHERE name=?1")
	public void deleteBook(String name);

}
