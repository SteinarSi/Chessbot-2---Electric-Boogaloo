# Chessbot-2---Electric-Boogaloo
Sjakkbot. 2.


ArrayList<Tuple> lovligeliste = new ArrayList<>();
        for (int fra = 0; fra < board.length(); fra++) {
            char brikke = board.charAt(fra);
            if (!Character.isUpperCase(brikke)) {
                continue;
            }
            //Sjekker om rokering er lovlig, legger det til i listen
            if(black) {
                if ((brikke == 'K' && (boolean) BC.getY() && board.charAt(fra + W) == '.' && board.charAt(fra + W + W) == '.') && board.charAt(fra + W + W + W) == '.') {
                    lovligeliste.add(new Tuple(95, 93)); //Svart rokerer langt
                }
                if ((brikke == 'K' && (boolean) BC.getX() && board.charAt(fra + E) == '.' && board.charAt(fra + E + E) == '.')) {
                    lovligeliste.add(new Tuple(95, 97)); //Svart rokerer kort
                }
            } else {
                if ((brikke == 'K' && (boolean) WC.getY() && board.charAt(fra + W) == '.' && board.charAt(fra + W + W) == '.') && board.charAt(fra + W + W + W) == '.') {
                    lovligeliste.add(new Tuple(95, 93)); //Hvit rokerer langt
                }
                if ((brikke == 'K' && (boolean) WC.getX() && board.charAt(fra + E) == '.' && board.charAt(fra + E + E) == '.')) {
                    lovligeliste.add(new Tuple(95, 97)); //Hvit rokerer kort
                }
            }
            //Legger til alle normale trekk
            for (int retning : directions.get(brikke)) {
                for (int til = fra + retning; true; til += retning) {
                    char mål = board.charAt(til);

                    if (Character.isUpperCase(mål) || Character.isWhitespace(mål)) break; //Om brikken prøver å ta en vennlig brikke, eller prøver å gå av brettet

                    if (brikke == 'P') {
                        if ((retning == N || retning == N * 2) && mål != '.') break;
                        if (retning == N * 2 && (fra < A1 + N || board.charAt(N + fra) != '.')) break;
                        if ((retning == N + E || retning == N + W) && mål == '.') break;
                    }
                    lovligeliste.add(new Tuple(fra, til));
                    if (brikke == 'P' || brikke == 'N' || brikke == 'K' || Character.isLowerCase(mål)) break;
                }
            }
        }
        for (int i = 0; i<lovligeliste.size(); i++){
            if(trekk.equals(lovligeliste.get(i))) lovlig = true;
        }
