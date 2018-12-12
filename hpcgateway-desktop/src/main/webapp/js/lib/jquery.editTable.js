(function($) {
	
	"use strict";
	
	var Table = function(element,options){
		this.$element = $(element);
		this.defaultOptions = {
				'data' : [ [ '' ] ],
				'tableClass' : 'inputtable',
				'jsonData' : false,
				'columns' : [],
				'maxRows' : 999,
				'first_row' : true,
				'disable' : false,
				'fieldTemplates' : {
					 'text' : {
							'type' : 'text',
							'setValue' : function(column,value) {
								var input = $("<input>",{"type":"text","class":"edit_item"}).val(value);
								if( column["width"] !== undefined )
								{
									input.css({"width":column["width"]+"px"});
								};
								return input;
							},
							'getValue' : function(el) {
								return el.val();
							},
							'disable' : function(el) {
								el.prop("disabled",true);
							},
							'enable' : function(el) {
								el.prop("disabled",false);
							},
					},
					'checkbox' : {
						'type': 'checkbox',
						'setValue' : function(column,value) {
							var input = $("<input>",{"type":"checkbox","class":"edit_item"}).prop("checked",value);
							if( column["width"] !== undefined )
							{
								input.css({"width":column["width"]+"px"});
							};
							return input;
						},
						'getValue' : function(el) {
							return el.prop("checked");
						},
						'disable' : function(el) {
							return el.prop("disabled",true);
						},
						'enable' : function(el) {
							return el.prop("disabled",false);
						},
					}
				},
				'validate_field' : function(col_id, value, col_type, $element) {
					return true;
				}
		};
		this.options = $.extend(this.defaultOptions,options);
	};

	
	Table.prototype.addColumn = function(column)
	{
		// { name, type, width }
		this.options.columns.push(column);
	};
	
	Table.prototype.addTemplate = function(template)
	{
		// { type, html, setValue, getValue }
		this.options.fieldTemplates[template.type] = template;
	};
	
	Table.prototype.addSelectTemplate = function(type,list)
	{
		var html = '';
		var tpl = _.template('<option value="<%=value%>"><%=name%></option>');
		$.each(list,function(idx,item){
			html = html + tpl(item);
		});

		var template = {
				'type' : type,
				'setValue' : function(column,value) {
					var select = $("<select/>",{"html":html,"class":"edit_item"}).val(value);
					if( column["width"] !== undefined )
					{
						select.css({"width":column["width"]+"px"});
					};
					return select;
				},
				'getValue' : function(el) {
					return el.val();
				},
				'disable' : function(el) {
					return el.prop("disabled",true);
				},
				'enable' : function(el) {
					return el.prop("disabled",false);
				},
		};
		this.addTemplate(template);
	};
	
	Table.prototype.buildCell = function(column,value) 
	{
		value = (value === undefined) ? "" : (value||"");
		var type = column["type"];
		var template = this.options.fieldTemplates[type];
		if( template === undefined )
		{
			template = this.options.fieldTemplates["text"];
		};
		return template.setValue.call(template,column,value);
	};
	
	Table.prototype.buildRow = function(row) 
	{
		var tr = $("<tr/>");
		var columns = this.options["columns"];
		var _this = this;
		if( row !== undefined )
		{
			$.each(row,function(idx,value){
				$("<td/>").append(_this.buildCell.call(_this,columns[idx],value)).appendTo(tr);
			});
		}
		else
		{
			$.each(columns,function(idx,column){
				$("<td/>").append(_this.buildCell.call(_this,column,"")).appendTo(tr);
			});
		};
		var rm_btn = $('<a class="delrow icon-button" href="#"></a>');
		$("<td/>").append(rm_btn).appendTo(tr);
		rm_btn.click(function(e){
			e.preventDefault();
			if( !_this.options["disable"] )
			{
				tr.remove();
			};
		});
		return tr;
	};
	
	Table.prototype.show = function(data){
		var defaultTableContent = '<thead><tr></tr></thead><tbody></tbody>';
		var $table = $('<table/>', { "html": defaultTableContent });
		$table.addClass(this.options.tableClass);
		if(this.options.first_row)
		{
			$table.addClass("wh");
		};
		this.$element.empty().append($table);
		var head = $table.find("thead tr");
		$.each(this.options.columns,function(idx,col){
			var th = $("<th/>").append(col.name).appendTo(head);
			if( col.width !== undefined )
			{
				th.css({"width":col.width+"px"})
			};
		});
		var add_btn = $('<a class="addrow icon-button" href="#"></a>');
		$("<th/>").append(add_btn).appendTo(head);
		var _this = this;
		add_btn.click(function(e){
			e.preventDefault();
			if( !_this.options["disable"] )
			{
				_this.addRow.call(_this);
			};
		});
		var _this = this;
		var _tbody = $table.find("tbody").empty();
		if( data !== undefined )
		{
			$.each(data,function(idx,row){
				_this.buildRow.call(_this,row).appendTo(_tbody);
			});
		};
		$table.on('dblclick', 'input', function() {
			$(this).select();
		});
	};
	
	Table.prototype.addRow = function() {
		var id = "table."+this.options["tableClass"]+" > tbody";
		this.$element.find(id).append(this.buildRow());
	};

	
	Table.prototype.reset = function() {
		
	};
	
	Table.prototype.disable = function() {
		this.options["disable"] = true;
		var id = "table."+this.options["tableClass"];
		var me = this.$element.find(id);
		//me.find("a.icon-button").unbind();
		var columns = this.options["columns"];
		var templates = this.options["fieldTemplates"];
		$.each(me.find("tbody>tr"),function(idx,tr){
			$.each($(tr).find("td:not(:last-child)"),function(i,td){
				var proc = templates[columns[i]["type"]]["disable"];
				var $el = $(td).find(".edit_item");
				proc($el);
			});
		});
	};
	
	Table.prototype.enable = function() {
		this.options["disable"] = false;
		var id = "table."+this.options["tableClass"];
		var me = this.$element.find(id);
		//me.find("a.icon-button").unbind();
		var columns = this.options["columns"];
		var templates = this.options["fieldTemplates"];
		$.each(me.find("tbody>tr"),function(idx,tr){
			$.each($(tr).find("td:not(:last-child)"),function(i,td){
				var proc = templates[columns[i]["type"]]["enable"];
				var $el = $(td).find(".edit_item");
				proc($el);
			});
		});
	};

	Table.prototype.exports = function(callback){
		var data = [];
		var id = "table."+this.options["tableClass"]+" > tbody";
		var tbody = this.$element.find(id)
		var columns = this.options["columns"];
		var templates = this.options["fieldTemplates"];
		$.each(tbody.find("tr"),function(idx,tr){
			var row = [];
			$.each($(tr).find("td:not(:last-child)"),function(i,td){
				var getValue = templates[columns[i]["type"]]["getValue"];
				var $el = $(td).find(".edit_item");
				row.push(getValue($el));
			});
			data.push(row);
		});
		callback(data);
	};
	
	var old = $.fn.editTable;
	
	$.fn.editTable = function(method) {
		var args = Array.prototype.slice.call(arguments,1);
		this.each(function(){
			var $this = $(this);
			var editTable = $this.data("js.editTable");
			if( !editTable )
			{
				var options = typeof method == "object" ? method : {};
				$this.data("js.editTable",(editTable=new Table(this,options)));
			};
			if( typeof method == "string" && editTable[method] )
			{
				editTable[method].apply(editTable,args);
			};
		});
		
		return this;
	};
	
	$.fn.editTable.Constructor = Table;
	
   $.fn.editTable.noConflict = function () {
        $.fn.editTable = old;
        return this
    };

/*
	$.fn.editTable = function(options) {
		
		 var Table = function(){
			 
		 };
		 
	var defaultOptions = 

	var s = $.extend(defaultOptions, options);
	
	var addColumn = function(type,name,length)
	{
		s.row_templates.push({"type":type,"name":name,"length":length});
	};
	var registerFieldTemplate = function(type,el,get,set)
	{
		s.fieldTemplates[type] = {"el":el,"getValue":get,"setValue":set};
	};
	
		var $el = $(this);
		var defaultTableContent = '<thead><tr></tr></thead><tbody></tbody>';
		var $table = $('<table/>', { 'class':s.tableClass+((s.first_row)?' wh':''),html : defaultTableContent});
		var defaultth = '<th><a class="addcol icon-button" href="#">+</a> <a class="delcol icon-button" href="#">-</a></th>';
		var colnumber, rownumber, reset;
		var is_validated = true;
		var rowOperateField = '<td><a class="delrow icon-button" href="#">-</a></td>';
		
		// Build cell


		// Build row


		// Check button status (enable/disabled)
		var checkButtons = function() {
			if (colnumber < 2) {
				$table.find('.delcol').addClass('disabled');
			}
			if (rownumber < 2) {
				$table.find('.delrow').addClass('disabled');
			}
			if (s.maxRows && rownumber === s.maxRows) {
				$table.find('.addrow').addClass('disabled');
			}
		};

		// Fill table with data
		var fillTableData = function(data)
		{
			var row = Math.min(s.maxRows, data.length);
			// Clear table
			$table.html(defaultTableContent);
			// If headers or row_template are set
			if( s.columns.length > 0 ) 
			{
				$.each(s.columns,function(idx,title){
					$table.find('thead tr').append('<th>' + title + '</th>');
				});
			};
			// Table content
			$.each(data,function(idx,item){
				buildRow(item).appendTo($table.find('tbody'));
			});

			// Append missing th
			$table.find('thead tr').append('<th></th>');

			// Count rows and columns
			colnumber = $table.find('thead th').length - 1;
			rownumber = $table.find('tbody tr').length;

			checkButtons();
		}

		// Export data
		function exportData()
		{
			var row = 0, data = [], value;

			is_validated = true;

			$table.find('tbody tr')
					.each(
							function() {

								row += 1;
								data[row] = [];

								$(this)
										.find('td:not(:last-child)')
										.each(
												function(i, v) {
													if (s.row_template
															&& 'text' !== s.row_template[i]) {
														var field = s.fieldTemplates[s.row_template[i]], el = $(
																this)
																.find(
																		$(
																				field.html)
																				.prop(
																						'tagName'));

														value = field
																.getValue(el);
														if (!s
																.validate_field(
																		i,
																		value,
																		s.row_template[i],
																		el)) {
															is_validated = false;
														}
														data[row].push(value);
													} else {
														value = $(this)
																.find(
																		'input[type="text"]')
																.val();
														if (!s.validate_field(
																i, value,
																'text', v)) {
															is_validated = false;
														}
														data[row].push(value);
													}
												});

							});

			// Remove undefined
			data.splice(0, 1);

			return data;
		}

		// Fill the table with data from textarea or given properties
		if ($el.is('textarea')) {

			try {
				reset = JSON.parse($el.val());
			} catch (e) {
				reset = s.data;
			}

			$el.after($table);

			// If inside a form set the textarea content on submit
			if ($table.parents('form').length > 0) {
				$table.parents('form').submit(function() {
					$el.val(JSON.stringify(exportData()));
				});
			}

		} else {
			reset = (JSON.parse(s.jsonData) || s.data);
			$el.append($table);
		}

		fillTableData(reset);

		// Add column
		$table.on('click', '.addcol', function() {

			var colid = parseInt($(this).closest('tr').children().index(
					$(this).parent('th')), 10);

			colnumber += 1;

			$table.find('thead tr').find('th:eq(' + colid + ')').after(
					defaultth);

			$table.find('tbody tr').each(function() {
				$(this).find('td:eq(' + colid + ')').after(buildCell());
			});

			$table.find('.delcol').removeClass('disabled');

			return false;
		});

		// Remove column
		$table.on('click', '.delcol', function() {

			if ($(this).hasClass('disabled')) {
				return false;
			}

			var colid = parseInt($(this).closest('tr').children().index(
					$(this).parent('th')), 10);

			colnumber -= 1;

			checkButtons();

			$(this).parent('th').remove();

			$table.find('tbody tr').each(function() {
				$(this).find('td:eq(' + colid + ')').remove();
			});

			return false;
		});

		// Add row
		$table.on('click', '.addrow', function() {

			if ($(this).hasClass('disabled')) {
				return false;
			}

			rownumber += 1;

			$(this).closest('tr').after(buildRow(0, colnumber));

			$table.find('.delrow').removeClass('disabled');

			checkButtons();

			return false;
		});

		// Delete row
		$table.on('click', '.delrow', function() {

			if ($(this).hasClass('disabled')) {
				return false;
			}

			rownumber -= 1;

			checkButtons();

			$(this).closest('tr').remove();

			$table.find('.addrow').removeClass('disabled');

			return false;
		});

		// Select all content on click
		$table.on('click', 'input', function() {
			$(this).select();
		});

		// Return functions
		return {
			// Get an array of data
			getData : function() {
				return exportData();
			},
			// Get the JSON rappresentation of data
			getJsonData : function() {
				return JSON.stringify(exportData());
			},
			// Load an array of data
			loadData : function(data) {
				fillTableData(data);
			},
			// Load a JSON rappresentation of data
			loadJsonData : function(data) {
				fillTableData(JSON.parse(data));
			},
			// Reset data to the first instance
			reset : function() {
				fillTableData(reset);
			},
			isValidated : function() {
				return is_validated;
			}
		};
	};
*/
})(jQuery, this, 0);