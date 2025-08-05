public static void main(String[] args) {
        Proxy    p;
        InetAddress local=null, remote=null;
        int         local_port=0, remote_port=0;
        String      tmp, tmp_addr, tmp_port;
        boolean     verbose=false, debug=false;
        int         index;
        String      mapping_file=null;

        try {
            for (int i=0; i < args.length; i++) {
                tmp=args[i];
                if ("-help".equals(tmp)) {
                    help();
                    return;
                }
                if ("-verbose".equals(tmp)) {
                    verbose=true;
                    continue;
                }
                if ("-local".equals(tmp)) {
                    tmp_addr=args[++i];
                    index=tmp_addr.indexOf(':');
                    if (index > -1) {                         tmp_port=tmp_addr.substring(index + 1);
                        local_port=Integer.parseInt(tmp_port);
                        tmp_addr=tmp_addr.substring(0, index);
                        local=InetAddress.getByName(tmp_addr);
                    }
                    else
                        local=InetAddress.getByName(args[++i]);
                    continue;
                }
                if ("-local_port".equals(tmp)) {
                    local_port=Integer.parseInt(args[++i]);
                    continue;
                }
                if ("-remote".equals(tmp)) {
                    tmp_addr=args[++i];
                    index=tmp_addr.indexOf(':');
                    if (index > -1) {                         tmp_port=tmp_addr.substring(index + 1);
                        remote_port=Integer.parseInt(tmp_port);
                        tmp_addr=tmp_addr.substring(0, index);
                        remote=InetAddress.getByName(tmp_addr);
                    }
                    else
                        remote=InetAddress.getByName(args[++i]);
                    continue;
                }
                if ("-remote_port".equals(tmp)) {
                    remote_port=Integer.parseInt(args[++i]);
                    continue;
                }
                if ("-file".equals(tmp)) {
                    mapping_file=args[++i];
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