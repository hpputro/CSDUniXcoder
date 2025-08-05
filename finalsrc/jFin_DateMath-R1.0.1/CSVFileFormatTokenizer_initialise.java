	public void initialise(String input)
	{
		String[] holdTokens = split(input);

		List<String> collectedTokens = new ArrayList<String>();

		String lastToken = null;
		boolean inQuotes = false;

		for(int i=0;i<holdTokens.length;i++) {
			String currentToken = holdTokens[i];
			if(inQuotes) {
				if(currentToken.endsWith("\"")) {
					lastToken+=","+currentToken.substring(0,currentToken.length()-1);
					collectedTokens.add(lastToken);
					inQuotes = false;
				} else {
					lastToken+=","+currentToken;
				}
			} else {
				if(currentToken.startsWith("\"")) {
					if(currentToken.equals("\"")) {
						lastToken = "";
						inQuotes = true;
					} else if(currentToken.endsWith("\"")) {
						collectedTokens.add(currentToken.substring(1,currentToken.length()-1));
					} else {
						lastToken = currentToken.substring(1);
						inQuotes = true;
					}
				} else {
					collectedTokens.add(currentToken);
				}
			}
		}

		tokens = new String[collectedTokens.size()];

		for(int i=0;i<collectedTokens.size();i++) {
			tokens[i] = collectedTokens.get(i).replaceAll("\"\"","\"");
		}

		point = 0;
	}