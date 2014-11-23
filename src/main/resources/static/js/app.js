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
	});

})();
