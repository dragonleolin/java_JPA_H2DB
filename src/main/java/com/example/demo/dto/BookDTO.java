package com.example.demo.dto;

import static com.example.demo.constant.VerifyRegexConst.PATTERN_BOOK_NAME;
import static com.example.demo.constant.VerifyRegexConst.PATTERN_BOOK_AUTHOR;
import static com.example.demo.constant.VerifyRegexConst.PATTERN_BOOK_PUBLICATIONDATE;

import javax.validation.constraints.Pattern;

import org.springframework.beans.BeanUtils;

import com.example.demo.bo.BookBO;
import com.example.demo.po.BookPO;
import com.example.demo.validator.annotation.Validator;
import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Data Transfer Object
 *
 * 數據傳輸物件主要用於遠程調用等需要大量傳輸物件的地方。
 * 比如我們一張表有100個欄位，那麼對應的 PO 就有100個屬性。
 * 但是我們畫面上只要顯示其中的10個欄位，客戶端用 WEB service 來獲取數據，沒有必要把整個 PO 物件傳遞到客戶端，
 * 這時我們就可以用只有這10個屬性的 DTO 來傳遞結果到客戶端，這樣也不會暴露服務端表結構。
 */

public class BookDTO {

    @Validator(notNull = false, pattern = PATTERN_BOOK_NAME)
    private String name;
    @Validator(notNull = false, pattern = PATTERN_BOOK_AUTHOR)
    private String author;
    @Validator(notNull = false, pattern = PATTERN_BOOK_PUBLICATIONDATE)
    private String publicationDate;

    public static BookDTO createByBookPO(BookPO po) {
        BookDTO dto = new BookDTO();
        BeanUtils.copyProperties(po, dto);

        return dto;
    }

    @JsonIgnore
    public BookBO getBookBO() {
    	BookBO bo = new BookBO();
        BeanUtils.copyProperties(this, bo);

        return bo;
    }

    @JsonIgnore
    public BookPO getBookPO() {
    	BookPO po = new BookPO();
        BeanUtils.copyProperties(this, po);

        return po;
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

	@Override
	public String toString() {
		return "BookDTO [name=" + name + ", author=" + author + ", publicationDate=" + publicationDate + "]";
	}

}
