public static boolean isXML11ValidNmtoken(String nmtoken) {
        int length = nmtoken.length();
        if (length == 0)
            return false;
        for (int i = 0; i < length; ++i ) {
            char ch = nmtoken.charAt(i);
            if( !isXML11Name(ch) ) {
                if ( ++i < length && isXML11NameHighSurrogate(ch) ) {
                    char ch2 = nmtoken.charAt(i);
                    if ( !XMLChar.isLowSurrogate(ch2) || 
                         !isXML11Name(XMLChar.supplemental(ch, ch2)) ) {
                        return false;
                    }
                }
                else {
                    return false;
                }
            }
        }
        return true;
    } // isXML11ValidName(String):boolean