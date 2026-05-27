package com.libr.mng.service;

import java.util.List;

import com.libr.mng.dto.response.IssuedBookResponseDTO;

public interface IssuedBookService {

	List<IssuedBookResponseDTO> getMyIssuedBooks();
}