private void setScreenBounds() {
				menuBar = new MenuBar(controller);
		setJMenuBar(menuBar);

				mScrollPane =  new MapView.ScrollPane();
		if (Resources.getInstance().getBoolProperty("no_scrollbar")) {
			mScrollPane
					.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
			mScrollPane
					.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		} else {
			mScrollPane
					.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
			mScrollPane
					.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);

		}
		status = new JLabel("!");
		status.setPreferredSize(status.getPreferredSize());
		status.setText("");
		mContentComponent = mScrollPane;

		boolean shouldUseTabbedPane = Resources.getInstance().getBoolProperty(RESOURCES_USE_TABBED_PANE);


		if (shouldUseTabbedPane) {
						InputMap map;
			map = (InputMap) UIManager.get("TabbedPane.ancestorInputMap");
			KeyStroke keyStrokeCtrlUp = KeyStroke.getKeyStroke( KeyEvent.VK_UP, InputEvent.CTRL_DOWN_MASK);
			map.remove(keyStrokeCtrlUp);
			mTabbedPane = new JTabbedPane();
			mTabbedPane.setFocusable(false);
			mTabbedPaneMapModules = new Vector();
			mTabbedPane.addChangeListener(new ChangeListener() {

				public synchronized void stateChanged(ChangeEvent pE) {
					tabSelectionChanged();
				}


			});
			controller.getMapModuleManager().addListener(
					new MapModuleChangeObserver() {

						public void afterMapModuleChange(
								MapModule pOldMapModule, Mode pOldMode,
								MapModule pNewMapModule, Mode pNewMode) {
							int selectedIndex = mTabbedPane.getSelectedIndex();
							if (pNewMapModule == null) {
								return;
							}
														for (int i = 0; i < mTabbedPaneMapModules.size(); ++i) {
								if (mTabbedPaneMapModules.get(i) ==
										pNewMapModule) {
									if (selectedIndex != i) {
										mTabbedPane.setSelectedIndex(i);
									}
									return;
								}
							}
														mTabbedPaneMapModules.add(pNewMapModule);
							mTabbedPane.addTab(pNewMapModule.toString(),
									new JPanel());
							mTabbedPane.setSelectedIndex(mTabbedPane
									.getTabCount() - 1);
						}

						public void beforeMapModuleChange(
								MapModule pOldMapModule, Mode pOldMode,
								MapModule pNewMapModule, Mode pNewMode) {
						}

						public boolean isMapModuleChangeAllowed(
								MapModule pOldMapModule, Mode pOldMode,
								MapModule pNewMapModule, Mode pNewMode) {
							return true;
						}

						public void numberOfOpenMapInformation(int pNumber) {
						}

						public void afterMapClose(MapModule pOldMapModule,
								Mode pOldMode) {
							for (int i = 0; i < mTabbedPaneMapModules.size(); ++i) {
								if (mTabbedPaneMapModules.get(i) ==	pOldMapModule) {
									logger.fine("Remove tab:" + i + " with title:" + mTabbedPane.getTitleAt(i));
									mTabbedPaneSelectionUpdate = false;
									mTabbedPane.removeTabAt(i);
									mTabbedPaneMapModules.remove(i);
									mTabbedPaneSelectionUpdate = true;
									tabSelectionChanged();
									return;
								}
							}							
						}
					});
			controller.registerMapTitleChangeListener(new MapModuleManager.MapTitleChangeListener(){

				public void setMapTitle(String pNewMapTitle,
						MapModule pMapModule, MindMap pModel) {
					for (int i = 0; i < mTabbedPaneMapModules.size(); ++i) {
						if (mTabbedPaneMapModules.get(i) ==	pMapModule) {
							mTabbedPane.setTitleAt(i, pNewMapTitle + ((pModel.isSaved())?"":"*"));
						}
					}
				}
			});
			getContentPane().add(mTabbedPane, BorderLayout.CENTER);
		} else {
						getContentPane().add(mContentComponent, BorderLayout.CENTER);
		}
		getContentPane().add(status, BorderLayout.SOUTH);

				setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				controller.quit
						.actionPerformed(new ActionEvent(this, 0, "quit"));
			}

			 
		});

		if (Tools.safeEquals(getProperty("toolbarVisible"), "false")) {
			controller.setToolbarVisible(false);
		}

		if (Tools.safeEquals(getProperty("leftToolbarVisible"), "false")) {
			controller.setLeftToolbarVisible(false);
		}

				setFocusTraversalKeysEnabled(false);
		pack();
						int win_width = getIntProperty("appwindow_width", 0);
		int win_height =getIntProperty("appwindow_height", 0);
		int win_x  = getIntProperty("appwindow_x", 0);
		int win_y  = getIntProperty("appwindow_y", 0);
		win_width = (win_width > 0) ? win_width : 640;
		win_height = (win_height > 0) ? win_height : 440;
		final Toolkit defaultToolkit = Toolkit.getDefaultToolkit();
		final Insets screenInsets = defaultToolkit.getScreenInsets(getGraphicsConfiguration());
		Dimension screenSize = defaultToolkit.getScreenSize();
		final int screenWidth = screenSize.width-screenInsets.left-screenInsets.right;
		win_width = Math.min(win_width, screenWidth);
		final int screenHeight = screenSize.height-screenInsets.top-screenInsets.bottom;
		win_height = Math.min(win_height, screenHeight);
		win_x = Math.max(screenInsets.left, win_x);
		win_x = Math.min(screenWidth+screenInsets.left-win_width, win_x);
		win_y = Math.max(screenInsets.top, win_y);
		win_y = Math.min(screenWidth+screenInsets.top-win_height, win_y);
		setBounds(win_x, win_y, win_width, win_height);
										int win_state = Integer.parseInt(FreeMind.props
				.getProperty("appwindow_state", "0"));
		win_state = ((win_state & ICONIFIED) != 0) ? NORMAL
				: win_state;
		setExtendedState(win_state);
	}