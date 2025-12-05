package com.example.controller;

import com.example.model.Product;
import com.example.service.CategoryService;
import com.example.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.util.List;

/**
 * Контроллер для управления товарами (Controller в MVC)
 */
@Controller
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;
    private final CategoryService categoryService;

    @Autowired
    public ProductController(ProductService productService, CategoryService categoryService) {
        this.productService = productService;
        this.categoryService = categoryService;
    }

    // READ - список всех товаров с фильтрацией и сортировкой
    @GetMapping
    public String list(@RequestParam(required = false) String search,
                       @RequestParam(required = false) Long categoryId,
                       @RequestParam(required = false) BigDecimal minPrice,
                       @RequestParam(required = false) BigDecimal maxPrice,
                       @RequestParam(required = false, defaultValue = "name") String sortBy,
                       @RequestParam(required = false, defaultValue = "asc") String sortDir,
                       Model model) {
        
        List<Product> products;
        
        // Если есть фильтры - используем фильтрацию
        if ((search != null && !search.isEmpty()) || categoryId != null || 
            minPrice != null || maxPrice != null) {
            products = productService.findByFilters(search, categoryId, minPrice, maxPrice);
        } else {
            // Иначе - сортировка
            products = productService.findAllSorted(sortBy, sortDir);
        }
        
        model.addAttribute("products", products);
        model.addAttribute("categories", categoryService.findAll());
        model.addAttribute("search", search);
        model.addAttribute("categoryId", categoryId);
        model.addAttribute("minPrice", minPrice);
        model.addAttribute("maxPrice", maxPrice);
        model.addAttribute("sortBy", sortBy);
        model.addAttribute("sortDir", sortDir);
        
        return "products/list";
    }

    // CREATE - форма создания
    @GetMapping("/new")
    public String createForm(Model model) {
        model.addAttribute("product", new Product());
        model.addAttribute("categories", categoryService.findAll());
        return "products/form";
    }

    // CREATE - сохранение
    @PostMapping
    public String create(@Valid @ModelAttribute("product") Product product,
                         BindingResult result,
                         @RequestParam(required = false) Long categoryId,
                         Model model,
                         RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            model.addAttribute("categories", categoryService.findAll());
            return "products/form";
        }
        
        if (categoryId != null) {
            categoryService.findById(categoryId).ifPresent(product::setCategory);
        }
        
        productService.save(product);
        redirectAttributes.addFlashAttribute("success", "Товар успешно создан");
        return "redirect:/products";
    }

    // READ - просмотр одного товара
    @GetMapping("/{id}")
    public String view(@PathVariable Long id, Model model) {
        Product product = productService.findById(id)
                .orElseThrow(() -> new RuntimeException("Товар не найден"));
        model.addAttribute("product", product);
        return "products/view";
    }

    // UPDATE - форма редактирования
    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        Product product = productService.findById(id)
                .orElseThrow(() -> new RuntimeException("Товар не найден"));
        model.addAttribute("product", product);
        model.addAttribute("categories", categoryService.findAll());
        return "products/form";
    }

    // UPDATE - сохранение изменений
    @PostMapping("/{id}")
    public String update(@PathVariable Long id,
                         @Valid @ModelAttribute("product") Product product,
                         BindingResult result,
                         @RequestParam(required = false) Long categoryId,
                         Model model,
                         RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            model.addAttribute("categories", categoryService.findAll());
            return "products/form";
        }
        
        if (categoryId != null) {
            categoryService.findById(categoryId).ifPresent(product::setCategory);
        } else {
            product.setCategory(null);
        }
        
        productService.update(id, product);
        redirectAttributes.addFlashAttribute("success", "Товар успешно обновлён");
        return "redirect:/products";
    }

    // DELETE - логическое удаление (одиночное)
    @PostMapping("/{id}/soft-delete")
    public String softDelete(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        productService.softDelete(id);
        redirectAttributes.addFlashAttribute("success", "Товар перемещён в корзину");
        return "redirect:/products";
    }

    // DELETE - физическое удаление (одиночное)
    @PostMapping("/{id}/hard-delete")
    public String hardDelete(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        productService.hardDelete(id);
        redirectAttributes.addFlashAttribute("success", "Товар удалён безвозвратно");
        return "redirect:/products";
    }

    // DELETE - множественное логическое удаление
    @PostMapping("/soft-delete-multiple")
    public String softDeleteMultiple(@RequestParam("ids") List<Long> ids,
                                     RedirectAttributes redirectAttributes) {
        productService.softDeleteMultiple(ids);
        redirectAttributes.addFlashAttribute("success", "Выбранные товары перемещены в корзину");
        return "redirect:/products";
    }

    // DELETE - множественное физическое удаление
    @PostMapping("/hard-delete-multiple")
    public String hardDeleteMultiple(@RequestParam("ids") List<Long> ids,
                                     RedirectAttributes redirectAttributes) {
        productService.hardDeleteMultiple(ids);
        redirectAttributes.addFlashAttribute("success", "Выбранные товары удалены безвозвратно");
        return "redirect:/products";
    }

    // Корзина - список удалённых
    @GetMapping("/trash")
    public String trash(Model model) {
        model.addAttribute("products", productService.findDeleted());
        return "products/trash";
    }

    // Восстановление из корзины
    @PostMapping("/restore-multiple")
    public String restoreMultiple(@RequestParam("ids") List<Long> ids,
                                  RedirectAttributes redirectAttributes) {
        productService.restoreMultiple(ids);
        redirectAttributes.addFlashAttribute("success", "Выбранные товары восстановлены");
        return "redirect:/products/trash";
    }
}

