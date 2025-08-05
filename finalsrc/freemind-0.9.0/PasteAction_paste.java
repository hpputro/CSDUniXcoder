public void paste(Object TransferData, MindMapNode target,
                boolean asSibling, boolean isLeft, Transferable t)
                throws UnsupportedFlavorException, IOException {
                        String textFromClipboard = (String) TransferData;
                                    MindMapNode pastedNode = pasteStringWithoutRedisplay(t, target,  asSibling, isLeft);

            textFromClipboard = textFromClipboard.replaceAll("<!--.*?-->", "");                                                                                                                                                                             String[] links = textFromClipboard
                    .split("<[aA][^>]*[hH][rR][eE][fF]=\"");

            MindMapNode linkParentNode = null;
            URL referenceURL = null;
            boolean baseUrlCanceled = false;

            for (int i = 1; i < links.length; i++) {
                String link = links[i].substring(0, links[i].indexOf("\""));
                String textWithHtml = links[i].replaceAll("^[^>]*>", "")
                        .replaceAll("</[aA]>[\\s\\S]*", "");
                String text = HtmlTools.toXMLUnescapedText(textWithHtml.replaceAll(
                        "\\n", "").replaceAll("<[^>]*>", "").trim());
                if (text.equals("")) {
                    text = link;
                }
                URL linkURL = null;
                try {
                    linkURL = new URL(link);
                } catch (MalformedURLException ex) {
                    try {
                                                if (referenceURL == null && !baseUrlCanceled) {
                            String referenceURLString = JOptionPane
                                    .showInputDialog(
                                    		pMindMapController.getView().getSelected(), 
                                    		pMindMapController.getText("enter_base_url"));
                            if (referenceURLString == null) {
                                baseUrlCanceled = true;
                            } else {
                                referenceURL = new URL(referenceURLString);
                            }
                        }
                        linkURL = new URL(referenceURL, link);
                    } catch (MalformedURLException ex2) {
                    }
                }
                if (linkURL != null) {
                    if (links.length == 2 & pastedNode != null) {
                                                                                                                                                ((MindMapNodeModel) pastedNode).setLink(linkURL
                                .toString());
                        break;
                    }
                    if (linkParentNode == null) {
                        linkParentNode = pMindMapController.newNode("Links", target.getMap());
                        linkParentNode.setLeft(target.isNewChildLeft());
                                                insertNodeInto(linkParentNode, target);
                        ((NodeAdapter) linkParentNode).setBold(true);
                    }
                    MindMapNode linkNode = pMindMapController.newNode(text, target.getMap());
                    linkNode.setLink(linkURL.toString());
                    insertNodeInto(linkNode, linkParentNode);
                }
            }
        }