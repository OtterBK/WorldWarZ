package worldwarz.worldwarz.promotemodule;

public enum PromoteType {
    민간인(0, "민간인"),
    훈련병(1, "훈련병"),
    이병(2, "이병"),
    일병(3, "일병"),
    상병(4, "상병"),
    병장(5, "병장");

    final public int priority;
    final public String promoteName;

    PromoteType(int priority, String promoteName) {
        this.priority = priority;
        this.promoteName = promoteName;
    }

    public static PromoteType getPromoteTypeFromString(String promoteName){
        switch (promoteName){
            case "민간인": return 민간인;
            case "훈련병": return 훈련병;
            case "이병": return 이병;
            case "일병": return 일병;
            case "상병": return 상병;
            case "병장": return 병장;

            default: return null;
        }
    }
}
