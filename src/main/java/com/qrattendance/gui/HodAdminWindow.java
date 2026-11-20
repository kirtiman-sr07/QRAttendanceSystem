package com.qrattendance.gui;

import com.qrattendance.db.DatabaseManager;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.Separator;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class HodAdminWindow {

    private static final String URL = "jdbc:sqlite:attendance.db";
    private static final String HOD_USER = "hod";
    private static final String HOD_PASS = "admin123";

    public static class AttendanceRow {
        private final SimpleIntegerProperty logId;
        private final SimpleIntegerProperty classId;
        private final SimpleStringProperty subject;
        private final SimpleStringProperty rollNo;
        private final SimpleStringProperty studentName;
        private final SimpleStringProperty status;
        private final SimpleStringProperty timestamp;

        public AttendanceRow(int logId, int classId, String subject, String rollNo, String studentName, String status, String timestamp) {
            this.logId = new SimpleIntegerProperty(logId);
            this.classId = new SimpleIntegerProperty(classId);
            this.subject = new SimpleStringProperty(subject);
            this.rollNo = new SimpleStringProperty(rollNo);
            this.studentName = new SimpleStringProperty(studentName);
            this.status = new SimpleStringProperty(status);
            this.timestamp = new SimpleStringProperty(timestamp);
        }

        public int getLogId() { return logId.get(); }
        public int getClassId() { return classId.get(); }
        public String getSubject() { return subject.get(); }
        public String getRollNo() { return rollNo.get(); }
        public String getStudentName() { return studentName.get(); }
        public String getStatus() { return status.get(); }
        public String getTimestamp() { return timestamp.get(); }
    }

    public static class AuditRow {
        private final SimpleIntegerProperty auditId;
        private final SimpleIntegerProperty classId;
        private final SimpleStringProperty rollNo;
        private final SimpleStringProperty oldStatus;
        private final SimpleStringProperty newStatus;
        private final SimpleStringProperty changedBy;
        private final SimpleStringProperty reason;
        private final SimpleStringProperty changedAt;

        public AuditRow(int auditId, int classId, String rollNo, String oldStatus, String newStatus, String changedBy, String reason, String changedAt) {
            this.auditId = new SimpleIntegerProperty(auditId);
            this.classId = new SimpleIntegerProperty(classId);
            this.rollNo = new SimpleStringProperty(rollNo);
            this.oldStatus = new SimpleStringProperty(oldStatus);
            this.newStatus = new SimpleStringProperty(newStatus);
            this.changedBy = new SimpleStringProperty(changedBy);
            this.reason = new SimpleStringProperty(reason);
            this.changedAt = new SimpleStringProperty(changedAt);
        }

        public int getAuditId() { return auditId.get(); }
        public int getClassId() { return classId.get(); }
        public String getRollNo() { return rollNo.get(); }
        public String getOldStatus() { return oldStatus.get(); }
        public String getNewStatus() { return newStatus.get(); }
        public String getChangedBy() { return changedBy.get(); }
        public String getReason() { return reason.get(); }
        public String getChangedAt() { return changedAt.get(); }
    }

    public static void openLoginWindow() {
        Stage stage = new Stage();
        stage.setTitle("HOD / Admin Portal Login");

        Label heading = new Label("HOD / Administrative Login");
        heading.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        Label hint = new Label("Demo Credentials: hod / admin123");
        hint.setStyle("-fx-font-size: 12px; -fx-text-fill: #7f8c8d;");

        TextField userField = new TextField();
        userField.setPromptText("Admin Username (e.g., hod)");

        PasswordField passField = new PasswordField();
        passField.setPromptText("Admin Password");

        Label errorLabel = new Label();
        errorLabel.setStyle("-fx-text-fill: #c0392b;");

        Button loginBtn = new Button("Sign In to Admin Panel");
        loginBtn.setMaxWidth(Double.MAX_VALUE);

        Runnable handleLogin = () -> {
            String u = userField.getText().trim();
            String p = passField.getText().trim();
            if (HOD_USER.equalsIgnoreCase(u) && HOD_PASS.equals(p)) {
                showDashboard(stage, u.toUpperCase());
            } else {
                errorLabel.setText("Invalid HOD credentials. Try hod / admin123.");
            }
        };

        loginBtn.setOnAction(e -> handleLogin.run());
        passField.setOnAction(e -> handleLogin.run());

        VBox layout = new VBox(12, heading, hint, userField, passField, loginBtn, errorLabel);
        layout.setPadding(new Insets(24));
        layout.setAlignment(Pos.CENTER);

        stage.setScene(new Scene(layout, 360, 270));
        stage.show();
    }

    private static void showDashboard(Stage stage, String adminUser) {
        stage.setTitle("HOD Attendance Control & Audit Trail (" + adminUser + ")");

        ObservableList<AttendanceRow> attendanceData = FXCollections.observableArrayList();
        ObservableList<AuditRow> auditData = FXCollections.observableArrayList();

        // --- Filter Controls ---
        TextField dateFilterField = new TextField();
        dateFilterField.setPromptText("Filter Date (yyyy-MM-dd)");

        TextField subjectFilterField = new TextField();
        subjectFilterField.setPromptText("Filter Subject (e.g., JAVA_101)");

        Button filterBtn = new Button("Apply Filters");
        Button resetFilterBtn = new Button("Reset");

        HBox filterBar = new HBox(8, dateFilterField, subjectFilterField, filterBtn, resetFilterBtn);
        filterBar.setAlignment(Pos.CENTER_LEFT);

        // --- Attendance Logs Table ---
        TableView<AttendanceRow> logsTable = new TableView<>(attendanceData);
        logsTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<AttendanceRow, Number> colLogId = new TableColumn<>("Log ID");
        colLogId.setCellValueFactory(c -> c.getValue().logId);

        TableColumn<AttendanceRow, Number> colSession = new TableColumn<>("Session");
        colSession.setCellValueFactory(c -> c.getValue().classId);

        TableColumn<AttendanceRow, String> colSubject = new TableColumn<>("Subject");
        colSubject.setCellValueFactory(c -> c.getValue().subject);

        TableColumn<AttendanceRow, String> colRoll = new TableColumn<>("Roll No");
        colRoll.setCellValueFactory(c -> c.getValue().rollNo);

        TableColumn<AttendanceRow, String> colName = new TableColumn<>("Student Name");
        colName.setCellValueFactory(c -> c.getValue().studentName);

        TableColumn<AttendanceRow, String> colStatus = new TableColumn<>("Status");
        colStatus.setCellValueFactory(c -> c.getValue().status);

        TableColumn<AttendanceRow, String> colTime = new TableColumn<>("Timestamp");
        colTime.setCellValueFactory(c -> c.getValue().timestamp);

        logsTable.getColumns().addAll(colLogId, colSession, colSubject, colRoll, colName, colStatus, colTime);

        // --- Manual Override Controls ---
        Label overrideTitle = new Label("Manual Attendance Override (With Audit Logging)");
        overrideTitle.setStyle("-fx-font-weight: bold; -fx-font-size: 13px;");

        TextField sessionIdField = new TextField();
        sessionIdField.setPromptText("Session ID");
        sessionIdField.setPrefWidth(90);

        TextField rollField = new TextField();
        rollField.setPromptText("Roll No (e.g., BCA2026001)");
        rollField.setPrefWidth(150);

        ComboBox<String> statusBox = new ComboBox<>(FXCollections.observableArrayList("ON-DUTY", "PRESENT", "ABSENT"));
        statusBox.setValue("ON-DUTY");

        TextField reasonField = new TextField();
        reasonField.setPromptText("Mandatory Reason (e.g., Official Hackathon / Medical Exemption)");
        HBox.setHgrow(reasonField, Priority.ALWAYS);

        Button applyOverrideBtn = new Button("Apply Override");
        Label feedbackLabel = new Label();

        // Auto-fill Session ID and Roll No when clicking a row in the table
        logsTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSel, newSel) -> {
            if (newSel != null) {
                sessionIdField.setText(String.valueOf(newSel.getClassId()));
                rollField.setText(newSel.getRollNo());
            }
        });

        HBox overrideRow = new HBox(8, sessionIdField, rollField, statusBox, reasonField, applyOverrideBtn);
        overrideRow.setAlignment(Pos.CENTER_LEFT);

        // --- Audit Trail Table ---
        TableView<AuditRow> auditTable = new TableView<>(auditData);
        auditTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<AuditRow, Number> aIdCol = new TableColumn<>("Audit ID");
        aIdCol.setCellValueFactory(c -> c.getValue().auditId);

        TableColumn<AuditRow, Number> aSessCol = new TableColumn<>("Session");
        aSessCol.setCellValueFactory(c -> c.getValue().classId);

        TableColumn<AuditRow, String> aRollCol = new TableColumn<>("Roll No");
        aRollCol.setCellValueFactory(c -> c.getValue().rollNo);

        TableColumn<AuditRow, String> aOldCol = new TableColumn<>("Old Status");
        aOldCol.setCellValueFactory(c -> c.getValue().oldStatus);

        TableColumn<AuditRow, String> aNewCol = new TableColumn<>("New Status");
        aNewCol.setCellValueFactory(c -> c.getValue().newStatus);

        TableColumn<AuditRow, String> aByCol = new TableColumn<>("Changed By");
        aByCol.setCellValueFactory(c -> c.getValue().changedBy);

        TableColumn<AuditRow, String> aReasonCol = new TableColumn<>("Reason");
        aReasonCol.setCellValueFactory(c -> c.getValue().reason);

        TableColumn<AuditRow, String> aTimeCol = new TableColumn<>("Changed At");
        aTimeCol.setCellValueFactory(c -> c.getValue().changedAt);

        auditTable.getColumns().addAll(aIdCol, aSessCol, aRollCol, aOldCol, aNewCol, aByCol, aReasonCol, aTimeCol);

        // --- Loaders ---
        Runnable refreshTables = () -> {
            attendanceData.setAll(fetchAttendanceRows(dateFilterField.getText(), subjectFilterField.getText()));
            auditData.setAll(fetchAuditRows());
        };

        filterBtn.setOnAction(e -> refreshTables.run());
        resetFilterBtn.setOnAction(e -> {
            dateFilterField.clear();
            subjectFilterField.clear();
            refreshTables.run();
        });

        applyOverrideBtn.setOnAction(e -> {
            String sessText = sessionIdField.getText().trim();
            String rollText = rollField.getText().trim();
            String newStatus = statusBox.getValue();
            String reason = reasonField.getText().trim();

            if (sessText.isEmpty() || rollText.isEmpty() || reason.isEmpty()) {
                feedbackLabel.setStyle("-fx-text-fill: #c0392b;");
                feedbackLabel.setText("Error: Session ID, Roll No, and Reason are all required for audit compliance.");
                return;
            }

            try {
                int classId = Integer.parseInt(sessText);
                boolean ok = DatabaseManager.overrideAttendanceStatus(classId, rollText, newStatus, adminUser, reason);
                if (ok) {
                    feedbackLabel.setStyle("-fx-text-fill: #27ae60;");
                    feedbackLabel.setText("Override saved & logged in audit trail: " + rollText + " -> " + newStatus);
                    reasonField.clear();
                    refreshTables.run();
                } else {
                    feedbackLabel.setStyle("-fx-text-fill: #c0392b;");
                    feedbackLabel.setText("Failed to apply override. Check Session ID and Roll No.");
                }
            } catch (NumberFormatException ex) {
                feedbackLabel.setStyle("-fx-text-fill: #c0392b;");
                feedbackLabel.setText("Error: Session ID must be a valid number.");
            }
        });

        refreshTables.run();

        VBox logsPane = new VBox(10, filterBar, logsTable, new Separator(), overrideTitle, overrideRow, feedbackLabel);
        logsPane.setPadding(new Insets(12));
        VBox.setVgrow(logsTable, Priority.ALWAYS);

        VBox auditPane = new VBox(10, auditTable);
        auditPane.setPadding(new Insets(12));
        VBox.setVgrow(auditTable, Priority.ALWAYS);

        Tab logsTab = new Tab("Attendance Logs & Overrides", logsPane);
        logsTab.setClosable(false);

        Tab auditTab = new Tab("Audit Trail History", auditPane);
        auditTab.setClosable(false);

        TabPane tabPane = new TabPane(logsTab, auditTab);

        stage.setScene(new Scene(tabPane, 920, 560));
        stage.centerOnScreen();
    }

    private static List<AttendanceRow> fetchAttendanceRows(String dateFilter, String subjectFilter) {
        List<AttendanceRow> list = new ArrayList<>();
        StringBuilder sql = new StringBuilder(
                "SELECT a.log_id, a.class_id, COALESCE(c.subject_name, 'N/A') AS subject_name, " +
                        "a.student_roll_no, COALESCE(s.name, 'Unknown') AS student_name, a.status, a.timestamp " +
                        "FROM attendance_logs a " +
                        "LEFT JOIN classes c ON a.class_id = c.class_id " +
                        "LEFT JOIN students s ON a.student_roll_no = s.roll_no " +
                        "WHERE 1=1 "
        );

        List<String> params = new ArrayList<>();
        if (dateFilter != null && !dateFilter.trim().isEmpty()) {
            sql.append("AND a.timestamp LIKE ? ");
            params.add(dateFilter.trim() + "%");
        }
        if (subjectFilter != null && !subjectFilter.trim().isEmpty()) {
            sql.append("AND UPPER(COALESCE(c.subject_name, '')) LIKE ? ");
            params.add("%" + subjectFilter.trim().toUpperCase() + "%");
        }
        sql.append("ORDER BY a.log_id DESC");

        try (Connection conn = DriverManager.getConnection(URL);
             PreparedStatement pstmt = conn.prepareStatement(sql.toString())) {

            for (int i = 0; i < params.size(); i++) {
                pstmt.setString(i + 1, params.get(i));
            }

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    list.add(new AttendanceRow(
                            rs.getInt("log_id"),
                            rs.getInt("class_id"),
                            rs.getString("subject_name"),
                            rs.getString("student_roll_no"),
                            rs.getString("student_name"),
                            rs.getString("status"),
                            rs.getString("timestamp")
                    ));
                }
            }
        } catch (SQLException e) {
            System.out.println("[HOD UI ERROR] Failed to load attendance logs: " + e.getMessage());
        }
        return list;
    }

    private static List<AuditRow> fetchAuditRows() {
        List<AuditRow> list = new ArrayList<>();
        String sql = "SELECT audit_id, class_id, student_roll_no, old_status, new_status, changed_by, reason, changed_at " +
                "FROM attendance_audit_log ORDER BY audit_id DESC";

        try (Connection conn = DriverManager.getConnection(URL);
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                list.add(new AuditRow(
                        rs.getInt("audit_id"),
                        rs.getInt("class_id"),
                        rs.getString("student_roll_no"),
                        rs.getString("old_status"),
                        rs.getString("new_status"),
                        rs.getString("changed_by"),
                        rs.getString("reason"),
                        rs.getString("changed_at")
                ));
            }
        } catch (SQLException e) {
            System.out.println("[HOD UI ERROR] Failed to load audit logs: " + e.getMessage());
        }
        return list;
    }
}
