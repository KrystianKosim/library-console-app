package com.company.repository.models.repository;

import com.company.repository.models.entity.AuthorEntity;
import com.company.repository.models.entity.BookEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<BookEntity, Integer> {
    Optional<List<BookEntity>> findBookEntitiesByQuantity(int quantity);

    Optional<List<BookEntity>> findBookEntitiesByTitle(String title);

    Optional<List<BookEntity>> findBookEntitiesByAuthorId(AuthorEntity authorId);

    Optional<List<BookEntity>> findBookEntitiesById(int Id);

    Optional<List<BookEntity>> findBookEntitiesByAuthorIdAndQuantity(AuthorEntity authorId, int Quantity);

    Optional<List<BookEntity>> findBookEntitiesByTitleAndQuantity(String title, int quantity);

    Optional<List<BookEntity>> findBookEntitiesByTitleAndAuthorId(String title, AuthorEntity authorId);

    Optional<List<BookEntity>> findBookEntitiesByIdAndQuantity(int id, int quantity);

    Optional<List<BookEntity>> findBookEntitiesByIdAndAuthorId(int id, AuthorEntity authorId);

    Optional<List<BookEntity>> findBookEntitiesByIdAndTitle(int id, String title);

    Optional<List<BookEntity>> findBookEntitiesByIdAndAuthorIdAndQuantity(int id, AuthorEntity authorId, int quantity);

    Optional<List<BookEntity>> findBookEntitiesByIdAndTitleAndQuantity(int id, String title, int quantity);

    Optional<List<BookEntity>> findBookEntitiesByIdAndTitleAndAuthorId(int id, String title, AuthorEntity authorId);

    Optional<List<BookEntity>> findBookEntitiesByIdAndTitleAndAuthorIdAndQuantity(int id, String title, AuthorEntity authorId, int quantity);

}
