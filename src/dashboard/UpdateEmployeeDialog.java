package dashboard;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;

public class UpdateEmployeeDialog extends JDialog {
    private final JTextField idField = new JTextField();
    private final JTextField nameField = new JTextField();
    private final JTextField deptField = new JTextField();
    private final JComboBox<String> statusBox = new JComboBox<>(new String[]{"Present","Absent","On Leave"});
    private final JCheckBox salaryProcessedBox = new JCheckBox("Salary processed");

    public UpdateEmployeeDialog(Window owner, Employee employee, Runnable onSave) {
        super(owner, "Update Employee", ModalityType.APPLICATION_MODAL);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(400, 360);
        setLocationRelativeTo(owner);

        // Prefill
        idField.setText(employee.id);
        idField.setEditable(false);
        nameField.setText(employee.name);
        deptField.setText(employee.dept);
        statusBox.setSelectedItem(employee.status);
        salaryProcessedBox.setSelected(employee.salaryProcessed);

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
        JButton save = new JButton("Save");
        actions.add(cancel);
        actions.add(save);

        cancel.addActionListener(e -> dispose());
        save.addActionListener(e -> {
            String name = nameField.getText().trim();
            String dept = deptField.getText().trim();
            String status = Objects.toString(statusBox.getSelectedItem(), employee.status);
            boolean sp = salaryProcessedBox.isSelected();
            if (name.isEmpty() || dept.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill Name and Department.", "Validation", JOptionPane.WARNING_MESSAGE);
                return;
            }
            employee.name = name;
            employee.dept = dept;
            employee.status = status;
            employee.salaryProcessed = sp;
            onSave.run();
            dispose();
        });

        root.add(form, BorderLayout.CENTER);
        root.add(actions, BorderLayout.SOUTH);
        setContentPane(root);
    }
}
