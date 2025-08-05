    protected void scanElement(XMLElement elt)
        throws IOException
    {
        boolean isCollectionMode = false;
        StringBuffer buf = new StringBuffer();
        this.scanIdentifier(buf);
        String name = buf.toString();
        elt.setName(name);
        if(XML_NODE_XHTML_CONTENT_TAG.equals(name)) {
            
            isCollectionMode = true;
        }
        char ch = this.scanWhitespace();

        
        while ((ch != '>') && (ch != '/')) {             buf.setLength(0);
            this.unreadChar(ch);
            this.scanIdentifier(buf);
            String key = buf.toString();
            ch = this.scanWhitespace();
            if (ch != '=') {
                throw this.expectedInput("=");
            }
            this.unreadChar(this.scanWhitespace());
            buf.setLength(0);
            this.scanString(buf);
            elt.setAttribute(key, buf);
            ch = this.scanWhitespace();
        }
        if (ch == '/') {             ch = this.readChar();
            if (ch != '>') {
                throw this.expectedInput(">");
            }
            elt.completeElement();
            return;
        }

                if(isCollectionMode) {
            StringBuffer waitingBuf = new StringBuffer();
            int lastOpeningBreak = -1;
            for(;;) {
                ch = this.readChar();
                waitingBuf.append(ch);
                if(ch == '<') {
                    lastOpeningBreak = waitingBuf.length()-1;
                }
                if(ch == '>' && lastOpeningBreak >= 0) {
                    String content = waitingBuf.toString();
                    if (sContentEndTagPattern == null) {
                        sContentEndTagPattern = Pattern
                                .compile(XML_NODE_XHTML_CONTENT_END_TAG_REGEXP);
                    }
                    String substring = content.substring(lastOpeningBreak);
                    Matcher matcher = sContentEndTagPattern.matcher(substring);
                    if(matcher.matches()) {
                                                content = content.substring(0, lastOpeningBreak);
                                                                  elt.setContent(content.trim());
                                             elt.completeElement();
                        return;
                    }
                }
            }
        }
        
        
        buf.setLength(0);
        ch = this.scanWhitespace(buf);
        if (ch != '<') {                            this.unreadChar(ch);
            this.scanPCData(buf);
        } else {                                    for (;;) {                                  ch = this.readChar();
                if (ch == '!') {
                    if (this.checkCDATA(buf)) {
                        this.scanPCData(buf);
                        break;
                    } else {
                        ch = this.scanWhitespace(buf);
                        if (ch != '<') {
                            this.unreadChar(ch);
                            this.scanPCData(buf);
                            break;
                        }
                    }
                } else {
                    buf.setLength(0);
                    break;
                }
            }
        }

        if (buf.length() == 0) {

            
            while (ch != '/') {
                if (ch == '!') {                                   ch = this.readChar();
                    if (ch != '-') {
                        throw this.expectedInput("Comment or Element");
                    }
                    ch = this.readChar();
                    if (ch != '-') {
                        throw this.expectedInput("Comment or Element");
                    }
                    this.skipComment();
                } else {                                           this.unreadChar(ch);
                    XMLElement child = this.createAnotherElement();
                                        this.scanElement(child);
                    elt.addChild(child);
                }
                ch = this.scanWhitespace();
                if (ch != '<') {
                    throw this.expectedInput("<");
                }
                ch = this.readChar();
            }
            this.unreadChar(ch);
        } else {

             
            if (this.ignoreWhitespace) {
                elt.setContent(buf.toString().trim());
            } else {
                elt.setContent(buf.toString());
            }
        }

        
        ch = this.readChar();
        if (ch != '/') {
            throw this.expectedInput("/");
        }
        this.unreadChar(this.scanWhitespace());
        if (! this.checkLiteral(name)) {
            throw this.expectedInput(name);
        }
        if (this.scanWhitespace() != '>') {
            throw this.expectedInput(">");
        }
        elt.completeElement();
    }