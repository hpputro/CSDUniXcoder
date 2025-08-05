	protected void inheritCellSize(int i, int j)
	{
		JRDesignCrosstabCell cell = crossCells[i][j];
		
		JRDesignCellContents contents = (JRDesignCellContents) cell.getContents();
		
		if (contents.getWidth() == JRCellContents.NOT_CALCULATED)
		{
			if (i < rowGroups.size())
			{
				JRDesignCrosstabCell rowCell = crossCells[rowGroups.size()][j];
				if (rowCell != null)
				{
					contents.setWidth(rowCell.getContents().getWidth());
				}
			}
			else
			{
				for (int k = j + 1; k <= columnGroups.size(); ++k)
				{
					if (crossCells[i][k] != null)
					{
						contents.setWidth(crossCells[i][k].getContents().getWidth());
						break;
					}
				}
			}
		}
		
		if (contents.getHeight() == JRCellContents.NOT_CALCULATED)
		{
			if (j < columnGroups.size())
			{
				JRDesignCrosstabCell colCell = crossCells[i][columnGroups.size()];
				if (colCell != null)
				{
					contents.setHeight(colCell.getContents().getHeight());
				}
			}
			else
			{
				for (int k = i + 1; k <= rowGroups.size(); ++k)
				{
					if (crossCells[k][j] != null)
					{
						contents.setHeight(crossCells[k][j].getContents().getHeight());
					}
				}
			}
		}
	}