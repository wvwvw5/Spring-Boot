package com.library.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.time.LocalDate;

/**
 * Сущность "Выдача книги"
 * Связь: ManyToOne с Book
 */
@Entity
@Table(name = "book_loans")
public class BookLoan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Имя читателя обязательно")
    @Size(min = 2, max = 200, message = "Имя должно быть от 2 до 200 символов")
    @Column(nullable = false)
    private String readerName;

    @Email(message = "Некорректный email")
    @Size(max = 100)
    private String readerEmail;

    @Pattern(regexp = "^[+]?[0-9\\-\\s()]*$", message = "Некорректный номер телефона")
    @Size(max = 20)
    private String readerPhone;

    @NotNull(message = "Дата выдачи обязательна")
    @Column(nullable = false)
    private LocalDate loanDate;

    @NotNull(message = "Дата возврата обязательна")
    @Column(nullable = false)
    private LocalDate dueDate;

    private LocalDate returnDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private LoanStatus status = LoanStatus.ACTIVE;

    @Size(max = 500)
    private String notes;

    // Связь многие-к-одному с Book
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id", nullable = false)
    private Book book;

    // Enum для статуса выдачи
    public enum LoanStatus {
        ACTIVE("Активна"),
        RETURNED("Возвращена"),
        OVERDUE("Просрочена");

        private final String displayName;

        LoanStatus(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return displayName;
        }
    }

    // Конструкторы
    public BookLoan() {}

    public BookLoan(String readerName, Book book, LocalDate loanDate, LocalDate dueDate) {
        this.readerName = readerName;
        this.book = book;
        this.loanDate = loanDate;
        this.dueDate = dueDate;
    }

    // Вспомогательные методы
    public boolean isOverdue() {
        return status == LoanStatus.ACTIVE && LocalDate.now().isAfter(dueDate);
    }

    // Геттеры и сеттеры
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getReaderName() { return readerName; }
    public void setReaderName(String readerName) { this.readerName = readerName; }

    public String getReaderEmail() { return readerEmail; }
    public void setReaderEmail(String readerEmail) { this.readerEmail = readerEmail; }

    public String getReaderPhone() { return readerPhone; }
    public void setReaderPhone(String readerPhone) { this.readerPhone = readerPhone; }

    public LocalDate getLoanDate() { return loanDate; }
    public void setLoanDate(LocalDate loanDate) { this.loanDate = loanDate; }

    public LocalDate getDueDate() { return dueDate; }
    public void setDueDate(LocalDate dueDate) { this.dueDate = dueDate; }

    public LocalDate getReturnDate() { return returnDate; }
    public void setReturnDate(LocalDate returnDate) { this.returnDate = returnDate; }

    public LoanStatus getStatus() { return status; }
    public void setStatus(LoanStatus status) { this.status = status; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }

    public Book getBook() { return book; }
    public void setBook(Book book) { this.book = book; }
}

