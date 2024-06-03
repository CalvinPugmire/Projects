package daos;

import models.Person;

import java.sql.*;
import java.util.HashSet;
import java.util.Set;

/**
 * A person DAO.
 */
public class PersonDao {
    private final Connection conn;

    public PersonDao(Connection conn)
    {
        this.conn = conn;
    }

    /**
     * Inserts a person into the database.
     *
     * @param person the person to be inserted.
     * @throws DataAccessException if a data access exception error occurs.
     */
    public void insert(Person person) throws DataAccessException {
        String sql = "INSERT INTO Persons (personID, associatedUsername, firstName, lastName, gender, fatherID, motherID, spouseID) values (?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, person.getPersonID());
            stmt.setString(2, person.getAssociatedUsername());
            stmt.setString(3, person.getFirstName());
            stmt.setString(4, person.getLastName());
            stmt.setString(5, person.getGender());
            stmt.setString(6, person.getFatherID());
            stmt.setString(7, person.getMotherID());
            stmt.setString(8, person.getSpouseID());

            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("Error encountered while inserting person into the database.");
        }
    }

    /**
     * Retrieves a person from the database.
     *
     * @param personID person to be returned.
     * @return a person.
     * @throws DataAccessException if a data access exception error occurs.
     */
    public Person find(String personID) throws DataAccessException {
        Person person;
        ResultSet rs = null;
        String sql = "SELECT * FROM Persons WHERE personID = ?;";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, personID);
            rs = stmt.executeQuery();
            if (rs.next()) {
                person = new Person(rs.getString(1), rs.getString(2), rs.getString(3),rs.getString(4),
                        rs.getString(5), rs.getString(6), rs.getString(7), rs.getString(8));
                return person;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error encountered while finding person.");
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
     * Retrieve a list persons from the database.
     *
     * @param associatedUsername user whose persons are to be returned.
     * @return a list of persons.
     * @throws DataAccessException if a data access exception error occurs.
     */
    public Person[] findAll(String associatedUsername) throws DataAccessException {
        Set<Person> persons = new HashSet<>();
        ResultSet rs = null;
        String sql = "SELECT * FROM Persons WHERE associatedUsername = ?;";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, associatedUsername);
            rs = stmt.executeQuery();
            while (rs.next()) {
                Person person = new Person(rs.getString(1), rs.getString(2), rs.getString(3),rs.getString(4),
                        rs.getString(5), rs.getString(6), rs.getString(7), rs.getString(8));
                persons.add(person);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error encountered while finding person.");
        } finally {
            if(rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return persons.toArray(new Person[0]);
    }

    /**
     * Clears all persons from the database and resets the auto-increment primary key sequence.
     *
     * @throws DataAccessException if a data access exception error occurs.
     */
    public void clear() throws DataAccessException {
        String sql = "DELETE FROM Persons";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("Error encountered while clearing the persons.");
        }
    }

    /**
     * Clears all persons associated with user from the database.
     *
     * @param associatedUsername user's data to be cleared.
     * @throws DataAccessException if a data access exception error occurs.
     */
    public void clear(String associatedUsername) throws DataAccessException {
        String sql = "DELETE FROM Persons WHERE associatedUsername = ?;";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, associatedUsername);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("Error encountered while clearing persons.");
        }
    }
}
