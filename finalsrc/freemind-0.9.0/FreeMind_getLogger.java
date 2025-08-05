public Logger getLogger(String forClass) {
		Logger loggerForClass = java.util.logging.Logger.getLogger(forClass);
		if (mFileHandler == null) {
						final Logger parentLogger = loggerForClass.getParent();
			final Handler[] handlers = parentLogger.getHandlers();
			for(int i = 0; i < handlers.length; i++){
				final Handler handler = handlers[i];
				if(handler instanceof ConsoleHandler){
					parentLogger.removeHandler(handler);
				}
			}

			try {
				mFileHandler = new FileHandler(getFreemindDirectory()
						+ File.separator + "log", 1400000, 5, false);
				mFileHandler.setFormatter(new StdFormatter());
				mFileHandler.setLevel(Level.INFO);
				parentLogger.addHandler(mFileHandler);
				
				final ConsoleHandler stdConsoleHandler = new ConsoleHandler();
				stdConsoleHandler.setFormatter(new StdFormatter());
				stdConsoleHandler.setLevel(Level.WARNING);
				parentLogger.addHandler(stdConsoleHandler);

				LoggingOutputStream los;
				Logger logger = Logger.getLogger(StdFormatter.STDOUT.getName());
				los = new LoggingOutputStream(logger, StdFormatter.STDOUT);
				System.setOut(new PrintStream(los, true));

				logger = Logger.getLogger(StdFormatter.STDERR.getName());
				los= new LoggingOutputStream(logger, StdFormatter.STDERR);
				System.setErr(new PrintStream(los, true));

			} catch (Exception e) {
				System.err.println("Error creating logging File Handler");
				e.printStackTrace();
											}
		}
		return loggerForClass;
	}