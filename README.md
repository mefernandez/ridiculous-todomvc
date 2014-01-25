# ridiculous-todomvc

A fun experiment to see how much software architecture can be stuffed into a Spring.io based TODO list Java web application.

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

### v1. Pushing TODO MVC VanillaJS into Spring's Pet Clinic web application _(work in progress)_

This version adds:

1. Spring Tool Suite 3.4.0
2. Spring Framework
3. Spring's Pet Clinic sample
3. TODO MVC resources

#### Diagram

|Architecture Tiers|Implementation|Resources               |
|:----------------:|:------------:|:----------------------:|
|View              |JSP           |WEB-INF/jsp             |
|Controller        |Spring-MVC    |@Controller, .xml       |
|Services          |Spring-IoC    |@Service                |
|Repository        |Spring        |@Repository             |
|Model             |POJO          |Java Classes            |
|Persistence       |Spring-JDBC   |business.xml            |
|Database          |HSQL          |data-sources.properties |

![Spring framework architecture diagram of a full-fledged Spring web application](http://docs.spring.io/spring/docs/4.0.0.RELEASE/spring-framework-reference/htmlsingle/images/overview-full.png)

### v2. ...Thinking...
