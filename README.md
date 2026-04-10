# Shift CRM System

Упрощённая CRM-система для управления продавцами, их транзакциями и аналитики.
## Технологии

- **Java 21**
- **Spring Boot 3.4**
- **Spring Data JPA**
- **PostgreSQL Database**
- **H2 Database** (in-memory для тестов)
- **Gradle**
- **Lombok**

## Сборка и запуск

### Сборка проекта

С прогоном юнит-тестов:
```bash
./gradlew build
```

Без запуска тестов:
```bash
./gradlew build -x test
```

### Запуск приложения

Запуск через Gradle:
```bash
./gradlew bootRun
```

- Приложение запустится на `http://localhost:8080`
- Подключение к БД настраивается в файле `src/main/resources/application.properties`

## API

### Продавцы (Sellers)

| Метод  | URL                 | Описание              |
|--------|---------------------|-----------------------|
| GET    | `/api/sellers`      | Список всех продавцов |
| GET    | `/api/sellers/{id}` | Информация о продавце |
| POST   | `/api/sellers`      | Создать продавца      |
| PUT    | `/api/sellers/{id}` | Обновить продавца     |
| DELETE | `/api/sellers/{id}` | Удалить продавца      |

### Транзакции (Transactions)

| Метод | URL                                   | Описание                |
|-------|---------------------------------------|-------------------------|
| GET   | `/api/transactions`                   | Список всех транзакций  |
| GET   | `/api/transactions/{id}`              | Информация о транзакции |
| POST  | `/api/transactions`                   | Создать транзакцию      |
| GET   | `/api/transactions/seller/{sellerId}` | Транзакции продавца     |

### Аналитика (Analytics)

| Метод | URL                                                                                                         | Описание                                                                                                          |
|-------|-------------------------------------------------------------------------------------------------------------|-------------------------------------------------------------------------------------------------------------------|
| GET   | `/api/transactions/analytics/most-productive?start=YYYY-MM-DDThh:mm:ss&end=YYYY-MM-DDThh:mm:ss`             | Информация о самом продуктивном продавце (по сумме) за указанный период времени                                   |
| GET   | `/api/transactions/analytics/low-sales?start=YYYY-MM-DDThh:mm:ss&end=YYYY-MM-DDThh:mm:ss&amount={Сумма}`    | Список продавцов, чья сумма транзакций за период строго меньше указанной `amount`                                 |
| GET   | `/api/transactions/analytics/best-period/{sellerId}?hours={Количество часов}`                               | Наилучший период скользящего окна для продавца с наибольшим количеством транзакций (размер окна по умолчанию 24ч) |

## Примеры использования

### Создать продавца

```bash
curl -X POST http://localhost:8080/api/sellers \
  -H "Content-Type: application/json" \
  -d '{"name": "Иван Иванов", "contactInfo": "ivan@mail.ru", "registrationDate": "2026-04-10T10:00:00"}'
```

Ответ:
```json
{
  "id": 1,
  "name": "Иван Иванов",
  "contactInfo": "ivan@mail.ru",
  "registrationDate": "2026-04-10T10:00:00"
}
```

### Получить всех продавцов

```bash
curl http://localhost:8080/api/sellers
```

### Создать транзакцию

```bash
curl -X POST http://localhost:8080/api/transactions \
  -H "Content-Type: application/json" \
  -d '{"seller": {"id": 1}, "amount": 1500.00, "paymentType": "CASH", "transactionDate": "2026-04-10T12:30:00"}'
```

Ответ:
```json
{
  "id": 1,
  "amount": 1500.00,
  "paymentType": "CASH",
  "transactionDate": "2026-04-10T12:30:00"
}
```

### Получить лучший период продавца (аналитика)

```bash
curl "http://localhost:8080/api/transactions/analytics/best-period/1?hours=48"
```

Ответ:
```json
{
  "sellerId": 1,
  "windowSizeHours": 48,
  "bestPeriodStart": "2026-04-10T10:00:00",
  "bestPeriodEnd": "2026-04-11T18:00:00",
  "transactionCount": 5
}
```

## Модели данных

### Seller (Продавец)

| Поле             | Тип           | Описание                            |
|------------------|---------------|-------------------------------------|
| id               | Long          | Уникальный идентификатор            |
| name             | String        | Имя продавца                        |
| contactInfo      | String        | Контактные данные                   |
| registrationDate | LocalDateTime | Дата и время регистрации в системе  |

### Transaction (Транзакция)

| Поле            | Тип                | Описание                          |
|-----------------|--------------------|-----------------------------------|
| id              | Long               | Уникальный идентификатор          |
| seller          | Seller             | Продавец (связь ManyToOne)        |
| amount          | BigDecimal         | Сумма транзакции                  |
| paymentType     | PaymentType (enum) | Тип оплаты (CASH, CARD, TRANSFER) |
| transactionDate | LocalDateTime      | Дата и время транзакции           |