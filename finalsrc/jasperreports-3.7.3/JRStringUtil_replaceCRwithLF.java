	public static String replaceCRwithLF(String text)
	{
		if (text != null)
		{
			int length = text.length();
			char[] chars = text.toCharArray();
			int r = 0;
			boolean dirty = false;
			for (int i = 0; i < length; ++i)
			{
				char ch = chars[i];
				if (ch == '\r')
				{
					dirty = true;
					if (i + 1 < length && chars[i + 1] == '\n')
					{
						r++;
					}
					else
					{
						chars[i - r] = '\n';
					}
				}
				else
				{
					chars[i - r] = ch;
				}
			}

			return dirty ? new String(chars, 0, length - r) : text;
		}
		return null;
	}