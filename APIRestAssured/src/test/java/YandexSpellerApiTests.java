import org.testng.annotations.Test;

import java.util.List;

import static constants.TestText.Properties.*;
import static constants.TestText.TestWords.*;
import static constants.YandexSpellerConstants.*;
import static core.YandexSpellerServiceObject.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;

public class YandexSpellerApiTests {

    @Test
    public void checkCorrectTexts() {
        List<String> result = getStringResult(
                requestBuilder()
                        .setLanguage(Lang.ENGLISH, Lang.RUSSIAN, Lang.UKRAINIAN)
                        .setText(EN_WORD.corrVer(), RU_WORD.corrVer(), RU_WORD.corrVer())
                        .buildRequest()
                        .sendGetRequest());
        assertThat("API reported errors in correct text: " + result, result.isEmpty());
    }

    @Test
    public void checkWrongTexts() {
        String[] texts = {EN_WORD.wrongVer(), RU_WORD.wrongVer(), UK_WORD.wrongVer()};
        List<String> result = getStringResult(
                requestBuilder()
                        .setLanguage(Lang.ENGLISH, Lang.RUSSIAN, Lang.UKRAINIAN)
                        .setText(texts)
                        .buildRequest()
                        .sendGetRequest());
        if (result.size() != texts.length) {
            for (String text : texts) {
                assertThat("API failed to find spelling error in text: " + text, result.contains(text));
            }
        }
    }

    @Test
    public void checkFindDigits() {
        String[] texts = {EN_WITH_DIGITS.toString(), RU_WITH_DIGITS.toString(), UK_WITH_DIGITS.toString()};
        List<String> result = getStringResult(
                requestBuilder()
                        .setLanguage(Lang.ENGLISH, Lang.RUSSIAN, Lang.UKRAINIAN)
                        .setText(texts)
                        .buildRequest()
                        .sendGetRequest());
        if (result.size() != texts.length) {
            for (String text : texts) {
                assertThat("API failed to find error in text with digits: " + text,
                        result.contains(text));
            }
        }
    }

    //BUG WAS FOUND
    @Test
    public void checkFindLinks() {
        String[] texts = {EN_WITH_URL.toString(), RU_WITH_URL.toString(), UK_WITH_URL.toString()};
        List<String> result = getStringResult(
                requestBuilder()
                        .setLanguage(Lang.ENGLISH, Lang.RUSSIAN, Lang.UKRAINIAN)
                        .setText(texts)
                        .buildRequest()
                        .sendGetRequest());
        if (result.size() != texts.length) {
            for (String text : texts) {
                assertThat("API failed to find error in text with URL: " + text,
                        result.contains(text));
            }
        }
    }

    //BUG WAS FOUND
    @Test
    public void checkFindEmail() {
        String[] texts = {EN_WITH_EMAIL.toString(), RU_WITH_EMAIL.toString(), UK_WITH_EMAIL.toString()};
        List<String> result = getStringResult(
                requestBuilder()
                        .setLanguage(Lang.ENGLISH, Lang.RUSSIAN, Lang.UKRAINIAN)
                        .setText(texts)
                        .buildRequest()
                        .sendGetRequest());
        if (result.size() != texts.length) {
            for (String text : texts) {
                assertThat("API failed to find error in text with email: " + text,
                        result.contains(text));
            }
        }
    }

    //BUG WAS FOUND
    @Test
    public void checkWrongCapital() {
        String[] texts = {EN_WRONG_CAPITAL.toString(), RU_WRONG_CAPITAL.toString(), UK_WRONG_CAPITAL.toString()};
        List<String> result = getStringResult(requestBuilder()
                .setLanguage(Lang.ENGLISH, Lang.RUSSIAN, Lang.UKRAINIAN)
                .setText(texts)
                .buildRequest()
                .sendGetRequest());
        if (result.size() != texts.length) {
            for (String text : texts) {
                assertThat("API failed to find error in proper names with lower case: " + text,
                        result.contains(text));
            }
        }
    }

    @Test
    public void checkIncorrectLang() {
        requestBuilder()
                .setLanguage(Lang.ENGLISH_INCORRECT)
                .setText(EN_WORD.corrVer())
                .buildRequest()
                .sendPostRequest()
                .then().assertThat()
                .specification(badResponseSpecification())
                .body(containsString("SpellerService: Invalid parameter 'lang'"));
    }

    @Test
    public void checkIgnoreCapitalization() {
        List<String> result = getStringResult(
                requestBuilder()
                        .setLanguage(Lang.ENGLISH, Lang.RUSSIAN, Lang.UKRAINIAN)
                        .setText(EN_WRONG_CAPITAL.toString(), RU_WRONG_CAPITAL.toString(), UK_WRONG_CAPITAL.toString())
                        .setOptions(Option.IGNORE_CAPITALIZATION)
                        .buildRequest()
                        .sendGetRequest());
        assertThat("API reported errors in text with digits despite 'ignore digits' option: " + result,
                result.isEmpty());
    }

    @Test
    public void checkIgnoreUrls() {
        List<String> result = getStringResult(
                requestBuilder()
                        .setLanguage(Lang.ENGLISH, Lang.RUSSIAN, Lang.UKRAINIAN)
                        .setText(EN_WITH_URL.toString(), RU_WITH_URL.toString(), UK_WITH_URL.toString())
                        .setOptions(Option.IGNORE_URLS)
                        .buildRequest()
                        .sendGetRequest());
        assertThat("API reported errors in text with URL despite 'ignore URLs' option: " + result,
                result.isEmpty());
    }

    @Test
    public void checkIgnoreEmail() {
        List<String> result = getStringResult(
                requestBuilder()
                        .setLanguage(Lang.ENGLISH, Lang.RUSSIAN, Lang.UKRAINIAN)
                        .setText(EN_WITH_EMAIL.toString(), RU_WITH_EMAIL.toString(), UK_WITH_EMAIL.toString())
                        .setOptions(Option.IGNORE_URLS)
                        .buildRequest()
                        .sendGetRequest());
        assertThat("API reported errors in text with URL despite 'ignore URLs' option: " + result,
                result.isEmpty());
    }

    @Test
    public void checkIncorrectFormatOption() {
        requestBuilder()
                .setLanguage(Lang.ENGLISH)
                .setText(EN_WORD.corrVer())
                .setFormat(Format.INCORRECT_FORMAT)
                .buildRequest()
                .sendPostRequest()
                .then().assertThat()
                .specification(badResponseSpecification())
                .and()
                .body(containsString("SpellerService: Invalid parameter 'format'"));
    }
}
