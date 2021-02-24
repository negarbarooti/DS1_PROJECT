package com.company;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.io.*;


class Graph {
    HashMap<String, Vertex> Vertex_map = new HashMap<>();
    HashMap<String, Edge> Edge_map = new HashMap<>();
}

public class Main {

    // faz3 : find path
    // localpathlist  : include all vertex in path but faz3 only suspects
    public static void check_AllPaths(account u,
                                      HashMap<String, Boolean> isVisited,
                                      ArrayList<account> localPathList,
                                      HashMap<String, Edge> Edge_map,
                                      HashMap<String, person> faz2, ArrayList<String> faz3, int count) {
        if (faz2.containsKey(u.ssn)) {
            if (0 < localPathList.size() && localPathList.size() < 6) {
                faz3.add(count, localPathList.get(localPathList.size() - 1).ssn);
                count++;
                return;
            }
        } else if (localPathList.size() > 4)
            return;

        ArrayList<account> adjList = find_adjList(u, Edge_map, isVisited);
        if (adjList.size() == 0) {
            return;

        }
        isVisited.put(u.account_id, true);


        for (int j = 0; j < adjList.size(); j++) {
            account i = adjList.get(j);
            if (!isVisited.get(i.account_id)) {

                localPathList.add(i);
                check_AllPaths(i, isVisited, localPathList, Edge_map, faz2, faz3, count);

                localPathList.remove(i);
            }
        }
        isVisited.put(u.account_id, false);
    }

    // faz3
    public static ArrayList<account> find_adjList(account s, HashMap<String, Edge> Edge_map,
                                                  HashMap<String, Boolean> isVisited) {
        ArrayList<account> adjList = new ArrayList<>();

        for (Map.Entry<String, Edge> mapElement : Edge_map.entrySet()) {

            if (mapElement.getKey().length() == 9 && mapElement.getKey().charAt(8) == '1') {

                transactions t = (transactions) mapElement.getValue();
                if (t.from.account_id.compareTo(s.account_id) == 0) {
                    adjList.add(t.to);
                    isVisited.put(t.to.account_id, false);
                }

            }
        }

        return adjList;
    }

    //faz3
    public static Boolean is_smuggler(String n, HashMap<String, Vertex> Vertex_map) {

        person p = (person) Vertex_map.get(n);
        if (p.work.length() == 9) {
            if (p.work.substring(1, 8).compareTo("قاچاقچی") == 0) {
                return true;

            } else return true;
        } else return false;

    }

    //faz3
    public static ArrayList<account> accounts_smuggler(HashMap<String, Vertex> Vertex_map) {

        ArrayList<account> accounts_of_smuggler = new ArrayList<>();
        for (Map.Entry<String, Vertex> mapElement : Vertex_map.entrySet()) {
            if (mapElement.getKey().length() == 8 && 48 <= mapElement.getKey().charAt(3) &&
                    mapElement.getKey().charAt(3) <= 57) {

                account a = (account) mapElement.getValue();
                if (is_smuggler(a.ssn, Vertex_map))
                    accounts_of_smuggler.add(a);

            }
        }

        return accounts_of_smuggler;
    }

    // faz4
    public static ArrayList<String> check_contact(HashMap<String, phones> number_map,
                                                  HashMap<String, phones> number_smuggler_map,
                                                  HashMap<String, Edge> Edge_map) {
        ArrayList<String> new_number_map = new ArrayList<>();
        for (Map.Entry<String, Edge> mapElement : Edge_map.entrySet()) {
            if (mapElement.getKey().length() == 8) {
                calls c = (calls) mapElement.getValue();
                if (number_map.containsKey(c.from.number)) {
                    if (number_smuggler_map.containsKey(c.to.number))
                        new_number_map.add(c.from.ssn);

                } else if (number_map.containsKey(c.to.number)) {
                    if (number_smuggler_map.containsKey(c.from.number))
                        new_number_map.add(c.to.ssn);


                }


            }

        }
        return new_number_map;
    }

    //faz4
    public static HashMap<String, phones> Find_number_faz3(HashMap<String, Vertex> Vertex_map,
                                                           HashMap<String, person> faz3) {

        HashMap<String, phones> number_map = new HashMap<>();

        for (Map.Entry<String, Vertex> mapElement : Vertex_map.entrySet()) {

            if (mapElement.getKey().length() == 13 && mapElement.getKey().charAt(1) == '0') {
                phones ph = (phones) mapElement.getValue();
                if (faz3.containsKey(ph.ssn)) {
                    number_map.put(mapElement.getKey(), ph);

                }
            }
        }
        return number_map;


    }

