import javax.swing.*;
import java.awt.*;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

public class UI extends JFrame {
    private static JButton button_update;
    private static JButton button_hire;
    private static JButton button_hire_contr;
    private static JButton button_payroll;
    private static JButton katastasimisthodosias;

    private static JButton button_min_max;
    private static JButton button_stixia;
    private static JButton total_pay_per_category;


    UI() {

        JFrame frame = new JFrame("A Simple Frame");

        frame.setBounds(300, 300, 920, 150);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


        button_hire = new JButton("Hire Permanent Employee");
        button_hire.setBounds(10, 10, 200, 20);
        button_hire.setName("button_hire_employee");

        frame.add(button_hire);

        button_hire_contr = new JButton("Hire Contract Employee");
        button_hire_contr.setBounds(230, 10, 200, 20);
        button_hire_contr.setName("button_hire_contr");

        frame.add(button_hire_contr);

        button_update = new JButton("Update Employee Details");
        button_update.setBounds(480, 10, 200, 20);
        button_update.setName("button_update");

        frame.add(button_update);

        button_payroll = new JButton("Payroll status");
        button_payroll.setBounds(700, 10, 200, 20);
        button_payroll.setName("button_payroll");

        frame.add(button_payroll);

        katastasimisthodosias = new JButton("katastasi misthodosias");
        katastasimisthodosias.setBounds(10, 50, 200, 20);
        katastasimisthodosias.setName("katastasimisthodosias");

        frame.add(katastasimisthodosias);

        button_min_max = new JButton("Min Max Avg For Both Categories");
        button_min_max.setBounds(230, 50, 230, 20);
        button_min_max.setName("button_min_max");

        frame.add(button_min_max);

        button_stixia = new JButton("stixia misthoy ipalilu");
        button_stixia.setBounds(480, 50, 200, 20);
        button_stixia.setName("button_stixia");

        frame.add(button_stixia);

        total_pay_per_category = new JButton("total pay per category");
        total_pay_per_category.setBounds(700, 50, 200, 20);
        total_pay_per_category.setName("total_pay_per_category");

        frame.add(total_pay_per_category);

        frame.setLayout(null);
        frame.setVisible(true);

    }

    public JButton getButtonUpdate() {
        return button_update;
    }

    public JButton getButtonHire() {
        return button_hire;
    }

    public JButton getButtonHireContr() {
        return button_hire_contr;
    }

    public JButton getButtonPayroll() {
        return button_payroll;
    }

    public JButton getButtonMinMax() {
        return button_min_max;
    }

    public JButton getButtonStixia() {
        return button_stixia;
    }

    public JButton getkatastasimisthodosias() {
        return katastasimisthodosias;
    }

    public JButton gettotal_pay_per_category() {
        return total_pay_per_category;
    }

    public class popup_HireEmployee extends popup_Hire {

        private JTextField
                input_childrenCount,
                input_childrenBirthdate;

        popup_HireEmployee() {
            super();

            input_childrenCount = new JTextField(20);

            input_childrenBirthdate = new JTextField(20);

            validateHire = new JButton("Hire");
            validateHire.setName("valid_Hire");

            panel.add(add(new JLabel("  childrenCount:")));
            panel.add(input_childrenCount);


            panel.add(add(new JLabel("  childrenBirthdate ( \"yyyy-MM-dd\" ):")));
            panel.add(input_childrenBirthdate);

            frame.add(panel, BorderLayout.CENTER);

            JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
            buttonPanel.add(validateHire);
            frame.add(buttonPanel, BorderLayout.SOUTH);


            frame.setVisible(true);
        }

        public String getinput_childrenBirthdate() {
            return input_childrenBirthdate.getText().trim();
        }

        public int getinput_childrenCount() {
            try {
                return Integer.parseInt(input_childrenCount.getText().trim());
            } catch (NumberFormatException e) {
                return -1;
            }
        }


    }


    public class popup_Hire {

        protected JTextField input_name,
                input_address,
                input_phone,
                input_iban,
                input_bankname,
                input_married,
                input_deptName,
                input_role;

