package com.company.repository.models.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;

//TODO modul repo integration test
// dependency -> repository
// postgres in memory
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
//@Builder
@Entity
@Table(name = "Reader")
@Inheritance(strategy = InheritanceType.JOINED)
public class ReaderEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    private String name;

    private String surname;

    private LocalDate birthDate;

    @Transient
    private int numberOfCurrentlyBorrowedBooks;
    @Transient
    private int numberOfEveryBorrowedBooks;

    @Override
    public String toString() {
        return "*****************Reader***************** " + "\n" +
                "\t" + "id: " + id + "\n" +
                "\t" + "name: " + name + "\n" +
                "\t" + "surname: " + surname + "\n" +
                "\t" + "birthDate: " + birthDate + "\n" +
                "\t" + "numberOfCurrentBorrowedBooks: " + numberOfCurrentlyBorrowedBooks + "\n" +
                "\t" + "numberOfEveryBorrowedBooks: " + numberOfEveryBorrowedBooks + "\n";
    }
}
