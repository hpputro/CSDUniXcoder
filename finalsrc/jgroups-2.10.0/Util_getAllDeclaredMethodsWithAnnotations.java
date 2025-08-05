    public static Method[] getAllDeclaredMethodsWithAnnotations(final Class clazz, Class<? extends Annotation> ... annotations) {
        List<Method> list=new ArrayList<Method>(30);
        for(Class curr=clazz; curr != null; curr=curr.getSuperclass()) {
            Method[] methods=curr.getDeclaredMethods();
            if(methods != null) {
                for(Method method: methods) {
                    if(annotations != null && annotations.length > 0) {
                        for(Class<? extends Annotation> annotation: annotations) {
                            if(method.isAnnotationPresent(annotation))
                                list.add(method);
                        }
                    }
                    else
                        list.add(method);
                }
            }
        }

        Method[] retval=new Method[list.size()];
        for(int i=0; i < list.size(); i++)
            retval[i]=list.get(i);
        return retval;
    }