private void setSessionOpened(boolean state) {
            closeFileItem.setEnabled(state);
            saveFileItem.setEnabled(state);
            saveAsFileItem.setEnabled(state);
            qifImportItem.setEnabled(state);
            mt940ImportItem.setEnabled(state);
            exportFileItem.setEnabled(state);
        }