package org.example;


import org.example.domain.Prietenie;
import org.example.domain.Utilizator;
import org.example.domain.validators.PrietenieValidator;
import org.example.domain.validators.UtilizatorValidator;
import org.example.domain.validators.Validator;
import org.example.repository.db.PrietenieDbRepository;
import org.example.repository.db.UtilizatorDbRepository;
import org.example.service.Service;
import org.example.ui.UI;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class Main {
    public static void main(String[] args) throws IOException {
        /*
        System.out.println("Hello world!");
        ValidationException v = new ValidationException("NU E BINE");
        System.out.println(v.getMessage());

        Validator<Utilizator> val = new UtilizatorValidator();
        Repository<Long, Utilizator> repo = new InMemoryRepository(val);
        Utilizator p1 = new Utilizator("u1", "l1");
        System.out.println(p1.getId());
        p1.setId(1l);
        Utilizator p2 = new Utilizator("u2", "l2");
        p2.setId(2l);
        Utilizator p3 = new Utilizator("u3", "l3");
        p3.setId(3l);

        try {
            repo.save(p1);
            repo.save(p2);
            repo.save(p3);
        }catch(ValidationException e){
            System.out.println(e.getMessage());
        }
        try {
            repo.delete(null);
        }catch(IllegalArgumentException e)
        {
            System.out.println(e.getMessage());
        }
        //repo.findAll().forEach(System.out::println);
        repo.findAll().forEach(x-> System.out.println(x));*/

        /*
        String username = "postgres";
        String password = "Andreas14321";
        String url = "jdbc:postgresql://localhost:5432/socialnetwork";
        Validator<Utilizator> val = new UtilizatorValidator();
        Validator<Prietenie> valP = new PrietenieValidator();
        //Repository<Long, Utilizator> repo = new InMemoryRepository(val);
        //UtilizatorFile repo = new UtilizatorFile("src/files/users.txt",val);
        UtilizatorDbRepository repo = new UtilizatorDbRepository(url, username, password, val);
        //PrietenieFile repoP = new PrietenieFile("src/files/prieteni.txt", valP, repo);
        PrietenieDbRepository repoP = new PrietenieDbRepository(url, username, password, valP, repo);
        Service service = new Service(repo, repoP);


        service.addUser("Andrei", "B", 1L);
        service.addUser("Eva", "S", 2L);
        service.addUser("Radu", "L", 3L);
        service.addUser("Adrian", "B", 4L);
        service.addUser("Alin", "P", 5L);
        service.addUser("Ana", "V", 6L);
        service.addUser("Corina", "T", 7L);

        service.addFriend(1L, 2L);
        service.addFriend(1L, 3L);
        service.addFriend(3L, 2L);
        service.addFriend(6L, 5L);
        service.addFriend(6L, 7L);
        */

        //UI ui = new UI(service);
        //System.out.println(repoP.findOne(21L));


//        //ui.loop();
//        String s = "Andrei      B";
//        String[] arr = s.split(" +");
//
//        for ( String ss : arr) {
//            System.out.println(ss);
//        }
//        System.out.println(repo.findOneByName("Andrei", "B"));
//        System.out.println(repo.getPasswordForUser(repo.findOneByName("Andrei", "B")));

        RunApp.main(args);


        //LocalDateTime n = LocalDateTime.now();

    }
}