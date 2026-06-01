package org.psyrioty.magicCostume.Database;

import java.sql.*;

public class Requests {

    public static final String CREATE_COSTUME_ENTITY_TABLE =
            "CREATE TABLE IF NOT EXISTS CostumeEntity (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "uuid TEXT NOT NULL UNIQUE, " +
                    "hideOtherCostumes INTEGER NOT NULL DEFAULT 0" +
                    ");";

    public static final String CREATE_SLOT_TABLE =
            "CREATE TABLE IF NOT EXISTS Slot (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "name TEXT NOT NULL, " +
                    "CostumeEntity_id INTEGER NOT NULL, " +
                    "UNIQUE(CostumeEntity_id, name), " +
                    "FOREIGN KEY (CostumeEntity_id) REFERENCES CostumeEntity(id) ON DELETE CASCADE" +
                    ");";

    public static final String CREATE_COSTUME_PART_TABLE =
            "CREATE TABLE IF NOT EXISTS CostumePart (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "name TEXT NOT NULL, " +
                    "scale REAL NOT NULL DEFAULT 1.0, " +
                    "brightness INTEGER NOT NULL DEFAULT 0, " +
                    "offsetX REAL NOT NULL DEFAULT 0.0, " +
                    "offsetY REAL NOT NULL DEFAULT 0.0, " +
                    "offsetZ REAL NOT NULL DEFAULT 0.0, " +
                    "slot_id INTEGER NOT NULL UNIQUE, " +
                    "FOREIGN KEY (slot_id) REFERENCES Slot(id) ON DELETE CASCADE" +
                    ");";

    public static void createTables(Connection connection) throws SQLException {
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate(CREATE_COSTUME_ENTITY_TABLE);
            statement.executeUpdate(CREATE_SLOT_TABLE);
            statement.executeUpdate(CREATE_COSTUME_PART_TABLE);
        }
    }

    public static Integer findCostumeEntityIdByUUID(Connection connection, String uuid) throws SQLException {
        String sql = "SELECT id FROM CostumeEntity WHERE uuid = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, uuid);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("id");
                }
            }
        }

        return null;
    }

    public static int getOrCreateCostumeEntityId(Connection connection,
                                                 String uuid,
                                                 boolean hideOtherCostumes) throws SQLException {
        Integer id = findCostumeEntityIdByUUID(connection, uuid);
        if (id != null) {
            return id;
        }
        return insertCostumeEntity(connection, uuid, hideOtherCostumes);
    }

    public static int insertCostumeEntity(Connection connection,
                                          String uuid,
                                          boolean hideOtherCostumes) throws SQLException {

        String sql = "INSERT INTO CostumeEntity (uuid, hideOtherCostumes) VALUES (?, ?)";

        try (PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, uuid);
            ps.setBoolean(2, hideOtherCostumes);

            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        }

        throw new SQLException("Не удалось получить ID созданной записи CostumeEntity");
    }

    public static Integer findSlotId(Connection connection,
                                     String name,
                                     int costumeEntityId) throws SQLException {
        String sql = "SELECT id FROM Slot WHERE name = ? AND CostumeEntity_id = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, name);
            ps.setInt(2, costumeEntityId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("id");
                }
            }
        }

        return null;
    }

    public static int getOrCreateSlotId(Connection connection,
                                        String name,
                                        int costumeEntityId) throws SQLException {
        Integer id = findSlotId(connection, name, costumeEntityId);
        if (id != null) {
            return id;
        }
        return insertSlot(connection, name, costumeEntityId);
    }

    public static int insertSlot(Connection connection,
                                 String name,
                                 int costumeEntityId) throws SQLException {

        String sql = "INSERT INTO Slot (name, CostumeEntity_id) VALUES (?, ?)";

        try (PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, name);
            ps.setInt(2, costumeEntityId);

            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        }

        throw new SQLException("Не удалось получить ID созданного Slot");
    }

    public static Integer findCostumePartIdBySlotId(Connection connection, int slotId) throws SQLException {
        String sql = "SELECT id FROM CostumePart WHERE slot_id = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, slotId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("id");
                }
            }
        }

        return null;
    }

    public static int getOrCreateCostumePart(Connection connection,
                                             String name,
                                             double scale,
                                             int brightness,
                                             double offsetX,
                                             double offsetY,
                                             double offsetZ,
                                             int slotId) throws SQLException {
        Integer id = findCostumePartIdBySlotId(connection, slotId);
        if (id != null) {
            updateCostumePart(connection, name, scale, brightness, offsetX, offsetY, offsetZ, slotId);
            return id;
        }
        return insertCostumePart(connection, name, scale, brightness, offsetX, offsetY, offsetZ, slotId);
    }

    public static int insertCostumePart(Connection connection,
                                        String name,
                                        double scale,
                                        int brightness,
                                        double offsetX,
                                        double offsetY,
                                        double offsetZ,
                                        int slotId) throws SQLException {
        String sql = "INSERT INTO CostumePart (name, scale, brightness, offsetX, offsetY, offsetZ, slot_id) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, name);
            ps.setDouble(2, scale);
            ps.setInt(3, brightness);
            ps.setDouble(4, offsetX);
            ps.setDouble(5, offsetY);
            ps.setDouble(6, offsetZ);
            ps.setInt(7, slotId);

            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        }

        throw new SQLException("Не удалось получить ID созданного CostumePart");
    }

    public static void updateCostumePart(Connection connection,
                                         String name,
                                         double scale,
                                         int brightness,
                                         double offsetX,
                                         double offsetY,
                                         double offsetZ,
                                         int slotId) throws SQLException {
        String sql = "UPDATE CostumePart SET name = ?, scale = ?, brightness = ?, offsetX = ?, offsetY = ?, offsetZ = ? WHERE slot_id = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, name);
            ps.setDouble(2, scale);
            ps.setInt(3, brightness);
            ps.setDouble(4, offsetX);
            ps.setDouble(5, offsetY);
            ps.setDouble(6, offsetZ);
            ps.setInt(7, slotId);
            ps.executeUpdate();
        }
    }

    public static PreparedStatement selectCostumePartsByEntityUUID(Connection connection, String uuid) throws SQLException {
        String sql = """
            SELECT cp.*
            FROM CostumePart cp
            JOIN Slot s ON cp.slot_id = s.id
            JOIN CostumeEntity ce ON s.CostumeEntity_id = ce.id
            WHERE ce.uuid = ?
            ORDER BY s.id, cp.id
            """;

        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setString(1, uuid);
        return ps;
    }

    public static int deleteCostumePartByEntityUUIDAndSlotName(Connection connection,
                                                               String uuid,
                                                               String slotName) throws SQLException {
        String sql = """
            DELETE FROM CostumePart
            WHERE slot_id IN (
                SELECT cp.slot_id
                FROM CostumePart cp
                JOIN Slot s ON cp.slot_id = s.id
                JOIN CostumeEntity ce ON s.CostumeEntity_id = ce.id
                WHERE ce.uuid = ? AND s.name = ?
            )
            """;

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, uuid);
            ps.setString(2, slotName);
            return ps.executeUpdate();
        }
    }
}