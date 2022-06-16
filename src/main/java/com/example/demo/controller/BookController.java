package com.example.demo.controller;

import static com.example.demo.constant.VerifyRegexConst.PATTERN_BOOK_NAME;

import java.io.UnsupportedEncodingException;
import java.util.Base64;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.BookDTO;
import com.example.demo.service.BookService;
import com.example.demo.validator.annotation.Validator;

@RestController
@RequestMapping(path = "/book")
public class BookController {
	final Base64.Decoder decoder = Base64.getDecoder();
	final Base64.Encoder encoder = Base64.getEncoder();
	@Autowired
	private BookService bookService;

	@Scheduled(fixedDelay = 10000) // fixedDelay = 60000 表示當前方法執行完畢 60000ms(1分鐘)後，Spring scheduling會再次呼叫該方法
	public void testFixDelay() {
		long num = bookService.countBook();
		System.out.println("資料庫裡有" + num + "本書");
		// Lambda參考:https://www.tpisoftware.com/tpu/articleDetails/800
		// 優點:簡化函式
		List<BookDTO> bookScheduled = bookService.getAllBook();
		try {
			String strEncode = stringToBase64String("string去編碼");
			decodeBase64String(strEncode);
			String byteEncode = bytesToBase64String("BYTE去編碼".getBytes());
			decodeBase64String(byteEncode);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@RequestMapping(method = RequestMethod.GET)
	public List<BookDTO> getAllBook(@RequestParam(value = "order", required = false) String sort) {
		System.out.println("sort=" + sort);
		if (sort == null) {
			return bookService.getAllBook();
		} else {
			return bookService.sortAllBooks(sort);
		}
	}

	@GetMapping(path = "/{bookName}")
	public BookDTO getBookByName(@PathVariable("bookName") String bookName) {
		System.out.println("name=" + bookName);
		return bookService.getBookByName(bookName);
	}

	@PostMapping
	public BookDTO addBook(@RequestBody BookDTO book) {
		return bookService.addBook(book);
	}

	@PutMapping(path = "/{bookName}")
	public BookDTO editBook(@PathVariable("bookName") String bookName, @RequestBody BookDTO book) {
		return bookService.editBookByName(bookName, book);
	}

	@DeleteMapping(path = "/{bookName}")
	public BookDTO removeBook(@PathVariable("bookName") String name) {
		BookDTO response = getBookByName(name);
		bookService.removeBookByName(name);
		return response;
	}

	public String stringToBase64String(String inputString) throws Exception {
		final byte[] textByte = inputString.getBytes("UTF-8");
		// 編碼
		final String encodedText = encoder.encodeToString(textByte);
		System.out.println("encodedText=" + encodedText);
		return encodedText;
	}

	public String bytesToBase64String(byte[] bytes) {

		String bytesToBase64String = encoder.encodeToString(bytes);
		System.out.println("bytesToBase64String=" + bytesToBase64String);
		return bytesToBase64String;
	}

	void decodeBase64String(String base64String) throws Exception {
		// 解碼
		String decode = new String(decoder.decode(base64String), "UTF-8");
		System.out.println("decode=" + decode);
		System.out.println("length=" + decode.length());

	}

}
