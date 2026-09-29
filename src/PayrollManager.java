import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class PayrollManager {

    // Krataei statistika pliromis ana kathgoria proswpikou (staff_type)
    public static class PayStats {
        public String staffType;
        public double maxPay;
        public double minPay;
        public double avgPay;

        public PayStats(String staffType, double maxPay, double minPay, double avgPay) {
            this.staffType = staffType;
            this.maxPay = maxPay;
            this.minPay = minPay;
            this.avgPay = avgPay;
        }

        @Override
        public String toString() {
            return staffType + " -> MAX=" + maxPay + ", MIN=" + minPay + ", AVG=" + avgPay;
        }
    }

    // Base misthos gia monimous
    private static double baseSalaryForStaffType(String staffType) {
        if (staffType == null) return 0.0;
        String t = staffType.trim().toUpperCase();
        if (t.equals("DIOIKITIKOS")) return 1200.0;
        if (t.equals("DIDAKTIKOS")) return 1500.0;
        return 0.0;
    }

    // Vriskei to dm_id pou antistoixei se enan employee
    private long getDmIdForEmployee(Connection conn, int empId) throws SQLException {
        String sql = "SELECT dm_id FROM has_misth WHERE id_employee = ? LIMIT 1";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, empId);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) throw new SQLException("Den vrethike dm_id gia employee=" + empId);
                return rs.getLong(1);
            }
        }
    }

    // Svinei ola ta epidomata pou exoun syndethei sto sugkekrimeno dm_id
    private void clearAllowancesForDm(Connection conn, long dmId) throws SQLException {
        String sql = "DELETE FROM has_ep WHERE dm_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, dmId);
            ps.executeUpdate();
        }
    }

    // Ftiaxnei neo epidoma  kai to syndeei me dm_id
    private void addAllowance(Connection conn, long dmId, String epType, double epAmount) throws SQLException {
        String sqlInsEp = "INSERT INTO epidoma (ep_type, ep_amount) VALUES (?, ?)";
        String sqlLink = "INSERT INTO has_ep (dm_id, epidoma_id) VALUES (?, ?)";
        long epidomaId;

        try (PreparedStatement ps = conn.prepareStatement(sqlInsEp, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, epType);
            ps.setDouble(2, epAmount);
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (!rs.next()) throw new SQLException("Den paroxthike epidoma_id");
                epidomaId = rs.getLong(1);
            }
        }

        try (PreparedStatement ps = conn.prepareStatement(sqlLink)) {
            ps.setLong(1, dmId);
            ps.setLong(2, epidomaId);
            ps.executeUpdate();
        }
    }

    // Efarmozei epidomata me vasi gamo + paidia kanei refresh
    private void applyAllowances(Connection conn, long dmId, boolean married, int childrenCount) throws SQLException {
        clearAllowancesForDm(conn, dmId);
        if (married) addAllowance(conn, dmId, "MARRIAGE", 50.0);
        if (childrenCount > 0) addAllowance(conn, dmId, "CHILDREN", 30.0 * childrenCount);
    }

    // PROSLIPSI MONIMOU: bazei employee + dm/has_misth + epidomata + paidia child + has_child
    public void hirePermanentEmployee(String name, String address, String phone, String iban, String bank,
                                      boolean married, int childrenCount, String deptName, String roleType,
                                      List<LocalDate> childrenBirthDates) {

        String sqlEmployee =
                "INSERT INTO employee (name, home_address, start_date, marriage_status, phone_number, iban, bank_name, dept_name, children_count) " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        String sqlDm = "INSERT INTO dedomena_misthodosias (staff_type) VALUES (?)";
        String sqlLinkMisth = "INSERT INTO has_misth (id_employee, dm_id) VALUES (?, ?)";

        String sqlInsertChild = "INSERT INTO child (birth_date) VALUES (?)";
        String sqlLinkChild = "INSERT INTO has_child (child_id, id_employee) VALUES (?, ?)";

        Connection conn = null;

        try {
            conn = DBConnection.getConnection();
            if (conn == null) return;
            conn.setAutoCommit(false); // ola se ena transaction

            //an les oti exeis N paidia, prepei na steileis kai N hmeromhnies gennhshs
            if (childrenCount < 0) throw new SQLException("childrenCount < 0");
            if (childrenCount > 0) {
                if (childrenBirthDates == null) throw new SQLException("childrenBirthDates is null");
                if (childrenBirthDates.size() != childrenCount)
                    throw new SQLException("childrenCount != childrenBirthDates.size()");
            } else {
                childrenBirthDates = null;
            }

            String startDate = LocalDate.now().withDayOfMonth(1).toString(); // se varchar sto DB

            //Insert employee kai pare to generated id_employee
            long newEmpId;
            try (PreparedStatement psEmp = conn.prepareStatement(sqlEmployee, Statement.RETURN_GENERATED_KEYS)) {
                psEmp.setString(1, name);
                psEmp.setString(2, address);
                psEmp.setString(3, startDate);
                psEmp.setString(4, married ? "MARRIED" : "SINGLE");
                psEmp.setString(5, phone);
                psEmp.setString(6, iban);
                psEmp.setString(7, bank);
                psEmp.setString(8, deptName);
                psEmp.setInt(9, childrenCount);
                psEmp.executeUpdate();

                try (ResultSet keys = psEmp.getGeneratedKeys()) {
                    if (!keys.next()) throw new SQLException("Den paroxthike ID ypallilou");
                    newEmpId = keys.getLong(1);
                }
            }

            //Ftiaxe dedomena_misthodosias row (staff_type) kai pare dm_id
            long dmId;
            try (PreparedStatement psDm = conn.prepareStatement(sqlDm, Statement.RETURN_GENERATED_KEYS)) {
                psDm.setString(1, roleType); // p.x. "DIOIKITIKOS" h "DIDAKTIKOS"
                psDm.executeUpdate();
                try (ResultSet rs = psDm.getGeneratedKeys()) {
                    if (!rs.next()) throw new SQLException("Den paroxthike dm_id");
                    dmId = rs.getLong(1);
                }
            }

            //Sundei employee me dm_id
            try (PreparedStatement psLink = conn.prepareStatement(sqlLinkMisth)) {
                psLink.setLong(1, newEmpId);
                psLink.setLong(2, dmId);
                psLink.executeUpdate();
            }

            //Vale epidomata me vasi married/childrenCount
            applyAllowances(conn, dmId, married, childrenCount);

            //An yparxoun paidia, kane insert sto child kai meta link sto has_child
            if (childrenBirthDates != null && !childrenBirthDates.isEmpty()) {
                try (PreparedStatement psChild = conn.prepareStatement(sqlInsertChild, Statement.RETURN_GENERATED_KEYS);
                     PreparedStatement psHas = conn.prepareStatement(sqlLinkChild)) {

                    for (LocalDate bd : childrenBirthDates) {
                        psChild.clearParameters();
                        psChild.setString(1, bd.toString());
                        psChild.executeUpdate();

                        int childId;
                        try (ResultSet keys = psChild.getGeneratedKeys()) {
                            if (!keys.next()) throw new SQLException("Den paroxthike child_id");
                            childId = keys.getInt(1);
                        }

                        psHas.clearParameters();
                        psHas.setInt(1, childId);
                        psHas.setLong(2, newEmpId);
                        psHas.executeUpdate();
                    }
                }
            }

            conn.commit(); // αn ola pane kala, apothhkeuontai
            System.out.println("Epituxis proslipsi monimou " + name);

        } catch (SQLException e) {
            try {
                if (conn != null) conn.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
        } finally {
            try {
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    // PROSLIPSI SIMVASIOUXOU: employee + dm/has_misth + simvasi/has_simv + epidomata
    public void hireContractEmployee(String name, String address, String phone,
                                     String iban, String bank, boolean married,
                                     double salaryAmount, int durationMonths, String deptName, String roleType) {

        String sqlEmp =
                "INSERT INTO employee (name, home_address, phone_number, iban, bank_name, marriage_status, start_date, dept_name, children_count) " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        String sqlDm = "INSERT INTO dedomena_misthodosias (staff_type) VALUES (?)";
        String sqlHasMisth = "INSERT INTO has_misth (id_employee, dm_id) VALUES (?, ?)";

        String sqlSimv = "INSERT INTO simvasi (Start_date_SIMB, end_date_SIMB, salary_amount) VALUES (?, ?, ?)";
        String sqlHasSimv = "INSERT INTO has_simv (dm_id, contract_id) VALUES (?, ?)";

        Connection conn = null;

        try {
            conn = DBConnection.getConnection();
            if (conn == null) return;
            conn.setAutoCommit(false); // transaction

            //Insert employee
            long empId;
            try (PreparedStatement p = conn.prepareStatement(sqlEmp, Statement.RETURN_GENERATED_KEYS)) {
                p.setString(1, name);
                p.setString(2, address);
                p.setString(3, phone);
                p.setString(4, iban);
                p.setString(5, bank);
                p.setString(6, married ? "MARRIED" : "SINGLE");
                p.setString(7, LocalDate.now().withDayOfMonth(1).toString());
                p.setString(8, deptName);
                p.setInt(9, 0);
                p.executeUpdate();
                try (ResultSet rs = p.getGeneratedKeys()) {
                    if (!rs.next()) throw new SQLException("Den paroxthike empId");
                    empId = rs.getLong(1);
                }
            }

            //Insert dedomena_misthodosias me staff_type=CONTRACT
            long dmId;
            try (PreparedStatement p = conn.prepareStatement(sqlDm, Statement.RETURN_GENERATED_KEYS)) {
                p.setString(1, roleType);
                p.executeUpdate();
                try (ResultSet rs = p.getGeneratedKeys()) {
                    if (!rs.next()) throw new SQLException("Den paroxthike dmId");
                    dmId = rs.getLong(1);
                }
            }

            //Link employee-dm
            try (PreparedStatement p = conn.prepareStatement(sqlHasMisth)) {
                p.setLong(1, empId);
                p.setLong(2, dmId);
                p.executeUpdate();
            }

            //Insert simvasi kai link me has_simv
            long contractId;
            try (PreparedStatement p = conn.prepareStatement(sqlSimv, Statement.RETURN_GENERATED_KEYS)) {
                LocalDate start = LocalDate.now().withDayOfMonth(1);
                LocalDate end = start.plusMonths(durationMonths);

                p.setString(1, start.toString());
                p.setString(2, end.toString());
                p.setInt(3, (int) Math.round(salaryAmount));
                p.executeUpdate();

                try (ResultSet rs = p.getGeneratedKeys()) {
                    if (!rs.next()) throw new SQLException("Den paroxthike contractId");
                    contractId = rs.getLong(1);
                }
            }

            try (PreparedStatement p = conn.prepareStatement(sqlHasSimv)) {
                p.setLong(1, dmId);
                p.setLong(2, contractId);
                p.executeUpdate();
            }

            //Epidomata (mono marriage edw, giati children_count=0)
            applyAllowances(conn, dmId, married, 0);

            conn.commit();
            System.out.println("Epitixis Proslipsi Simvasiouxou: " + name);

        } catch (SQLException e) {
            try {
                if (conn != null) conn.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
        } finally {
            try {
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    // TREXEI MISTHODOSIA: gia kathe employee, ypologizei salary kai grafei pay rows
    public void runPayroll() {
        System.out.println("\nEKKINISI KATAVOLIS MISTHODOSIAS");

        String sqlAllEmp = "SELECT id_employee, name FROM employee";
        String insertPayData = "INSERT INTO dedomena_katavolwn_misthodosias (pay_date, pay_amount) VALUES (?, ?)";
        String insertLink = "INSERT INTO pay (id_employee, dkm_id) VALUES (?, ?)";

        try (Connection conn = DBConnection.getConnection()) {
            if (conn == null) return;

            try (PreparedStatement pstmt = conn.prepareStatement(sqlAllEmp);
                 ResultSet rs = pstmt.executeQuery()) {

                while (rs.next()) {
                    int id = rs.getInt("id_employee");
                    String name = rs.getString("name");

                    double amount = calculateSalary(id);
                    if (amount <= 0) continue;

                    long dkmId;
                    try (PreparedStatement payStmt = conn.prepareStatement(insertPayData, Statement.RETURN_GENERATED_KEYS)) {
                        LocalDate lastDay = LocalDate.now().withDayOfMonth(LocalDate.now().lengthOfMonth());
                        payStmt.setString(1, lastDay.toString());
                        payStmt.setString(2, String.valueOf(amount));
                        payStmt.executeUpdate();

                        try (ResultSet generatedKeys = payStmt.getGeneratedKeys()) {
                            if (!generatedKeys.next()) continue;
                            dkmId = generatedKeys.getLong(1);
                        }
                    }

                    try (PreparedStatement linkStmt = conn.prepareStatement(insertLink)) {
                        linkStmt.setInt(1, id);
                        linkStmt.setLong(2, dkmId);
                        linkStmt.executeUpdate();
                    }

                    System.out.printf("Pliromi: %s | Poso: %.2f EUR (Pay ID: %d)\n", name, amount, dkmId);
                }
            }

            System.out.println("H MISTHODOSIA OLOKLIROTHIKE");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Ypologizei mistho: contract -> simvasi, monimos -> baseSalaryForStaffType, + epidomata
    public double calculateSalary(int empId) {
        double totalAmount = 0.0;

        String sqlStaffType =
                "SELECT dm.staff_type " +
                        "FROM has_misth hm " +
                        "JOIN dedomena_misthodosias dm ON hm.dm_id = dm.dm_id " +
                        "WHERE hm.id_employee = ? LIMIT 1";

        String sqlContractBase =
                "SELECT s.salary_amount " +
                        "FROM has_misth hm " +
                        "JOIN dedomena_misthodosias dm ON hm.dm_id = dm.dm_id " +
                        "JOIN has_simv hs ON dm.dm_id = hs.dm_id " +
                        "JOIN simvasi s ON hs.contract_id = s.contract_id " +
                        "WHERE hm.id_employee = ?";

        String sqlEp =
                "SELECT e.ep_amount " +
                        "FROM has_misth hm " +
                        "JOIN dedomena_misthodosias dm ON hm.dm_id = dm.dm_id " +
                        "JOIN has_ep he ON dm.dm_id = he.dm_id " +
                        "JOIN epidoma e ON he.epidoma_id = e.epidoma_id " +
                        "WHERE hm.id_employee = ?";

        try (Connection conn = DBConnection.getConnection()) {
            if (conn == null) return 0.0;

            String staffType = null;
            try (PreparedStatement ps = conn.prepareStatement(sqlStaffType)) {
                ps.setInt(1, empId);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) staffType = rs.getString("staff_type");
                }
            }

            boolean isContract = staffType != null && staffType.trim().equalsIgnoreCase("CONTRACT");

            if (isContract) {
                try (PreparedStatement pstmt = conn.prepareStatement(sqlContractBase)) {
                    pstmt.setInt(1, empId);
                    try (ResultSet rs = pstmt.executeQuery()) {
                        if (rs.next()) totalAmount += rs.getDouble("salary_amount");
                    }
                }
            } else {
                totalAmount += baseSalaryForStaffType(staffType);
            }

            try (PreparedStatement pstmt = conn.prepareStatement(sqlEp)) {
                pstmt.setInt(1, empId);
                try (ResultSet rs = pstmt.executeQuery()) {
                    while (rs.next()) totalAmount += rs.getDouble("ep_amount");
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return totalAmount;
    }

    // Allazei address/phone/marriage kai kanei refresh ta epidomata me vasi ta stoixeia
    public void updateEmployeeDetails(int empId, String newAddress, String newPhone, boolean newMarriageStatus) {
        String sql = "UPDATE employee SET home_address = ?, phone_number = ?, marriage_status = ? WHERE id_employee = ?";

        try (Connection conn = DBConnection.getConnection()) {
            if (conn == null) return;
            conn.setAutoCommit(false);

            int childrenCount = 0;
            String sqlChildren = "SELECT IFNULL(children_count,0) AS cc FROM employee WHERE id_employee = ? LIMIT 1";
            try (PreparedStatement ps = conn.prepareStatement(sqlChildren)) {
                ps.setInt(1, empId);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) childrenCount = rs.getInt("cc");
                }
            }

            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, newAddress);
                pstmt.setString(2, newPhone);
                pstmt.setString(3, newMarriageStatus ? "MARRIED" : "SINGLE");
                pstmt.setInt(4, empId);
                int rows = pstmt.executeUpdate();
                if (rows <= 0) {
                    conn.rollback();
                    System.out.println("Den vrethike ypallilos me ID: " + empId);
                    return;
                }
            }

            long dmId = getDmIdForEmployee(conn, empId);
            applyAllowances(conn, dmId, newMarriageStatus, childrenCount);

            conn.commit();
            System.out.println("Epitixis enimerosi stoixeion gia ID: " + empId);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //kanei update to end_date_SIMB stin simvasi (mono gia contract)
    public void fireEmployee(int empId) {
        String sqlUpdate =
                "UPDATE simvasi s " +
                        "JOIN has_simv hs ON s.contract_id = hs.contract_id " +
                        "JOIN dedomena_misthodosias dm ON hs.dm_id = dm.dm_id " +
                        "JOIN has_misth hm ON dm.dm_id = hm.dm_id " +
                        "SET s.end_date_SIMB = ? " +
                        "WHERE hm.id_employee = ?";

        try (Connection conn = DBConnection.getConnection()) {
            if (conn == null) return;
            try (PreparedStatement pstmt = conn.prepareStatement(sqlUpdate)) {
                LocalDate lastDay = LocalDate.now().withDayOfMonth(LocalDate.now().lengthOfMonth());
                pstmt.setString(1, lastDay.toString());
                pstmt.setInt(2, empId);

                int rows = pstmt.executeUpdate();
                System.out.println(rows > 0 ? "O Ypallilos me ID " + empId + " apolythike."
                        : "Den vrethike simvasi gia ton ID: " + empId);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Allazei base salary sto simvasi (mono gia contract)
    public void updateBaseSalary(int empId, double newAmount) {
        String sqlUpdate =
                "UPDATE simvasi s " +
                        "JOIN has_simv hs ON s.contract_id = hs.contract_id " +
                        "JOIN dedomena_misthodosias dm ON hs.dm_id = dm.dm_id " +
                        "JOIN has_misth hm ON dm.dm_id = hm.dm_id " +
                        "SET s.salary_amount = ? " +
                        "WHERE hm.id_employee = ?";

        try (Connection conn = DBConnection.getConnection()) {
            if (conn == null) return;
            try (PreparedStatement pstmt = conn.prepareStatement(sqlUpdate)) {
                pstmt.setInt(1, (int) Math.round(newAmount));
                pstmt.setInt(2, empId);
                int rows = pstmt.executeUpdate();
                System.out.println(rows > 0 ? "O misthos allaxe gia ID: " + empId : "Apotixia (den vrethike).");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //min/max/avg gia DIOIKITIKOS kai DIDAKTIKOS apo ta pay records
    public List<PayStats> getMinMaxAvgForBothCategories() {
        String sql =
                "SELECT dm.staff_type, " +
                        "MAX(CAST(dkm.pay_amount AS DECIMAL(10,2))) AS MaxPayAmount, " +
                        "MIN(CAST(dkm.pay_amount AS DECIMAL(10,2))) AS MinPayAmount, " +
                        "AVG(CAST(dkm.pay_amount AS DECIMAL(10,2))) AS AvgPayAmount " +
                        "FROM employee e " +
                        "JOIN pay p ON e.id_employee = p.id_employee " +
                        "JOIN dedomena_katavolwn_misthodosias dkm ON p.dkm_id = dkm.dkm_id " +
                        "JOIN has_misth m ON m.id_employee = e.id_employee " +
                        "JOIN dedomena_misthodosias dm ON dm.dm_id = m.dm_id " +
                        "WHERE dm.staff_type IN (?, ?) " +
                        "GROUP BY dm.staff_type";

        List<PayStats> results = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection()) {
            if (conn == null) return results;

            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, "DIOIKITIKOS");
                ps.setString(2, "DIDAKTIKOS");
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        results.add(new PayStats(
                                rs.getString("staff_type"),
                                rs.getDouble("MaxPayAmount"),
                                rs.getDouble("MinPayAmount"),
                                rs.getDouble("AvgPayAmount")
                        ));
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return results;
    }


    //-- /katastasi misthodosias*/
    public void katastasimisthodosias() {
        System.out.println("\n--- katastasimisthodosias ---\n");
        String sql_1 = "SELECT DISTINCT \n" +
                "e.id_employee,\n" +
                "e.name, \n" +
                "dkm.pay_amount \n" +
                "FROM EMPLOYEE e " +
                "JOIN PAY p ON  e.id_employee = p.id_employee \n" +
                "JOIN DEDOMENA_KATAVOLWN_MISTHODOSIAS dkm ON dkm.dkm_id = p.dkm_id  " +
                "JOIN HAS_MISTH m ON  m.id_employee = e.id_employee\n" +
                "JOIN DEDOMENA_MISTHODOSIAS dm ON dm.dm_id = m.dm_id " +
                "AND dm.staff_type = 'Dioikitikos'\n";

        String sql_2 = "SELECT DISTINCT \n" +
                "e.id_employee,\n" +
                "e.name, \n" +
                "dkm.pay_amount \n" +
                "FROM EMPLOYEE e " +
                "JOIN PAY p ON  e.id_employee = p.id_employee \n" +
                "JOIN DEDOMENA_KATAVOLWN_MISTHODOSIAS dkm ON dkm.dkm_id = p.dkm_id  " +
                "JOIN HAS_MISTH m ON  m.id_employee = e.id_employee\n" +
                "JOIN DEDOMENA_MISTHODOSIAS dm ON dm.dm_id = m.dm_id " +
                "AND dm.staff_type = 'DIDAKTIKOS'\n";

        try (Connection conn = DBConnection.getConnection()) {
            System.out.println("\n--- Dioikitikos ---\n");
            try (PreparedStatement ps = conn.prepareStatement(sql_1)) {
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        System.out.println("employee_id: " + rs.getString("id_employee") +
                                ", employee_name: " + rs.getString("name") +
                                ", employee_pay_amount: " + rs.getString("pay_amount"));
                    }
                }
            }

            try (PreparedStatement ps = conn.prepareStatement(sql_2)) {
                System.out.println("\n--- DIDAKTIKOS ---\n");
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        System.out.println("employee_id: " + rs.getString("e.id_employee") +
                                ", employee_name: " + rs.getString("e.name") +
                                ", employee_pay_amount: " + rs.getString("dkm.pay_amount"));
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    //stixia_misth_ipalilu
    public void stixia_misth_ipalilu(int empId) {
        String sql_1 = "SELECT DISTINCT \n" +
                " e.* ,dkm.*\n" +
                "    from hy360_payroll_database.employee e, hy360_payroll_database.dedomena_katavolwn_misthodosias dkm , hy360_payroll_database.pay p\n" +
                "    where   p.id_employee =e.id_employee and p.dkm_id = dkm.dkm_id\n" +
                "    and e.id_employee = " + empId;
        try (Connection conn = DBConnection.getConnection()) {
            System.out.println("\n--- Employee details and misth ---\n");
            try (PreparedStatement ps = conn.prepareStatement(sql_1)) {
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        System.out.println("employee_id: " + empId);

                        for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
                            System.out.print(rs.getMetaData().getColumnLabel(i) + ": " + rs.getString(i) + ", ");
                            if (i % 4 == 0) System.out.println();
                        }
                    } else {
                        System.out.println("employee not found");
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public void total_pay_per_category() {
        String sql_1 = "select sum(dkm.pay_amount) as total_pay_per_Category\n" +
                "from dedomena_katavolwn_misthodosias dkm\n" +
                "join pay p on p.dkm_id = dkm.dkm_id\n" +
                "join employee e  on e.id_employee =  p.id_employee\n" +
                "join has_misth hm on e.id_employee = hm.id_employee \n" +
                "join dedomena_misthodosias dm on hm.dm_id = dm.dm_id \n" +
                "where dm.staff_type = 'Dioikitikos'";

        String sql_2 =
                "select sum(dkm.pay_amount) as total_pay_per_Category\n" +
                        "from dedomena_katavolwn_misthodosias dkm\n" +
                        "join pay p on p.dkm_id = dkm.dkm_id\n" +
                        "join employee e  on e.id_employee =  p.id_employee\n" +
                        "join has_misth hm on e.id_employee = hm.id_employee \n" +
                        "join dedomena_misthodosias dm on hm.dm_id = dm.dm_id \n" +
                        "where dm.staff_type = 'DIDAKTIKOS'";

        try (Connection conn = DBConnection.getConnection()) {
            System.out.println("\n--- Dioikitikos ---\n");
            try (PreparedStatement ps = conn.prepareStatement(sql_1)) {
                try (ResultSet rs = ps.executeQuery()) {
                    rs.next();
                        System.out.println("total pay = "+ rs.getString("total_pay_per_Category" ));

                }
            }

            try (PreparedStatement ps = conn.prepareStatement(sql_2)) {
                System.out.println("\n--- DIDAKTIKOS ---\n");
                try (ResultSet rs = ps.executeQuery()) {
                    rs.next();
                    System.out.println("total pay = "+ rs.getString("total_pay_per_Category" ));

                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    void printgetMinMaxAvgForBothCategories(){
        System.out.println("\n--- getMinMaxAvgForBothCategories ---");
        List<PayrollManager.PayStats> paystats = getMinMaxAvgForBothCategories();
        if(paystats.isEmpty())
        {
            System.out.println("no pay stats");
        }
        else{
            for(PayrollManager.PayStats ps : paystats)
            System.out.println("Staff Type: " + ps.staffType +"\navg Pay = "+ ps.avgPay+"\nmax Pay = " +ps.maxPay+"\nmin Pay = " +ps.minPay);
        }

    }

}
