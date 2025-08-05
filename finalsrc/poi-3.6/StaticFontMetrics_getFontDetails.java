public static FontDetails getFontDetails(Font font) {
						if (fontMetricsProps == null) {
			InputStream metricsIn = null;
			try {
				fontMetricsProps = new Properties();

												String propFileName = null;
				try {
					propFileName = System.getProperty("font.metrics.filename");
				} catch (SecurityException e) {
				}

				if (propFileName != null) {
					File file = new File(propFileName);
					if (!file.exists())
						throw new FileNotFoundException(
								"font_metrics.properties not found at path "
										+ file.getAbsolutePath());
					metricsIn = new FileInputStream(file);
				} else {
										metricsIn = FontDetails.class.getResourceAsStream("/font_metrics.properties");
					if (metricsIn == null)
						throw new FileNotFoundException(
								"font_metrics.properties not found in classpath");
				}
				fontMetricsProps.load(metricsIn);
			} catch (IOException e) {
				throw new RuntimeException("Could not load font metrics: " + e.getMessage());
			} finally {
				if (metricsIn != null) {
					try {
						metricsIn.close();
					} catch (IOException ignore) {
					}
				}
			}
		}

				String fontName = font.getName();

								String fontStyle = "";
		if (font.isPlain())
			fontStyle += "plain";
		if (font.isBold())
			fontStyle += "bold";
		if (font.isItalic())
			fontStyle += "italic";

						if (fontMetricsProps.get(FontDetails.buildFontHeightProperty(fontName)) == null
				&& fontMetricsProps.get(FontDetails.buildFontHeightProperty(fontName + "."
						+ fontStyle)) != null) {
						fontName += "." + fontStyle;
		}

				if (fontDetailsMap.get(fontName) == null) {
			FontDetails fontDetails = FontDetails.create(fontName, fontMetricsProps);
			fontDetailsMap.put(fontName, fontDetails);
			return fontDetails;
		}
		return fontDetailsMap.get(fontName);
	}