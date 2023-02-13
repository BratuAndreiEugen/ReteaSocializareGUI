package org.example.repository.db;

import org.example.domain.Mesaj;
import org.example.domain.Prietenie;
import org.example.domain.Utilizator;
import repository.Repository;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class MessageDbRepository implements repository.Repository<Long, Mesaj> {

    private String url;
    private String username;
    private String password;

    private repository.Repository<Long, Utilizator> userRepo;

    public MessageDbRepository(String url, String username, String password, Repository<Long, Utilizator> userRepo) {
        this.url = url;
        this.username = username;
        this.password = password;
        this.userRepo = userRepo;
    }

    @Override
    public Mesaj findOne(Long aLong) {
        return null;
    }

    @Override
    public Iterable<Mesaj> findAll() {
        return null;
    }

    @Override
    public Mesaj save(Mesaj entity) {
        String sql = "insert into messages (sender, receiver, content, date) values (?, ?, ?, ?)";
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setLong(1, entity.getSender().getId());
            ps.setLong(2, entity.getReceiver().getId());
            ps.setString(3, entity.getContent());
            ps.setTimestamp(4, Timestamp.valueOf(entity.getDate()));

            ps.executeUpdate();

        }catch(SQLException e)
        {
            return null;
        }
        return null;
    }

    @Override
    public Mesaj delete(Long aLong) {
        return null;
    }

    @Override
    public Mesaj update(Mesaj entity) {
        return null;
    }

    public Iterable<Mesaj> findForUsers(Utilizator u1, Utilizator u2){
        List<Mesaj> msgs = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("SELECT * from messages WHERE (sender = ? AND receiver = ?) OR (sender = ? AND receiver = ?)");
        ){
            statement.setLong(1, u1.getId());
            statement.setLong(2, u2.getId());
            statement.setLong(3, u2.getId());
            statement.setLong(4, u1.getId());
            ResultSet resultSet = statement.executeQuery();
            while(resultSet.next())
            {
                Long id1 = resultSet.getLong("sender");
                Long id2 = resultSet.getLong("receiver");
                String content = resultSet.getString("content");
                LocalDateTime date = resultSet.getTimestamp("date").toLocalDateTime();
                if(id1 == u1.getId())
                {
                    msgs.add(new Mesaj(u1, u2, content, date));
                }
                else
                {
                    msgs.add(new Mesaj(u2, u1, content, date));
                }
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return msgs;
    }
}
