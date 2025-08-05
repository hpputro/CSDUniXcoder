	public void writeCrosstab( JRCrosstab crosstab, String crosstabName)
	{
		if(crosstab != null)
		{
			write( "JRDesignCrosstab " + crosstabName + " = new JRDesignCrosstab(jasperDesign);\n");
			write( crosstabName + ".setRepeatColumnHeaders({0});\n", crosstab.isRepeatColumnHeaders(), true);
			write( crosstabName + ".setRepeatRowHeaders({0});\n", crosstab.isRepeatRowHeaders(), true);
			write( crosstabName + ".setColumnBreakOffset({0, number, #});\n", crosstab.getColumnBreakOffset(), JRCrosstab.DEFAULT_COLUMN_BREAK_OFFSET);
			write( crosstabName + ".setRunDirection({0});\n", crosstab.getRunDirectionValue(), RunDirectionEnum.LTR);
			write( crosstabName + ".setIgnoreWidth({0});\n", getBooleanText(crosstab.getIgnoreWidth()));
	
			writeReportElement( crosstab, crosstabName);
	
			JRCrosstabParameter[] parameters = crosstab.getParameters();
			if (parameters != null)
			{
				for (int i = 0; i < parameters.length; i++)
				{
					if (!parameters[i].isSystemDefined())
					{
						writeCrosstabParameter( parameters[i], crosstabName + "Parameter" + i);
						write( crosstabName + ".addParameter(" + crosstabName + "Parameter" + i + ");\n");
						
					}
				}
			}

			writeExpression( crosstab.getParametersMapExpression(), crosstabName, "ParametersMapExpression");
	
			writeCrosstabDataset( crosstab, crosstabName);

			writeCrosstabHeaderCell( crosstab, crosstabName);
	
			JRCrosstabRowGroup[] rowGroups = crosstab.getRowGroups();
			for (int i = 0; i < rowGroups.length; i++)
			{
				writeCrosstabRowGroup( rowGroups[i], crosstabName + "RowGroup" + i);
				write( crosstabName + ".addRowGroup(" + crosstabName + "RowGroup" + i + ");\n");
			}
	
			JRCrosstabColumnGroup[] columnGroups = crosstab.getColumnGroups();
			for (int i = 0; i < columnGroups.length; i++)
			{
				writeCrosstabColumnGroup( columnGroups[i], crosstabName + "ColumnGroup" + i);
				write( crosstabName + ".addColumnGroup(" + crosstabName + "ColumnGroup" + i + ");\n");
			}
	
			JRCrosstabMeasure[] measures = crosstab.getMeasures();
			for (int i = 0; i < measures.length; i++)
			{
				writeCrosstabMeasure( measures[i], crosstabName + "Measure" + i);
				write( crosstabName + ".addMeasure(" + crosstabName + "Measure" + i + ");\n");
			}
	
			if (crosstab instanceof JRDesignCrosstab)
			{
				List cellsList = ((JRDesignCrosstab) crosstab).getCellsList();
				for (int i = 0; i < cellsList.size(); i++)
				{
					JRCrosstabCell cell = (JRCrosstabCell) cellsList.get(i);
					writeCrosstabCell( cell, crosstabName + "Cell" + i);
					write( crosstabName + ".addCell(" + crosstabName + "Cell" + i + ");\n");
				}
			}
			else
			{
				JRCrosstabCell[][] cells = crosstab.getCells();
				Set cellsSet = new HashSet();
				for (int i = cells.length - 1; i >= 0 ; --i)
				{
					for (int j = cells[i].length - 1; j >= 0 ; --j)
					{
						JRCrosstabCell cell = cells[i][j];
						if (cell != null && cellsSet.add(cell))
						{
							writeCrosstabCell( cell, crosstabName + "Cell" + i + "" + j);
							write( crosstabName + ".addCell(" + crosstabName + "Cell" + i + "" + j + ");\n");
						}
					}
				}
			}
	
			writeCrosstabWhenNoDataCell( crosstab, crosstabName);

			flush();
		}
	}