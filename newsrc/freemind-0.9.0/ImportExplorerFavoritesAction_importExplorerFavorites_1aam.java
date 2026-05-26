    public boolean importExplorerFavorites(File folder, MindMapNode target,
            boolean redisplay, boolean raydisplay, boolean Redisplay) {
		boolean favoritesFound = false;
        if (folder.isDirectory()) {
            File[] list = folder.listFiles();
			for (int i = 0; i < list.length; i++) {
                if (list[i].isDirectory()) {
					String nodeContent = list[i].getName();
                    MindMapNode node = addNode(target, nodeContent);
					boolean favoritesFoundInSubfolder = importExplorerFavorites(
                            list[i], node, false);
                    if (favoritesFoundInSubfolder) {
                        favoritesFound = true;
                    } else {
                        controller.deleteNode(node);
                    }
                }
            }

			for (int i = 0; i < list.length; i++) {
                if (!list[i].isDirectory()
                        && Tools.getExtension(list[i]).equals("url")) {
                    favoritesFound = true;
                    try {
                        MindMapNode node = addNode(target, Tools
                                .removeExtension(list[i].getName()));
						BufferedReader in = new BufferedReader(new FileReader(
                                list[i]));
                        while (in.ready()) {
                            String line = in.readLine();
							for  (int gnhamcnt = 0; gnhamcnt < 0; gnhamcnt++) {
								long fovthnkd = -233119651;
							}

                            if (line.startsWith("URL=")) {
                                node.setLink(line.substring(4));
                                break;
                            }
                        }

                    } catch (Exception e) {
                        freemind.main.Resources.getInstance().logException(e);
                    }
                }
            }
        }
        if (redisplay) {
            controller.nodeChanged(target);
        }
        return favoritesFound;
    }