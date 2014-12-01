# ridiculous-todomvc

A fun experiment to see how much software architecture can be stuffed into a Spring.io based TODO list Java web application.

[ ![Codeship Status for mefernandez/ridiculous-todomvc](https://www.codeship.io/projects/01bf8030-448c-0132-c733-5ea9e4cf64c6/status)](https://www.codeship.io/projects/44841)

[ ![Heroku Favicon](https://github.com/heroku/favicon/raw/master/favicon.iconset/icon_32x32.png) **Application Running on Heroku**](https://pure-hamlet-7867.herokuapp.com/)

## Goal

Try to fit in as many Frameworks, Java language features, and Enterprise Patterns as possible into a Java web application based on [TODO MVC](http://todomvc.com/) project.

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

### v1.0.0 Building TODO MVC VanillaJS with Spring Boot _(work in progress)_

Start from scratch with [Spring Boot](http://projects.spring.io/spring-boot/).
Rewrite all tests in [TODO MVC VanillaJS](https://github.com/tastejs/todomvc/tree/gh-pages/examples/vanillajs) and make them pass.

#### Highlights
- Porting tests from TODO MVC VanillasJS was quite straightforward, although some tests needed to be interpreted in a different way, and some just could not be ported because they did not test any client-server interaction, like double clicking an item to enter in "edit mode".
- Although 26 ported tests passed Ok, much of the functionality still did not work. The testing "power" diminished, so to speak. Extra tests were added.
- The original TODO MVC stored Todos in local storage, hence a Todo list was only visible to a specific user. In this version, the Todo list is stored in the server (using `@Repository` with `JPA` and `HSQLDB`), which is shared and is visible to all users. Although this would not be the right behaviour for a Todo app, I think this will arise be some interesting scenarios to deal with, so let's leave it as it is for now.
- There's no `@Service` implementation yet. I rather wait to code it when I need it.
- I setup 2 `Todo` as test data, one active, one completed, `@Before` each `@Test` method. 
```java
	@Before
	public void setup() throws Exception {
    ...
		repository.save(new Todo("New Active Todo", false));
		repository.save(new Todo("New Completed Todo", true));
	}
```
But I needed to know the `@Id` for each `Todo` to set expectations at the end of each test. However, the id generation strategy is set to `@GeneratedValue` in the `Todo.class`. 
```java
  @Id
  @GeneratedValue(strategy=GenerationType.AUTO)
  Long id;
````
That caused Todos to have a greater id number every time `@Before` gets called. I searched all over the internet for information on how to change the id generation strategy at runtime to another one like `@Assigned`, but I found no example. I took some deep dives into Hibernate's API but did not find a way to properly swap strategies at runtime. Finally, debugging Hibernate code lead me to find where the strategy is stored, and so I could only change it using Reflection. Here's how:
```java
	@Autowired
	EntityManagerFactory factory;
	...
	SessionFactoryImplementor sessionFactory = this.factory.unwrap(SessionFactoryImplementor.class);
	EntityPersister persister = sessionFactory.getEntityPersister( Todo.class.getName() );
	IdentifierProperty identifierProperty = persister.getEntityMetamodel().getIdentifierProperty();
	/* Things get tough now, since I've found no way in Hibnerate's API to set a different Generator,
	 * so I'm using Reflection.
	 */
	Field f = identifierProperty.getClass().getDeclaredField("identifierGenerator");
	f.setAccessible(true);
	f.set(identifierProperty, myGenerator);
```
Definitively not elegant, but effective.

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


### v1.1.0 Add Jade View Tier

The idea is to keep the architecture shown above and add [Jade](http://jade-lang.com/) as a templating engine in the View tier using [jade4j](https://github.com/neuland/jade4j).

Also, let's add a footer with some form controls to switch between template engines. 
We'll extend this later on to make further changed in the application's configuration.

#### Highlights
- Adding `Jade` was a bliss. It only took a little effort to realize how to let Spring delegate to `JadeViewResolver`. I had to debug and inspect Spring's sources to see how `ContentNegotiatingViewResolver.getBestView()` picks a `ViewResolver` from the list of registered `ViewResolver`s. All it takes is sorting this list to prioritize a specific `ViewResolver`.
- Very basic JMeter tests are also in place to see how the app performs. It responds to 10 concurrent users in 1.5 seconds steadily. The intent is to see how the performance evolves as we add more layers and `archtifacts` to the app.

### v1.2.0 RabbitMQ

### v1.3.0 Cometd

### v1.4.0 Elastic Search

### v1.5.0 _(proposals anyone?)_
