private void initialize() {
        this.setTitle(mTranslator
                .getText("accessories/plugins/EncryptNode.properties_0"));         this.setSize(300, 200);
        this.setContentPane(getJContentPane());
        setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent we) {
                cancelPressed();
            }

        });
        Tools.addEscapeActionToDialog(this, new AbstractAction(){
			public void actionPerformed(ActionEvent pE) {
				cancelPressed();
			}});

    }