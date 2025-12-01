# Калькулятор и Конвертер валют

Простое веб-приложение на Spring Boot с двумя страницами:
- **Калькулятор** — операции сложения, вычитания, умножения и деления
- **Конвертер валют** — конвертация рублей в иностранные валюты

## Требования

- Java 17+
- Maven 3.6+

## Запуск

```bash
# Перейти в папку проекта
cd "уп 1"

# Запустить приложение
./mvnw spring-boot:run
```

Или для Windows:
```bash
mvnw.cmd spring-boot:run
```

## Использование

После запуска откройте в браузере:
- http://localhost:8080/calculator — калькулятор
- http://localhost:8080/converter — конвертер валют

## Структура проекта

```
src/
├── main/
│   ├── java/com/example/
│   │   ├── Application.java           # Главный класс
│   │   └── controller/
│   │       ├── CalculatorController.java  # Контроллер калькулятора
│   │       └── CurrencyController.java    # Контроллер конвертера
│   └── resources/
│       ├── templates/
│       │   ├── calculator.html        # Страница калькулятора
│       │   └── converter.html         # Страница конвертера
│       ├── static/css/
│       │   └── style.css              # Стили
│       └── application.properties     # Настройки
```

## Технологии

- Spring Boot 3.2
- Thymeleaf (шаблонизатор)
- HTML5 / CSS3

