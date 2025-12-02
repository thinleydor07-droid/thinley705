package dashboard;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.net.URI;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

public class DashboardApp extends JFrame {
    // Theme colors (orange accents)
    private static final Color BG = new Color(0x0b1220);
    private static final Color PANEL = new Color(0x0b1220);
    private static final Color TEXT = new Color(0xe5e7eb);
    private static final Color MUTED = new Color(0x9ca3af);
    private static final Color ORANGE = new Color(0xf59e0b);
    private static final Color ORANGE_LIGHT = new Color(0xfbbf24);

    private final List<Employee> employees = new ArrayList<>(Arrays.asList(
            new Employee("EMP12345","Alice","Engineering","Present", true),
            new Employee("EMP67890","Bob","HR","Present", true),
            new Employee("EMP00001","Charlie","Finance","On Leave", false),
            new Employee("EMP00002","Dawa","Engineering","Absent", false),
            new Employee("EMP00003","Karma","Operations","Present", true)
    ));

    private JLabel kpiEmployees = new JLabel("0");
    private JLabel kpiDepartments = new JLabel("0");
    private JLabel kpiAttendance = new JLabel("0%");
    private JLabel kpiSalary = new JLabel("0%");

    private JProgressBar salaryProgress = new JProgressBar(0, 100);
    private JTable attendanceTable = new JTable();
    private JTable deptTable = new JTable();

