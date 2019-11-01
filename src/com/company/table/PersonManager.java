package com.company.table;

import com.company.Main;
import com.company.db.DBUtil;
import com.company.db.beans.Person;
import javafx.beans.binding.BooleanBinding;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.util.Pair;

import java.sql.*;
import java.util.Optional;

public class PersonManager {

    public static Person getRow(ResultSet resultSet, int rowNumber) {
        try {
            Person bean = new Person();
            if (resultSet.absolute(rowNumber)) {
                bean.setName(resultSet.getString("name"));
                bean.setFamily(resultSet.getString("family"));
                bean.setNationalId(resultSet.getString("nationalId"));
                bean.setPersonId(rowNumber);
                return bean;
            } else {
                Alert noRow = new Alert(Alert.AlertType.WARNING);
                noRow.setHeaderText("No row were found !");
                noRow.setContentText("Unfortunately we couldn't reach that specific row.\n\tProbably your database is still empty.");
                noRow.showAndWait();
                return null;
            }
        } catch (SQLException e) {
            Alert error = new Alert(Alert.AlertType.ERROR);
            error.setHeaderText("Something went wrong at retrieving rows !");
            error.setContentText(e.getMessage());
            error.showAndWait();
            System.err.println(e);
            return null;
        }
    }

    private static void showData(ResultSet resultSet) {
        Main.data.clear();
        int rowNumber = 0;
        try {
            resultSet.beforeFirst();
            while (resultSet.next()) {
                Person bean = getRow(resultSet, ++rowNumber);
                if (bean != null) {
                    Main.data.add(bean);
                }
            }
            Main.tableView.setItems(Main.data);
        } catch (SQLException e) {
            Alert error = new Alert(Alert.AlertType.ERROR);
            error.setHeaderText("Something went wrong !");
            error.setContentText("We are not able to show that data !\n" + e.getMessage());
            error.showAndWait();
        }
        clear();
    }

    public static void displayAllRows() {
        String sql = "SELECT * FROM profiles";
        try (
                Connection connection = DBUtil.getConnection();
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(sql);
        ) {
            showData(resultSet);
        } catch (SQLException e) {
            Alert error = new Alert(Alert.AlertType.ERROR);
            error.setHeaderText("Something went wrong !");
            error.setContentText(e.getMessage());
            error.showAndWait();
            System.err.println(e);
        }
    }

