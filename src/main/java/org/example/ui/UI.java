package org.example.ui;

import org.example.domain.Utilizator;
import org.example.service.Service;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class UI {
    private Service service;

    public UI(Service service) {
        this.service = service;
    }

    public void printOptions()
    {
        System.out.println("Optiuni : ");
        System.out.println("1. Adauga utilizator");
        System.out.println("2. Sterge utilizator");
        System.out.println("3. Adauga prieten");
        System.out.println("4. Sterge prieten");
        System.out.println("5. Afiseaza numarul de comunitati");
        System.out.println("6. Afiseaza cea mai sociabila comunitate");
        System.out.println("7. Afiseaza toti utilizatorii");
        System.out.println("8. Afiseaza prietenii pentru un user");
        System.out.println("9. Schimba datele unui utilizator");
    }

    private void adaugaUtilizator(Scanner s){


        System.out.print("Introduceti prenume : ");
        String firstName = s.next();
        System.out.print("Introduceti nume : ");
        String lastName = s.next();
        System.out.print("Introduceti ID : ");
        long id = 0;
        try {
            id = Long.parseLong(s.next());
        }catch(NumberFormatException e)
        {
            System.out.println(e.getMessage());
            return;
        }

        try{
            this.service.addUser(firstName, lastName, id);
        }catch(RuntimeException e)
        {
            System.out.println(e.getMessage());
            return;
        }

    }

    private void stergeUtilizator(Scanner s){


        System.out.print("Introduceti ID : ");
        long id = 0;
        try {
            id = Long.parseLong(s.next());
        }catch(NumberFormatException e)
        {
            System.out.println(e.getMessage());
            return;
        }

        try{
            this.service.removeUser(id);
        }catch(RuntimeException e)
        {
            System.out.println(e.getMessage());
            return;
        }

    }

    private void updateUtilizator(Scanner s){


        System.out.print("Introduceti ID : ");
        long id = 0;
        try {
            id = Long.parseLong(s.next());
        }catch(NumberFormatException e)
        {
            System.out.println(e.getMessage());
            return;
        }
        System.out.print("Introduceti prenume nou: ");
        String firstName = s.next();
        System.out.print("Introduceti nume nou: ");
        String lastName = s.next();

        try{
            this.service.updateUser(id, firstName, lastName);
        }catch(RuntimeException e)
        {
            System.out.println(e.getMessage());
            return;
        }

    }

    private void adaugaPrieten(Scanner s){


        System.out.print("Introduceti ID-ul userului : ");
        long idUser, idFriend;
        try {
            idUser = Long.parseLong(s.next());
            System.out.print("Introduceti ID-ul prietenului : ");
            idFriend = Long.parseLong(s.next());
        }catch(NumberFormatException e)
        {
            System.out.println(e.getMessage());
            return;
        }


        try{
            this.service.addFriend(idUser, idFriend);
        }catch(RuntimeException e)
        {
            System.out.println(e.getMessage());
            return;
        }

    }

    private void stergePrieten(Scanner s){


        System.out.print("Introduceti ID-ul userului : ");
        long idUser, idFriend;
        try {
            idUser = Long.parseLong(s.next());
            System.out.print("Introduceti ID-ul prietenului : ");
            idFriend = Long.parseLong(s.next());
        }catch(NumberFormatException e)
        {
            System.out.println(e.getMessage());
            return;
        }

        try{
            this.service.removeFriend(idUser, idFriend);
        }catch(RuntimeException e)
        {
            System.out.println(e.getMessage());
            return;
        }

    }


    private void afiseazaPrieteni(Scanner s){
        long idUser;
        try {
            System.out.print("Introduceti ID-ul userului : ");
            idUser = 0;
            try {
                idUser = Long.parseLong(s.next());
            }catch(NumberFormatException e)
            {
                System.out.println(e.getMessage());
                return;
            }

            List<Utilizator> friends = service.getFriendsForUser(idUser);
            for(Utilizator f : friends)
            {
                System.out.println(f);
            }
        }catch (RuntimeException e)
        {
            System.out.println(e.getMessage());
            return;
        }
    }

    public void loop() {

        while(true) {
            this.printOptions();
            Long option;
            Scanner scan = new Scanner(System.in);
            try {
                option = Long.parseLong(scan.next());

                System.out.println(option);
                switch (option.intValue()) {
                    case 1:
                        adaugaUtilizator(scan);
                        break;
                    case 2:
                        stergeUtilizator(scan);
                        break;
                    case 3:
                        adaugaPrieten(scan);
                        break;
                    case 4:
                        stergePrieten(scan);
                        break;
                    case 5:
                        System.out.println("Reteaua are " + service.getCommunities() + " comunitati");
                        break;
                    case 6:
                        System.out.println("Cea mai sociabila comunitate");
                        for(Utilizator u : service.findMostSociableCommunity())
                            System.out.println(u);
                        break;
                    case 7:
                        service.getAll().forEach(x-> System.out.println(x));
                        break;
                    case 8:
                        afiseazaPrieteni(scan);
                        break;
                    case 9:
                        updateUtilizator(scan);
                        break;
                    default:
                        return;
                }
            } catch (RuntimeException  e) {
                System.out.println(e.getMessage());
            }



        }
    }
}
