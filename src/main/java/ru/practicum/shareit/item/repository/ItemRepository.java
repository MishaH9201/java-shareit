package ru.practicum.shareit.item.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.item.model.Item;

import java.util.List;


public interface ItemRepository extends JpaRepository<Item,Long> {

    Page<Item> findByOwnerIdOrderById(Long id, PageRequest pageRequest);

    @Query("select item from Item item" +
            " where item.available = true " +
            " and (lower(item.name) like LOWER(concat('%', ?1, '%'))" +
            " or lower(item.description) like LOWER(concat('%', ?1, '%')))")
    Page<Item> search(String text,PageRequest pageRequest);

    List<Item> findByRequestId(Long id);
}