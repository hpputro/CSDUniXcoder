public void run() {
            byte[] val;
            String line;
            
            while(client_sock.isConnected()) {
                try {
                    line=Util.readLine(input);
                    if(line == null)
                        break;

                    
                    Request req=parseRequest(line);
                    if(req == null) {
                        break;
                    }

                    
                    switch(req.type) {
                        case SET:
                            byte[] data=new byte[req.number_of_bytes];
                            int num=input.read(data, 0, data.length);
                            if(num == -1)
                                throw new EOFException();
                            cache.put(req.key, data, req.caching_time);
                            output.write(STORED);
                            output.flush();
                            Util.discardUntilNewLine(input);
                            break;

                        case GET:
                        case GETS:
                            if(req.keys != null && !req.keys.isEmpty()) {
                                for(String key: req.keys) {
                                    val=cache.get(key);
                                    if(val != null) {
                                        int length=val.length;
                                        output.write(("VALUE " + key + " 0 " + length + "\r\n").getBytes());
                                        output.write(val, 0, length);
                                        output.write(RN);
                                    }
                                }
                            }
                            output.write(END);
                            output.flush();
                            break;

                        case DELETE:
                            cache.remove(req.key);
                            output.write(DELETED);
                            output.flush();
                            break;

                        case STATS:
                            Map<String,Object> stats=getStats();
                            StringBuilder sb=new StringBuilder();
                            for(Map.Entry<String,Object> entry: stats.entrySet()) {
                                sb.append("STAT ").append(entry.getKey()).append(" ").append(entry.getValue()).append("\r\n");
                            }

                            sb.append("END\r\n");
                            output.write(sb.toString().getBytes());
                            output.flush();
                            break;
                    }
                }
                catch(StreamCorruptedException corrupted_ex) {
                    try {
                        output.write(("CLIENT_ERROR failed to parse request: " + corrupted_ex + ":\r\n").getBytes());
                        output.flush();
                    }
                    catch(IOException e) {}
                }
                catch(EOFException end_of_file_ex) {
                    break;
                }
                catch(Throwable e) {
                }
            }
            Util.close(client_sock);
        }