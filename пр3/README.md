# Система управления библиотекой

Spring Boot приложение с PostgreSQL для управления библиотекой.

## Требования

- Java 17+
- Maven 3.6+
- PostgreSQL 12+

## Настройка PostgreSQL

1. Создайте базу данных:
```sql
CREATE DATABASE library_db;
```

2. Настройте подключение в `application.properties`:
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/library_db
spring.datasource.username=postgres
spring.datasource.password=postgres
```

## Запуск

```bash
cd пр3
mvn spring-boot:run
```

Приложение будет доступно по адресу: http://localhost:8080

## Структура проекта

### Сущности (5 связанных таблиц)

1. **Author** (Автор) - OneToMany с Book
2. **Publisher** (Издательство) - OneToMany с Book
3. **Genre** (Жанр) - ManyToMany с Book
4. **Book** (Книга) - ManyToOne с Author, Publisher; ManyToMany с Genre; OneToMany с BookLoan
5. **BookLoan** (Выдача книги) - ManyToOne с Book

### Архитектура MVC

- **Model** - сущности в `com.library.model`
- **View** - Thymeleaf шаблоны в `templates/`
- **Controller** - контроллеры в `com.library.controller`

### Функциональность

- ✅ CRUD-операции для всех сущностей
- ✅ Валидация данных с аннотациями
- ✅ Фильтрация и сортировка
- ✅ Множественное удаление с проверкой связей
- ✅ Учёт выдачи книг
- ✅ Отслеживание просроченных выдач

## Технологии

- Spring Boot 3.2
- Spring Data JPA
- PostgreSQL
- Thymeleaf
- Bean Validation


