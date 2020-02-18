package Chessbot2;

import java.awt.event.*;

import static Chessbot2.Chess.*;

public class Action extends KeyAdapter implements ActionListener {
    /* En klasse for å holde styr på alle knappene, både på tastaturet og på skjermen.

    For å legge til en tast legger du til en case i keyPressed, og skriver
    '<knapp>.addKeyListener(new Action())' i main.

    En knapp på skjermen legger du til i actionPerformed,
    og med '<knapp>.addActionListener(new Action())' i main.
    */
    public void keyTyped(KeyEvent e) { } //Implementer senere hvis vi gidder

    public void keyPressed(KeyEvent e){
        /* Holder styr på hva som skjer hver gang brukeren trykker en tast på tastaturet.
         */
        int key = e.getKeyCode();
        try{
            switch(key){
                case KeyEvent.VK_ENTER:
                    usertext = textField.getText();
                    textField.setText("");
                    System.out.println("You wrote " + usertext);
                    break;
                default:
                    break;
            }
        } catch(Exception a) {
            System.out.println("Unknown button");
        }
    }
    @Override
    public void keyReleased(KeyEvent e) { } //Implementer senere hvis vi gidder
    //@Override

    public void actionPerformed(ActionEvent Event) {
        /* Holder styr på hva som skjer hver gang brukeren trykker en knapp på skjermen.
         */
        if(Event.getSource() == quit) {
            System.exit(0);
        } else if(Event.getSource() == enter){
            trykket = true;
            usertext = textField.getText();
            textField.setText("");
            System.out.println("You wrote " + usertext);
        } else if(Event.getSource() == back){
            game.back();
        }
    }
}
