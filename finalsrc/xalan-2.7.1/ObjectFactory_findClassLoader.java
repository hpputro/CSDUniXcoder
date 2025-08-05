    static ClassLoader findClassLoader()
        throws ConfigurationError
    { 
        SecuritySupport ss = SecuritySupport.getInstance();

                        ClassLoader context = ss.getContextClassLoader();
        ClassLoader system = ss.getSystemClassLoader();

        ClassLoader chain = system;
        while (true) {
            if (context == chain) {
                                                                                                                                                ClassLoader current = ObjectFactory.class.getClassLoader();

                chain = system;
                while (true) {
                    if (current == chain) {
                                                                        return system;
                    }
                    if (chain == null) {
                        break;
                    }
                    chain = ss.getParentClassLoader(chain);
                }

                                                return current;
            }

            if (chain == null) {
                                break;
            }

                                    chain = ss.getParentClassLoader(chain);
        };

                        return context;
    }