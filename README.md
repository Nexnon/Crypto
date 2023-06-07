# Crypto

Упрощенная система криптобиржи. 
Реализована на Spring Boot. 
Все данные возвращаются в формате json.

## Реализованы следующие методы
#### Для Пользователя:
1. Регистрация нового пользователя;  ![](screenshots/2023-06-07_07-46-23.png)
2. Просмотр баланса своего кошелька;  ![](screenshots/2023-06-07_07-46-52.png)
3. Пополнение кошелька;  ![](screenshots/2023-06-07_07-47-10.png)
4. Вывод денег с биржи;  ![](screenshots/2023-06-07_07-47-33.png)
5. Просмотр актуальных курсов валют;  ![](screenshots/2023-06-07_07-47-56.png)
6. Обмен валют по установленному курсу.  ![](screenshots/2023-06-07_07-48-09.png)
#### Для Администратора:
1. Изменить курс валют;  ![](screenshots/2023-06-07_07-48-25.png)
2. Просмотреть общую сумму на все счета указанной валюты;  ![](screenshots/2023-06-07_07-48-40.png)
3. Просмотреть количество операций, которые были проведены за указанный период.  ![](screenshots/2023-06-07_07-48-54.png)
#### Если у пользователя недостаточно прав, возввращается json {"error":"you_dont_have_enough_permissions"}

## Все данные сохраняются в бд PostgeSQL
Для подключения бд использовался Hibernate  
Имеется 4 таблицы:
1. users (поля: secret_key \<pk\>, username, email)
2. wallets (поля: id \<pk\>, secret_key \<fk\>, value, currency)
3. rates (поля: id \<pk\>, first_currency, second_currency, rate)
4. operations (поля: id \<pk\>, date, code)

