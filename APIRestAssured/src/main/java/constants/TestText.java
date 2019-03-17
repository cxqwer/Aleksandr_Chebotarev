package constants;

public class TestText {

    public enum TestWords {
        EN_WORD("mother", "motther"),
        RU_WORD("молоко", "маллоко"),
        UK_WORD("Україна", "Укроїна");

        private String corrVer;
        private String wrongVer;

        TestWords(String corrVer, String wrongVer) {
            this.corrVer = corrVer;
            this.wrongVer = wrongVer;
        }

        public String corrVer(){
            return corrVer;
        }

        public String wrongVer(){
            return wrongVer;
        }
    }

    public enum Properties {
        EN_WITH_DIGITS("122father"),
        EN_WITH_URL("text www.leningrad.ru"),
        EN_WRONG_CAPITAL("england"),
        EN_WITH_EMAIL("text@email.ru"),

        RU_WITH_DIGITS("молоко11"),
        RU_WITH_URL("сайт https://www.google.ru/"),
        RU_WRONG_CAPITAL("город санкт-петербург"),
        RU_WITH_EMAIL("мыло: Ivanov@email.ru"),

        UK_WITH_DIGITS("з6устріч"),
        UK_WITH_URL("зустріч https://www.google.ru/"),
        UK_WRONG_CAPITAL("yкраїна"),
        UK_WITH_EMAIL("пошта poshta@email.ru");

        public String value;

        Properties(String value) {
        this.value=value;
        }

        @Override
        public String toString() {
            return String.valueOf(value);
        }
    }
}