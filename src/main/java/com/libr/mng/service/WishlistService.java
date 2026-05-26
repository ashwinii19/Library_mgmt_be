package com.libr.mng.service;

import java.util.List;

import com.libr.mng.dto.response.WishlistResponseDTO;

public interface WishlistService {

    String addToWishlist(Long bookId);

    String removeFromWishlist(Long bookId);

    List<WishlistResponseDTO> getWishlist();
    
    String notifyWhenAvailable(Long bookId);
}