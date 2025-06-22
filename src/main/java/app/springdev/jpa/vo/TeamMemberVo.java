package app.springdev.jpa.vo;

public class TeamMemberVo {
    private String teamName;
    private String memberName;

    public TeamMemberVo(String teamName, String memberName) {
        this.teamName = teamName;
        this.memberName = memberName;
    }

    @Override
    public String toString() {
        return "Team: " + teamName + ", Member: " + memberName;
    }
}
