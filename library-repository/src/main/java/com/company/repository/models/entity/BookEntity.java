package com.company.repository.models.entity;


import lombok.*;

import javax.persistence.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Book")
public class BookEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    private String title;

    @ManyToOne
    @JoinColumn(name = "author_id")
    private AuthorEntity authorId;

    private int quantity;

    @Transient
    private int quantityAvailable;

    @Override
    public String toString() {
        return "*****************Book*****************" + "\n" +
                "\t" + "id: " + id + "\n" +
                "\t" + "title: " + title + "\n" +
                "\t" + "author: " + " id: " + authorId.getId() + " name: " + authorId.getName() + " surname: " + authorId.getSurname() + "\n" +
                "\t" + "quantity: " + quantity + "\n" +
                "\t" + "quantityAvailable: " + quantityAvailable + "\n";
    }
}
