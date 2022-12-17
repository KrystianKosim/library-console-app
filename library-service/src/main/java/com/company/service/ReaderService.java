package com.company.service;

import com.company.repository.models.entity.ChildEntity;
import com.company.repository.models.entity.ParentEntity;
import com.company.repository.models.entity.ReaderEntity;
import com.company.repository.models.repository.*;
import com.company.utils.ValuesService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@RequiredArgsConstructor
@Service
public class ReaderService {

    private final ReaderRepository readerRepository;
    private final ParentRepository parentRepository;
    private final ChildRepository childRepository;
    private final ValuesService valuesService;
    private final LoansRepository loansRepository;
    private final BookRepository bookRepository;
    private List<ReaderEntity> allReaders = new LinkedList<>();


    /**
     * Method to get id of books which reader have too much time
     *
     * @param reader
     * @param maxNumberOfDaysToBorrowABook
     * @return
     */
    public List<Integer> booksIdWhichReaderHaveTooMuchTime(ReaderEntity reader, int maxNumberOfDaysToBorrowABook) {
        List<Object[]> result = loansRepository.booksIdWhichReaderHaveTooMuchTime(reader, maxNumberOfDaysToBorrowABook);
        return checkIsReaderHaveBookTooMuchTime(result, maxNumberOfDaysToBorrowABook);
    }

    /**
     * Method to find single reader by id
     *
     * @param readerId
     * @return
     */
    public Optional<ReaderEntity> findReaderById(int readerId) {
        return readerRepository.findById(readerId);
    }

    /**
     * Method to find reader by parameters
     *
     * @param id
     * @param name
     * @param surname
     * @param birthDate
     * @param readers
     * @return
     */
    public List<ReaderEntity> findReaderByParameters(String id, String name, String surname, LocalDate birthDate, List<ReaderEntity> readers) {
        boolean idValue = valuesService.checkIdValue(id);
        boolean nameValue = valuesService.checkStringValueToFind(name);
        boolean surnameValue = valuesService.checkStringValueToFind(surname);
        boolean birthDateValue = valuesService.checkBirthDateValue(birthDate);
        return readers.stream()
                .filter(reader -> !idValue || reader.getId() == Integer.parseInt(id))
                .filter(reader -> !nameValue || reader.getName().contains(name))
                .filter(reader -> !surnameValue || reader.getSurname().contains(surname))
                .filter(reader -> !birthDateValue || reader.getBirthDate().equals(birthDate))
                .collect(Collectors.toList());
    }

    /**
     * Method to find parent reader by parameters
     *
     * @param address
     * @param phoneNumber
     * @param readerResult
     * @return
     */

    public List<ReaderEntity> findParentByParameters(String address, String phoneNumber, List<ReaderEntity> readerResult) {
        boolean addressValue = valuesService.checkStringValueToFind(address);
        boolean phoneNumberValue = valuesService.checkStringValueToFind(phoneNumber);
        readerResult = readerResult.stream()
                .filter(reader -> reader instanceof ParentEntity)
                .filter(reader -> !addressValue || ((ParentEntity) reader).getAddress().contains(address))
                .filter(reader -> !phoneNumberValue || ((ParentEntity) reader).getPhoneNumber().contains(phoneNumber))
                .collect(Collectors.toList());
        return readerResult;
    }

    /**
     * Method to find child reader by parameters
     *
     * @param parentId
     * @param readerResult
     * @return
     */

    public List<ReaderEntity> findChildByParameters(String parentId, List<ReaderEntity> readerResult) {
        boolean parentIdValue = valuesService.checkIdValue(parentId);
        readerResult = readerResult.stream()
                .filter(reader -> reader instanceof ChildEntity)
                .filter(reader -> !parentIdValue || ((ChildEntity) reader).getParent().getId().equals(Integer.parseInt(parentId)))
                .collect(Collectors.toList());
        return readerResult;
    }

    /**
     * Method to get all readers
     *
     * @return
     */

    public List<ReaderEntity> findAllReaders() {
        if (allReaders.isEmpty()) {
            allReaders = readerRepository.findAll();
        }
        return allReaders;
    }

    /**
     * Method to delete single reader
     *
     * @param id
     */

