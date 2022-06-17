package com.example.demo.controller;

import static com.example.demo.constant.VerifyRegexConst.PATTERN_BOOK_NAME;
import static com.example.demo.constant.VerifyRegexConst.PATTERN_USER_CODE;

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
//1. 按照以下 API 規格進行開發
public class BookController {
	final Base64.Decoder decoder = Base64.getDecoder();
	final Base64.Encoder encoder = Base64.getEncoder();
	@Autowired
	private BookService bookService;

	// 6-1. 新增一排程每10秒鐘統計一次當前DB裡有多少本書，並在console log印出
	@Scheduled(fixedDelay = 10000) // fixedDelay = 60000 表示當前方法執行完畢 60000ms(1分鐘)後，Spring scheduling會再次呼叫該方法
	public void testFixDelay() {
		long num = bookService.countBook();
		System.out.println("There are " + num + " books in the Database");
		// 9-1. 說明 Lambda 的概念、使用方法
		// Lambda參考:https://www.tpisoftware.com/tpu/articleDetails/800
		// 優點:簡化函式
		List<BookDTO> bookScheduled = bookService.getAllBook();
		bookScheduled.forEach(System.out::println);

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

	// 1-1. API：查詢所有書籍
	// 8. 修改 Book API：查詢所有書籍
	@RequestMapping(method = RequestMethod.GET)
	public List<BookDTO> getAllBook(@RequestParam(value = "order", required = false) String sort) {
		System.out.println("sort=" + sort);
		if (sort == null) {
			return bookService.getAllBook();
		} else {
			return bookService.sortAllBooks(sort);
		}
	}

	// 1-2. API：以書名查詢書籍
	// 7-1. 除 API「更新書籍」外，其餘 API request 參數皆為必填
	@GetMapping(path = "/{bookName}")
	public BookDTO getBookByName(@Validator(pattern = PATTERN_BOOK_NAME) @PathVariable("bookName") String bookName) {
		System.out.println("name=" + bookName);
		return bookService.getBookByName(bookName);
	}

	// 1-3. API：新增書籍
	@PostMapping
	public BookDTO addBook(@RequestBody BookDTO book) {
		return bookService.addBook(book);
	}

	// 1-5. API：更新書籍
	@PutMapping(path = "/{bookName}")
	public BookDTO editBook(@PathVariable("bookName") String bookName, @RequestBody BookDTO book) {
		return bookService.editBookByName(bookName, book);
	}

	// 1-4. API：刪除書籍
	@DeleteMapping(path = "/{bookName}")
	public BookDTO removeBook(@PathVariable("bookName") String name) {
		BookDTO response = getBookByName(name);
		bookService.removeBookByName(name);
		return response;
	}

	// 5-2. 實作一個方法將一個字串做Base64編碼
	public String stringToBase64String(String inputString) throws Exception {
		final byte[] textByte = inputString.getBytes("UTF-8");
		// 編碼
		final String encodedText = encoder.encodeToString(textByte);
		// System.out.println("encodedText=" + encodedText);
		return encodedText;
	}

	// 5-3. 實作一個方法將byte[]做Base64編碼
	public String bytesToBase64String(byte[] bytes) {

		String bytesToBase64String = encoder.encodeToString(bytes);
		// System.out.println("bytesToBase64String=" + bytesToBase64String);
		return bytesToBase64String;
	}

	// 5-4. 實作一個方法將Base64字串做Base64解碼，並印出byte[]的長度
	void decodeBase64String(String base64String) throws Exception {
		// 解碼
		String decode = new String(decoder.decode(base64String), "UTF-8");
		// System.out.println("decode=" + decode);
		// System.out.println("length=" + decode.length());

	}

}
