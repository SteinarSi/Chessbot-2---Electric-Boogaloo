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

                    //Gjør at brikken du trykket på, om den er riktig farge, blir "selektert".
                    if (Character.isUpperCase(brikke)){
                        pressedtuple.setX(indeks);
                    }
                    //Selekterer ruten du vil flytte til, om du ikke trykket på en ny hvit brikke.
                    else if(pressedtuple.getX() != null && !Character.isUpperCase(brikke)) {
                        pressedtuple.setY(indeks);
                    }
                    //Prøver å gjøre trekket.
                    if(pressedtuple.getY() != null){
                        Move move = new Move(pressedtuple.getX(), pressedtuple.getY()); //Konverterer tuppelen med knappinput til et Move.
                        if(getCurrentBoard().check_player_move(move)){
                            game.playerMove(move);
                            pressedtuple.setX(pressedtuple.getY()); //Gjør at når du flytter en brikke, er den automatisk selektert til å bli flyttet i neste trekk.
                            pressedtuple.setY(null);
                        } else {
                            System.err.println("Not a legal move!");
                            pressedtuple.setY(null);
                        }
                    }
                }
            }
        }
    }
    public void keyReleased(KeyEvent e) { } //Implementer hvis vi får behov
    public void keyTyped(KeyEvent e) { } //Implementer hvis vi får behov
}
