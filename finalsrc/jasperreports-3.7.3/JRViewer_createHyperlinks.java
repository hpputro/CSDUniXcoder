protected void createHyperlinks(List elements, int offsetX, int offsetY)
	{
		if(elements != null && elements.size() > 0)
		{
			for(Iterator it = elements.iterator(); it.hasNext();)
			{
				JRPrintElement element = (JRPrintElement)it.next();

				JRImageMapRenderer imageMap = null;
				if (element instanceof JRPrintImage)
				{
					JRRenderable renderer = ((JRPrintImage) element).getRenderer();
					if (renderer instanceof JRImageMapRenderer)
					{
						imageMap = (JRImageMapRenderer) renderer;
						if (!imageMap.hasImageAreaHyperlinks())
						{
							imageMap = null;
						}
					}
				}
				boolean hasImageMap = imageMap != null;

				JRPrintHyperlink hyperlink = null;
				if (element instanceof JRPrintHyperlink)
				{
					hyperlink = (JRPrintHyperlink) element;
				}
				boolean hasHyperlink = !hasImageMap 
					&& hyperlink != null && hyperlink.getHyperlinkTypeValue() != HyperlinkTypeEnum.NONE;
				boolean hasTooltip = hyperlink != null && hyperlink.getHyperlinkTooltip() != null;

				if (hasHyperlink || hasImageMap || hasTooltip)
				{
					JPanel link;
					if (hasImageMap)
					{
						Rectangle renderingArea = new Rectangle(0, 0, element.getWidth(), element.getHeight());
						link = new ImageMapPanel(renderingArea, imageMap);
					}
					else 					{
						link = new JPanel();
						if (hasHyperlink)
						{
							link.addMouseListener(mouseListener);
						}
					}

					if (hasHyperlink)
					{
						link.setCursor(new Cursor(Cursor.HAND_CURSOR));
					}

					link.setLocation(
						(int)((element.getX() + offsetX) * realZoom),
						(int)((element.getY() + offsetY) * realZoom)
						);
					link.setSize(
						(int)(element.getWidth() * realZoom),
						(int)(element.getHeight() * realZoom)
						);
					link.setOpaque(false);

					String toolTip = getHyperlinkTooltip(hyperlink);
					if (toolTip == null && hasImageMap)
					{
						toolTip = "";					}
					link.setToolTipText(toolTip);

					pnlLinks.add(link);
					linksMap.put(link, element);
				}

				if (element instanceof JRPrintFrame)
				{
					JRPrintFrame frame = (JRPrintFrame) element;
					int frameOffsetX = offsetX + frame.getX() + frame.getLineBox().getLeftPadding().intValue();
					int frameOffsetY = offsetY + frame.getY() + frame.getLineBox().getTopPadding().intValue();
					createHyperlinks(frame.getElements(), frameOffsetX, frameOffsetY);
				}
			}
		}
	}