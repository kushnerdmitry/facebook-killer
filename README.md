# facebook-killer

####Требования:
Сделать микро-соцсеть: сгенерировать БД с десятками - сотней пользователей, имя, фамилия, возраст, какие-то интересы и у каждого есть подписки и подписчики. Ну и к ней базовые фичи: посмотреть информацию о ком-то, посмотреть его подписки/подписчиков/друзей (взаимные подписки)/общие интересы.
Можно по аналогии добавить туда группы. У группы есть админ/админы, есть информация о ней, есть подписчики.
К этому можно прикрутить REST (например, akka-http), можно формочку, если хочется.
Ну и понятно, у этого всего должна быть сборка в sbt

####[API LINK](https://facebook-killer.herokuapp.com)  

###Используемый стек

- scala 2.12
  - doobie
  - http4s
  - circe
  - pureconfig
  - flyway
- swagger
- sbt
- java 8
- docker
- docker-compose
- postgresql

###CI/CD

- github
- travis
- heroku

###TODOs
- Модульные тесты на:
  - Миграци
  - Чтнение конфига
  - Все алгебры 
- Интеграционные тесты на остальные модули