    //faz4
    public static HashMap<String, phones> Find_number_smuggler(HashMap<String, Vertex> Vertex_map,
                                                               HashMap<String, Edge> Edge_map) {

        HashMap<String, person> smuggler_map = new HashMap<>();

        for (Map.Entry<String, Vertex> mapElement : Vertex_map.entrySet()) {
            if (mapElement.getKey().length() == 13 && mapElement.getKey().charAt(1) != '0') {
                person p = (person) mapElement.getValue();
                if (p.work.length() == 9) {
                    if (p.work.substring(1, 8).compareTo("قاچاقچی") == 0) {
                        smuggler_map.put(p.ssn, p);


                    }
                }
            }
        }
        HashMap<String, phones> number_smuggler_map = new HashMap<>();
        for (Map.Entry<String, Vertex> mapElement1 : Vertex_map.entrySet()) {

            if (mapElement1.getKey().length() == 13 && mapElement1.getKey().charAt(1) == '0') {
                phones ph = (phones) mapElement1.getValue();
                if (smuggler_map.containsKey(ph.ssn)) {
                    number_smuggler_map.put(mapElement1.getKey(), ph);

                }
            }
        }
        return number_smuggler_map;


    }

    //faz2
    public static HashMap<String, ownerships> check_Date_for_relative(HashMap<String, relationship> relative,
                                                                      HashMap<String, Edge> Edge_map) {
        HashMap<String, ownerships> owner_map = new HashMap<>();
        for (Map.Entry<String, Edge> mapElement : Edge_map.entrySet()) {

            if (mapElement.getKey().length() == 9 && mapElement.getKey().charAt(8) == '0') {

                ownerships o = (ownerships) mapElement.getValue();
                // getter date of system
                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                LocalDateTime now = LocalDateTime.now();

                String s = dtf.format(now);
                int i = Integer.parseInt(s.substring(0, 4)) - 2;
                s = String.valueOf(i) + s.substring(4, 10);
                //
                if (o.date.substring(1, 11).compareTo(s) >= 0) {
                    if (relative.containsKey(o.from.ssn))
                        owner_map.put(o.owner_ships_id, o);
                }

            }


        }

        return owner_map;
    }

    //faz2
    public static HashMap<String, relationship> find_relationships(HashMap<String, person> suspect_map,
                                                                   HashMap<String, Edge> Edge_map) {


        HashMap<String, relationship> relative_of_suspect_map = new HashMap<>();
        for (Map.Entry<String, Edge> mapElement : Edge_map.entrySet()) {
            if (mapElement.getKey().length() == 26) {

                relationship r = (relationship) mapElement.getValue();
                if (suspect_map.containsKey(r.from.ssn)) {
                    relative_of_suspect_map.put(r.to.ssn, r);
                }

            }
        }

        return relative_of_suspect_map;

    }

    //faz2
    public static HashMap<String, person> find_work(HashMap<String, Vertex> Vertex_map) {

        HashMap<String, person> person_map = new HashMap<>();

        for (Map.Entry<String, Vertex> mapElement : Vertex_map.entrySet()) {
            if (mapElement.getKey().length() == 13 && mapElement.getKey().charAt(1) != '0') {

                person p = (person) mapElement.getValue();
                if (p.work.length() == 6) {
                    if (p.work.substring(1, 5).compareTo("گمرک") == 0)
                        person_map.put(mapElement.getKey(), (person) mapElement.getValue());
                }
                if (p.work.length() == 14) {
                    if (p.work.substring(1, 13).compareTo("سازمان بنادر") == 0) {
                        person_map.put(mapElement.getKey(), (person) mapElement.getValue());
                    }

                }
            }
        }
        return person_map;
    }

    //faz2
    public static HashMap<String, ownerships> Date_after_2019(HashMap<String, person> person_map,
                                                              HashMap<String, Edge> Edge_map) {

        HashMap<String, ownerships> ownerships_map = new HashMap<>();
        for (Map.Entry<String, Edge> mapElement : Edge_map.entrySet()) {

            if (mapElement.getKey().length() == 9 && mapElement.getKey().charAt(8) == '0') {
                ownerships o = (ownerships) mapElement.getValue();

                // getter date of system
                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                LocalDateTime now = LocalDateTime.now();

                String s = dtf.format(now);
                int i = Integer.parseInt(s.substring(0, 4)) - 2;
                s = String.valueOf(i) + s.substring(4, 10);

                if (o.date.substring(1, 11).compareTo("s") >= 0)
                    if (person_map.containsKey(o.from.ssn))
                        ownerships_map.put(mapElement.getKey(), (ownerships) mapElement.getValue());


            }
        }

        return ownerships_map;

    }

