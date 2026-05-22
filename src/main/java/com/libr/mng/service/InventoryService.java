package com.libr.mng.service;

import java.util.List;

import com.libr.mng.dto.request.IssueBookRequestDTO;
import com.libr.mng.dto.response.BookDetailsResponseDTO;
import com.libr.mng.dto.response.InventoryResponseDTO;
import com.libr.mng.dto.response.IssueBookResponseDTO;

public interface InventoryService {

	List<InventoryResponseDTO> getInventory();

	List<InventoryResponseDTO> searchBooks(String keyword);

	List<InventoryResponseDTO> filterByCategory(String category);

	BookDetailsResponseDTO getBookDetails(Long bookId);

	IssueBookResponseDTO issueBook(IssueBookRequestDTO dto);
}