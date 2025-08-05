	private void fillSummaryWithLastFooterNoPageBands() throws JRException
	{
		if (
			!isSummaryNewPage
			&& columnIndex == 0
			&& summary.getBreakHeight() <= lastPageColumnFooterOffsetY - offsetY
			)
		{
			setLastPageFooter(true);

			summary.evaluatePrintWhenExpression(JRExpression.EVALUATION_DEFAULT);

			if (summary != missingFillBand && summary.isToPrint())
			{
				summary.evaluate(JRExpression.EVALUATION_DEFAULT);

				JRPrintBand printBand = summary.fill(columnFooterOffsetY - offsetY);

				if (summary.willOverflow() && summary.isSplitPrevented())
				{
					fillColumnFooter(JRExpression.EVALUATION_DEFAULT);

					fillPageFooter(JRExpression.EVALUATION_DEFAULT);

					resolveGroupBoundElements(JRExpression.EVALUATION_DEFAULT, true);
					resolveColumnBoundElements(JRExpression.EVALUATION_DEFAULT);
					resolvePageBoundElements(JRExpression.EVALUATION_DEFAULT);
					scriptlet.callBeforePageInit();
					calculator.initializeVariables(ResetTypeEnum.PAGE, IncrementTypeEnum.PAGE);
					scriptlet.callAfterPageInit();

					addPage(false);

					printBand = summary.refill(pageHeight - bottomMargin - offsetY);

					fillBand(printBand);
					offsetY += printBand.getHeight();
				}
				else
				{
					fillBand(printBand);
					offsetY += printBand.getHeight();

					fillColumnFooter(JRExpression.EVALUATION_DEFAULT);

					fillPageFooter(JRExpression.EVALUATION_DEFAULT);
				}

				
				fillSummaryOverflow(printBand);
				
							}
			else
			{
				fillColumnFooter(JRExpression.EVALUATION_DEFAULT);

				fillPageFooter(JRExpression.EVALUATION_DEFAULT);
				
							}
		}
		else if (
			!isSummaryNewPage
			&& columnIndex == 0
			&& summary.getBreakHeight() <= columnFooterOffsetY - offsetY
			)
		{
			summary.evaluatePrintWhenExpression(JRExpression.EVALUATION_DEFAULT);

			if (summary != missingFillBand && summary.isToPrint())
			{
				summary.evaluate(JRExpression.EVALUATION_DEFAULT);

				JRPrintBand printBand = summary.fill(columnFooterOffsetY - offsetY);

				if (summary.willOverflow() && summary.isSplitPrevented())
				{
					if (offsetY <= lastPageColumnFooterOffsetY)
					{
						setLastPageFooter(true);

						fillColumnFooter(JRExpression.EVALUATION_DEFAULT);

						fillPageFooter(JRExpression.EVALUATION_DEFAULT);

						resolveGroupBoundElements(JRExpression.EVALUATION_DEFAULT, true);
						resolveColumnBoundElements(JRExpression.EVALUATION_DEFAULT);
						resolvePageBoundElements(JRExpression.EVALUATION_DEFAULT);
						scriptlet.callBeforePageInit();
						calculator.initializeVariables(ResetTypeEnum.PAGE, IncrementTypeEnum.PAGE);
						scriptlet.callAfterPageInit();

						addPage(false);

						printBand = summary.refill(pageHeight - bottomMargin - offsetY);

						fillBand(printBand);
						offsetY += printBand.getHeight();
					}
					else
					{
						fillPageBreak(false, JRExpression.EVALUATION_DEFAULT, JRExpression.EVALUATION_DEFAULT, false);

						setLastPageFooter(true);

						printBand = summary.refill(lastPageColumnFooterOffsetY - offsetY);

						fillBand(printBand);
						offsetY += printBand.getHeight();

						fillColumnFooter(JRExpression.EVALUATION_DEFAULT);

						fillPageFooter(JRExpression.EVALUATION_DEFAULT);
					}
				}
				else
				{
					fillBand(printBand);
					offsetY += printBand.getHeight();

					fillPageBreak(false, JRExpression.EVALUATION_DEFAULT, JRExpression.EVALUATION_DEFAULT, false);

					setLastPageFooter(true);

					if (summary.willOverflow())
					{
						printBand = summary.fill(lastPageColumnFooterOffsetY - offsetY);

						fillBand(printBand);
						offsetY += printBand.getHeight();
					}

					fillColumnFooter(JRExpression.EVALUATION_DEFAULT);

					fillPageFooter(JRExpression.EVALUATION_DEFAULT);
				}

				
				fillSummaryOverflow(printBand);
				
							}
			else
			{
				if(offsetY > lastPageColumnFooterOffsetY)
				{
					fillPageBreak(false, JRExpression.EVALUATION_DEFAULT, JRExpression.EVALUATION_DEFAULT, false);
				}

				setLastPageFooter(true);

				fillColumnFooter(JRExpression.EVALUATION_DEFAULT);

				fillPageFooter(JRExpression.EVALUATION_DEFAULT);
				
							}
		}
		else if (columnIndex == 0 && offsetY <= lastPageColumnFooterOffsetY)
		{
			setLastPageFooter(true);

			fillColumnFooter(JRExpression.EVALUATION_DEFAULT);

			fillPageFooter(JRExpression.EVALUATION_DEFAULT);

			summary.evaluatePrintWhenExpression(JRExpression.EVALUATION_DEFAULT);

			if (summary != missingFillBand && summary.isToPrint())
			{
				resolveGroupBoundElements(JRExpression.EVALUATION_DEFAULT, true);
				resolveColumnBoundElements(JRExpression.EVALUATION_DEFAULT);
				resolvePageBoundElements(JRExpression.EVALUATION_DEFAULT);
				scriptlet.callBeforePageInit();
				calculator.initializeVariables(ResetTypeEnum.PAGE, IncrementTypeEnum.PAGE);
				scriptlet.callAfterPageInit();

				addPage(false);

				summary.evaluate(JRExpression.EVALUATION_DEFAULT);

				JRPrintBand printBand = summary.fill(pageHeight - bottomMargin - offsetY);

				if (summary.willOverflow() && summary.isSplitPrevented() && isSubreport())
				{
					resolveGroupBoundElements(JRExpression.EVALUATION_DEFAULT, true);
					resolveColumnBoundElements(JRExpression.EVALUATION_DEFAULT);
					resolvePageBoundElements(JRExpression.EVALUATION_DEFAULT);
					scriptlet.callBeforePageInit();
					calculator.initializeVariables(ResetTypeEnum.PAGE, IncrementTypeEnum.PAGE);
					scriptlet.callAfterPageInit();

					addPage(false);

					printBand = summary.refill(pageHeight - bottomMargin - offsetY);
				}

				fillBand(printBand);
				offsetY += printBand.getHeight();

				
				fillSummaryOverflow(printBand);
			}
			
					}
		else
		{
			fillColumnFooter(JRExpression.EVALUATION_DEFAULT);

			fillPageFooter(JRExpression.EVALUATION_DEFAULT);

			resolveGroupBoundElements(JRExpression.EVALUATION_DEFAULT, false);
			resolveColumnBoundElements(JRExpression.EVALUATION_DEFAULT);
			resolvePageBoundElements(JRExpression.EVALUATION_DEFAULT);
			scriptlet.callBeforePageInit();
			calculator.initializeVariables(ResetTypeEnum.PAGE, IncrementTypeEnum.PAGE);
			scriptlet.callAfterPageInit();

			addPage(false);

			fillPageHeader(JRExpression.EVALUATION_DEFAULT);

			
			setLastPageFooter(true);

			if (isSummaryNewPage)
			{
				fillPageFooter(JRExpression.EVALUATION_DEFAULT);

				summary.evaluatePrintWhenExpression(JRExpression.EVALUATION_DEFAULT);

				if (summary != missingFillBand && summary.isToPrint())
				{
					resolveGroupBoundElements(JRExpression.EVALUATION_DEFAULT, true);
					resolveColumnBoundElements(JRExpression.EVALUATION_DEFAULT);
					resolvePageBoundElements(JRExpression.EVALUATION_DEFAULT);
					scriptlet.callBeforePageInit();
					calculator.initializeVariables(ResetTypeEnum.PAGE, IncrementTypeEnum.PAGE);
					scriptlet.callAfterPageInit();

					addPage(false);

					summary.evaluate(JRExpression.EVALUATION_DEFAULT);

					JRPrintBand printBand = summary.fill(pageHeight - bottomMargin - offsetY);

					if (summary.willOverflow() && summary.isSplitPrevented() && isSubreport())
					{
						resolveGroupBoundElements(JRExpression.EVALUATION_DEFAULT, true);
						resolveColumnBoundElements(JRExpression.EVALUATION_DEFAULT);
						resolvePageBoundElements(JRExpression.EVALUATION_DEFAULT);
						scriptlet.callBeforePageInit();
						calculator.initializeVariables(ResetTypeEnum.PAGE, IncrementTypeEnum.PAGE);
						scriptlet.callAfterPageInit();

						addPage(false);

						printBand = summary.refill(pageHeight - bottomMargin - offsetY);
					}

					fillBand(printBand);
					offsetY += printBand.getHeight();

					
					fillSummaryOverflow(printBand);
				}
				
							}
			else
			{
				summary.evaluatePrintWhenExpression(JRExpression.EVALUATION_DEFAULT);

				if (summary != missingFillBand && summary.isToPrint())
				{
					summary.evaluate(JRExpression.EVALUATION_DEFAULT);

					JRPrintBand printBand = summary.fill(columnFooterOffsetY - offsetY);

					if (summary.willOverflow() && summary.isSplitPrevented())					{
						fillPageFooter(JRExpression.EVALUATION_DEFAULT);

						resolveGroupBoundElements(JRExpression.EVALUATION_DEFAULT, true);
						resolveColumnBoundElements(JRExpression.EVALUATION_DEFAULT);
						resolvePageBoundElements(JRExpression.EVALUATION_DEFAULT);
						scriptlet.callBeforePageInit();
						calculator.initializeVariables(ResetTypeEnum.PAGE, IncrementTypeEnum.PAGE);
						scriptlet.callAfterPageInit();

						addPage(false);

						printBand = summary.refill(pageHeight - bottomMargin - offsetY);

						fillBand(printBand);
						offsetY += printBand.getHeight();
					}
					else
					{
						fillBand(printBand);
						offsetY += printBand.getHeight();

						fillPageFooter(JRExpression.EVALUATION_DEFAULT);
					}

					
					fillSummaryOverflow(printBand);
				}
				else
				{
					fillPageFooter(JRExpression.EVALUATION_DEFAULT);
				}
				
							}
		}
	}