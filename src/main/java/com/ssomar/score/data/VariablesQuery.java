package com.ssomar.score.data;

import com.ssomar.score.SCore;
import com.ssomar.score.utils.emums.VariableType;
import com.ssomar.score.utils.logging.Utils;
import com.ssomar.score.variables.Variable;
import com.ssomar.score.variables.VariableForEnum;

import java.sql.*;
import java.util.*;

public class VariablesQuery {

    private final static String TABLE_VARIABLES = "variables";
    private final static String TABLE_VARIABLES_NAME = "Variables";

    private final static String COL_INDEX = "col_index";
    private final static String COL_ID = "col_id";
    private final static String COL_TYPE = "col_type";
    private final static String COL_FOR = "col_for";
    private final static String COL_VALUES = "col_values";
    private final static String COL_DEFAULTVALUE = "col_defaultvalue";

    public final static String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_VARIABLES + " (" + COL_INDEX + " INT PRIMARY KEY NOT NULL AUTO_INCREMENT, " + COL_ID + " TEXT NOT NULL, " + COL_TYPE + " TEXT NOT NULL, " + COL_FOR + " TEXT NOT NULL, " + COL_VALUES + " TEXT NOT NULL, " + COL_DEFAULTVALUE + " TEXT NOT NULL)";


    public static void createNewTable(Connection conn) {
        Statement stmt = null;
        try  {
            stmt = conn.createStatement();
            Utils.sendConsoleMsg(SCore.NAME_COLOR + " &7Creating table &6" + TABLE_VARIABLES_NAME + " &7if not exists...");
            stmt.execute(CREATE_TABLE);
        } catch (SQLException e) {
            SCore.plugin.getLogger().severe("Error while creating table " + TABLE_VARIABLES_NAME + " in database "+e.getMessage());
        }
        finally {
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }


    public static void insertVariableNotExists(Connection conn, List<Variable> variables) {

        String sql ="INSERT INTO " + TABLE_VARIABLES + " (" + COL_ID + "," + COL_TYPE + "," + COL_FOR + "," + COL_VALUES + "," + COL_DEFAULTVALUE + ") SELECT ?,?,?,?,? WHERE NOT EXISTS (SELECT 1 FROM "+TABLE_VARIABLES+" WHERE "+COL_ID+" = ?)";

        PreparedStatement pstmt = null;

        int i = 0;

        try {
            pstmt = conn.prepareStatement(sql);
            for (Variable var : variables) {
                pstmt.setString(1, var.getId());
                pstmt.setString(2, var.getType().getValue().get().toString());
                pstmt.setString(3, var.getForFeature().getValue().get().toString());
                String data = transformValues(var.getValues());
                pstmt.setString(4, data);
                pstmt.setString(5, var.getDefaultValue().getValue().orElse("NULL"));
                pstmt.setString(6, var.getId());
                //System.out.println("query ::: "+ pstmt.toString());
                pstmt.addBatch();
                if (i % 1000 == 0 || i == variables.size()) {
                   pstmt.executeBatch(); // Execute every 1000 items.
                }
            }
        } catch (SQLException e) {
            SCore.plugin.getLogger().severe("Error while inserting variables in database "+e.getMessage());
            e.printStackTrace();
        } finally {
            if (pstmt != null) {
                try {
                    pstmt.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void insertVariablesAndDeleteIfExists(Connection conn, List<Variable> variables) {

        for (Variable variable : variables) {
            deleteVariable(conn, variable.getId());
        }

        String sql ="INSERT INTO " + TABLE_VARIABLES + " (" + COL_ID + "," + COL_TYPE + "," + COL_FOR + "," + COL_VALUES + "," + COL_DEFAULTVALUE + ") VALUES(?,?,?,?,?)";

        PreparedStatement pstmt = null;

        int i = 0;

        try {
            pstmt = conn.prepareStatement(sql);
            for (Variable command : variables) {
                pstmt.setString(1, command.getId());
                pstmt.setString(2, command.getType().getValue().get().toString());
                pstmt.setString(3, command.getForFeature().getValue().get().toString());
                String data = transformValues(command.getValues());
                pstmt.setString(4, data);
                pstmt.setString(5, command.getDefaultValue().getValue().orElse("NULL"));
                pstmt.addBatch();
                if (i % 1000 == 0 || i == variables.size()) {
                    pstmt.executeBatch(); // Execute every 1000 items.
                }
            }
        } catch (SQLException e) {
            SCore.plugin.getLogger().severe("Error while inserting variables in database "+e.getMessage());
            e.printStackTrace();
        } finally {
            if (pstmt != null) {
                try {
                    pstmt.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void updateVariable(Connection conn, List<Variable> variables) {

        /*String sql ="BEGIN\n" +
                "   IF NOT EXISTS (SELECT * FROM "+TABLE_VARIABLES+" \n" +
                "                   WHERE "+ COL_ID +" = ?)\n" +
                "   BEGIN\n" +
                "INSERT INTO " + TABLE_VARIABLES + " (" + COL_ID + "," + COL_TYPE + "," + COL_FOR + "," + COL_VALUES + "," + COL_DEFAULTVALUE + ") VALUES(?,?,?,?,?)"+
                "   END\n" +
                "END\n";

        PreparedStatement pstmt = null;

        int i = 0;

        try {
            pstmt = conn.prepareStatement(sql);
            for (Variable command : variables) {
                pstmt.setString(1, command.getId());
                pstmt.setString(2, command.getId());
                pstmt.setString(3, command.getType().getValue().get().toString());
                pstmt.setString(4, command.getForFeature().getValue().get().toString());
                String data = transformValues(command.getValues());
                pstmt.setString(5, data);
                pstmt.setString(6, command.getDefaultValue().getValue().orElse("NULL"));
                pstmt.addBatch();
                if (i % 1000 == 0 || i == variables.size()) {
                    pstmt.executeBatch(); // Execute every 1000 items.
                }
            }
        } catch (SQLException e) {
            System.out.println(SCore.NAME_2 + " " + e.getMessage());
            e.printStackTrace();
        } finally {
            if (pstmt != null) {
                try {
                    pstmt.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } */
    }

    public static String transformValues(Map<String, List<String>> entries){
        String data = "";
        for (String key : entries.keySet()) {
            String values = "::::";
            for (String value : entries.get(key)) {
                values += value + "::::";
            }
            values = values.substring(0, values.length() - 4);
            data += data + ">>>>" + key + values;
        }
        return data;
    }

    public static HashMap<String, List<String>> deconvertValues(String data){
        HashMap<String, List<String>> entries = new HashMap<>();
        String[] datas = data.split(">>>>");
        for(String d : datas){
            String[] values = d.split("::::");
            String key = values[0];
            List<String> list = new ArrayList<>();
            for(int i = 1; i < values.length; i++){
                list.add(values[i]);
            }
            entries.put(key, list);
        }
        return entries;
    }


    public static void deleteVariables(Connection conn) {

        String sql = "DELETE FROM " + TABLE_VARIABLES;

        PreparedStatement pstmt = null;

        try {
            pstmt = conn.prepareStatement(sql);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            SCore.plugin.getLogger().severe("Error while deleting variables in database "+e.getMessage());
        } finally {
            if (pstmt != null) {
                try {
                    pstmt.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void deleteVariable(Connection conn, String id) {

        String sql = "DELETE FROM " + TABLE_VARIABLES+" WHERE "+COL_ID+" = '"+id+"'";

        PreparedStatement pstmt = null;

        try {
            pstmt = conn.prepareStatement(sql);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            SCore.plugin.getLogger().severe("Error while deleting variable in database "+e.getMessage());
        } finally {
            if (pstmt != null) {
                try {
                    pstmt.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static List<Variable> selectAllVariables(Connection conn) {
        String sql = "SELECT " + COL_ID + "," + COL_TYPE + "," + COL_FOR + "," + COL_VALUES + "," + COL_DEFAULTVALUE + " FROM " + TABLE_VARIABLES;

        List<Variable> list = new ArrayList<>();
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();

            while (rs.next()) {

                String id = rs.getString(COL_ID);
                String type = rs.getString(COL_TYPE);
                String forFeature = rs.getString(COL_FOR);
                String values = rs.getString(COL_VALUES);
                String defaultValue = rs.getString(COL_DEFAULTVALUE);


                Variable v = new Variable(id, "plugins/SCore/variables/" + id + ".yml");

                VariableType variableType = VariableType.valueOf(type);
                v.getType().setValue(Optional.of(variableType));

                VariableForEnum variableForEnum = VariableForEnum.valueOf(forFeature);
                v.getForFeature().setValue(Optional.of(variableForEnum));

                HashMap<String, List<String>> entries = deconvertValues(values);
                v.setValues(entries);

                Optional<String> optional = Optional.ofNullable(defaultValue);
                if(optional.isPresent() && !optional.get().equalsIgnoreCase("NULL")){
                    v.getDefaultValue().setValue(optional);
                }

                list.add(v);
            }
        } catch (SQLException e) {
            SCore.plugin.getLogger().severe("Error while selecting variables in database "+e.getMessage());
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (pstmt != null) {
                try {
                    pstmt.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return list;
    }

    public static Optional<Variable> selectVariable(Connection conn, String id) {
        String sql = "SELECT " + COL_TYPE + "," + COL_FOR + "," + COL_VALUES + "," + COL_DEFAULTVALUE + " FROM " + TABLE_VARIABLES+" WHERE "+COL_ID+" = '"+id+"'";

        Optional<Variable> varOpt = Optional.empty();
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();

            while (rs.next()) {

                String type = rs.getString(COL_TYPE);
                String forFeature = rs.getString(COL_FOR);
                String values = rs.getString(COL_VALUES);
                String defaultValue = rs.getString(COL_DEFAULTVALUE);


                Variable v = new Variable(id, "plugins/SCore/variables/" + id + ".yml");

                VariableType variableType = VariableType.valueOf(type);
                v.getType().setValue(Optional.of(variableType));

                VariableForEnum variableForEnum = VariableForEnum.valueOf(forFeature);
                v.getForFeature().setValue(Optional.of(variableForEnum));

                HashMap<String, List<String>> entries = deconvertValues(values);
                v.setValues(entries);

                Optional<String> optional = Optional.ofNullable(defaultValue);
                if(optional.isPresent() && !optional.get().equalsIgnoreCase("NULL")){
                    v.getDefaultValue().setValue(optional);
                }

                varOpt = Optional.of(v);
                break;
            }
        } catch (SQLException e) {
            SCore.plugin.getLogger().severe("Error while selecting variable in database "+e.getMessage());
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (pstmt != null) {
                try {
                    pstmt.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return varOpt;
    }

}