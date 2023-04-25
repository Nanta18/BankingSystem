import java.util.*;
import java.io.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;

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
        readFile();
        populateHashMap();
        System.out.println(accountDetails);
        applicationWindow();
    }

    public static void applicationWindow() {
        try {
            JFrame JFWindow = new JFrame();

            JFWindow.setBackground(new Color(255,255,255));
            JFWindow.repaint(0, 0, 0, 0, 0);
            ImageIcon logoIcon = new ImageIcon("logo.png");
            JFWindow.setIconImage(logoIcon.getImage());

            JFWindow.setResizable(false);

            JFWindow.setLayout(new FlowLayout());

            JFWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            JFWindow.setSize(400, 500);

            JFWindow.setTitle("Turku Wallstreet Bank");

           

            /* Add user nappi */
            JButton addUserButton = new JButton();
            addUserButton.setName("addUserButton");
            addUserButton.setText("Add a user");
            addUserButton.setFont(new Font("Open Sans Bold", Font.BOLD, 14));
            addUserButton.setPreferredSize(new Dimension(JFWindow.getWidth() / 2, 50));
            JFWindow.add(addUserButton);

            JTextField accountsTextField = new JTextField("Accounts:");
            accountsTextField.setEditable(false);
            JFWindow.add(accountsTextField);

            ActionListener addUserButtonListener = new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    String userName = JOptionPane.showInputDialog(JFWindow, "Enter user to be added:");
                    if (userName != null && !userName.isEmpty()) {
                        addUser(userName);
                        JButton newUserButton = new JButton(userName);
                        newUserButton.setName(userName);
                        newUserButton.setPreferredSize(new Dimension((int) (JFWindow.getWidth() * 0.8), 50));
                        JFWindow.getContentPane().add(newUserButton);
                    } else {
                        JOptionPane.showMessageDialog(JFWindow, "Invalid user name.");
                    }
                    // Add new button to the GUI..... this took way too much work, idk what i was
                    // thinking jesus christ.... the voices.
                    
                    
                    JFWindow.getContentPane().revalidate();
                    JFWindow.getContentPane().repaint();
                }
            };
            addUserButton.addActionListener(addUserButtonListener);

            /* remove user nappi */
            JButton deleteUserButton = new JButton();
            deleteUserButton.setText("Delete a user");
            deleteUserButton.setFont(new Font("Open Sans Bold", Font.BOLD, 14));
            deleteUserButton.setPreferredSize(new Dimension(JFWindow.getWidth() / 2, 50));
            deleteUserButton.setName("addUserButton");
            JFWindow.add(deleteUserButton);

            ActionListener deleteUserButtonListener = new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    String userName = JOptionPane.showInputDialog(JFWindow, "Enter user to be Deleted:");

                    if (userName != null && !userName.isEmpty()) {
                        deleteUser(userName);
                        /* Remove button from the GUI */
                        // tässä otetaan kaikki ikkunan componentit ja laitetaan listaan jota vastaan
                        // voidaan verrata poisto-operaatiota varten.
                        // component on awt:n objekti jolla on graafinen esitys, nappula tms.
                        Component[] components = JFWindow.getContentPane().getComponents();
                        for (Component component : components) {
                            // Tarkistetaan onko component jbutton classin osa (instaneof metodi), ja
                            // tarkastetaan onko
                            // komponentin nimi sama kuin poistettava käyttäjä sovelluksesta.
                            if (component instanceof JButton && ((JButton) component).getName().equals(userName)) {
                                JFWindow.getContentPane().remove(component);
                                JFWindow.getContentPane().revalidate();
                                JFWindow.getContentPane().repaint();
                                // tärkeää breakata jotta ei iteroida kaikkia nappeja jos mm. ensimmäinen
                                // poistetaan.
                                break;
                            }
                        }
                    } else {
                        JOptionPane.showMessageDialog(JFWindow, "Invalid username.");
                    }
                    JFWindow.getContentPane().revalidate();
                    JFWindow.getContentPane().repaint();
                }
            };
            deleteUserButton.addActionListener(deleteUserButtonListener);

            for (String key : accountDetails.keySet()) {
                JButton button = new JButton(key);
                JFWindow.setBackground(new Color(38, 47, 48));
                button.setName(key);
                button.setPreferredSize(new Dimension((int) (JFWindow.getWidth() * 0.8), 50));
                JFWindow.getContentPane().add(button);
                JFWindow.setVisible(true);
                button.revalidate();
                button.repaint();
                JFWindow.revalidate();
                JFWindow.revalidate();
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

                        /*
                         * ActionListener funktiovalinta komponentille, nappia painettaessa kutsuu
                         * funktiota joka on valittu combobox elementissä.
                         */
                        submitButton.addActionListener(new ActionListener() {
                            public void actionPerformed(ActionEvent e) {
                                String selectedOption = (String) comboBox.getSelectedItem();
                                buttonHandler(selectedOption);
                                balanceWindow.repaint();
                            }
                        });
                    }
                });
            }
        } finally {
            System.out.println("exited GUI.");
        }
    }

    public static void buttonHandler(String selectedOption) {

        if (selectedOption.equals("addMoney()")) {
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
            if (accountDetails.size() == 0) {
                accountDetails.put("bankAdmin", 1000.0);
            }
            System.out.println("\nHashmap populated with " + linesCheck + "entries.");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            System.out.println("DEBUG:\nleft populateHashMap()");
        }
    }

    public static void addUser(String buttonInput) {
        try {
            File bankDetails = new File(fileName);
            FileWriter bdWriter = new FileWriter(bankDetails, true);
            Scanner scIN = new Scanner(System.in);

            String name = buttonInput;
            Double initialBalance = 0.0;

            System.out.println("\nGive the user to be added");

            if (!(name.equals("")) && !accountDetails.containsKey(name)) {
                bdWriter.append(name + "," + initialBalance + "\r\n");
                bdWriter.close();
                System.out.println("\nUser " + name + " added");
            } else {
                System.out.println("DEBUG:\nKäyttäjä on jo olemassa tai nimi on invalid.");
            }
            populateHashMap();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void deleteUser(String buttonInput) {
        try {
            File bankDetails = new File(fileName);
            FileWriter bdWriter = new FileWriter(bankDetails, false);

            String nameToDelete = buttonInput;

            System.out.println("\nGive the user to be deleted.\noptions:");
            for (String name : accountDetails.keySet()) {
                System.out.println(name);
            }

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
            System.out.println("\nhow much money to add?");
            Double amount = scIN.nextDouble();
            System.out.println("\nto what account should the money be added?");
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
            System.out.println("\nhow much money to withdraw?");
            Double amount = scIN.nextDouble();
            System.out.println("\nfrom which account do you want to withdraw?");
            String accountName = scIN.next();

            /*
             * Käytetään samaa logiikkaa kuin addMoney() metodissa, lisättynä vain tarkistus
             * että tilillä on enemmän rahaa kuin halutaan nostaa.
             */
            if (accountDetails.get(accountName) >= amount) {
                accountDetails.replace(accountName, (accountDetails.get(accountName) - amount));
            } else {
                System.out.println("\ndebug: \tMoney was not withdrawn\n\tAmount or user issue");
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
            populateHashMap();
            File bankDetails = new File(fileName);
            Scanner scIN = new Scanner(System.in);
            FileWriter fWriter = new FileWriter(bankDetails, false);

            System.out.println("Transferring money");
            System.out.println("\npossible senders and receivers: " + accountDetails.keySet());

            System.out.println("\nGive sender:");
            String sender = scIN.nextLine();
            System.out.println("\nGive recipient:");
            String recipient = scIN.nextLine();
            System.out.println("\nhow much money do you want to transfer?");
            Double amount = scIN.nextDouble();

            /*
             * Tässä tarkistetaan onko käyttäjällä tarpeeksi siirrettävää rahaa ja ovatko
             * molemmat, lähettäjä ja vastaanottaja valideja.
             */
            if (accountDetails.containsKey(sender) && accountDetails.containsKey(recipient)
                    && (accountDetails.get(sender) >= amount)) {
                accountDetails.replace(sender, accountDetails.get(sender) - amount);
                accountDetails.replace(recipient, accountDetails.get(recipient) + amount);
            } else {
                System.out.println("\nsender or recipient not found, or sender too poor.");
            }

            /* Write updated contents back to file */
            for (String name : accountDetails.keySet()) {
                fWriter.write(name + "," + accountDetails.get(name) + "\n");
            }
            fWriter.close();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            populateHashMap();
            System.out.println("\ndebug\tout of transferMoney");
        }
    }
}