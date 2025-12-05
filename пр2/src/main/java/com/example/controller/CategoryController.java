package com.example.controller;

import com.example.model.Category;
import com.example.service.CategoryService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

/**
 * Контроллер для управления категориями (Controller в MVC)
 */
@Controller
@RequestMapping("/categories")
public class CategoryController {

    private final CategoryService categoryService;

    @Autowired
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    // READ - список всех категорий с фильтрацией и сортировкой
    @GetMapping
    public String list(@RequestParam(required = false) String search,
                       @RequestParam(required = false, defaultValue = "name") String sortBy,
                       @RequestParam(required = false, defaultValue = "asc") String sortDir,
                       Model model) {
        List<Category> categories;
        
        if (search != null && !search.isEmpty()) {
            categories = categoryService.findByName(search);
        } else {
            categories = categoryService.findAllSorted(sortBy, sortDir);
        }
        
        model.addAttribute("categories", categories);
        model.addAttribute("search", search);
        model.addAttribute("sortBy", sortBy);
        model.addAttribute("sortDir", sortDir);
        
        return "categories/list";
    }

    // CREATE - форма создания
    @GetMapping("/new")
    public String createForm(Model model) {
        model.addAttribute("category", new Category());
        return "categories/form";
    }

    // CREATE - сохранение
    @PostMapping
    public String create(@Valid @ModelAttribute("category") Category category,
                         BindingResult result,
                         RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "categories/form";
        }
        
        categoryService.save(category);
        redirectAttributes.addFlashAttribute("success", "Категория успешно создана");
        return "redirect:/categories";
    }

    // READ - просмотр одной категории
    @GetMapping("/{id}")
    public String view(@PathVariable Long id, Model model) {
        Category category = categoryService.findById(id)
                .orElseThrow(() -> new RuntimeException("Категория не найдена"));
        model.addAttribute("category", category);
        return "categories/view";
    }

    // UPDATE - форма редактирования
    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        Category category = categoryService.findById(id)
                .orElseThrow(() -> new RuntimeException("Категория не найдена"));
        model.addAttribute("category", category);
        return "categories/form";
    }

    // UPDATE - сохранение изменений
    @PostMapping("/{id}")
    public String update(@PathVariable Long id,
                         @Valid @ModelAttribute("category") Category category,
                         BindingResult result,
                         RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "categories/form";
        }
        
        categoryService.update(id, category);
        redirectAttributes.addFlashAttribute("success", "Категория успешно обновлена");
        return "redirect:/categories";
    }

    // DELETE - логическое удаление (одиночное)
    @PostMapping("/{id}/soft-delete")
    public String softDelete(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        categoryService.softDelete(id);
        redirectAttributes.addFlashAttribute("success", "Категория перемещена в корзину");
        return "redirect:/categories";
    }

    // DELETE - физическое удаление (одиночное)
    @PostMapping("/{id}/hard-delete")
    public String hardDelete(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        categoryService.hardDelete(id);
        redirectAttributes.addFlashAttribute("success", "Категория удалена безвозвратно");
        return "redirect:/categories";
    }

    // DELETE - множественное логическое удаление
    @PostMapping("/soft-delete-multiple")
    public String softDeleteMultiple(@RequestParam("ids") List<Long> ids,
                                     RedirectAttributes redirectAttributes) {
        categoryService.softDeleteMultiple(ids);
        redirectAttributes.addFlashAttribute("success", "Выбранные категории перемещены в корзину");
        return "redirect:/categories";
    }

    // DELETE - множественное физическое удаление
    @PostMapping("/hard-delete-multiple")
    public String hardDeleteMultiple(@RequestParam("ids") List<Long> ids,
                                     RedirectAttributes redirectAttributes) {
        categoryService.hardDeleteMultiple(ids);
        redirectAttributes.addFlashAttribute("success", "Выбранные категории удалены безвозвратно");
        return "redirect:/categories";
    }

    // Корзина - список удалённых
    @GetMapping("/trash")
    public String trash(Model model) {
        model.addAttribute("categories", categoryService.findDeleted());
        return "categories/trash";
    }

    // Восстановление из корзины
    @PostMapping("/restore-multiple")
    public String restoreMultiple(@RequestParam("ids") List<Long> ids,
                                  RedirectAttributes redirectAttributes) {
        categoryService.restoreMultiple(ids);
        redirectAttributes.addFlashAttribute("success", "Выбранные категории восстановлены");
        return "redirect:/categories/trash";
    }
}


