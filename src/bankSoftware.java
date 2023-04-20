import java.util.*;
import java.io.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class bankSoftware {
    /*
     * luodaan universaaleja muuttujia, toinen näistä on final koska emme halua
     * tiedostopolun muuttuvan kesken projektin tai käytössä.
     * Hashmappiin talletetaan käyttäjän nimi ja saldo, Hashmap ei salli uniikkeja
     * avaimia.
     */
    public static final String fileName = "bankDetails.txt";
    public static HashMap<String, Double> accountDetails = new HashMap<>();

    // userPrompt(), addUser(), addMoney(), withdrawMoney(), transferMoney(),
    // deleteUser()

    public static void main(String[] args) {
        populateHashMap();
        System.out.println(accountDetails);
        applicationWindow();
        readFile();
        // userPrompt();
    }

    public static void applicationWindow() {
        try {
            JFrame JFWindow = new JFrame();
            JFWindow.setLayout(new FlowLayout());

            JFWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            JFWindow.setSize(400, 400);

            JFWindow.setTitle("Turku Wallstreet Bank");

            for (String key : accountDetails.keySet()) {
                JButton button = new JButton(key);
                button.setName(key);
                button.setPreferredSize(new Dimension(JFWindow.getWidth(), 50));
                JFWindow.getContentPane().add(button);
                JFWindow.setVisible(true);
                button.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        /*
                         * debug line mikä napeista valittiin, tätä tietoa voidaan käyttää myöhemmin
                         * hashmapin päivitykseen > napin nimet tulevat käyttäjien nimistä joten
                         * toimivat
                         * avaimina hashmappiin josta haetaan käyttäjän pankkitilin saldo.
                         */
                        String selectedOptionString = (String) button.getName();
                        System.out.println("user " + selectedOptionString + " clicked");

                        Double balance = accountDetails.get(selectedOptionString);

                        /* Luodaan uusi ikkuna käyttäjälle joka valittiin. */
                        JFrame balanceWindow = new JFrame();
                        balanceWindow.setLayout(new FlowLayout());

                        /*
                         * tässä on tärkeää että käytetään dispose on close eikä exit on close, sillä
                         * exit terminoi koko prosessin, dispose vain sulkee ikkunan.
                         */
                        balanceWindow.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                        balanceWindow.setSize(400, 225);
                        balanceWindow.setTitle("Banking details for " + selectedOptionString);

                        JLabel label = new JLabel("Account Balance: " + balance);
                        balanceWindow.getContentPane().add(label);
                        balanceWindow.setLocationRelativeTo(null); // KREDIITIT STACKOVERFLOW nyt kun tiedän metodin
                                                                   // olemassaolosta, sen dokumentaatiossa lukee että
                                                                   // jos argumentti on null niin ikkuna ilmestyy ruudun
                                                                   // keskelle.:
                                                                   // https://stackoverflow.com/questions/9543320/how-to-position-the-form-in-the-center-screen

                        String[] options = {
                                " ",
                                "addUser()",
                                "deleteUser()",
                                "addMoney()",
                                "withdrawMoney()",
                                "transferMoney()",
                        };

                        JComboBox<String> comboBox = new JComboBox<>(options);
                        balanceWindow.getContentPane().add(comboBox);

                        /*
                         * lol luulin että tätä tarvitaan ja ihmettelin miksei se ankkuroidu
                         * balancewindowiin, sit tajusin että tää on popup...
                         */
                        // JOptionPane.showMessageDialog(balanceWindow, comboBox, "Select an option",
                        // JOptionPane.INFORMATION_MESSAGE);

                        JButton submitButton = new JButton();
                        submitButton.setPreferredSize(new Dimension(80, 25));
                        submitButton.setName("submit-button");
                        submitButton.setText("submit");
                        balanceWindow.add(submitButton);

                        balanceWindow.setVisible(true);
                        String selectedOption = (String) comboBox.getSelectedItem();
                        buttonHandler(selectedOption);
                    }
                });
            }
        } finally {
            System.out.println("exited.");
        }
    }

    public static void buttonHandler(String selectedOption) {
        if (selectedOption.equals("addUser()")) {
            System.out.println("DEBUG\tCalling " + selectedOption);
            System.out.println(selectedOption);
            addUser();

        } else if (selectedOption.equals("deleteUser()")) {
            deleteUser();
            System.out.println("Calling " + selectedOption);
        } else if (selectedOption.equals("addMoney()")) {
            addMoney();
            System.out.println("Calling " + selectedOption);
        } else if (selectedOption.equals("withdrawMoney()")) {
            withdrawMoney();
            System.out.println("Calling " + selectedOption);
        } else if (selectedOption.equals("transferMoney()")) {
            transferMoney();
            System.out.println("Calling " + selectedOption);
        }
    }

    public static void userPrompt() {
        try {
            Scanner scIN = new Scanner(System.in);
            String input = "";
            while (!(input.equals("6"))) {
                System.out.println("1) - Add an user\n" +
                        "2) - Delete an user\n" +
                        "3) - Add money\n" +
                        "4) - Withdraw money\n" +
                        "5) - Transfer money\n" +
                        "6) - Exit the application\n");

                input = scIN.nextLine();

                if (input.equals("1")) {
                    addUser();
                } else if (input.equals("2")) {
                    deleteUser();
                } else if (input.equals("3")) {
                    addMoney();
                } else if (input.equals("4")) {
                    withdrawMoney();
                } else if (input.equals("5")) {
                    transferMoney();
                }
            }
        } finally {
            System.out.println("exited");
        }
    }

    /* Callataan joka metodin lopussa pitämään hashmap ajan tasalla. */
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
            populateHashMap();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void deleteUser() {
        try {
            File bankDetails = new File(fileName);
            FileWriter bdWriter = new FileWriter(bankDetails, false);
            Scanner scIN = new Scanner(System.in);

            String nameToDelete = "";

            System.out.println("Give the user to be deleted.\noptions:");
            for (String name : accountDetails.keySet()) {
                System.out.println(name);
            }
            nameToDelete = scIN.nextLine();

            if (accountDetails.containsKey(nameToDelete)) {
                accountDetails.remove(nameToDelete);
                System.out.println("user " + nameToDelete + " deleted.");
            }

            for (String name : accountDetails.keySet()) {
                bdWriter.write(name + "," + accountDetails.get(name) + "\n");
            }
            bdWriter.close();
            populateHashMap();

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
            populateHashMap();
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
            populateHashMap();
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