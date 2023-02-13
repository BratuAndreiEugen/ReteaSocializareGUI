package org.example.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Utilizator extends Entity<Long>{
    private String firstName;
    private String lastName;
    private List<Utilizator> friends = new ArrayList<>();

    /**
     * Constructor
     * @param firstName
     * @param lastName
     */
    public Utilizator(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    /**
     * Getter pt. prenume
     * @return
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Setter pt. prenume
     * @param firstName
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * Getter pt. nume
     * @return
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Setter pt. nume
     * @param lastName
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     *
     * @return lista de prieteni a utilizatorului
     *
     */
    public List<Utilizator> getFriends() {
        return friends;
    }

    @Override
    public String toString() {
        return "Utilizator{" + "id='" + getId() + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", friends=" + friends.size() +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Utilizator)) return false;
        Utilizator that = (Utilizator) o;
        return getFirstName().equals(that.getFirstName()) &&
                getLastName().equals(that.getLastName()) &&
                getFriends().equals(that.getFriends());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }

    /**
     * adauga un prieten
     * @param u utilizatorul de adaugat
     */
    public void addFriend(Utilizator u)
    {
        if(!friends.contains(u))
            friends.add(u);
        else throw new RuntimeException("Deja sunt prieteni");
    }

    /**
     * sterge un prieten
     * @param u utilizatorul de sters
     */
    public void removeFriend(Utilizator u)
    {
        if(friends.contains(u))
            friends.remove(u);
        else throw new RuntimeException("Nu sunt prieteni");
    }
}
