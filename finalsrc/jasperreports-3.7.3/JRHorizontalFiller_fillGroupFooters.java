	private void fillGroupFooters(boolean isFillAll) throws JRException
	{
		if (groups != null && groups.length > 0)
		{
			SavePoint savePoint = null;
			
			byte evaluation = (isFillAll)?JRExpression.EVALUATION_DEFAULT:JRExpression.EVALUATION_OLD;

			for(int i = groups.length - 1; i >= 0; i--)
			{
				JRFillGroup group = groups[i];
				
				if (isFillAll || group.hasChanged())
				{
					SavePoint newSavePoint = fillGroupFooter(group, evaluation);
					if (newSavePoint != null)
					{
						switch (group.getFooterPositionValue())
						{
							case STACK_AT_BOTTOM:
							{
								savePoint = advanceSavePoint(savePoint, newSavePoint);

								if (savePoint != null)
								{
									savePoint.footerPosition = FooterPositionEnum.STACK_AT_BOTTOM;
								}

								break;
							}
							case FORCE_AT_BOTTOM:
							{
								savePoint = advanceSavePoint(savePoint, newSavePoint);

								if (savePoint != null)
								{
									savePoint.moveSavePointContent();
									offsetY = columnFooterOffsetY;
								}

								savePoint = null;

								break;
							}
							case COLLATE_AT_BOTTOM:
							{
								savePoint = advanceSavePoint(savePoint, newSavePoint);

								break;
							}
							case NORMAL:
							default:
							{
								if (savePoint != null)
								{
																		
																		if (
										savePoint.page == newSavePoint.page
										&& savePoint.columnIndex == newSavePoint.columnIndex
										)
									{
																																								
										if (savePoint.footerPosition == FooterPositionEnum.STACK_AT_BOTTOM)
										{
											savePoint.saveHeightOffset(newSavePoint.heightOffset);
										}
										else
										{
																						savePoint = null;
										}
									}
									else
									{
																																								savePoint.moveSavePointContent();
										savePoint = null;
									}
								}
								else
								{
																		savePoint = null;
								}
							}
						}
					}
					
															if (
						keepTogetherSavePoint != null
						&& i <= keepTogetherSavePoint.groupIndex
						)
					{
						keepTogetherSavePoint = null;
					}
				}
			}
			
			if (savePoint != null)
			{
				savePoint.moveSavePointContent();
				offsetY = columnFooterOffsetY;
			}
		}
	}