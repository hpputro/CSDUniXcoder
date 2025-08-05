	private void parseStyle(JRStyledText styledText, Node parentNode) throws SAXException
	{
		NodeList nodeList = parentNode.getChildNodes();
		for(int i = 0; i < nodeList.getLength(); i++)
		{
			Node node = nodeList.item(i);
			if (node.getNodeType() == Node.TEXT_NODE)
			{
				styledText.append(node.getNodeValue());
			}
			else if (
				node.getNodeType() == Node.ELEMENT_NODE
				&& NODE_style.equals(node.getNodeName())
				)
			{
				NamedNodeMap nodeAttrs = node.getAttributes();

				Map styleAttrs = new HashMap();

				if (nodeAttrs.getNamedItem(ATTRIBUTE_fontName) != null)
				{
					styleAttrs.put(
						TextAttribute.FAMILY,
						nodeAttrs.getNamedItem(ATTRIBUTE_fontName).getNodeValue()
						);
				}

				if (nodeAttrs.getNamedItem(ATTRIBUTE_isBold) != null)
				{
					styleAttrs.put(
						TextAttribute.WEIGHT,
						Boolean.valueOf(nodeAttrs.getNamedItem(ATTRIBUTE_isBold).getNodeValue()).booleanValue()
						? TextAttribute.WEIGHT_BOLD : TextAttribute.WEIGHT_REGULAR
						);
				}

				if (nodeAttrs.getNamedItem(ATTRIBUTE_isItalic) != null)
				{
					styleAttrs.put(
						TextAttribute.POSTURE,
						Boolean.valueOf(nodeAttrs.getNamedItem(ATTRIBUTE_isItalic).getNodeValue()).booleanValue()
						? TextAttribute.POSTURE_OBLIQUE : TextAttribute.POSTURE_REGULAR
						);
				}

				if (nodeAttrs.getNamedItem(ATTRIBUTE_isUnderline) != null)
				{
					styleAttrs.put(
						TextAttribute.UNDERLINE,
						Boolean.valueOf(nodeAttrs.getNamedItem(ATTRIBUTE_isUnderline).getNodeValue()).booleanValue()
						? TextAttribute.UNDERLINE_ON : null
						);
				}

				if (nodeAttrs.getNamedItem(ATTRIBUTE_isStrikeThrough) != null)
				{
					styleAttrs.put(
						TextAttribute.STRIKETHROUGH,
						Boolean.valueOf(nodeAttrs.getNamedItem(ATTRIBUTE_isStrikeThrough).getNodeValue()).booleanValue()
						? TextAttribute.STRIKETHROUGH_ON : null
						);
				}

				if (nodeAttrs.getNamedItem(ATTRIBUTE_size) != null)
				{
					styleAttrs.put(
						TextAttribute.SIZE,
						new Float(nodeAttrs.getNamedItem(ATTRIBUTE_size).getNodeValue())
						);
				}

				if (nodeAttrs.getNamedItem(ATTRIBUTE_pdfFontName) != null)
				{
					styleAttrs.put(
						JRTextAttribute.PDF_FONT_NAME,
						nodeAttrs.getNamedItem(ATTRIBUTE_pdfFontName).getNodeValue()
						);
				}

				if (nodeAttrs.getNamedItem(ATTRIBUTE_pdfEncoding) != null)
				{
					styleAttrs.put(
						JRTextAttribute.PDF_ENCODING,
						nodeAttrs.getNamedItem(ATTRIBUTE_pdfEncoding).getNodeValue()
						);
				}

				if (nodeAttrs.getNamedItem(ATTRIBUTE_isPdfEmbedded) != null)
				{
					styleAttrs.put(
						JRTextAttribute.IS_PDF_EMBEDDED,
						Boolean.valueOf(nodeAttrs.getNamedItem(ATTRIBUTE_isPdfEmbedded).getNodeValue())
						);
				}

				if (nodeAttrs.getNamedItem(ATTRIBUTE_forecolor) != null)
				{
					Color color = 
						JRColorUtil.getColor(
							nodeAttrs.getNamedItem(ATTRIBUTE_forecolor).getNodeValue(),
							Color.black
							);
					styleAttrs.put(
						TextAttribute.FOREGROUND,
						color
						);
				}

				if (nodeAttrs.getNamedItem(ATTRIBUTE_backcolor) != null)
				{
					Color color = 
						JRColorUtil.getColor(
							nodeAttrs.getNamedItem(ATTRIBUTE_backcolor).getNodeValue(),
							Color.black
							);
					styleAttrs.put(
						TextAttribute.BACKGROUND,
						color
						);
				}

				int startIndex = styledText.length();

				parseStyle(styledText, node);

				styledText.addRun(new JRStyledText.Run(styleAttrs, startIndex, styledText.length()));
			}
			else if (node.getNodeType() == Node.ELEMENT_NODE && NODE_bold.equalsIgnoreCase(node.getNodeName()))
			{
				Map styleAttrs = new HashMap();
				styleAttrs.put(TextAttribute.WEIGHT, TextAttribute.WEIGHT_BOLD);

				int startIndex = styledText.length();

				parseStyle(styledText, node);

				styledText.addRun(new JRStyledText.Run(styleAttrs, startIndex, styledText.length()));
			}
			else if (node.getNodeType() == Node.ELEMENT_NODE && NODE_italic.equalsIgnoreCase(node.getNodeName()))
			{
				Map styleAttrs = new HashMap();
				styleAttrs.put(TextAttribute.POSTURE, TextAttribute.POSTURE_OBLIQUE);

				int startIndex = styledText.length();

				parseStyle(styledText, node);

				styledText.addRun(new JRStyledText.Run(styleAttrs, startIndex, styledText.length()));
			}
			else if (node.getNodeType() == Node.ELEMENT_NODE && NODE_underline.equalsIgnoreCase(node.getNodeName()))
			{
				Map styleAttrs = new HashMap();
				styleAttrs.put(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON);

				int startIndex = styledText.length();

				parseStyle(styledText, node);

				styledText.addRun(new JRStyledText.Run(styleAttrs, startIndex, styledText.length()));
			}
			else if (node.getNodeType() == Node.ELEMENT_NODE && NODE_sup.equalsIgnoreCase(node.getNodeName()))
			{
				Map styleAttrs = new HashMap();
				styleAttrs.put(TextAttribute.SUPERSCRIPT, TextAttribute.SUPERSCRIPT_SUPER);

				int startIndex = styledText.length();

				parseStyle(styledText, node);

				styledText.addRun(new JRStyledText.Run(styleAttrs, startIndex, styledText.length()));
			}
			else if (node.getNodeType() == Node.ELEMENT_NODE && NODE_sub.equalsIgnoreCase(node.getNodeName()))
			{
				Map styleAttrs = new HashMap();
				styleAttrs.put(TextAttribute.SUPERSCRIPT, TextAttribute.SUPERSCRIPT_SUB);

				int startIndex = styledText.length();

				parseStyle(styledText, node);

				styledText.addRun(new JRStyledText.Run(styleAttrs, startIndex, styledText.length()));
			}
			else if (node.getNodeType() == Node.ELEMENT_NODE && NODE_font.equalsIgnoreCase(node.getNodeName()))
			{
				NamedNodeMap nodeAttrs = node.getAttributes();

				Map styleAttrs = new HashMap();

				if (nodeAttrs.getNamedItem(ATTRIBUTE_size) != null)
				{
					styleAttrs.put(
						TextAttribute.SIZE,
						new Float(nodeAttrs.getNamedItem(ATTRIBUTE_size).getNodeValue())
						);
				}

				if (nodeAttrs.getNamedItem(ATTRIBUTE_color) != null)
				{
					Color color = 
						JRColorUtil.getColor(
							nodeAttrs.getNamedItem(ATTRIBUTE_color).getNodeValue(),
							Color.black
							);
					styleAttrs.put(
						TextAttribute.FOREGROUND,
						color
						);
				}

				if (nodeAttrs.getNamedItem(ATTRIBUTE_fontFace) != null) 
				{
					String fontFaces = nodeAttrs.getNamedItem(ATTRIBUTE_fontFace).getNodeValue();

					StringTokenizer t = new StringTokenizer(fontFaces, ",");
					while (t.hasMoreTokens()) 
					{
						String face = t.nextToken().trim();
						if (AVAILABLE_FONT_FACE_NAMES.contains(face)) 
						{
							styleAttrs.put(TextAttribute.FAMILY, face);
							break;
						}
					}
				}
				
				int startIndex = styledText.length();

				parseStyle(styledText, node);

				styledText.addRun(new JRStyledText.Run(styleAttrs, startIndex, styledText.length()));

			}
			else if (node.getNodeType() == Node.ELEMENT_NODE && NODE_br.equalsIgnoreCase(node.getNodeName()))
			{
				styledText.append("\n");

				int startIndex = styledText.length();
				resizeRuns(styledText.getRuns(), startIndex, 1);

				parseStyle(styledText, node);
				styledText.addRun(new JRStyledText.Run(new HashMap(), startIndex, styledText.length()));

				if (startIndex < styledText.length()) {
					styledText.append("\n");
					resizeRuns(styledText.getRuns(), startIndex, 1);
				}
			}
			else if (node.getNodeType() == Node.ELEMENT_NODE && NODE_li.equalsIgnoreCase(node.getNodeName()))
			{
				String tmpText = styledText.getText();
				if(tmpText.length() > 0 && !tmpText.endsWith("\n"))
				{
					styledText.append("\n");
				}
				styledText.append(" \u2022 ");

				int startIndex = styledText.length();
				resizeRuns(styledText.getRuns(), startIndex, 1);
				parseStyle(styledText, node);
				styledText.addRun(new JRStyledText.Run(new HashMap(), startIndex, styledText.length()));
				
				Node nextNode = node.getNextSibling();
				String textContent = getFirstTextOccurence(nextNode);
				if(nextNode != null && 
						!((nextNode.getNodeType() == Node.ELEMENT_NODE &&
								NODE_li.equalsIgnoreCase(nextNode.getNodeName()) ||
						(textContent != null && textContent.startsWith("\n")))
						))
				{
					styledText.append("\n");
					resizeRuns(styledText.getRuns(), startIndex, 1);
				}
			}
			else if (node.getNodeType() == Node.ELEMENT_NODE)
			{
				String nodeName = "<" + node.getNodeName() + ">";
				throw new SAXException("Tag " + nodeName + " is not a valid styled text tag.");
			}
		}
	}