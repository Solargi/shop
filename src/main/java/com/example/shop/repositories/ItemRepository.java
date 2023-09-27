package com.example.shop.repositories;

import com.example.shop.models.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
//extending with jpa rep gives access to standard crud methods save, save all, findbyid etc
public interface ItemRepository extends JpaRepository<Item, Integer> {
}
