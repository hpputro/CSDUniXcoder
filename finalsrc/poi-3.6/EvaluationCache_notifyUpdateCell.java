public void notifyUpdateCell(int bookIndex, int sheetIndex, EvaluationCell cell) {
		FormulaCellCacheEntry fcce = _formulaCellCache.get(cell);

		int rowIndex = cell.getRowIndex();
		int columnIndex = cell.getColumnIndex();
		Loc loc = new Loc(bookIndex, sheetIndex, rowIndex, columnIndex);
		PlainValueCellCacheEntry pcce = _plainCellCache.get(loc);

		if (cell.getCellType() == HSSFCell.CELL_TYPE_FORMULA) {
			if (fcce == null) {
				fcce = new FormulaCellCacheEntry();
				if (pcce == null) {
					if (_evaluationListener != null) {
						_evaluationListener.onChangeFromBlankValue(sheetIndex, rowIndex,
								columnIndex, cell, fcce);
					}
					updateAnyBlankReferencingFormulas(bookIndex, sheetIndex, rowIndex,
							columnIndex);
				}
				_formulaCellCache.put(cell, fcce);
			} else {
				fcce.recurseClearCachedFormulaResults(_evaluationListener);
				fcce.clearFormulaEntry();
			}
			if (pcce == null) {
							} else {
								pcce.recurseClearCachedFormulaResults(_evaluationListener);
				_plainCellCache.remove(loc);
			}
		} else {
			ValueEval value = WorkbookEvaluator.getValueFromNonFormulaCell(cell);
			if (pcce == null) {
				if (value != BlankEval.instance) {
																				pcce = new PlainValueCellCacheEntry(value);
					if (fcce == null) {
						if (_evaluationListener != null) {
							_evaluationListener.onChangeFromBlankValue(sheetIndex, rowIndex, columnIndex, cell, pcce);
						}
						updateAnyBlankReferencingFormulas(bookIndex, sheetIndex,
								rowIndex, columnIndex);
					}
					_plainCellCache.put(loc, pcce);
				}
			} else {
				if (pcce.updateValue(value)) {
					pcce.recurseClearCachedFormulaResults(_evaluationListener);
				}
				if (value == BlankEval.instance) {
					_plainCellCache.remove(loc);
				}
			}
			if (fcce == null) {
							} else {
								_formulaCellCache.remove(cell);
				fcce.setSensitiveInputCells(null);
				fcce.recurseClearCachedFormulaResults(_evaluationListener);
			}
		}
	}