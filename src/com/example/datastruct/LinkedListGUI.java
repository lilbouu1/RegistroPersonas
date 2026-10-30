package com.example.datastruct;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.LinkedList;
import javax.swing.*;

public class LinkedListGUI extends JFrame {
    private final LinkedList<Person> people = new LinkedList<>();

    private final JTextField txtId = new JTextField(15);
    private final JTextField txtName = new JTextField(15);
    private final JTextField txtAge = new JTextField(15);
    private final JComboBox<String> cmbGender =
            new JComboBox<>(new String[]{"Masculino", "Femenino", "Otro"});
    private final JTextField txtAddress = new JTextField(15);

    private final JTextField txtSearchId = new JTextField(12);
    private final JTextField txtAgeRange = new JTextField("25-35", 12);
    private final JTextArea txtOutput = new JTextArea(9, 55);

    public LinkedListGUI() {
        setTitle("Person Registration and Search (Java Swing)");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        add(buildForm(), BorderLayout.CENTER);
        add(buildOperations(), BorderLayout.EAST);

        txtOutput.setEditable(false);
        txtOutput.setLineWrap(true);
        txtOutput.setWrapStyleWord(true);
        JScrollPane scroll = new JScrollPane(txtOutput);
        scroll.setBorder(BorderFactory.createTitledBorder("Resultados / Consola"));
        add(scroll, BorderLayout.SOUTH);

        setJMenuBar(buildMenu());
        pack();
        setLocationRelativeTo(null);
        setMinimumSize(getSize());
    }

