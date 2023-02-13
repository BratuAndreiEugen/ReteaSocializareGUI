package org.example.repository.db;


import org.example.domain.Prietenie;
import org.example.domain.Utilizator;
import org.example.domain.validators.Validator;
import org.example.repository.memory.InMemoryRepository;
import repository.Repository;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

public class PrietenieDbRepository implements Repository<Long, Prietenie> {
    private String url;
    private String username;
    private String password;

    private Validator<Prietenie> validator;

    private Repository<Long, Utilizator> userRepo;

    public PrietenieDbRepository(String url, String username, String password, Validator<Prietenie> validator, Repository<Long, Utilizator> repo) {
        this.url = url;
        this.username = username;
        this.password = password;
        this.validator = validator;
        this.userRepo = repo;
    }
    @Override
    public Prietenie findOne(Long id) {
        Prietenie f = null;
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("SELECT * from friendships WHERE id = ?");) {

            statement.setLong(1,id);
            try(ResultSet resultSet = statement.executeQuery()) {

                while (resultSet.next()) {
                    Long u1 = resultSet.getLong("usera");
                    Long u2 = resultSet.getLong("userb");
                    Long idd = resultSet.getLong("id");
                    LocalDateTime date = resultSet.getTimestamp("friendsfrom").toLocalDateTime();
                    Long status = resultSet.getLong("status");
                    Prietenie fren = new Prietenie(userRepo.findOne(u1), userRepo.findOne(u2), date);
                    fren.setStatus(status);
                    return fren;

                }

            }catch (SQLException e) {
                throw e;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
        return null;
    }


    public Prietenie findOne2(Long id1, Long id2) {
        Prietenie f = null;
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("SELECT * from friendships WHERE usera = ? AND userb = ?");) {

            statement.setLong(1,id1);
            statement.setLong(2,id2);
            try(ResultSet resultSet = statement.executeQuery()) {

                while (resultSet.next()) {
                    Long u1 = resultSet.getLong("usera");
                    Long u2 = resultSet.getLong("userb");
                    Long idd = resultSet.getLong("id");
                    LocalDateTime date = resultSet.getTimestamp("friendsfrom").toLocalDateTime();
                    Long status = resultSet.getLong("status");
                    Prietenie fren = new Prietenie(userRepo.findOne(u1), userRepo.findOne(u2), date);
                    fren.setStatus(status);
                    return fren;

                }

            }catch (SQLException e) {
                throw e;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
        return null;
    }

    @Override
    public Iterable<Prietenie> findAll() {
        Set<Prietenie> frens = new HashSet<>();
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("SELECT * from friendships");
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                Long u1 = resultSet.getLong("usera");
                Long u2 = resultSet.getLong("userb");
                Long idd = resultSet.getLong("id");
                LocalDateTime date = resultSet.getTimestamp("date").toLocalDateTime();
                Long status = resultSet.getLong("status");
                Prietenie fren = new Prietenie(userRepo.findOne(u1), userRepo.findOne(u2), date);
                fren.setId(u1*10 + u2);
                fren.setStatus(status);




                frens.add(fren);
            }


            return frens;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return frens;
    }

    @Override
    public Prietenie save(Prietenie entity) {
        Prietenie p = findOne(entity.getId());
        if(p != null)
            return p;
        String sql = "insert into friendships (usera, userb, id, friendsfrom) values (?, ?, ?, ?)";
        validator.validate(entity);
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setLong(1, entity.getUser1().getId());
            ps.setLong(2, entity.getUser2().getId());
            ps.setLong(3, entity.getId());
            ps.setTimestamp(4,Timestamp.valueOf(entity.getDate()));


            ps.executeUpdate();
        } catch (SQLException e) {
            //e.printStackTrace();
            return null;
        }
        return null;
    }

    @Override
    public Prietenie delete(Long id) {
        Prietenie p = findOne(id);
        if(p == null)
            return null;
        String sql = "delete from friendships where id = ?";
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement ps = connection.prepareStatement(sql);
        ) {

            ps.setLong(1, id);

            ps.executeUpdate();
            return p;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public Prietenie update(Prietenie entity) {
        Prietenie p = findOne2(entity.getUser1().getId(), entity.getUser2().getId());
        if(p == null)
            return null;
        String sql = "update friendships set usera = ?, userb = ?, status = ? where usera = ? AND userb = ?";
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement ps = connection.prepareStatement(sql);
        ) {
            ps.setLong(1, entity.getUser1().getId());
            ps.setLong(2, entity.getUser2().getId());
            ps.setLong(3, 1L);
            ps.setLong(4, entity.getUser1().getId());
            ps.setLong(5, entity.getUser2().getId());

            ps.executeUpdate();
            return p;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }


    public Iterable<Prietenie> findAllFriendsForUser(Utilizator u) {
        Set<Prietenie> frens = new HashSet<>();
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("SELECT * from friendships WHERE usera = ? OR userb = ?");
             ) {
            statement.setLong(1, u.getId());
            statement.setLong(2, u.getId());
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Long u1 = resultSet.getLong("usera");
                Long u2 = resultSet.getLong("userb");
                Long idd = resultSet.getLong("id");
                LocalDateTime date = resultSet.getTimestamp("friendsfrom").toLocalDateTime();
                Long status = resultSet.getLong("status");
                Prietenie fren = new Prietenie(userRepo.findOne(u1), userRepo.findOne(u2), date);
                fren.setId(u1*10 + u2);
                fren.setStatus(status);




                frens.add(fren);
            }


            return frens;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return frens;
    }
}


