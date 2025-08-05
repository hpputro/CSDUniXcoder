private Vector findConstructors() {
        Vector result = null;
        final String namespace = _fname.getNamespace();

        final int nArgs = _arguments.size();
        try {
          if (_clazz == null) {
            _clazz = ObjectFactory.findProviderClass(
              _className, ObjectFactory.findClassLoader(), true);

            if (_clazz == null) {
              final ErrorMsg msg = new ErrorMsg(ErrorMsg.CLASS_NOT_FOUND_ERR, _className);
              getParser().reportError(Constants.ERROR, msg);
            }          
          }

          final Constructor[] constructors = _clazz.getConstructors();

          for (int i = 0; i < constructors.length; i++) {
              final int mods = constructors[i].getModifiers();
                            if (Modifier.isPublic(mods) &&
                  constructors[i].getParameterTypes().length == nArgs)
              {
                if (result == null) {
                  result = new Vector();
                }
                result.addElement(constructors[i]);
              }
          }
        }
        catch (ClassNotFoundException e) {
          final ErrorMsg msg = new ErrorMsg(ErrorMsg.CLASS_NOT_FOUND_ERR, _className);
          getParser().reportError(Constants.ERROR, msg);
        }
            
        return result;
    }