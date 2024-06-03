package org.example;

public class Adapter implements TableData {
    ContactManager manager;

    public Adapter (ContactManager manager) {
        this.manager = manager;
    }

    @Override
    public int getColumnCount() {
        return 4;
    }

    @Override
    public int getRowCount() {
        return manager.getContactCount();
    }

    @Override
    public int getColumnSpacing() {
        return 1;
    }

    @Override
    public int getRowSpacing() {
        return 1;
    }

    @Override
    public char getHeaderUnderline() {
        return 1;
    }

    @Override
    public String getColumnHeader(int col) {
        if (col == 0) {
            return "First Name";
        } else if (col == 1) {
            return "Last Name";
        } else if (col == 2) {
            return "Phone";
        } else if (col == 3) {
            return "Email";
        } else {
            return "";
        }
    }

    @Override
    public int getColumnWidth(int col) {
        return 20;
    }

    @Override
    public Justification getColumnJustification(int col) {
        return Justification.Center;
    }

    @Override
    public String getCellValue(int row, int col) {
        Contact contact = manager.getContact(row);
        if (col == 0) {
            return contact.getFirstName();
        } else if (col == 1) {
            return contact.getLastName();
        } else if (col == 2) {
            return contact.getPhone();
        } else if (col == 3) {
            return contact.getEmail();
        } else {
            return "invalid query";
        }
    }
}