    public boolean deleteReader(int id) {
        Optional<ReaderEntity> optionalReader = readerRepository.findById(id);
        if (optionalReader.isPresent()) {
            ReaderEntity readerEntity = optionalReader.get();
            if (readerEntity instanceof ChildEntity) {
                childRepository.deleteById(id);
            }
            if (readerEntity instanceof ParentEntity) {
                deleteParentReader(id);
            }
            readerRepository.deleteById(id);
            return true;
        }
        return false;
    }

    /**
     * Method to add parent reader with parameters
     *
     * @param name
     * @param surname
     * @param address
     * @param phoneNumber
     * @param birthDate
     */
    public ParentEntity addParentReader(String name, String surname, String address, String phoneNumber, LocalDate birthDate) {
        ParentEntity parent = new ParentEntity();
        parent.setName(name);
        parent.setSurname(surname);
        parent.setAddress(address);
        parent.setPhoneNumber(phoneNumber);
        parent.setBirthDate(birthDate);
        parentRepository.save(parent);
        return parent;
    }

    /**
     * Method to add child reader and check if id of parent is correct
     *
     * @param name
     * @param surname
     * @param parentId
     * @param birthDate
     */
    public ChildEntity addChildReader(String name, String surname, int parentId, LocalDate birthDate) {
        Optional<ParentEntity> parentOptional = parentRepository.findById(parentId);
        if (parentOptional.isPresent()) {
            ParentEntity parent = parentOptional.get();
            return addChildToRepository(name, surname, parent, birthDate);
        }
        return null;
    }

    /**
     * Change reader type from child to parent or if it is parent, change him values
     *
     * @param reader
     * @param name
     * @param surname
     * @param address
     * @param phoneNumber
     * @param birthDate
     */
    public ParentEntity editReaderToParent(ReaderEntity reader, String name, String surname, String address, String phoneNumber, LocalDate birthDate) {
        if (reader instanceof ParentEntity) {
            return editParentValues(reader, name, surname, address, phoneNumber, birthDate);
        } else {
            return editChildValuesToParent(reader, name, surname, address, phoneNumber, birthDate);
        }
    }

    /**
     * Change reader type from parent to child or if it is child, change him values
     *
     * @param reader
     * @param name
     * @param surname
     * @param parentId
     * @param birthDate
     */
    public ChildEntity editReaderToChild(ReaderEntity reader, String name, String surname, String parentId, LocalDate birthDate) {
        boolean parentIdValue = valuesService.checkIdValue(parentId);
        if (reader instanceof ChildEntity) {
            return editChildValues(reader, name, surname, parentId, birthDate, parentIdValue);
        } else {
            return editParentValuesToChild(reader, name, surname, parentId, birthDate);
        }
    }

    /**
     * Method to edit reader without type
     *
     * @param reader
     * @param name
     * @param surname
     * @param birthDate
     */
    public ReaderEntity editReader(ReaderEntity reader, String name, String surname, LocalDate birthDate) {
        Optional.ofNullable(name).ifPresent(reader::setName);
        Optional.ofNullable(surname).ifPresent(reader::setSurname);
        Optional.ofNullable(birthDate).ifPresent(reader::setBirthDate);
        readerRepository.save(reader);
        return reader;
    }

    private ChildEntity editParentValuesToChild(ReaderEntity reader, String name, String surname, String parentId, LocalDate birthDate) {
        ChildEntity child = new ChildEntity();
        boolean checkIfAllChildValuesArePresent = checkIfAllChildValuesArePresent(parentId);
        if (!checkIfAllChildValuesArePresent) {
            return null;
        }
        Optional.ofNullable(name).ifPresentOrElse(child::setName, () -> {
            child.setName(reader.getName());
        });
        Optional.ofNullable(surname).ifPresentOrElse(child::setSurname, () -> {
            child.setSurname(reader.getSurname());
        });
        Optional.ofNullable(birthDate).ifPresentOrElse(child::setBirthDate, () -> {
            child.setBirthDate(reader.getBirthDate());
        });
        int parentIdInt = Integer.parseInt(parentId);
        Optional<ParentEntity> parentEntity = parentRepository.findById(parentIdInt);
        if (!parentEntity.isPresent()) {
            return null;
        }
        child.setParent(parentEntity.get());
        child.setId(reader.getId());
        childRepository.save(child);
        return child;
    }

