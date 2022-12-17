package services;

import com.rbinternational.repository.models.entity.ChildEntity;
import com.rbinternational.repository.models.entity.ParentEntity;
import com.rbinternational.repository.models.entity.ReaderEntity;
import com.rbinternational.repository.models.repository.*;
import com.rbinternational.service.ReaderService;
import com.rbinternational.utils.ValuesService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.mock.mockito.MockitoTestExecutionListener;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;

import java.time.Instant;
import java.time.LocalDate;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@TestExecutionListeners(MockitoTestExecutionListener.class)
@MockitoSettings(strictness = Strictness.LENIENT)
@ContextConfiguration
@AutoConfigureMockMvc
public class ReaderServiceTest {

    @Mock
    private ReaderRepository readerRepository;
    @Mock
    private ParentRepository parentRepository;
    @Mock
    private ChildRepository childRepository;
    @Mock
    private ValuesService valuesService;
    @Mock
    private LoansRepository loansRepository;
    @Mock
    private BookRepository bookRepository;
    @InjectMocks
    private ReaderService readerService;

    private List<ReaderEntity> readers = new LinkedList<>();
    private List<ReaderEntity> parents = new LinkedList<>();
    private List<ReaderEntity> childs = new LinkedList<>();

    @Test
    @DisplayName("Should return list of books id which reader have too long")
    void shouldReturnListOfBooksIdWhichReaderHaveTooLong() {
        ReaderEntity reader = readers.get(0);
        int maxNumberOfDaysToBorrowABook = 2;

        int bookId = 1;
        Date date = Date.from(Instant.now().minusSeconds(86400 * (maxNumberOfDaysToBorrowABook + 1)));

        Object[] loan = new Object[]{bookId, date};

        List<Object[]> foundedBooks = new LinkedList<>();
        foundedBooks.add(loan);

        when(loansRepository.booksIdWhichReaderHaveTooMuchTime(any(ReaderEntity.class), any(Integer.class))).thenReturn(foundedBooks);

        List<Integer> result = readerService.booksIdWhichReaderHaveTooMuchTime(reader, maxNumberOfDaysToBorrowABook);

        Assertions.assertTrue(result.size() == 1 && result.get(0) == bookId);
    }

