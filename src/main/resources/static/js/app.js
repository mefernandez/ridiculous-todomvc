(function () {
	'use strict';
	
	$on(window, 'load', function() {
		
		var $toggleAll = qs('#toggle-all');
		$on($toggleAll, 'change', function () {
			var $form = this.form;
			$form.submit();
        });		

		var $toggleItems = qsa('.toggle');
		$toggleItems.forEach(function($item) {
			$on($item, 'change', function () {
				var $form = this.form;
				$form.submit();
			});
        });

		var $toggleItems = qsa('#todo-list li label');
		$toggleItems.forEach(function($item) {
			$on($item, 'dblclick', function () {
				var $form = this.form;
				var id = this.getAttribute('data-id');
				var $li = qs('li[data-id="' + id + '"]');
				$li.className = $li.className + ' editing';
			});
        });

		var $editItems = qsa('#todo-list li .edit');
		$editItems.forEach(function($item) {
			$on($item, 'keyup', function (event) {
				if (event.keyCode === 27) {
					var id = this.getAttribute('data-id');
					var $li = qs('li[data-id="' + id + '"]');
					$li.className = '';
				}
			});

			$on($item, 'blur', function () {
				var id = this.getAttribute('data-id');
				var $li = qs('li[data-id="' + id + '"]');
				$li.className = '';
			});
		});
		
	});

})();
