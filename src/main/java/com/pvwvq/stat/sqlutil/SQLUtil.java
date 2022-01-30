package com.pvwvq.stat.sqlutil;

import java.io.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;

public class SQLUtil {

    String dataPath;

    public SQLUtil(String path) {

        dataPath = path;

        try {

            Class.forName("org.sqlite.JDBC");

        } catch (ClassNotFoundException exception) {

            throw new SQLUtilError("Need JDBC Class");

        }

    }

    public String get() {

        return dataPath;

    }

    public void makeTable(String tableName, HashMap<String, ArrayList<FieldType>> fields) {

        checkFile(true);

        String query = makeTableQuery(tableName, fields);

        Connection con = null;
        Statement statement = null;

        try {

            con = DriverManager.getConnection("jdbc:sqlite:" + dataPath);
            statement = con.createStatement();

            statement.execute(query);

        } catch (SQLException exception1) {

            throw new SQLUtilError("Internal Error");

        } finally {

            if (con != null) try {con.close();} catch (Exception ignored) {}
            if (statement != null) try {statement.close();} catch (Exception ignored) {}

        }
    }

    public void insertData(String tableName, ArrayList<String> fieldNames, ArrayList<String> values) {

        checkFile(true);

        String query = "INSERT INTO " + tableName + " (";
        boolean comma = false;

        for (String fieldName : fieldNames) {

            if (!comma) {

                query += " '"  + fieldName + "'";
                comma = true;
            }
            else {

                query += ", '" + fieldName + "'";

            }

        }

        query += ") VALUES (";

        comma = false;

        for (String value : values) {
            if (!comma) {

                query += "'" + value + "'";
                comma = true;
            }
            else {

                query += ", '" + value + "'";

            }
        }


        query += ")";

        Connection con = null;
        Statement statement = null;

        try {

            con = DriverManager.getConnection("jdbc:sqlite:" + dataPath);
            statement = con.createStatement();

            statement.execute(query);

        } catch (SQLException exception1) {

            exception1.printStackTrace();

            throw new SQLUtilError("Internal Error");

        } finally {

            if (con != null) try {con.close();} catch (Exception ignored) {}
            if (statement != null) try {statement.close();} catch (Exception ignored) {}

        }
    }

    public boolean checkDataExist(String tableName, String fieldName, String value) {

        checkFile(true);

        String query = "SELECT EXISTS (" +
                "SELECT * FROM " + tableName +" " +
                "WHERE " + fieldName +" = '" + value +"')";

        Connection con = null;
        Statement statement = null;

        try {

            con = DriverManager.getConnection("jdbc:sqlite:" + dataPath);
            statement = con.createStatement();

            if (statement.executeQuery(query).getString(1).contains("0")) {

                try {con.close();} catch (Exception ignored) {}
                try {statement.close();} catch (Exception ignored) {}

                return true;

            } else {

                try {con.close();} catch (Exception ignored) {}
                try {statement.close();} catch (Exception ignored) {}

                return false;

            }

        } catch (SQLException exception1) {

            throw new SQLUtilError("Internal Error");

        } finally {

            if (con != null) try {con.close();} catch (Exception ignored) {}
            if (statement != null) try {statement.close();} catch (Exception ignored) {}

        }
    }

    public void deleteData(String tableName, String fieldName, String value) {

        checkFile(true);

        String query = "DELETE FROM " + tableName +
                "WHERE " + fieldName +" = '" + value +"')";

        Connection con = null;
        Statement statement = null;

        try {

            con = DriverManager.getConnection("jdbc:sqlite:" + dataPath);
            statement = con.createStatement();

            statement.execute(query);

        } catch (SQLException exception1) {

            throw new SQLUtilError("Internal Error");

        } finally {

            if (con != null) try {con.close();} catch (Exception ignored) {}
            if (statement != null) try {statement.close();} catch (Exception ignored) {}

        }
    }

