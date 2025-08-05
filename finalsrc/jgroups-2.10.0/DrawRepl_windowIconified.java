public void windowIconified(WindowEvent e) {}
    public void windowOpened(WindowEvent e) {}










    public void actionPerformed(ActionEvent e) {
	String command=e.getActionCommand();
	if("Clear".equals(command))
	    clearPanel();
	else if("Exit".equals(command)) {
	    mainFrame.setVisible(false);
	    System.exit(0);
	}
	else
	    System.out.println("Unknown action");
    }


}