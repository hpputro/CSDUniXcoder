public void exportAccount(Session session, Account account, File file) {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(file));
            Vector entries = account.getEntries();

                        writeln(writer, "!Type:Bank");

                        if (entries.size() > 0) {
                Entry entry = (Entry) entries.elementAt(0);
                String dateString = formatDate(entry.getDate());
                if (dateString != null)
                    writeln(writer, dateString);
            }
            writeln(
                writer,
                "T" + formatAmount(account.getStartBalance(), account));
            writeln(writer, "CX");
            writeln(writer, "POpening Balance");
            writeln(writer, "L[" + account.getName() + "]");
            writeln(writer, "^");

                        for (int i = 0; i < entries.size(); i++) {
                Entry entry = (Entry) entries.elementAt(i);
                                String dateString = formatDate(entry.getDate());
                if (dateString != null)
                    writeln(writer, dateString);
                                if (entry.getMemo() != null)
                    writeln(writer, "M" + entry.getMemo());
                                if (entry.getStatus() == Entry.RECONCILING)
                    writeln(writer, "C*");
                else if (entry.getStatus() == Entry.CLEARED)
                    writeln(writer, "CX");
                                writeln(writer, "T" + formatAmount(entry.getAmount(), account));
                                if (entry.getCheck() != null)
                    writeln(writer, "N" + entry.getCheck());
                                if (entry.getDescription() != null)
                    writeln(writer, "P" + entry.getDescription());
                                Category category = entry.getCategory();
                if (category != null) {
                    if (category instanceof Account)
                        writeln(
                            writer,
                            "L[" + category.getCategoryName() + "]");
                    else {
                        writeln(writer, "L" + category.getFullCategoryName());
                    }
                                    }
                                writeln(writer, "^");
            }
            writer.close();
        } catch (IOException e) {
            mainFrame.fileWriteError(file);
        }
    }