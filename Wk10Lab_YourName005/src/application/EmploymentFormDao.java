
package application;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Date;

public class EmploymentFormDao {

    public int saveEmploymentForm(EmploymentForm employment)
            throws SQLException {

        String query = "INSERT INTO employment_applications "
                + "(full_name, current_address, contact_number, "
                + "email_address, education, gender, date_available, "
                + "desired_position, desired_salary, authorized_to_work, "
                + "relatives_working, explanation, signature) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        Connection connection = Database.getDBConnection();

        if (connection == null) {
            throw new SQLException("Database connection failed.");
        }

        try (Connection conn = connection;
             PreparedStatement statement = conn.prepareStatement(
                     query, Statement.RETURN_GENERATED_KEYS)) {

            int counter = 1;

            statement.setString(counter++, employment.getFullName());
            statement.setString(counter++, employment.getCurrentAddress());
            statement.setString(counter++, employment.getContactNumber());
            statement.setString(counter++, employment.getEmailAddress());
            statement.setString(counter++, employment.getEducation());
            statement.setString(counter++, employment.getGender());

            if (employment.getDateAvailable() != null) {
                statement.setDate(counter++,
                        Date.valueOf(employment.getDateAvailable()));
            } else {
                statement.setNull(counter++, java.sql.Types.DATE);
            }

            statement.setString(counter++, employment.getDesiredPosition());
            statement.setString(counter++, employment.getDesiredSalary());
            statement.setBoolean(counter++, employment.isAuthorizedToWork());
            statement.setBoolean(counter++, employment.isRelativesWorking());
            statement.setString(counter++, employment.getExplanation());
            statement.setString(counter++, employment.getSignature());

            statement.executeUpdate();

            try (ResultSet resultSet = statement.getGeneratedKeys()) {
                if (resultSet.next()) {
                    return resultSet.getInt(1);
                }
            }
        }

        return 0;
    }
}
