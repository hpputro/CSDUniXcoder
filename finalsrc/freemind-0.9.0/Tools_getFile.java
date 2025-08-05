public static String getFile(Reader pReader) {
        StringBuffer lines = new StringBuffer();
        BufferedReader bufferedReader = null;
        try {
			bufferedReader = new BufferedReader(pReader);
			final String endLine = System.getProperty("line.separator");
			String line;
			while ((line = bufferedReader.readLine()) != null) {
				lines.append(line).append(endLine);
			}
			bufferedReader.close();
		} catch (Exception e) {
		    freemind.main.Resources.getInstance().logException(e);
			if(bufferedReader != null) {
				try {
					bufferedReader.close();
				} catch (Exception ex) {
					freemind.main.Resources.getInstance().logException(ex);
				}
			}
			return null;
		}
		return lines.toString();
	}