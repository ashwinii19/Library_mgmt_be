package com.libr.mng.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.libr.mng.entity.Wishlist;

public interface WishlistRepository extends JpaRepository<Wishlist, Long> {

	boolean existsByUserIdAndBookBookId(Long userId, Long bookId);

	Optional<Wishlist> findByUserIdAndBookBookId(Long userId, Long bookId);

	List<Wishlist> findByUserId(Long userId);
}
