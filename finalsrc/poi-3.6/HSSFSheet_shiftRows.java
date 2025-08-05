	public void shiftRows(int startRow, int endRow, int n,
            boolean copyRowHeight, boolean resetOriginalRowHeight, boolean moveComments) {
        int s, inc;
        if (n < 0) {
            s = startRow;
            inc = 1;
        } else {
            s = endRow;
            inc = -1;
        }
        NoteRecord[] noteRecs;
        if (moveComments) {
            noteRecs = _sheet.getNoteRecords();
        } else {
            noteRecs = NoteRecord.EMPTY_ARRAY;
        }

        shiftMerged(startRow, endRow, n, true);
        _sheet.getPageSettings().shiftRowBreaks(startRow, endRow, n);

        for ( int rowNum = s; rowNum >= startRow && rowNum <= endRow && rowNum >= 0 && rowNum < 65536; rowNum += inc ) {
            HSSFRow row = getRow( rowNum );
            HSSFRow row2Replace = getRow( rowNum + n );
            if ( row2Replace == null )
                row2Replace = createRow( rowNum + n );


                                                                        row2Replace.removeAllCells();

                                    if (row == null) continue; 
                        if (copyRowHeight) {
                row2Replace.setHeight(row.getHeight());
            }
            if (resetOriginalRowHeight) {
                row.setHeight((short)0xff);
            }

                                    for(Iterator<Cell> cells = row.cellIterator(); cells.hasNext(); ) {
                HSSFCell cell = (HSSFCell)cells.next();
                row.removeCell( cell );
                CellValueRecordInterface cellRecord = cell.getCellValueRecord();
                cellRecord.setRow( rowNum + n );
                row2Replace.createCellFromRecord( cellRecord );
                _sheet.addValueRecord( rowNum + n, cellRecord );

                HSSFHyperlink link = cell.getHyperlink();
                if(link != null){
                    link.setFirstRow(link.getFirstRow() + n);
                    link.setLastRow(link.getLastRow() + n);
                }
            }
                        row.removeAllCells();

                                                if(moveComments) {
                                for(int i=noteRecs.length-1; i>=0; i--) {
                    NoteRecord nr = noteRecs[i];
                    if (nr.getRow() != rowNum) {
                        continue;
                    }
                    HSSFComment comment = getCellComment(rowNum, nr.getColumn());
                    if (comment != null) {
                       comment.setRow(rowNum + n);
                    }
                }
            }
        }
        if ( endRow == _lastrow || endRow + n > _lastrow ) _lastrow = Math.min( endRow + n, SpreadsheetVersion.EXCEL97.getLastRowIndex() );
        if ( startRow == _firstrow || startRow + n < _firstrow ) _firstrow = Math.max( startRow + n, 0 );

                        int sheetIndex = _workbook.getSheetIndex(this);
        short externSheetIndex = _book.checkExternSheet(sheetIndex);
        FormulaShifter shifter = FormulaShifter.createForRowShift(externSheetIndex, startRow, endRow, n);
        _sheet.updateFormulasAfterCellShift(shifter, externSheetIndex);

        int nSheets = _workbook.getNumberOfSheets();
        for(int i=0; i<nSheets; i++) {
            Sheet otherSheet = _workbook.getSheetAt(i).getSheet();
            if (otherSheet == this._sheet) {
                continue;
            }
            short otherExtSheetIx = _book.checkExternSheet(i);
            otherSheet.updateFormulasAfterCellShift(shifter, otherExtSheetIx);
        }
        _workbook.getWorkbook().updateNamesAfterCellShift(shifter);
    }