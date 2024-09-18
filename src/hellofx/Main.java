package hellofx;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import hellofx.models.Person;
import hellofx.utils.Database;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;

import java.sql.Statement;

public class Main extends Application {
  private Connection connection;

  @Override
  public void start(Stage primaryStage) throws Exception {
    try {
      connection = Database.getConnection();
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }

    TableView<Person> tableView = new TableView<>();

    TableColumn<Person, Integer> idColumn = new TableColumn<>("ID");
    idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));

    TableColumn<Person, String> nameColumn = new TableColumn<>("Name");
    nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

    tableView.getColumns().addAll(idColumn, nameColumn);

    try {
      populateTable(tableView);
    } catch (SQLException e) {
      e.printStackTrace();
    }

    VBox root = new VBox(tableView);
    Scene scene = new Scene(root, 300, 250);

    primaryStage.setScene(scene);
    primaryStage.show();
  }

  private void populateTable(TableView<Person> tableView) throws SQLException {
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM people");

        while (resultSet.next()) {
            int id = resultSet.getInt("id");
            String name = resultSet.getString("name");
            tableView.getItems().add(new Person(id, name));
        }

        resultSet.close();
        statement.close();
    }

    @Override
    public void stop() {
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

  public static void main(String[] args) {
    launch(args);
  }
}