    private static void generateDecoratorsClass(File input_path, File output_path)
        throws IOException {

        String raw_template = readFile(DECORATORS_CLASS_TEMP, input_path);

                Map<Integer, String> replicated_content = new HashMap<Integer, String>();

                        String processed_template = null;
        BufferedReader reader = new BufferedReader(new StringReader(raw_template));
        String line;
        StringBuilder buffer = new StringBuilder();
        boolean in_replicated_block = false;
        boolean need_newline = false;
        while ((line = reader.readLine()) != null) {
            if (!in_replicated_block &&
                line.startsWith("====START_REPLICATED_CONTENT #")) {

                in_replicated_block = true;
                need_newline = false;

                if (processed_template == null) {
                    processed_template = buffer.toString();
                }

                buffer = new StringBuilder();
            }
            else if (in_replicated_block &&
                line.startsWith("=====END_REPLICATED_CONTENT #")) {
                int number_start_index = "=====END_REPLICATED_CONTENT #".length();
                int number_end_index = line.indexOf("=", number_start_index);

                String number = line.substring(number_start_index, number_end_index);
                Integer number_obj = new Integer(number);

                replicated_content.put(number_obj, buffer.toString());

                in_replicated_block = false;
                need_newline = false;
            }
            else {
                if (need_newline) {
                    buffer.append(System.getProperty("line.separator"));
                }
                else need_newline = true;

                buffer.append(line);
            }
        }

        for (Map.Entry<Integer, String> entry : replicated_content.entrySet()) {
            
            StringBuilder entry_buffer = new StringBuilder();

            boolean first_loop = true;
            for (int i = 0; i < WRAPPERS.length; i++) {
                WrapperInfo info = WRAPPERS[ i ];

                String k = info.primitive;
                String KT = info.class_name;
                String K = shortInt(KT);
                String KMAX = info.max_value;
                String KMIN = info.min_value;

                for (int j = 0; j < WRAPPERS.length; j++) {
                    WrapperInfo jinfo = WRAPPERS[ j ];

                    String v = jinfo.primitive;
                    String VT = jinfo.class_name;
                    String V = shortInt(VT);
                    String VMAX = jinfo.max_value;
                    String VMIN = jinfo.min_value;

                    String out = entry.getValue();
                    String before_e = out;
                    out = Pattern.compile("#e#").matcher(out).replaceAll(k);
                    out = Pattern.compile("#E#").matcher(out).replaceAll(K);
                    out = Pattern.compile("#ET#").matcher(out).replaceAll(KT);
                    out = Pattern.compile("#EMAX#").matcher(out).replaceAll(KMAX);
                    out = Pattern.compile("#EMIN#").matcher(out).replaceAll(KMIN);
                    boolean uses_e = !out.equals(before_e);

                                                            if (uses_e && j != 0) break;

                    out = Pattern.compile("#v#").matcher(out).replaceAll(v);
                    out = Pattern.compile("#V#").matcher(out).replaceAll(V);
                    out = Pattern.compile("#VT#").matcher(out).replaceAll(VT);
                    out = Pattern.compile("#VMAX#").matcher(out).replaceAll(VMAX);
                    out = Pattern.compile("#VMIN#").matcher(out).replaceAll(VMIN);

                    out = Pattern.compile("#k#").matcher(out).replaceAll(k);
                    out = Pattern.compile("#K#").matcher(out).replaceAll(K);
                    out = Pattern.compile("#KT#").matcher(out).replaceAll(KT);
                    out = Pattern.compile("#KMAX#").matcher(out).replaceAll(KMAX);
                    out = Pattern.compile("#KMIN#").matcher(out).replaceAll(KMIN);

                    if (first_loop) first_loop = false;
                    else {
                        entry_buffer.append(System.getProperty("line.separator"));
                        entry_buffer.append(System.getProperty("line.separator"));
                    }

                    entry_buffer.append(out);
                }
            }

            processed_template =
                Pattern.compile("#REPLICATED" + entry.getKey() + "#").matcher(
                    processed_template).replaceAll(entry_buffer.toString());
        }

        writeFile("Decorators.java", processed_template, output_path);
    }