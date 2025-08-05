    public Vector<Protocol> copyProtocols(ProtocolStack targetStack) throws IllegalAccessException, InstantiationException {
        Vector<Protocol> list=getProtocols();
        Vector<Protocol> retval=new Vector<Protocol>(list.size());
        for(Protocol prot: list) {
            Protocol new_prot=prot.getClass().newInstance();
            new_prot.setProtocolStack(targetStack);
            retval.add(new_prot);

            for(Class<?> clazz=prot.getClass(); clazz != null; clazz=clazz.getSuperclass()) {

                                Field[] fields=clazz.getDeclaredFields();
                for(Field field: fields) {
                    if(field.isAnnotationPresent(Property.class)) {
                        Object value=Configurator.getField(field, prot);
                        Configurator.setField(field, new_prot, value);
                    }
                }

                                Method[] methods=clazz.getDeclaredMethods();
                for(Method method: methods) {
                    String methodName=method.getName();
                    if(method.isAnnotationPresent(Property.class) && Configurator.isSetPropertyMethod(method)) {
                        Property annotation=method.getAnnotation(Property.class);
                        List<String> possible_names=new LinkedList<String>();
                        if(annotation.name() != null)
                            possible_names.add(annotation.name());
                        possible_names.add(methodName.substring(3));
                        possible_names.add(Util.methodNameToAttributeName(methodName));
                        Field field=findField(prot, possible_names);
                        if(field != null) {
                            Object value=Configurator.getField(field, prot);
                            Configurator.setField(field, new_prot, value);
                        }
                    }
                }
            }
        }
        return retval;
    }