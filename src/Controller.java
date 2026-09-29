import javax.swing.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.util.Objects;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
public class Controller {
    UI ui;
    PayrollManager manager;
    UI.popup_HireContractEmployee contrhire;
    UI.popup_HireEmployee hiremployee;
    UI.update updateemployee;
    UI.stixia_misth_ipalilu misth_ipalilu;

    public Controller(){
        ui = new UI();
        manager = new PayrollManager();

        SwingUtilities.invokeLater(this::setListeners);
    }

    public void setListeners(){
        ui.getButtonHire().addActionListener(new ButtonHandler());
        ui.getButtonHireContr().addActionListener(new ButtonHandler());
        ui.getButtonMinMax().addActionListener(new ButtonHandler());
        ui.getButtonPayroll().addActionListener(new ButtonHandler());
        ui.getButtonStixia().addActionListener(new ButtonHandler());
        ui.getButtonUpdate().addActionListener(new ButtonHandler());
        ui.getkatastasimisthodosias().addActionListener(new ButtonHandler());
        ui.gettotal_pay_per_category().addActionListener(new ButtonHandler());


    }
    public void setListeners_Hirepopup(){
        hiremployee.getvalidateHire().addActionListener(new ButtonHandler());
    }
    public void setListeners_HireContractpopup(){
        contrhire.getvalidateHire().addActionListener(new ButtonHandler());
    }

    public void setListeners_Updateemployee(){
        updateemployee.getconfirmupdate().addActionListener(new ButtonHandler());
    }

    public void setListeners_stixia_misth_ipalilu(){
        misth_ipalilu.getconfirm_stixia_misth_ipalilu().addActionListener(new ButtonHandler());
    }




    public class ButtonHandler implements ActionListener {


        public void actionPerformed(ActionEvent e) {

            System.out.println("click ");
            JButton but = ((JButton) e.getSource());
            System.out.println(but.getName());

            if (Objects.equals(but.getName(), "button_hire_employee")) {
                hiremployee = ui.new popup_HireEmployee()   ;
                setListeners_Hirepopup();

            }

            if (Objects.equals(but.getName(), "button_hire_contr")) {
                 contrhire = ui.new popup_HireContractEmployee()   ;
                setListeners_HireContractpopup();

            }

            if(Objects.equals(but.getName(), "button_update")){
                updateemployee = ui.new update()   ;
                setListeners_Updateemployee();

            }

            /*output*/

            if(Objects.equals(but.getName(), "button_payroll")){
                ui.payrolltext(manager);
            }

            if(Objects.equals(but.getName(),"katastasimisthodosias" )){
               ui.katastasimisthodosias_text( manager);
            }


            if(Objects.equals(but.getName(), "button_min_max")){
                ui.MinMaxAvgForBothCategories_text( manager);
            }

            if(Objects.equals(but.getName(), "button_stixia")){
                misth_ipalilu = ui.new stixia_misth_ipalilu()   ;
                setListeners_stixia_misth_ipalilu();
            }

            if(Objects.equals(but.getName(), "total_pay_per_category")){
                ui.total_pay_per_category_text( manager);
            }





            if (Objects.equals(but.getName(), "valid_Hire")) {
                // Children count validation
                int childrenCount = hiremployee.getinput_childrenCount();
                if (childrenCount < 0) {
                    showError("Children count cannot be negative");
                }
                else if (Validateinputs(hiremployee)  ) {
                    JOptionPane.showMessageDialog(null, "Valid input", "Valid result", JOptionPane.INFORMATION_MESSAGE);

                    List<LocalDate> childrenbirthdaylist = parseChildrenBirthdates();

                    if (childrenbirthdaylist != null) {
                        JOptionPane.showMessageDialog(null, "Valid input", "Valid result",
                                JOptionPane.INFORMATION_MESSAGE);

                    manager.hirePermanentEmployee(
                            hiremployee.getinput_name(),
                            hiremployee.getinput_address(),
                            hiremployee.getinput_phone(),
                            hiremployee.getinput_iban(),
                            hiremployee.getinput_bankname(),
                            hiremployee.getinput_married(),
                            hiremployee.getinput_childrenCount(),
                            hiremployee.getinput_deptName(),
                            hiremployee.getinput_role(),
                        childrenbirthdaylist
                      );

                    } else {
                        JOptionPane.showMessageDialog(null, "Invalid birthdate format",
                                "Invalid result", JOptionPane.ERROR_MESSAGE);
                    }

                } else {
                    JOptionPane.showMessageDialog(null, "Invalid input", "Invalid result", JOptionPane.ERROR_MESSAGE);
                }

            }


            if (Objects.equals(but.getName(), "valid_ContractHire")) {


                if (Validateinputs(contrhire)) {
                    JOptionPane.showMessageDialog(null, "Valid input", "Valid result", JOptionPane.INFORMATION_MESSAGE);

                        manager.hireContractEmployee(
                                contrhire.getinput_name(),
                                contrhire.getinput_address(),
                                contrhire.getinput_phone(),
                                contrhire.getinput_iban(),
                                contrhire.getinput_bankname(),
                                contrhire.getinput_married(),
                                contrhire.getinput_salaryAmount(),
                                contrhire.getinput_durationMonths(),
                                contrhire.getinput_deptName(),
                                contrhire.getinput_role()
                        );

                } else {
                    JOptionPane.showMessageDialog(null, "Invalid input", "Invalid result", JOptionPane.ERROR_MESSAGE);
                }

            }

            if(Objects.equals(but.getName(), "button_condirm_update"))
            {
                manager.updateEmployeeDetails(
                        updateemployee.getinput_employee_id(),
                        updateemployee.getinput_address(),
                        updateemployee.getinput_phone(),
                        updateemployee.getinput_married()
                );
            }

            if (Objects.equals(but.getName(), "confirm_stixia_misth_ipalilu")) {
                ui.stixia_misth_ipalilu_text( manager, misth_ipalilu);
            }

        }

    }





