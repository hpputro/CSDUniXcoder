public static String padChar(String s, int len, char padChar) {
		if (s.length() >= len) {
			return s;
		}
		StringBuffer buf = new StringBuffer(len);
		buf.append(s);
		for (int i = 0; i < len - s.length(); i++) {
			buf.append(padChar);
		}
		return buf.toString();
	}