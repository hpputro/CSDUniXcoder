		public FillColumn visitColumnGroup(ColumnGroup columnGroup)
		{
			try
			{
				boolean toPrint = toPrintColumn(columnGroup, evaluation);
				FillColumn fillColumn;
				if (toPrint)
				{
					List<BaseColumn> columns = columnGroup.getColumns();
					List<FillColumn> subColumns = new ArrayList<FillColumn>(columns.size());
					int printWidth = 0;
					for (BaseColumn column : columns)
					{
						FillColumn fillSubColumn = column.visitColumn(this);
						if (fillSubColumn != null)
						{
							printWidth += fillSubColumn.getWidth();
							subColumns.add(fillSubColumn);
						}
					}
					
					if (subColumns.isEmpty())
					{
																		fillColumn = null;
					}
					else
					{
						fillColumn = new FillColumn(columnGroup, printWidth, subColumns);
					}
				}
				else
				{
					fillColumn = null;
				}
				return fillColumn;
			}
			catch (JRException e)
			{
				throw new JRRuntimeException(e);
			}
		}