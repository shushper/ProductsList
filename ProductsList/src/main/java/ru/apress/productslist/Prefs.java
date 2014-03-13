package ru.apress.productslist;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Вспомогательный класс для работы с SharedPreferences.
 */
public class Prefs {
    private final static String PREFS_KEY_FILE_WAS_DOWNLOADED = "file_was_downloaded";
    private final static String PREFS_FILE_PATH = "file_path";

    /**
     * Возвращает объект SharedPreferences по умолчанию.
     * @return SharedPreferences по умолчанию.
     */
    public static SharedPreferences getPrefs() {
        return PreferenceManager.getDefaultSharedPreferences(App.getContext());
    }

    /**
     * Проверяет, был ли в предыдущие сессии работы приложения скачан файл
     * с описанием продуктов.
     *
     * @return <b>true</b> если файл был скачен, иначе <b>false</b>
     */
    public static boolean wasFileDownloaded() {
        return  getPrefs().getBoolean(PREFS_KEY_FILE_WAS_DOWNLOADED, false);
    }

    /**
     * Записывает состояние файла с описанием продкутов (скачен или нет)
     * @param  b <b>true</b> если файл был скачен. Для очистки информации о состоянии загрузку файла
     *           нужно передать <b>false</b>
     */
    public static void setFileWasDownloaded(boolean b) {
        getPrefs().edit().putBoolean(PREFS_KEY_FILE_WAS_DOWNLOADED, b).commit();
    }

    /**
     * Возвращает путь к файлу с описанием продуктов.
     * @return путь к файлу с описанием продуктов.
     */
    public static String getFilePath() {
        return  getPrefs().getString(PREFS_FILE_PATH, null);
    }

    /**
     * Записывает путь к файлу с описанием продуктов.
     * @param path путь к файлу с описанием продуктов.
     */
    public static void setFilePath(String path) {
        getPrefs().edit().putString(PREFS_FILE_PATH, path).commit();
    }

}
