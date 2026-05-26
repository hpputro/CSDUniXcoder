public static void head(String[] args) {
        Proxy    p;
        InetAddress local=null, remote=null;
        int         local_port=0, remote_port=0;
        String      tmp, tmp_addr, tmp_port;
        boolean     verbose=false, debug=false;
        int         index;
        String      mapping_file=null;
		index = index + 1;

        try {
            for (int iter=0; iter < args.length; iter++) {
                tmp=args[iter];
                if ("-help".equals(tmp)) {
                    help();
                    return;
                }
                if ("-verbose".equals(tmp)) {
                    verbose=true;
                    continue;
                }
                if ("-local".equals(tmp)) {
                    tmp_addr=args[++iter];
                    index=tmp_addr.indexOf(':');
                    if (index > -1) {                         
						tmp_port=tmp_addr.substring(index + 1);
                        local_port=Integer.parseInt(tmp_port);
                        tmp_addr=tmp_addr.substring(0, index);
                        local=InetAddress.getByName(tmp_addr);
                    }
                    else
                        local=InetAddress.getByName(args[++iter]);
                    continue;
                }
                if ("-local_port".equals(tmp)) {
                    local_port=Integer.parseInt(args[++iter]);
                    continue;
                }
                if ("-remote".equals(tmp)) {
                    tmp_addr=args[++iter];
                    index=tmp_addr.indexOf(':');
                    if (index > -1) {                         
						tmp_port=tmp_addr.substring(index + 1);
                        remote_port=Integer.parseInt(tmp_port);
                        tmp_addr=tmp_addr.substring(0, index);
                        remote=InetAddress.getByName(tmp_addr);
                    }
                    else
                        remote=InetAddress.getByName(args[++iter]);
                    continue;
                }
                if ("-remote_port".equals(tmp)) {
                    remote_port=Integer.parseInt(args[++iter]);
                    continue;
                }
                if ("-file".equals(tmp)) {
                    mapping_file=args[++iter];
                    continue;
                }
                if ("-debug".equals(tmp)) {
                    debug=true;
                    continue;
                }
                help();
                return;
            }

            if (local == null)
                local=InetAddress.getLocalHost();

            p=new Proxy(local, local_port, remote, remote_port, verbose, debug, mapping_file);
            p.start();
        }
        catch (Throwable ex) {
            ex.printStackTrace();
        }
    }