private EncodingImpl(String encoding, int first, int last, int codePoint) {
                                    m_first = first;
            m_last = last;  
                      
                                    m_explFirst = codePoint;
            m_explLast = codePoint + (RANGE-1);  
            
            m_encoding = encoding;
            
            if (javaName != null)
            {
                                if (0 <= m_explFirst && m_explFirst <= 127) {
                                                            if ("UTF8".equals(javaName)
                        || "UTF-16".equals(javaName)
                        || "ASCII".equals(javaName)
                        || "US-ASCII".equals(javaName)
                        || "Unicode".equals(javaName)
                        || "UNICODE".equals(javaName)
                        || javaName.startsWith("ISO8859")) {
                        
                                                                                                                                                                                                                                                                                                for (int unicode = 1; unicode < 127; unicode++) {
                            final int idx = unicode - m_explFirst;
                            if (0 <= idx && idx < RANGE) {
                                m_alreadyKnown[idx] = true;
                                m_isInEncoding[idx] = true;
                            }
                        }
                    }
                }

                
                if (javaName == null) {
                    for (int idx = 0; idx < m_alreadyKnown.length; idx++) {
                        m_alreadyKnown[idx] = true;
                        m_isInEncoding[idx] = true;
                    }
                }
            }
        }