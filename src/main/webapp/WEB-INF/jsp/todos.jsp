<!doctype html>

<html lang="en" data-framework="javascript">
	<head>
		<meta charset="utf-8">
		<meta http-equiv="X-UA-Compatible" content="IE=edge">
		<title>Spring.io - TodoMVC</title>
		<link rel="stylesheet" href="resources/css/base.css">
	</head>
	<body>
		<section id="todoapp">
			<header id="header">
				<h1>todos</h1>
				<input id="new-todo" placeholder="What needs to be done?" autofocus>
			</header>
			<section id="main">
				<input id="toggle-all" type="checkbox">
				<label for="toggle-all">Mark all as complete</label>
				<ul id="todo-list"></ul>
			</section>
			<footer id="footer">
				<span id="todo-count"></span>
				<ul id="filters">
					<li>
						<a href="#/">All</a>
					</li>
					<li>
						<a href="#/active">Active</a>
					</li>
					<li>
						<a href="#/completed">Completed</a>
					</li>
				</ul>
				<button id="clear-completed">Clear completed</button>
			</footer>
		</section>
		<footer id="info">
			<p>Double-click to edit a todo</p>
			<p>Created by <a href="http://twitter.com/oscargodson">Oscar Godson</a></p>
			<p>Refactored by <a href="https://github.com/cburgmer">Christoph Burgmer</a></p>
			<p>Part of <a href="http://todomvc.com">TodoMVC</a></p>
		</footer>
		<script src="resources/js/base.js"></script>
		<script src="resources/js/helpers.js"></script>
		<script src="resources/js/store.js"></script>
		<script src="resources/js/model.js"></script>
		<script src="resources/js/template.js"></script>
		<script src="resources/js/view.js"></script>
		<script src="resources/js/controller.js"></script>
		<script src="resources/js/app.js"></script>
	</body>
</html>
