		protected void resetVariables()
		{
			for (int i = 0; i < rowGroups.length; i++)
			{
				rowGroups[i].getFillVariable().setValue(null);
			}
			
			for (int i = 0; i < columnGroups.length; i++)
			{
				columnGroups[i].getFillVariable().setValue(null);
			}
			
			for (int i = 0; i < measures.length; i++)
			{
				measures[i].getFillVariable().setValue(null);
			}
			
			for (int row = 0; row <= rowGroups.length; ++row)
			{
				for (int col = 0; col <= columnGroups.length; ++col)
				{
					if (retrieveTotal[row][col])
					{
						for (int i = 0; i < measures.length; i++)
						{
							totalVariables[row][col][i].setValue(null);
						}
					}
				}
			}
		}