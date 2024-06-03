package daos;

import models.User;

import java.sql.*;

/**
 * A user DAO.
 */
public class UserDao {
    private final Connection conn;

    public UserDao(Connection conn)
    {
        this.conn = conn;
    }

    /**
     * Inserts a user into the database.
     *
     * @param user the user to be inserted.
     * @throws DataAccessException if a data access exception error occurs.
     */
    public void insert(User user) throws DataAccessException {
        String sql = "INSERT INTO Users (username, password, email, firstName, lastName, gender, personID) VALUES(?,?,?,?,?,?,?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, user.getUsername());
            stmt.setString(2, user.getPassword());
            stmt.setString(3, user.getEmail());
            stmt.setString(4, user.getFirstName());
            stmt.setString(5, user.getLastName());
            stmt.setString(6, user.getGender());
            stmt.setString(7, user.getPersonID());

            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("Error encountered while inserting user into the database.");
        }
    }

    /**
     * Retrieves a user from the database.
     *
     * @param username user to be returned.
     * @return a user.
     * @throws DataAccessException if a data access exception error occurs.
     */
    public User find(String username) throws DataAccessException {
        User user;
        ResultSet rs = null;
        String sql = "SELECT * FROM Users WHERE username = ?;";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            rs = stmt.executeQuery();
            if (rs.next()) {
                user = new User(rs.getString(1), rs.getString(2), rs.getString(3),rs.getString(4),
                        rs.getString(5), rs.getString(6), rs.getString(7));
                return user;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error encountered while finding user.");
        } finally {
            if(rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

        }
        return null;
    }

    /**
     * Clears all users from the database and resets the auto-increment primary key sequence.
     *
     * @throws DataAccessException if a data access exception error occurs.
     */
    public void clear() throws DataAccessException {
        String sql = "DELETE FROM Users";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("Error encountered while clearing the users.");
        }
    }
}
