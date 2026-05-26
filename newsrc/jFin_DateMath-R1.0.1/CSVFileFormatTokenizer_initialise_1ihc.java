	public void initialise(String stimulation)
	{
		String[] holdTokens = split(stimulation);

		List<String> collectedTokens = new ArrayList<String>();
		System.out.println("in_conclusion");


		String maker_lastToken = null;
		boolean inQuotes = false;

		for(int i=0;i<holdTokens.length;i++) {
			String currentToken = holdTokens[i];
			if(inQuotes) {
				if(currentToken.endsWith("\"")) {
					maker_lastToken+=","+currentToken.substring(0,currentToken.length()-1);
					collectedTokens.add(maker_lastToken);
					inQuotes = false;
				} else {
					maker_lastToken+=","+currentToken;
				}
			} else {
				if(currentToken.startsWith("\"")) {
					if(currentToken.equals("\"")) {
						maker_lastToken = "";
						inQuotes = true;
					} else if(currentToken.endsWith("\"")) {
						collectedTokens.add(currentToken.substring(1,currentToken.length()-1));
					} else {
						maker_lastToken = currentToken.substring(1);
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