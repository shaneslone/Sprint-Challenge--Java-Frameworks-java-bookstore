package com.lambdaschool.bookstore.services;

import com.lambdaschool.bookstore.BookstoreApplication;
import com.lambdaschool.bookstore.exceptions.ResourceNotFoundException;
import com.lambdaschool.bookstore.models.*;
import com.lambdaschool.bookstore.repository.AuthorRepository;
import com.lambdaschool.bookstore.repository.BookRepository;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = BookstoreApplication.class)
//**********
// Note security is handled at the controller, hence we do not need to worry about security here!
//**********
public class BookServiceImplTest
{
    @Autowired
    private BookService bookService;

    @MockBean
    private BookRepository bookrepos;

    @MockBean
    private SectionService sectionService;

    @MockBean
    private AuthorRepository authorrepos;

    private List<Author>authorList = new ArrayList<>();
    private List<Book> bookList = new ArrayList<>();
    private List<Section> sectionList = new ArrayList<>();

    @Before
    public void setUp() throws
            Exception
    {
        Author a1 = new Author("John", "Mitchell");
        a1.setAuthorid(1);
        Author a2 = new Author("Dan", "Brown");
        a2.setAuthorid(2);
        Author a3 = new Author("Jerry", "Poe");
        a3.setAuthorid(3);
        Author a4 = new Author("Wells", "Teague");
        a4.setAuthorid(4);
        Author a5 = new Author("George", "Gallinger");
        a5.setAuthorid(5);
        Author a6 = new Author("Ian", "Stewart");
        a6.setAuthorid(6);

        authorList.add(a1);
        authorList.add(a2);
        authorList.add(a3);
        authorList.add(a4);
        authorList.add(a5);
        authorList.add(a6);

        Section s1 = new Section("Fiction");
        s1.setSectionid(1);
        Section s2 = new Section("Technology");
        s2.setSectionid(2);
        Section s3 = new Section("Travel");
        s3.setSectionid(3);
        Section s4 = new Section("Business");
        s4.setSectionid(4);
        Section s5 = new Section("Religion");
        s5.setSectionid(5);

        sectionList.add(s1);
        sectionList.add(s2);
        sectionList.add(s3);
        sectionList.add(s4);
        sectionList.add(s5);


        Book b1 = new Book("Flatterland", "9780738206752", 2001, s1);
        b1.setBookid(1);
        b1.getWrotes()
                .add(new Wrote(a6, new Book()));
        bookList.add(b1);

        Book b2 = new Book("Digital Fortess", "9788489367012", 2007, s1);
        b2.setBookid(2);
        b2.getWrotes()
                .add(new Wrote(a2, new Book()));
        bookList.add(b2);

        Book b3 = new Book("The Da Vinci Code", "9780307474278", 2009, s1);
        b3.setBookid(3);
        b3.getWrotes()
                .add(new Wrote(a2, new Book()));
        bookList.add(b3);

        Book b4 = new Book("Essentials of Finance", "1314241651234", 0, s4);
        b4.setBookid(4);
        b4.getWrotes()
                .add(new Wrote(a3, new Book()));
        b4.getWrotes()
                .add(new Wrote(a5, new Book()));
        bookList.add(b4);

        Book b5 = new Book("Calling Texas Home", "1885171382134", 2000, s3);
        b5.setBookid(5);
        b5.getWrotes()
                .add(new Wrote(a4, new Book()));
        bookList.add(b5);

        MockitoAnnotations.initMocks(this);
    }

    @After
    public void tearDown() throws
            Exception
    {
    }

    @Test
    public void findAll()
    {
        Mockito.when(bookrepos.findAll())
                .thenReturn(bookList);
        assertEquals(5, bookService.findAll().size());
    }

    @Test
    public void findBookById()
    {
        Mockito.when(bookrepos.findById(1L))
                .thenReturn(Optional.of(bookList.get(0)));
        assertEquals("Flatterland", bookService.findBookById(1L).getTitle());
    }

    @Test(expected = ResourceNotFoundException.class)
    public void notFindBookById()
    {
        Mockito.when(bookrepos.findById(10L))
                .thenReturn(Optional.of(bookList.get(0)));
        assertEquals("Flatterland", bookService.findBookById(1L).getTitle());
    }

    @Test
    public void delete()
    {
        Mockito.when(bookrepos.findById(1L))
                .thenReturn(Optional.of(bookList.get(0)));
        Mockito.doNothing()
                .when(bookrepos)
                .deleteById(1L);
        bookService.delete(1L);
        assertEquals(5, bookList.size());
    }

    @Test
    public void save()
    {
        Author a1 = new Author("Ian", "Stewart");
        a1.setAuthorid(1);

        Section s1 = new Section("Fiction");
        s1.setSectionid(1);

        Book b1 = new Book("Flatterland", "9780738206752", 2001, s1);
        b1.setBookid(0);
        b1.getWrotes()
                .add(new Wrote(a1, new Book()));

        Mockito.when(bookrepos.save(any(Book.class)))
                .thenReturn(b1);
        Mockito.when(sectionService.findSectionById(1L))
                .thenReturn(s1);
        Mockito.when(authorrepos.findById(1L))
                .thenReturn(Optional.of(a1));

        Book addBook = bookService.save(b1);
        assertNotNull(addBook);
        assertEquals(b1.getTitle(), addBook.getTitle());
    }

    @Test
    public void update()
    {
    }

    @Test
    public void deleteAll()
    {
    }
}