        protected JButton validateHire;
        protected JFrame frame;
        protected JPanel panel;


        popup_Hire() {
            frame = new JFrame("Hire");
            frame.setBounds(300, 300, 900, 400);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            panel = new JPanel(new GridLayout(11, 2, 10, 10));

            input_name = new JTextField(20);
            input_address = new JTextField(20);
            input_phone = new JTextField(20);
            input_iban = new JTextField(20);
            input_bankname = new JTextField(20);
            input_married = new JTextField(20);
            input_deptName = new JTextField(20);
            input_role = new JTextField(20);

            panel.add(add(new JLabel("  Name:")));
            panel.add(input_name);
            panel.add(add(new JLabel("  Address:")));
            panel.add(input_address);
            panel.add(add(new JLabel("  phone:")));
            panel.add(input_phone);
            panel.add(add(new JLabel("  iban:")));
            panel.add(input_iban);
            panel.add(add(new JLabel("  input_bankname:")));
            panel.add(input_bankname);
            panel.add(add(new JLabel("  Married (yes/no):")));
            panel.add(input_married);
            panel.add(add(new JLabel("  deptName:")));
            panel.add(input_deptName);
            panel.add(add(new JLabel("  role:")));
            panel.add(input_role);

        }


        public String getinput_name() {
            return input_name.getText().trim();
        }

        public String getinput_address() {
            return input_address.getText().trim();
        }

        public String getinput_phone() {
            return input_phone.getText().trim();
        }

        public String getinput_iban() {
            return input_iban.getText().trim();
        }

        public String getinput_bankname() {
            return input_bankname.getText().trim();
        }

        public Boolean getinput_married() {
            String input = input_married.getText().trim().toLowerCase();
            return input.equals("yes") ? true : input.equals("no") ? false : null;
        }

        public String getinput_deptName() {
            return input_deptName.getText().trim();
        }

        public String getinput_role() {
            return input_role.getText().trim();
        }

        public JButton getvalidateHire() {
            return validateHire;
        }

    }


    public class popup_HireContractEmployee extends popup_Hire {
        protected JTextField input_salaryAmount, input_durationMonths;

        popup_HireContractEmployee() {
            super();

            input_salaryAmount = new JTextField(20);
            input_durationMonths = new JTextField(20);

            panel.add(add(new JLabel("  Salary ammount:")));
            panel.add(input_salaryAmount);
            panel.add(add(new JLabel("  Duration:")));
            panel.add(input_durationMonths);

            frame.add(panel, BorderLayout.CENTER);

            validateHire = new JButton("Hire");
            validateHire.setName("valid_ContractHire");

            JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
            buttonPanel.add(validateHire);
            frame.add(buttonPanel, BorderLayout.SOUTH);

            frame.setVisible(true);
        }

        public float getinput_salaryAmount() {
            try {
                return Float.parseFloat(input_salaryAmount.getText().trim());
            } catch (NumberFormatException e) {
                return -1;
            }
        }

        public int getinput_durationMonths() {
            try {
                return Integer.parseInt(input_durationMonths.getText().trim());
            } catch (NumberFormatException e) {
                return -1;
            }
        }
    }


    public class update {
        private JTextField employee_id,
                input_address,
                input_phone,
                input_married;

        private JButton confirmupdate;
        private JFrame frame;
        private JPanel panel;

        update() {
            frame = new JFrame("Update details");
            frame.setBounds(300, 300, 900, 400);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            panel = new JPanel(new GridLayout(11, 2, 10, 10));

            employee_id = new JTextField(20);
            input_address = new JTextField(20);
            input_phone = new JTextField(20);
            input_married = new JTextField(20);

            panel.add(add(new JLabel("  Employee ID:")));
            panel.add(employee_id);
            panel.add(add(new JLabel("  Address:")));
            panel.add(input_address);
            panel.add(add(new JLabel("  phone:")));
            panel.add(input_phone);
            panel.add(add(new JLabel("  Married (yes/no):")));
            panel.add(input_married);

            confirmupdate = new JButton("button_condirm_update");
            confirmupdate.setName("button_condirm_update");

            frame.add(panel, BorderLayout.CENTER);

            JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
            buttonPanel.add(confirmupdate);
            frame.add(buttonPanel, BorderLayout.SOUTH);

            frame.setVisible(true);

        }

