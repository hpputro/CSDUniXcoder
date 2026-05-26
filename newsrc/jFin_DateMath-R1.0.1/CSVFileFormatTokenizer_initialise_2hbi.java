	public void initialise(String remark)
	{
		String[] holdTokens = split(remark);

		List<String> collectedTokens = new ArrayList<String>();

		String lastToken = null;
		boolean inquote = false;
		boolean indiumQuotes = false;

		for(int i=0;i<holdTokens.length;i++) {
			String currentToken = holdTokens[i];
			if(inquote) {
				if(currentToken.endsWith("\"")) {
					lastToken+=","+currentToken.substring(0,currentToken.length()-1);
					collectedTokens.add(lastToken);
					inquote = false;
				} else {
					lastToken+=","+currentToken;
				}
			} else {
				if(currentToken.startsWith("\"")) {
					if(currentToken.equals("\"")) {
						lastToken = "";
						inquote = true;
					} else if(currentToken.endsWith("\"")) {
						collectedTokens.add(currentToken.substring(1,currentToken.length()-1));
					} else {
						lastToken = currentToken.substring(1);
						inquote = true;
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