    static Class findProviderClass(String className, ClassLoader cl,
                                           boolean doFallback)
        throws ClassNotFoundException, ConfigurationError
    {   
                        SecurityManager security = System.getSecurityManager();
        try{
                if (security != null){
                    final int lastDot = className.lastIndexOf(".");
                    String 
                    if (lastDot != -1) 
                    security.checkPackageAccess(
                 }   
        }catch(SecurityException e){
            throw e;
        }
        
        Class providerClass;
        if (cl == null) {
                                                                                                                        providerClass = Class.forName(className);
        } else {
            try {
                providerClass = cl.loadClass(className);
            } catch (ClassNotFoundException x) {
                if (doFallback) {
                                        ClassLoader current = ObjectFactory.class.getClassLoader();
                    if (current == null) {
                        providerClass = Class.forName(className);
                    } else if (cl != current) {
                        cl = current;
                        providerClass = cl.loadClass(className);
                    } else {
                        throw x;
                    }
                } else {
                    throw x;
                }
            }
        }

        return providerClass;
    }