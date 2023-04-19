import java.util.*;
import java.io.*;
import javax.swing.*;
import java.awt.*;

public class bankSoftware {
    /*
     * luodaan universaaleja muuttujia, toinen näistä on final koska emme halua
     * tiedostopolun muuttuvan kesken projektin tai käytössä.
     * Hashmappiin talletetaan käyttäjän nimi ja saldo, Hashmap ei salli uniikkeja
     * avaimia.
     */
    public static final String fileName = "bankDetails.txt";
    public static HashMap<String, Double> accountDetails = new HashMap<>();

    public static void main(String[] args) {
        populateHashMap();
        System.out.println(accountDetails);
        applicationWindow();
        readFile();
        userPrompt();

    }

    public static void applicationWindow() {
        try {
            JFrame JFWindow = new JFrame();

            JFWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            JTextArea tekstialue = new JTextArea();
            tekstialue.setEditable(false);

            JScrollPane rullausJutu = new JScrollPane(tekstialue);
            JFWindow.getContentPane().add(rullausJutu);

            JFWindow.setSize(400, 400);
            JFWindow.setVisible(true);
            JFWindow.setTitle("Turku Wallstreet Bank");

            /*
             * TextField JFtextField = new TextField();
             * JButton JFButton = new JButton("Test button");
             * ImageIcon JFLogo = new ImageIcon();
             * JLabel JFLogoLabel = new JLabel("logo");
             * 
             * 
             * JFtextField.setPreferredSize(new Dimension(200, 30));
             * JFButton.setPreferredSize(new Dimension(100, 30));
             * JFLogoLabel.setPreferredSize(new Dimension(100, 100));
             */
        } finally {

        }
    }

    public static void userPrompt() {
        try {
            Scanner scIN = new Scanner(System.in);
            String input = "";
            while (!(input.equals("5"))) {
                System.out.println("1) - Add money\n" +
                        "2) - Withdraw money\n" +
                        "3) - Transfer money\n" +
                        "4) - Add an user\n" +
                        "5) - Exit the application\n");

                input = scIN.nextLine();

                if (input.equals("1")) {
                    addMoney();
                } else if (input.equals("2")) {
                    withdrawMoney();
                } else if (input.equals("3")) {
                    transferMoney();
                } else if (input.equals("4")) {
                    addUser();
                }
            }
        } finally {
            System.out.println("exited");
        }
    }

    public static void populateHashMap() {
        try {
            File bankDetails = new File(fileName);
            Scanner scIN = new Scanner(bankDetails);
            int linesCheck = 0;

            while (scIN.hasNextLine()) {
                String[] temp = scIN.nextLine().split(",");
                accountDetails.put(temp[0], Double.parseDouble(temp[1]));
                linesCheck++;
            }
            System.out.println("Hashmap populated with " + linesCheck + "entries.");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            System.out.println("left populateHashMap()");
        }
    }

    public static void addUser() {
        try {
            File bankDetails = new File(fileName);
            FileWriter bdWriter = new FileWriter(bankDetails, true);
            Scanner scIN = new Scanner(System.in);

            String name = "";
            Double initialBalance = 0.0;

            System.out.println("Give the user to be added");
            name = scIN.nextLine();

            if (!(name.equals(""))) {
                bdWriter.append(name + "," + initialBalance + "\r\n");
                bdWriter.close();
                System.out.println("User " + name + " added");
            } else {
                System.out.println("jotain meni pieleen");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void addMoney() {
        try {
            /*
             * callataan populate hashmap joka päivittää hashmapin siinä tapauksessa jos
             * bankdetails tiedosto on ollut tyhjä ohjelmaa runatessa, johtaen siihen että
             * rahansiirto operaatiot eivät toimi oikein.
             */
            populateHashMap();
            File bankDetails = new File(fileName);
            Scanner scIN = new Scanner(System.in);
            FileWriter fWriter = new FileWriter(bankDetails, false);

            /* debug testi miltä hashmap näyttää metodin alussa. */
            System.out.println(accountDetails);

            if (bankDetails.createNewFile()) {
                System.out.println("File created: " + bankDetails.getName() + "at" + bankDetails.getAbsolutePath());
            } else {
                System.out.println("File already exists at: " + bankDetails.getAbsolutePath());
            }

            /* otetaan käyttäjän syötteet ja päivitetään hashmap */
            System.out.println("how much money to add?");
            Double amount = scIN.nextDouble();
            System.out.println("to what account should the money be added?");
            String accountName = scIN.next();

            accountDetails.replace(accountName, (accountDetails.get(accountName) + amount));

            /* Write updated contents back to file */
            for (String name : accountDetails.keySet()) {
                fWriter.write(name + "," + accountDetails.get(name) + "\n");
            }
            fWriter.close();

            /* debug testi miltä hashmap näyttää metodin lopussa. */
            System.out.println(accountDetails);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            System.out.println("debug\tout of addMoney");
        }
    }

    public static void readFile() {
        try {
            File bankDetails = new File(fileName);
            Scanner fileReader = new Scanner(bankDetails);

            if (bankDetails.exists()) {
                System.out.println("File name: " + bankDetails.getName());
                System.out.println("Absolute path: " + bankDetails.getAbsolutePath());
                System.out.println("Writeable: " + bankDetails.canWrite());
                System.out.println("Readable " + bankDetails.canRead());
                System.out.println("File size in bytes " + bankDetails.length());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void withdrawMoney() {
        try {
            populateHashMap();
            File bankDetails = new File(fileName);
            Scanner scIN = new Scanner(System.in);
            FileWriter fWriter = new FileWriter(bankDetails, false);

            /* debug testi miltä hashmap näyttää metodin alussa. */
            System.out.println(accountDetails);

            if (bankDetails.createNewFile()) {
                System.out.println("File created: " + bankDetails.getName() + "at" + bankDetails.getAbsolutePath());
            } else {
                System.out.println("File already exists at: " + bankDetails.getAbsolutePath());
            }

            /* otetaan käyttäjän syötteet ja päivitetään hashmap */
            System.out.println("how much money to withdraw?");
            Double amount = scIN.nextDouble();
            System.out.println("from which account do you want to withdraw?");
            String accountName = scIN.next();

            /*
             * Käytetään samaa logiikkaa kuin addMoney() metodissa, lisättynä vain tarkistus
             * että tilillä on enemmän rahaa kuin halutaan nostaa.
             */
            if (accountDetails.get(accountName) >= amount) {
                accountDetails.replace(accountName, (accountDetails.get(accountName) - amount));
            } else {
                System.out.println("debug: \tMoney was not withdrawn\n\tAmount or user issue");
            }

            /* Write updated contents back to file */
            for (String name : accountDetails.keySet()) {
                fWriter.write(name + "," + accountDetails.get(name) + "\n");
            }
            fWriter.close();

            /* debug testi miltä hashmap näyttää metodin lopussa. */
            System.out.println(accountDetails);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            System.out.println("debug\tout of withdrawMoney");
        }
    }

    public static void transferMoney() {
        try {

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            System.out.println("debug\tout of transferMoney");
        }
    }
}