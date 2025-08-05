public ConnectionTableNIO(Receiver r, InetAddress bind_addr, InetAddress external_addr, int srv_port, int max_port,
                             long reaper_interval, long conn_expire_time
                             ) throws Exception
   {
      setReceiver(r);
      this.bind_addr=bind_addr;
      this.external_addr=external_addr;
      this.srv_port=srv_port;
      this.max_port=max_port;
      this.reaper_interval=reaper_interval;
      this.conn_expire_time=conn_expire_time;
      use_reaper=true;
      start();
   }