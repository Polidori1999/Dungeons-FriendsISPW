package it.uniroma2.marchidori.maininterface.enumerate;

public enum RoleEnum {
    DM,
    PLAYER,
    NONE;

    public String getRoleName() {
        if(PLAYER.equals(this)){
            return "Player";
        }else{
            return "DM";
        }
    }
}