    //faz1
    public static void write_file_of_edge(HashMap<String, Edge> Edge_map) throws IOException {

        FileWriter ownerships = new FileWriter("ownerships.txt");
        FileWriter transactions = new FileWriter("transactions.txt");
        FileWriter calls = new FileWriter("calls.txt");
        FileWriter relationships = new FileWriter("relationships.txt");

        String[] columnNames = {"from", "to", "ownership_id", "date", "amount"};
        ownerships.write(Arrays.toString(columnNames));
        ownerships.write("\n");

        String[] columnNames1 = {"from", "to", "transaction_id", "date", "amount"};
        transactions.write(Arrays.toString(columnNames1));
        transactions.write("\n");

        String[] columnNames2 = {"from", "to", "call_id", "date", "duration"};
        calls.write(Arrays.toString(columnNames2));
        calls.write("\n");

        String[] columnNames3 = {"from", "to", "relation", "date"};
        relationships.write(Arrays.toString(columnNames3));
        relationships.write("\n");


        for (Map.Entry<String, Edge> mapElement : Edge_map.entrySet()) {
            Edge value = mapElement.getValue();

            if (mapElement.getKey().length() == 9 && mapElement.getKey().charAt(8) == '0') {

                ownerships e = (ownerships) mapElement.getValue();
                e.owner_ships_id = mapElement.getKey().substring(0, 8);

                e.print(ownerships);
                e.owner_ships_id += '0';

            } else if (mapElement.getKey().length() == 9 && mapElement.getKey().charAt(8) == '1') {

                transactions t = (transactions) mapElement.getValue();
                t.transactions_id = mapElement.getKey().substring(0, 8);


                t.print(transactions);
                t.transactions_id += '1';
            } else if (mapElement.getKey().length() == 8) {
                value.print(calls);
            } else if (mapElement.getKey().length() == 26) {
                value.print(relationships);
            }


        }
        ownerships.close();
        transactions.close();
        calls.close();
        relationships.close();

    }

    //faz1
    public static void write_file_of_vertex(HashMap<String, Vertex> Vertex_map) throws IOException {

        FileWriter person = new FileWriter("person.txt");
        FileWriter car = new FileWriter("car.txt");
        FileWriter account = new FileWriter("account.txt");
        FileWriter home = new FileWriter("home.txt");
        FileWriter phone = new FileWriter("phone.txt");

        String[] columnNames = {" city", "work ", " birthday", "کدملی ", "last_name", "first_name "};
        person.write(Arrays.toString(columnNames));
        person.write("\n");

        String[] columnNames1 = {"color", "model", "ssn", "plate"};
        car.write(Arrays.toString(columnNames1));
        car.write("\n");

        String[] columnNames2 = {"IBAN", "account_id", "bank_name", "ssn"};
        account.write(Arrays.toString(columnNames2));
        account.write("\n");

        String[] columnNames3 = {"address", "size", "postal_code", "price", "ssn"};
        home.write(Arrays.toString(columnNames3));
        home.write("\n");

        String[] columnNames4 = {"operator", "number", "ssn"};
        phone.write(Arrays.toString(columnNames4));
        phone.write("\n");


        for (Map.Entry<String, Vertex> mapElement : Vertex_map.entrySet()) {
            Vertex value = mapElement.getValue();
            if (mapElement.getKey().length() == 13 && mapElement.getKey().charAt(1) != '0') {

                value.print(person);
            }
            if (mapElement.getKey().length() == 8 && 48 <= mapElement.getKey().charAt(3) &&
                    mapElement.getKey().charAt(3) <= 57) {
                value.print(account);
            } else if (mapElement.getKey().length() == 8) {
                value.print(car);
            }
            if (mapElement.getKey().length() == 13 && mapElement.getKey().charAt(1) == '0') {
                value.print(phone);
            }

            if (mapElement.getKey().length() == 12) {
                value.print(home);
            }


        }
        person.close();
        car.close();
        account.close();
        home.close();
        phone.close();

    }

