package Chessbot2;

import javax.swing.*;
import java.awt.event.*;

import static Chessbot2.Chess.*;
import static Chessbot2.Game.*;

public class Action extends KeyAdapter implements ActionListener {
    /* En klasse for å holde styr på alle knappene, både på tastaturet og på skjermen.

    For å legge til en tast legger du til en case i keyPressed, og skriver
    '<knapp>.addKeyListener(new Action())' i main.

    En knapp på skjermen legger du til i actionPerformed,
    og med '<knapp>.addActionListener(new Action())' i main.
    */
    public void keyPressed(KeyEvent e){
        /* Holder styr på hva som skjer hver gang brukeren trykker en tast på tastaturet.
         */
        int key = e.getKeyCode();
        try {
            switch (key) {
                case KeyEvent.VK_ENTER:
                    usertext = textField.getText();
                    textField.setText("");
                    if (usertext.equals("quit")) System.exit(0);
                    else if (usertext.equals("back")) game.back();
                    else if (usertext.equals("new")) game.newGame();
                    else if (usertext.equals("print")) System.out.println(Game.getCurrentBoard().board);
                    else {
                        if(IsAMove(usertext)){
                            Move move = parse(usertext);
                            if(getCurrentBoard().check_player_move(move)){
                                game.playerMove(move);
                            } else System.err.println("Not a legal move!");
                        } else System.err.println("Try typing a move on the format 'letter number letter number'");
                    }
                    break;
            }
        } catch(Exception a) {
            System.out.println("Unexpected button");
        }
    }
    public void actionPerformed(ActionEvent Event) {
        /* Holder styr på hva som skjer hver gang brukeren trykker en knapp på skjermen.
         */
        if(Event.getSource() == quit) System.exit(0);
        else if(Event.getSource() == enter){
            usertext = textField.getText();
            textField.setText("");
            Move move = parse(usertext);
            if(getCurrentBoard().check_player_move(move)) game.playerMove(move);
            else System.err.println("Not a legal move!");
        }
        else if(Event.getSource() == back) game.back();
        else if(Event.getSource() == neww) game.newGame();
        else{
            for(int i=0; i<buttonlist.size(); i++){

                //Alt dette er kun for at spilleren skal kunne trykke på brikkene på skjermen for å flytte.
                //Jeg vet dette er jævlig stygg kode, men det funker. Ikke døm meg.
                if(Event.getSource() == buttonlist.get(i).getY()) {
                    int indeks = buttonlist.get(i).getX();
                    char brikke = getCurrentBoard().board.charAt(indeks);

                    if (Character.isUpperCase(brikke)){
                        userpressed = "" + indeks;
                    }
                    else if(userpressed.length()>=2 && !Character.isUpperCase(brikke)) {
                        userpressed += indeks;
                    }
                    if(userpressed.length() >= 4){
                        String a = "" + userpressed.charAt(0);
                        a+= userpressed.charAt(1);
                        String b = "" + userpressed.charAt(2);
                        b += userpressed.charAt(3);
                        Move move = new Move(Integer.parseInt(a), Integer.parseInt(b));
                        if(getCurrentBoard().check_player_move(move)){
                            game.playerMove(move);
                        } else {
                            System.err.println("Not a legal move!");
                            userpressed = a;
                        }
                    }
                }
            }
        }
    }
    public void keyReleased(KeyEvent e) { } //Implementer hvis vi får behov
    public void keyTyped(KeyEvent e) { } //Implementer hvis vi får behov
}
