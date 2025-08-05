	protected void appendTruncateSuffix(AttributedCharacterIterator allParagraphs)
	{
		String truncateSuffx = getTruncateSuffix();
		if (truncateSuffx == null)
		{
			return;
		}
		
		int lineStart = prevMeasuredState.textOffset;

				StringBuffer lineText = new StringBuffer();
		allParagraphs.setIndex(lineStart);
		while (allParagraphs.getIndex() < measuredState.textOffset 
				&& allParagraphs.current() != '\n')
		{
			lineText.append(allParagraphs.current());
			allParagraphs.next();
		}
		int linePosition = allParagraphs.getIndex() - lineStart;
		
				boolean done = false;
		do
		{
			measuredState = prevMeasuredState.cloneState();

			String text = lineText.substring(0, linePosition) + truncateSuffx;
			AttributedString attributedText = new AttributedString(text);
			
						AttributedCharacterIterator lineAttributes = new AttributedString(
					allParagraphs, 
					measuredState.textOffset,
					measuredState.textOffset + linePosition).getIterator();
			setAttributes(attributedText, lineAttributes, 0);
			
						setAttributes(attributedText, globalAttributes, 
					text.length() - truncateSuffx.length(), text.length());
			
			AttributedCharacterIterator lineParagraph = attributedText.getIterator();
			
			BreakIterator breakIterator = 
				isToTruncateAtChar() 
				? BreakIterator.getCharacterInstance() 
				: BreakIterator.getLineInstance();
			LineBreakMeasurer lineMeasurer = new LineBreakMeasurer(
					lineParagraph,
					breakIterator,
					FONT_RENDER_CONTEXT);

			if (renderNextLine(lineMeasurer, lineParagraph))
			{
				int lastPos = lineMeasurer.getPosition();
								if (lastPos == linePosition + truncateSuffx.length())
				{
										measuredState.textOffset -= truncateSuffx.length();
					measuredState.textSuffix = truncateSuffx;
					done = true;
				}
				else
				{
					linePosition = breakIterator.preceding(linePosition);
					if (linePosition == BreakIterator.DONE)
					{
						
												String actualSuffix = truncateSuffx.substring(0, 
								measuredState.textOffset - prevMeasuredState.textOffset);
												if (prevMeasuredState.textOffset > 0
								&& allParagraphs.setIndex(prevMeasuredState.textOffset - 1) != '\n')
						{
														actualSuffix = '\n' + actualSuffix;
						}
						measuredState.textSuffix = actualSuffix;
						
												measuredState.textOffset = prevMeasuredState.textOffset;

						done = true;
					}
				}
			}
			else
			{
								done = true;
			}
		}
		while (!done);
	}