    public void updateData(String tableName, String fieldName, String oldValue, String newValue) {

        checkFile(true);

        String query = "UPDATE " + tableName + " SET " + fieldName + " = '" + newValue + "' WHERE " + fieldName + " = '" + oldValue + "';";


        Connection con = null;
        Statement statement = null;

        try {

            con = DriverManager.getConnection("jdbc:sqlite:" + dataPath);
            statement = con.createStatement();

            statement.execute(query);

        } catch (SQLException exception1) {

            throw new SQLUtilError("Internal Error");

        } finally {

            if (con != null) try {con.close();} catch (Exception ignored) {}
            if (statement != null) try {statement.close();} catch (Exception ignored) {}

        }
    }

    public Boolean checkFile(boolean makeFile) {

        File file = new File(dataPath);

        if (makeFile) {

            try {

                return !file.createNewFile();

            } catch (IOException exception) {

                return null;

            }
        }

        else {

            return file.exists();

        }
    }

    private FieldType checkType(ArrayList<FieldType> types) {

        if (types.contains(FieldType.NULL)) return FieldType.NULL;
        if (types.contains(FieldType.INTEGER)) return FieldType.INTEGER;
        if (types.contains(FieldType.TEXT)) return FieldType.TEXT;
        if (types.contains(FieldType.BLOB)) return FieldType.BLOB;
        if (types.contains(FieldType.REAL)) return FieldType.REAL;
        if (types.contains(FieldType.NUMERIC)) return FieldType.NUMERIC;

        return null;

    }

    private String makeTableQuery(String tableName, HashMap<String, ArrayList<FieldType>> fields) {

        boolean AI = false;
        String query = "CREATE TABLE IF NOT EXISTS '" + tableName + "' (";
        String lastQuery = "";
        boolean comma = false;

        for (int i = 0; i < fields.size(); i++) {

            String name = (String) fields.keySet().toArray()[i];
            ArrayList<FieldType> types = fields.get(name);
            String typeQuery = "";
            FieldType type = checkType(types);

            if (type == null) { // Check type exist

                throw new SQLUtilError("Need to set field type");

            }

            // Check type >= 2
            ArrayList<FieldType> types1 = types;
            types1.remove(type);

            if (checkType(types1) != null) {

                throw new SQLUtilError("Too many type be set");

            }

            if (types.contains(FieldType.NULL) && types.contains(FieldType.NOT_NULL)) { // Check type contains null and not null

                throw new SQLUtilError("NULL type and NOT NULL type cannot exist at same time");

            }

            if (types.contains(FieldType.PRIMARY_KEY)) {

                if (AI) {

                    throw new SQLUtilError("Only one field con use AUTO_INCREMENT");

                }

                types.remove(FieldType.PRIMARY_KEY);

                if (!lastQuery.contains("PRIMARY")) {

                    lastQuery += "\tPRIMARY KEY('" + name + "')";

                }
                else {

                    lastQuery = lastQuery.replace(")", "");
                    lastQuery += ", '" + name + "')";

                }
            }

            else if (types.contains(FieldType.AUTO_INCREMENT)) {

                types.remove(FieldType.AUTO_INCREMENT);
                if (type != FieldType.INTEGER) {

                    throw new SQLUtilError("AUTO_INCREMENT can only INTEGER type");

                }

                if (AI) {

                    throw new SQLUtilError("Only one field con use AUTO_INCREMENT");

                }
                else {

                    lastQuery = "\tPRIMARY KEY('" + name +"' AUTOINCREMENT)";

                    AI = true;

                }
            }

            typeQuery += type.toString() + " ";
            types.remove(type);

            for (FieldType fieldType : types) {

                String ft = fieldType.toString();

                if (fieldType == FieldType.NOT_NULL) {

                    typeQuery += "NOT NULL" + " ";

                }
                else if (fieldType == FieldType.UNIQUE_FIELD) {

                    typeQuery += "UNIQUE" + " ";

                }
                else {

                    typeQuery += ft + " ";

                }
            }

            if (comma) {

                query += ",\n\t'" + name + "' " + typeQuery;

            }
            else {

                query += "\n\t'" + name + "' " + typeQuery;
                comma = true;

            }

        }

        if (!lastQuery.equals("")) {

            query += ",\n" + lastQuery;

        } else {

        }

        query += "\n);";

        return query;
    }

}