    @Test
    @DisplayName("Should return empty list because reader haven't got any book to long")
    void shouldReturnEmptyListBecauseReaderHaventGotAnyBookToLong() {
        ReaderEntity reader = readers.get(0);
        int maxNumberOfDaysToBorrowABook = 2;

        int bookId = 1;
        Date date = Date.from(Instant.now().minusSeconds(86400 * (maxNumberOfDaysToBorrowABook - 1)));

        Object[] loan = new Object[]{bookId, date};

        List<Object[]> foundedBooks = new LinkedList<>();
        foundedBooks.add(loan);

        when(loansRepository.booksIdWhichReaderHaveTooMuchTime(any(ReaderEntity.class), any(Integer.class))).thenReturn(foundedBooks);

        List<Integer> result = readerService.booksIdWhichReaderHaveTooMuchTime(reader, maxNumberOfDaysToBorrowABook);

        Assertions.assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("Should return readers with given parameters")
    void shouldReturnReadersWithGivenParameters() {
        List<ReaderEntity> readerEntities = readers;
        ReaderEntity reader = readerEntities.get(0);
        String idToFind = String.valueOf(reader.getId());

        when(valuesService.checkIdValue(any(String.class))).thenReturn(true);
        when(valuesService.checkStringValueToFind(any(String.class))).thenReturn(false);
        when(valuesService.checkBirthDateValue(any(LocalDate.class))).thenReturn(false);

        List<ReaderEntity> result = readerService.findReaderByParameters(idToFind, null, null, null, readerEntities);

        Assertions.assertEquals(List.of(reader), result);
    }

    @Test
    @DisplayName("Should return empty list because couldn't find reader with given parameters")
    void shouldReturnEmptyListBecauseCouldntFindReaderWithGivenParameters() {
        List<ReaderEntity> readerEntities = readers;
        String idToFind = "90";

        when(valuesService.checkIdValue(any(String.class))).thenReturn(true);
        when(valuesService.checkStringValueToFind(any(String.class))).thenReturn(false);
        when(valuesService.checkBirthDateValue(any(LocalDate.class))).thenReturn(false);

        List<ReaderEntity> result = readerService.findReaderByParameters(idToFind, null, null, null, readerEntities);

        Assertions.assertEquals(List.of(), result);
    }

    @Test
    @DisplayName("Should return all readers because not given filter parameters")
    void shouldReturnAllReadersBecauseNotGivenFilterParameters() {
        List<ReaderEntity> readerEntities = readers;


        when(valuesService.checkIdValue(any(String.class))).thenReturn(false);
        when(valuesService.checkStringValueToFind(any(String.class))).thenReturn(false);
        when(valuesService.checkBirthDateValue(any(LocalDate.class))).thenReturn(false);

        List<ReaderEntity> result = readerService.findReaderByParameters(null, null, null, null, readerEntities);

        Assertions.assertEquals(readerEntities, result);
    }

    @Test
    @DisplayName("Should return parent reader with given parameters")
    void shouldReturnParentReaderWithGivenParameters() {
        List<ReaderEntity> parentEntities = parents;
        ParentEntity parent = (ParentEntity) parentEntities.get(0);
        String phoneToFind = parent.getPhoneNumber();


        when(valuesService.checkStringValueToFind(phoneToFind)).thenReturn(true);
        when(valuesService.checkStringValueToFind(null)).thenReturn(false);

        List<ReaderEntity> result = readerService.findParentByParameters(null, phoneToFind, parentEntities);

        Assertions.assertEquals(List.of(parent), result);
    }

    @Test
    @DisplayName("Should return empty list because couldn't find any parent with given parameters")
    void shouldReturnEmptyListBecauseCouldntFindAnyParentWithGivenParameters() {
        List<ReaderEntity> parentEntities = parents;
        String phoneToFind = "phone3333333";


        when(valuesService.checkStringValueToFind(phoneToFind)).thenReturn(true);
        when(valuesService.checkStringValueToFind(null)).thenReturn(false);

        List<ReaderEntity> result = readerService.findParentByParameters(null, phoneToFind, parentEntities);

        Assertions.assertEquals(List.of(), result);
    }

    @Test
    @DisplayName("Should return all parents because not given any filter parameters")
    void shouldReturnAllParentsBecauseNotGivenAnyFilterParameters() {
        List<ReaderEntity> parentEntities = parents;

        when(valuesService.checkStringValueToFind(any())).thenReturn(false);

        List<ReaderEntity> result = readerService.findParentByParameters(null, null, parentEntities);

        Assertions.assertEquals(parentEntities, result);
    }

    @Test
    @DisplayName("Should return child with given parent id")
    void shouldReturnChildWithGivenParentId() {
        List<ReaderEntity> childEntities = childs;
        ChildEntity child = (ChildEntity) childEntities.get(0);
        String parentIdValue = String.valueOf(child.getParent().getId());

        when(valuesService.checkIdValue(any(String.class))).thenReturn(true);

        List<ReaderEntity> result = readerService.findChildByParameters(parentIdValue, childEntities);

        Assertions.assertEquals(List.of(child), result);
    }

    @Test
    @DisplayName("Should return empty list because couldn't find any child with given parameters")
    void shouldReturnEmptyListBecauseCouldntFindAnyChildWithGivenParameters() {
        List<ReaderEntity> childEntities = childs;
        String parentIdValue = String.valueOf(99);

        when(valuesService.checkIdValue(any(String.class))).thenReturn(true);

        List<ReaderEntity> result = readerService.findChildByParameters(parentIdValue, childEntities);

        Assertions.assertEquals(List.of(), result);
    }

    @Test
    @DisplayName("Should return all child because not given any filter parameters")
    void shouldReturnAllChildBecauseNotGivenAnyFilterParameters() {
        List<ReaderEntity> childEntities = childs;
        ChildEntity child = (ChildEntity) childEntities.get(0);
        String parentIdValue = String.valueOf(child.getParent().getId());

        when(valuesService.checkIdValue(any(String.class))).thenReturn(false);

        List<ReaderEntity> result = readerService.findChildByParameters(parentIdValue, childEntities);

        Assertions.assertEquals(childEntities, result);
    }

    @Test
    @DisplayName("Should return false because couldn't find reader with given id to delete")
    void shouldReturnFalseBecauseCouldntFindReaderWithGivenIdToDelete() {
        int idToDelte = 9;

        when(readerRepository.findById(any(Integer.class))).thenReturn(Optional.empty());

        boolean result = readerService.deleteReader(idToDelte);

        Assertions.assertFalse(result);
    }

    @Test
    @DisplayName("Should delete reader with given id")
    void shouldDeleteReaderWithGivenId() {
        ReaderEntity reader = readers.get(0);
        int idToDelte = reader.getId();

        when(readerRepository.findById(any(Integer.class))).thenReturn(Optional.of(reader));

        boolean result = readerService.deleteReader(idToDelte);

        Assertions.assertTrue(result);
    }

    @Test
    @DisplayName("Should add parent reader with given parameters")
    void shouldAddParentReaderWithGivenParameters() {
        ParentEntity parent = (ParentEntity) parents.get(0);
        parent.setId(null);
        ParentEntity result = readerService.addParentReader(parent.getName(),
                parent.getSurname(), parent.getAddress(), parent.getPhoneNumber(), parent.getBirthDate());

        Assertions.assertEquals(parent, result);
    }

    @Test
    @DisplayName("Should add child reader")
    void shouldAddChildReader() {
        ChildEntity child = (ChildEntity) childs.get(0);
        child.setId(null);
        ParentEntity parent = (ParentEntity) parents.get(0);

        when(parentRepository.findById(any(Integer.class))).thenReturn(Optional.of(parent));

        ChildEntity result = readerService.addChildReader(child.getName(), child.getSurname()
                , child.getParent().getId(), child.getBirthDate());

        Assertions.assertEquals(child, result);
    }

    @Test
    @DisplayName("Should return null because couldn't find parent with given id")
    void shouldReturnNullBecauseCouldntFindParentWithGivenId() {
        ChildEntity child = (ChildEntity) childs.get(0);
        child.setId(null);

        when(parentRepository.findById(any(Integer.class))).thenReturn(Optional.empty());

        ChildEntity result = readerService.addChildReader(child.getName(), child.getSurname()
                , child.getParent().getId(), child.getBirthDate());

        Assertions.assertNull(result);
    }

    @Test
    @DisplayName("Should edit given parent")
    void shouldEditGivenParent() {
        ReaderEntity parent = parents.get(0);
        String name = "name example";

        ParentEntity result = readerService.editReaderToParent(parent, name, null, null, null, null);

        parent.setName(name);

        Assertions.assertEquals(parent, result);
    }

    @Test
    @DisplayName("Should return null because not all values are present to change child to parent")
    void shouldReturnNullBecauseNotAllValuesArePresentToChangeChildToParent() {
        ReaderEntity child = childs.get(0);
        String name = "name example";

        ParentEntity result = readerService.editReaderToParent(child, name, null, null, null, null);

        child.setName(name);

        Assertions.assertNull(result);
    }

    @Test
    @DisplayName("Should return parent with given child values")
    void shouldReturnParentWithGivenChildValues() {
        ReaderEntity child = childs.get(0);
        String address = "address";
        String phoneNumber = "phoneNum";

        ReaderEntity result = readerService.editReaderToParent(child, null, null, address, phoneNumber, null);


        Assertions.assertEquals(child.getName(), result.getName());
        Assertions.assertEquals(child.getSurname(), result.getSurname());
        Assertions.assertEquals(child.getBirthDate(), result.getBirthDate());
        Assertions.assertEquals(child.getName(), result.getName());
        Assertions.assertEquals(child.getId(), result.getId());
    }

    @Test
    @DisplayName("Should edit child values")
    void shouldEditValuesOfChildReader() {
        ReaderEntity child = childs.get(0);
        String name = "new name";

        when(valuesService.checkIdValue(any(String.class))).thenReturn(false);

        ChildEntity result = readerService.editReaderToChild(child, name, null, null, null);
        child.setName(name);

        Assertions.assertEquals(child, result);
    }

    @Test
    @DisplayName("Shouldn't edit parent id value because is incorrect")
    void shouldntEditParentIdValueBecauseIsIncorrect() {
        ReaderEntity child = childs.get(0);
        String parentId = "parentId";

        when(valuesService.checkIdValue(any(String.class))).thenReturn(false);

        ChildEntity result = readerService.editReaderToChild(child, null, null, parentId, null);

        Assertions.assertNotEquals(parentId, result.getParent().getId());
    }

    @Test
    @DisplayName("Should return null because not all values are present change parent to child")
    void shouldReturnNullBecauseNotAllValuesArePresentToChangeParentToChild() {
        ReaderEntity parent = parents.get(0);

        when(valuesService.checkIdValue(any(String.class))).thenReturn(false);

        ChildEntity result = readerService.editReaderToChild(parent, null, null, null, null);

        Assertions.assertNull(result);
    }

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        readerService = new ReaderService(readerRepository, parentRepository, childRepository, valuesService, loansRepository, bookRepository);
    }