    private JPanel buildForm() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createTitledBorder("PERSON REGISTRATION FORM"));
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(8, 8, 8, 8);
        c.anchor = GridBagConstraints.WEST;

        addFormRow(panel, c, 0, "ID (Number)", txtId);
        addFormRow(panel, c, 1, "Name", txtName);
        addFormRow(panel, c, 2, "Age", txtAge);
        addFormRow(panel, c, 3, "Gender", cmbGender);
        addFormRow(panel, c, 4, "Address", txtAddress);
        return panel;
    }

    private void addFormRow(JPanel panel, GridBagConstraints c,
                            int row, String label, JComponent field) {
        c.gridx = 0; c.gridy = row; c.weightx = 0;
        c.fill = GridBagConstraints.NONE;
        panel.add(new JLabel(label + ":"), c);

        c.gridx = 1; c.weightx = 1;
        c.fill = GridBagConstraints.HORIZONTAL;
        panel.add(field, c);
    }

    private JPanel buildOperations() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createTitledBorder("Person Operations"));

        addButton(panel, "Register Person", this::registerPerson);
        addButton(panel, "Update Person", this::updatePerson);
        addButton(panel, "Remove Person (by ID)", this::removePerson);
        addButton(panel, "Search Person (by ID)", this::searchPerson);
        addButton(panel, "List All Persons", this::listPeople);
        addButton(panel, "Clear Form", this::clearForm);
        addButton(panel, "Find Persons by Name", this::findByName);
        addButton(panel, "Check ID exists", this::checkId);

        JPanel filters = new JPanel();
        filters.setLayout(new BoxLayout(filters, BoxLayout.Y_AXIS));
        filters.setBorder(BorderFactory.createTitledBorder("Search & Filter"));
        filters.add(new JLabel("Enter ID to Search:"));
        filters.add(txtSearchId);
        filters.add(Box.createVerticalStrut(8));
        filters.add(new JLabel("Filter by Age range (min-max):"));
        filters.add(txtAgeRange);

        JButton btnFilter = new JButton("Execute age filter");
        btnFilter.addActionListener(e -> filterByAge());
        filters.add(Box.createVerticalStrut(6));
        filters.add(btnFilter);
        panel.add(filters);
        return panel;
    }

    private void addButton(JPanel panel, String text, Runnable action) {
        JButton button = new JButton(text);
        button.setAlignmentX(LEFT_ALIGNMENT);
        button.setMaximumSize(new java.awt.Dimension(Integer.MAX_VALUE, 34));
        button.addActionListener(e -> action.run());
        panel.add(button);
        panel.add(Box.createVerticalStrut(5));
    }

    private JMenuBar buildMenu() {
        JMenuBar bar = new JMenuBar();
        JMenu menu = new JMenu("Help");
        JMenuItem about = new JMenuItem("About this project");
        about.addActionListener(e -> JOptionPane.showMessageDialog(this,
                "Ejemplo educativo de Java Swing + LinkedList<Person>."));
        menu.add(about);
        bar.add(menu);
        return bar;
    }

    private Person readForm() {
        try {
            int id = Integer.parseInt(txtId.getText().trim());
            int age = Integer.parseInt(txtAge.getText().trim());
            String name = txtName.getText().trim();
            String address = txtAddress.getText().trim();
            String gender = (String) cmbGender.getSelectedItem();

            if (id <= 0 || age < 0 || name.isEmpty() || address.isEmpty()) {
                throw new IllegalArgumentException(
                        "ID debe ser positivo; edad no negativa; nombre y dirección son obligatorios.");
            }
            return new Person(id, name, age, gender, address);
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException("ID y edad deben contener números enteros.");
        }
    }

    private Person findPerson(int id) {
        for (Person p : people) {
            if (p.getId() == id) return p;
        }
        return null;
    }

    private void registerPerson() {
        try {
            Person p = readForm();
            if (findPerson(p.getId()) != null) {
                showMessage("Ya existe una persona con ese ID.");
                return;
            }
            people.add(p);
            log("Persona registrada: " + p);
            clearFormFields();
        } catch (IllegalArgumentException ex) {
            showMessage(ex.getMessage());
        }
    }

    private void updatePerson() {
        try {
            Person data = readForm();
            Person existing = findPerson(data.getId());
            if (existing == null) {
                showMessage("No se encontró el ID indicado.");
                return;
            }
            existing.setName(data.getName());
            existing.setAge(data.getAge());
            existing.setGender(data.getGender());
            existing.setAddress(data.getAddress());
            log("Registro actualizado: " + existing);
        } catch (IllegalArgumentException ex) {
            showMessage(ex.getMessage());
        }
    }

    private void removePerson() {
        Integer id = readId(txtSearchId.getText().trim().isEmpty()
                ? txtId.getText().trim() : txtSearchId.getText().trim());
        if (id == null) return;

        Person p = findPerson(id);
        if (p == null) {
            log("No existe una persona con ID " + id + ".");
            return;
        }
        people.remove(p);
        log("Persona eliminada: " + p);
        clearFormFields();
    }

    private void searchPerson() {
        Integer id = readId(txtSearchId.getText().trim().isEmpty()
                ? txtId.getText().trim() : txtSearchId.getText().trim());
        if (id == null) return;

        Person p = findPerson(id);
        if (p == null) {
            log("No se encontró el ID " + id + ".");
            return;
        }
        txtId.setText(String.valueOf(p.getId()));
        txtName.setText(p.getName());
        txtAge.setText(String.valueOf(p.getAge()));
        cmbGender.setSelectedItem(p.getGender());
        txtAddress.setText(p.getAddress());
        log("Persona encontrada: " + p);
    }

    private void listPeople() {
        if (people.isEmpty()) {
            log("La lista está vacía.");
            return;
        }
        StringBuilder sb = new StringBuilder("=== TODAS LAS PERSONAS ===\n");
        for (Person p : people) sb.append(p).append('\n');
        log(sb.toString());
    }

    private void findByName() {
        String name = txtName.getText().trim().toLowerCase();
        if (name.isEmpty()) {
            showMessage("Escribe un nombre en el campo Name para buscar.");
            return;
        }
        StringBuilder sb = new StringBuilder("Coincidencias para: " + name + "\n");
        int count = 0;
        for (Person p : people) {
            if (p.getName().toLowerCase().contains(name)) {
                sb.append(p).append('\n');
                count++;
            }
        }
        log(count == 0 ? "No se encontraron coincidencias." : sb.toString());
    }

    private void checkId() {
        Integer id = readId(txtSearchId.getText().trim().isEmpty()
                ? txtId.getText().trim() : txtSearchId.getText().trim());
        if (id == null) return;
        log(findPerson(id) == null
                ? "El ID " + id + " está disponible."
                : "El ID " + id + " ya está registrado.");
    }

    private void filterByAge() {
        try {
            String[] limits = txtAgeRange.getText().trim().split("-");
            if (limits.length != 2) throw new NumberFormatException();
            int min = Integer.parseInt(limits[0].trim());
            int max = Integer.parseInt(limits[1].trim());
            if (min < 0 || max < min) throw new NumberFormatException();

            StringBuilder sb = new StringBuilder(
                    "Personas con edad entre " + min + " y " + max + ":\n");
            int count = 0;
            for (Person p : people) {
                if (p.getAge() >= min && p.getAge() <= max) {
                    sb.append(p).append('\n');
                    count++;
                }
            }
            log(count == 0 ? "No hay personas en ese rango de edad." : sb.toString());
        } catch (NumberFormatException ex) {
            showMessage("Escribe el rango como dos números, por ejemplo: 25-35.");
        }
    }

    private Integer readId(String value) {
        try {
            int id = Integer.parseInt(value);
            if (id <= 0) throw new NumberFormatException();
            return id;
        } catch (NumberFormatException ex) {
            showMessage("Escribe un ID numérico positivo en el campo ID o Enter ID to Search.");
            return null;
        }
    }

    private void clearForm() {
        clearFormFields();
        txtOutput.setText("");
    }

    private void clearFormFields() {
        txtId.setText("");
        txtName.setText("");
        txtAge.setText("");
        cmbGender.setSelectedIndex(0);
        txtAddress.setText("");
        txtId.requestFocus();
    }

    private void log(String message) {
        txtOutput.append(message + "\n\n");
        txtOutput.setCaretPosition(txtOutput.getDocument().getLength());
    }

    private void showMessage(String message) {
        JOptionPane.showMessageDialog(this, message,
                "Aviso", JOptionPane.INFORMATION_MESSAGE);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LinkedListGUI().setVisible(true));
    }
}
