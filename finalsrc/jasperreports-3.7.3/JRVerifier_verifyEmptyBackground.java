	protected void verifyEmptyBackground()
	{
		JRBand background = jasperDesign.getBackground();
		if (background != null 
				&& background.getHeight() > 0)
		{
			JRElement[] elements = background.getElements();
			if (elements != null && elements.length > 0)
			{
				boolean foundContent = false;
				for (int i = 0; i < elements.length; i++)
				{
					if (elements[i].getWidth() > 0 && elements[i].getHeight() > 0)
					{
						foundContent = true;
						break;
					}
				}
				
				if (foundContent)
				{
					addBrokenRule("Use of the background section is not recommended " +
							"for reports that are supposed to be exported using grid exporters such as HTML and XLS " +
							"because the background content would likely be overlapped by other sections " +
							"resulting in it not showing up.", 
							background);
				}
			}
		}
	}