    @Test
    @DisplayName("Should return null while change parent to child because couldn't find parent with given id")
    void shouldReturnNullWhileChangeParentToChildBecauseCouldntFindParentWithGivenId() {
        ReaderEntity parent = parents.get(0);
        String parentId = "123";
        when(valuesService.checkIdValue(any(String.class))).thenReturn(true);
        when(parentRepository.findById(any(Integer.class))).thenReturn(Optional.empty());

        ChildEntity result = readerService.editReaderToChild(parent, null, null, parentId, null);

        Assertions.assertNull(result);
    }

    @Test
    @DisplayName("Should edit parent to child with given parameters")
    void shouldReturnParentToChildWithGivenParameters() {
        ReaderEntity parent = parents.get(0);
        ParentEntity childParent = (ParentEntity) parents.get(1);
        when(valuesService.checkIdValue(any(String.class))).thenReturn(true);
        when(parentRepository.findById(any(Integer.class))).thenReturn(Optional.of(childParent));

        ChildEntity result = readerService.editReaderToChild(parent, null, null, String.valueOf(childParent.getId()), null);

        Assertions.assertEquals(parent.getId(), result.getId());
        Assertions.assertEquals(parent.getName(), result.getName());
        Assertions.assertEquals(parent.getSurname(), result.getSurname());
        Assertions.assertEquals(parent.getBirthDate(), result.getBirthDate());
        Assertions.assertEquals(childParent, result.getParent());
    }

