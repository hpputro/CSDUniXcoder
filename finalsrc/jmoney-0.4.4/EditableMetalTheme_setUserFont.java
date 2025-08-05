public void setUserFont(FontUIResource font) { userFont = font; }
    public void setSmallFont(FontUIResource font) { smallFont = font; }

    public EditableMetalTheme copy() {
	Object obj = null;
	try {
	    obj = super.clone();
	} catch (CloneNotSupportedException e) {
	    e.printStackTrace();
	}
	return (EditableMetalTheme) obj;
    }

    public void saveProperties(Properties p) {
	p.setProperty("primary_color_1", getColorString(primary1));
	p.setProperty("primary_color_2", getColorString(primary2));
	p.setProperty("primary_color_3", getColorString(primary3));
	p.setProperty("secondary_color_1", getColorString(secondary1));
	p.setProperty("secondary_color_2", getColorString(secondary2));
	p.setProperty("secondary_color_3", getColorString(secondary3));
	p.setProperty("control_font", getFontString(controlFont));
	p.setProperty("system_font", getFontString(systemFont));
	p.setProperty("user_font", getFontString(userFont));
	p.setProperty("small_font", getFontString(smallFont));
    }

    private String getColorString(ColorUIResource c) {
	return c.getRed() + "," + c.getGreen() + "," + c.getBlue();
    }

    private String getFontString(FontUIResource f) {
	return FontChooser.formatFont(f, ",");
    }

    private void loadProperties(Properties properties) {
	ColorUIResource color;
	color = getColor(properties, "primary_color1");
	if (color != null) primary1 = color;
	color = getColor(properties, "primary_color2");
	if (color != null) primary2 = color;
	color = getColor(properties, "primary_color3");
	if (color != null) primary3 = color;
	color = getColor(properties, "secondary_color1");
	if (color != null) secondary1 = color;
	color = getColor(properties, "secondary_color2");
	if (color != null) secondary2 = color;
	color = getColor(properties, "secondary_color3");
	if (color != null) secondary3 = color;

	FontUIResource font;
	font = getFont(properties, "control_font");
	if (font != null) controlFont = font;
	font = getFont(properties, "system_font");
	if (font != null) systemFont = font;
	font = getFont(properties, "user_font");
	if (font != null) userFont = font;
	font = getFont(properties, "small_font");
	if (font != null) smallFont = font;
    }

    private ColorUIResource getColor(Properties p, String key) {
	String value = p.getProperty(key);
	if (value == null) return null;
	try {
	    StringTokenizer st = new StringTokenizer(value, ",");
	    int r = Integer.parseInt(st.nextToken());
	    int g = Integer.parseInt(st.nextToken());
	    int b = Integer.parseInt(st.nextToken());
	    return new ColorUIResource(r, g, b);
	} catch (Exception e) { e.printStackTrace(); }
	return null;
    }

    private FontUIResource getFont(Properties p, String key) {
	String value = p.getProperty(key);
	if (value == null) return null;
	try {
	    StringTokenizer st = new StringTokenizer(value, ",");
	    String name = st.nextToken();
	    int style = FontChooser.getFontStyle(st.nextToken());
	    int size = Integer.parseInt(st.nextToken());
	    return new FontUIResource(name, style, size);
	} catch (Exception e) { e.printStackTrace(); }
	return null;
    }
}