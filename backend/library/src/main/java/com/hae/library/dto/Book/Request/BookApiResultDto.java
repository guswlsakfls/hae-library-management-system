package com.hae.library.dto.Book.Request;

import lombok.Getter;
import lombok.Setter;

// TODO: 필요없는 변수들 제거
// 국립중앙 도서관 API에서 제공하는 도서 정보를 담는 DTO 입니다.
//@Setter
@Getter
public class BookApiResultDto {
    private String titleInfo;
    private String typeName;
    private String placeInfo;
    private String authorInfo;
    private String pubInfo;
    private String menuName;
    private String mediaName;
    private String manageName;
    private String pubYearInfo;
    private String controlNo;
    private String docYn;
    private String orgLink;
    private String id;
    private String typeCode;
    private String licYn;
    private String licText;
    private String regDate;
    private String detailLink;
    private String isbn;
    private String callNo;
    private String kdcCode1s;
    private String kdcName1s;
    private String classNo;
    private String imageUrl;
    private String typeOfRes;
}