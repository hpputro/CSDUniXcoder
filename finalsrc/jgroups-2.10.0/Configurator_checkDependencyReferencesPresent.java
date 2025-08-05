static void checkDependencyReferencesPresent(List<AccessibleObject> objects, Map<String, AccessibleObject> props) {
    	
    	    	for(int i = 0; i < objects.size(); i++) {
    		
    		    		AccessibleObject ao = objects.get(i) ;
    		Property annotation = ao.getAnnotation(Property.class) ;
            if (annotation == null) {
            	throw new IllegalArgumentException("@Property annotation is required for checking dependencies;" + 
            			" annotation is missing for Field/Method " + ao.toString()) ;
            }
    		
    		String dependsClause = annotation.dependsUpon() ;
    		if (dependsClause.trim().length() == 0)
    			continue ;
    		
    		    		StringTokenizer st = new StringTokenizer(dependsClause, ",") ;
    		while (st.hasMoreTokens()) {
    			String token = st.nextToken().trim() ;
    			
    			    			boolean found = false ;
    			Set<String> keyset = props.keySet();
    			for (Iterator<String> iter = keyset.iterator(); iter.hasNext();) {
    				if (iter.next().equals(token)) {
    					found = true ;
    					break ;
    				}
    			}
    			if (!found) {
    				throw new IllegalArgumentException("@Property annotation " + annotation.name() + 
    						" has an unresolved dependsUpon property: " + token) ;
    			}
    		}
    	}
    	
    }