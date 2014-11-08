# ridiculous-todomvc

A fun experiment to see how much software architecture can be stuffed into a Spring.io based TODO list Java web application.

[ ![Codeship Status for mefernandez/ridiculous-todomvc](https://www.codeship.io/projects/01bf8030-448c-0132-c733-5ea9e4cf64c6/status)](https://www.codeship.io/projects/44841)

## Goal

Try to fit in as much Frameworks, Java language features, and Enterprise Patterns as possible into a Java web application based on [TODO MVC](http://todomvc.com/) project.

Examples of things that can be added are: 
- Spring.io projects: [Spring Security, etc.](http://spring.io/docs)
- [Java concurrency / multithreading features](http://docs.oracle.com/javase/tutorial/essential/concurrency/)
- Enterprise patterns. [Here's a bunch of them](http://martinfowler.com/articles/enterprisePatterns.html)
- Integration and Testing stuff: [Jenkins](http://jenkins-ci.org/), [JMeter](http://jmeter.apache.org/), etc.

## Rules

1. Every added software architecture feature must contribute to the functionality of the application.
2. Every added software architecture feature must be well behaved with the existing ones.
3. The final result must have tests in place to check the new feature works fine.
4. The final result must be added to the overall architecture diagram.

## Join

If you think it's fun then jump in! 
Things you can do:
- Fork this repo.
- Add yet another "archtifact" to the application.
- Post an issue, open a debate here at GitHub.
- Contact me, let's have a fireside chat on software development.

## Roadmap

### v1. Building TODO MVC VanillaJS with Spring Boot _(work in progress)_

Start from scratch with [Spring Boot](http://projects.spring.io/spring-boot/).
Rewrite all tests in [TODO MVC VanillaJS](https://github.com/tastejs/todomvc/tree/gh-pages/examples/vanillajs) and make them pass.

#### Architecture

|Architecture Tiers|Implementation      |Resources                               |
|:----------------:|:------------------:|:--------------------------------------:|
|View              |Thymeleaf           |src/main/resources/templates            |
|Controller        |spring-webmvc       |@Controller                             |
|Services          |spring-core         |@Service                                |
|Repository        |spring-data         |@Repository                             |
|Model             |POJO                |@Entity                                 |
|Persistence       |spring-data         |Hibernate                               |
|Database          |HSQL                |application.properties                  |


### v2. Add Jade View Tier

### v3. _(proposals anyone?)

