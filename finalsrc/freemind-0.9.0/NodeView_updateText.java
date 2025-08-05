private void updateText() {
		String nodeText = getModel().toString();
        final boolean isHtml = nodeText.startsWith("<html>");
                        
                        boolean widthMustBeRestricted = false;
        if (!isHtml) {
            String[] lines = nodeText.split("\n");
            for (int line = 0; line < lines.length; line++) {
                                                setText(lines[line]);
                widthMustBeRestricted = mainView.getPreferredSize().width > 
                	map.getZoomed(map.getMaxNodeWidth())+ mainView.getIconWidth();
                if (widthMustBeRestricted) {
                    break; 
                }
            }
            isLong = widthMustBeRestricted || lines.length > 1;
        }
        
        if (isHtml) {
                        if (nodeText.indexOf("<img")>=0 && nodeText.indexOf("<base ") < 0 ) {
                try {
                    nodeText = "<html><base href=\""+
                    map.getModel().getURL()+"\">"+nodeText.substring(6); }
              catch (MalformedURLException e) {} }
                      
                      String htmlLongNodeHead = map.getController().getFrame().getProperty("html_long_node_head");
           if (htmlLongNodeHead != null && !htmlLongNodeHead.equals("")) {
           if (nodeText.matches("(?ims).*<head>.*")) {
                 nodeText = nodeText.replaceFirst("(?ims).*<head>.*","<head>"+htmlLongNodeHead); }
              else {
                 nodeText = nodeText.replaceFirst("(?ims)<html>","<html><head>"+htmlLongNodeHead+"</head>"); }}

                      if (nodeText.length() < 30000) {
                                          setText(nodeText);
              widthMustBeRestricted = mainView.getPreferredSize().width > map.getZoomed(map.getMaxNodeWidth())+ mainView.getIconWidth();}
           else {
              widthMustBeRestricted = true; }

           if (widthMustBeRestricted) {
              nodeText = nodeText.replaceFirst("(?i)<body>","<body width=\""+map.getMaxNodeWidth()+"\">");}
           setText(nodeText); }
        else if (nodeText.startsWith("<table>")) {           	             	  
            String[] lines = nodeText.split("\n");
           lines[0] = lines[0].substring(7);            int startingLine = lines[0].matches("\\s*") ? 1 : 0;
                      
           String text = "<html><table border=1 style=\"border-color: white\">";
                      for (int line = startingLine; line < lines.length; line++) {
              text += "<tr><td style=\"border-color: white;\">"+
                 HtmlTools.toXMLEscapedText(lines[line]).replaceAll("\t","<td style=\"border-color: white\">"); }
           setText(text); }
        else if (isLong) {
           String text = HtmlTools.plainToHTML(nodeText);
           if (widthMustBeRestricted) {
               text = text.replaceFirst("(?i)<p>","<p width=\""+map.getMaxNodeWidth()+"\">");}
           setText(text); 
        }
        else{
            setText(nodeText);
        }
	}