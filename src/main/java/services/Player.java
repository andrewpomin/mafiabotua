package services;

import game.Rules;

public class Player {
    public boolean isWinner;
    private final long id;
    public String role;
    public boolean alive = true;
    public boolean lastWord;
    private long chosenByYou;
    private int votes = 0;
    private boolean voted;
    private boolean youConfirm;
    private boolean youNotConfirm;
    public int mafiasVotes = 0;
    public int donVotes = 0;
    public int commissionerVotes = 0;
    public boolean mafiasVisited;
    public boolean doctorsVisited;
    public boolean doctorsVisitedPreviousNight;
    public boolean commissionersVisited;
    public boolean commissionerVisitedThisGame;
    private boolean whoreVisited;
    private boolean maniacVisited;
    private boolean lawyerVisited;
    private boolean persecutorVisited;
    private boolean samuraiVisited;

    public Player(long id, long groupId) {this.id = id; Rules r = Storage.getRules(groupId); role = r.generateRole(id);}

    public long getId() {
        return this.id;
    }
    public String getRole() {
        return this.role;
    }

    public boolean isLastWord() {
        return this.lastWord;
    }
    public void setLastWord(boolean b) {
        this.lastWord = b;
    }

    public boolean isYouConfirm() {
        return this.youConfirm || this.youNotConfirm;
    }
    public void setYouConfirm(boolean b) {
        if (b) {
            this.youConfirm = true;
            this.youNotConfirm = false;
        } else {
            this.youConfirm = false;
            this.youNotConfirm = true;
        }
    }
    public boolean getConfirmVote() {
        return youConfirm;
    }
    public void nullYourConfirm() {youConfirm = false; youNotConfirm = false;}

    public boolean isAlivePlayer() {
        return alive;
    }
    public void setAlive(boolean alive) {
        this.alive = alive;
    }

    public int getCommissionerVotes() {
        return this.commissionerVotes;
    }
    public void addCommissionerVotes() {++commissionerVotes;}
    public void subtractCommissionerVotes() {--commissionerVotes;}
    public void nullCommissionerVotes() {
        this.commissionerVotes = 0;
    }

    public long getChosenByYou() {return chosenByYou;}
    public void setChosenByYou(long id) {chosenByYou = id;}

    public void addVote() {
        ++votes;
    }
    public int getVotes() {
        return votes;
    }
    public void nullVotesPlayer() {
        this.votes = 0;
    }

    public void setVoted(boolean b) {voted = b;}
    public boolean getVoted() {return voted;}

    public int getMafiasVotes() {
        return mafiasVotes;
    }
    public void addMafiasVotes() {++mafiasVotes;}
    public void subtractMafiasVotes() {--mafiasVotes;}
    public void nullMafiasVotes() {
        this.mafiasVotes = 0;
    }

    public int getDonVotes() {
        return donVotes;
    }
    public void addDonVotes() {
        ++donVotes;
    }
    public void subtractDonVotes() {--donVotes;}
    public void nullDonVotes() {
        this.donVotes = 0;
    }

    public boolean isMafiasVisited() {
        return mafiasVisited;
    }
    public void setMafiasVisited(boolean mafiasVisited) {
        this.mafiasVisited = mafiasVisited;
    }

    public boolean isDoctorsVisited() {
        return doctorsVisited;
    }
    public void setDoctorsVisited(boolean doctorsVisited) {
        this.doctorsVisited = doctorsVisited;
    }
    public boolean isDoctorsVisitedPreviousNight() {
        return doctorsVisitedPreviousNight;
    }

    public boolean isCommissionersVisited() {
        return commissionersVisited;
    }
    public void setCommissionersVisited(boolean b) {commissionersVisited = b;}
    public boolean isCommissionerVisitedThisGame() {return commissionerVisitedThisGame;}
    public void setCommissionerVisitedThisGame(boolean b) {commissionerVisitedThisGame = b;}

    public boolean isWhoreVisited() {return whoreVisited;}
    public void setWhoreVisited(boolean b) {whoreVisited = b;}

    public boolean isManiacVisited() {return maniacVisited;}
    public void setManiacVisited(boolean b) {maniacVisited = b;}

    public boolean isLawyerVisited() {return lawyerVisited;}
    public void setLawyerVisited(boolean b) {lawyerVisited = b;}

    public boolean isPersecutorVisited() {return persecutorVisited;}
    public void setPersecutorVisited(boolean b) {persecutorVisited = b;}

    public boolean isSamuraiVisited() {return samuraiVisited;}
    public void setSamuraiVisited(boolean b) {samuraiVisited = b;}
}