    private boolean checkIfAllChildValuesArePresent(String parentIdValue) {
        boolean parentValue = valuesService.checkIdValue(parentIdValue);
        boolean parentValueOptional = Optional.ofNullable(parentIdValue).isPresent();
        return parentValue && parentValueOptional;
    }

    private ChildEntity editChildValues(ReaderEntity reader, String name, String surname, String parentId, LocalDate birthDate, boolean parentIdValue) {
        ChildEntity child = (ChildEntity) reader;
        Optional.ofNullable(name).ifPresent(child::setName);
        Optional.ofNullable(surname).ifPresent(child::setSurname);
        if (parentIdValue) {
            int parentIdInt = Integer.parseInt(parentId);
            Optional<ParentEntity> parent = parentRepository.findById(parentIdInt);
            parent.ifPresent(child::setParent);
        }
        Optional.ofNullable(birthDate).ifPresent(child::setBirthDate);
        childRepository.save(child);
        return child;
    }

    private ParentEntity editChildValuesToParent(ReaderEntity reader, String name, String surname, String address, String phoneNumber, LocalDate birthDate) {
        ParentEntity parent = new ParentEntity();
        boolean checkIfAllParentValuesArePresent = checkIfAllValuesArePresentToCreateParentFromChild(address, phoneNumber);
        if (!checkIfAllParentValuesArePresent) {
            return null;
        }
        Optional.ofNullable(name).ifPresentOrElse(parent::setName, () -> {
            parent.setName(reader.getName());
        });
        Optional.ofNullable(surname).ifPresentOrElse(parent::setSurname, () -> {
            parent.setSurname(reader.getSurname());
        });
        Optional.ofNullable(birthDate).ifPresentOrElse(parent::setBirthDate, () -> {
            parent.setBirthDate(reader.getBirthDate());
        });
        Optional.ofNullable(address).ifPresent(parent::setAddress);
        Optional.ofNullable(phoneNumber).ifPresent(parent::setPhoneNumber);
        parent.setId(reader.getId());
        parentRepository.save(parent);
        return parent;
    }

    private ParentEntity editParentValues(ReaderEntity reader, String name, String surname, String address, String phoneNumber, LocalDate birthDate) {
        ParentEntity parent = (ParentEntity) reader;
        Optional.ofNullable(name).ifPresent(parent::setName);
        Optional.ofNullable(surname).ifPresent(parent::setSurname);
        Optional.ofNullable(address).ifPresent(parent::setAddress);
        Optional.ofNullable(phoneNumber).ifPresent(parent::setPhoneNumber);
        Optional.ofNullable(birthDate).ifPresent(parent::setBirthDate);
        parentRepository.save(parent);
        return parent;
    }

    private boolean checkIfAllValuesArePresentToCreateParentFromChild(String address, String phoneNumber) {
        boolean isAddressPresent = Optional.ofNullable(address).isPresent();
        boolean isPhoneNumberPresent = Optional.ofNullable(phoneNumber).isPresent();
        return isAddressPresent && isPhoneNumberPresent;
    }

    private ChildEntity addChildToRepository(String name, String surname, ParentEntity parent, LocalDate birthDate) {
        ChildEntity child = new ChildEntity();
        child.setName(name);
        child.setSurname(surname);
        child.setParent(parent);
        child.setBirthDate(birthDate);
        childRepository.save(child);
        return child;
    }

    private List<Integer> checkIsReaderHaveBookTooMuchTime(List<Object[]> allResults, int maxNumberOfDaysToBorrowABook) {
        LocalDate currentDate = LocalDate.now();
        List<Integer> listOfBorrowedBooks = new LinkedList<>();
        for (Object[] loan : allResults) {
            int bookId = (int) loan[0];
            Date date = (Date) loan[1];
            LocalDate borrowDate = new java.sql.Date(date.getTime()).toLocalDate();
            LocalDate deadLine = borrowDate.plusDays(maxNumberOfDaysToBorrowABook);
            if (!currentDate.isBefore(deadLine)) {
                listOfBorrowedBooks.add(bookId);
            }
        }
        return listOfBorrowedBooks;
    }

    private void deleteParentReader(int id) {
        List<ChildEntity> child = childRepository.findAll();
        child.stream()
                .filter(childEntity -> childEntity.getParent().getId() == id)
                .forEach(childEntity -> childEntity.setParent(null));
        parentRepository.deleteById(id);
    }


}
