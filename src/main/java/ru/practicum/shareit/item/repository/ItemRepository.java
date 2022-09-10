package ru.practicum.shareit.item.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.item.model.Item;

import java.util.List;


public interface ItemRepository extends JpaRepository<Item,Long> {

    List<Item> findByOwnerIdOrderById(Long id);

    @Query("select item from Item item" +
            " where item.available = true " +
            " and (lower(item.name) like LOWER(concat('%', ?1, '%'))" +
            " or lower(item.description) like LOWER(concat('%', ?1, '%')))")
    List<Item> search(String text);
}