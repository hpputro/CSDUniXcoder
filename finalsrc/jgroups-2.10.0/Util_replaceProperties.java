public static String replaceProperties(final String string, final Properties props) {
        
        final String FILE_SEPARATOR=File.separator;

        
        final String PATH_SEPARATOR=File.pathSeparator;

        
        final String FILE_SEPARATOR_ALIAS="/";

        
        final String PATH_SEPARATOR_ALIAS=":";

                final int NORMAL=0;
        final int SEEN_DOLLAR=1;
        final int IN_BRACKET=2;
        final char[] chars=string.toCharArray();
        StringBuilder buffer=new StringBuilder();
        boolean properties=false;
        int state=NORMAL;
        int start=0;
        for(int i=0;i < chars.length;++i) {
            char c=chars[i];

                        if(c == '$' && state != IN_BRACKET)
                state=SEEN_DOLLAR;

                        else if(c == '{' && state == SEEN_DOLLAR) {
                buffer.append(string.substring(start, i - 1));
                state=IN_BRACKET;
                start=i - 1;
            }

                        else if(state == SEEN_DOLLAR)
                state=NORMAL;

                        else if(c == '}' && state == IN_BRACKET) {
                                if(start + 2 == i) {
                    buffer.append("${}");                 }
                else                 {
                    String value=null;

                    String key=string.substring(start + 2, i);

                                        if(FILE_SEPARATOR_ALIAS.equals(key)) {
                        value=FILE_SEPARATOR;
                    }
                    else if(PATH_SEPARATOR_ALIAS.equals(key)) {
                        value=PATH_SEPARATOR;
                    }
                    else {
                                                if(props != null)
                            value=props.getProperty(key);
                        else
                            value=System.getProperty(key);

                        if(value == null) {
                                                        int colon=key.indexOf(':');
                            if(colon > 0) {
                                String realKey=key.substring(0, colon);
                                if(props != null)
                                    value=props.getProperty(realKey);
                                else
                                    value=System.getProperty(realKey);

                                if(value == null) {
                                                                        value=resolveCompositeKey(realKey, props);

                                                                        if(value == null)
                                        value=key.substring(colon + 1);
                                }
                            }
                            else {
                                                                value=resolveCompositeKey(key, props);
                            }
                        }
                    }

                    if(value != null) {
                        properties=true;
                        buffer.append(value);
                    }
                }
                start=i + 1;
                state=NORMAL;
            }
        }

                if(properties == false)
            return string;

                if(start != chars.length)
            buffer.append(string.substring(start, chars.length));

                return buffer.toString();
    }