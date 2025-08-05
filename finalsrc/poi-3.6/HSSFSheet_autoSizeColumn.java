public void autoSizeColumn(int column, boolean useMergedCells) {
        AttributedString str;
        TextLayout layout;
        
        char defaultChar = '0';

        
        double fontHeightMultiple = 2.0;

        FontRenderContext frc = new FontRenderContext(null, true, true);

        HSSFWorkbook wb = new HSSFWorkbook(_book);
        HSSFFont defaultFont = wb.getFontAt((short) 0);

        str = new AttributedString("" + defaultChar);
        copyAttributes(defaultFont, str, 0, 1);
        layout = new TextLayout(str.getIterator(), frc);
        int defaultCharWidth = (int)layout.getAdvance();

        double width = -1;
        rows:
        for (Iterator<Row> it = rowIterator(); it.hasNext();) {
            HSSFRow row = (HSSFRow) it.next();
            HSSFCell cell = row.getCell(column);

            if (cell == null) {
                continue;
            }

            int colspan = 1;
            for (int i = 0 ; i < getNumMergedRegions(); i++) {
                CellRangeAddress region = getMergedRegion(i);
                if (containsCell(region, row.getRowNum(), column)) {
                    if (!useMergedCells) {
                                                continue rows;
                    }
                    cell = row.getCell(region.getFirstColumn());
                    colspan = 1 + region.getLastColumn() - region.getFirstColumn();
                }
            }

            HSSFCellStyle style = cell.getCellStyle();
            int cellType = cell.getCellType();
            if(cellType == HSSFCell.CELL_TYPE_FORMULA) cellType = cell.getCachedFormulaResultType();

            HSSFFont font = wb.getFontAt(style.getFontIndex());

            if (cellType == HSSFCell.CELL_TYPE_STRING) {
                HSSFRichTextString rt = cell.getRichStringCellValue();
                String[] lines = rt.getString().split("\\n");
                for (int i = 0; i < lines.length; i++) {
                    String txt = lines[i] + defaultChar;
                    str = new AttributedString(txt);
                    copyAttributes(font, str, 0, txt.length());

                    if (rt.numFormattingRuns() > 0) {
                        for (int j = 0; j < lines[i].length(); j++) {
                            int idx = rt.getFontAtIndex(j);
                            if (idx != 0) {
                                HSSFFont fnt = wb.getFontAt((short) idx);
                                copyAttributes(fnt, str, j, j + 1);
                            }
                        }
                    }

                    layout = new TextLayout(str.getIterator(), frc);
                    if(style.getRotation() != 0){
                        
                        AffineTransform trans = new AffineTransform();
                        trans.concatenate(AffineTransform.getRotateInstance(style.getRotation()*2.0*Math.PI/360.0));
                        trans.concatenate(
                        AffineTransform.getScaleInstance(1, fontHeightMultiple)
                        );
                        width = Math.max(width, ((layout.getOutline(trans).getBounds().getWidth() / colspan) / defaultCharWidth) + cell.getCellStyle().getIndention());
                    } else {
                        width = Math.max(width, ((layout.getBounds().getWidth() / colspan) / defaultCharWidth) + cell.getCellStyle().getIndention());
                    }
                }
            } else {
                String sval = null;
                if (cellType == HSSFCell.CELL_TYPE_NUMERIC) {
                    String dfmt = style.getDataFormatString();
                    String format = dfmt == null ? null : dfmt.replaceAll("\"", "");
                    double value = cell.getNumericCellValue();
                    try {
                        NumberFormat fmt;
                        if ("General".equals(format))
                            sval = "" + value;
                        else
                        {
                            fmt = new DecimalFormat(format);
                            sval = fmt.format(value);
                        }
                    } catch (Exception e) {
                        sval = "" + value;
                    }
                } else if (cellType == HSSFCell.CELL_TYPE_BOOLEAN) {
                    sval = String.valueOf(cell.getBooleanCellValue());
                }
                if(sval != null) {
                    String txt = sval + defaultChar;
                    str = new AttributedString(txt);
                    copyAttributes(font, str, 0, txt.length());

                    layout = new TextLayout(str.getIterator(), frc);
                    if(style.getRotation() != 0){
                        
                        AffineTransform trans = new AffineTransform();
                        trans.concatenate(AffineTransform.getRotateInstance(style.getRotation()*2.0*Math.PI/360.0));
                        trans.concatenate(
                        AffineTransform.getScaleInstance(1, fontHeightMultiple)
                        );
                        width = Math.max(width, ((layout.getOutline(trans).getBounds().getWidth() / colspan) / defaultCharWidth) + cell.getCellStyle().getIndention());
                    } else {
                        width = Math.max(width, ((layout.getBounds().getWidth() / colspan) / defaultCharWidth) + cell.getCellStyle().getIndention());
                    }
                }
            }

        }
        if (width != -1) {
            width *= 256;
            if (width > Short.MAX_VALUE) {                 width = Short.MAX_VALUE;
            }
            _sheet.setColumnWidth(column, (short) (width));
        }
    }