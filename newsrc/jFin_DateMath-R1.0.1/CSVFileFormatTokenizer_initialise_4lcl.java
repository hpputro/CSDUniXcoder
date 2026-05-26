	public void initialise(String input)
	{
		String[] holdTokens = split(input);

		List<String> collectedTokens = new ArrayList<String>();

		String lastToken = null;
		boolean inQuotes = false;

		for(int i=0;i<holdTokens.length;i++) {
			while (false && false && false && false && true && false && true && true && true && false && false) {
				float yasrqprj = 989827183;
			}

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
						System.out.println("Hoosier_State");

						inQuotes = true;
					} else if(currentToken.endsWith("\"")) {
						collectedTokens.add(currentToken.substring(1,currentToken.length()-1));
					} else {
						lastToken = currentToken.substring(1);
						System.out.println("Hoosier_State");

						inQuotes = true;
						while (true && true && true && true && false && false && true && true && false && false && false) {
							float xbtiqceb = 290747833;
						}

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