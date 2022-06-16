package com.example.demo.po;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Persistant Object 持久物件
 *
 * 一個 PO 就是 DB 中的一條記錄。好處是可以把一條記錄作為一個物件處理，可以方便的轉為其它物件。
 */
@Entity
@Table(name = "Books")
@DynamicInsert
@DynamicUpdate
public class BookPO {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@Column(name = "name", length = 255)
	private String name;

	@Column(name = "author", length = 255)
	private String author;

	@Column(name = "publicationdate", length = 100)
	private String publicationDate;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getPublicationDate() {
		return publicationDate;
	}

	public void setPublicationDate(String publicationDate) {
		this.publicationDate = publicationDate;
	}

}
