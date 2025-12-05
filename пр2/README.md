# CRUD Application - MVC архитектура

Веб-приложение на Spring Boot для управления товарами и категориями с полным набором CRUD-операций.

## Функционал

- ✅ **CRUD операции** для двух сущностей (Product и Category)
- ✅ **Валидация полей** при помощи аннотаций
- ✅ **Фильтрация** по нескольким параметрам (название, категория, цена)
- ✅ **Сортировка** по нескольким параметрам (название, цена, количество)
- ✅ **Множественное удаление** (логическое и физическое)
- ✅ **Корзина** с возможностью восстановления

## Запуск

```bash
cd пр2
mvn spring-boot:run
```

Приложение будет доступно по адресу: http://localhost:8080

## Структура MVC

```
src/main/java/com/example/
├── Application.java              # Точка входа
├── model/                        # MODEL - сущности
│   ├── Product.java
│   └── Category.java
├── repository/                   # MODEL - доступ к данным
│   ├── ProductRepository.java
│   └── CategoryRepository.java
├── service/                      # MODEL - бизнес-логика
│   ├── ProductService.java
│   └── CategoryService.java
├── controller/                   # CONTROLLER - обработка запросов
│   ├── HomeController.java
│   ├── ProductController.java
│   └── CategoryController.java
└── config/
    └── DataInitializer.java      # Инициализация тестовых данных

src/main/resources/
├── templates/                    # VIEW - шаблоны Thymeleaf
│   ├── index.html
│   ├── products/
│   │   ├── list.html
│   │   ├── form.html
│   │   ├── view.html
│   │   └── trash.html
│   └── categories/
│       ├── list.html
│       ├── form.html
│       ├── view.html
│       └── trash.html
└── static/css/
    └── style.css
```

## Технологии

- Spring Boot 3.2
- Spring Data JPA
- H2 Database (in-memory)
- Thymeleaf
- Bean Validation (Jakarta Validation)