    private List<LocalDate> parseChildrenBirthdates() {
        List<LocalDate> childrenBirthdayList = new ArrayList<>();
        String input = hiremployee.getinput_childrenBirthdate().trim();

        // Handle empty input
        if (input.isEmpty()) {
            return childrenBirthdayList;
        }

        int childrenCount = hiremployee.getinput_childrenCount();
            String[] dates = input.split(","); // Expect: "1990-05-15,1992-03-20"

        // Validate count matches
        if (dates.length != childrenCount) {
            JOptionPane.showMessageDialog(null,
                    "Number of birthdates (" + dates.length + ") doesn't match children count (" + childrenCount + ")",
                    "Count Mismatch", JOptionPane.WARNING_MESSAGE);
            return null;
        }

        // Parse each date
        for (String date : dates) {
            try {
                LocalDate birthdate = LocalDate.parse(date.trim()); // Expected format: yyyy-MM-dd
                childrenBirthdayList.add(birthdate);
            } catch (java.time.format.DateTimeParseException e) {
                JOptionPane.showMessageDialog(null,
                        "Invalid date format: '" + date.trim() + "'. Use yyyy-MM-dd",
                        "Date Parse Error", JOptionPane.ERROR_MESSAGE);
                return null;
            }
        }

        return childrenBirthdayList;
    }


    private boolean Validateinputs(UI.popup_Hire hire) {
        // Name validation
        String name = hire.getinput_name().trim();
        if (name.isEmpty() || name.length() < 2) {
            showError("Name must be at least 2 characters long");
            return false;
        }

        // Phone validation (basic: digits and optional + or -)
        String phone = hire.getinput_phone().trim();
        if (!phone.matches("^[+]?[0-9\\-\\s]{7,}$")) {
            showError("Invalid phone number");
            return false;
        }

        // IBAN validation (basic: alphanumeric, 15-34 characters)
        String iban = hire.getinput_iban().trim();
        if (!iban.matches("^[A-Z]{2}[0-9]{1,30}$")) {
            showError("Invalid IBAN format");
            return false;
        }

        // Address validation
        String address = hire.getinput_address().trim();
        if (address.isEmpty() || address.length() < 2) {
            showError("Address must be at least 2 characters long");
            return false;
        }

        // Bank name validation
        String bankname = hire.getinput_bankname().trim();
        if (bankname.isEmpty()) {
            showError("Bank name cannot be empty");
            return false;
        }



        // Department and role validation
        String deptName = hire.getinput_deptName().trim();
        String role = hire.getinput_role().trim();
        if (deptName.isEmpty() || role.isEmpty()) {
            showError("Department and role cannot be empty");
            return false;
        }

        return true;
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(null, message, "Validation Error",
                JOptionPane.ERROR_MESSAGE);
    }

}