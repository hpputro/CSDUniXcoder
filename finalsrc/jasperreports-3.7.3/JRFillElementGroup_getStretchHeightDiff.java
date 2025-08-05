	protected int getStretchHeightDiff()
	{
		if (topElementInGroup == null)
		{
			stretchHeightDiff = 0;
			
			setTopBottomElements();

			JRElement[] allElements = getElements();

			if (allElements != null && allElements.length > 0)
			{
				JRFillElement topElem = null;
				JRFillElement bottomElem = null;

				for(int i = 0; i < allElements.length; i++)
				{
					JRFillElement element = (JRFillElement)allElements[i];
										if (element.isToPrint())
					{
						if (
							topElem == null ||
							(
							element.getRelativeY() + element.getStretchHeight() <
							topElem.getRelativeY() + topElem.getStretchHeight())
							)
						{
							topElem = element;
						}

						if (
							bottomElem == null ||
							(
							element.getRelativeY() + element.getStretchHeight() >
							bottomElem.getRelativeY() + bottomElem.getStretchHeight())
							)
						{
							bottomElem = element;
						}
					}
				}

				if (topElem != null)
				{
					stretchHeightDiff = 
						bottomElem.getRelativeY() + bottomElem.getStretchHeight() - topElem.getRelativeY() -
						(bottomElementInGroup.getY() + bottomElementInGroup.getHeight() - topElementInGroup.getY());
				}

				if (stretchHeightDiff < 0)
				{
					stretchHeightDiff = 0;
				}
			}
		}
		
		return stretchHeightDiff;
	}