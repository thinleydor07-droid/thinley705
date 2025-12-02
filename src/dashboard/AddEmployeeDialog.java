package dashboard;

import javax.swing.*;
import java.awt.*;
import java.util.function.Consumer;

public class AddEmployeeDialog extends JDialog {
    private JTextField idField = new JTextField();
    private JTextField nameField = new JTextField();
    private JTextField deptField = new JTextField();
    private JComboBox<String> statusBox = new JComboBox<>(new String[]{"Present","Absent","On Leave"});
    private JCheckBox salaryProcessedBox = new JCheckBox("Salary processed");

    public AddEmployeeDialog(Window owner, Consumer<Employee> onCreate) {
        super(owner, "Add New Employee", ModalityType.APPLICATION_MODAL);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(380, 360);
        setLocationRelativeTo(owner);

        JPanel root = new JPanel(new BorderLayout(12,12));
        root.setBorder(BorderFactory.createEmptyBorder(12,12,12,12));

        JPanel form = new JPanel(new GridBagLayout());
        GridBagConstraints gc = new GridBagConstraints();
        gc.insets = new Insets(6,6,6,6);
        gc.fill = GridBagConstraints.HORIZONTAL;
        gc.gridx = 0; gc.gridy = 0;

        form.add(new JLabel("EMPID"), gc);
        gc.gridx = 1; form.add(idField, gc);
        gc.gridx = 0; gc.gridy++;
        form.add(new JLabel("Name"), gc);
        gc.gridx = 1; form.add(nameField, gc);
        gc.gridx = 0; gc.gridy++;
        form.add(new JLabel("Department"), gc);
        gc.gridx = 1; form.add(deptField, gc);
        gc.gridx = 0; gc.gridy++;
        form.add(new JLabel("Status"), gc);
        gc.gridx = 1; form.add(statusBox, gc);
        gc.gridx = 1; gc.gridy++;
        form.add(salaryProcessedBox, gc);

        JPanel actions = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton cancel = new JButton("Cancel");
        JButton create = new JButton("Create");
        actions.add(cancel);
        actions.add(create);

        cancel.addActionListener(e -> dispose());
        create.addActionListener(e -> {
            String id = idField.getText().trim();
            String name = nameField.getText().trim();
            String dept = deptField.getText().trim();
            String status = (String) statusBox.getSelectedItem();
            boolean sp = salaryProcessedBox.isSelected();
            if (id.isEmpty() || name.isEmpty() || dept.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill EMPID, Name and Department.", "Validation", JOptionPane.WARNING_MESSAGE);
                return;
            }
            onCreate.accept(new Employee(id, name, dept, status, sp));
            dispose();
        });

        root.add(form, BorderLayout.CENTER);
        root.add(actions, BorderLayout.SOUTH);
        setContentPane(root);
    }
}
