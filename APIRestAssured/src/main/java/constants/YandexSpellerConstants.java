package constants;

public class YandexSpellerConstants {

    public static final String SPELLER_URI = "https://speller.yandex.net/services/spellservice.json/checkTexts";
    public static long requestNumber = 0L;

    public enum ParameterName {
        LANGUAGE("lang"),
        OPTIONS("options"),
        FORMAT("format"),
        TEXT("text");

        public String value;

        ParameterName(String parameterName) {
            this.value = parameterName;
        }

        @Override
        public String toString() {
            return String.valueOf(value);
        }
    }

    public enum Lang {
        ENGLISH("en"),
        RUSSIAN("ru"),
        UKRAINIAN("uk"),
        ENGLISH_INCORRECT("engl");

        public String value;

        Lang(String value) {
            this.value = value;
        }
        @Override
        public String toString() {
            return String.valueOf(value);
        }
    }

    public enum Option {
        DEFAULT(0),
        IGNORE_DIGITS(2),
        IGNORE_URLS(4),
        FIND_REPEAT_WORDS(8),
        IGNORE_CAPITALIZATION(512);

        public int value;

        Option(int value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return String.valueOf(value);
        }
    }

    public enum Format {
        DEFAULT_FORMAT("plain"),
        HTML_FORMAT("html"),
        INCORRECT_FORMAT("1234");

        public String value;

        Format(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return String.valueOf(value);
        }
    }

}
