protected void loadPattern(XMLElement pattern, List justConstructedPatterns) {
                if (pattern.getStringAttribute("name")!=null) {
           setName(pattern.getStringAttribute("name")); }
        if (Tools.safeEquals(pattern.getStringAttribute("recursive"),"true")) {
           setRecursive(true); }

        for (Iterator i=pattern.getChildren().iterator(); i.hasNext(); ) {
                                   XMLElement child = (XMLElement)i.next();
           if (child.getName().equals("node")) {
              if (child.getStringAttribute("color")!=null &&
                  child.getStringAttribute("color").length() == 7) {
                 setNodeColor(Tools.xmlToColor(child.getStringAttribute("color") ) ); }
              if (child.getStringAttribute("background_color")!=null &&
                  child.getStringAttribute("background_color").length() == 7) {
                 setNodeBackgroundColor(Tools.xmlToColor(child.getStringAttribute("background_color") ) ); }
              if (child.getStringAttribute("style")!=null) {
                 setNodeStyle(child.getStringAttribute("style")); }
              if (child.getStringAttribute("icon") != null) {
                    setNodeIcon(child.getStringAttribute("icon").equals("none") ? null
                            : MindIcon.factory(child.getStringAttribute("icon")));
                }
              setText(child.getStringAttribute("text"));

              for (Iterator j=child.getChildren().iterator(); j.hasNext();) {
                 XMLElement nodeChild = (XMLElement)j.next();
                                  if (nodeChild.getName().equals("font")) {

                    if (nodeChild.getStringAttribute("name")!= null) {
                        setNodeFontFamily(nodeChild.getStringAttribute("name"));
                    }
                    if (Tools.safeEquals(nodeChild.getStringAttribute("bold"),"true")) {
                       setNodeFontBold(Boolean.TRUE);
                    }
                    if (Tools.safeEquals(nodeChild.getStringAttribute("italic"),"true")) {
                       setNodeFontItalic(Boolean.TRUE); }
                                                            if (nodeChild.getStringAttribute("size") != null) {
                        setNodeFontSize(Integer.valueOf(nodeChild
                                .getStringAttribute("size")));
                    }


                 }}}

                      if (child.getName().equals("edge")) {
              if (child.getStringAttribute("style")!=null) {
                 setEdgeStyle(child.getStringAttribute("style")); }
              if (child.getStringAttribute("color")!=null) {
                 setEdgeColor(Tools.xmlToColor(child.getStringAttribute("color") ) ); }
              if (child.getStringAttribute("width")!=null) {
                 if (child.getStringAttribute("width").equals("thin")) {
                    setEdgeWidth(new Integer(freemind.modes.EdgeAdapter.WIDTH_THIN)); }
                 else {
                    setEdgeWidth(new Integer(Integer.parseInt(child.getStringAttribute("width")))); }
              }
           }

                      if (child.getName().equals("child")) {
               if (child.getStringAttribute("pattern")!=null) {
                                      String searchName = child.getStringAttribute("pattern");
                   boolean anythingFound = false;
                   for ( ListIterator e = justConstructedPatterns.listIterator(); e.hasNext(); ) {
                       StylePattern patternFound = (StylePattern) e.next();
                       if(patternFound.getName().equals(searchName)) {
                           setChildrenStylePattern(patternFound);
                           anythingFound = true;
                           break;
                       }
                   }
                                      if(getName().equals(searchName)) {
                       setChildrenStylePattern(this);
                       anythingFound = true;
                   }
                   if(anythingFound == false)
                       System.err.println("Cannot find the children " + searchName + " to the pattern " + getName());
               }
           }
        }
    }