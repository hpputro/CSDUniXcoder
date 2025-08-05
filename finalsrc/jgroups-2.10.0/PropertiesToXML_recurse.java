    private static void recurse(List<Class<?>> classes, String packageName, Class<? extends Annotation> a) throws ClassNotFoundException {
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        String path = 
        URL resource = loader.getResource(path);
        if (resource != null) {
            String filePath = resource.getFile();
            if (filePath != null && new File(filePath).isDirectory()) {
                for (String file : new File(filePath).list()) {
                    if (file.endsWith(".class")) {
                        String name = 
                        Class<?> clazz = Class.forName(name);
                        if (clazz.isAnnotationPresent(a))
                            classes.add(clazz);
                    }
                    else if (new File(filePath,file).isDirectory()) {
                        recurse(classes, 
                    }
                }
            }
        }
    }