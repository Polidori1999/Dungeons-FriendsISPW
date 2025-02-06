package it.uniroma2.marchidori.maininterface.enumerate;

public enum RoleEnum {
    DM,
    PLAYER,
    GUEST,
    NONE;

    public String getRoleName() {
        if(PLAYER.equals(this)){
            return "Player";
        }else if(DM.equals(this)){
            return "DM";
        }else{
            return "Guest";
        }
    }
}