    public static void main(String[] args) throws IOException {


        Graph G = new Graph();

        Scanner sc = new Scanner(new File("/home/negar/Documents/درسی نگار/DS1_PROJECT/داده های اولیه پروژه/people.csv"));
        sc.useDelimiter(",");
        sc.nextLine();
        while (sc.hasNextLine()) {

            person p = new person();
            p.first_name = sc.next();
            p.last_name = sc.next();
            p.ssn = sc.next();
            p.birthday = sc.next();
            p.city = sc.next();
            p.work = sc.nextLine().substring(1);
            G.Vertex_map.put(p.ssn, p);

        }

        sc = new Scanner(new File("/home/negar/Documents/درسی نگار/DS1_PROJECT/داده های اولیه پروژه/accounts.csv"));
        sc.useDelimiter(",");
        sc.nextLine();
        while (sc.hasNextLine()) {

            account a = new account();
            a.ssn = sc.next();
            a.bank_name = sc.next();
            a.IBAN = sc.next();
            a.account_id = sc.nextLine().substring(1);

            G.Vertex_map.put(a.account_id, a);

        }

        sc = new Scanner(new File("/home/negar/Documents/درسی نگار/DS1_PROJECT/داده های اولیه پروژه/homes.csv"));
        sc.useDelimiter(",");
        sc.nextLine();
        while (sc.hasNextLine()) {

            home h = new home();
            h.ssn = sc.next();
            h.price = sc.next();
            h.postal_code = sc.next();
            h.size = sc.next();
            h.address = sc.nextLine().substring(1);

            G.Vertex_map.put(h.postal_code, h);

        }

        sc = new Scanner(new File("/home/negar/Documents/درسی نگار/DS1_PROJECT/داده های اولیه پروژه/cars.csv"));
        sc.useDelimiter(",");
        sc.nextLine();
        while (sc.hasNextLine()) {

            car c = new car();
            c.plat = sc.next();
            c.ssn = sc.next();
            c.model = sc.next();
            c.color = sc.nextLine().substring(1);

            G.Vertex_map.put(c.plat, c);
        }

        sc = new Scanner(new File("/home/negar/Documents/درسی نگار/DS1_PROJECT/داده های اولیه پروژه/phones.csv"));
        sc.useDelimiter(",");
        sc.nextLine();
        while (sc.hasNextLine()) {

            phones ph = new phones();
            ph.ssn = sc.next();
            ph.number = sc.next();
            ph.operatore = sc.nextLine().substring(1);

            G.Vertex_map.put(ph.number, ph);
        }

        sc = new Scanner(new File("/home/negar/Documents/درسی نگار/DS1_PROJECT/داده های اولیه پروژه/ownerships.csv"));
        sc.useDelimiter(",");
        sc.nextLine();
        while (sc.hasNextLine()) {

            ownerships o = new ownerships();
            o.from = (person) G.Vertex_map.get(sc.next());
            String key = sc.next();
            if (key.length() == 12) {
                o.to = (home) G.Vertex_map.get(key);
                o.t = true;
            }
            if (key.length() == 8) {
                o.to = (car) G.Vertex_map.get(key);
                o.t = false;
            }
            o.owner_ships_id = sc.next();
            o.owner_ships_id += '0';
            o.date = sc.next();
            o.amount = sc.nextLine().substring(1);
            G.Edge_map.put(o.owner_ships_id, o);


        }

        sc = new Scanner(new File("/home/negar/Documents/درسی نگار/DS1_PROJECT/داده های اولیه پروژه/transactions.csv"));
        sc.useDelimiter(",");
        sc.nextLine();
        while (sc.hasNextLine()) {

            transactions t = new transactions();
            String key = sc.next();
            t.from = (account) G.Vertex_map.get(key);
            key = sc.next();
            t.to = (account) G.Vertex_map.get(key);
            t.transactions_id = sc.next();
            t.transactions_id += '1';
            t.date = sc.next();
            t.amount = sc.nextLine().substring(1);

            G.Edge_map.put(t.transactions_id, t);
        }

        sc = new Scanner(new File("/home/negar/Documents/درسی نگار/DS1_PROJECT/داده های اولیه پروژه/calls.csv"));
        sc.useDelimiter(",");
        sc.nextLine();
        while (sc.hasNextLine()) {
            calls c = new calls();
            String key = sc.next();
            c.from = (phones) G.Vertex_map.get(key);
            c.to = (phones) G.Vertex_map.get(sc.next());
            c.call_id = sc.next();
            c.date = sc.next();
            c.duration = sc.nextLine().substring(1);

            G.Edge_map.put(c.call_id, c);
        }

        sc = new Scanner(new File("/home/negar/Documents/درسی نگار/DS1_PROJECT/داده های اولیه پروژه/relationships.csv"));
        sc.useDelimiter(",");
        sc.nextLine();
        while (sc.hasNextLine()) {

            relationship r = new relationship();
            r.from = (person) G.Vertex_map.get(sc.next());
            r.to = (person) G.Vertex_map.get(sc.next());
            r.relation = sc.next();
            r.date = sc.nextLine().substring(1);
            G.Edge_map.put(r.from.ssn + r.to.ssn, r);

        }

        write_file_of_vertex(G.Vertex_map);
        write_file_of_edge(G.Edge_map);

        /////////////////////////////////////////////////////////////////////////////////////////////////// faze 2


        HashMap<String, person> suspects_map = find_work(G.Vertex_map);
        HashMap<String, ownerships> ownerships_map = Date_after_2019(suspects_map, G.Edge_map);// صد درصد مضنون اند
        HashMap<String, relationship> relative_of_suspects_map = find_relationships(suspects_map, G.Edge_map);
        HashMap<String, ownerships> ownerships_map_new = check_Date_for_relative(relative_of_suspects_map, G.Edge_map);


        FileWriter fw = new FileWriter("faz2.txt");
        HashMap<String, person> faz2 = new HashMap<>();
        for (Map.Entry<String, ownerships> mapElement : ownerships_map_new.entrySet()) {

            person p = mapElement.getValue().from;
            for (Map.Entry<String, Edge> mapElement1 : G.Edge_map.entrySet()) {
                if (mapElement1.getKey().length() == 26) {
                    relationship r = (relationship) mapElement1.getValue();

                    if (r.from.ssn.compareTo(p.ssn) == 0) {

                        if (r.to.work.length() == 6) {
                            if (r.to.work.substring(1, 5).compareTo("گمرک") == 0)
                                faz2.put(r.to.ssn, r.to);

                        } else if (r.to.work.length() == 14) {
                            if (r.to.work.substring(1, 13).compareTo("سازمان بنادر") == 0)
                                faz2.put(r.to.ssn, r.to);


                        }
                    }

                }


            }

        }
        for (Map.Entry<String, ownerships> mapElement : ownerships_map.entrySet()) {
            person p = mapElement.getValue().from;

            faz2.put(p.ssn, p);

        }
        for (Map.Entry<String, person> mapElement : faz2.entrySet()) {
            mapElement.getValue().print(fw);
        }

        fw.close();
        ////////////////////////////////////////////////////////////////////////////////////////////////faz3


        ArrayList<account> accounts_of_smuggler = accounts_smuggler(G.Vertex_map);

        ArrayList<String> faz3 = new ArrayList<>();
        int count = 0;

        for (int i = 0; i < accounts_of_smuggler.size(); i++) {

            HashMap<String, Boolean> isVisited = new HashMap<>();

            ArrayList<account> localPathList = new ArrayList<>();

            localPathList.add(accounts_of_smuggler.get(i));

            check_AllPaths(accounts_of_smuggler.get(i), isVisited, localPathList, G.Edge_map, faz2, faz3, count);
        }

        FileWriter fn = new FileWriter("faz3.txt");
        HashMap<String, person> faz3_map = new HashMap<>();

        for (int i = 0; i < faz3.size(); i++) {

            faz3_map.put(faz3.get(i), (person) G.Vertex_map.get(faz3.get(i)));
        }
        for (Map.Entry<String, person> mapElement : faz3_map.entrySet()) {
            mapElement.getValue().print(fn);
        }
        fn.close();


        ////////////////////////////////////////////////////////////////////////////////////////////////////faz4

        HashMap<String, phones> number_map = Find_number_faz3(G.Vertex_map, faz3_map);

        HashMap<String, phones> number_smuggler_map = Find_number_smuggler(G.Vertex_map, G.Edge_map);

        ArrayList<String> new_number_map = check_contact(number_map, number_smuggler_map, G.Edge_map);

        FileWriter fr = new FileWriter("faz4.txt");
        HashMap<String, person> faz4 = new HashMap<>();

        for (int i = 0; i < new_number_map.size(); i++) {

            faz4.put(new_number_map.get(i), (person) G.Vertex_map.get(new_number_map.get(i)));
        }
        for (Map.Entry<String, person> mapElement : faz4.entrySet()) {
            mapElement.getValue().print(fr);
        }
        fr.close();
        ///////////////////////////////////////////////

        sc.close();
    }
}



