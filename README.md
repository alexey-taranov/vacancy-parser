## Парсер вакансий

**Общее описание.**

Приложение парсер ежедневно посещает сайт sql.ru в раздел "Работа" и собирает Java вакансии.

------------------------
### Используемые технологии:
* MySQL
* JDBC
* Log4j
* Maven

------------------------
* Реализован модуль сборки анализа данных с sql.ru.

* Система использует Jsoup для парсинга страниц.

* Система запускается раз в день.

Для этого была использована библиотека quartz. 
```
<!-- https://mvnrepository.com/artifact/org.quartz-scheduler/quartz -->
<dependency>
    <groupId>org.quartz-scheduler</groupId>
    <artifactId>quartz</artifactId>
    <version>2.3.0</version>
</dependency>
```
[Пример использования](http://www.quartz-scheduler.org/documentation/quartz-2.x/tutorials/tutorial-lesson-06.html)

* Система собирает данные только про вакансии java. Учтено , что JavaScript не подходит, как и Java Script.

* Данные хранятся в базе данных. 

* Для вывода нужной информации использовался логгер log4j.

