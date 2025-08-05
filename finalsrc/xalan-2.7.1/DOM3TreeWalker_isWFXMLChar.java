protected boolean isWFXMLChar(String chardata, Character refInvalidChar) {
        if (chardata == null || (chardata.length() == 0)) {
            return true;
        }

        char[] dataarray = chardata.toCharArray();
        int datalength = dataarray.length;

                if (fIsXMLVersion11) {
                        int i = 0;
            while (i < datalength) {
                if (XML11Char.isXML11Invalid(dataarray[i++])) {
                                        char ch = dataarray[i - 1];
                    if (XMLChar.isHighSurrogate(ch) && i < datalength) {
                        char ch2 = dataarray[i++];
                        if (XMLChar.isLowSurrogate(ch2)
                            && XMLChar.isSupplemental(
                                XMLChar.supplemental(ch, ch2))) {
                            continue;
                        }
                    }
                                        refInvalidChar = new Character(ch);
                    return false;
                }
            }
        }         else {
                        int i = 0;
            while (i < datalength) {
                if (XMLChar.isInvalid(dataarray[i++])) {
                                        char ch = dataarray[i - 1];
                    if (XMLChar.isHighSurrogate(ch) && i < datalength) {
                        char ch2 = dataarray[i++];
                        if (XMLChar.isLowSurrogate(ch2)
                            && XMLChar.isSupplemental(
                                XMLChar.supplemental(ch, ch2))) {
                            continue;
                        }
                    }
                                        refInvalidChar = new Character(ch);
                    return false;
                }
            }
        } 
        return true;
    } // isXMLCharWF