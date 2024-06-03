package daos;

import models.AuthToken;

import java.sql.*;

/**
 * An authtoken DAO.
 */
public class AuthTokenDao {
    private final Connection conn;

    public AuthTokenDao(Connection conn)
    {
        this.conn = conn;
    }

    /**
     * Inserts an authtoken into the database.
     *
     * @param authtoken the authtoken to be inserted.
     * @throws DataAccessException if a data access exception error occurs.
     */
    public void insert(AuthToken authtoken) throws DataAccessException {
        String sql = "INSERT INTO Authtokens (authtoken, username) values (?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, authtoken.getAuthtoken());
            stmt.setString(2, authtoken.getUsername());

            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("Error encountered while inserting authtoken into the database.");
        }
    }

    /**
     * Retrieves an authtoken from the database.
     *
     * @param token authtoken to be returned.
     * @return an authtoken.
     * @throws DataAccessException if a data access exception error occurs.
     */
    public AuthToken find(String token) throws DataAccessException {
        AuthToken authtoken;
        ResultSet rs = null;
        String sql = "SELECT * FROM Authtokens WHERE authtoken = ?;";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, token);
            rs = stmt.executeQuery();
            if (rs.next()) {
                authtoken = new AuthToken(rs.getString(1), rs.getString(2));
                return authtoken;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error encountered while finding authtoken.");
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
     * Clears all authtokens from the database and resets the auto-increment primary key sequence.
     *
     * @throws DataAccessException if a data access exception error occurs.
     */
    public void clear() throws DataAccessException {
        String sql = "DELETE FROM Authtokens";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("Error encountered while clearing the authtokens.");
        }
    }
}
