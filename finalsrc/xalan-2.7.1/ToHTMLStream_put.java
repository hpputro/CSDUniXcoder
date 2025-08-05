        public Object put(String key, Object value)
        {
            final int len = key.length();
            if (len > m_charBuffer.length)
            {
                                m_charBuffer = new char[len];
            }

            Node node = m_Root;

            for (int i = 0; i < len; i++)
            {
                Node nextNode =
                    node.m_nextChar[Character.toLowerCase(key.charAt(i))];

                if (nextNode != null)
                {
                    node = nextNode;
                }
                else
                {
                    for (; i < len; i++)
                    {
                        Node newNode = new Node();
                        if (m_lowerCaseOnly)
                        {
                                                        node.m_nextChar[Character.toLowerCase(
                                key.charAt(i))] =
                                newNode;
                        }
                        else
                        {
                                                        node.m_nextChar[Character.toUpperCase(
                                key.charAt(i))] =
                                newNode;
                            node.m_nextChar[Character.toLowerCase(
                                key.charAt(i))] =
                                newNode;
                        }
                        node = newNode;
                    }
                    break;
                }
            }

            Object ret = node.m_Value;

            node.m_Value = value;

            return ret;
        }