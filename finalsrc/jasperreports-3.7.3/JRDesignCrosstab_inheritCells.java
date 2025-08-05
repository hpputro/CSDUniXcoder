	protected void inheritCells()
	{
		for (int i = rowGroups.size(); i >= 0 ; --i)
		{
			for (int j = columnGroups.size(); j >= 0 ; --j)
			{
				boolean used = (i == rowGroups.size() || getRowGroup(i).hasTotal()) &&
					(j == columnGroups.size() || getColumnGroup(j).hasTotal());
				
				if (used)
				{
					if (crossCells[i][j] == null)
					{
						inheritCell(i, j);
						
						if (crossCells[i][j] == null)
						{
							crossCells[i][j] = emptyCell(i, j);
							inheritCellSize(i, j);
						}
					}
					else
					{
						inheritCellSize(i, j);
					}
				}
				else
				{
					crossCells[i][j] = null;
				}
			}
		}
	}