package ru.taranov.vacancyparser;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.util.List;

public class DBConnect implements AutoCloseable {

    private static final Logger LOG = LogManager.getLogger(DBConnect.class.getName());
    private final Config config;
    private Connection connection;

    public DBConnect() {
        this.config = new Config();
        init();
        createTable();
    }

    public boolean init() {
        try {
            Class.forName(this.config.getValue("driver-class-name"));
            this.connection = DriverManager.getConnection(
                    this.config.getValue("url"),
                    this.config.getValue("username"),
                    this.config.getValue("password"));
        } catch (Exception e) {
            LOG.warn(e.getMessage(), e);
        }
        return this.connection != null;
    }

    public void addVacancies(List<Vacancy> vacanciesList) {
        try (PreparedStatement st = this.connection.prepareStatement("INSERT INTO "
                + "vacancy(name_vacancy, text_vacancy, link_vacancy, date_vacancy) values (?, ?, ?, ?)")) {
            for (Vacancy vac : vacanciesList) {
                st.setString(1, vac.getName());
                st.setString(2, vac.getText());
                st.setString(3, vac.getLink());
                st.setDate(4, Date.valueOf(String.valueOf(vac.getDate())));
                st.addBatch();
            }
            st.executeBatch();
        } catch (SQLException e) {
            LOG.warn(e.getMessage(), e);
        }
    }

    public void deleteDuplicates() {
        try (Statement state = this.connection.createStatement()) {
            state.executeUpdate("DELETE v1 FROM vacancy as v1, vacancy as v2 "
                    + "WHERE v1.id < v2.id AND v1.name = v2.name");
        } catch (SQLException e) {
            LOG.warn(e.getMessage(), e);
        }
    }

    public void createTable() {
        try (Statement state = this.connection.createStatement()) {
            state.executeUpdate("CREATE TABLE IF NOT EXISTS "
                    + "vacancy(id serial primary key,"
                    + " name_vacancy varchar(200),"
                    + " text_vacancy varchar(3000),"
                    + " link_vacancy varchar(300),"
                    + " date_vacancy varchar(200));");
        } catch (SQLException e) {
            LOG.warn(e.getMessage(), e);
        }
    }

    @Override
    public void close() throws Exception {
        if (connection != null) {
            this.connection.close();
        }
    }
}
