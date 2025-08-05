public HookInstanciationMethod getInstanciationMethod() {
		if (pluginAction.getInstanciation() != null) {
			HashMap allInstMethods = HookInstanciationMethod
					.getAllInstanciationMethods();
			for (Iterator i = allInstMethods.keySet().iterator(); i.hasNext();) {
				String name = (String) i.next();
				if (pluginAction.getInstanciation().equalsIgnoreCase(name)) {
					return (HookInstanciationMethod) allInstMethods.get(name);
				}
			}
		}
				return HookInstanciationMethod.Other;
	}