    public static boolean insert(Person bean) {
        String sql = "INSERT INTO profiles(name, family, nationalId) VALUES (?, ?, ?)";
//        ResultSet keys = null;
        try (
                Connection connection = DBUtil.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)
        ) {
            statement.setString(1, bean.getName());
            statement.setString(2, bean.getFamily());
            statement.setString(3, bean.getNationalId());

            int affected = statement.executeUpdate();
//            if (affected == 1) {                                            // DELETE IF BLOCK using if(affected != 1)
//                keys = statement.getGeneratedKeys();
//                keys.next();
//                int newKey = keys.getInt(1);
//                bean.setPersonId(newKey);
//            } else {
            if (affected != 1) {
                Alert noRowEffected = new Alert(Alert.AlertType.WARNING);
                noRowEffected.setHeaderText("No row were Effected !");
                noRowEffected.setContentText("Something went wrong or no data added to database.");
                noRowEffected.showAndWait();
                return false;
            }
        } catch (SQLException e) {
            Alert error = new Alert(Alert.AlertType.ERROR);
            error.setHeaderText("Something went wrong !");
            error.setContentText("Insert task wasn't success !\n" + e.getMessage());
            error.showAndWait();
            return false;
        } finally {
            displayAllRows();
            clear();
        }
        return true;
    }

    public static void search() {
        Person bean = new Person();
        //                   new Person(tfName.getText(),tfFamily.getText(),tfNationalID.getText());
        bean.setName(Main.tfName.getText());
        bean.setFamily(Main.tfFamily.getText());
        bean.setNationalId(Main.tfNationalID.getText());

        String sql = "SELECT * FROM profiles WHERE ";
        if (bean.getName().equals("") && bean.getFamily().equals("")) {
            sql += "nationalId LIKE '%" + bean.getNationalId() + "%'";
        } else if (bean.getName().equals("") && bean.getNationalId().equals("")) {
            sql += "family LIKE '%" + bean.getFamily() + "%'";
        } else if (bean.getFamily().equals("") && bean.getNationalId().equals("")) {
            sql += "name LIKE '%" + bean.getName() + "%'";
        } else if (bean.getName().equals("")) {
            sql += "nationalId LIKE '%" + bean.getNationalId() + "%' AND family Like '%" + bean.getFamily() + "%'";
        } else if (bean.getFamily().equals("")) {
            sql += "nationalId LIKE '%" + bean.getNationalId() + "%' AND name Like '%" + bean.getName() + "%'";
        } else if (bean.getNationalId().equals("")) {
            sql += "name LIKE '%" + bean.getName() + "%' AND family Like '%" + bean.getFamily() + "%'";
        } else {
            sql += "name LIKE '%" + bean.getName() + "%' AND family Like '%" + bean.getFamily() + "%' AND nationalId Like '%" + bean.getNationalId() + "%'";
        }
        try (
                Connection connection = DBUtil.getConnection();
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(sql);
        ) {
            if (resultSet.next() == resultSet.isAfterLast()) {
                Alert noData = new Alert(Alert.AlertType.INFORMATION);
                noData.setHeaderText("No Search result found!");
                noData.setContentText("Please try something else.");
                noData.showAndWait();
            } else {
                showData(resultSet);
            }
        } catch (SQLException e) {
            Alert fail = new Alert(Alert.AlertType.ERROR);
            fail.setHeaderText("Something went wrong !");
            fail.setContentText("Search task wasn't success !\n" + e.getMessage());
        } finally {
            clear();
        }
    }

    public static boolean delete() {
        String sql = "DELETE FROM profiles WHERE nationalId = ?";

        Person bean = Main.selectedItem.get(0);
//        ResultSet resultSet = null;
        try (
                Connection connection = DBUtil.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql);
        ) {
            statement.setString(1, bean.getNationalId());
            int affected = statement.executeUpdate();
            if (affected != 1) {
                Alert fail = new Alert(Alert.AlertType.ERROR);
                fail.setHeaderText("Something went wrong !");
                fail.setContentText("Delete task wasn't success !");
                return false;
            }
        } catch (SQLException e) {
            Alert fail = new Alert(Alert.AlertType.ERROR);
            fail.setHeaderText("Something went wrong !");
            fail.setContentText("Delete task wasn't success !\n" + e.getMessage());
            return false;
        } finally {
            clear();
            displayAllRows();
        }
        return true;
    }

    public static boolean update(Person bean) {
        String sql = "UPDATE profiles SET name = ?, family = ?, nationalId = ? WHERE nationalId = ?";
        String oldNationalId = Main.selectedItem.get(0).getNationalId();

        try (
                Connection connection = DBUtil.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql);
        ) {
            statement.setString(1, bean.getName());
            statement.setString(2, bean.getFamily());
            statement.setString(3, bean.getNationalId());
            statement.setString(4, oldNationalId);

            int affected = statement.executeUpdate();
            if (affected != 1) {
                Alert fail = new Alert(Alert.AlertType.ERROR);
                fail.setHeaderText("Something went wrong !");
                fail.setContentText("Edit task wasn't success !");
                return false;
            }
        } catch (SQLException e) {
            Alert fail = new Alert(Alert.AlertType.ERROR);
            fail.setHeaderText("Something went wrong !");
            fail.setContentText("Edit task wasn't success !\n" + e.getMessage());
            return false;
        } finally {
            clear();
            displayAllRows();
        }
        return true;
    }

    public static void clear() {
        Main.tfName.setText("");
        Main.tfFamily.setText("");
        Main.tfNationalID.setText("");
        Main.txtWarning.setText("");
        Main.tfName.requestFocus();
    }

}