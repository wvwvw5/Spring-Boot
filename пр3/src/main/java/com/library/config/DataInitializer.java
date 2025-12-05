package com.library.config;

import com.library.model.*;
import com.library.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;

/**
 * Инициализация тестовых данных при запуске приложения
 */
@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private AuthorRepository authorRepository;
    
    @Autowired
    private PublisherRepository publisherRepository;
    
    @Autowired
    private GenreRepository genreRepository;
    
    @Autowired
    private BookRepository bookRepository;
    
    @Autowired
    private BookLoanRepository bookLoanRepository;

    @Override
    public void run(String... args) {
        // Проверяем, есть ли уже данные
        if (authorRepository.count() > 0) {
            return;
        }

        // Создаём авторов
        Author pushkin = new Author("Александр", "Пушкин");
        pushkin.setBirthDate(LocalDate.of(1799, 6, 6));
        pushkin.setCountry("Россия");
        pushkin.setBiography("Великий русский поэт, драматург и прозаик");
        authorRepository.save(pushkin);

        Author tolstoy = new Author("Лев", "Толстой");
        tolstoy.setBirthDate(LocalDate.of(1828, 9, 9));
        tolstoy.setCountry("Россия");
        tolstoy.setBiography("Один из наиболее известных русских писателей и мыслителей");
        authorRepository.save(tolstoy);

        Author dostoevsky = new Author("Фёдор", "Достоевский");
        dostoevsky.setBirthDate(LocalDate.of(1821, 11, 11));
        dostoevsky.setCountry("Россия");
        dostoevsky.setBiography("Классик мировой литературы, философ и публицист");
        authorRepository.save(dostoevsky);

        Author chekhov = new Author("Антон", "Чехов");
        chekhov.setBirthDate(LocalDate.of(1860, 1, 29));
        chekhov.setCountry("Россия");
        chekhov.setBiography("Русский писатель, драматург, врач");
        authorRepository.save(chekhov);

        // Создаём издательства
        Publisher eksmo = new Publisher("Эксмо", "Москва");
        eksmo.setAddress("ул. Зорге, 1");
        eksmo.setCountry("Россия");
        eksmo.setEmail("info@eksmo.ru");
        eksmo.setPhone("+7 (495) 411-68-86");
        publisherRepository.save(eksmo);

        Publisher ast = new Publisher("АСТ", "Москва");
        ast.setAddress("Пресненская набережная, 6");
        ast.setCountry("Россия");
        ast.setEmail("info@ast.ru");
        publisherRepository.save(ast);

        Publisher piter = new Publisher("Питер", "Санкт-Петербург");
        piter.setCity("Санкт-Петербург");
        piter.setCountry("Россия");
        piter.setEmail("info@piter.com");
        publisherRepository.save(piter);

        // Создаём жанры
        Genre classics = new Genre("Классика", "Произведения, признанные образцовыми");
        genreRepository.save(classics);

        Genre novel = new Genre("Роман", "Прозаическое произведение большого объёма");
        genreRepository.save(novel);

        Genre poetry = new Genre("Поэзия", "Художественное творчество в стихотворной форме");
        genreRepository.save(poetry);

        Genre drama = new Genre("Драма", "Литературное произведение для театральной постановки");
        genreRepository.save(drama);

        Genre philosophy = new Genre("Философия", "Произведения с глубоким философским содержанием");
        genreRepository.save(philosophy);

        // Создаём книги
        Book evgeniyOnegin = new Book("Евгений Онегин", "978-5-04-098765-4");
        evgeniyOnegin.setAuthor(pushkin);
        evgeniyOnegin.setPublisher(eksmo);
        evgeniyOnegin.setPublicationYear(2020);
        evgeniyOnegin.setPages(320);
        evgeniyOnegin.setDescription("Роман в стихах Александра Сергеевича Пушкина");
        evgeniyOnegin.setPrice(new BigDecimal("450.00"));
        evgeniyOnegin.setTotalCopies(5);
        evgeniyOnegin.setAvailableCopies(3);
        evgeniyOnegin.setGenres(Arrays.asList(classics, poetry, novel));
        bookRepository.save(evgeniyOnegin);

        Book warAndPeace = new Book("Война и мир", "978-5-17-123456-7");
        warAndPeace.setAuthor(tolstoy);
        warAndPeace.setPublisher(ast);
        warAndPeace.setPublicationYear(2019);
        warAndPeace.setPages(1225);
        warAndPeace.setDescription("Роман-эпопея Льва Николаевича Толстого");
        warAndPeace.setPrice(new BigDecimal("890.00"));
        warAndPeace.setTotalCopies(3);
        warAndPeace.setAvailableCopies(2);
        warAndPeace.setGenres(Arrays.asList(classics, novel));
        bookRepository.save(warAndPeace);

        Book crimeAndPunishment = new Book("Преступление и наказание", "978-5-389-09876-5");
        crimeAndPunishment.setAuthor(dostoevsky);
        crimeAndPunishment.setPublisher(eksmo);
        crimeAndPunishment.setPublicationYear(2021);
        crimeAndPunishment.setPages(672);
        crimeAndPunishment.setDescription("Социально-психологический и социально-философский роман");
        crimeAndPunishment.setPrice(new BigDecimal("550.00"));
        crimeAndPunishment.setTotalCopies(4);
        crimeAndPunishment.setAvailableCopies(4);
        crimeAndPunishment.setGenres(Arrays.asList(classics, novel, philosophy));
        bookRepository.save(crimeAndPunishment);

        Book cherryOrchard = new Book("Вишнёвый сад", "978-5-17-654321-0");
        cherryOrchard.setAuthor(chekhov);
        cherryOrchard.setPublisher(ast);
        cherryOrchard.setPublicationYear(2022);
        cherryOrchard.setPages(128);
        cherryOrchard.setDescription("Пьеса в четырёх действиях Антона Павловича Чехова");
        cherryOrchard.setPrice(new BigDecimal("320.00"));
        cherryOrchard.setTotalCopies(2);
        cherryOrchard.setAvailableCopies(2);
        cherryOrchard.setGenres(Arrays.asList(classics, drama));
        bookRepository.save(cherryOrchard);

        Book annaKarenina = new Book("Анна Каренина", "978-5-04-111222-3");
        annaKarenina.setAuthor(tolstoy);
        annaKarenina.setPublisher(eksmo);
        annaKarenina.setPublicationYear(2020);
        annaKarenina.setPages(864);
        annaKarenina.setDescription("Роман Льва Толстого о трагической любви");
        annaKarenina.setPrice(new BigDecimal("680.00"));
        annaKarenina.setTotalCopies(3);
        annaKarenina.setAvailableCopies(3);
        annaKarenina.setGenres(Arrays.asList(classics, novel));
        bookRepository.save(annaKarenina);

        // Создаём выдачи книг
        BookLoan loan1 = new BookLoan("Иванов Иван Иванович", evgeniyOnegin, 
            LocalDate.now().minusDays(7), LocalDate.now().plusDays(7));
        loan1.setReaderEmail("ivanov@mail.ru");
        loan1.setReaderPhone("+7 (999) 123-45-67");
        bookLoanRepository.save(loan1);

        BookLoan loan2 = new BookLoan("Петрова Мария Сергеевна", warAndPeace, 
            LocalDate.now().minusDays(14), LocalDate.now().minusDays(2));
        loan2.setReaderEmail("petrova@gmail.com");
        loan2.setStatus(BookLoan.LoanStatus.OVERDUE);
        bookLoanRepository.save(loan2);

        BookLoan loan3 = new BookLoan("Сидоров Пётр Алексеевич", evgeniyOnegin, 
            LocalDate.now().minusDays(30), LocalDate.now().minusDays(16));
        loan3.setReturnDate(LocalDate.now().minusDays(17));
        loan3.setStatus(BookLoan.LoanStatus.RETURNED);
        bookLoanRepository.save(loan3);

        System.out.println("✅ Тестовые данные успешно загружены!");
    }
}

