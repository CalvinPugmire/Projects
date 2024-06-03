package daos;

import models.Event;
import models.Person;

import java.sql.*;
import java.util.HashSet;
import java.util.Set;

/**
 * An event DAO.
 */
public class EventDao {
    private final Connection conn;

    public EventDao(Connection conn)
    {
        this.conn = conn;
    }

    /**
     * Inserts an event into the database.
     *
     * @param event the event to be inserted.
     * @throws DataAccessException if a data access exception error occurs.
     */
    public void insert(Event event) throws DataAccessException {
        String sql = "INSERT INTO Events (eventID, associatedUsername, personID, latitude, longitude, country, city, eventType, year) VALUES(?,?,?,?,?,?,?,?,?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, event.getEventID());
            stmt.setString(2, event.getAssociatedUsername());
            stmt.setString(3, event.getPersonID());
            stmt.setFloat(4, event.getLatitude());
            stmt.setFloat(5, event.getLongitude());
            stmt.setString(6, event.getCountry());
            stmt.setString(7, event.getCity());
            stmt.setString(8, event.getEventType());
            stmt.setInt(9, event.getYear());

            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("Error encountered while inserting event into the database.");
        }
    }

    /**
     * Retrieves an event from the database.
     *
     * @param eventID event to be returned.
     * @return an event.
     * @throws DataAccessException if a data access exception error occurs.
     */
    public Event find(String eventID) throws DataAccessException {
        Event event;
        ResultSet rs = null;
        String sql = "SELECT * FROM Events WHERE eventID = ?;";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, eventID);
            rs = stmt.executeQuery();
            if (rs.next()) {
                event = new Event(rs.getString("eventID"), rs.getString("associatedUsername"),
                        rs.getString("personID"), rs.getFloat("latitude"), rs.getFloat("longitude"),
                        rs.getString("country"), rs.getString("city"), rs.getString("eventType"),
                        rs.getInt("year"));
                return event;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error encountered while finding event.");
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
     * Retrieve a list events from the database.
     *
     * @param associatedUsername user whose events are to be returned.
     * @return a list of events.
     * @throws DataAccessException if a data access exception error occurs.
     */
    public Event[] findAll(String associatedUsername) throws DataAccessException {
        Set<Event> events = new HashSet<>();
        ResultSet rs = null;
        String sql = "SELECT * FROM Events WHERE associatedUsername = ?;";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, associatedUsername);
            rs = stmt.executeQuery();
            while (rs.next()) {
                Event event = new Event(rs.getString(1), rs.getString(2), rs.getString(3),rs.getFloat(4),
                        rs.getFloat(5), rs.getString(6), rs.getString(7), rs.getString(8), rs.getInt(9));
                events.add(event);
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
        return events.toArray(new Event[0]);
    }

    /**
     * Clears all events from the database and resets the auto-increment primary key sequence.
     *
     * @throws DataAccessException if a data access exception error occurs.
     */
    public void clear() throws DataAccessException {
        String sql = "DELETE FROM Events";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("Error encountered while clearing the events.");
        }
    }

    /**
     * Clears all events associated with user from the database.
     *
     * @param associatedUsername user's data to be cleared.
     * @throws DataAccessException if a data access exception error occurs.
     */
    public void clear(String associatedUsername) throws DataAccessException {
        String sql = "DELETE FROM Events WHERE associatedUsername = ?;";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, associatedUsername);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("Error encountered while clearing events.");
        }
    }
}