        public Integer getinput_employee_id() {
            return Integer.parseInt(employee_id.getText().trim());
        }

        public String getinput_address() {
            return input_address.getText().trim();
        }

        public String getinput_phone() {
            return input_phone.getText().trim();
        }

        public Boolean getinput_married() {
            String input = input_married.getText().trim().toLowerCase();
            return input.equals("yes") ? true : input.equals("no") ? false : null;
        }

        JButton getconfirmupdate() {
            return confirmupdate;
        }
    }


    public class stixia_misth_ipalilu {
        private JTextField employee_id;

        private JButton confirm_stixia_misth_ipalilu;
        private JFrame frame;
        private JPanel panel;

        stixia_misth_ipalilu() {
            frame = new JFrame("Update details");
            frame.setBounds(300, 300, 900, 400);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            panel = new JPanel(new GridLayout(11, 2, 10, 10));

            employee_id = new JTextField(20);

            panel.add(add(new JLabel("  Employee ID:")));
            panel.add(employee_id);


            confirm_stixia_misth_ipalilu = new JButton("confirm_stixia_misth_ipalilu");
            confirm_stixia_misth_ipalilu.setName("confirm_stixia_misth_ipalilu");

            frame.add(panel, BorderLayout.CENTER);

            JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
            buttonPanel.add(confirm_stixia_misth_ipalilu);
            frame.add(buttonPanel, BorderLayout.SOUTH);

            frame.setVisible(true);

        }

        public Integer getinput_employee_id() {
            return Integer.parseInt(employee_id.getText().trim());
        }

        JButton getconfirm_stixia_misth_ipalilu() {
            return confirm_stixia_misth_ipalilu;
        }
    }

    void payrolltext(PayrollManager manager)
    {
        JTextArea textArea;

        setTitle("Payroll Manager");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        textArea = setuptext();

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos);
        PrintStream old = System.out;
        System.setOut(ps);

        manager.runPayroll();

        System.setOut(old);
        textArea.setText(baos.toString());


        setVisible(true);
    }


    void katastasimisthodosias_text(PayrollManager manager)
    {
        JTextArea textArea;

        setTitle("katastasi misthodosias");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        textArea = setuptext();

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos);
        PrintStream old = System.out;
        System.setOut(ps);

        manager.katastasimisthodosias();

        System.setOut(old);
        textArea.setText(baos.toString());


        setVisible(true);
    }

    JTextArea setuptext(){
        JTextArea textArea;
        textArea = new JTextArea();
        textArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(textArea);
        add(scrollPane);

        return textArea;
    }


    void MinMaxAvgForBothCategories_text(PayrollManager manager)
    {
        JTextArea textArea;

        setTitle("Min Max Avg For Both Categories");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        textArea = setuptext();

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos);
        PrintStream old = System.out;
        System.setOut(ps);

        manager.printgetMinMaxAvgForBothCategories();

        System.setOut(old);
        textArea.setText(baos.toString());


        setVisible(true);
    }


    void total_pay_per_category_text(PayrollManager manager)
    {
        JTextArea textArea;

        setTitle("Min Max Avg For Both Categories");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        textArea = setuptext();

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos);
        PrintStream old = System.out;
        System.setOut(ps);

        manager.total_pay_per_category();

        System.setOut(old);
        textArea.setText(baos.toString());


        setVisible(true);
    }


    void stixia_misth_ipalilu_text(PayrollManager manager,stixia_misth_ipalilu misth_ipalilu)
    {
        JTextArea textArea;

        setTitle("Min Max Avg For Both Categories");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        textArea = setuptext();

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos);
        PrintStream old = System.out;
        System.setOut(ps);

        manager.stixia_misth_ipalilu(
                misth_ipalilu.getinput_employee_id()
        );

        System.setOut(old);
        textArea.setText(baos.toString());


        setVisible(true);
    }


}






