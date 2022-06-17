package com.example.demo.service;

import static com.example.demo.constant.CacheConst.CACHE_NAME_BOOK_CACHE;
import static com.example.demo.constant.DemoResponseConst.ReturnCode.E0001;
import static com.example.demo.constant.PropertiesConst.PropertiesKeyEL.RSA_PRIVATE_KEY;
import static java.util.stream.Collectors.toList;

import java.util.List;
import java.util.stream.StreamSupport;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import static com.example.demo.constant.DemoResponseConst.ReturnCode.E9997;
import com.example.demo.dao.BookDao;
import com.example.demo.dto.BookDTO;
import com.example.demo.error.DemoException;
import com.example.demo.util.CryptoUtil;

@Service
public class BookService {
	private static final Logger LOGGER = LoggerFactory.getLogger(BookService.class);

	@Autowired
	private BookDao bookDao;
	@Autowired
	private PasswordEncoder passwordEncoder;
	@Value(RSA_PRIVATE_KEY)
	private String rsaPrivateKey;

	@Cacheable(cacheNames = CACHE_NAME_BOOK_CACHE, key = "#root.methodName")
	public long countBook() {
		return bookDao.count();
	}

	// 10. Stream
	public List<BookDTO> sortAllBooks(String orderType) {
		if (orderType.equals("author")) {
			return StreamSupport.stream(bookDao.sortAuthor(orderType).spliterator(), false).map(BookDTO::createByBookPO)
					.collect(toList());
		} else if (orderType.equals("publicationDate")) {
			return StreamSupport.stream(bookDao.sortPublicationDate(orderType).spliterator(), false)
					.map(BookDTO::createByBookPO).collect(toList());
		} else {
			return StreamSupport.stream(bookDao.sortName(orderType).spliterator(), false).map(BookDTO::createByBookPO)
					.collect(toList());
		}

	}

	// 2-2. 查詢時若有Cache則回傳Cache，若沒Cache則重新查詢後更新Cache並回傳
	// @Cacheable(cacheNames = CACHE_NAME_BOOK_CACHE, key = "#root.methodName")
	public List<BookDTO> getAllBook() {
		return StreamSupport.stream(bookDao.findAllBook().spliterator(), false).map(BookDTO::createByBookPO)
				.collect(toList());
	}

	@Cacheable(cacheNames = CACHE_NAME_BOOK_CACHE, key = "#bookName")
	public BookDTO getBookByName(String bookName) {
		return bookDao.findBookByName(bookName).map(BookDTO::createByBookPO)
				.orElseThrow(() -> DemoException.createByCode(E0001));
	}

	// 3-1. 設計一API：同時新增使用者、書籍，並以 Transaction 控制若有任何例外發生，則 rollback
	// 2-3. 變更時更新Cache
	@Transactional
	@CachePut(cacheNames = CACHE_NAME_BOOK_CACHE, key = "#bookDTO.name")
	public BookDTO addBook(BookDTO bookDTO) {
		BookDTO response = bookDTO;
		String name = bookDTO.getName();
		String author = bookDTO.getAuthor();
		String date = bookDTO.getPublicationDate();
		String publicationDate = "";
		publicationDate = date.substring(0, 4) + "-" + date.substring(4, 6) + "-" + date.substring(6, 8);
		System.out.println("publicationDate=" + publicationDate);
		bookDao.addBook(name, author, publicationDate);
		return response;
	}

	// 3-2. 設計一API：同時更新使用者、書籍，並以 Transaction 控制若有任何例外發生也不影響已經執行的 DB 操作
	@Transactional
	@CachePut(cacheNames = CACHE_NAME_BOOK_CACHE, key = "#result.name")
	public BookDTO editBookByName(String bookName, BookDTO book) {
		BookDTO response = null;
		String name = book.getName();
		String author = book.getAuthor();
		String date = book.getPublicationDate();
		String publicationDate = "";
		publicationDate = date.substring(0, 4) + "-" + date.substring(4, 6) + "-" + date.substring(6, 8);
		System.out.println("publicationDate=" + publicationDate);
		bookDao.editBook(name, author, publicationDate, bookName);
		response = book;
		return response;
	}

	// 2-4. 刪除時清除Cache
	@Transactional
	@CacheEvict(cacheNames = CACHE_NAME_BOOK_CACHE, allEntries = true)
	public void removeBookByName(String bookName) {
		bookDao.deleteBook(bookName);
	}

	private String processPcode(String inputPcode) {
		String decryptedPcode;
		try {
			decryptedPcode = CryptoUtil.rsaDecryptByPrivateKey(inputPcode, rsaPrivateKey);
		} catch (Exception e) {
			LOGGER.error("Decrypted failed by value: ", e);
			throw DemoException.createByCode(E9997);
		}

		return passwordEncoder.encode(decryptedPcode);
	}
}
