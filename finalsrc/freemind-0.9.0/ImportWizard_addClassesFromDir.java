	public void addClassesFromDir(
		Vector classList,
		File rootDir,
		File currentDir, int recursionLevel) {
	    if(recursionLevel >= 6){
            	        return;
        }
		String[] files = currentDir.list();
		if(files != null) { 
			for (int i = 0; i < files.length; i++) {
				String current = files[i];
				logger.info("looking at: " + current);
				if (isInteresting(current)) {
					String rootPath = rootDir.getPath();
					String currentPath = currentDir.getPath();
					if (! currentPath.startsWith(rootPath)) {
						logger.severe(
							"currentPath doesn't start with rootPath!\n"
								+ "rootPath: "
								+ rootPath
								+ "\n"
								+ "currentPath: "
								+ currentPath
								+ "\n");
					} else {
						current =
							current.substring(
								0,
								current.length() - lookFor.length());
						String packageName =
							currentPath.substring(rootPath.length());
						String fileName;
						if (packageName.length() > 0) {
														fileName = 
						} else {
														fileName = current;
						}
						classList.addElement(fileName);
						logger.info("Found: " + fileName);
					}
				} else {
										File currentFile = new File(currentDir, current);
					if (currentFile.isDirectory()) {
						addClassesFromDir(classList, rootDir, currentFile, recursionLevel+1);
					}
				}
			}
		}
	}