package org.example.repository.db;



import org.example.domain.Utilizator;
import org.example.domain.validators.Validator;
import org.example.repository.memory.InMemoryRepository;
import repository.Repository;

import java.sql.*;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class UtilizatorDbRepository implements Repository<Long, Utilizator> {
    private String url;
    private String username;
    private String password;
    private Validator<Utilizator> validator;


    public UtilizatorDbRepository(String url, String username, String password, Validator<Utilizator> validator) {
        this.url = url;
        this.username = username;
        this.password = password;
        this.validator = validator;
    }

    @Override
    public Utilizator findOne(Long id) {
        Utilizator utilizator = null;
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("SELECT * from users WHERE id = ? ");) {

            statement.setLong(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {

                while (resultSet.next()) {
                    String firstName = resultSet.getString("first_name");
                    String lastName = resultSet.getString("last_name");

                    utilizator = new Utilizator(firstName, lastName);
                    utilizator.setId(id);


                    return utilizator;

                }

            } catch (SQLException e) {
                throw e;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
        return utilizator;
    }

    @Override
    public Iterable<Utilizator> findAll() {
        Set<Utilizator> users = new HashSet<>();
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("SELECT * from users");
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                Long id = resultSet.getLong("id");
                String firstName = resultSet.getString("first_name");
                String lastName = resultSet.getString("last_name");

                Utilizator utilizator = new Utilizator(firstName, lastName);
                utilizator.setId(id);


                users.add(utilizator);
            }
            Set<Utilizator> aux = Set.copyOf(users);
            for (Utilizator u : users) {
                Long id = u.getId();
                PreparedStatement statementFr = connection.prepareStatement("SELECT * from friendships WHERE usera = ? or userb = ? ");
                statementFr.setLong(1, id);
                statementFr.setLong(2, id);
                ResultSet s1 = statementFr.executeQuery();
                while (s1.next()) {
                    Long id1 = s1.getLong("usera");
                    Long id2 = s1.getLong("userb");
                    Long idF;
                    if (id1 == id)
                        idF = id2;
                    else
                        idF = id1;

                    for (Utilizator u1 : aux) {
                        if (u1.getId() == idF) {
                            if (!u.getFriends().contains(u1))
                                u.addFriend(u1);
                            if (!u1.getFriends().contains(u))
                                u1.addFriend(u);
                        }
                    }
                }
            }

            return users;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }

    @Override
    public Utilizator save(Utilizator entity) {

        String sql = "insert into users (first_name, last_name) values (?, ?)";
        validator.validate(entity);
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setString(1, entity.getFirstName());
            ps.setString(2, entity.getLastName());

            ps.executeUpdate();
        } catch (SQLException e) {
            //e.printStackTrace();
            return null;
        }
        return null;
    }

    public String savePassword(Utilizator entity, String parola) {

        String sql = "insert into passwords (userid, password) values (?, ?)";

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setLong(1, entity.getId());
            ps.setString(2, parola);

            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
        return null;
    }


    @Override
    public Utilizator delete(Long id) {
        Utilizator u = findOne(id);
        if (u == null)
            return null;
        String sql = "delete from users where id = ?";
        String sql1 = "delete from friendships where usera = ? or userb = ?";
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement ps = connection.prepareStatement(sql);
             PreparedStatement ps1 = connection.prepareStatement(sql1);
        ) {

            ps.setLong(1, id);
            ps1.setLong(1, id);
            ps1.setLong(2, id);


            ps.executeUpdate();
            ps1.executeUpdate();
            return u;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * @param entity entity must not be null
     * @return datele vechi ale utilizatorului
     */
    @Override
    public Utilizator update(Utilizator entity) {

        Utilizator u = findOne(entity.getId());
        if (u == null)
            return null;
        String sql = "update users set first_name = ?, last_name = ? where id = ?";
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement ps = connection.prepareStatement(sql);
        ) {

            ps.setString(1, entity.getFirstName());
            ps.setString(2, entity.getLastName());
            ps.setLong(3, entity.getId());


            ps.executeUpdate();
            return u;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public Utilizator findOneByName(String firstname, String lastname) {
        Utilizator utilizator = null;
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("SELECT * from users WHERE first_name = ? AND last_name = ?");) {

            statement.setString(1, firstname);
            statement.setString(2, lastname);
            try (ResultSet resultSet = statement.executeQuery()) {

                while (resultSet.next()) {
                    Long id = resultSet.getLong("id");
                    String firstName = resultSet.getString("first_name");
                    String lastName = resultSet.getString("last_name");

                    utilizator = new Utilizator(firstName, lastName);
                    utilizator.setId(id);


                    return utilizator;

                }

            } catch (SQLException e) {
                throw e;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
        return utilizator;
    }

    public String getPasswordForUser(Utilizator u) {
        String passwordU = null;
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("SELECT * from passwords WHERE userid = ?");) {

            statement.setLong(1, u.getId());
            try (ResultSet resultSet = statement.executeQuery()) {

                while (resultSet.next()) {
                    Long id = resultSet.getLong("userid");
                    passwordU = resultSet.getString("password");

                    return passwordU;

                }

            } catch (SQLException e) {
                throw e;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
        return passwordU;
    }


}
