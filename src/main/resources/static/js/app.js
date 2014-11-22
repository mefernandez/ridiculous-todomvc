(function () {
	'use strict';
	
	$on(window, 'load', function() {
		var $toggleAll = qs('#toggle-all');
		$on($toggleAll, 'change', function () {
			var $form = qs('#toggle-all').form;
			$form.submit();
        });		
	});

})();
