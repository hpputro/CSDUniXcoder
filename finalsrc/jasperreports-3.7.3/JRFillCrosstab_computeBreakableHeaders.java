		protected boolean[] computeBreakableHeaders(HeaderCell[][] headersData, JRFillCrosstabGroup[] groups, int[] offsets, boolean width, boolean startHeaders)
		{
			boolean[] breakable = new boolean[headersData[0].length];
			for (int i = 0; i < breakable.length; i++)
			{
				breakable[i] = true;
			}

			for (int j = 0; j < groups.length; ++j)
			{
				JRFillCellContents fillHeader = groups[j].getFillHeader();
				
				if (fillHeader != null)
				{
					int size = width ? fillHeader.getWidth() : fillHeader.getHeight();
					
					for (int i = 0; i < headersData[0].length; i++)
					{
						HeaderCell header = headersData[j][i];
						if (header != null && !header.isTotal() && header.getLevelSpan() > 1)
						{
							int span = header.getLevelSpan();
							
							if (startHeaders)
							{
								for (int k = i + 1; k < i + span && offsets[k] - offsets[i] < size; ++k)
								{
									breakable[k] = false;
								}
							}
							
							for (int k = i + span - 1; k > i && offsets[i + span] - offsets[k] < size; --k)
							{
								breakable[k] = false;
							}
						}
					}
				}
			}
			return breakable;
		}