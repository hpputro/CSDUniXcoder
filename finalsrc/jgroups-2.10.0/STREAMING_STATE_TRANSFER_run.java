public void run() {
            runner = Thread.currentThread();
            for (; running;) {
                try {
                    if (log.isDebugEnabled())
                        log.debug("StateProviderThreadSpawner listening at "
                                + getServerSocketAddress() + "...");

                    final Socket socket = serverSocket.accept();
                    pool.execute(new Runnable() {
                        public void run() {
                            if (log.isDebugEnabled())
                                log.debug("Accepted request for state transfer from "
                                        + socket.getInetAddress() + ":" + socket.getPort()
                                        + " handing of to PooledExecutor thread");
                            new StateProviderHandler().process(socket);
                        }
                    });

                } catch (IOException e) {
                    if (log.isWarnEnabled()) {
                                                                        if (serverSocket != null && !serverSocket.isClosed()) {
                            log.warn("Spawning socket from server socket finished abnormaly", e);
                        }
                    }
                }
            }
        }