    public DashboardApp() {
        super("Admin Dashboard");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1100, 700);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Sidebar
        JPanel sidebar = new JPanel();
        sidebar.setPreferredSize(new Dimension(220, 700));
        sidebar.setBackground(new Color(0x0a0f1c));
        sidebar.setBorder(BorderFactory.createMatteBorder(0,0,0,1,new Color(255,255,255,20)));
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));

        JPanel brand = new JPanel(new FlowLayout(FlowLayout.LEFT));
        brand.setOpaque(false);
        JLabel dot = new JLabel(" ");
        dot.setOpaque(true);
        dot.setBackground(ORANGE);
        dot.setPreferredSize(new Dimension(10,10));
        dot.setBorder(BorderFactory.createLineBorder(ORANGE_LIGHT,1));
        JLabel title = new JLabel("Admin");
        title.setForeground(TEXT);
        title.setFont(title.getFont().deriveFont(Font.BOLD, 16f));
        brand.add(dot); brand.add(title);
        brand.setBorder(BorderFactory.createEmptyBorder(12,12,12,12));

        sidebar.add(brand);
        sidebar.add(makeNavButton("Dashboard", true));
        sidebar.add(makeNavButton("Employees", false));
        sidebar.add(makeNavButton("Departments", false));
        sidebar.add(makeNavButton("Attendance", false));
        sidebar.add(makeNavButton("Payroll", false));
        sidebar.add(makeNavButton("Settings", false));

        // Content
        JPanel content = new JPanel(new BorderLayout());
        content.setBackground(BG);

        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(0x0a0f1c));
        header.setBorder(BorderFactory.createMatteBorder(0,0,1,0,new Color(245,158,11, 90)));
        JLabel hTitle = new JLabel("  Dashboard Overview");
        hTitle.setForeground(TEXT);
        hTitle.setFont(hTitle.getFont().deriveFont(Font.BOLD, 16f));
        JLabel welcome = new JLabel("Welcome, Admin");
        welcome.setForeground(MUTED);
        welcome.setBorder(BorderFactory.createEmptyBorder(0,0,0,12));
        header.add(hTitle, BorderLayout.WEST);
        header.add(welcome, BorderLayout.EAST);

        content.add(header, BorderLayout.NORTH);

        // KPI grid
        JPanel kpiGrid = new JPanel(new GridLayout(1,4,12,12));
        kpiGrid.setBorder(BorderFactory.createEmptyBorder(12,12,12,12));
        kpiGrid.setBackground(BG);
        kpiGrid.add(makeKpiCard("Total Employees", kpiEmployees));
        kpiGrid.add(makeKpiCard("Departments", kpiDepartments));
        kpiGrid.add(makeKpiCard("Attendance Today", kpiAttendance));
        kpiGrid.add(makeKpiCard("Salary Processed", kpiSalary));

        // Row: Salary progress + Attendance table
        JPanel row = new JPanel(new GridLayout(1,2,12,12));
        row.setBorder(BorderFactory.createEmptyBorder(0,12,12,12));
        row.setBackground(BG);

        JPanel salaryPanel = makePanel("Salary Processing Progress");
        salaryProgress.setForeground(ORANGE);
        salaryProgress.setBackground(new Color(0x111827));
        salaryPanel.add(salaryProgress);
        JLabel salaryPctLbl = new JLabel("0% of salaries processed this month");
        salaryPctLbl.setForeground(MUTED);
        salaryPanel.add(salaryPctLbl);

        JPanel attendancePanel = makePanel("Attendance Summary (Today)");
        attendanceTable.setModel(new DefaultTableModel(new Object[][]{}, new String[]{"Status","Count"}){
            public boolean isCellEditable(int r,int c){return false;}
        });
        attendancePanel.setLayout(new BorderLayout());
        attendancePanel.add(new JScrollPane(attendanceTable), BorderLayout.CENTER);

        row.add(salaryPanel);
        row.add(attendancePanel);

        // Departments panel
        JPanel deptPanel = makePanel("Departments");
        JPanel actions = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton addBtn = new JButton("ADD NEW Employee");
        JButton readBtn = new JButton("Read");
        JButton updateBtn = new JButton("UPDATE EMPLOYEE DETAILS");
        JButton deleteBtn = new JButton("Delete");
        styleActionButton(addBtn, true);
        styleActionButton(readBtn, false);
        styleActionButton(updateBtn, false);
        styleActionButton(deleteBtn, false);
        actions.add(addBtn); actions.add(readBtn); actions.add(updateBtn); actions.add(deleteBtn);

        deptPanel.setLayout(new BorderLayout());
        deptPanel.add(actions, BorderLayout.NORTH);
        deptTable.setModel(new DefaultTableModel(new Object[][]{}, new String[]{"Department","Headcount","Trend"}){
            public boolean isCellEditable(int r,int c){return false;}
        });
        deptPanel.add(new JScrollPane(deptTable), BorderLayout.CENTER);

        JPanel mainCenter = new JPanel(new BorderLayout());
        mainCenter.setBackground(BG);
        mainCenter.add(kpiGrid, BorderLayout.NORTH);
        mainCenter.add(row, BorderLayout.CENTER);
        mainCenter.add(deptPanel, BorderLayout.SOUTH);

        content.add(mainCenter, BorderLayout.CENTER);

        add(sidebar, BorderLayout.WEST);
        add(content, BorderLayout.CENTER);

        // Button actions
        addBtn.addActionListener(this::onAdd);
        readBtn.addActionListener(e -> onRead());
        updateBtn.addActionListener(e -> onUpdate());
        deleteBtn.addActionListener(e -> onDelete());

        refreshAll();
    }

    private void styleActionButton(JButton b, boolean primary){
        b.setFocusPainted(false);
        b.setForeground(primary ? ORANGE_LIGHT : TEXT);
        b.setBackground(new Color(0x0f172a));
        b.setBorder(BorderFactory.createLineBorder(primary ? new Color(245,158,11,130) : new Color(255,255,255,25)));
    }

    private JPanel makeKpiCard(String title, JLabel value){
        JPanel card = new JPanel();
        card.setBackground(PANEL);
        card.setBorder(BorderFactory.createLineBorder(new Color(255,255,255,25)));
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        JLabel t = new JLabel(title);
        t.setForeground(MUTED);
        t.setBorder(BorderFactory.createEmptyBorder(8,8,0,8));
        value.setForeground(TEXT);
        value.setFont(value.getFont().deriveFont(Font.BOLD, 22f));
        value.setBorder(BorderFactory.createEmptyBorder(6,8,8,8));
        card.add(t); card.add(value);
        return card;
    }

    private JPanel makePanel(String title){
        JPanel p = new JPanel();
        p.setBackground(PANEL);
        p.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(255,255,255,25)),
                BorderFactory.createEmptyBorder(10,10,10,10)
        ));
        p.setLayout(new FlowLayout(FlowLayout.LEFT));
        JLabel t = new JLabel(title);
        t.setForeground(TEXT);
        t.setFont(t.getFont().deriveFont(Font.BOLD, 14f));
        p.add(t);
        return p;
    }

    private JButton makeNavButton(String text, boolean active){
        JButton b = new JButton(text);
        b.setHorizontalAlignment(SwingConstants.LEFT);
        b.setFocusPainted(false);
        b.setForeground(TEXT);
        b.setBackground(new Color(0x0f172a));
        b.setBorder(BorderFactory.createEmptyBorder(8,12,8,12));
        if (active) {
            b.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(245,158,11,100)),
                    BorderFactory.createEmptyBorder(8,12,8,12)
            ));
        }
        return b;
    }

    private void refreshAll(){
        int total = employees.size();
        Set<String> departments = employees.stream().map(e->e.dept).collect(Collectors.toCollection(LinkedHashSet::new));
        long present = employees.stream().filter(e->"Present".equals(e.status)).count();
        long absent = employees.stream().filter(e->"Absent".equals(e.status)).count();
        long onLeave = employees.stream().filter(e->"On Leave".equals(e.status)).count();
        long processed = employees.stream().filter(e->e.salaryProcessed).count();

        int attendancePct = total == 0 ? 0 : Math.round( (present * 100f) / total);
        int salaryPct = total == 0 ? 0 : Math.round( (processed * 100f) / total);

        kpiEmployees.setText(String.valueOf(total));
        kpiDepartments.setText(String.valueOf(departments.size()));
        kpiAttendance.setText(attendancePct + "%");
        kpiSalary.setText(salaryPct + "%");

        salaryProgress.setValue(salaryPct);

        // attendance table
        DefaultTableModel attModel = (DefaultTableModel) attendanceTable.getModel();
        attModel.setRowCount(0);
        attModel.addRow(new Object[]{"Present", present});
        attModel.addRow(new Object[]{"Absent", absent});
        attModel.addRow(new Object[]{"On Leave", onLeave});

        // department table
        Map<String, Long> counts = employees.stream()
                .collect(Collectors.groupingBy(e->e.dept, LinkedHashMap::new, Collectors.counting()));
        List<Map.Entry<String, Long>> sorted = new ArrayList<>(counts.entrySet());
        sorted.sort((a,b)-> Long.compare(b.getValue(), a.getValue()));

        DefaultTableModel dModel = (DefaultTableModel) deptTable.getModel();
        dModel.setRowCount(0);
        for (Map.Entry<String, Long> e : sorted) {
            long c = e.getValue();
            String trend = c > 3 ? "Growing" : (c == 3 ? "Stable" : "Hiring");
            dModel.addRow(new Object[]{ e.getKey(), c, trend });
        }
    }

    private void onAdd(ActionEvent e){
        // Open existing add-employee.html in browser
        try {
            File base = new File(".").getCanonicalFile();
            File html = new File(base, "add-employee.html");
            if (html.exists()) {
                Desktop.getDesktop().browse(html.toURI());
            } else {
                int opt = JOptionPane.showConfirmDialog(this, "add-employee.html not found. Add here in the app instead?", "Open Form", JOptionPane.YES_NO_OPTION);
                if (opt == JOptionPane.YES_OPTION) {
                    new AddEmployeeDialog(this, emp -> {
                        // check unique id
                        if (employees.stream().anyMatch(x->x.id.equals(emp.id))) {
                            JOptionPane.showMessageDialog(this, "Employee ID already exists.", "Validation", JOptionPane.WARNING_MESSAGE);
                            return;
                        }
                        employees.add(emp);
                        refreshAll();
                    }).setVisible(true);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Failed to open form: " + ex.getMessage());
        }
    }

    private void onRead(){
        String id = JOptionPane.showInputDialog(this, "Enter Employee ID to view:");
        if (id == null || id.isEmpty()) return;
        Employee emp = employees.stream().filter(x->x.id.equals(id)).findFirst().orElse(null);
        if (emp == null) {
            JOptionPane.showMessageDialog(this, "Not found");
            return;
        }
        JOptionPane.showMessageDialog(this, String.format("ID: %s\nName: %s\nDepartment: %s\nStatus: %s\nSalary Processed: %s",
                emp.id, emp.name, emp.dept, emp.status, emp.salaryProcessed?"Yes":"No"));
    }

    private void onUpdate(){
        String id = JOptionPane.showInputDialog(this, "Enter Employee ID to update:");
        if (id == null || id.isEmpty()) return;
        Employee emp = employees.stream().filter(x->x.id.equals(id)).findFirst().orElse(null);
        if (emp == null) { JOptionPane.showMessageDialog(this, "Not found"); return; }
        new UpdateEmployeeDialog(this, emp, this::refreshAll).setVisible(true);
    }

    private void onDelete(){
        String id = JOptionPane.showInputDialog(this, "Enter Employee ID to delete:");
        if (id == null || id.isEmpty()) return;
        int idx = -1;
        for (int i=0;i<employees.size();i++) if (employees.get(i).id.equals(id)) { idx = i; break; }
        if (idx == -1) { JOptionPane.showMessageDialog(this, "Not found"); return; }
        int c = JOptionPane.showConfirmDialog(this, "Delete employee "+id+"?", "Confirm", JOptionPane.YES_NO_OPTION);
        if (c == JOptionPane.YES_OPTION) {
            employees.remove(idx);
            refreshAll();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ignored) {}
            new DashboardApp().setVisible(true);
        });
    }
}
