void loop(Selector selector) {
        Set                 ready_keys;
        SelectionKey        key;
        ServerSocketChannel srv_sock;
        SocketChannel       in_sock, out_sock;
        InetSocketAddress   src, dest;

        while (true) {
            if (verbose)
                log("[Proxy] ready to accept connection");

                        try {
                selector.select();

                                ready_keys=selector.selectedKeys();
                for (Iterator it=ready_keys.iterator(); it.hasNext();) {
                    key=(SelectionKey) it.next();
                    it.remove();

                    if (key.isAcceptable()) {
                        srv_sock=(ServerSocketChannel) key.channel();
                                                src=(InetSocketAddress) key.attachment();
                        in_sock=srv_sock.accept();                         if (verbose)
                            log("Proxy.loop()", "accepted connection from " + toString(in_sock));
                        dest=(InetSocketAddress) mappings.get(src);
                                                if (dest == null) {
                            in_sock.close();
                            log("Proxy.loop()", "did not find a destination host for " + src);
                            continue;
                        }
                        else {
                            if (verbose)
                                log("Proxy.loop()", "relaying traffic from " + toString(src) + " to " + toString(dest));
                        }

                                                try {
                            out_sock=SocketChannel.open(dest);
                                                        handleConnection(in_sock, out_sock);
                        }
                        catch (Exception ex) {
                            in_sock.close();
                            throw ex;
                        }
                    }
                }
            }
            catch (Exception ex) {
                log("Proxy.loop()", "exception: " + ex);
            }
        }
    }