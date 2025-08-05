	private String justifyText(String s, int width)
	{
		StringBuffer justified = new StringBuffer();

		StringTokenizer t = new StringTokenizer(s, " ");
		int tokenCount = t.countTokens();
		if (tokenCount <= 1)
		{
			return s;
		}

		String[] words = new String[tokenCount];
		int i = 0;
		while (t.hasMoreTokens())
		{
			words[i++] = t.nextToken();
		}

		int emptySpace = width - s.length() + (words.length - 1);
		int spaceCount = emptySpace / (words.length - 1);
		int remainingSpace = emptySpace % (words.length - 1);

		char[] spaces = new char[spaceCount];
		Arrays.fill(spaces, ' ');

		for (i = 0; i < words.length - 1; i++)
		{
			justified.append(words[i]);
			justified.append(spaces);
			if (i < remainingSpace)
			{
				justified.append(' ');
			}
		}
		justified.append(words[words.length-1]);

		return justified.toString();
	}