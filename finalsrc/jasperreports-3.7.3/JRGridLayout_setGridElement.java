protected void setGridElement(ElementWrapper wrapper, int row1, int col1, int row2, int col2)
	{
		yCuts.addUsage(row1, CutsInfo.USAGE_NOT_EMPTY);
		xCuts.addUsage(col1, CutsInfo.USAGE_NOT_EMPTY);

		if (col2 - col1 != 0 && row2 - row1 != 0)
		{
			JRPrintElement element = wrapper.getElement();
			JRPrintFrame frame = element instanceof JRPrintFrame ? (JRPrintFrame) element : null;

			int rowSpan = nature.isSpanCells() ? row2 - row1 : 1;
			int colSpan = nature.isSpanCells() ? col2 - col1 : 1;
			JRExporterGridCell gridCell =
				new ElementGridCell(
					wrapper,
					element.getWidth(),
					element.getHeight(),
					colSpan,
					rowSpan
					);

			if (frame != null)			{
				gridCell.setLayout(
					new JRGridLayout(
						nature,
						wrapper.getWrappers(),
						frame.getWidth(),
						frame.getHeight(),
						0, 						0, 						wrapper.getAddress()
						)
					);
			}

			gridCell.setBox((element instanceof JRBoxContainer)?((JRBoxContainer)element).getLineBox():null);

			if (nature.isBreakBeforeRow(element))
			{
				yCuts.addUsage(row1,  CutsInfo.USAGE_BREAK);
			}
			if (nature.isBreakAfterRow(element))
			{
				yCuts.addUsage(row1 + rowSpan,  CutsInfo.USAGE_BREAK);
			}

			if (nature.isSpanCells())
			{
				OccupiedGridCell occupiedGridCell = new OccupiedGridCell(gridCell);
				for (int row = row1; row < row2; row++)
				{
					for (int col = col1; col < col2; col++)
					{
						grid[row][col] = occupiedGridCell;
					}
					yCuts.addUsage(row, CutsInfo.USAGE_SPANNED);
				}

				for (int col = col1; col < col2; col++)
				{
					xCuts.addUsage(col, CutsInfo.USAGE_SPANNED);
				}
			}

			grid[row1][col1] = gridCell;
		}
	}