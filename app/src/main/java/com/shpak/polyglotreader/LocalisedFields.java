package com.shpak.polyglotreader;

class LocalisedFields {

    static Language[] SUPPORTED_LANGUAGES;
    static String YANDEX_BUTTON_TEXT;
    static String PROGRESS_LABEL_TEXT;
    static String LAST_PAGE_TEXT;
    static String FIRST_PAGE_TEXT;
    static String BACK_BUTTON_TEXT;
    static String LABEL_FROM_TEXT;
    static String LABEL_TO_TEXT;
    static String TEXT_EXAMPLE;
    static String NO_INTERNET_CONNECTION;
    static String NO_INTERNET_MESSAGE;
    static String ATTENTION;
    static String FIRST_RUN_MESSAGE;
    static String NO_BOOKS;
    static String SUPPORTED_EXTENSION_MESSAGE;

    LocalisedFields(String systemLang) {
        switch (systemLang) {
            case "ru":
            case "ua":
            case "by":
                SUPPORTED_LANGUAGES = new Language[]{new Language("Английский", "en"),
                                                     new Language("Арабский", "ar"),
                                                     new Language("Бенгальский", "bn"),
                                                     new Language("Индонезийский", "id"),
                                                     new Language("Итальянский", "it"),
                                                     new Language("Исландский", "is"),
                                                     new Language("Испанский", "es"),
                                                     new Language("Казахский", "kk"),
                                                     new Language("Китайский", "zh"),
                                                     new Language("Немецкий", "de"),
                                                     new Language("Португальский", "pt"),
                                                     new Language("Русский", "ru"),
                                                     new Language("Турецкий", "tr"),
                                                     new Language("Украинский", "uk"),
                                                     new Language("Французский", "fr"),
                                                     new Language("Хинди", "hi")};
                YANDEX_BUTTON_TEXT = "Приложение использует Yandex Translate API";
                PROGRESS_LABEL_TEXT = "Прогресс";
                LAST_PAGE_TEXT = "Это последняя страница";
                FIRST_PAGE_TEXT = "Это первая страница";
                BACK_BUTTON_TEXT = "Назад";
                LABEL_FROM_TEXT = "Язык текста";
                LABEL_TO_TEXT = "Язык перевода";
                TEXT_EXAMPLE = "Пример текста";
                NO_INTERNET_CONNECTION = "Отсутствует интернет соединение";
                NO_INTERNET_MESSAGE = "Приложение не может работать без доступа к Интернету.";
                ATTENTION = "Внимание";
                FIRST_RUN_MESSAGE = "Приложение поддерживает только текстовые файлы формата txt. Если Ваш файл имеет другой формат, его можно конвертировать в txt любым онлайн конвертатором. \nДля полученя перевода достаточно кликнуть по незнакомому слову. Переключение страниц осуществляется боковыми стрелками. \nНастройки языков и текста производятся в меню настроек. (Значек ⚙)";
                NO_BOOKS = "Вы пока не добавили ни одной книги";
                SUPPORTED_EXTENSION_MESSAGE = "Приложение поддерживает только файлы формата txt";
                break;

            default:
                SUPPORTED_LANGUAGES = new Language[]{new Language("English", "en"),
                                                     new Language("Arabic", "ar"),
                                                     new Language("Bengali", "bn"),
                                                     new Language("Indonesian", "id"),
                                                     new Language("Italian", "it"),
                                                     new Language("Icelandic", "is"),
                                                     new Language("Spanish", "es"),
                                                     new Language("Kazakh", "kk"),
                                                     new Language("Chinese", "zh"),
                                                     new Language("German", "de"),
                                                     new Language("Portuguese", "pt"),
                                                     new Language("Russian", "ru"),
                                                     new Language("Turkish", "tr"),
                                                     new Language("Ukrainian", "uk"),
                                                     new Language("French", "fr"),
                                                     new Language("Hindi", "hi")};
                YANDEX_BUTTON_TEXT = "Application uses Yandex Translate API";
                PROGRESS_LABEL_TEXT = "Progress";
                LAST_PAGE_TEXT = "The last page";
                FIRST_PAGE_TEXT = "The first page";
                BACK_BUTTON_TEXT = "Back";
                LABEL_FROM_TEXT = "From";
                LABEL_TO_TEXT = "To";
                TEXT_EXAMPLE = "Text example";
                NO_INTERNET_CONNECTION = "No internet connection";
                NO_INTERNET_MESSAGE = "Application can't work without access to the Internet connection.";
                ATTENTION = "Attention";
                FIRST_RUN_MESSAGE = "This application supports files with txt extension only. \nTo get a translation, just click on word you don't know. \nTo change language or text settings enter the main menu. (⚙ Sign)";
                NO_BOOKS = "No books";
                SUPPORTED_EXTENSION_MESSAGE = "This application supports txt files only";
        }
    }

    static String[] getLanguagesNames() {
        String[] langsNames = new String[SUPPORTED_LANGUAGES.length];
        for (int i = 0; i < SUPPORTED_LANGUAGES.length; i++) {
            langsNames[i] = SUPPORTED_LANGUAGES[i].getLangName();
        }
        return langsNames;
    }
}
