package com.library.controller;

import com.library.model.BookLoan;
import com.library.service.BookLoanService;
import com.library.service.BookService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/loans")
public class BookLoanController {

    private final BookLoanService bookLoanService;
    private final BookService bookService;

    @Autowired
    public BookLoanController(BookLoanService bookLoanService, BookService bookService) {
        this.bookLoanService = bookLoanService;
        this.bookService = bookService;
    }

    @GetMapping
    public String list(@RequestParam(required = false) String search,
                       @RequestParam(required = false) Long bookId,
                       @RequestParam(required = false) BookLoan.LoanStatus status,
                       @RequestParam(required = false, defaultValue = "loanDate") String sortBy,
                       Model model) {
        
        List<BookLoan> loans;
        if ((search != null && !search.isEmpty()) || bookId != null || status != null) {
            loans = bookLoanService.findByFilters(search, bookId, status);
        } else {
            loans = bookLoanService.findAllSorted(sortBy);
        }
        
        model.addAttribute("loans", loans);
        model.addAttribute("books", bookService.findAll());
        model.addAttribute("statuses", BookLoan.LoanStatus.values());
        model.addAttribute("search", search);
        model.addAttribute("bookId", bookId);
        model.addAttribute("status", status);
        model.addAttribute("sortBy", sortBy);
        
        return "loans/list";
    }

    @GetMapping("/new")
    public String createForm(Model model) {
        model.addAttribute("loan", new BookLoan());
        model.addAttribute("books", bookService.findAvailable());
        return "loans/form";
    }

    @PostMapping
    public String create(@Valid @ModelAttribute("loan") BookLoan loan,
                         BindingResult result,
                         @RequestParam Long bookId,
                         Model model,
                         RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            model.addAttribute("books", bookService.findAvailable());
            return "loans/form";
        }
        
        bookService.findById(bookId).ifPresent(loan::setBook);
        bookLoanService.save(loan);
        redirectAttributes.addFlashAttribute("success", "Выдача успешно оформлена");
        return "redirect:/loans";
    }

    @GetMapping("/{id}")
    public String view(@PathVariable Long id, Model model) {
        BookLoan loan = bookLoanService.findById(id)
                .orElseThrow(() -> new RuntimeException("Выдача не найдена"));
        model.addAttribute("loan", loan);
        return "loans/view";
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        BookLoan loan = bookLoanService.findById(id)
                .orElseThrow(() -> new RuntimeException("Выдача не найдена"));
        model.addAttribute("loan", loan);
        model.addAttribute("books", bookService.findAll());
        return "loans/form";
    }

    @PostMapping("/{id}")
    public String update(@PathVariable Long id,
                         @Valid @ModelAttribute("loan") BookLoan loan,
                         BindingResult result,
                         Model model,
                         RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            model.addAttribute("books", bookService.findAll());
            return "loans/form";
        }
        bookLoanService.update(id, loan);
        redirectAttributes.addFlashAttribute("success", "Выдача успешно обновлена");
        return "redirect:/loans";
    }

    @PostMapping("/{id}/return")
    public String returnBook(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        bookLoanService.returnBook(id);
        redirectAttributes.addFlashAttribute("success", "Книга успешно возвращена");
        return "redirect:/loans";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        bookLoanService.delete(id);
        redirectAttributes.addFlashAttribute("success", "Выдача успешно удалена");
        return "redirect:/loans";
    }

    @PostMapping("/delete-multiple")
    public String deleteMultiple(@RequestParam("ids") List<Long> ids,
                                 RedirectAttributes redirectAttributes) {
        bookLoanService.deleteMultiple(ids);
        redirectAttributes.addFlashAttribute("success", "Удалено выдач: " + ids.size());
        return "redirect:/loans";
    }

    @GetMapping("/overdue")
    public String overdue(Model model) {
        model.addAttribute("loans", bookLoanService.findOverdue());
        return "loans/overdue";
    }
}


