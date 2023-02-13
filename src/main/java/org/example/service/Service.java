package org.example.service;


import events.ChangeEventType;
import events.UtilizatorEntityChangeEvent;
import jdk.jshell.execution.Util;
import org.example.domain.Mesaj;
import org.example.domain.Prietenie;
import org.example.domain.PrietenieUI;
import org.example.domain.Utilizator;
import org.example.repository.RepoException;
import org.example.repository.db.MessageDbRepository;
import org.example.repository.db.PrietenieDbRepository;
import org.example.repository.db.UtilizatorDbRepository;
import org.example.utils.Observable;
import org.example.utils.Observer;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Clasa controller asigura comunicarea intre UI si Repo
 */
public class Service implements Observable{
    private UtilizatorDbRepository repo;
    private PrietenieDbRepository repoFr;

    private MessageDbRepository repoMsg;

    public Service(UtilizatorDbRepository repo, PrietenieDbRepository repoFr, MessageDbRepository repoMsg) {
        this.repo = repo;
        this.repoFr = repoFr;
        this.repoMsg = repoMsg;
    }

    private List<Observer> obs=new ArrayList<>();

    /**
     * Creeaza si trimite un user pentru adaugare
     * @param firstName - prenume
     * @param lastName - nume
     * @param id - id-ul utilizatorului
     * @throws IllegalArgumentException de la repo
     * @throws org.example.domain.validators.ValidationException datele nu sunt valide
     */
    public void addUser(String firstName, String lastName, Long id)
    {
        Utilizator u = new Utilizator(firstName, lastName);
        u.setId(id);
        if(this.repo.save(u) != null)
        {
            throw new RepoException("Acest ID exista deja");
        }
        notifyObservers();
    }

    public void addPassword(Utilizator u, String password)
    {
        repo.savePassword(u, password);
        notifyObservers();
    }

    /**
     *
     * @param id -user id
     */
    public void removeUser(Long id)
    {
        Utilizator u = this.repo.findOne(id);
        this.repo.delete(id);

    }

    /**
     *
     * @param id
     */
    public void updateUser(Long id, String firstname, String lastname)
    {
        Utilizator u = new Utilizator(firstname, lastname);
        u.setId(id);
        this.repo.update(u);

    }

    /**
     * cauta utilizatorul cu id-ul dat
     * @param id -id de utlilizator
     * @return Utlizator, util gasit sau null
     * @throws IllegalArgumentException daca id e null
     */
    public Utilizator findUser(Long id)
    {
        return repo.findOne(id);
    }

    public Utilizator findUserByName(String firstname, String lastname)
    {
        return repo.findOneByName(firstname, lastname);
    }

    public String getPasswordForUser(Utilizator u){
        return this.repo.getPasswordForUser(u);
    }

    /**
     *
     * @return all users
     */
    public Iterable<Utilizator> getAll() {
        return repo.findAll();
    }

    public Iterable<Utilizator> getNotFriends(Utilizator u){
        Set<Utilizator> notFriends = new HashSet<>();
        List<Utilizator> users = new ArrayList<>();
        getAll().forEach(x->users.add(x));
        for(Utilizator u1 : users) {
            boolean ok = true;
            for(Utilizator u2 : u1.getFriends())
                if(u2.getId() == u.getId())
                {
                    ok = false;
                    break;
                }


            if (ok && u1.getId() != u.getId())
                notFriends.add(u1);
        }

        return notFriends;

    }

    public Iterable<Prietenie> getAllFriendshipsForUser(Utilizator u){
        return repoFr.findAllFriendsForUser(u);
    }

    public void acceptFriend(Long idUser, Long idFriend)
    {
        Utilizator user = repo.findOne(idUser);
        Utilizator friend = repo.findOne(idFriend);
        Prietenie p = repoFr.findOne2(idUser, idFriend);
        p.setStatus(1L);
        repoFr.update(p);
        notifyObservers();
    }
    /**
     *
     * @param idUser user-ul caruia i se adauga prieten
     * @param idFriend id-ul viitorului prieten
     */
    public void addFriend(Long idUser, Long idFriend){
        Utilizator user = repo.findOne(idUser);
        Utilizator friend = repo.findOne(idFriend);
        if(user.getFriends().indexOf(friend) == -1)
            user.addFriend(friend);
        if(friend.getFriends().indexOf(user) == -1)
            friend.addFriend(user);
        Prietenie p = new Prietenie(user, friend, LocalDateTime.now());
        long aux = Long.valueOf(friend.getId());
        int c = 1;
        while(aux > 0)
        {
            aux /= 10;
            c *= 10;
        }
        p.setId(user.getId()*c+friend.getId());
        repoFr.save(p);
        notifyObservers();

    }

    /**
     *
     * @param idUser id-ul userului caruia i se va sterge prietenul idFriend
     * @param idFriend prietenul de sters
     */
    public void removeFriend(Long idUser, Long idFriend){
        Utilizator user = repo.findOne(idUser);
        Utilizator friend = repo.findOne(idFriend);
        try {
            user.removeFriend(friend);
            friend.removeFriend(user);
        }catch(RuntimeException e){};
        long aux = Long.valueOf(friend.getId());
        int c = 1;
        while(aux > 0)
        {
            aux /= 10;
            c *= 10;
        }
        long aux1 = Long.valueOf(user.getId());
        int c1 = 1;
        while(aux1 > 0)
        {
            aux1 /= 10;
            c1 *= 10;
        }
        repoFr.delete(user.getId()*c+friend.getId());
        repoFr.delete(user.getId()+friend.getId()*c1);
        notifyObservers();
    }

    /**
     *
     * @param id - id-ul userului pt. care se doreste lista de prieteni
     * @return lista de prieteni
     */
    public List<Utilizator> getFriendsForUser(Long id){
        List<Utilizator> users = new ArrayList<>();
        getAll().forEach(x->users.add(x));
        Utilizator u = null;
        for(Utilizator u1 : users)
            if(u1.getId() == id)
                u = u1;

        if(u == null)
            return new ArrayList<>();
        return u.getFriends();
    }

    /**
     *
     * @return int, numarul de comunitati
     */
    public int getCommunities() {
        List<Utilizator> users = new ArrayList<>();
        getAll().forEach(x-> users.add(x));
        CommunitiesAuxiliary com = new CommunitiesAuxiliary(users);
        return com.communities();
    }

    /**
     *
     * @return lista utilizatorilor din cea mai sociabila comunitate
     */
    public List<Utilizator> findMostSociableCommunity() {
        List<Utilizator> users = new ArrayList<>();
        getAll().forEach(x-> users.add(x));
        CommunitiesAuxiliary com = new CommunitiesAuxiliary(users);
        return com.mostSociableCommunity2();
    }


    public List<Mesaj> getMessages(Utilizator loggedIn, Utilizator friend)
    {
        List<Mesaj> msgs = new ArrayList<>();
        repoMsg.findForUsers(loggedIn, friend).forEach(x->msgs.add(x));
        return msgs;
    }

    public void addMessage(Utilizator sender, Utilizator receiver, String content, LocalDateTime date)
    {
        Mesaj m = new Mesaj(sender, receiver, content, date);
        this.repoMsg.save(m);
        //this.notifyObservers();
    }

    @Override
    public void addObserver(Observer e) {
        obs.add(e);
    }

    @Override
    public void removeObserver(Observer e) {
        obs.remove(e);
    }

    @Override
    public void notifyObservers() {
        obs.stream().forEach(x->x.update());
    }
}
