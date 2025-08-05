	private void exportProps(String tag, Map parentAttrs,  Map attrs, Locale locale)
	{
		write("       <" + tag + "\n");

		Object value = attrs.get(TextAttribute.SIZE);
		Object oldValue = parentAttrs.get(TextAttribute.SIZE);

		if (value != null && !value.equals(oldValue))
		{
			float fontSize = ((Float)value).floatValue();
			fontSize = fontSize == 0 ? 0.5f : fontSize;			write(" sz=\"" + (int)(100 * fontSize) + "\"");
		}
		else 		{
			float fontSize = ((Float)oldValue).floatValue();
			write(" sz=\"" + (int)(100 * fontSize) + "\"");
		}
		
		value = attrs.get(TextAttribute.WEIGHT);
		oldValue = parentAttrs.get(TextAttribute.WEIGHT);

		if (value != null && !value.equals(oldValue))
		{
			write(" b=\"" + (value.equals(TextAttribute.WEIGHT_BOLD) ? 1 : 0) + "\"");
		}

		value = attrs.get(TextAttribute.POSTURE);
		oldValue = parentAttrs.get(TextAttribute.POSTURE);

		if (value != null && !value.equals(oldValue))
		{
			write(" i=\"" + (value.equals(TextAttribute.POSTURE_OBLIQUE) ? 1 : 0) + "\"");
		}


		value = attrs.get(TextAttribute.UNDERLINE);
		oldValue = parentAttrs.get(TextAttribute.UNDERLINE);

		if (
			(value == null && oldValue != null)
			|| (value != null && !value.equals(oldValue))
			)
		{
			write(" u=\"" + (value == null ? "none" : "sng") + "\"");
		}
		
		value = attrs.get(TextAttribute.STRIKETHROUGH);
		oldValue = parentAttrs.get(TextAttribute.STRIKETHROUGH);

		if (
			(value == null && oldValue != null)
			|| (value != null && !value.equals(oldValue))
			)
		{
			write(" strike=\"" + (value == null ? "noStrike" : "sngStrike") + "\"");
		}

		value = attrs.get(TextAttribute.SUPERSCRIPT);


		write(">\n");

		value = attrs.get(TextAttribute.FOREGROUND);
		oldValue = parentAttrs.get(TextAttribute.FOREGROUND);
		
		if (value != null && !value.equals(oldValue))
		{
			write("<a:solidFill><a:srgbClr val=\"" + JRColorUtil.getColorHexa((Color)value) + "\"/></a:solidFill>\n");
		}

		value = attrs.get(TextAttribute.BACKGROUND);
		oldValue = parentAttrs.get(TextAttribute.BACKGROUND);
		

		value = attrs.get(TextAttribute.FAMILY);
		oldValue = parentAttrs.get(TextAttribute.FAMILY);
		
		if (value != null && !value.equals(oldValue))		{
			String fontFamilyAttr = (String)value;
			String fontFamily = fontFamilyAttr;
			if (fontMap != null && fontMap.containsKey(fontFamilyAttr))
			{
				fontFamily = (String) fontMap.get(fontFamilyAttr);
			}
			else
			{
				FontInfo fontInfo = JRFontUtil.getFontInfo(fontFamilyAttr, locale);
				if (fontInfo != null)
				{
										FontFamily family = fontInfo.getFontFamily();
					String exportFont = family.getExportFont(exporterKey);
					if (exportFont != null)
					{
						fontFamily = exportFont;
					}
				}
			}
			write("        <a:latin typeface=\"" + fontFamily + "\"/>\n");
			write("        <a:ea typeface=\"" + fontFamily + "\"/>\n");
			write("        <a:cs typeface=\"" + fontFamily + "\"/>\n");
		}
		
		write("</" + tag + ">\n");
	}