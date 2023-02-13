package org.example.service;


import org.example.domain.Utilizator;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Clasa pentru efectuarea operatiilor pe graful retelei
 */
public class CommunitiesAuxiliary {
    List<Utilizator> users;
    List<Utilizator> dumps = new ArrayList<>();


    /**
     * Constructor
     * @param users lista de utilizatori
     */
    public CommunitiesAuxiliary(List<Utilizator> users) {
        this.users = users;
    }

    /**
     * Partea recursiva a DFS
     * @param v vectorul de vizitari
     * @param poz pozitia din this.users a utilizatorului curent
     */
    private void recursive(boolean[] v, int poz)
    {
        dumps.add(users.get(poz));
        v[poz] = true;
        Utilizator curent = users.get(poz);
        List<Utilizator> adj = curent.getFriends();
        for(Utilizator fr : adj)
        {
            if(v[users.indexOf(fr)] == false) {
                recursive(v, users.indexOf(fr));

            }
        }
    }

    /**
     * DFS care numara cate comunitati izolate sunt in graful retelei
     * @return int numarul de comunitati
     */
    public int communities()
    {
        int commCount = 0;

        int i = 0;
        boolean[] v = new boolean[users.size()];
        for(i = 0; i < users.size(); i++)
            v[i] = false;
        for(i = 0; i < users.size(); i++)
        {
            if(v[i] == false){

                recursive(v, i);
                commCount ++;
            }

        }

        return commCount;
    }

    /**
     * Algoritm care foloseste DFS pentru a determina cea mai sociabila comunitate
     * @return lista cu userii celei mai sociabile comunitati
     */
    public List<Utilizator> mostSociableCommunity(){

        List<Utilizator> mostSociable = new ArrayList<>();
        int i = 0;
        int max = 0;
        boolean[] v = new boolean[users.size()];
        dumps.clear();
        for(i = 0; i < users.size(); i++)
        {
            if(v[i] == false){
                recursive(v, i);
            }
            if(dumps.size() > max)
            {
                max = dumps.size();
                mostSociable = List.copyOf(dumps);

            }
            else {
                if(dumps.size() == max){
                    int pmS = 0, pD = 0;
                    for(Utilizator u : mostSociable)
                    {
                        pmS += u.getFriends().size();
                    }
                    for(Utilizator u : dumps)
                    {
                        pD += u.getFriends().size();
                    }
                    pmS /= 2;
                    pD /= 2;
                    if(pD > pmS)
                        mostSociable = List.copyOf(dumps);

                }
            }
            dumps.clear();
        }


        return mostSociable;
    }

    /**
     * Clasa folosita in BFS reprezinta o pereche de int
     */
    class Pair{
        public int nod;
        public int len;

        public Pair(int nod, int len) {
            this.nod = nod;
            this.len = len;
        }
    }

    /**
     *
     * @param s pozitie in lista dumps
     * @return lungimea drumului cel mai lung de la s la un alt nod
     */
    private int BFSmS(int s)
    {

        boolean v[] = new boolean[dumps.size()];


        LinkedList<Pair> queue = new LinkedList<Pair>();

        v[s] = true;
        queue.add(new Pair(s, 0));
        int len = 0;
        int maxLen = 0;

        while (queue.size() != 0)
        {
            Pair cr = queue.poll();
            Utilizator u = dumps.get(cr.nod);
            if(cr.len > maxLen)
                maxLen = cr.len;
            for(Utilizator fr : u.getFriends())
            {
                if(v[dumps.indexOf(fr)] == false)
                {
                    v[dumps.indexOf(fr)] = true;
                    queue.add(new Pair(dumps.indexOf(fr), cr.len + 1));
                }
            }
        }

        return maxLen;
    }


    /**
     * Algoritm care foloseste BFS pentru a determina cea mai sociabila comunitate
     * ( In functie de cel mai lung drum )
     * @return lista cu userii celei mai sociabile comunitati
     */
    public List<Utilizator> mostSociableCommunity2(){
        List<Utilizator> mostSociable = new ArrayList<>();
        int i = 0;
        int max = 0;
        boolean[] v = new boolean[users.size()];
        dumps.clear();
        for(i = 0; i < users.size(); i++)
        {
            if(v[i] == false){
                recursive(v, i);
            }
            int lMax = 0;
            int len = 0;
            for(int j = 0; j < dumps.size(); j++)
            {
                len = BFSmS(j);
                if(len > lMax)
                    lMax = len;
            }
            if(lMax > max)
            {
                max = lMax;
                mostSociable = List.copyOf(dumps);
            }
            dumps.clear();



        }
        return mostSociable;
    }


}
