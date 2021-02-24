package com.company;


import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;

class Vertex {

    void print(FileWriter fw) throws IOException {
        System.out.println("yes");

    }


}

class person extends Vertex {

    String first_name;
    String last_name;
    String ssn;
    String birthday;
    String work;
    String city;

    void print(FileWriter fw) throws IOException {
        String[] data = {this.first_name, this.last_name, this.ssn, this.birthday, this.work, this.city};

//        Column Names
//        String[] columnNames = {  "first_name " , "last_name" , "کدملی " ," birthday" , "work " , " city"};
//        System.out.println(Arrays.toString(columnNames) );
//        System.out.println(Arrays.toString(data));

        fw.write(Arrays.toString(data));
        fw.write("\n");

    }
}

class account extends Vertex {
    String ssn;
    String bank_name;
    String IBAN;
    String account_id;

    @Override
    void print(FileWriter fw) throws IOException {

        String[] data = {this.ssn, this.bank_name, this.IBAN, this.account_id};


        fw.write(Arrays.toString(data));
        fw.write("\n");
    }
}

class home extends Vertex {
    String ssn;
    String price;
    String postal_code;
    String size;
    String address;

    @Override
    void print(FileWriter fw) throws IOException {
        String[] data = {this.ssn, this.price, this.postal_code, this.size, this.address};


        fw.write(Arrays.toString(data));
        fw.write("\n");
    }
}

class car extends Vertex {
    String plat;
    String ssn;
    String model;
    String color;

    @Override
    void print(FileWriter fw) throws IOException {
        String[] data = {this.plat, this.ssn, this.model, this.color};


        fw.write(Arrays.toString(data));
        fw.write("\n");
    }
}

class phones extends Vertex {
    String ssn;
    String number;
    String operatore;

    @Override
    void print(FileWriter fw) throws IOException {
        String[] data = {this.ssn, this.number, this.operatore};


        fw.write(Arrays.toString(data));
        fw.write("\n");
    }
}