    @Test
    @DisplayName("Should edit reader surname")
    void shouldEditReaderSurname() {
        ReaderEntity reader = readers.get(0);
        String surname = "new surname";

        ReaderEntity result = readerService.editReader(reader, null, surname, null);
        reader.setSurname(surname);

        Assertions.assertEquals(reader, result);
    }

    @Test
    @DisplayName("Should return the same reader because not given parameters")
    void shouldReturnTheSameReaderBecauseNotGivenParameters() {
        ReaderEntity reader = readers.get(0);

        ReaderEntity result = readerService.editReader(reader, null, null, null);

        Assertions.assertEquals(reader, result);
    }

    @BeforeEach
    public void initValues() {
        ReaderEntity reader1 = new ReaderEntity();
        reader1.setId(1);
        reader1.setName("Jan");
        reader1.setSurname("Nowak");
        reader1.setBirthDate(LocalDate.now().minusYears(20));

        ReaderEntity reader2 = new ReaderEntity();
        reader2.setId(2);
        reader2.setName("Adam");
        reader2.setSurname("Kowalczyk");
        reader2.setBirthDate(LocalDate.now().minusYears(5));

        readers.addAll(List.of(reader1, reader2));

        ParentEntity parent1 = new ParentEntity();
        parent1.setId(3);
        parent1.setName("name1");
        parent1.setSurname("surname1");
        parent1.setAddress("address1");
        parent1.setPhoneNumber("phone1");
        parent1.setBirthDate(LocalDate.of(1990, 10, 10));

        ParentEntity parent2 = new ParentEntity();
        parent2.setId(4);
        parent2.setName("name2");
        parent2.setSurname("surname2");
        parent2.setAddress("address2");
        parent2.setPhoneNumber("phone2");
        parent1.setBirthDate(LocalDate.of(1992, 1, 20));

        parents.addAll(List.of(parent1, parent2));

        ChildEntity child1 = new ChildEntity();
        child1.setId(5);
        child1.setName("child1");
        child1.setSurname("childSurname1");
        child1.setParent(parent1);
        child1.setBirthDate(LocalDate.of(2005, 10, 10));

        ChildEntity child2 = new ChildEntity();
        child2.setId(6);
        child2.setName("child2");
        child2.setSurname("childSurname2");
        child2.setParent(parent2);
        child1.setBirthDate(LocalDate.of(2015, 10, 10));


        childs.addAll(List.of(child1, child2));
    }
}
