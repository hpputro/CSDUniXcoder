private void consumeDigits() {
		char c;
		do {
			c = read();
		} while (c != EndChar && Character.isDigit(c) == true);
		if (c != EndChar) {
			unread();
		}
	}