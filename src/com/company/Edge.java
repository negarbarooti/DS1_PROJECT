package com.company;

import java.awt.font.TextHitInfo;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;

class Edge {

    void print(FileWriter fw) throws IOException {
        System.out.println("yes");

    }

}

class ownerships extends Edge {
    person from;
    Vertex to;
    Boolean t;
    //if t==true : to = home
    //if t==false : to = car
    String owner_ships_id;
    String date;
    String amount;


    @Override
    void print(FileWriter fw) throws IOException {

        if (this.t) {
            String[] data = {this.from.ssn, ((home) this.to).postal_code, this.owner_ships_id, this.date, this.amount};
            fw.write(Arrays.toString(data));
            fw.write("\n");

        } else {
            String[] data = {this.from.ssn, ((car) this.to).plat, this.owner_ships_id, this.date, this.amount};
            fw.write(Arrays.toString(data));
            fw.write("\n");
        }

    }
}

class transactions extends Edge {
    String date;
    account from;
    account to;
    String transactions_id;
    String amount;

    @Override
    void print(FileWriter fw) throws IOException {


        String[] data = {this.from.account_id, this.to.account_id, this.transactions_id, this.amount};

        fw.write(Arrays.toString(data));
        fw.write("\n");

    }
}

class calls extends Edge {
    phones from;
    phones to;
    String call_id;
    String date;
    String duration;

    @Override
    void print(FileWriter fw) throws IOException {

        String[] data = {this.from.number, this.to.number, this.call_id, this.date, this.duration};


        fw.write(Arrays.toString(data));
        fw.write("\n");

    }
}

class relationship extends Edge {
    person from;
    person to;
    String relation;
    String date;

    @Override
    void print(FileWriter fw) throws IOException {

        String[] data = {this.from.ssn, this.to.ssn, this.relation, this.date};


        fw.write(Arrays.toString(data));
        fw.write("\n");

    }
}
